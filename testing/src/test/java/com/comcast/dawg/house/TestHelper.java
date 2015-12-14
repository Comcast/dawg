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
package com.comcast.dawg.house;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
/**
 * The Helper class to define the valid and invalid query strings that need to be used in the tests.
 * @author TATA
 *
 */
public class TestHelper {
    /*
     * String values used for query test cases.
     */
    public static final String STR_NORMAL           = "normal";
    public static final String STR_BYTE_ARRAY       = new String(new byte[] { 0x00, 0x00, 0x27, 0x13 },Charset.forName("UTF-8"));
    public static final String STR_LONG_NUMBER      = "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
    public static final String STR_EMPTY            = "";
    public static final String STR_URL_PARAM_1      = "?refresh=true";
    public static final String STR_URL_PARAM_2      = "&refresh=true";
    public static final String STR_URL_PARAM_2_ENC  = "%26refresh%3dtrue";
    public static final String STR_NULL_ENC         = "%0";
    public static final String STR_URL_ENC          = "%3cscript%20src=%22http%3a%2f%2fwww.jsfromanothersite.com%2fnasty.js%22%3e%3c%2fscript%3e";
    public static final String STR_SQL_ENC          = "%3BDROP%20TABLE%20table_name";
    public static final String STR_HASH             = "#AnchorTag";
    public static final String STR_HASH_ENC         = "%23AnchorTag";
    public static final String STR_BLANK            = "          ";
    public static final String STR_BLANK_ENC        = "%20%20%20%20%20%20%20%20%20%20";
    public static final String STR_NEWLINE          = "\r\nFLUSHDB";
    public static final String STR_LEAD_SPACES      = "        this_string_begins_with_spaces";
    public static final String STR_TRAIL_SPACES     = "this_string_has_trailing_spaces     ";
    public static final String STR_MID_SPACES       = "spaces_in_the_       _of_this";
    public static final String STR_QUOTE            = "\" FLUSHDB";
    public static final String STR_QUOTE_NEWLINE    = "\"\r\nFLUSHDB";
    public static final String STR_QUOTE_PAIR       = "\"escapedQuotes\"";
    public static final String STR_QUOTE_NESTED     = "\"\"\"\"\"";
    public static final String STR_SPACE_UTF8       = "\u0020";
    public static final String STR_UTF8             = "\u0048\u0065\u006C\u006C\u006FWorld";
    public static final String STR_UTF16            = "\u140000"; // not recognized by UTF-8
    public static final String STR_UTF8_2           = "\\ FLUSHDB \u0022 + "\"";
    public static final String STR_UTF8_HIGH        = "\uD800";
    public static final String STR_LEAD_SPACES_ENC  = "%20%20%20%20%20%20%20%20this_string_begins_with_spaces";
    public static final String STR_TRAIL_SPACES_ENC = "this_string_has_trailing_spaces%20%20%20%20%20";
    public static final String STR_MID_SPACES_ENC   = "spaces_in_the_%20%20%20%20%20%20%20_of_this";
    public static final String STR_NEWLINE_ENC      = "%5Cr%5CnFLUSHDB";
    public static final String STR_QUOTE_ENC        = "%5C%22%20FLUSHDB";
    public static final String STR_URL_PARAM_1_ENC  = "%3Frefresh%3Dtrue";
    public static final String STR_QUOTE_NESTED_ENC = "%22%22%22%22%22";


    /**
     * @return a list of String objects containing only the null string
     */
    public static List<String> getNullString() {
        List<String> list = new ArrayList<String>();

        list.add(null);

        return list;
    }

    /**
     * @return a list of string literals used as test parameters against
     * the config server
     */
    public static List<String> getTestStrings() {
        List<String> list = new ArrayList<String>();

        list.add(STR_NORMAL);
        list.add(STR_BYTE_ARRAY);
        list.add(STR_LONG_NUMBER);
        list.add(STR_EMPTY);
        list.add(STR_URL_PARAM_1);
        list.add(STR_URL_PARAM_1_ENC);
        list.add(STR_URL_PARAM_2);
        list.add(STR_URL_PARAM_2_ENC);
        list.add(STR_NULL_ENC);
        list.add(STR_URL_ENC);
        list.add(STR_SQL_ENC);
        list.add(STR_HASH);
        list.add(STR_HASH_ENC);
        list.add(STR_BLANK);
        list.add(STR_BLANK_ENC);
        list.add(STR_NEWLINE);
        list.add(STR_NEWLINE_ENC);
        list.add(STR_LEAD_SPACES);
        list.add(STR_LEAD_SPACES_ENC);
        list.add(STR_TRAIL_SPACES);
        list.add(STR_TRAIL_SPACES_ENC);
        list.add(STR_MID_SPACES);
        list.add(STR_MID_SPACES_ENC);
        list.add(STR_QUOTE);
        list.add(STR_QUOTE_ENC);
        list.add(STR_QUOTE_NESTED);
        list.add(STR_QUOTE_NESTED_ENC);
        list.add(STR_QUOTE_NEWLINE);
        list.add(STR_QUOTE_PAIR);
        list.add(STR_SPACE_UTF8);
        list.add(STR_UTF8);
        list.add(STR_UTF8_2);
        list.add(STR_UTF8_HIGH);

        list.add(null);

        return list;
    }
}
