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
package com.comcast.video.dawg.cats.serial;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.video.dawg.util.DawgUtil;
import com.comcast.video.stbio.Capture;
import com.comcast.video.stbio.SerialInput;
import com.comcast.video.stbio.SerialListener;
import com.comcast.video.stbio.SerialOutput;
import com.comcast.video.stbio.exceptions.SerialException;
import com.comcast.video.stbio.meta.SerialMeta;

/**
 * A Serial client for receiving and sending data through the serial port of an stb
 * @author Kevin Pearson
 *
 */
public class SerialClient implements SerialInput, SerialOutput, WebSocketListener {

    public static final Logger logger = LoggerFactory.getLogger(SerialClient.class);
    public static final int DEFAULT_PORT = 15080;
    public static final long DEFAULT_CONNECT_TIME = 30000;
    public static final long DEFAULT_DISCONNECT_TIME = 5000;
    private static final Pattern NON_ASCII_FILTER = Pattern.compile("[^\\p{Print}\\p{Space}]");
    private static final Pattern FULL_LINE_FILTER = Pattern.compile("(.*?)[\r\n]+");

    /** Maximum idle timeout of client connection in milli seconds. */
    private static final int CLIENT_MAX_IDLE_TIMEOUT = 120000;

    private String alias;
    private Session session = null;
    private WebSocketClient client;
    private Collection<SerialListener> listeners = new ArrayList<SerialListener>();
    private String buffer = "";
    private String wsUrl;

    /**
     * Creates a SerialClient
     * @param host The host of the SCAT server
     * @param macAddress The mac address of the device to connect to. This is used to identify
     * a device between the multiple devices that are served with the same SCAT server
     */
    public SerialClient(String host, String macAddress) {
        this(host, DEFAULT_PORT, macAddress);
    }

    /**
     * Creates a SerialClient
     * @param serialMeta Object that contains the metadata necessary to receive and send serial
     */
    public SerialClient(SerialMeta serialMeta) {
        this(serialMeta.getSerialHost(), DEFAULT_PORT, serialMeta.getMacAddress());
    }

