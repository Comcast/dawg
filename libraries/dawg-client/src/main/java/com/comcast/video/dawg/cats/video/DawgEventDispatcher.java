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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.event.CatsEventType;

public class DawgEventDispatcher implements CatsEventDispatcher {

    public static final Object DEFAULT_SOURCE = new Object();

    /** Source => (EventType => List of handler) */
    private Map<Object,Map<CatsEventType, Collection<CatsEventHandler>>> handlers = new HashMap<Object, Map<CatsEventType, Collection<CatsEventHandler>>>();
    private Map<CatsEventType, Collection<CatsEventHandler>> allSourceHandlers = new HashMap<CatsEventType, Collection<CatsEventHandler>>();

    private Collection<CatsEventHandler> getHandlerList(CatsEventType eventType, Object source) {
        Map<CatsEventType, Collection<CatsEventHandler>> handlerMap = handlers.get(source);
        if (handlerMap == null) {
            handlerMap = new HashMap<CatsEventType, Collection<CatsEventHandler>>();
            handlers.put(source, handlerMap);
        }
        return getOrCreateHandlerList(handlerMap, eventType);
    }

    private Collection<CatsEventHandler> getOrCreateHandlerList(
            Map<CatsEventType, Collection<CatsEventHandler>> handlerMap, CatsEventType eventType) {
    Collection<CatsEventHandler> eventHandlers = handlerMap.get(eventType);
    if (eventHandlers == null) {
        eventHandlers = new ArrayList<CatsEventHandler>();
        handlerMap.put(eventType, eventHandlers);
    }
    return eventHandlers;
}

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(CatsEventHandler handler, CatsEventType eventType) {
        this.addListener(handler, eventType, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(CatsEventHandler handler, List<CatsEventType> eventTypes) {
        for (CatsEventType eventType : eventTypes) {
            addListener(handler, eventType);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(CatsEventHandler handler, CatsEventType eventType, Object source) {
        Collection<CatsEventHandler> eventHandlers;
        if (source != null) {
            eventHandlers = getHandlerList(eventType, source);
        } else {
            eventHandlers = getOrCreateHandlerList(allSourceHandlers, eventType);
        }
        eventHandlers.add(handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(CatsEventHandler handler) {
        for (Map<CatsEventType, Collection<CatsEventHandler>> maps : handlers.values()) {
            for (Collection<CatsEventHandler> handlers : maps.values()) {
                handlers.remove(handler);
            }
        }
        for (Collection<CatsEventHandler> handlers : allSourceHandlers.values()) {
            handlers.remove(handler);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCatsEvent(CatsEvent event) throws IllegalArgumentException {
        Object source = event.getSource();
        source = source == null ? DEFAULT_SOURCE : source;
        Collection<CatsEventHandler> handlerList = new ArrayList<CatsEventHandler>(getHandlerList(event.getType(), source));
        Collection<CatsEventHandler> global = allSourceHandlers.get(event.getType());
        if (global != null) {
            handlerList.addAll(global);
        }
        for (CatsEventHandler handler : handlerList) {
            handler.catsEventPerformed(event);
        }
    }
}
