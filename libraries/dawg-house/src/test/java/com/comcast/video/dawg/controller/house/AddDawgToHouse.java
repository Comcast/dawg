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
package com.comcast.video.dawg.controller.house;

import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.house.DawgHouseClient;
import com.comcast.video.stbio.meta.Capability;
import com.comcast.video.stbio.meta.Model;
import com.comcast.video.stbio.meta.Family;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class AddDawgToHouse {

    private static final String dawgHouseUrlPropName = "dawgHouseUrl";
    private static final String[] requiredProps = {MetaStb.ID, MetaStb.TAGS, MetaStb.NAME, MetaStb.MAKE, MetaStb.MODEL,
        MetaStb.PLANT, MetaStb.CONTENT, MetaStb.IPADDRESS, MetaStb.MACADDRESS, MetaStb.IRSERVICEURL, MetaStb.IRSERVICEPORT,
        MetaStb.VIDEOSOURCEURL, MetaStb.VIDEOCAMERA, MetaStb.POWERSERVICEURL, MetaStb.POWEROUTLET, MetaStb.POWERTYPE,
        MetaStb.SERIAL_HOST, MetaStb.CATSSERVERHOST, MetaStb.REMOTETYPE, MetaStb.CAPABILITIES, MetaStb.FAMILY,
        dawgHouseUrlPropName};


    @Test(enabled = true, dependsOnMethods = {"listRequiredProps"})
    public void addStb() {
        DawgHouseClient dhc;
        MetaStb mstb = new MetaStb();

        {
            String idString = System.getProperty(MetaStb.ID);
            Assert.assertNotNull(idString, "Required system property " + MetaStb.ID + " not set.");
            mstb.setId(idString);
        }

        {
            String tagsString = System.getProperty(MetaStb.TAGS);
            Assert.assertNotNull(tagsString, "Required system property " + MetaStb.TAGS + " not set.");
            HashSet<String> tagSet = new HashSet<String>();
            for (String tag : tagsString.split("\\s")) {
                tagSet.add(tag);
            }
            mstb.setTags(Collections.unmodifiableSet(tagSet));
        }

        {
            String nameString = System.getProperty(MetaStb.NAME);
            Assert.assertNotNull(nameString, "Required system property " + MetaStb.NAME + " not set.");
            mstb.setName(nameString);
        }

        {
            String makeString = System.getProperty(MetaStb.MAKE);
            Assert.assertNotNull(makeString, "Required system property " + MetaStb.MAKE + " not set.");
            mstb.setMake(makeString);
        }

        {
            String modelString = System.getProperty(MetaStb.MODEL);
            Assert.assertNotNull(modelString, "Required system property " + MetaStb.MODEL + " not set.");
            mstb.setModel(Model.valueOf(modelString));
        }

        {
            String plantString = System.getProperty(MetaStb.PLANT);
            Assert.assertNotNull(plantString, "Required system property " + MetaStb.PLANT + " not set.");
            mstb.setPlant(plantString);
        }

        {
            String contentString = System.getProperty(MetaStb.CONTENT);
            Assert.assertNotNull(contentString, "Required system property " + MetaStb.CONTENT + " not set.");
            mstb.setContent(contentString);
        }

        {
            String ipAddressString = System.getProperty(MetaStb.IPADDRESS);
            InetAddress inAddr;
            Assert.assertNotNull(ipAddressString, "Required system property " + MetaStb.IPADDRESS + " not set.");
            try {
                inAddr = InetAddress.getByName(ipAddressString);
                mstb.setIpAddress(inAddr);
            } catch (UnknownHostException e) {
                Assert.fail("Required system property " + MetaStb.IPADDRESS + " set to unknown host \"" + ipAddressString + "\"");
            }
        }

        {
            String macAddressString = System.getProperty(MetaStb.MACADDRESS);
            Assert.assertNotNull(macAddressString, "Required system property " + MetaStb.MACADDRESS + " not set.");
            mstb.setMacAddress(macAddressString);
        }

        {
            String irServiceUrlString = System.getProperty(MetaStb.IRSERVICEURL);
            Assert.assertNotNull(irServiceUrlString, "Required system property " + MetaStb.IRSERVICEURL + " not set.");
            mstb.setIrServiceUrl(irServiceUrlString);
        }

        {
            String irServicePortString = System.getProperty(MetaStb.IRSERVICEPORT);
            Assert.assertNotNull(irServicePortString, "Required system property " + MetaStb.IRSERVICEPORT + " not set.");
            mstb.setIrServicePort(irServicePortString);
        }

        {
            String videoSourceUrlString = System.getProperty(MetaStb.VIDEOSOURCEURL);
            Assert.assertNotNull(videoSourceUrlString, "Required system property " + MetaStb.VIDEOSOURCEURL + " not set.");
            mstb.setVideoSourceUrl(videoSourceUrlString);
        }

        {
            String videoCameraString = System.getProperty(MetaStb.VIDEOCAMERA);
            Assert.assertNotNull(videoCameraString, "Required system property " + MetaStb.VIDEOCAMERA + " not set.");
            mstb.setVideoCamera(videoCameraString);
        }

        {
            String powerServiceUrlString = System.getProperty(MetaStb.POWERSERVICEURL);
            Assert.assertNotNull(powerServiceUrlString, "Required system property " + MetaStb.POWERSERVICEURL + " not set.");
            mstb.setPowerServiceUrl(powerServiceUrlString);
        }

        {
            String powerOutletString = System.getProperty(MetaStb.POWEROUTLET);
            Assert.assertNotNull(powerOutletString, "Required system property " + MetaStb.POWEROUTLET + " not set.");
            mstb.setPowerOutlet(powerOutletString);
        }

        {
            String powerTypeString = System.getProperty(MetaStb.POWERTYPE);
            Assert.assertNotNull(powerTypeString, "Required system property " + MetaStb.POWERTYPE + " not set.");
            mstb.setPowerType(powerTypeString);
        }

        {
            String traceServiceUrlString = System.getProperty(MetaStb.SERIAL_HOST);
            Assert.assertNotNull(traceServiceUrlString, "Required system property " + MetaStb.SERIAL_HOST + " not set.");
            mstb.setSerialHost(traceServiceUrlString);
        }

        {
            String catsServerHostString = System.getProperty(MetaStb.CATSSERVERHOST);
            Assert.assertNotNull(catsServerHostString, "Required system property " + MetaStb.CATSSERVERHOST + " not set.");
            mstb.setCatsServerHost(catsServerHostString);
        }

        {
            String remoteTypeString = System.getProperty(MetaStb.REMOTETYPE);
            Assert.assertNotNull(remoteTypeString, "Required system property " + MetaStb.REMOTETYPE + " not set.");
            mstb.setRemoteType(remoteTypeString);
        }

        {
            String capabilitiesString = System.getProperty(MetaStb.CAPABILITIES);
            Assert.assertNotNull(capabilitiesString, "Required system property " + MetaStb.CAPABILITIES + " not set.");
            ArrayList<Capability> capArray = new ArrayList<Capability>();
            for (String cap : capabilitiesString.split("\\s")) {
                capArray.add(Capability.valueOf(cap));
            }
            mstb.setCapabilities(capArray);
        }

        {
            String familyString = System.getProperty(MetaStb.FAMILY);
            Assert.assertNotNull(familyString, "Required system property " + MetaStb.FAMILY + " not set.");
            mstb.setFamily(Family.valueOf(familyString));
        }

        {
            String dawgHouseUrlString = System.getProperty(dawgHouseUrlPropName);
            Assert.assertNotNull(dawgHouseUrlString, "Required system property " + dawgHouseUrlPropName + " not set.");
            dhc = new DawgHouseClient(dawgHouseUrlString);
        }

        dhc.add(mstb);
    }

    @Test(enabled = true)
    public void listRequiredProps() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Properties required to add an STB to the Dawg Pound");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        for (String prop : requiredProps) {
            System.out.println(prop);
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }
}
