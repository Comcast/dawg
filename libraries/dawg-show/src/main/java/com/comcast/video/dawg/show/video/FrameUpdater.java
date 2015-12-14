/**
 * Copyright 2010 Comcast Cable Communications Management, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.comcast.video.dawg.show.video;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.event.CatsEventType;
import com.comcast.cats.event.VideoEvent;
import com.comcast.cats.video.service.VideoController;
import com.comcast.cereal.CerealException;
import com.comcast.cereal.engines.JsonCerealEngine;
import com.comcast.video.dawg.cats.video.DawgEventDispatcher;
import com.comcast.video.dawg.cats.video.DawgVideoOutput;
import com.comcast.video.dawg.show.cache.MetaStbCache;

/**
 * Responsible for forwarding the frames to the client through a websocket
 * @author Kevin Pearson
 *
 */
public class FrameUpdater implements CatsEventHandler, OnTextMessage {

    private VideoController videoController;
    private Connection connection;
    private BufferedImage lastFrame = null;
    private JsonCerealEngine engine = new JsonCerealEngine();
    private MetaStbCache metaStbCache;
    private String deviceId;
    private boolean open = false;

    public static final long KEEP_VIDEO_STREAM_IDLE = TimeUnit.SECONDS.toMillis(30);
    public static final long RECONNECT_PERIOD = TimeUnit.SECONDS.toMillis(3);
    public static final long CONNECT_TIMEOUT = TimeUnit.SECONDS.toMillis(30);

    public FrameUpdater(MetaStbCache metaStbCache) {
        this.metaStbCache = metaStbCache;
    }

    public synchronized void connect() {
        try {
            videoController.connect();
            this.wait(CONNECT_TIMEOUT);

            if (lastFrame == null) {
                throw new Exception("Failed to connect to video");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called when a new video frame is received. This will first check if anyone has been consuming
     * the video frames in the last 30 seconds. If no one has, then the video stream will be disconnected
     */
    @Override
    public synchronized void catsEventPerformed(CatsEvent evt) {
        if (evt instanceof VideoEvent) {
            if (this.open) {
                VideoEvent ve = (VideoEvent) evt;
                BufferedImage img = ve.getImage();
                if (img != null) {
                    if (this.lastFrame == null) {
                        // tell the connect method that we have received the first frame so we are connected
                        this.notifyAll();
                    }
                    this.lastFrame = img;
                    ImageAndTime iat = new ImageAndTime(deviceId, img, System.currentTimeMillis());
                    try {
                        if (connection.isOpen()) {
                            connection.sendMessage(engine.writeToString(iat));
                        }
                    } catch (IOException e) {
                        // just ignore, probably caused by closing the connection at same time as sending message
                    } catch (CerealException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public VideoController getVideoController() {
        return videoController;
    }

    /**
     * Called when the socket connect is created
     */
    @Override
    public synchronized void onOpen(Connection connection) {
        this.connection = connection;
        this.open = true;
    }

    /**
     * Called when the socket is closed. This will disconnect this object from the video server stream
     */
    @Override
    public synchronized void onClose(int closeCode, String message) {
        this.open = false;
        videoController.disconnectFromAxisDevice();
    }

    /**
     * Message handler for the socket. Some of the events that can take place are:
     * CONNECT - connect to the video server
     * SET_FPS - sets the frames per second that the frames will be sent back to the client
     * SET_RESOLUTION - sets the resolution of the frames
     */
    @Override
    public synchronized void onMessage(String data) throws VideoMessageException {
        try {
            VideoSocketMessage msg = engine.readFromString(data, VideoSocketMessage.class);
            switch(msg.getMessageType()) {
                case CONNECT:
                    if (this.videoController == null) {
                        this.deviceId = msg.getMessage();
                        DawgVideoOutput dawgVideoOutput = new DawgVideoOutput(metaStbCache.getMetaStb(deviceId));
                        this.videoController = dawgVideoOutput.getVideoController();

                        DawgEventDispatcher dispatcher = dawgVideoOutput.getDispatcher();
                        this.videoController.setFrameRate(20);
                        dispatcher.addListener(this, CatsEventType.VIDEO);
                        connect();
                    }
                    break;
                case SET_FPS:
                    if (this.videoController != null) {
                        this.videoController.setFrameRate(Integer.parseInt(msg.getMessage()));
                    }
                    break;
                case SET_RESOLUTION:
                    if (this.videoController != null) {
                        String message = msg.getMessage();
                        if (message.contains(",")) {
                            String[] dim = message.split(",");
                            Dimension dimension = new Dimension(Integer.parseInt(dim[0].trim()), Integer.parseInt(dim[1].trim()));
                            this.videoController.setVideoDimension(dimension);
                        } else {
                            throw new VideoMessageException("Invalid message format for setting resolution, must be w,h");
                        }
                    }
                    break;
            }
        } catch (CerealException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new VideoMessageException(e);
        }
    }
}
