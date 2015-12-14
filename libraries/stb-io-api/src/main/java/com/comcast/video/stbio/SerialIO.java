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

import com.comcast.video.stbio.exceptions.SerialException;

/**
 * Interface for communicating through the serial port of an stb
 * @author Kevin Pearson
 *
 */
public interface SerialIO extends StbIO {

    /**
     * Connects through the serial port
     * @throws SerialException
     */
    void connect() throws SerialException;

    /**
     * @return true if connected to the serial port, false otherwise.
     * @throws SerialException
     */
    boolean isConnected();

    /**
     * Disconnects from the serial port
     * @throws SerialException
     */
    void disconnect() throws SerialException;
}
