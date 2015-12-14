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

import com.comcast.video.dawg.cats.ir.IrClient;
import com.comcast.video.dawg.cats.power.PowerClient;
import com.comcast.video.dawg.cats.serial.SerialClient;
import com.comcast.video.dawg.house.IDawgHouseClient;
import com.comcast.video.dawg.house.IDawgPoundClient;

/**
 * {@link IDawgClientFactory} is used to easily generate
 * the various dawg clients used by dawg.
 *
 * The {@link DawgClientFactory} implementation provides
 * a new client for each getter.
 *
 * {@link CachedDawgClientFactory} will only create
 * one object each time the method is called.
 *
 * @author Val Apgar
 *
 */
public interface IDawgClientFactory {

    public IDawgHouseClient<?> getHouseClient();
    public IDawgPoundClient getPoundClient();
    public IrClient getIrClient();
    public PowerClient getPowerClient();
    public SerialClient getSerialClient();
}
