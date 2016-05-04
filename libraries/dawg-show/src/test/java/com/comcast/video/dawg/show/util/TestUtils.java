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
package com.comcast.video.dawg.show.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.stbio.meta.Capability;
import com.comcast.video.stbio.meta.Family;
import com.comcast.video.stbio.meta.Model;
import com.comcast.video.stbio.meta.Program;

public class TestUtils {
    public static final String CATS_SERVER = "http://10.253.86.157:8080/";
    public static final String CONTENT = "Native";
    public static final String ID = "000041b7cd52";
    public static final InetAddress IP;
    static {
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {}
        IP = ip;
    }
    public static final String IRSERVICEURL = "10.253.85.10";
    public static final String IRSERVICEPORT = "2";
    public static final String MAC_ADDRESS = "00:00:41:B7:CD:52";
    public static final String MAKE = "Motorola";
    public static final Model MODEL = Model.valueOf("DCT 6416");
    public static final Family FAMILY = Family.valueOf("MAC_G");
    public static final Program PROGRAM = new Program("bali");
    public static final Collection<Capability> CAPABILITIES = new ArrayList<Capability>();
    static {
        CAPABILITIES.add(Capability.valueOf("DVR"));
    }
    public static final String NAME = "DCT6416p3_789";
    public static final String PLANT = "CVS-MV-DAC1_Env";
    public static final String POWERSERVICEURL = "10.253.84.130";
    public static final String POWER_OUTLET = "10";
    public static final String REMOTE_TYPE = "MOTOROLA";
    public static final Set<String> TAGS = new TreeSet<String>(new Comparator<String>() {

        @Override
        public int compare(String o1, String o2) {
            if (o1 == null) {
                return Integer.MIN_VALUE;
            }
            if (o2 == null) {
                return Integer.MAX_VALUE;
            }
            return o1.compareTo(o2);
        }

    });

    static {
        TAGS.add("kevin");
        TAGS.add(null);
        TAGS.add("kp");
    }
    public static final String SERIALURL = "10.253.84.75";
    public static final String VIDEOURL = "10.253.85.25";
    public static final String VIDEOCAMERA = "2";
    public static final String AUDIOURL = "10.172.46.121";

    public static MetaStb createGenericMetaStb() {
        MetaStb stb = new MetaStb();
        stb.setCatsServerHost(CATS_SERVER);
        stb.setContent(CONTENT);
        stb.setId(ID);
        stb.setIpAddress(IP);
        stb.setIrServiceUrl(IRSERVICEURL);
        stb.setIrServicePort(IRSERVICEPORT);
        stb.setMacAddress(MAC_ADDRESS);
        stb.setMake(MAKE);
        stb.setModel(MODEL);
        stb.setName(NAME);
        stb.setFamily(FAMILY);
        stb.setProgram(PROGRAM);
        stb.setCapabilities(CAPABILITIES);
        stb.setPlant(PLANT);
        stb.setPowerServiceUrl(POWERSERVICEURL);
        stb.setPowerOutlet(POWER_OUTLET);
        stb.setRemoteType(REMOTE_TYPE);
        stb.setTags(TAGS);
        stb.setSerialHost(SERIALURL);
        stb.setVideoSourceUrl(VIDEOURL);
        stb.setVideoCamera(VIDEOCAMERA);
        stb.setAudioUrl(AUDIOURL);

        return stb;
    }
}
