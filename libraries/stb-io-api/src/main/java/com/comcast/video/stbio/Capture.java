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
package com.comcast.video.stbio;

import java.util.List;

/**
 * Captures serial output and provides methods to search that output
 * @author Kevin Pearson
 *
 */
public interface Capture {
    /**
     * Finds serial output that matches the given regular expression from
     * the already captured messages
     * @param regex The regular expression to match
     * @return The matched string
     */
    String find(String regex);

    /**
     * Same as {@link Capture#find(String)} but it will wait indefinitely until there is a match
     * @param regex The regular expression to match
     * @return The matched string
     */
    String waitFor(String regex);

    /**
     * Same as {@link Capture#find(String)} but it will wait until the given timeout is reached or there is a match
     * @param regex The regular expression to match
     * @param timeout The time in milliseconds to wait
     * @return The matched string
     */
    String waitFor(String regex, long timeout);

    /**
     * Stops any further capture
     */
    void stop();

    /**
     * Rewind the index to the beginning of the capture buffer
     */
    void rewind();

    /**
     * Gets all the serial output that was captured
     * @return
     */
    List<String> getCaptured();
}
