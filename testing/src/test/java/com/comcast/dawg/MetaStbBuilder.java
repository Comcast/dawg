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
package com.comcast.dawg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.stbio.meta.Capability;
import com.comcast.video.stbio.meta.Family;
import com.comcast.video.stbio.meta.Model;
import com.comcast.video.stbio.meta.Program;

public class MetaStbBuilder {
    private MetaStb stb = new MetaStb();
    public static final String MAC_ADDRESS = "00:00:00:00:00:01";
    public static final Model MODEL = Model.valueOf("DCT6416");
    public static final Family FAMILY = Family.MAC_G;
    public static final String MAKE = "Motorola";
    public static final String NAME = "DeviceName";
    public static final String POWER_TYPE = "wti1600";
    public static final Program PROGRAM = Program.valueOf("cobra");
    public static final String REMOTE_TYPE = "MOTOROLA";
    public static final String RACK_NAME = "UTR-RD104";
    public static final String CONTROLLER_IP_ADDRESS = "0.253.210.1";
    public static final String HARDWARE_REVISION = "1.2";
    public static final String SLOT_NAME = "UTR-RD107-A01";
    public static final String IR_BLASTER_TYPE = "gc100";

    private static Integer UID = 0;

    public static Integer getNextUID() {
        synchronized(UID) {
            return ++UID;
        }
    }

    public static String getUID(String pref) {
        return pref + (System.currentTimeMillis() % TimeUnit.DAYS.toMillis(365)) + "" + getNextUID();
    }


    public MetaStbBuilder() {
        stb.setCapabilities(new ArrayList<Capability>());
        stb.setFamily(FAMILY);
        stb.setId("000000000001");
        stb.setMacAddress(MAC_ADDRESS);
        stb.setMake(MAKE);
        stb.setModel(MODEL);
        stb.setName(NAME);
        stb.setPowerType(POWER_TYPE);
        stb.setProgram(PROGRAM);
        stb.setRemoteType(REMOTE_TYPE);
        stb.setTags(new HashSet<String>());
        stb.setRackName(RACK_NAME);
        stb.setControllerIpAddress(CONTROLLER_IP_ADDRESS);
        stb.setHardwareRevision(HARDWARE_REVISION);
        stb.setSlotName(SLOT_NAME);
        stb.setBlasterType(IR_BLASTER_TYPE);
    }

    public MetaStbBuilder caps(String... caps) {
        Collection<Capability> c = new ArrayList<Capability>();
        for (String cap : caps) {
            c.add(Capability.valueOf(cap));
        }
        stb.setCapabilities(c);
        return this;
    }

    public MetaStbBuilder family(String family) {
        stb.setFamily(Family.valueOf(family));
        return this;
    }

    public MetaStbBuilder id(String id) {
        stb.setId(id);
        return this;
    }

    public MetaStbBuilder uid(String pref) {
        stb.setId(getUID(pref));
        return this;
    }

    public MetaStbBuilder mac(String mac) {
        stb.setMacAddress(mac);
        return this;
    }

    public MetaStbBuilder make(String make) {
        stb.setMake(make);
        return this;
    }

    public MetaStbBuilder irBlasterType(String irBlasterType) {
        stb.setBlasterType(irBlasterType);
        return this;
    }

    public MetaStbBuilder model(String model) {
        stb.setModel(Model.valueOf(model));
        return this;
    }

    public MetaStbBuilder name(String name) {
        stb.setName(name);
        return this;
    }

    public MetaStbBuilder power(String powerType) {
        stb.setPowerType(powerType);
        return this;
    }

    public MetaStbBuilder remote(String remoteType) {
        stb.setRemoteType(remoteType);
        return this;
    }

    public MetaStbBuilder rackName(String rackName) {
        stb.setRackName(rackName);
        return this;
    }

    public MetaStbBuilder controllerIpAddress(String controllerIpAddress) {
        stb.setControllerIpAddress(controllerIpAddress);
        return this;
    }

    public MetaStbBuilder slotName(String slotName) {
        stb.setSlotName(slotName);
        return this;
    }

    public MetaStbBuilder hardwareRevision(String hardwareRevision) {
        stb.setHardwareRevision(hardwareRevision);
        return this;
    }

    public MetaStbBuilder tags(String... tags) {
        Set<String> tagSet = new HashSet<String>();
        for (String tag : tags) {
            tagSet.add(tag);
        }
        stb.setTags(tagSet);
        return this;
    }

    public MetaStb stb() {
        return stb;
    }

    public static MetaStbBuilder build() {
        return new MetaStbBuilder();
    }
}
