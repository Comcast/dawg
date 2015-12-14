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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comcast.video.dawg.cats.power.PowerClient;
import com.comcast.video.dawg.cats.power.PowerOperation;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.show.cache.MetaStbCache;
import com.comcast.video.stbio.exceptions.PowerException;

/**
 * Controller for handling sending power commands
 * @author Kevin Pearson
 *
 */
@Controller
public class PowerController implements ViewConstants {

    @Autowired
    private MetaStbCache metaStbCache;

    /**
     * Sends the given power command
     * @param deviceId The id of the device to send the power command to
     * @param command The power command to send. See {@link PowerOperation}
     * @return
     * @throws PowerException
     */
    @RequestMapping(method = { RequestMethod.POST }, value = "/power")
    @ResponseBody
    public void power(@RequestParam(value="deviceIds[]") String[] deviceIds, @RequestParam String command) throws PowerException {
        PowerException exc = null;
        StringBuilder failedIds = new StringBuilder();
        for (String deviceId : deviceIds) {
            MetaStb stb = metaStbCache.getMetaStb(deviceId);
            try {
                PowerClient client = getPowerClient(stb);

                try {
                    PowerOperation op = PowerOperation.valueOf(command);
                    client.performPowerOperation(op);
                } catch (IllegalArgumentException iae) {
                    throw new PowerException("Invalid power command '" + command + "'", iae);
                }
            } catch (PowerException e) {
                failedIds.append((!failedIds.toString().isEmpty() ? ", " : "") + stb.getId());
                exc = e;
            }
        }
        /** Let power be sent to all the stbs before failing if one of them failed */
        if (exc != null) {
            throw new PowerException("Failed to send power command '" + command + "' to " + failedIds.toString(), exc);
        }
    }

    protected PowerClient getPowerClient(MetaStb stb) {
        return new PowerClient(stb);
    }
}
