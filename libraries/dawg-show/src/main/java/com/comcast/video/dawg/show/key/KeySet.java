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

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.comcast.video.stbio.Key;

/**
 * Holds information about a set of keys
 * @author Kevin Pearson
 *
 */
public class KeySet {

    class SortByKeyName implements Comparator<Key> {
        @Override
        public int compare(Key o1, Key o2) {
            return o1.name().compareTo(o2.name());
        }

    }
    private String name;
    private Set<Key> keys = new TreeSet<Key>(new SortByKeyName());

    /**
     * Constructor
     * @param name Then name of the remote
     */
    public KeySet(String name) {
        this.setName(name);
    }

    public Set<Key> getKeys() {
        return keys;
    }

    public void addKey(Key key) {
        keys.add(key);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void union(KeySet keySet) {
        this.setName(name + "-" + keySet.getName());
        keys.addAll(keySet.getKeys());
    }
}

