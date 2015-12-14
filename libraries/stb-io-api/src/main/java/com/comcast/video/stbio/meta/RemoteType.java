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

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the remote types that a user can choose to send IR keys. This is useful when a user
 * requires to send some keys which work only on particular remote types.
 *
 * @author Prasad Menon
 *
 */
public class RemoteType extends StbProp {

    /** The set of remoteTypes applicable for a settop*/
    private static final Set<RemoteType> enumerated = new HashSet<RemoteType>();

    /** The Motorola remote type for sending keys*/
    public static final RemoteType MOTOROLA = RemoteType.valueOf("MOTOROLA");

    /** The Scientific Atlanta remote type for sending keys*/
    public static final RemoteType SA = RemoteType.valueOf("SA");

    /** The XR2 LEGACY MOTOROLA remote type for sending keys*/
    public static final RemoteType XR2_LEGACY_MOTOROLA = RemoteType.valueOf("XR2_LEGACY_MOTOROLA");

    /** The COMCAST_XMP remote type for sending keys*/
    public static final RemoteType COMCAST_XMP = RemoteType.valueOf("COMCAST_XMP");

    /** The BCM-7437-1 remote type for sending keys*/
    public static final RemoteType BCM = RemoteType.valueOf("BCM-7437-1");

    /** The HD-DTA remote type for sending keys*/
    public static final RemoteType HDDTA = RemoteType.valueOf("HD-DTA");

    /** The XR2 remote type for sending keys*/
    public static final RemoteType XR2 = RemoteType.valueOf("XR2");

    /**
     * Creates Remote type
     * @param name The name of the remote which stb uses
     */
    private RemoteType(String name) {
        super(name);
    }

    /**
     * Gets a remoteType with the given name
     * @param remoteName  The name of the remote
     * @return the RemoteType to be used while pressing key
     */
    public static synchronized RemoteType valueOf(String remoteName) {
        if (remoteName == null) {
            return null;
        }
        for (RemoteType remoteType : enumerated) {
            if (remoteType.name.equals(remoteName)) {
                return remoteType;
            }
        }
        RemoteType remoteType = new RemoteType(remoteName);
        enumerated.add(remoteType);
        return remoteType;
    }
}
