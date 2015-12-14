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

import java.util.Collection;

import com.comcast.video.stbio.meta.StbProp;

/**
 * Helper class to format java strings into html
 * @author Kevin Pearson
 *
 */
public class HtmlFormatter {
    public static final String SPACE = "&nbsp;";
    public static final String NEWLINE = "<br/>";

    /**
     * Helper method that will replace any null values with a -
     * @param val The value to check if null
     * @return
     */
    public static final String n(Object val) {
        return n(val, "-");
    }

    /**
     * Helper method to replace null stb props with a -
     * @param stbProp
     * @return
     */
    public static final String n(StbProp stbProp) {
        return n(stbProp == null ? null : stbProp.name(), "-");
    }

    /**
     * Helper method that will replace any null values with another value
     * @param val The value to check if null
     * @param replace What to return if the value was null
     * @return
     */
    public static final String n(Object val, String replace) {
        return val == null || val.toString().trim().isEmpty() ? replace : val.toString();
    }

    /**
     * Converts a collection to a comma separated list of strings as a string
     * @param coll The collection to convert
     * @return
     */
    public static final String collectionToString(Collection<?> coll) {
        if (coll == null) {
            return "-";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Object obj : coll) {
                if (obj != null) {
                    if (!sb.toString().isEmpty()) {
                        sb.append("," + SPACE);
                    }
                    sb.append(obj.toString());
                }
            }
            return sb.toString();
        }
    }

    /**
     * Formats a string so that space are non break space and new lines are line breaks.
     * @param str The string to format
     * @return The formatted string
     */
    public static final String format(String str) {
        return str.replace(" ", SPACE).replace("\t", SPACE + SPACE + SPACE).replace("\n", NEWLINE);
    }
}
