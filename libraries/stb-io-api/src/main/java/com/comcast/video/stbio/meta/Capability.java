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
 * Represents a capability that an stb could have. This is meant to just mock
 * an enum that can be set to any value, for backwards compatibility sake.
 * @author Kevin Pearson
 *
 */
public class Capability extends StbProp {
    private static final Set<Capability> enumerated = new HashSet<Capability>();

    public static final Capability _68K = Capability.valueOf("_68K");
    public static final Capability ASTB = Capability.valueOf("ASTB");
    public static final Capability DVR = Capability.valueOf("DVR");
    public static final Capability HD = Capability.valueOf("HD");
    public static final Capability VOD = Capability.valueOf("VOD");
    public static final Capability MIPS32 = Capability.valueOf("MIPS32");
    public static final Capability SINGLE_TUNER = Capability.valueOf("SINGLE_TUNER");
    public static final Capability DUAL_TUNER = Capability.valueOf("DUAL_TUNER");

    /**
     * Creates a Capability
     * @param name The name of the capability
     */
    private Capability(String name) {
        super(name);
    }

    /**
     * Gets a capability with the given name
     * @param name
     * @return
     */
    public static synchronized Capability valueOf(String name) {
        if (name == null) {
            return null;
        }
        for (Capability cap : enumerated) {
            if (cap.name.equals(name)) {
                return cap;
            }
        }
        Capability cap = new Capability(name);
        enumerated.add(cap);
        return cap;
    }
}
