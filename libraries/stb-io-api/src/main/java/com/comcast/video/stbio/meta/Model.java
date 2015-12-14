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
 * Represents a model of an stb. This is meant to just mock
 * an enum that can be set to any value, for backwards compatibility sake.
 * @author Kevin Pearson
 *
 */
public class Model extends StbProp {

    private static final Set<Model> enumerated = new HashSet<Model>();

    /**
     * Creates a Model
     * @param name The name of the model
     */
    private Model(String name) {
        super(name);
    }

    /**
     * Gets a capability with the given name
     * @param name
     * @return
     */
    public static synchronized Model valueOf(String name) {
        if (name == null) {
            return null;
        }
        for (Model model : enumerated) {
            if (model.name.equals(name)) {
                return model;
            }
        }
        Model model = new Model(name);
        enumerated.add(model);
        return model;
    }
}
