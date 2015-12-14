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

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.event.CatsEventType;
import com.comcast.cats.event.TraceEvent;
import com.comcast.cats.event.VideoEvent;

public class DawgEventDispatcherTest {
    class MockCatsEventHandler implements CatsEventHandler {
        private int performed = 0;

        @Override
        public void catsEventPerformed(CatsEvent evt) {
            performed++;
        }

        public int getPerformed() {
            return performed;
        }

        public boolean anyPerformed() {
            return performed > 0;
        }

        public void reset() {
            performed = 0;
        }
    }

    @Test
    public void testAddListener() {
        MockCatsEventHandler global = new MockCatsEventHandler();
        MockCatsEventHandler sourced = new MockCatsEventHandler();
        MockCatsEventHandler sourced2 = new MockCatsEventHandler();
        Object source = new Object();

        DawgEventDispatcher dispatch = new DawgEventDispatcher();
        dispatch.addListener(global, Arrays.asList(CatsEventType.VIDEO));
        dispatch.addListener(sourced, CatsEventType.VIDEO, source);
        dispatch.addListener(sourced2, CatsEventType.TRACE, source);

        CatsEvent event = new VideoEvent(1, "sourceId", source);
        dispatch.sendCatsEvent(event);

        testGotEvent(new MockCatsEventHandler[] { global, sourced }, new MockCatsEventHandler[] { sourced2 });

        CatsEvent tEvent = new TraceEvent(1,"sourceId", null);
        dispatch.sendCatsEvent(tEvent);

        testGotEvent(new MockCatsEventHandler[] { }, new MockCatsEventHandler[] { global, sourced, sourced2 });
    }

    private void testGotEvent(MockCatsEventHandler[] did, MockCatsEventHandler[] didNot) {
        for (MockCatsEventHandler e : did) {
            Assert.assertTrue(e.anyPerformed());
            e.reset();
        }
        for (MockCatsEventHandler e : didNot) {
            Assert.assertFalse(e.anyPerformed());
            e.reset();
        }
    }

    @Test
    public void testRemoveListener() {
        MockCatsEventHandler global = new MockCatsEventHandler();
        MockCatsEventHandler sourced = new MockCatsEventHandler();
        Object source = new Object();

        DawgEventDispatcher dispatch = new DawgEventDispatcher();
        dispatch.addListener(global, Arrays.asList(CatsEventType.VIDEO));
        dispatch.addListener(sourced, CatsEventType.VIDEO, source);
        CatsEvent event = new VideoEvent(1, "sourceId", source);
        dispatch.sendCatsEvent(event);

        testGotEvent(new MockCatsEventHandler[] { global, sourced }, new MockCatsEventHandler[] { });

        dispatch.removeListener(global);
        dispatch.removeListener(sourced);

        testGotEvent(new MockCatsEventHandler[] { }, new MockCatsEventHandler[] { global, sourced });
    }
}
