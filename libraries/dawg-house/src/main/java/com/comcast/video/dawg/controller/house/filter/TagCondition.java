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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * Creates a condition with tag and query to fetch details from Mongo. It will do an AND operation on the input fields.
 *
 * @author Jijo Jose
 *
 */
public class TagCondition extends BaseCondition {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagCondition.class);
    private static final String KEY_TAG = "data.tags";
    /* Tag value to be searched */
    private String tag;
    /* Query string */
    private String query;

    /**
     * Create a TagCondition
     *
     * @param tag
     *            The tag to be searched
     * @param query
     *            The query to be search along with tag value
     */
    public TagCondition(String tag, String query) {
        this.tag = tag;
        this.query = query;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Criteria toCriteria() {
        return toCriteria(false);
    }

    /**
     * Converts to a mongo db Criteria to apply to a query
     *
     * @param negate
     *            negates the condition to do the opposite
     * @return criteria with tag and query
     */
    @Override
    public Criteria toCriteria(boolean negate) {
        Criteria criteria = (!negate) ? Criteria.where(KEY_TAG).is(tag) : Criteria.where(KEY_TAG).not().is(tag);
        if (query != null) {
            String[] keys = query.toLowerCase().split(" ");
            List<Criteria> list = new ArrayList<Criteria>();
            for (String key : keys) {
                list.add(Criteria.where("keys").regex(key));
            }
            criteria.andOperator(list.toArray(new Criteria[list.size()]));
        }
        LOGGER.debug("Tag search criteria : " + criteria.getCriteriaObject());

        return criteria;
    }
}
