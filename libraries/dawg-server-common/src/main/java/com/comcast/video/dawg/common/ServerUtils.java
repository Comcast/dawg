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
package com.comcast.video.dawg.common;

import com.comcast.video.dawg.common.exceptions.DawgIllegalArgumentException;

/**
 * Utilities for servers
 *
 * @author Val Apgar
 *
 */
public class ServerUtils {

    public String toLowerAlphaNumeric(String str) {
        return str.replaceAll("[^A-Za-z0-9 ]", "").trim().toLowerCase();
    }

    /**
     * See {@link ServerUtils#toLowerAlphaNumeric(String)} This method performs
     * the same function but does argument checking and exception handling.
     * Whenever a string can't be cleaned an {@link IllegalArgumentException} is
     * thrown.
     *
     * @param string
     * @return
     */
    public String clean(String string) {
        try {
            if (null != string) {
                return toLowerAlphaNumeric(string);
            } else {
                throw new IllegalArgumentException(
                        "The provided id cannot be null.");
            }
        } catch (Exception e) {
            throw new DawgIllegalArgumentException(
                    "The provided id was not acceptable.  Id=" + string, e);
        }
    }

    /**
     * This method checks for the presence of special characters in the input
     * string. If the string contains alpha numeric characters only, then it is
     * a valid string. Otherwise, it is invalid.
     *
     * @param string
     *            The input string to check the presence of special characters.
     * @return true, if it does not contain any special characters.
     *         false, otherwise.
     */

    public boolean isValid(String string) {
        boolean valid = false;
        if ((null != string) && !string.isEmpty()) {
            String str = string.replaceAll("[A-Za-z0-9]", "");
            if (str.length() == 0) {
                valid = true;
            }
        }

        return valid;
    }
}
