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

/**
 * Exception thrown when an invalid message is sent from the client
 * @author Kevin Pearson
 *
 */
public class VideoMessageException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    public VideoMessageException(Throwable cause) {
        super(cause);
    }

    public VideoMessageException(String message) {
        super(message);
    }
}
