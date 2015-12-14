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
package com.comcast.video.dawg.cats.power;

import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.drivethru.test.MockRestClient;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.stbio.exceptions.PowerException;

/**
 * The TestNG test class for PowerClient
 * @author TATA
 *
 */
public class PowerClientTest {
    String catsHost ="10.253.86.157:8080";

    class MockPowerClient extends PowerClient {

        public MockPowerClient(String catsHost, String powerHost, String outlet, String powerType) {
            this(catsHost, powerHost, outlet, powerType, HttpStatus.SC_OK);
        }

        public MockPowerClient(String catsHost, String powerHost, String outlet, String powerType, int status) {
            super(catsHost, powerHost, outlet, powerType);
            assignMockRestClient(status);
        }

        public void assignMockRestClient(int status) {
            MockRestClient mrc = new MockRestClient(null);
            mrc.expect().andReturn(status);
            this.client = mrc;
        }
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testConstructorWithNullParam() {
        new PowerClient(null);
    }

    @Test
    public void testConstructor() {
        MetaStb stb = new MetaStb();
        PowerClient client = new PowerClient(stb);
        Assert.assertNotNull(client);
    }

    @Test (expectedExceptions=PowerException.class)
    public void testPowerOnWithNullPowerHost() throws PowerException {

        MockPowerClient client = new MockPowerClient(null,null,null,null, HttpStatus.SC_BAD_REQUEST);
        client.powerOn();
    }

    @Test
    public void testPowerOn()  {
        PowerClient client = new MockPowerClient(catsHost,null,null,null);
        try {
            client.powerOn();
        } catch (PowerException e) {
            Assert.fail("power on threw exception " + e);
        }
    }

    @Test
    public void testPowerOff()  {
        PowerClient client = new MockPowerClient(catsHost,null,null,null);
        try {
            client.powerOff();
        } catch (PowerException e) {
            Assert.fail("power on threw exception " + e);
        }
    }

    @Test
    public void testReboot()  {
        PowerClient client = new MockPowerClient(catsHost,null,null,null);
        try {
            client.reboot();
        } catch (PowerException e) {
            Assert.fail("power on threw exception " + e);
        }
    }

    @Test
    public void testPowerException() {
        String msg = "msg";
        PowerException exp = new PowerException(msg);
        Assert.assertEquals(exp.getMessage(), msg);
        exp = new PowerException(new Throwable());
        Assert.assertNotEquals(exp.getMessage(), msg);
    }
}
