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
package com.comcast.video.dawg.service.house;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.WriteResult;

import org.easymock.EasyMock;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.test.util.ReflectionTestUtils;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.video.dawg.common.DawgDevice;
import com.comcast.video.dawg.common.PersistableDevice;
import com.comcast.video.dawg.service.park.ParkService;

/**
 * The TestNG test class for MongoHouseService.
 *
 * @author  TATA
 */
public class MongoHouseServiceTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAll() {

        MongoHouseService service = new MongoHouseService();
        MongoOperations op = EasyMock.createMock(MongoOperations.class);
        List<PersistableDevice> list = new ArrayList<PersistableDevice>();
        list.add(new PersistableDevice());
        EasyMock.expect(op.find((Query) EasyMock.anyObject(), (Class) EasyMock.anyObject(), (String) EasyMock.anyObject())).andReturn(list);
        EasyMock.replay(op);
        ReflectionTestUtils.setField(service, "template", op);
        Assert.assertNotNull(service.getAll());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetById() {
        MongoHouseService service = new MongoHouseService();
        MongoOperations operations = EasyMock.createMock(MongoOperations.class);
        EasyMock.expect(operations.findById((Object) EasyMock.anyObject(), (Class) EasyMock.anyObject(), (String) EasyMock.anyObject())).andReturn(null).anyTimes();
        EasyMock.replay(operations);
        ReflectionTestUtils.setField(service, "template", operations);
        Assert.assertNull(service.getById("id"), "expected null but got" + service.getById("id"));
    }

    //@Test
    //public void testDeleteStbById() {
        //MongoHouseService service = new MongoHouseService();
        //MongoOperations operations = EasyMock.createMock(MongoOperations.class);
        //WriteResult result = WriteResult.unacknowledged();
        //EasyMock.expect(operations.remove((Query)EasyMock.anyObject(), (String)EasyMock.anyObject())).andReturn(result);
        //EasyMock.replay(operations);
        //ReflectionTestUtils.setField(service, "template", operations);

        //String[] id = new String[1];
        //id[0] = "";

        //try {
            //service.deleteStbById(id);
        //} catch (Exception e) {
            //Assert.fail(e.getMessage());
        //}
    //}

    @SuppressWarnings("unchecked")
    @Test
    public void testGetStbsByQuery() {
        MongoHouseService service = new MongoHouseService();
        MongoOperations operations = EasyMock.createMock(MongoOperations.class);
        EasyMock.expect(operations.find((Query) EasyMock.anyObject(), (Class) EasyMock.anyObject(),
                    (String) EasyMock.anyObject())).andReturn(null);
        EasyMock.replay(operations);
        ReflectionTestUtils.setField(service, "template", operations);

        String query = "";
        Assert.assertNotNull(service.getStbsByQuery(query), "Got Null ");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetStbsByKey() {
        MongoHouseService service = new MongoHouseService();
        MongoOperations operations = EasyMock.createMock(MongoOperations.class);
        EasyMock.expect(operations.find((Query) EasyMock.anyObject(), (Class) EasyMock.anyObject(),
                    (String) EasyMock.anyObject())).andReturn(null);

        EasyMock.replay(operations);
        ReflectionTestUtils.setField(service, "template", operations);

        String[] id = new String[1];
        id[0] = "a";
        Assert.assertNotNull(service.getStbsByKey(id), "Got Null");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetStbsById() {
        MongoHouseService service = new MongoHouseService();
        MongoOperations operations = EasyMock.createMock(MongoOperations.class);
        List<Object> obj = new ArrayList<Object>();
        EasyMock.expect(operations.find(EasyMock.anyObject(Query.class), EasyMock.anyObject(Class.class),
                    EasyMock.anyObject(String.class))).andReturn(obj);
        EasyMock.replay(operations);
        ReflectionTestUtils.setField(service, "template", operations);

        String[] id = new String[1];
        id[0] = "";
        Assert.assertNotNull(service.getStbsById(id), "Got Null");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpsertStb() {
        MongoHouseService service = new MongoHouseService();
        MongoOperations operations = EasyMock.createMock(MongoOperations.class);
        EasyMock.expect(operations.collectionExists((Class) EasyMock.anyObject())).andReturn(false);
        EasyMock.expect(operations.createCollection((Class) EasyMock.anyObject())).andReturn(null);

        operations.save((PersistableDevice) EasyMock.anyObject(), (String) EasyMock.anyObject());
        EasyMock.expectLastCall();
        EasyMock.expect(operations.findById((Object) EasyMock.anyObject(), (Class) EasyMock.anyObject(),
                    (String) EasyMock.anyObject())).andReturn(null);
        EasyMock.replay(operations);
        ReflectionTestUtils.setField(service, "template", operations);

        ParkService mockParkService  = EasyMock.createMock(ParkService.class);
        EasyMock.expect(mockParkService.getModelById((String) EasyMock.anyObject())).andReturn(null);
        EasyMock.replay(mockParkService);
        ReflectionTestUtils.setField(service, "parkService", mockParkService);

        DawgDevice[] stb = new DawgDevice[1];
        stb[0] = new PersistableDevice();

        try {
            service.upsertStb(stb);
        } catch (Exception e) {
            Assert.fail(e.getMessage(),e);
        }
    }
}
