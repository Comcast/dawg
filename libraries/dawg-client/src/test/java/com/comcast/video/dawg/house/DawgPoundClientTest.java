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

import java.util.HashMap;
import java.util.Map;

import com.comcast.video.dawg.common.Config;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.drivethru.RestClient;
import com.comcast.drivethru.exception.HttpException;
import com.comcast.video.dawg.exception.HttpRuntimeException;

/**
 * The TestNG Test class for DawgPoundClient
 * @author TATA
 *
 */
public class DawgPoundClientTest {
    private static final String DEFAULT_BASE_URL = "http://localhost/dawg-pound/reservations";

    @SuppressWarnings("unchecked")
    @Test
    public void testConstructor() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);
        DawgPoundClient client = new DawgPoundClient(DEFAULT_BASE_URL);
        client.setClient(mockClient);
        Map[] maps = new HashMap[1];
        maps[0] = new HashMap<>();
        EasyMock.expect(mockClient.get((String) EasyMock.anyObject(),(Class<Map[]>)EasyMock.anyObject())).andReturn(maps);
        EasyMock.replay(mockClient);
        Assert.assertNotNull(client);
    }

    @SuppressWarnings("unchecked")
    @Test(expectedExceptions=HttpRuntimeException.class)
    public void testGetReservedDevicesWithEmptyToken() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);
        DawgPoundClient client = new DawgPoundClient();
        client.setClient(mockClient);
        Map[] maps = new HashMap[1];
        maps[0] = new HashMap<>();
        EasyMock.expect(mockClient.get((String) EasyMock.anyObject(),(Class<Map[]>)EasyMock.anyObject())).andThrow(new HttpRuntimeException() );
        EasyMock.replay(mockClient);
        String token="";
        client.getReservedDevices(token);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testGetReservedDevices() throws HttpException {
        RestClient restClient = EasyMock.createMock(RestClient.class);
        Map<String,Object>[] maps = new HashMap[1];
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("key", new Object());
        maps[0] = map;
        EasyMock.expect(restClient.get((String)EasyMock.anyObject(), (Class) EasyMock.anyObject())).andReturn(maps);
        DawgPoundClient client = new DawgPoundClient();
        client.setClient(restClient);
        EasyMock.replay(restClient);

        String token="abc";

        Assert.assertNotNull(   client.getReservedDevices(token));
    }
}
