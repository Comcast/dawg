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
 * Represents a family of an stb. This is meant to just mock
 * an enum that can be set to any value, for backwards compatibility sake.
 * @author Kevin Pearson
 *
 */
public class Family extends StbProp {
    private static final Set<Family> enumerated = new HashSet<Family>();

    public static final Family MAC_G = Family.valueOf("MAC_G");
    public static final Family MAC_S = Family.valueOf("MAC_S");
    public static final Family DTA = Family.valueOf("DTA");
    public static final Family HD_DTA = Family.valueOf("HD-DTA");


    /**
     * Creates a Family
     * @param name The name of the family
     */
    private Family(String name) {
        super(name);
    }

    /**
     * Same as calling {@link Family#Family(String)}
     * @param name
     * @return
     */
    public static synchronized Family valueOf(String name) {
        if (name == null) {
            return null;
        }
        for (Family fam : enumerated) {
            if (fam.name.equals(name)) {
                return fam;
            }
        }
        Family fam = new Family(name);
        enumerated.add(fam);
        return fam;
    }
}
