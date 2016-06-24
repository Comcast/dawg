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
package com.comcast.video.dawg.cats.video;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URI;

import com.comcast.video.stbio.VideoOutput;
import com.comcast.video.stbio.meta.VideoMeta;

/**
 * Object that provides a way to capture an image from video
 * @author Kevin Pearson
 *
 */
public class DawgVideoOutput implements VideoOutput {

    /**
     * Creates a DawgVideoOutput
     * @param videoMeta The source of information necessary for accessing video
     */
    public DawgVideoOutput(VideoMeta videoMeta) {
        this(videoMeta.getVideoSourceUrl(), videoMeta.getVideoCamera());
    }

    /**
     * Creates a DawgVideoOutput
     * @param videoSourceUrl The video service host
     * @param videoCamera The camera to use at that service host
     */
    public DawgVideoOutput(String videoSourceUrl, String videoCamera) {
        try {
            URI videoUri = new URI(generateVideoUrl(videoSourceUrl, videoCamera));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage captureScreen() {
        /* TODO: Add video to image capture */
        return null;
    }

    /**
     * Generes the video url for the host and camera
     * @param videoHost The host of the video service
     * @param camera The camera to use at that service host
     * @return
     */
    protected String generateVideoUrl(String videoHost, String camera) {
        String cam = camera == null ? "1" : camera;
        if (    null != videoHost
             && !(    videoHost.startsWith("http://")
                   || videoHost.startsWith("https://"))) {
            videoHost = "http://"+videoHost;
        }
        return videoHost + "/axis-cgi/mjpg/video.cgi?camera=" + cam + "&resolution=4CIF&fps=10";
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setDimension(Dimension dm) {
        /* TODO: When image capture is implemented, this will likely need to be added */
    }
}
