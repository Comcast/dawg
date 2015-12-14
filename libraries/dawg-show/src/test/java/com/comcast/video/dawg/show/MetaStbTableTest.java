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
import org.testng.annotations.Test;

import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.show.util.TestUtils;

public class MetaStbTableTest {

    @Test
    public void testAllGetters() {
        MetaStb stb = TestUtils.createGenericMetaStb();
        stb.setIrServiceUrl(null);
        stb.setPlant(null);

        MetaStbTable table = new MetaStbTable(stb);
        Assert.assertEquals(table.getCatsServerHost(), TestUtils.CATS_SERVER);
        Assert.assertEquals(table.getContent(), TestUtils.CONTENT);
        Assert.assertEquals(table.getId(), TestUtils.ID);
        Assert.assertEquals(table.getIpAddress(), TestUtils.IP.toString());
        Assert.assertEquals(table.getIrServiceUrl(), "-&nbsp;:&nbsp;" + TestUtils.IRSERVICEPORT);
        Assert.assertEquals(table.getMacAddress(), TestUtils.MAC_ADDRESS);
        Assert.assertEquals(table.getMake(), TestUtils.MAKE);
        Assert.assertEquals(table.getModel(), TestUtils.MODEL.name());
        Assert.assertEquals(table.getName(), TestUtils.NAME);
        Assert.assertEquals(table.getPlant(), "-");
        Assert.assertEquals(table.getPowerServiceUrl(), TestUtils.POWERSERVICEURL + "&nbsp;:&nbsp;" + TestUtils.POWER_OUTLET);
        Assert.assertEquals(table.getRemoteType(), TestUtils.REMOTE_TYPE);
        Assert.assertEquals(table.getTags(), "kevin,&nbsp;kp");
        Assert.assertEquals(table.getSerialHost(), TestUtils.SERIALURL);
        Assert.assertEquals(table.getVideoSourceUrl(), TestUtils.VIDEOURL + "&nbsp;:&nbsp;" + TestUtils.VIDEOCAMERA);
    }

    @Test
    public void testNullCollection() {
        MetaStb stb = new MetaStb();
        stb.setTags(null);

        MetaStbTable table = new MetaStbTable(stb);
        Assert.assertEquals(table.getTags(), "-");
    }
}
