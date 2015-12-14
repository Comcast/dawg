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
import java.util.HashMap;
import java.util.Map;

import org.apache.http.ProtocolVersion;
import org.apache.http.message.BasicStatusLine;

import org.easymock.EasyMock;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.cereal.CerealException;
import com.comcast.cereal.engines.JsonCerealEngine;

import com.comcast.drivethru.RestClient;
import com.comcast.drivethru.exception.HttpException;
import com.comcast.drivethru.test.MockRestClient;
import com.comcast.drivethru.utils.RestRequest;
import com.comcast.drivethru.utils.RestResponse;

import com.comcast.video.dawg.common.Config;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.exception.HttpRuntimeException;

/**
 * The TestNG Test class for DawgHouseClient
 * @author TATA
 *
 */
public class DawgHouseClientTest {

    private static final String URL = Config.get("testing", "default-dawg-house", "http://localhost:8080/dawg-house");

    @SuppressWarnings("unchecked")
    @Test
    public void testConstructor() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);
        DawgHouseClient client = new DawgHouseClient(URL);
        client.setClient(mockClient);
        Map[] maps = new HashMap[1];
        maps[0] = new HashMap<>();
        EasyMock.expect(mockClient.get((String) EasyMock.anyObject(),(Class<Map[]>)EasyMock.anyObject())).andReturn(maps);
        EasyMock.replay(mockClient);
        Assert.assertNotNull(client.getAll());
    }

    @Test(expectedExceptions=HttpRuntimeException.class)
    public void testGetByIdWithEmptyId() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);

        EasyMock.expect(mockClient.get((String) EasyMock.anyObject(),(Class<Map>)EasyMock.anyObject())).andThrow(new HttpRuntimeException());
        EasyMock.replay(mockClient);

        String id = "";
        client.getById(id );
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testGetByIdWithValidId() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);

        Map map = new HashMap();
        EasyMock.expect(mockClient.get((String) EasyMock.anyObject(),(Class<Map>)EasyMock.anyObject())).andReturn(map);
        EasyMock.replay(mockClient);

        String id = "770000004A12";
        Assert.assertNotNull(client.getById(id ));
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void testAddWithNullStb() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);

        EasyMock.expect(mockClient.put((String) EasyMock.anyObject(),(Map)EasyMock.anyObject())).andReturn(true);
        EasyMock.replay(mockClient);

        MetaStb stb = null;

        client.add(stb);

    }

    @Test
    public void testAddWithStb() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);


        EasyMock.expect(mockClient.put((String) EasyMock.anyObject(),(Map)EasyMock.anyObject())).andReturn(true);
        EasyMock.replay(mockClient);


        String id = "770000004A12";
        MetaStb stb = EasyMock.createMock(MetaStb.class);
        EasyMock.expect(stb.getId()).andReturn(id);
        EasyMock.expect(stb.getData()).andReturn(new HashMap<String,Object>());
        EasyMock.replay(stb);
        client.add(stb);
    }

    @Test
    public void testDeleteWithEmptyId() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);


        EasyMock.expect(mockClient.delete((String) EasyMock.anyObject())).andReturn(true);
        EasyMock.replay(mockClient);

        String id = "";
        try {
            client.delete(id);
        } catch(Exception e) {
            Assert.fail("delete  threw " + e);
        }
    }

    @Test(expectedExceptions=HttpRuntimeException.class)
    public void testDeleteWithId() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);


        EasyMock.expect(mockClient.delete((String) EasyMock.anyObject())).andThrow(new HttpRuntimeException());
        EasyMock.replay(mockClient);

        String id = "123";

        client.delete(id);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testGetByIds() throws HttpException, CerealException {
        MockRestClient mockClient = new MockRestClient(URL);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);
        JsonCerealEngine engine = new JsonCerealEngine();
        String id = "770000004A12";

        Map[] returnDevices = new Map[1];
        MetaStb stb = new MetaStb();
        stb.setId(id);
        returnDevices[0] = stb.getData();
        mockClient.expect().andReturn(200).withBody(engine.writeToString(returnDevices), "application/json");

        Collection<MetaStb> stbs = client.getByIds(Arrays.asList(id));
        Assert.assertNotNull(stbs);
        Assert.assertEquals(stbs.size(), 1);
        Assert.assertEquals(stbs.iterator().next().getId(), id);
    }

    @Test(expectedExceptions=HttpRuntimeException.class)
    public void testGetByQueryWithNullQuery() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);
        DawgHouseClient client = new DawgHouseClient();

        EasyMock.expect(mockClient.execute((RestRequest) EasyMock.anyObject())).andThrow(new HttpRuntimeException());
        EasyMock.replay(mockClient);

        client.setClient(mockClient);
        client.getByQuery(null);

    }
    @Test(expectedExceptions=HttpRuntimeException.class)
    public void testGetByQuery() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);
        RestResponse response = new RestResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 0, 0), 0, null));
        EasyMock.expect(mockClient.execute((RestRequest) EasyMock.anyObject())).andReturn(response);
        EasyMock.replay(mockClient);

        String query = "";

        client.getByQuery(query);
    }

    @Test
    public void testDeleteByIdsWithNullIds()throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);

        RestResponse response = new RestResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 0, 0), 200, null));
        EasyMock.expect(mockClient.execute((RestRequest) EasyMock.anyObject())).andReturn(response);
        EasyMock.replay(mockClient);

        Collection<String> ids = null;
        try {
            client.deleteByIds(ids );
        } catch (Exception e) {
            Assert.fail("deleteByIds  threw " + e);
        }
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void testDeleteMetaStbWithNull() throws HttpException  {
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);
        RestResponse response = new RestResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 0, 0), 0, null));
        EasyMock.expect(mockClient.execute((RestRequest) EasyMock.anyObject())).andReturn(response);
        EasyMock.replay(mockClient);

        MetaStb stb = null;

        client.delete(stb);
    }

    @Test
    public void testDeleteMetaStb() throws HttpException{
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);

        RestResponse response = new RestResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 0, 0), 0, null));
        EasyMock.expect(mockClient.delete((String) EasyMock.anyObject())).andReturn(true);
        EasyMock.replay(mockClient);

        String id = "";
        MetaStb stb = EasyMock.createMock(MetaStb.class);
        EasyMock.expect(stb.getId()).andReturn(id);
        EasyMock.expect(stb.getData()).andReturn(new HashMap<String,Object>());
        EasyMock.replay(stb);
        try {
            client.delete(stb);
        } catch (Exception e) {
            Assert.fail("delete stb  threw " + e);
        }
    }

    @Test
    public void testAddStbs() throws HttpException{
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);

        EasyMock.expect(mockClient.put((String) EasyMock.anyObject(),(Map)EasyMock.anyObject())).andReturn(true);
        EasyMock.replay(mockClient);

        String id = "770000004A12";
        Collection<MetaStb> stbs = new ArrayList<MetaStb>();

        MetaStb stb = EasyMock.createMock(MetaStb.class);
        EasyMock.expect(stb.getId()).andReturn(id);
        EasyMock.expect(stb.getData()).andReturn(new HashMap<String,Object>());
        EasyMock.replay(stb);
        stbs.add(stb);
        client.add(stbs);
    }

    @Test
    public void testAddStbsWithNullStbs() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);

        EasyMock.expect(mockClient.put((String) EasyMock.anyObject(),(Map)EasyMock.anyObject())).andReturn(true);
        EasyMock.replay(mockClient);
        Collection<MetaStb> stbs = null;

        client.add(stbs);
    }

    @Test
    public void testDeleteMetaStbs() throws HttpException {
        RestClient mockClient = EasyMock.createMock(RestClient.class);

        DawgHouseClient client = new DawgHouseClient();
        client.setClient(mockClient);

        RestResponse response = new RestResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 0, 0), 200, null));
        EasyMock.expect(mockClient.execute((RestRequest) EasyMock.anyObject())).andReturn(response);
        EasyMock.replay(mockClient);

        String id = "";
        Collection<MetaStb> stbs = new ArrayList<MetaStb>();
        MetaStb stb = EasyMock.createMock(MetaStb.class);
        EasyMock.expect(stb.getId()).andReturn(id).anyTimes();

        EasyMock.replay(stb);
        stbs.add(stb);
        try {
            client.delete(stbs);
        } catch (Exception e) {
            Assert.fail("delete stb  threw " + e);
        }
    }
}
