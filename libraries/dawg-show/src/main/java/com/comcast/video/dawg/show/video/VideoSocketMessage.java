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
 * Message format that contains the type of message that is coming from the client as well
 * as the actual message
 * @author Kevin Pearson
 *
 */
public class VideoSocketMessage {
    public enum VideoSocketMessageType {
        CONNECT,
        SET_FPS,
        SET_RESOLUTION
    }

    private VideoSocketMessageType messageType;
    private String message;

    public VideoSocketMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(VideoSocketMessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
