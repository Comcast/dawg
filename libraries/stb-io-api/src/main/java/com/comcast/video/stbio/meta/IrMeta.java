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
package com.comcast.video.stbio.meta;

/**
 * Provides metadata necessary to send IR keys via CATS
 * @author Kevin Pearson
 *
 */
public interface IrMeta {
    /**
     * The server that forwards to the actual services
     * @return
     */
    String getCatsServerHost();

    /**
     * The type of IR blaster. eg. gc100
     * @return
     */
    String getBlasterType();

    /**
     * The url to the IR service
     * @return
     */
    String getIrServiceUrl();

    /**
     * The port of the ir service
     * @return
     */
    String getIrServicePort();

    /**
     * The type of remote to use for IR. eg. MOTOROLA
     * @return
     */
    String getRemoteType();

    /**
     * The type of cats keyset to use. e.g. XR2
     * @return
     */
    String getCatsKeySetMapping();
}
