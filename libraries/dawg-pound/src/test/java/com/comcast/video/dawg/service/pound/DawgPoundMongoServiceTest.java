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
package com.comcast.video.dawg.service.pound;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.video.dawg.common.PersistableDevice;
import com.comcast.video.dawg.util.TestConstants;

/**
 * Unit test for {@link DawgPoundMongoService} class.
 *
 * @author Rahul R.Prasad
 *
 */
public class DawgPoundMongoServiceTest {

    @Test
    public void testUnReserveWithValidDevice() {
        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        PersistableDevice persistableDev = EasyMock.createMock(PersistableDevice.class);

        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);

        EasyMock.expect(mockMongoTemplate.findById(TestConstants.DEVICE_ID, PersistableDevice.class, TestConstants.COLLECTION_NAME )).andReturn(persistableDev);
        EasyMock.expect(persistableDev.unreserve()).andReturn(TestConstants.TOKEN);
        mockMongoTemplate.save(persistableDev, TestConstants.COLLECTION_NAME);

        EasyMock.replay(mockMongoTemplate, persistableDev);
        Assert.assertEquals(dawgPoundService.unreserve(TestConstants.DEVICE_ID), TestConstants.TOKEN);
        EasyMock.verify(mockMongoTemplate, persistableDev);
    }

    @Test
    public void testUnReserveWithInvalidDevice() {
        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);

        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);

        EasyMock.expect(mockMongoTemplate.findById(TestConstants.DEVICE_ID, PersistableDevice.class, TestConstants.COLLECTION_NAME )).andReturn(null);

        EasyMock.replay(mockMongoTemplate);
        Assert.assertNull(dawgPoundService.unreserve(TestConstants.DEVICE_ID));
        EasyMock.verify(mockMongoTemplate);
    }

    @DataProvider(name = "EXPIRY_TIME_PROVIDER")
        public Object[][] expirationTimeProvider() {
            return new Object[][] {
                { System.currentTimeMillis() -  TestConstants.ONE_DAY_IN_MILLIS},
                    { -1L  },
                    { 0L  },
                    {1367389800000L}, // 1st May 2013 12:00:00
                    { System.currentTimeMillis() -  (10 * TestConstants.ONE_DAY_IN_MILLIS)},
            };
        }

    @Test(dataProvider="EXPIRY_TIME_PROVIDER")
    public void testOverrideReservationInvalidExpiryTime(long expiryTime) {
        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);

        Assert.assertFalse(dawgPoundService.overrideReserve(TestConstants.DEVICE_ID, TestConstants.TOKEN, expiryTime));
    }

    @Test
    public void testOverrideReserveWithInvalidDevice() {
        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);

        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);
        EasyMock.expect(mockMongoTemplate.findById(TestConstants.DEVICE_ID, PersistableDevice.class, TestConstants.COLLECTION_NAME )).andReturn(null);

        EasyMock.replay(mockMongoTemplate);
        long expiryTime = System.currentTimeMillis()+  TestConstants.ONE_DAY_IN_MILLIS;
        Assert.assertFalse(dawgPoundService.overrideReserve(TestConstants.DEVICE_ID, TestConstants.TOKEN, expiryTime));
        EasyMock.verify(mockMongoTemplate);
    }

    @Test
    public void testOverrideReserve() {
        long expiryTime = System.currentTimeMillis()+  TestConstants.ONE_DAY_IN_MILLIS;

        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        PersistableDevice persistableDev = EasyMock.createMock(PersistableDevice.class);

        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);

        EasyMock.expect(mockMongoTemplate.findById(TestConstants.DEVICE_ID, PersistableDevice.class, TestConstants.COLLECTION_NAME )).andReturn(persistableDev);
        EasyMock.expect(persistableDev.unreserve()).andReturn("previous_token");
        EasyMock.expect(persistableDev.reserve(TestConstants.TOKEN)).andReturn(TestConstants.TOKEN);
        persistableDev.setExpiration(expiryTime);
        mockMongoTemplate.save(persistableDev, TestConstants.COLLECTION_NAME);
        EasyMock.replay(mockMongoTemplate,persistableDev);

        Assert.assertTrue(dawgPoundService.overrideReserve(TestConstants.DEVICE_ID, TestConstants.TOKEN, expiryTime));
        EasyMock.verify(mockMongoTemplate,persistableDev);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetByReserver() {
        Map<String, Object> deviceIds = new HashMap<String, Object>();
        deviceIds.put("id", TestConstants.DEVICE_ID);

        List<PersistableDevice> devices = new ArrayList<PersistableDevice>();

        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        PersistableDevice persistableDev = EasyMock.createMock(PersistableDevice.class);
        devices.add(persistableDev);

        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();

        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);
        EasyMock.expect(mockMongoTemplate.find((Query)EasyMock.anyObject(), (Class<PersistableDevice>) EasyMock.anyObject(), (String)EasyMock.anyObject())).andReturn(devices);
        EasyMock.expect(persistableDev.getData()).andReturn(deviceIds);

        EasyMock.replay(mockMongoTemplate,persistableDev);

        Map<String,Object>[] reservers = dawgPoundService.getByReserver(TestConstants.TOKEN);

        Assert.assertEquals(reservers.length, 1);
        Assert.assertEquals(reservers[0].get("id"), TestConstants.DEVICE_ID);
        EasyMock.verify(mockMongoTemplate,persistableDev);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetReserverWithNoDeviceAssoicatedWithToken() {
        List<PersistableDevice> devices = new ArrayList<PersistableDevice>();

        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();

        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);
        EasyMock.expect(mockMongoTemplate.find((Query)EasyMock.anyObject(), (Class<PersistableDevice>) EasyMock.anyObject(), (String)EasyMock.anyObject())).andReturn(devices);

        EasyMock.replay(mockMongoTemplate);

        Map<String,Object>[] reservers = dawgPoundService.getByReserver(TestConstants.TOKEN);
        Assert.assertEquals(reservers.length, 0);
        EasyMock.verify(mockMongoTemplate);
    }

    @Test
    public void testReserveFeatureWithNoDevice() {
        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);

        EasyMock.expect(mockMongoTemplate.findById(TestConstants.DEVICE_ID, PersistableDevice.class, TestConstants.COLLECTION_NAME )).andReturn(null);
        EasyMock.replay(mockMongoTemplate);

        Assert.assertNull(dawgPoundService.reserve(TestConstants.DEVICE_ID, TestConstants.TOKEN, (System.currentTimeMillis() + TestConstants.ONE_DAY_IN_MILLIS)));
        EasyMock.verify(mockMongoTemplate);
    }

    @Test
    public void testReserveWithDeviceAlreadyReserved() {
        String prevReserveToken = "8e6488ed-60f9-487f-b4bf-83c74fa0b06a";
        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        PersistableDevice persistableDev = EasyMock.createMock(PersistableDevice.class);

        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);

        EasyMock.expect(mockMongoTemplate.findById(TestConstants.DEVICE_ID, PersistableDevice.class, TestConstants.COLLECTION_NAME )).andReturn(persistableDev);
        EasyMock.expect(persistableDev.isReserved()).andReturn(true);
        EasyMock.expect(persistableDev.getReserver()).andReturn(prevReserveToken);
        EasyMock.replay(mockMongoTemplate,persistableDev);

        Assert.assertEquals(dawgPoundService.reserve(TestConstants.DEVICE_ID, TestConstants.TOKEN, (System.currentTimeMillis() + TestConstants.ONE_DAY_IN_MILLIS)), prevReserveToken);
        EasyMock.verify(mockMongoTemplate,persistableDev);
    }

    @Test
    public void testValidReservation() {
        String newToken = "8e6488ed-60f9-487f-b4bf-83c74fa0b06a";
        long expiryTime = System.currentTimeMillis()+  TestConstants.ONE_DAY_IN_MILLIS;

        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        PersistableDevice persistableDev = EasyMock.createMock(PersistableDevice.class);

        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);

        EasyMock.expect(mockMongoTemplate.findById(TestConstants.DEVICE_ID, PersistableDevice.class, TestConstants.COLLECTION_NAME )).andReturn(persistableDev);
        EasyMock.expect(persistableDev.isReserved()).andReturn(false);
        EasyMock.expect(persistableDev.unreserve()).andReturn("previous_token");
        EasyMock.expect(persistableDev.reserve(newToken)).andReturn(newToken);
        persistableDev.setExpiration(expiryTime);
        EasyMock.expect(persistableDev.getKeys()).andReturn(new String[] { });
        persistableDev.setKeys(EasyMock.aryEq(new String[] {newToken }));
        mockMongoTemplate.save(persistableDev, TestConstants.COLLECTION_NAME);

        EasyMock.replay(mockMongoTemplate,persistableDev);

        Assert.assertEquals(dawgPoundService.reserve(TestConstants.DEVICE_ID, newToken, expiryTime), newToken);
        EasyMock.verify(mockMongoTemplate,persistableDev);
    }

    @Test
    public void testIsReservedWithDeviceNotReserved() {
        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        PersistableDevice persistableDev = EasyMock.createMock(PersistableDevice.class);

        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);

        EasyMock.expect(mockMongoTemplate.findById(TestConstants.DEVICE_ID, PersistableDevice.class, TestConstants.COLLECTION_NAME )).andReturn(persistableDev);
        EasyMock.expect(persistableDev.isReserved()).andReturn(false);

        EasyMock.replay(mockMongoTemplate,persistableDev);

        Assert.assertNull(dawgPoundService.isReserved(TestConstants.DEVICE_ID));
        EasyMock.verify(mockMongoTemplate,persistableDev);
    }

    @Test
    public void testIsReservedWithInvalidDeviceId() {
        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);

        EasyMock.expect(mockMongoTemplate.findById(TestConstants.DEVICE_ID, PersistableDevice.class, TestConstants.COLLECTION_NAME )).andReturn(null);
        EasyMock.replay(mockMongoTemplate);

        Assert.assertNull(dawgPoundService.isReserved(TestConstants.DEVICE_ID));
        EasyMock.verify(mockMongoTemplate);
    }

    @Test
    public void testIsReservedWithDeviceReserved() {
        long expiryTime = System.currentTimeMillis() + TestConstants.ONE_DAY_IN_MILLIS;
        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        PersistableDevice persistableDev = EasyMock.createMock(PersistableDevice.class);

        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);

        EasyMock.expect(mockMongoTemplate.findById(TestConstants.DEVICE_ID, PersistableDevice.class, TestConstants.COLLECTION_NAME )).andReturn(persistableDev);
        EasyMock.expect(persistableDev.isReserved()).andReturn(true);
        EasyMock.expect(persistableDev.getReserver()).andReturn(TestConstants.TOKEN);
        EasyMock.expect(persistableDev.getExpiration()).andReturn(expiryTime);


        EasyMock.replay(mockMongoTemplate,persistableDev);
        Map<String, String> map = dawgPoundService.isReserved(TestConstants.DEVICE_ID);
        Assert.assertNotNull(map);
        Assert.assertEquals(map.size(), 2);
        Assert.assertEquals(map.get("reserver"), TestConstants.TOKEN);
        Assert.assertEquals(map.get("expiration"), String.valueOf(expiryTime));
        EasyMock.verify(mockMongoTemplate,persistableDev);
    }

    @Test
    public void testListAllDevicesReturnsEmptyList() {
        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);
        List<PersistableDevice> devices = new ArrayList<PersistableDevice>();

        EasyMock.expect(mockMongoTemplate.findAll(PersistableDevice.class, TestConstants.COLLECTION_NAME )).andReturn(devices);
        EasyMock.replay(mockMongoTemplate);

        Assert.assertEquals(dawgPoundService.list().length, 0);
        EasyMock.verify(mockMongoTemplate);
    }

    @Test
    public void testListAllDevices() {
        MongoTemplate mockMongoTemplate = EasyMock.createMock(MongoTemplate.class);
        DawgPoundMongoService dawgPoundService = new DawgPoundMongoService();
        ReflectionTestUtils.setField(dawgPoundService, "mongoTemplate", mockMongoTemplate);

        List<PersistableDevice> devices = new ArrayList<PersistableDevice>();
        PersistableDevice device1 = EasyMock.createMock(PersistableDevice.class);
        PersistableDevice device2 = EasyMock.createMock(PersistableDevice.class);
        Map<String,Object> dev1Map = new HashMap<String,Object>();
        Map<String,Object> dev2Map = new HashMap<String,Object>();


        devices.add(device1);
        devices.add(device2);
        dev1Map.put("id", TestConstants.DEVICE_ID);
        dev2Map.put("id", "000000682A48");


        EasyMock.expect(mockMongoTemplate.findAll(PersistableDevice.class, TestConstants.COLLECTION_NAME )).andReturn(devices);
        EasyMock.expect(device1.getData()).andReturn(dev1Map);
        EasyMock.expect(device2.getData()).andReturn(dev2Map);

        EasyMock.replay(mockMongoTemplate, device1, device2);

        Map<String,Object>[] list = dawgPoundService.list();
        Assert.assertEquals(list.length, 2);
        Assert.assertEquals(list[0].get("id"), TestConstants.DEVICE_ID);
        Assert.assertEquals(list[1].get("id"), "000000682A48");
        EasyMock.verify(mockMongoTemplate, device1, device2);
    }
}
