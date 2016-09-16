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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.comcast.cereal.CerealException;
import com.comcast.cereal.engines.JsonCerealEngine;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.exception.HttpRuntimeException;
import com.comcast.video.dawg.show.cache.MetaStbCache;
import com.comcast.video.dawg.show.key.Remote;
import com.comcast.video.dawg.show.key.RemoteManager;
import com.comcast.video.dawg.show.video.VideoSnap;
import com.comcast.video.dawg.util.DawgUtil;
import com.comcast.video.stbio.meta.Model;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for serving up views
 * @author Kevin Pearson
 *
 */
@Controller
public class ViewController implements ViewConstants {

    public static final Logger LOGGER = LoggerFactory.getLogger(ViewController.class);
    /** Represent generic remote name */
    private static final String GENERIC_REMOTE_NAME = "GENERIC";
    @Autowired
    private MetaStbCache metaStbCache;

    @Autowired
    private RemoteManager remoteManager;

    @Autowired
    private VideoSnap videoSnap;

    /** JSON engine. */
    private JsonCerealEngine jsonEngine = new JsonCerealEngine();

    /**
     * Serves up the standard view of the stb
     *
     * @param deviceId
     * @param mobile true if the device type is mobile.
     * @param remoteType the remote type to be selected
     * @param refresh true if the cache holding the devices should be cleared
     * @param uaStr The user agents string defining what browser and system the user is using
     * @return
     */

    @RequestMapping(method = { RequestMethod.GET }, value = "/stb")
    public ModelAndView stbView(@RequestParam String deviceId, @RequestParam(required=false) String mobile,
            @RequestParam(required = false) String remoteType,
            @RequestParam(required = false) String refresh,
            @RequestHeader("User-Agent") String uaStr) {
    	String stbViewType = STB;
    	return getStbView(deviceId, mobile, remoteType, refresh, uaStr, stbViewType);
    }

    @RequestMapping(method = { RequestMethod.GET }, value = "/simplified")
    public ModelAndView stbSimplifiedView(@RequestParam String deviceId, @RequestParam(required=false) String mobile,
            @RequestParam(required = false) String remoteType,
            @RequestParam(required = false) String refresh,
            @RequestHeader("User-Agent") String uaStr) {
    	String stbViewType = SIMPLIFIED;
    	return getStbView(deviceId, mobile, remoteType, refresh, uaStr, stbViewType);
    }

    public ModelAndView getStbView(String deviceId, String mobile, String remoteType, String refresh, String uaStr, String stbViewType){
        MetaStb stb = null;
        boolean ref = refresh == null ? false : Boolean.parseBoolean(refresh);
        try {
            stb = metaStbCache.getMetaStb(deviceId, ref);
        } catch (HttpRuntimeException e) {
            LOGGER.error("Failed to fetch the STB detail.", e);
        }

        ModelAndView mav;
        if (stb == null) {
            mav = new ModelAndView(NOSTB);
            mav.addObject(DEVICE_ID, deviceId);
        } else {
            boolean supported = BrowserSupport.isBrowserSupported(uaStr);
            String videoUrl = prependMissingProtocol(stb.getVideoSourceUrl(), "http://");
            String audioUrl = prependMissingProtocol(stb.getAudioUrl(), "http://");

            Boolean mob = false;

            if (mobile == null) {
                DeviceType deviceType = UserAgent.parseUserAgentString(uaStr).getOperatingSystem().getDeviceType();
                mob = deviceType.equals(DeviceType.MOBILE) || deviceType.equals(DeviceType.TABLET);
            } else {
                mob = Boolean.parseBoolean(mobile);
            }

            if(remoteType == null) {
                remoteType = stb.getRemoteType();
            }

            Remote remote = getRemote(remoteType);

            Set <String> remoteTypes = remoteManager.getRemoteTypes();
            mav = new ModelAndView(stbViewType);

            mav.addObject(DEVICE_ID, DawgUtil.toLowerAlphaNumeric(deviceId));
            try {
                mav.addObject(REMOTE_TYPES, jsonEngine.writeToString(remoteTypes, Set.class));
            } catch (CerealException exception) {
                LOGGER.error("Cereal exception while loading remote types", exception);
            }
            mav.addObject(STB_PARAM, stb);
            mav.addObject(REMOTE_MANAGER, remoteManager);
            mav.addObject(REMOTE, remote);
            mav.addObject(SELECTED_REMOTE_TYPE, remoteType);
            mav.addObject(MOBILE, mob);
            mav.addObject(VIDEO_URL, videoUrl);
            mav.addObject(VIDEO_CAMERA, stb.getVideoCamera());
            mav.addObject(VIDEO_AVAILABLE, validUrl(videoUrl) || validUrl(stb.getHdVideoUrl()));
            mav.addObject(HD_VIDEO_URL, stb.getHdVideoUrl());
            mav.addObject(TRACE_AVAILABLE, validUrl(stb.getSerialHost()));
            mav.addObject(TRACE_HOST, stb.getSerialHost());
            mav.addObject(IR_AVAILABLE, validUrl(stb.getIrServiceUrl()) && validUrl(stb.getIrServicePort()));
            mav.addObject(SUPPORTED, supported);
            mav.addObject(IPADDRESS, stb.getIpAddress().getHostName());
            mav.addObject(AUDIO_URL, audioUrl);
        }
        return mav;
    }

