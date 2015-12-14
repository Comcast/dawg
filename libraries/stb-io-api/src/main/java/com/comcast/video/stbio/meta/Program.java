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
 * Represents some software that an stb is running. This is meant to just mock
 * an enum that can be set to any value, for backwards compatibility sake.
 * @author Kevin Pearson
 *
 */
public class Program extends StbProp {

    private static final Set<Program> enumerated = new HashSet<Program>();

    /**
     * Creates a Program
     * @param name The name of the program
     */
    public Program(String name) {
        super(name);
    }

    public static synchronized Program valueOf(String name) {
        if (name == null) {
            return null;
        }
        for (Program program : enumerated) {
            if (program.name.equals(name)) {
                return program;
            }
        }
        Program program = new Program(name);
        enumerated.add(program);
        return program;
    }
}
