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
package com.comcast.video.dawg.house;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.comcast.cereal.CerealException;
import com.comcast.cereal.engines.JsonCerealEngine;
import com.comcast.drivethru.exception.HttpException;
import com.comcast.drivethru.utils.Method;
import com.comcast.drivethru.utils.RestRequest;
import com.comcast.drivethru.utils.RestResponse;
import com.comcast.drivethru.utils.URL;

import com.comcast.video.dawg.DawgClient;
import com.comcast.video.dawg.common.Config;
import com.comcast.video.dawg.common.DawgModel;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.exception.HttpRuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DawgHouseClient extends DawgClient implements IDawgHouseClient<MetaStb> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DawgHouseClient.class);
    private static final String DEFAULT_BASE_URL = Config.get("dawg", "dawg-house-url", "http://localhost/dawg-house");
    private JsonCerealEngine cerealEngine = new JsonCerealEngine();

    public DawgHouseClient() {
        this(DEFAULT_BASE_URL);
    }

    public DawgHouseClient(String baseURL) {
        super(baseURL);
    }

    @SuppressWarnings("unchecked")
    private MetaStb httpget(String path) {
        try {
            return new MetaStb(client.get(path, Map.class));
        } catch (HttpException e) {
            throw new HttpRuntimeException(e);
        }
    }

    private void httpput(String path, MetaStb stb) {
        try {
            client.put(path, stb.getData());
        } catch (HttpException e) {
            throw new HttpRuntimeException(e);
        }
    }

    private void httpdelete(String path) {
        try {
            client.delete(path);
        } catch (HttpException e) {
            throw new HttpRuntimeException(e);
        }
    }

    @Override
    public MetaStb getById(String id) {
        return httpget("/devices/id/"+id);
    }

    @Override
    public void add(MetaStb stb) {
        httpput("/devices/id/"+stb.getId(), stb);
    }

    @Override
    public void delete(String id) {
        httpdelete("/devices/id/" + id);
    }

    @Override
    public Collection<MetaStb> getByIds(Collection<String> ids) {
        return getByCollection("/devices/id", ids, "id");
    }

    @Override
    public Collection<MetaStb> getByQuery(String query) {
        return getByCollection("/devices//query", Arrays.asList(query), "q");
    }

    private Collection<MetaStb> getByCollection(String path, Collection<String> values, String key) {
        RestRequest request = new RestRequest(path, Method.POST);
        if (null != values && values.size() > 0 ) {
            for(String value : values) {
                if (null != value) {
                    request.addQuery(key, value);
                }
            }
        }
        try {
            RestResponse response = client.execute(request);
            @SuppressWarnings("unchecked")
            Map<String, Object>[] maps = new JsonCerealEngine().readFromString(response.getBodyString(), Map[].class);
            List<MetaStb> list = new ArrayList<>();
            for(Map<String, Object> map : maps) {
                list.add(new MetaStb(map));
            }
            return list;
        } catch (Exception e) {
            throw new HttpRuntimeException(e);
        }
    }

    @Override
    public void deleteByIds(Collection<String> ids) {
        post("/devices/delete",ids,"id");
    }

    @Override
    public void delete(MetaStb stb) {
        delete(stb.getId());
    }

    @Override
    public void delete(Collection<MetaStb> stbs) {
        Set<String> ids = new HashSet<>();
        for(MetaStb s : stbs) {
            if (null != s.getId()) {
                ids.add(s.getId());
            }
        }
        deleteByIds(ids);
    }

    @Override
    public void add(Collection<MetaStb> stbs) {
        if (null != stbs) {
            for (MetaStb metaStb : stbs) {
                add(metaStb);
            }
        }
    }

    @Override
    public void update(MetaStb stb) {
        update(Arrays.asList(stb.getId()), stb.getData());
    }

    @Override
    public void update(Collection<String> id, Map<String, Object> data) {
        String body;
        try {
            body = cerealEngine.writeToString(data);
        } catch (CerealException e) {
            throw new HttpRuntimeException(e);
        }
        post("/devices/update", id, "id", body);
    }

    private void post(String path, Collection<String> values, String key) {
        post(path, values, key, null);
    }

    private void post(String path, Collection<String> values, String key, String body) {
        RestRequest request = new RestRequest(path, Method.POST);
        request.setContentType("application/json");
        if (null != values) {
            for(String v : values) {
                request.addQuery(key, v);
            }
        }
        if (body != null) {
            request.setBody(body);
        }
        try {
            RestResponse response = client.execute(request);
            int status = response.getStatusCode();
            if ((status/100) != 2) {
                throw new HttpRuntimeException("Request returned a " + status + " response.");
            }
        } catch (HttpException e) {
            throw new HttpRuntimeException(e);
        }

    }

    @Override
    public Collection<MetaStb> getAll() {
        Collection<MetaStb> stbs = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object>[] maps = client.get("/devices/list/", Map[].class);
            for(Map<String,Object> map : maps) {
                stbs.add(new MetaStb(map));
            }
        } catch (HttpException e) {
            e.printStackTrace();
        }

        return stbs;
    }

    public DawgModel[] getDawgModels(String... modelNames) {
        URL url = new URL(client.getDefaultBaseUrl(), "models");
        for (String modelName : modelNames) {
            url.addQuery("id", modelName);
        }
        try {
            return client.get(url, DawgModel[].class);
        } catch (HttpException e) {
            e.printStackTrace();
        }
        return null;
    }
}
