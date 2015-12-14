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
package com.comcast.video.dawg.cats.power;

import org.apache.commons.lang.Validate;

import com.comcast.drivethru.utils.Method;
import com.comcast.drivethru.utils.URL;
import com.comcast.video.dawg.DawgClient;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.stbio.PowerInput;
import com.comcast.video.stbio.PowerOutput;
import com.comcast.video.stbio.exceptions.PowerException;

/**
 * Client for issuing power commands to an stb
 * @author Kevin Pearson
 *
 */
public class PowerClient extends DawgClient implements PowerInput, PowerOutput {
    public static final String POWER_SERVICE = "power-service";
    public static final String REST = "rest";
    public static final int DEFAULT_PORT = 0;

    private String catsHost;
    private String powerHost;
    private String outlet;
    private String powerType;

    /**
     * Creates a client for the stb with the given metadata
     * @param stb The metadata about the stb
     *
     */
    public PowerClient(MetaStb stb) {
        Validate.notNull(stb);
        this.catsHost = stb.getCatsServerHost();
        this.powerHost = stb.getPowerServiceUrl();
        this.outlet = stb.getPowerOutlet();
        this.powerType = stb.getPowerType();
    }

    /**
     * Creates a new PowerClient
     * @param catsHost The host to the CATS server
     * @param powerHost The host to the power service
     * @param outlet The outlet that the stb is plugged into
     * @param powerType The type of the power controller (eg. wti1600)
     */
    public PowerClient(String catsHost, String powerHost, String outlet, String powerType) {
        this.catsHost = catsHost;
        this.powerHost = powerHost;
        this.outlet = outlet;
        this.powerType = powerType;
    }

    /**
     * Powers on the stb
     * @throws PowerException
     */
    @Override
    public void powerOn() throws PowerException {
        performPowerOperation(PowerOperation.on);
    }

    /**
     * Powers off the stb
     * @throws PowerException
     */
    @Override
    public void powerOff() throws PowerException {
        performPowerOperation(PowerOperation.off);
    }

    /**
     * Reboots the stb
     * @throws PowerException
     */
    @Override
    public void reboot() throws PowerException {
        performPowerOperation(PowerOperation.reboot);
    }

    /**
     * Performs the given power operation
     * @param op The operation to perform
     * @throws PowerException
     */
    public void performPowerOperation(PowerOperation op) throws PowerException {
        executeRequest(formPowerUrl(op), op);
    }

    /**
     * Actually sends the request through a rest client
     * @param url
     * @param op
     * @throws PowerException
     */
    private void executeRequest(URL url, PowerOperation op) throws PowerException {
        try {
            executeRequest(url, Method.POST);
        } catch (Exception e) {
            throw new PowerException("Failed to issue power command '" + op.name() + "'", e);
        }
    }

    /**
     * Forms the URL to issue the power command
     * @param op The type of power operation to use
     * @return
     */
    private URL formPowerUrl(PowerOperation op) {
        URL url = new URL(formCatsServerBaseUrl(catsHost));
        url.addPath(POWER_SERVICE).addPath(REST);
        url.addPath(powerType).addPath(powerHost).addPath("" + DEFAULT_PORT).addPath(outlet).addPath(op.name());
        return url;
    }

    @Override
    public boolean isPowerOn() throws PowerException {
        // TODO Auto-generated method stub
        return false;
    }
}
