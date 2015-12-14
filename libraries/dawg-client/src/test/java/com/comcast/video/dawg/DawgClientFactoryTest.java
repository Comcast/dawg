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
package com.comcast.video.dawg;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.video.dawg.cats.ir.IrClient;
import com.comcast.video.dawg.cats.power.PowerClient;
import com.comcast.video.dawg.cats.serial.SerialClient;
import com.comcast.video.dawg.common.MetaStb;

/**
 * The TestNG Test class for   DawgClientFactory
 * @author TATA
 *
 */
public class DawgClientFactoryTest {
    @Test
    public void testGetHouseClient() {
        DawgClientFactory factory = new DawgClientFactory(new MetaStb(),"","");
        Assert.assertNotNull(factory.getHouseClient());
    }

    @Test
    public void testGetPoundClient() {
        DawgClientFactory factory = new DawgClientFactory(new MetaStb(),"","");
        Assert.assertNotNull(factory.getPoundClient());
    }

    @Test
    public void testGetIrClient() {
        DawgClientFactory factory = new DawgClientFactory(new MetaStb(),"","");
        Assert.assertNotNull(factory.getIrClient());
        IrClient expected = factory.getIrClient();
        Assert.assertNotEquals(factory.getIrClient(), expected);
    }
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testGetIrClientWithNullParam() {
        DawgClientFactory factory = new DawgClientFactory(null);
        factory.getIrClient();
    }

    @Test
    public void testGetPowerClient() {
        DawgClientFactory factory = new DawgClientFactory(new MetaStb(),"","");
        Assert.assertNotNull(factory.getPowerClient());
        PowerClient expected = factory.getPowerClient();
        Assert.assertNotEquals(factory.getPowerClient(), expected);
    }
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testGetPowerClientWithNullParam() {
        DawgClientFactory factory = new DawgClientFactory(null);
        factory.getPowerClient();
    }

    @Test
    public void testGetTraceClient() {
        MetaStb stb = new MetaStb();
        stb.setMacAddress("00:00:41:B7:CD:52");
        DawgClientFactory factory = new DawgClientFactory(stb,"","");
        Assert.assertNotNull(factory.getSerialClient());
        SerialClient expected = factory.getSerialClient();
        Assert.assertNotEquals(factory.getSerialClient(), expected);
    }
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testGetTraceClienWithNullParam() {
        DawgClientFactory factory = new DawgClientFactory(null);
        factory.getSerialClient();
    }

    @Test
    public void testGetDawgHouseHost() {
        String host = "host";
        String newHost = "newHost";
        DawgClientFactory factory = new DawgClientFactory(new MetaStb(),host,"");
        Assert.assertNotNull(factory.getDawgHouseHost());
        Assert.assertEquals(factory.getDawgHouseHost(), host);
        factory.setDawgHouseHost(newHost);
        Assert.assertNotNull(factory.getDawgHouseHost());
        Assert.assertEquals(factory.getDawgHouseHost(), newHost);
    }
    @Test
    public void testGetDawgPoundHost() {
        String host = "host";
        String newHost = "newHost";
        DawgClientFactory factory = new DawgClientFactory(new MetaStb(),"",host);
        Assert.assertNotNull(factory.getDawgPoundHost());
        Assert.assertEquals(factory.getDawgPoundHost(), host);
        factory.setDawgPoundHost(newHost);
        Assert.assertNotNull(factory.getDawgPoundHost());
        Assert.assertEquals(factory.getDawgPoundHost(), newHost);
    }
}
