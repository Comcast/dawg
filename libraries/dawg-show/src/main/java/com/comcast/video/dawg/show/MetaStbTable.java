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
package com.comcast.video.dawg.show;

import static com.comcast.video.dawg.show.HtmlFormatter.collectionToString;
import static com.comcast.video.dawg.show.HtmlFormatter.format;
import static com.comcast.video.dawg.show.HtmlFormatter.n;

import com.comcast.video.dawg.common.MetaStb;

/**
 * Formats the data from {@link MetaStb} to data to be displayed in the metdata table
 * @author Kevin Pearson
 *
 */
public class MetaStbTable {
    private MetaStb stb;

    /**
     * Creates a MetaStbTable
     * @param stb The stb to create the table for
     */
    public MetaStbTable(MetaStb stb) {
        this.stb = stb;
    }

    /**
     * See {@link MetaStb#getId()}
     * @return
     */
    public String getId() {
        return n(stb.getId());
    }

    /**
     * See {@link MetaStb#getName()}
     * @return
     */
    public String getName() {
        return n(stb.getName());
    }

    /**
     * See {@link MetaStb#getMake()}
     * @return
     */
    public String getMake() {
        return n(stb.getMake());
    }

    /**
     * See {@link MetaStb#getModel()}
     * @return
     */
    public String getModel() {
        return n(stb.getModel());
    }

    /**
     * See {@link MetaStb#getPlant()}
     * @return
     */
    public String getPlant() {
        return n(stb.getPlant());
    }

    /**
     * See {@link MetaStb#getContent()}
     * @return
     */
    public String getContent() {
        return n(stb.getContent());
    }

    /**
     * See {@link MetaStb#getIpAddress()}
     * @return
     */
    public String getIpAddress() {
        return n(stb.getIpAddress());
    }

    /**
     * See {@link MetaStb#getMacAddress()}
     * @return
     */
    public String getMacAddress() {
        return n(stb.getMacAddress());
    }

    /**
     * See {@link MetaStb#getTags()}
     * @return
     */
    public String getTags() {
        return collectionToString(stb.getTags());
    }

    /**
     * See {@link MetaStb#getIrServiceUrl()}
     * @return
     */
    public String getIrServiceUrl() {
        return format(n(stb.getIrServiceUrl()) + " : "  + n(stb.getIrServicePort()));
    }

    /**
     * See {@link MetaStb#getVideoSourceUrl()}
     * @return
     */
    public String getVideoSourceUrl() {
        return format(n(stb.getVideoSourceUrl()) + " : " + n(stb.getVideoCamera()));
    }

    /**
     * See {@link MetaStb#getPowerServiceUrl()}
     * @return
     */
    public String getPowerServiceUrl() {
        return format(n(stb.getPowerServiceUrl()) + " : " + n(stb.getPowerOutlet()));
    }

    /**
     * See {@link MetaStb#getSerialHost()}
     * @return
     */
    public String getSerialHost() {
        return n(stb.getSerialHost());
    }

    /**
     * See {@link MetaStb#getCatsServerHost()}
     * @return
     */
    public String getCatsServerHost() {
        return n(stb.getCatsServerHost());
    }

    /**
     * See {@link MetaStb#getRemoteType()}
     * @return
     */
    public String getRemoteType() {
        return n(stb.getRemoteType());
    }

}
