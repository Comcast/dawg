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

import com.comcast.video.dawg.common.ServerUtils;

/**
 * Constant class which holds the common constant variables used for unit testing.
 *
 * @author Rahul R.Prasad
 */
public class TestConstants {

    /** MongoDB collection name*/
    public  static final String COLLECTION_NAME = "DawgDevices";

    /** Device Id which is cleaned using {@link ServerUtils#clean(String)} */
    public static final String DEVICE_ID = "0000007f2acd";

    /** Token for testing */
    public static final String TOKEN = "094411bd-3a3d-4407-a339-173cc40a0479";

    /** Value of one day in milliseconds. */
    public static final long ONE_DAY_IN_MILLIS = 1 * 24 * 60 * 60 * 1000 ;

    /** Holds the Token which is cleaned using  {@link ServerUtils#clean(String)}*/
    public static final String PROCESSED_TOKEN = "094411bd3a3d4407a339173cc40a0479";

    /** Actual device MAC with out processing */
    public static final String ORIG_DEVICE_ID = "00:00:00:7F:2A:CD";
}
