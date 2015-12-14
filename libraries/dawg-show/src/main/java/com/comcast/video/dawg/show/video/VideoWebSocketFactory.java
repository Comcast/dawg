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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.video.dawg.show.cache.MetaStbCache;

/**
 * This creates the jetty server that will handle all the socket connects for serving up video.
 * @author Kevin Pearson
 *
 */
//@Component
public class VideoWebSocketFactory {
    public static final int PORT = 8081;
    public static final String CONTEXT_PATH = "/video";

    @Autowired
    private MetaStbCache metaStbCache;

    private Server server;

    /**
     * Starts up the jetty server to handle socket connections
     * @throws Exception
     */
    @PostConstruct
    public void start() throws Exception {
        server = new Server(PORT);
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
                FrameUpdater frameUpdater = new FrameUpdater(metaStbCache);
                return frameUpdater;
            }
        };
        ContextHandler context = new ContextHandler();
        context.setContextPath(CONTEXT_PATH);
        context.setHandler(wsHandler);

        HandlerCollection handlerList = new HandlerCollection();
        handlerList.setHandlers(new Handler[]{context});
        server.setHandler(handlerList);

        server.start();
    }

    @PreDestroy
    public void shutdown() {
        try {
            if (server != null) {
                server.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
