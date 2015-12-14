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

import com.comcast.video.stbio.exceptions.KeyException;
import com.comcast.video.stbio.meta.RemoteType;

/**
 * Interface for inputing a key in the stb system
 * @author Kevin Pearson
 *
 */
public interface KeyInput extends StbIO {

    /**
     * Press a single key
     * @param key The key to press
     * @throws KeyException
     */
    void pressKey(Key key) throws KeyException;

    /**
     * Press a key by overriding the type of the remote. This is particularly useful while sending IRKeys which are not present in some remote types.
     * The current approach would be 'IR' for sending keys via this method.
     *
     * @param key          The key to press
     * @param remoteType   The type of remote user requires
     * @throws KeyException when sending of IR key fail.
     */
    void pressKey(Key key, RemoteType remoteType) throws KeyException;

    /**
     * Press multiple keys
     * @param keys The keys to press
     * @throws KeyException
     */
    void pressKeys(Key... keys) throws KeyException;

    /**
     * Press multiple keys
     * @param delayInMillis the amount of milliseconds to wait before each key is sent
     * @param keys The keys to press
     * @throws KeyException
     */
    void pressKeys(long delayInMillis, Key... keys) throws KeyException;

    /**
     * Press multiple keys
     * @param validate true if the key input should be validated
     * @param keys The keys to press
     * @throws KeyException
     */
    void pressKeys(boolean validate, Key... keys) throws KeyException;

    /**
     * Hold a single key
     * @param key The key to hold
     * @param holdTime The time in milliseconds to hold the key
     * @throws KeyException
     */
    void holdKey(Key key, long holdTime) throws KeyException;
}
