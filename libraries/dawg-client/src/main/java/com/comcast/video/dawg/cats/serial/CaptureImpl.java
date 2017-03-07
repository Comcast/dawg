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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.pantry.io.CircularQueue;
import com.comcast.video.stbio.Capture;
import com.comcast.video.stbio.SerialListener;

/**
 * Implementation for a Capture interface. This will record all the serial output that comes
 * in from {@link CaptureImpl#newLogMessage(String)}
 * @author Kevin Pearson
 *
 */
public class CaptureImpl implements SerialListener, Capture {
    private static Logger logger = LoggerFactory.getLogger(CaptureImpl.class);

    // The 500 previous captured strings should be more than enough to search through
    public static final int MAX_CAPTURED = 500;
    private CircularQueue captured = new CircularQueue(MAX_CAPTURED);
    private CircularQueue capturedFromTrace = new CircularQueue(MAX_CAPTURED);

    private boolean stopped = false;
    private int currentIndex = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public void newLogMessage(String log) {
        newLogMessage(log, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newLogMessage(String log, boolean artificial) {
        if(!stopped) {
            synchronized(captured) {
                captured.add(log);
                if (!artificial) {
                    capturedFromTrace.add(log);
                }
                captured.notifyAll();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        stopped = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rewind() {
        currentIndex = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String find(String regex) {
        Pattern pattern = Pattern.compile(regex);
        int startDataIndex = captured.getDataStartIndex();
        int endDataIndex = captured.getDataEndIndex();

        if(currentIndex < startDataIndex) {
            currentIndex = startDataIndex;
        }

        for(; currentIndex <= endDataIndex ; currentIndex++) {
            if(pattern.matcher(captured.get(currentIndex)).find()) {
                return captured.get(currentIndex++);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String waitFor(String regex, long timeout) {
        String found = find(regex);
        if(null != found) {
            return found;
        }
        logger.debug("Waiting for '" + regex + "' for " + timeout);
        synchronized(captured) {
            long startTime = System.currentTimeMillis();
            while(null == found) {
                found = find(regex);
                if(null == found) {
                    try {
                        if (timeout < 0) {
                            captured.wait();
                        } else {
                            captured.wait(timeout);
                        }
                    } catch (InterruptedException iex) {
                        /*Deal with the confusing concept of thread interruption
                         * If we're interrupted we want to stop, and we want to be able to notify
                         * whomever called this method that we were interrupted. But we don't want to
                         * re-throw this exception since most callers wont care.  Instead re-interrupt the thread
                         * so its interrupt flag gets marked and we can check against that.
                         */
                        Thread.currentThread().interrupt();
                        return null;
                    }
                    if (timeout >= 0) {
                        long timePassed = System.currentTimeMillis() - startTime;
                        if (timePassed > timeout) {
                            break;
                        }
                    }
                }
            }
        }
        return found;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String waitFor(String regex) {
        return waitFor(regex, -1);
    }

    /**
     *
     * @return an Collection of strings for the lines captured in this Capture.
     */
    @Override
    public List<String> getCaptured() {
        List<String> rv = new ArrayList<String>();
        int startDataIndex = capturedFromTrace.getDataStartIndex();
        int endDataIndex = capturedFromTrace.getDataEndIndex();

        for(int i=startDataIndex; i<=endDataIndex; i++) {
            rv.add(capturedFromTrace.get(i));
        }

        return rv;
    }
}
