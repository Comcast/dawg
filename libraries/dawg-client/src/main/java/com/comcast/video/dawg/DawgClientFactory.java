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

public class DawgClientFactory implements IDawgClientFactory {

    private String dawgHouseHost;
    private String dawgPoundHost;
    private MetaStb stb;

    public DawgClientFactory(MetaStb metaStb) {
        this(metaStb, null, null);
    }

    public DawgClientFactory(MetaStb metaStb, String dawgHouseHost, String dawgPoundHost) {
        Validate.notNull(metaStb);
        this.dawgHouseHost = dawgHouseHost;
        this.dawgPoundHost = dawgPoundHost;
        this.stb = metaStb;
    }


    @Override
    public IDawgHouseClient<?> getHouseClient() {
        if (null == dawgHouseHost) {
            return new DawgHouseClient();
        } else {
            return new DawgHouseClient(dawgHouseHost);
        }

    }

    @Override
    public IDawgPoundClient getPoundClient() {
        if (null == dawgPoundHost) {
            return new DawgPoundClient();
        } else {
            return new DawgPoundClient(dawgPoundHost);
        }
    }

    @Override
    public IrClient getIrClient() {
        return new IrClient(stb);
    }

    @Override
    public PowerClient getPowerClient() {
        return new PowerClient(stb);
    }

    @Override
    public SerialClient getSerialClient() {
        return new SerialClient(stb);
    }

    public String getDawgHouseHost() {
        return dawgHouseHost;
    }

    public void setDawgHouseHost(String dawgHouseHost) {
        this.dawgHouseHost = dawgHouseHost;
    }

    public String getDawgPoundHost() {
        return dawgPoundHost;
    }

    public void setDawgPoundHost(String dawgPoundHost) {
        this.dawgPoundHost = dawgPoundHost;
    }
}
