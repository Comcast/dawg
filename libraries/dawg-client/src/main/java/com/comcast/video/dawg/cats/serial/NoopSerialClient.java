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

import com.comcast.video.stbio.Capture;
import com.comcast.video.stbio.SerialInput;
import com.comcast.video.stbio.SerialListener;
import com.comcast.video.stbio.SerialOutput;
import com.comcast.video.stbio.exceptions.SerialException;

/**
 * Class for handling settops where serial support is not available. This will help HAWT in execution in production boxes.
 *
 * @author Prasad Menon
 *
 */

public class NoopSerialClient implements SerialOutput, SerialInput {

    @Override
    public void connect() throws SerialException {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void disconnect() throws SerialException {

    }

    @Override
    public void sendToSerial(byte[] data) throws SerialException {

    }

    @Override
    public Capture startCapture() {
        return null;
    }

    @Override
    public void stopCapture(Capture capture) {

    }

    @Override
    public void addSerialListener(SerialListener listener) {

    }

    @Override
    public void removeSerialListener(SerialListener listener) {

    }

    @Override
    public void removeAllSerialListeners() {

    }

    @Override
    public void writeArtificialSerialOutput(byte[] data) {

    }
}
