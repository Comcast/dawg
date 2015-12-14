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
package com.comcast.video.dawg.controller.house.filter;

import org.springframework.data.mongodb.core.query.Criteria;

/**
 * Interface for a condition on searching for a device. This will provide a way
 * to convert a user query to a mongodb {@link Criteria}
 * @author Kevin Pearson
 *
 */
public interface Condition {
    /**
     * Converts to a mongo db Criteria to apply to a query
     * @return
     */
    Criteria toCriteria();

    /**
     * Converts to a mongo db Criteria to apply to a query
     * @param negate negates the condition to do the opposite
     * @return
     */
    Criteria toCriteria(boolean negate);
}
