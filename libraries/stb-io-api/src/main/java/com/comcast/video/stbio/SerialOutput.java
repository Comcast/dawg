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


/**
 * Interface for handling output from the serial port of an stb
 * @author Kevin Pearson
 *
 */
public interface SerialOutput extends SerialIO {

    /**
     * Starts a capture that will record the output from serial
     * @return a valid {@link Capture} object if trace is available. Null, otherwise
     */
    Capture startCapture();

    /**
     * Stops the given capture from recording output from serial
     * @param capture The capture to stop
     */
    void stopCapture(Capture capture);

    /**
     * Adds a listener to this serial output
     * @param listener The listener to add
     */
    void addSerialListener(SerialListener listener);

    /**
     * removes a listener to this serial output
     * @param listener The listener to add
     */
    void removeSerialListener(SerialListener listener);

    /**
     * Removes all the listeners
     */
    void removeAllSerialListeners();

    /**
     * Manually writes data to into this object so that the listeners can see it
     * @param data The data to write
     */
    void writeArtificialSerialOutput(byte[] data);
}
