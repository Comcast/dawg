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
package com.comcast.video.dawg.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapScanner {

    private static volatile MapScanner instance;

    private MapScanner() {  }

    public static MapScanner instance() {
        if (null == instance) {
            synchronized(MapScanner.class) {
                if (instance == null) {
                    instance = new MapScanner();
                }
            }
        }
        return instance;
    }

    public String[] scan(Map<String,Object> map) {
        Set<String> keySet = new HashSet<String>();

        deepScan(map.keySet(), keySet);
        deepScan(map.values(), keySet);

        return clean(keySet);
    }

    private String[] clean(Set<String> keys) {
        Set<String> clean = new HashSet<String>();
        for (String string : keys) {
            clean.add(clean(string));
        }
        return clean.toArray(new String[clean.size()]);
    }

    private String clean(String s) {
        if (null != s)
            return s.trim().toLowerCase();
        else return s;
    }

    /**
     * Deep scan a collection for string values and add them to the output
     * @param in
     * @param out
     */
    @SuppressWarnings("rawtypes")
    private void deepScan(Iterable in, Collection<String> out) {
        for (Object o : in) {
            if (o instanceof Iterable) {
                deepScan((Iterable) o, out);
            } else if (o instanceof Map) {
                deepScan(((Map) o).keySet(), out);
                deepScan(((Map) o).values(), out);
            } else {
                if (o instanceof String) {
                    out.add((String) o);
                }
            }
        }
    }
}
