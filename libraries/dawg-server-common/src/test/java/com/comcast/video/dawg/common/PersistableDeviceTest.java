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

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * The TestNG test class for PersistableDevice
 * @author TATA
 *
 */
public class PersistableDeviceTest {
    @Test
    public void testConstructorWithParam() {
        DawgDevice dawgDevice = new MetaStb();
        PersistableDevice device = new PersistableDevice(dawgDevice);
        Assert.assertNotNull(device);
    }

    @Test
    public void testGetExpiration() {
        PersistableDevice device = new PersistableDevice();
        Long expectedExpiration = Long.valueOf(0l);
        device.setExpiration(expectedExpiration);
        Assert.assertEquals(device.getExpiration(),expectedExpiration);
    }

    @Test
    public void testGetExpirationStoredAsInteger() {
        Map<String, Object> data = new HashMap<String, Object>();
        Integer i = 100;
        data.put(PersistableDevice.EXPIRATION_KEY, i);
        PersistableDevice device = new PersistableDevice(data);
        Assert.assertEquals(device.getExpiration().longValue(), 100);
    }

    @Test
    public void testGetKeys() {
        PersistableDevice device = new PersistableDevice();
        String[] expectedKeys = new String[] {""};
        device.setKeys(expectedKeys);
        Assert.assertEquals(device.getKeys(),expectedKeys);
    }
    @Test
    public void testGetId() {
        PersistableDevice device = new PersistableDevice();
        String expectedId = "";
        device.setId(expectedId);
        Assert.assertEquals(device.getId(),expectedId);
    }
    @Test
    public void testGetData() {
        PersistableDevice device = new PersistableDevice();
        Map<String,Object> expectedData = null;
        device.setData(expectedData);
        Assert.assertEquals(device.getData(),expectedData);
    }
    @Test
    public void testHashCode() {
        PersistableDevice device = new PersistableDevice();
        Assert.assertNotNull(device.hashCode());
    }
    @Test
    public void testEquals() {
        PersistableDevice device = new PersistableDevice();
        PersistableDevice device2 = device;
        Assert.assertTrue(device.equals(device2));
    }
    @Test
    public void testEqualsToNull() {
        PersistableDevice device = new PersistableDevice();
        PersistableDevice device2 = null;
        Assert.assertFalse(device.equals(device2));
    }
    @Test
    public void testEqualsToDifferentType() {
        PersistableDevice device = new PersistableDevice();
        Object  device2 = new Object();;
        Assert.assertFalse(device.equals(device2));
    }
    @Test
    public void testEqualsToSameType() {
        PersistableDevice device = new PersistableDevice();
        device.setId(null);
        PersistableDevice  device2 = new PersistableDevice();
        device2.setId("id");
        Assert.assertFalse(device.equals(device2));
        device.setId("id1");
        Assert.assertFalse(device.equals(device2));
        device.setId("id");
        Assert.assertTrue(device.equals(device2));
    }
    @Test
    public void testReserve() {
        PersistableDevice device = new PersistableDevice();
        Assert.assertNotNull( device.reserve("token"));
    }
    @Test
    public void testIsReserved() {
        PersistableDevice device = new PersistableDevice();
        device.reserve("token");
        Assert.assertTrue( device.isReserved());
        device.unreserve();
        Assert.assertFalse( device.isReserved());
    }

    @Test
    public void testGetReserver() {
        PersistableDevice device = new PersistableDevice();
        String expectedToken = "token";
        device.reserve(expectedToken);
        Assert.assertEquals(device.getReserver(), expectedToken);
    }

}
