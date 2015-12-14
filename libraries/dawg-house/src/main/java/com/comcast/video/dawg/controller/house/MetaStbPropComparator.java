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
package com.comcast.video.dawg.controller.house;

import java.util.Comparator;

import org.apache.commons.lang.ArrayUtils;

/**
 * Sorts MetaStb properties. This will put certain important properties first
 * and then sort the rest alphabetically
 * @author Kevin Pearson
 *
 */
class MetaStbPropComparator implements Comparator<String> {

    public static final String[] orderedProps = new String[] { "name" , "model"};

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(String o1, String o2) {

        int order1 = ArrayUtils.indexOf(orderedProps, o1);
        int order2 = ArrayUtils.indexOf(orderedProps, o2);
        if (order1 < 0) {
            if (order2 < 0) {
                // none have a specified order, order alphabetically
                return o1.compareTo(o2);
            } else {
                // o1 is not ordered but o2 is, o2 should go first
                return  1;
            }
        } else {
            if (order2 < 0) {
                // o1 is  ordered but o2 is not, o1 should go first
                return  -1;
            } else {
                // both are ordered, so order by the order...
                return  order1 - order2;
            }
        }
    }
}
