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
package com.comcast.video.dawg.show.key;


import org.apache.commons.codec.binary.Base64;

import com.comcast.video.dawg.cats.ir.IrClient;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.show.cache.MetaStbCache;
import com.comcast.video.dawg.show.plugins.RemotePluginManager;
import com.comcast.video.stbio.Key;
import com.comcast.video.stbio.KeyInput;
import com.comcast.video.stbio.exceptions.KeyException;

/**
 * Thread that sends a key to a device. This is so we can send keys to multiple devices in parallel
 * @author Kevin Pearson
 *
 */
public class SendKeyThread extends Thread {
    private String deviceId;
    private Key[] keys;
    private String holdTime;
    private MetaStbCache metaStbCache;
    private RemotePluginManager remotePluginManager;
    private KeyException exc;
    private String remoteType;
    private String jwt;

    public SendKeyThread(String deviceId, Key[] keys, String holdTime, MetaStbCache metaStbCache, RemotePluginManager remotePluginManager) {
        this.deviceId = deviceId;
        this.keys = keys;
        this.holdTime = holdTime;
        this.metaStbCache = metaStbCache;
        this.remotePluginManager = remotePluginManager;

    }

    public SendKeyThread(String deviceId, Key[] keys, String holdTime, MetaStbCache metaStbCache, RemotePluginManager remotePluginManager, String remoteType) {
        this(deviceId, keys, holdTime, metaStbCache, remotePluginManager, remoteType, null);
    }

    public SendKeyThread(String deviceId, Key[] keys, String holdTime, MetaStbCache metaStbCache, RemotePluginManager remotePluginManager, String remoteType, String jwt) {
        this(deviceId, keys, holdTime, metaStbCache, remotePluginManager);
        this.remoteType = remoteType;
        this.jwt = jwt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        MetaStb stb = metaStbCache.getMetaStb(deviceId);
        KeyInput irClient = getIrClient(stb, remoteType);
        try {
            if (holdTime != null) {
                irClient.holdKey(keys[0], Long.valueOf(holdTime));
            } else {
                irClient.pressKeys(keys);
            }
        } catch (KeyException e) {
            this.exc = e;
        }
    }

    protected KeyInput getIrClient(MetaStb stb, String remoteType) {
        KeyInput inp = remotePluginManager.getKeyInput(stb, remoteType);
        if ((inp instanceof IrClient) && (this.jwt != null)) {
            String b64 = Base64.encodeBase64String(this.jwt.getBytes());
            ((IrClient) inp).getClient().addDefaultHeader("Authorization", "Bearer " + b64);
        }
        
        return inp;
    }

    /**
     * Returns the exception that happened when sending the key, or null if there was
     * no exception
     * @return
     */
    public KeyException getExc() {
        return exc;
    }

    public String getDeviceId() {
        return deviceId;
    }

}
