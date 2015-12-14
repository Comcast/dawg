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
import com.comcast.video.dawg.house.IDawgHouseClient;
import com.comcast.video.dawg.house.IDawgPoundClient;

/**
 * TestNG Test for CachedDawgClientFactory
 * @author TATA
 *
 */
public class CachedDawgClientFactoryTest {

    @SuppressWarnings({ "rawtypes", "unused" })
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testConstructorWithNullParam() {
        CachedDawgClientFactory factory = new CachedDawgClientFactory(null,"","");
    }

    @Test
    public void testGetHouseClient() {
        CachedDawgClientFactory factory = new CachedDawgClientFactory(new MetaStb(),"","");
        @SuppressWarnings("rawtypes")
        IDawgHouseClient expected = factory.getHouseClient();
        Assert.assertEquals(factory.getHouseClient(), expected);
    }

    @Test
    public void testGetPoundClient() {
        CachedDawgClientFactory factory = new CachedDawgClientFactory(new MetaStb(),"","");
        Assert.assertNotNull(factory.getPoundClient());
        IDawgPoundClient expected = factory.getPoundClient();
        Assert.assertEquals(factory.getPoundClient(), expected);
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testGetIrClientWithNullParam() {
        CachedDawgClientFactory factory = new CachedDawgClientFactory(null);
        factory.getIrClient();
    }

    @Test
    public void testGetIrClient() {
        CachedDawgClientFactory factory = new CachedDawgClientFactory(new MetaStb(),"","");
        Assert.assertNotNull(factory.getIrClient());
        IrClient expected = factory.getIrClient();
        Assert.assertEquals(factory.getIrClient(), expected);
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testGetPowerClientWithNullParam() {
        CachedDawgClientFactory factory = new CachedDawgClientFactory(null);
        factory.getPowerClient();
    }

    @Test
    public void testGetPowerClient() {
        CachedDawgClientFactory factory = new CachedDawgClientFactory(new MetaStb(),"","");
        Assert.assertNotNull(factory.getPowerClient());
        PowerClient expected = factory.getPowerClient();
        Assert.assertEquals(factory.getPowerClient(), expected);
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testGetTraceClientWithNullParam() {
        CachedDawgClientFactory factory = new CachedDawgClientFactory(null);
        factory.getSerialClient();
    }

    @Test
    public void testGetTraceClient() {
        MetaStb stb = new MetaStb();
        stb.setMacAddress("00:00:41:B7:CD:52");
        CachedDawgClientFactory factory = new CachedDawgClientFactory(stb,"","");
        Assert.assertNotNull(factory.getSerialClient());
        SerialClient expected = factory.getSerialClient();
        Assert.assertEquals(factory.getSerialClient(), expected);
    }
}
