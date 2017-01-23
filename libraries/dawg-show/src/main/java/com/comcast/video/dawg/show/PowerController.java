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

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comcast.video.dawg.cats.power.PowerClient;
import com.comcast.video.dawg.cats.power.PowerOperation;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.security.jwt.DawgCookieUtils;
import com.comcast.video.dawg.common.security.jwt.JwtDeviceAccessValidator;
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

    @Autowired
    private DawgShowConfiguration config;

    @Autowired
    private JwtDeviceAccessValidator accessValidator;
    
    private DawgCookieUtils cookieUtils = new DawgCookieUtils();

    /**
     * Sends the given power command
     * @param deviceId The id of the device to send the power command to
     * @param command The power command to send. See {@link PowerOperation}
     * @return
     * @throws PowerException
     */
    @RequestMapping(method = { RequestMethod.POST }, value = "/power")
    @ResponseBody
    public void power(@RequestParam(value="deviceIds[]") String[] deviceIds, @RequestParam String command,
            HttpServletRequest req, HttpServletResponse resp, @CookieValue(name=DawgCookieUtils.COOKIE_NAME, required=false) String dawt) throws PowerException {
        accessValidator.validateUserHasAccessToDevices(req, resp, false, deviceIds);
        Cookie cookie = dawt == null ? null : cookieUtils.createApacheCookie(dawt, -1, req);
        PowerException exc = null;
        StringBuilder failedIds = new StringBuilder();
        for (String deviceId : deviceIds) {
            MetaStb stb = metaStbCache.getMetaStb(deviceId);
            try {
                PowerClient client = getPowerClient(stb, cookie);

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

    protected PowerClient getPowerClient(MetaStb stb, Cookie authCookie) {
        MetaStb meta = new MetaStb(new HashMap<String, Object>(stb.getData())); // make a copy so we can set some defaults
        if (!meta.getData().containsKey(MetaStb.RACK_PROXY_ENABLED)) {
            meta.setRackProxyEnabled(config.getRackProxyEnabledDefault());
        }
        if (!meta.getData().containsKey(MetaStb.RACK_PROXY_URL)) {
            meta.setRackProxyUrl(config.getRackProxyUrlDefault());
        }
        PowerClient pc = new PowerClient(meta);
        if (authCookie != null) {
            pc.getCookieStore().addCookie(authCookie);
        }
        return pc;
    }
}
