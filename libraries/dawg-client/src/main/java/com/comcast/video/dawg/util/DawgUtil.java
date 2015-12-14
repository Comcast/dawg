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
package com.comcast.video.dawg.util;

/**
 * Various Utility methods
 * @author Kevin Pearson
 *
 */
public class DawgUtil {
    /**
     * Strips all non-alphanumeric characters from a string
     * @param str The string to strip the characters from
     * @return
     */
    public static String onlyAlphaNumerics(String str) {
        StringBuilder rv = new StringBuilder();

        for (char c : str.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                rv.append(c);
            }
        }

        return rv.toString();
    }

    public static String toLowerAlphaNumeric(String str) {
        return DawgUtil.onlyAlphaNumerics(str).trim().toLowerCase();
    }
}
