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

import java.util.List;

import com.comcast.video.dawg.common.MetaStb;

/**
 * This is a drop-in interface to handle populating a list of set top boxes.
 *
 * @author Andrew Benton
 */
public interface PopulatingClient {

    /**
     * This method should be implemented in to return a populated list of set top boxes.
     *
     * @param arg The string to use to specify the requested list of set top boxes.
     * @return The list of available set top boxes
     * @throws Exception Indicates a failure to obtain the list
     */
    public List<MetaStb> populate(String arg) throws Exception;
}