    /**
     * Check to see if the url has http:// or https:// as the protocol.  If not specifies, use the default
     *
     * @param url to make have https:// or http:// if it doesn't already exist
     * @param defaultProtocol string to prepend if http:// or https:// is not found
     */
    private String prependMissingProtocol(String url, String defaultProtocol) {
        String rv = url;
        if (    null != url
             && !(    url.startsWith("http://")
                   || url.startsWith("https://")))
       {
           rv = defaultProtocol + url;
       }
        return rv;
    }

    /**
     * Method to refresh the settop cache when the device metadata is edited.
     *
     * @param deviceIds
     *            Array of device Ids whose metadata is to be update in the
     *            cache.
     *
     */
    @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, value = {"/stb/refreshCache", "/simplified/refreshCache"})
    @ResponseBody
    public void refreshStbCache(@RequestParam String deviceIds[]) {
        for (String deviceId : deviceIds) {
            try {
                LOGGER.debug("About to refresh cache for device id : "
                        + deviceId);
                metaStbCache.getMetaStb(deviceId, true);
            } catch (HttpRuntimeException e) {
                LOGGER.error(
                        String.format(
                            "Failed to refresh the cache and fetch the STB detail for device id : %s.",
                            deviceId), e);
            }
        }
    }


    /**
     * Handler that will gives the valid remote types from RemoteManager.
     *
     */
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = "/remotetypes", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Set<String> getValidRemoteTypes() {
        return remoteManager.getRemoteTypes();
    }

    /**
     * Serves up the view that displays dawg-show for multiple stbs at one time
     * @param deviceIds The ids of the devices to display dawg-show for
     * @param refresh true if the cache holding the devices should be cleared
     * @param isGenericRemote true if using a generic remote
     * @param uaStr The user agents string defining what browser and system the user is using
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET}, value = "/multi")
    public ModelAndView multiView(@RequestParam String[] deviceIds, @RequestParam(required = false) String refresh,
            @RequestParam(required = false, defaultValue = "false") boolean isGenericRemote,
            @RequestHeader("User-Agent") String uaStr) {

        Collection<MetaStb> stbs = null;
        boolean ref = refresh == null ? false : Boolean.parseBoolean(refresh);
        try {
            stbs = metaStbCache.getMetaStbs(deviceIds, ref);
        } catch (HttpRuntimeException e) {
            LOGGER.error("Failed to fetch the STB detail.", e);
        }

        if (stbs == null) {
            /** No stbs found, redirect user to no stb page */
            ModelAndView mav = new ModelAndView(NOSTB);
            StringBuilder deviceStr = new StringBuilder();
            for (String deviceId : deviceIds) {
                deviceStr.append((!deviceStr.toString().isEmpty() ? ", " : "") + deviceId);
            }
            mav.addObject(DEVICE_ID, deviceStr.toString());
            return mav;
        }

        ModelAndView mav = new ModelAndView(MULTI);
        mav.addObject(STBS, stbs);

        /* Map containing remote types with corresponding count */
        Map<String, Integer> allUsedRemoteCounts = getAllUsedRemotes(stbs);

        Remote remote = isGenericRemote ? getRemote(GENERIC_REMOTE_NAME) : mostUsedRemote(allUsedRemoteCounts);
        mav.addObject(REMOTE, remote);

        if (isGenericRemote) {
            mav.addObject(GENERIC_REMOTE_KEYS, getKeysOfAllUsedRemotes(allUsedRemoteCounts));
        }


        mav.addObject(SUPPORTED, BrowserSupport.isBrowserSupported(uaStr));
        return mav;
    }

    /**
     * API to snap video in multiview, store it in the image cache and map to corresponding device id
     *
     * @param deviceIds
     *            the id of the devices to snap the video for
     *
     * @param session
     *            hold session details for the client
     *
     * @return the view of snapddownlaod page
     */
    @RequestMapping(method = {RequestMethod.GET}, value = "/video/multi/snap")
    @ResponseBody
    public ModelAndView snapMultiVideo(@RequestParam String[] deviceIds, HttpSession session) {
        String deviceIdImageIdJson = null;
        try {
            //JSON engine declared locally to avoid the "--class" key from the the serialized JSON output
            JsonCerealEngine jsonCerealEngine = new JsonCerealEngine();
            jsonCerealEngine.getSettings().setIncludeClassName(false);
            deviceIdImageIdJson = jsonCerealEngine.writeToString(getVideoSnap().getCaptureImageIds(deviceIds, session, metaStbCache));
        } catch (CerealException e) {
            LOGGER.error("Convertion of image id map to JSON got failed", e);
        }
        ModelAndView mav = new ModelAndView(SNAP_DOWNLOAD);
        mav.addObject(DEVICEID_IMAGEID_MAP, deviceIdImageIdJson);
        return mav;
    }

    /**
     * API to get zip with snapshots of devices in dawg-show multiview
     *
     * @param deviceIdImageIdMap
     *            map containing device ids with corresponding snapshot image ids
     * @param refresh
     *            true if the cached stb should be refreshed
     * @param response
     *            http response
     * @param session
     *            hold session details for the client
     * @return zip with snapshots of devices in dawg-show multiview
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(method = {RequestMethod.GET}, value = "/snap/multi", produces = "application/zip")
    public ModelAndView multiSnap(@RequestParam Map<String, String> deviceIdImageIdMap,
            @RequestParam(required = false) String refresh, HttpServletResponse response, HttpSession session) {

        String[] deviceIds = deviceIdImageIdMap.keySet().toArray(new String[deviceIdImageIdMap.keySet().size()]);
        String[] imageIds = deviceIdImageIdMap.values().toArray(new String[deviceIdImageIdMap.values().size()]);;

        Collection<MetaStb> stbs = null;
        boolean ref = refresh == null ? false : Boolean.parseBoolean(refresh);
        try {
            stbs = metaStbCache.getMetaStbs(deviceIds, ref);
        } catch (HttpRuntimeException e) {
            LOGGER.error("Failed to fetch the STB detail.", e);
        }

        if (stbs == null) {
            /** No stbs found, redirect user to no stb page */
            ModelAndView mav = new ModelAndView(NOSTB);
            StringBuilder deviceStr = new StringBuilder();
            for (String deviceId : deviceIds) {
                deviceStr.append((!deviceStr.toString().isEmpty() ? ", " : "") + deviceId);
            }
            mav.addObject(DEVICE_ID, deviceStr.toString());
            return mav;
        } else {
            getVideoSnap().addImagesToZipFile(imageIds, deviceIds, response, session);
        }

        return null;
    }

    /**
     * Helper method that looks through all the stbs and determines what remote type is used most by the given stbs.
     * This is the remote that will be displayed for multi-show.
     *
     * @param allUsedRemoteCounts
     *            the map of all used remote types with corresponding count in dawg-show multi-view
     * @return most used remote in the given stbs.
     */
    private Remote mostUsedRemote(Map<String, Integer> allUsedRemoteCounts) {
        /* Stores the highest count of remote */
        int max = Integer.MIN_VALUE;
        String maxRemote = null;
        for (String remote : allUsedRemoteCounts.keySet()) {
            Integer count = allUsedRemoteCounts.get(remote);
            if (count > max) {
                max = count;
                maxRemote = remote;
            }
        }
        return getRemote(maxRemote);
    }

    /**
     * Method that looks through all the stbs and determines what remote type is used by the given stbs and returns map
     * of all used remote types with corresponding count in dawg-show multi-view
     *
     * @param stbs
     *            the collection of settops to look through
     * @return a map containing remote types with corresponding count in dawg show multi- view
     */
    private Map<String, Integer> getAllUsedRemotes(Collection<MetaStb> stbs) {
        /* Map containing remote types with corresponding count */
        Map<String, Integer> remoteCounts = new HashMap<String, Integer>();
        String remote = null;
        Integer count = null;
        for (MetaStb stb : stbs) {
            remote = stb.getRemoteType();
            count = remoteCounts.get(remote);
            count = (count == null) ? 0 : count;
            remoteCounts.put(remote, ++count);
        }
        return remoteCounts;
    }

    /**
     * Method that looks through all the stbs and determines the name of keys used in all remote in dawg-show multi-view
     * to represent generic remote.
     *
     * @param allUsedRemoteCounts
     *            the map of all used remote types with corresponding count in dawg-show multi-view
     * @return the name of keys used in all remote in dawg-show multi-view.
     */
    private String getKeysOfAllUsedRemotes(Map<String, Integer> allUsedRemoteCounts) {
        Set<String> keySetsList = new HashSet<String>();
        for (String remoteType : allUsedRemoteCounts.keySet()) {
            Set<String> keySet = remoteManager.getKeySet(remoteType);
            if (keySet != null) {
                keySetsList.addAll(keySet);
            }
        }

        String json = null;
        try {
            json = jsonEngine.writeToString(keySetsList, Set.class);
        } catch (CerealException exception) {
            LOGGER.error("Cereal exception while getting keys of used remotes", exception);
        }
        return json;
    }

    /**
     * Helper method to create a remote for the given remote type or if the remote doesn't exist, return the default
     *
     * @param remoteType
     *            The remote type for which remote object needs to be create
     * @return the remote object for the given remote type
     */
    private Remote getRemote(String remoteType) {
        Remote remote = remoteManager.getRemote(remoteType);
        return remote == null ? remoteManager.getRemote(RemoteManager.DEFAULT_REMOTE_TYPE) : remote;
    }

    private boolean validUrl(String url) {
        return url != null && !url.isEmpty();
    }

    /**
     * Method for creating a VideoSnap instance
     * @return viedoSnap instance
     */
    protected VideoSnap getVideoSnap() {
        return videoSnap;
    }

}
