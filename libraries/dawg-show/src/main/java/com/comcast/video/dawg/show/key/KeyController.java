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
package com.comcast.video.dawg.show.key;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

import com.comcast.video.dawg.common.security.jwt.DawgCookieUtils;
import com.comcast.video.dawg.common.security.jwt.JwtDeviceAccessValidator;
import com.comcast.video.dawg.show.ViewConstants;
import com.comcast.video.dawg.show.cache.MetaStbCache;
import com.comcast.video.dawg.show.plugins.RemotePluginManager;
import com.comcast.video.stbio.Key;
import com.comcast.video.stbio.exceptions.KeyException;

/**
 * Controller for handling sending keys
 * @author Kevin Pearson
 *
 */
@Controller
public class KeyController implements ViewConstants {
    @Autowired
    private MetaStbCache metaStbCache;
    @Autowired
    private RemotePluginManager remotePluginManager;

    @Autowired
    private JwtDeviceAccessValidator accessValidator;
    
    private DawgCookieUtils cookieUtils = new DawgCookieUtils();

    @RequestMapping(method = { RequestMethod.POST }, value = "/sendKey")
    @ResponseBody
    public void sendKey(HttpServletRequest req, HttpServletResponse resp,
            @RequestParam(value="deviceIds[]") String[] deviceIds, @RequestParam String key, @RequestParam(required = false) String remoteType,
            @CookieValue(name="dawt", required=false) String dawt) throws KeyException, InterruptedException {
        sendKey(req, resp, deviceIds, key, null, remoteType, dawt);
    }


    @RequestMapping(method = {RequestMethod.POST }, value = "/holdKey")
    @ResponseBody
    public void holdKey(HttpServletRequest req, HttpServletResponse resp, 
            @RequestParam(value="deviceIds[]") String[] deviceIds, @RequestParam String key, @RequestParam String holdTime, @RequestParam(required = false) String remoteType,
            @CookieValue(name="dawt", required=false) String dawt) throws KeyException, InterruptedException {
        sendKey(req, resp, deviceIds, key, holdTime, remoteType, dawt);
    }

    /**
     * Helper method for sending a key or holding a key
     * @param deviceIds The deviceIds to send the key to
     * @param key The key to send
     * @param holdTime The time to hold the key, null if not holding the key
     * @param remoteType The current remote type
     * @throws KeyException Exception that happened when sending the key
     * @throws InterruptedException
     *
     */
    private void sendKey(HttpServletRequest req, HttpServletResponse resp, 
            String[] deviceIds, String key, String holdTime, String remoteType, String dawt) throws KeyException, InterruptedException {
        Key[] keys = new Key[] {Key.valueOf(key)};
        sendKeys (req, resp, deviceIds, keys, holdTime, remoteType, dawt);
    }

    /**
     * Sends a set of keys to specified devices
     * @param deviceIds The deviceIds to send the key to
     * @param keys The key to send
     * @param holdTime The time to hold the key, null if not holding the key
     * @param remoteType The current remote type
     * @throws KeyException Exception that happened when sending the key
     * @throws InterruptedException If any thread has interrupted the current thread.
     *
     */
    private void sendKeys(HttpServletRequest req, HttpServletResponse resp, String[] deviceIds, Key[] keys, String holdTime, String remoteType, String dawt) throws KeyException, InterruptedException {
        accessValidator.validateUserHasAccessToDevices(req, resp, false, deviceIds);
        StringBuilder failedIds = new StringBuilder();
        KeyException exc = null;
        /** Send the key to all the devices in parallel */
        Collection<SendKeyThread> threads = new ArrayList<SendKeyThread>();
        Cookie cookie = dawt == null ? null : cookieUtils.createApacheCookie(dawt, -1, req);
        for (String deviceId : deviceIds) {
            SendKeyThread keyThread = createSendKeyThread(deviceId, keys, holdTime, remotePluginManager, remoteType, cookie);
            threads.add(keyThread);
            keyThread.start();
        }

        for (SendKeyThread keyThread : threads) {
            keyThread.join();
            exc = keyThread.getExc();
            if (exc != null) {
                failedIds.append((!failedIds.toString().isEmpty() ? ", " : "") + keyThread.getDeviceId());
            }
        }
        /** Let the key be sent to all the stbs before failing if one of them failed */
        if (exc != null) {
            throw new KeyException("Failed to send '" + Arrays.asList(keys) + " to " + failedIds.toString(), exc);
        }
    }


    /**
     * Controller method for direct tuning to a specific channel
     * @param deviceIds The deviceIds to send the key to
     * @param key The key to send
     * @param holdTime The time to hold the key, null if not holding the key
     * @param remoteType The current remote type
     * @throws KeyException Exception that happened when sending the key
     * @throws InterruptedException If any thread has interrupted the current thread.
     *
     */
    @RequestMapping(method = { RequestMethod.POST }, value = "/directtune")
    @ResponseBody
    public void directTune(HttpServletRequest req, HttpServletResponse resp,
            @RequestParam(value = "deviceIds[]") String[] deviceIds,
            @RequestParam String channelNum,
            @RequestParam(required = false) String remoteType,
            @CookieValue(name="dawt", required=false) String dawt)
        throws KeyException, InterruptedException {
        Key[] keys = Key.keysForChannel(channelNum);
        sendKeys(req, resp, deviceIds, keys, null, remoteType, dawt);
    }


    protected SendKeyThread createSendKeyThread(String deviceId, Key[] keys, String holdTime, RemotePluginManager remotePluginManager, String remoteType, Cookie cookie) {
        return new SendKeyThread(deviceId, keys, holdTime, metaStbCache, remotePluginManager, remoteType, cookie);
    }
}
