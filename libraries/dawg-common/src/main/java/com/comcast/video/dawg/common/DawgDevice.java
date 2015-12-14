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

import java.util.Map;

/**
 * A DawgDevice is the central interface that handles all the data associated with a device.
 * A DawgDevice is essentially an arbitrary key=>value pair store.  Any keys are allowed
 * but typically small set of known keys should be shared among users.
 * @author Val Apgar
 *
 */
public interface DawgDevice {

    /**
     * @return The data associated with this DawgDevice.
     */
    public Map<String,Object> getData();

    /**
     * @param data The data associated with this DawgDevice.
     */
    public void setData(Map<String,Object> data);

}
