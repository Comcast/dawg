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
package com.comcast.video.dawg.controller.pound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.video.dawg.common.ServerUtils;
import com.comcast.video.dawg.service.pound.DawgPoundMongoService;
import com.comcast.video.dawg.util.TestConstants;

/**
 * Unit test for {@link DawgPoundRestController} class.
 *
 * @author Rahul R.Prasad
 */
public class DawgPoundRestControllerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testGetReservedWhichReturnsEmptyReserveList() {
        ServerUtils serverUtil = new ServerUtils();
        DawgPoundMongoService poundService = EasyMock.createMock(DawgPoundMongoService.class);

        DawgPoundRestController poundController  = new DawgPoundRestController();
        ReflectionTestUtils.setField(poundController, "serverUtils", serverUtil);
        ReflectionTestUtils.setField(poundController, "service", poundService);

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        EasyMock.expect(poundService.getByReserver(TestConstants.PROCESSED_TOKEN)).andReturn(list.toArray(new Map[list.size()]));

        EasyMock.replay(poundService);


        Map<String,Object>[] reservers = poundController.getReservered(TestConstants.TOKEN);
        Assert.assertEquals(reservers.length, 0);

        EasyMock.verify(poundService);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetReservedWithValidReservedList() {
        Map<String, Object> deviceIds = new HashMap<String, Object>();
        deviceIds.put("id", TestConstants.DEVICE_ID);

        ServerUtils serverUtil = new ServerUtils();
        DawgPoundMongoService poundService = EasyMock.createMock(DawgPoundMongoService.class);

        DawgPoundRestController poundController  = new DawgPoundRestController();
        ReflectionTestUtils.setField(poundController, "serverUtils", serverUtil);
        ReflectionTestUtils.setField(poundController, "service", poundService);

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        list.add(deviceIds);
        EasyMock.expect(poundService.getByReserver(TestConstants.PROCESSED_TOKEN)).andReturn(list.toArray(new Map[list.size()]));

        EasyMock.replay(poundService);

        Map<String,Object>[] reservers = poundController.getReservered(TestConstants.TOKEN);
        Assert.assertEquals(reservers.length, 1);
        EasyMock.verify(poundService);
    }

    @DataProvider(name = "Token_Provider")
    public Object[][] getTokens() {
        return new Object[][] {
            { null , null},
                {TestConstants.TOKEN, TestConstants.TOKEN}
        };
    }

    @Test(dataProvider= "Token_Provider")
    public void testUnReserveController(String actualToken, String expectedToken) {
        ServerUtils serverUtil = new ServerUtils();
        DawgPoundMongoService poundService = EasyMock.createMock(DawgPoundMongoService.class);

        DawgPoundRestController poundController  = new DawgPoundRestController();
        ReflectionTestUtils.setField(poundController, "serverUtils", serverUtil);
        ReflectionTestUtils.setField(poundController, "service", poundService);

        EasyMock.expect(poundService.unreserve(TestConstants.PROCESSED_TOKEN)).andReturn(expectedToken);
        EasyMock.replay(poundService);

        actualToken = poundController.unreserve(TestConstants.TOKEN);
        Assert.assertEquals(actualToken, expectedToken);

        EasyMock.verify(poundService);
    }

    @Test
    public void testReservePolitely() {
        ServerUtils serverUtil = new ServerUtils();
        DawgPoundMongoService poundService = EasyMock.createMock(DawgPoundMongoService.class);

        DawgPoundRestController poundController  = new DawgPoundRestController();
        ReflectionTestUtils.setField(poundController, "serverUtils", serverUtil);
        ReflectionTestUtils.setField(poundController, "service", poundService);
        long expirationTime = System.currentTimeMillis() + TestConstants.ONE_DAY_IN_MILLIS;


        EasyMock.expect(poundService.reserve(TestConstants.DEVICE_ID, TestConstants.PROCESSED_TOKEN, expirationTime)).andReturn(TestConstants.PROCESSED_TOKEN);

        EasyMock.replay(poundService);
        String actualReservedToken = poundController.reservePolitely(TestConstants.TOKEN, TestConstants.ORIG_DEVICE_ID,  String.valueOf(expirationTime));
        Assert.assertEquals(actualReservedToken, TestConstants.PROCESSED_TOKEN);
        EasyMock.verify(poundService);
    }

    @Test
    public void testReserve() {
        ServerUtils serverUtil = new ServerUtils();
        DawgPoundMongoService poundService = EasyMock.createMock(DawgPoundMongoService.class);

        DawgPoundRestController poundController  = new DawgPoundRestController();
        ReflectionTestUtils.setField(poundController, "serverUtils", serverUtil);
        ReflectionTestUtils.setField(poundController, "service", poundService);
        long expirationTime = System.currentTimeMillis() + TestConstants.ONE_DAY_IN_MILLIS;

        EasyMock.expect(poundService.overrideReserve( TestConstants.DEVICE_ID, TestConstants.PROCESSED_TOKEN, expirationTime)).andReturn(true);

        EasyMock.replay(poundService);
        boolean reserveStatus = poundController.reserve(TestConstants.TOKEN, TestConstants.ORIG_DEVICE_ID, String.valueOf(expirationTime));
        Assert.assertEquals(reserveStatus, true);
        EasyMock.verify(poundService);
    }

    @Test
    public void testIsReserved() {
        ServerUtils serverUtil = new ServerUtils();
        DawgPoundMongoService poundService = EasyMock.createMock(DawgPoundMongoService.class);

        DawgPoundRestController poundController  = new DawgPoundRestController();
        ReflectionTestUtils.setField(poundController, "serverUtils", serverUtil);
        ReflectionTestUtils.setField(poundController, "service", poundService);
        long expirationTime = System.currentTimeMillis() + TestConstants.ONE_DAY_IN_MILLIS;

        Map<String, String> reserveMapForDevice = new HashMap<String, String>();
        reserveMapForDevice.put("reserver", TestConstants.PROCESSED_TOKEN);
        reserveMapForDevice.put("expiration", String.valueOf(expirationTime));

        EasyMock.expect(poundService.isReserved(TestConstants.DEVICE_ID)).andReturn(reserveMapForDevice);

        EasyMock.replay(poundService);
        Map<String, String> reserveList = poundController.isReservered(TestConstants.ORIG_DEVICE_ID);
        Assert.assertEquals(reserveList.size(), 2);
        Assert.assertEquals(reserveList.get("reserver"), TestConstants.PROCESSED_TOKEN);
        Assert.assertEquals(reserveList.get("expiration"),  String.valueOf(expirationTime));

        EasyMock.verify(poundService);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListAll() {
        DawgPoundMongoService poundService = EasyMock.createMock(DawgPoundMongoService.class);

        DawgPoundRestController poundController  = new DawgPoundRestController();
        ReflectionTestUtils.setField(poundController, "service", poundService);

        Map<String,Object> deviceMap = new HashMap<String,Object>();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        deviceMap.put("id", TestConstants.DEVICE_ID);
        list.add(deviceMap);

        EasyMock.expect(poundService.list()).andReturn(list.toArray(new Map[list.size()]));
        EasyMock.replay(poundService);

        Map<String,Object>[] actualDeviceMap = poundController.listAll();
        Assert.assertEquals(actualDeviceMap.length, 1);
        Assert.assertEquals(actualDeviceMap[0].get("id"), TestConstants.DEVICE_ID);
        EasyMock.verify(poundService);
    }
}
