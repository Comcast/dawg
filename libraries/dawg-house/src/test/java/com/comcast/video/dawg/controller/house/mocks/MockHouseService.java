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
package com.comcast.video.dawg.controller.house.mocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.comcast.video.dawg.common.DawgDevice;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.service.house.HouseService;

/**
 * This mocks how the database will behave. This will just store all the stbs
 * added in a list
 * @author Kevin Pearson
 *
 */
public class MockHouseService implements HouseService {
    List<Map<String,Object>> stbs = new ArrayList<Map<String, Object>>();
    boolean readExc = false;
    boolean writeExc = false;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object>[] getStbsById(String... ids) {
        exc(true);
        List<Map<String,Object>> matched = new ArrayList<Map<String, Object>>();
        for (String id : ids) {
            for (Map<String,Object> stb : stbs) {
                if (id.equals(stb.get(MetaStb.ID))) {
                    matched.add(stb);
                    break;
                }
            }
        }
        return matched.toArray(new Map[matched.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object>[] getStbsByKey(String... keys) {
        exc(true);
        List<Map<String,Object>> matched = new ArrayList<Map<String, Object>>();
        for (String key : keys) {
            for (Map<String,Object> stb : stbs) {
                if (stb.toString().contains(key)) {
                    matched.add(stb);
                }
            }
        }
        return matched.toArray(new Map[matched.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object>[] getStbsByQuery(String query) {
        exc(true);
        return getStbsByKey(query.split(" "));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object>[] getAll() {
        exc(true);
        return stbs.toArray(new Map[stbs.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getById(String id) {
        exc(true);
        Map<String, Object>[] s = getStbsById(id);
        if ((s != null) && (s.length > 0)) {
            return s[0];
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteStbById(String... id) {
        exc(false);
        Map<String, Object>[] todelete = getStbsById(id);
        for (Map<String, Object> del : todelete) {
            stbs.remove(del);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object>[] getStbsByTag(String tag, String query) {
        exc(true);
        List<Map<String, Object>> matched = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> stb : stbs) {
            if (stb.get(MetaStb.TAGS).toString().contains(tag)) {
                matched.add(stb);
            }
        }
        return matched.toArray(new Map[matched.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void upsertStb(DawgDevice... stb) {
        exc(false);
        for (DawgDevice s : stb) {
            Map<String, Object> found = null;
            for (Map<String, Object> existing : stbs) {
                if (existing.get(MetaStb.ID).equals(s.getData().get(MetaStb.ID))) {
                    found = existing;
                    break;
                }
            }
            if (found != null) {
                found.putAll(s.getData());
            } else {
                stbs.add(s.getData());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replace(DawgDevice... stb) {
        exc(false);
        for (DawgDevice s : stb) {
            for (Iterator<Map<String, Object>> it = stbs.iterator(); it.hasNext(); ) {
                Map<String, Object> existing = it.next();
                if (existing.get(MetaStb.ID).equals(s.getData().get(MetaStb.ID))) {
                    it.remove();
                }
            }
            stbs.add(s.getData());
        }
    }

    private void exc(boolean read) {
        if ((read && readExc) || (!read && writeExc)) {
            throw new RuntimeException("Mocked database exception");
        }
    }

    public void setReadExc(boolean readExc) {
        this.readExc = readExc;
    }

    public void setWriteExc(boolean writeExc) {
        this.writeExc = writeExc;
    }
}
