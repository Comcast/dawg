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
package com.comcast.video.stbio.exceptions;

/**
 * An exception thrown when controlling power fails
 * @author Kevin Pearson
 *
 */
public class SoftPowerException extends StbIOException {

    private static final long serialVersionUID = 1L;

    public SoftPowerException(String msg) {
        super(msg);
    }

    public SoftPowerException(Throwable throwable) {
        super(throwable);
    }

    public SoftPowerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