    /**
     * Creates a SerialClient
     * @param host The host of the SCAT server
     * @param port The port of the SCAT server
     * @param macAddress The mac address of the device to connect to. This is used to identify
     * a device between the multiple devices that are served with the same SCAT server
     */
    public SerialClient(String host, int port, String macAddress) {
        this.alias = "SerialClient-" + macAddress;
        this.client = new WebSocketClient();
        String deviceId = DawgUtil.onlyAlphaNumerics(macAddress);
        this.wsUrl = "ws://" + host + ":" + port + "/scat/trace/" + deviceId.toUpperCase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void connect() throws SerialException {
        if (!isConnected()) {
            try {
                if (client.isStopping()  || client.isStopped()) {
                    client.start();
                }
                client.connect(this, new URI(wsUrl));
                this.wait(DEFAULT_CONNECT_TIME);
                if (this.session == null) {
                    client.stop();
                    throw new SerialException("Failed to connect to " + alias + " in " + (DEFAULT_CONNECT_TIME / 1000) + " seconds");
                }
                // Setting the idle timeout for the client connection.
                client.setMaxIdleTimeout(CLIENT_MAX_IDLE_TIMEOUT);
            } catch (Throwable t) {
                throw new SerialException("Failed to establish the web socket connection.", t);
            }
        } else {
            logger.warn(alias + " is already connected");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        return (client != null) && !client.isStopped() && (session != null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void disconnect() throws SerialException {
        try {
            if (isConnected()) {
                session.close();
                try {
                    this.wait(DEFAULT_DISCONNECT_TIME);
                    if (this.session != null) {
                        logger.warn("Failed to disconnect from " + alias + " in " + (DEFAULT_DISCONNECT_TIME / 1000) + " seconds");
                    }
                } catch (InterruptedException e) {
                }
            }
        } finally {
            if (client != null) {
                try {
                    client.stop();
                } catch (Exception e) {
                    logger.warn("Failed to stop WebSocketClient from " + alias,e );
                }
            }
            this.session = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Capture startCapture() {
        CaptureImpl capture = new CaptureImpl();
        addSerialListener(capture);

        return capture;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopCapture(Capture capture) {
        capture.stop();
        if (capture instanceof SerialListener) {
            removeSerialListener((SerialListener) capture);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSerialListener(SerialListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSerialListener(SerialListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAllSerialListeners() {
        synchronized (listeners) {
            listeners.clear();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeArtificialSerialOutput(byte[] data) {
        notifyListeners(new String(data), true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendToSerial(byte[] data) throws SerialException {
        if (isConnected()) {
            try {
                session.getRemote().sendBytes(ByteBuffer.wrap(Arrays.copyOf(data, data.length)));
            } catch (IOException e) {
                throw new SerialException(e);
            }
        } else {
            throw new SerialException("Cannot send serial to " + alias + " because it is not connected");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            int j = i + offset;
            if (j >= payload.length) {
                break;
            }
            data[i] = payload[j];
        }
        onWebSocketText(new String(data));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void onWebSocketClose(int statusCode, String reason) {
        String reasonClause = reason != null ? " due to : " + reason : "";
        logger.info(alias + " has closed its connection" + reasonClause + " with a status code: " + statusCode);
        this.session = null;
        this.notifyAll();
        // Any status code other than normal it will attempt for a connection.
        if ((StatusCode.NORMAL < statusCode) && (statusCode <= StatusCode.FAILED_TLS_HANDSHAKE)) {
            Thread reconnectionThread = new Thread(new SerialClientReconnectThread());
            reconnectionThread.setDaemon(true);
            reconnectionThread.start();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void onWebSocketConnect(Session session) {
        logger.info("Connection established to " + alias);
        this.session = session;
        try {
            sendToSerial(new byte[]{13, 13});
        } catch (SerialException e) {
            logger.error("Send to serial failed.",e);
        }
        this.notifyAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void onWebSocketError(Throwable cause) {
        logger.info("An error occurred with the connection to " + alias,cause);
        this.notifyAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void onWebSocketText(String message) {
        String cleanedUp = NON_ASCII_FILTER.matcher(message).replaceAll("");
        cleanedUp = buffer + cleanedUp;

        Matcher lines = FULL_LINE_FILTER.matcher(cleanedUp);
        int lastEnd = 0;
        while(lines.find()) {
            notifyListeners(lines.group(1) + "\n", false);
            lastEnd = lines.end();
        }
        if(lastEnd < cleanedUp.length()) {
            buffer = cleanedUp.substring(lastEnd);
        } else {
            buffer = "";
        }
    }

    private void notifyListeners(String msg, boolean artificial) {
        synchronized(listeners) {
            for (SerialListener listener : listeners) {
                listener.newLogMessage(msg, artificial);
            }
        }
    }

    /**
     * Thread to reconnect in case connection fail over to set-top trace at the time of run.
     */
    private class SerialClientReconnectThread implements Runnable {

        /** Maximum reconnection interval allowed in milli seconds. */
        private static final int MAX_RECONNECT_INTERVAL = 60000;

        /** Reconnect interval in milli second that need to be incremented. */
        private static final int INCREMENT_RECONNECT_INTERVAL = 5000;

        /**
         *
         * {@inheritDoc}
         */
        @Override
        public void run() {
            long timeToWait = 0;
            boolean isToReconnect = true;
            int attempt = 0;

            while(isToReconnect){
                try {
                    logger.info(String.format("Attempting for %s reconnect on %s.", ++attempt, alias));
                    connect();
                    isToReconnect = false;
                } catch (SerialException exc) {
                    logger.error(String.format("Failed to reconnect to %s on %s attempt.", alias, attempt),exc);

                    // Ensuring wait duration does not exceeds the maximum allowable reconnect interval.
                    if (MAX_RECONNECT_INTERVAL > timeToWait) {

                        // In case of reconnect failures a longer wait is followed to facilitate the connectivity on next attempt.
                        // For example in case of 5 second reconnect increment 1st reconnect failure it wait for 5 sec before connection attempt,
                        // on 2nd reconnect failure it wait for 10 second before next connection attempt,
                        // on 3rd failure it wait for 15 second etc. Its proceeded till the maximum allowable wait duration exceeds.
                        timeToWait = timeToWait + INCREMENT_RECONNECT_INTERVAL;

                        try {
                            Thread.sleep(timeToWait);
                        } catch (InterruptedException intExc) {
                            logger.error("Reconnection wait got interrupted. So quitting with no further connection attempt.", intExc);
                            // Exiting the attempt for reconnect.
                            isToReconnect = false;
                        }
                    } else {
                        logger.error("Failed to reconnect. So quitting from further reconnection.");
                        isToReconnect = false;
                    }
                }
            }
        }
    }
}
