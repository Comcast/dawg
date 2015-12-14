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
package com.comcast.video.dawg.show;

import java.util.ArrayList;
import java.util.List;

import com.comcast.cereal.engines.JsonCerealEngine;
import com.comcast.drivethru.exception.HttpException;
import com.comcast.drivethru.utils.Method;
import com.comcast.drivethru.utils.RestRequest;
import com.comcast.drivethru.utils.RestResponse;
import com.comcast.drivethru.utils.URL;
import com.comcast.video.dawg.DawgClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to control operations on dawg-show.
 *
 * @author Jijo Jose
 */
public class DawgShowClient extends DawgClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DawgShowClient.class);
    /* Path of controller in Dawg-show for updating the meta stb cache */
    private static final String CACHE_REFRESH_PATH = "stb/refreshCache";
    /* Path of controller in Dawg-show for returning the valid remote types */
    private static final String GET_REMOTE_TYPES_PATH = "remotetypes";
    /* Key of the query parameter */
    private static final String DEVICE_IDS_QUERY_KEY = "deviceIds";
    private JsonCerealEngine cerealEngine = new JsonCerealEngine();

    public DawgShowClient(String baseURL) {
        super(baseURL);
    }

    /**
     * Method to refresh the settop cache in dawg-show.
     *
     * @param ids
     *            Array of device IDs whose metadata is to be update in the
     *            cache
     * @throws HttpException
     *             if method failed to update DAWG show meta stb cache.
     */
    public void updateCache(String[] ids) throws HttpException {
        URL dawgShowUrl = new URL();
        dawgShowUrl.addPath(CACHE_REFRESH_PATH);
        for (String id : ids) {
            dawgShowUrl.addQuery(DEVICE_IDS_QUERY_KEY, id);
        }

        executeRequest(dawgShowUrl, Method.GET);
    }

    /**
     * Method to get the valid remote types from Dawg-show
     *
     * @return List<String> List of valid remote types
     */
    public List<String> getRemoteTypes() {
        return getByList(GET_REMOTE_TYPES_PATH);
    }

    @SuppressWarnings("unchecked")
    private List<String> getByList(String path) {
        RestRequest request = new RestRequest(path, Method.POST);
        List<String> list = new ArrayList<>();
        try {
            RestResponse response = client.execute(request);
            list = cerealEngine.readFromString(response.getBodyString(), List.class);
        } catch (Exception e) {
            LOGGER.error("Failed to get the response from Dawg-show ",e);
        }
        return list;
    }
}
