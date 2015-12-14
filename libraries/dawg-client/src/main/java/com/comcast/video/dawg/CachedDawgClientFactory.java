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

import org.apache.commons.lang.Validate;

import com.comcast.video.dawg.cats.ir.IrClient;
import com.comcast.video.dawg.cats.power.PowerClient;
import com.comcast.video.dawg.cats.serial.SerialClient;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.house.DawgHouseClient;
import com.comcast.video.dawg.house.DawgPoundClient;
import com.comcast.video.dawg.house.IDawgHouseClient;
import com.comcast.video.dawg.house.IDawgPoundClient;

public class CachedDawgClientFactory implements IDawgClientFactory {

    IDawgPoundClient poundClient;
    IDawgHouseClient<?> houseClient;
    PowerClient powerClient;
    IrClient irClient;
    SerialClient serialClient;
    private String dawgHouseHost;
    private String dawgPoundHost;
    private MetaStb stb;

    public CachedDawgClientFactory(MetaStb metaStb) {
        this(metaStb, null, null);
    }

    public CachedDawgClientFactory(MetaStb metaStb, String dawgHouseHost, String dawgPoundHost) {
        Validate.notNull(metaStb);
        this.dawgHouseHost = dawgHouseHost;
        this.dawgPoundHost = dawgPoundHost;
        this.stb = metaStb;
    }

    @Override
    public IDawgHouseClient<?> getHouseClient() {
        if (null == houseClient) {
            if (null == dawgHouseHost) {
                houseClient = new DawgHouseClient();
            } else {
                houseClient = new DawgHouseClient(dawgHouseHost);
            }
        }
        return houseClient;
    }

    @Override
    public IDawgPoundClient getPoundClient() {
        if (null == poundClient) {
            if (null == dawgPoundHost) {
                poundClient = new DawgPoundClient();
            } else {
                poundClient = new DawgPoundClient(dawgPoundHost);
            }
        }
        return poundClient;
    }

    @Override
    public IrClient getIrClient() {
        if (null == irClient) {
            irClient = new IrClient(stb);
        }
        return irClient;
    }

    @Override
    public PowerClient getPowerClient() {
        if (null == powerClient) {
            powerClient = new PowerClient(stb);
        }
        return powerClient;
    }

    @Override
    public SerialClient getSerialClient() {
        if (null == serialClient) {
            serialClient = new SerialClient(stb);
        }
        return serialClient;
    }
}
