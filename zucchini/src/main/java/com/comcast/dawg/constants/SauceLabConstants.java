/**
 * Copyright 2017 Comcast Cable Communications Management, LLC
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
package com.comcast.dawg.constants;



/**
 * Sauce constant class  
 *
 * @author  Priyanka 
 *  */
public class SauceLabConstants {


    /** Constant to run tests in saucelabs. */
    public static final String SAUCE = "sauce";

    /** Constant to run tests in saucelabs. */
    public static final String LOCAL = "local";

    /** sauce url constant. */
    public static final String SAUCE_URL = "http://ondemand.saucelabs.com:80/wd/hub";

    /** Constant for dawg test */
    public static final String DAWG_TEST = "dawgtest";

    /** Constant for dawg test name for setting sauce capability */
    public static final String DAWG_TEST_INFO = "xDAWG Tests";

    /** Constant for dawg test name key for setting sauce capability */
    public static final String NAME = "name";

    /** Constant for tunnelIdentifier key for setting sauce capability */
    public static final String TUNNEL_IDENTIFIER = "tunnelIdentifier";

    /**
     * Constants to store the platform types
     */
    public static final String WINDOWS = "windows";
    public static final String MAC = "mac";
    public static final String LINUX = "linux"; 

}
