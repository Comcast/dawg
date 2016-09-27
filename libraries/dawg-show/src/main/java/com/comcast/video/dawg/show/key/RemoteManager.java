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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.comcast.video.stbio.Key;

/**
 * Responsible for loading and containing information about all of the remotes
 * @author Kevin Pearson
 *
 */
@Component
public class RemoteManager {
    public static final String DEFAULT_REMOTE_TYPE = "MOTOROLA";

    public static final String KEY_SETS_FILE = "keysets.txt";
    public static final String REMOTES_FILE = "remotes.txt";

    private Map<String, Remote> remotes;
    private Map<String, KeySet> keysets;

    /**
     * Called when the server is launched and loads remote information from a config file
     * @throws IOException
     */
    @PostConstruct
    public void load() throws IOException {
        loadKeySets();
        loadRemotes();
    }

    /**
     * Loads the remotes from remotes.txt
     * @throws IOException
     */
    public void loadRemotes() throws IOException {
        InputStream is = null;

        try {
            is = this.getClass().getResourceAsStream(REMOTES_FILE);
            loadRemotes(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Loads the keysets from keysets.txt
     * @throws IOException
     */
    public void loadKeySets() throws IOException {
        InputStream is = null;

        try {
            is = this.getClass().getResourceAsStream(KEY_SETS_FILE);
            loadKeySets(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Loads the remotes from an input stream. The structure of the file is:
     * REMOTE_NAME1|KEYSET1,KEYSET2|REMOTETYPE1,REMOTETYPE2
     * REMOTE_NAME2|KEYSET1|REMOTETYPE1
     *
     * The KEYSET are the names of {@link KeySet}. The remote keyset then becomes the union of all the
     * key sets it is defined to have
     * @param is The stream to load the data from
     * @throws IOException
     */
    public void loadRemotes(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        remotes = new HashMap<String, Remote>();
        if (keysets == null) {
            loadKeySets();
        }
        String line = reader.readLine();
        while (line != null) {
            // skip all lines that start with a '#'
            if (!line.trim().startsWith("#")) {

                String[] parts = line.split("\\|");
                if (parts.length != 3) {
                    throw new RuntimeException("Invalid remotes file, must be NAME|KEYSET1,KEYSET2|REMOTETYPE1,REMOTETYPE2");
                }
                String name = parts[0];
                String[] keySets = parts[1].split(",");
                String[] remoteTypes = parts[2].split(",");
                KeySet keySet = new KeySet("");
                for (String ks : keySets) {
                    KeySet newKs = keysets.get(ks);
                    if (newKs == null) {
                        throw new RuntimeException("No keyset named : " + ks);
                    }
                    keySet.union(newKs);
                }
                Remote remote = new Remote(name);
                remote.setKeySet(keySet);
                for (String type : remoteTypes) {
                    remote.addRemoteType(type);
                }
                remotes.put(name, remote);
            }
            line = reader.readLine();
        }
    }

    /**
     * Loads the key sets from an input stream. The structure of the file is:
     * KEY_SET_NAME:
     * KEY1
     * KEY2
     *
     * KEY_SET_NAME2:
     * KEY1
     * KEY2
     *
     * The KEY is the name of {@link Key}
     * @param is The stream to load the data from
     * @throws IOException
     */
    public void loadKeySets(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        keysets = new HashMap<String, KeySet>();
        KeySet keySet = null;
        String line = reader.readLine();
        while (line != null) {
            if (line.contains(":")) {
                String name = line.replace(":", "");
                keySet = new KeySet(name);
                keysets.put(name, keySet);
            } else if (!line.trim().isEmpty()) {
                keySet.addKey(Key.valueOf(line));
            }
            line = reader.readLine();
        }
    }

    /**
     * Gets the remote object for the given remote name
     * @param remoteType
     * @return
     */
    public Remote getRemote(String remoteType) {
        for (Remote remote : remotes.values()) {
            if (remote.getRemoteTypes().contains(remoteType)) {
                return remote;
            }
        }
        return null;
    }

    /**
     * Gets all the remotes in this manager
     * @return
     */
    public Collection<Remote> getRemotes() {
        return remotes.values();
    }

    /**
     * Returns the list of all remote types present in this manager.
     */
    public Set<String> getRemoteTypes() {
        Set <String> remoteTypes = new HashSet<String>();
        for (Remote remote : remotes.values()) {
            for (String remoteType : remote.getRemoteTypes()) {
                remoteTypes.add(remoteType);
            }
        }
        return remoteTypes;
    }

    /**
     * Returns the key set of the given remote type.
     *
     * @param remoteType
     *            type of remote.
     * @return a set that contains all keys present in the given remote.
     */
    public Set<String> getKeySet(String remoteType) {
        Remote remote = getRemote(remoteType);
        Set<Key> remoteKeys = (null != remote) ? remote.getKeys() : remotes.get(DEFAULT_REMOTE_TYPE).getKeys();
        Set<String> remoteKeyList = new HashSet<String>();
        for (Key key : remoteKeys) {
            remoteKeyList.add(key.getKey());
        }
        return remoteKeyList;
    }

    public void addRemoteTypeToRemote(String remoteId, String remoteType) {
        Remote remote = this.remotes.get(remoteId);
        if (remote != null) {
            remote.addRemoteType(remoteType);
        }
    }
}
