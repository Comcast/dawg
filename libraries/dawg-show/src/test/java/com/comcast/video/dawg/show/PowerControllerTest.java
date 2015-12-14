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

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.pantry.test.TestList;
import com.comcast.pantry.test.Wiring;
import com.comcast.video.dawg.cats.power.PowerClient;
import com.comcast.video.dawg.cats.power.PowerOperation;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.show.cache.MetaStbCache;
import com.comcast.video.stbio.exceptions.PowerException;


public class PowerControllerTest {
    class MockPowerClient extends PowerClient {
        public PowerOperation lastOp = null;

        public MockPowerClient(MetaStb stb) {
            super(stb);
        }

        @Override
        public void performPowerOperation(PowerOperation op) {
            this.lastOp = op;
        }
    }

    @DataProvider(name="testPowerData")
    public TestList testPowerData() {
        TestList list = new TestList();

        list.add("on", false);
        list.add("BAD", true);

        return list;
    }

    @Test(dataProvider="testPowerData")
    public void testPower(String op, boolean throwExc) {
        MetaStbCache cache = new MetaStbCache();
        MetaStb stb = new MetaStb();
        stb.setMacAddress("00:00:41:B7:CD:52");
        cache.putMetaStb(stb);
        final MockPowerClient client = new MockPowerClient(stb);
        PowerController controller = new PowerController();
        Assert.assertNotNull(controller.getPowerClient(stb)); // really just for code coverage
        controller = new PowerController() {
            protected PowerClient getPowerClient(MetaStb stb) {
                return client;
            }
        };
        PowerException exc = null;
        Wiring.autowire(controller, cache);
        Assert.assertNotNull(controller.getPowerClient(stb));
        try {
            controller.power(new String[] {"000041B7CD52"}, op);
        } catch (PowerException e) {
            exc = e;
        }
        Assert.assertEquals(exc != null, throwExc);
        if (!throwExc) {
            Assert.assertNotNull(client.lastOp);
            Assert.assertEquals(client.lastOp, PowerOperation.valueOf(op));
        }
    }
}
