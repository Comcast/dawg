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
package com.comcast.dawg.saucelab;

import java.io.IOException;

import com.saucelabs.ci.sauceconnect.SauceConnectFourManager;
import com.saucelabs.ci.sauceconnect.SauceTunnelManager;

/**
 * Class which starts and stop sauce connection.
 * @author Priyanka
 *
 */
public class SauceConnector {

    private static final String options = " -i " + SauceConstants.DAWG_TEST;
    private static SauceTunnelManager tunnelManager;
    private static SauceConnector connector = null;

    /**
     * Creates instance of SauceConnector
     * @return SauceConnector
     */
    public static SauceConnector getInstance() {
        if (null == connector) {
            connector = new SauceConnector();
        }
        return connector;
    }

    /**
     * Method to establish secure sauce connection to run tests in sauce lab
     * @throws SauceTestException 
     */

    public void startSauceConnect() throws SauceTestException {
        tunnelManager = new SauceConnectFourManager(true);
        try {
            tunnelManager.openConnection(SauceProvider.getSauceUserName(), SauceProvider.getSauceKey(),
                Integer.parseInt(SauceProvider.getSaucePort()), null, options, null, true, null);

        } catch (IOException e) {
            throw new SauceTestException("Error generated when launching Sauce Connect", e);
        }

    }

    /**
     * Close sauce connection with sauce labs once the test execution completes.  
     *
     */
    public void stopSauceConnect() {
        //close running process
        try {
            if (null != tunnelManager) {
                tunnelManager.closeTunnelsForPlan(SauceProvider.getSauceKey(), options, null);
            } else {
                throw new SauceTestException("Failed to close Sauce Connection");
            }
        } catch (SauceTestException e) {
            e.printStackTrace();
        }
    }
}
