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
package com.comcast.video.dawg.show.video;

import java.util.List;

import com.comcast.cereal.annotations.Cereal;

/**
 * Object that correlates with the compare object that is created and sent to the javascript
 * @author Kevin Pearson
 *
 */
public class Compare {
    private List<CerealizableRegionInfo> regions;
    private String imgBase64;
    @Cereal(defaultValue="10")
    private int timeout;
    private String name;

    public Compare() {

    }

    public Compare(List<CerealizableRegionInfo> regions, String imgBase64, String name) {
        this.regions = regions;
        this.imgBase64 = imgBase64;
        this.name = name;
    }

    public List<CerealizableRegionInfo> getRegions() {
        return regions;
    }

    public void setRegions(List<CerealizableRegionInfo> regions) {
        this.regions = regions;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    /**
     * Gets the number of seconds for the timeout
     * @return
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets the timeout in seconds
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
