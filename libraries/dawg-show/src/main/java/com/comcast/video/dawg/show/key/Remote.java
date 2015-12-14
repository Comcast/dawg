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

import java.util.HashSet;
import java.util.Set;

import com.comcast.video.stbio.Key;

/**
 * Holds information about a remote
 * @author Kevin Pearson
 *
 */
public class Remote {
    private String name;
    private KeySet keySet;
    private Set<String> remoteTypes = new HashSet<String>();

    /**
     * Constructor
     * @param name Then name of the remote
     */
    public Remote(String name) {
        this.setName(name);
    }

    public Set<Key> getKeys() {
        return keySet.getKeys();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageSubpath() {
        return name.toLowerCase();
    }

    public KeySet getKeySet() {
        return keySet;
    }

    public void setKeySet(KeySet keySet) {
        this.keySet = keySet;
    }

    public Set<String> getRemoteTypes() {
        return remoteTypes;
    }

    public void addRemoteType(String remoteType) {
        this.remoteTypes.add(remoteType);
    }
}

