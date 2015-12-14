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

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.video.stbio.meta.Model;

/**
 * The TestNG  test class for MetaStb
 * @author TATA
 *
 */
public class MetaStbTest {
    private MetaStb metaStb = new MetaStb();
    private String id = "";
    private InetAddress ipAddress = EasyMock.createMock(InetAddress.class);
    private String irServicePort = "";
    private String  irServiceUrl = "";
    private String macAddress = "";
    private String make="";

    private String name="";
    private String plant="";
    private String powerOutlet = "";
    private String powerServiceUrl = "";
    private String powerType = "";
    private String remoteType = "";
    private String catsServerHost = "";
    private Model model =  Model.valueOf(catsServerHost);
    private Set<String> tags;
    private String videoCamera ="";
    private String videoSourceUrl="";
    private String traceServiceUrl="";
    private String content = "";

    @Test
    public void MetaStbtest() {
        EasyMock.expect(ipAddress.getHostName()).andReturn(null).anyTimes();
        EasyMock.replay(ipAddress);
        populateMetaStb(this.metaStb);

        Assert.assertEquals( metaStb.getId(),id);
        Assert.assertTrue( metaStb.getIpAddress().toString().contains("localhost"));
        Assert.assertEquals( metaStb.getIrServicePort() , irServicePort);
        Assert.assertEquals( metaStb.getIrServiceUrl(), irServiceUrl);
        Assert.assertEquals( metaStb.getMacAddress(), macAddress);
        Assert.assertEquals( metaStb.getMake() , make);
        Assert.assertEquals( metaStb.getModel(), model);
        Assert.assertEquals( metaStb.getName(), name);
        Assert.assertEquals( metaStb.getPlant(), plant);
        Assert.assertEquals( metaStb.getPowerOutlet(), powerOutlet);
        Assert.assertEquals( metaStb.getPowerServiceUrl(), powerServiceUrl);
        Assert.assertEquals( metaStb.getPowerType(), powerType);
        Assert.assertEquals( metaStb.getRemoteType(), remoteType);
        Assert.assertEquals( metaStb.getTags(), tags);
        Assert.assertEquals( metaStb.getSerialHost(), traceServiceUrl);
        Assert.assertEquals( metaStb.getVideoCamera(), videoCamera);
        Assert.assertEquals( metaStb.getVideoSourceUrl(), videoSourceUrl);
        Assert.assertEquals( metaStb.getCatsServerHost(), catsServerHost);
        Assert.assertEquals( metaStb.getContent(), content);
        Assert.assertEquals(metaStb.getControllerId(), "18235");
        Assert.assertEquals(metaStb.getControllerName(), "dncs.dtdncs");

        MetaStb obj = new MetaStb();

        populateMetaStb(obj);

        Assert.assertTrue(metaStb.compareTo(obj) == 0);
        Assert.assertTrue(metaStb.compareTo(null) < 0);
        obj.setId(null);
        metaStb.setId(null);
        Assert.assertTrue(metaStb.compareTo(obj) == 0);
        obj.setId("");
        Assert.assertTrue(metaStb.compareTo(obj) < 0);
        metaStb.setId("");
        obj.setId(null);
        Assert.assertTrue(metaStb.compareTo(obj) == 1);
        Assert.assertTrue(metaStb.hashCode() > 0);
        obj.setId("");
        Assert.assertTrue(metaStb.equals(obj));
        Assert.assertNotNull(metaStb.toString());
        Map<String, Object> data = new HashMap<String,Object>();
        MetaStb stb = new MetaStb(data);
        Assert.assertEquals(stb.getData(), new HashMap<String,Object>());

    }

    private void populateMetaStb(MetaStb metaStb) {
        metaStb.setId(id);
        metaStb.setIpAddress(ipAddress);
        metaStb.setIrServicePort(irServicePort);
        metaStb.setIrServiceUrl(irServiceUrl);
        metaStb.setMacAddress(macAddress);
        metaStb.setMake(make);
        metaStb.setModel(model);
        metaStb.setName(name);
        metaStb.setPlant(plant);
        metaStb.setPowerOutlet(powerOutlet);
        metaStb.setPowerServiceUrl(powerServiceUrl);
        metaStb.setPowerType(powerType);
        metaStb.setRemoteType(remoteType);
        metaStb.setTags(tags);
        metaStb.setSerialHost(traceServiceUrl);
        metaStb.setVideoCamera(videoCamera);
        metaStb.setVideoSourceUrl(videoSourceUrl);
        metaStb.setCatsServerHost(catsServerHost);
        metaStb.setContent(content);
        metaStb.setControllerId("18235");
        metaStb.setControllerName("dncs.dtdncs");
    }
}
