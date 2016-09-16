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
package com.comcast.video.dawg.controller.house;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comcast.drivethru.exception.HttpException;
import com.comcast.video.dawg.PopulatingClient;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.PersistableDevice;
import com.comcast.video.dawg.common.ServerUtils;
import com.comcast.video.dawg.common.exceptions.DawgIllegalArgumentException;
import com.comcast.video.dawg.common.exceptions.DawgNotFoundException;
import com.comcast.video.dawg.service.house.HouseService;
import com.comcast.video.dawg.show.DawgShowClient;

/**
 * The {@link HouseRestController} defines the public REST API for Dawg House,
 * the device indexing system.  For more details on DAWG, see
 * TODO: Add new documentation location
 *
 * These methods are exposed at:
 * http://\<host\>:\<port\>/dawg-house/
 *
 * Please note that <code>/devices/</code> is prefixed to all RequestMappings for the individual methods.
 *
 * So the effect contextPath for this interface is:
 * http://\<host\>:\<port\>/dawg-house/devices/
 *
 * When an id parameter is required, it typically corresponds to the MAC Address
 * of a device, though it is an arbitrary string specified when the device is added to DAWG
 *
 * For consistency, the server will trim, lower case and remove all
 * non-alphanumeric characters from ids passed into methods.
 *
 * @author Val Apgar
 *
 */
@Controller
@RequestMapping(value="devices")
public class HouseRestController {

    Logger logger = Logger.getLogger(HouseRestController.class);

    @Autowired
    HouseService service;
    @Autowired(required=false)
    PopulatingClient client;
    @Autowired
    ServerUtils serverUtils;
    /**{@link DawgHouseConfiguration} read from the property file.*/
    @Autowired
    private DawgHouseConfiguration config;
    /**
     * List all the devices in the data store
     *
     * @return An array of map objects where each map represents a device in the data store
     */
    @RequestMapping(value="list*", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object>[] listAll() {
        return service.getAll();
    }

    /**
     * Get a specific devices information by its DAWG deviceID
     *
     * @param id the DAWG deviceId of the device.
     * @return a Map representing the device matching the provided DAWG deviceId
     */
    @RequestMapping(value="id/{id}", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> getStbsById(@PathVariable String id) {
        id = clean(id);
        Map<String,Object> device = service.getById(id);
        if (null != device) {
            return device;
        } else {
            throw new DawgNotFoundException("No existing device for id="+id);
        }
    }

    public static final String[] ignoreProps = new String[] { "--class",
        PersistableDevice.RESERVER, PersistableDevice.EXPIRATION_KEY,
        MetaStb.CATSSERVERHOST,MetaStb.MACADDRESS, MetaStb.ID, MetaStb.MODIFIED_PROPS,
        MetaStb.CATSKEYSETMAPPING
    };

    /**
     * Gets properties for the given stb that can be edited. Will also sort the properties.
     * @param id The id of the device to get editable properties for
     * @return
     */
    @RequestMapping(value="edit/{id}", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> editStb(@PathVariable String id) {
        MetaStb stb = new MetaStb(getStbsById(id));
        Set<String> modifiedProps = stb.getModifiedProps();
        Map<String, Object> data = stb.getData();
        for (String iProp : ignoreProps) {
            data.remove(iProp);
        }
        Map<String, Object> sortedProps = new TreeMap<String, Object>(new MetaStbPropComparator());
        for (String key : data.keySet()) {
            boolean modified = modifiedProps != null ? modifiedProps.contains(key) : false;
            sortedProps.put(key, new PropAndModified(data.get(key), modified));
        }

        return sortedProps;
    }

    /**
     * Delete a specific device by its DAWG deviceId
     *
     * @param id the DAWG deviceId of the device.
     */
    @RequestMapping(value="id/{id}", method={RequestMethod.DELETE})
    @ResponseBody
    public void deleteSingle(@PathVariable String id) {
        id = clean(id);
        try {
            service.deleteStbById(id);
        } catch (Exception e) {
            throw new DawgIllegalArgumentException("Unable to delete device by id="+id);
        }
    }

    /**
     * Add a new device with a specific DAWG deviceId
     *
     * @param id the DAWG deviceId of the device.
     * @param data a Map<String,Object> representation of the device.  Typically this should contain the keys from {@link MetaStb}
     */
    @RequestMapping(value="id/{id}", method = {RequestMethod.PUT})
    @ResponseBody
    public void add(@PathVariable String id, @RequestBody Map<String,Object> data) {
        id = clean(id);
        try {
            data.put(MetaStb.ID, id);
            service.upsertStb(new PersistableDevice(data));
        } catch (Exception e) {
            throw new DawgIllegalArgumentException("Could not insert the specified device with \n id="+id+",\n data="+data);
        }
    }

    /**
     * Get a batch of devices by a set of DAWG deviceIds
     *
     * @param id An array of DAWG deviceIds to return the corresponding devices from
     * @return An array of Maps representing the devices matching the provided DAWG deviceIds.
     */
    @RequestMapping(value="id", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String,Object>[] getStbsByIds(@RequestParam String[] id) {
        for (int i = 0; i < id.length; i++) {
            id[i] = clean(id[i]);
        }
        Map<String,Object>[] devices = service.getStbsById(id);
        if (null == devices || 0 == devices.length) {
            throw new DawgNotFoundException("No devices were found with the specified id, id="+Arrays.asList(id));
        } else {
            return devices;
        }
    }

    /**
     * Update a set of devices by id with the corresponding data.
     * The server will attempt to retrieve existing devices by id and apply the provided
     * key value pairs into those devices.  If no device exists for the device, nothing will happen.
     *
     * TODO: This method needs serious work.
     *
     * @param id An array of DAWG deviceIds to update
     * @param data A Map of key value pairs.
     * @return
     */
    @RequestMapping(value="update", method = {RequestMethod.POST})
    @ResponseBody
    public int update(@RequestParam String[] id, @RequestBody Map<String,Object> data,
            @RequestParam(defaultValue="false") boolean replace) {
        int counter = 0;
        for (int i = 0; i < id.length; i++) {
            id[i] = clean(id[i]);
        }
        Map<String, Object>[] matches = service.getStbsById(id);
        if (null != matches) {
            for (Map<String, Object> match : matches) {
                Map<String, Object> newProps = new HashMap<String, Object>(data);
                try {
                    /** Do not override id and mac address */
                    newProps.put(MetaStb.ID, match.get(MetaStb.ID));
                    newProps.put(MetaStb.MACADDRESS, match.get(MetaStb.MACADDRESS));
                    if (!newProps.containsKey(MetaStb.CATSSERVERHOST)) {
                        newProps.put(MetaStb.CATSSERVERHOST, match.get(MetaStb.CATSSERVERHOST));
                    }
                    if (!newProps.containsKey(MetaStb.CATSKEYSETMAPPING)) {
                        newProps.put(MetaStb.CATSKEYSETMAPPING, match.get(MetaStb.CATSKEYSETMAPPING));
                    }
                    PersistableDevice pd = new PersistableDevice(newProps);
                    if (replace) {
                        service.replace(pd);
                    } else {
                        service.upsertStb(pd);
                    }
                    counter++;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn("Failed to update device.  deviceId="+match.get("id")+", data="+match);
                }
            }
            try {
                getDawgShowClient().updateCache(id);
            } catch (HttpException e) {
                logger.error("Failed to update DAWG show meta stb cache", e);
            }
        }
        return counter;
    }

    /**
     * Get the valid remote types from Dawg-show using DawgShowClient
     * @return A list of valid remote types
     */
    @RequestMapping(value="remotetypes", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<String> getRemoteTypes() {
        return getDawgShowClient().getRemoteTypes();
    }

    /**
     * Update a set of devices by DAWG deviceId with the corresponding data.
     * The server will attempt to retrieve existing devices by deviceId and apply the provided
     * key value pairs into those devices.  If no device exists for the device, nothing will happen.
     *
     * @param payload An array of DAWG deviceIds to update
     * @param op A Map of key value pairs.
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    @RequestMapping(value="update/tags/{op}", method = {RequestMethod.POST})
    @ResponseBody
    public void updateTags(@RequestBody Map<String,Object> payload, @PathVariable("op") String op) {

        List<String> id = (List<String>) payload.get("id");
        List<String> tag = (List<String>) payload.get("tag");
        boolean add = op.equals("add");

        if (null == id || id.size() < 1) {
            throw new DawgIllegalArgumentException("Could not update tags.  No ids were provided.");
        }

        if (null == tag || tag.size() < 1) {
            throw new DawgIllegalArgumentException("Could not update tags.  No tags were provided.");
        }

        tag = validateTags(tag);

        List<String> validIds = new ArrayList<String>();

        // Cleanup the ids
        for(String i : id) {
            try {
                validIds.add(clean(i));
            } catch (Exception e) {
                logger.warn("Updating tags, id unparsable. Id="+i);
            }
        }

        if (validIds.size() < 1) {
            throw new DawgIllegalArgumentException("Could not update tags.  None of the provided ids were valid.");
        }

        // find all the data matching the device ids
        Map<String, Object>[] matches = service.getStbsById(validIds.toArray(new String[validIds.size()]));

        if (null == matches || matches.length < 1) {
            throw new DawgIllegalArgumentException("Could not update tags.  None of the provided existed.");
        }

        // update the tags
        List<PersistableDevice> devicesToUpdate = new ArrayList<PersistableDevice>();
        for (Map<String, Object> match : matches) {
            if (null != match) {
                try {
                    Set<String> updated = new HashSet<String>();
                    if (match.containsKey("tags")) {
                        updated.addAll((Collection) match.get("tags"));
                    }
                    if (add) {
                        updated.addAll(tag);
                    } else {
                        updated.removeAll(tag);
                    }
                    match.put("tags", updated);
                    devicesToUpdate.add(new PersistableDevice(match));

                } catch (Exception e) {
                    logger.warn("Failed to update device.  deviceId="+match.get("id")+", data="+match,e);
                }
            }
        }
        service.upsertStb(devicesToUpdate.toArray(new PersistableDevice[devicesToUpdate.size()]));
    }

    // TODO: stubbed out for future development
    private List<String> validateTags(List<String> tags) {
        return tags;
    }

    /**
     * Get devices by searching with a query.
     *
     * TODO: The implementation details of this query need to be determined.
     *
     * @param q the query.  Currently the query is an inline AND operation.  For example, is q=this,that your query will be this AND that
     * @return An array of Maps corresponding to the devices that were found using the provided query.
     */
    @RequestMapping(value="query", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String,Object>[] getStbsByQuery(@RequestParam String q) {
        return service.getStbsByQuery(q.toLowerCase());
    }

    /**
     * Get all devices which are matching with the provided tag.
     *
     * @param tag the tag to be searched
     * @param q the query need to check along with tag specified
     * @return an array of devices that were found using the provided tag and query.
     */
    @RequestMapping(value = "tag/{tag}", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Map<String, Object>[] getStbsByTag(@PathVariable String tag, @RequestParam(required = false) String q) {
        return service.getStbsByTag(tag, q);
    }

    /**
     * Batch deletion of devices by deviceId
     *
     * @param id An array of DAWG deviceIds to delete. <b>Each device with the matching id will be deleted</b>
     */
    @RequestMapping(value="delete", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public void deleteStb(@RequestParam String[] id) {
        for (int i = 0; i < id.length; i++) {
            id[i] = clean(id[i]);
        }
        service.deleteStbById(id);
    }

    /**
     * Scrape for boxes reserved but not allocated by the specified token
     * and add new entries into the data store for each device that was found.
     *
     * @param token The token to scrape device information from the client.
     * @param isToOverrideMetaData if true, the client meta data will override DAWG meta data ignoring the modified properties in DAWG.
     * @return int The number of devices that were entered into the data store.
     */
    @RequestMapping(value="populate/{token}", method=RequestMethod.POST)
    @ResponseBody
    public int populate(@PathVariable String token,
            @RequestParam(required = false, defaultValue = "false", value = "isToOverride") Boolean isToOverrideMetaData) {
        int counter = 0;
        try {
            List<MetaStb> stbs;
            if(null != client)
                stbs = client.populate(token);
            else
                stbs = new LinkedList<MetaStb>();
            //List<MetaStb> stbs = client.populate(token);
            /** Gather all deviceIds of all devices to upsert */
            String[] ids = new String[stbs.size()];
            for(int i = 0; i < stbs.size(); i++) {
                ids[i] = stbs.get(i).getId();
            }

            /** Create a mapping of deviceId to the existing device in dawg-house */
            Map<String, Object>[] existingStbs = service.getStbsById(ids);
            Map<String, MetaStb> existingStbMap = new HashMap<String, MetaStb>();
            for (Map<String, Object> data : existingStbs) {
                MetaStb stb = new MetaStb(data);
                existingStbMap.put(stb.getId(), stb);
            }

            /** Loop over all stbs from the client
             * If there is an existing stb we need to look at which properties are
             * purposely overridden in dawg-house. They must not change with any new values
             * from client.
             */
            for(MetaStb stb : stbs) {
                MetaStb existing = existingStbMap.get(stb.getId());
                MetaStb stbToUpsert;
                if (existing == null) {
                    stbToUpsert = stb;
                } else {
                    Map<String, Object> newData = stb.getData();
                    if (!isToOverrideMetaData) {
                        Set<String> modifiedProps = existing.getModifiedProps();
                        for (String key : newData.keySet()) {
                            if ((modifiedProps == null) || !modifiedProps.contains(key)) {
                                existing.getData().put(key, newData.get(key));
                            }
                        }
                    } else {
                        for (String key : newData.keySet()) {
                            existing.getData().put(key, newData.get(key));
                        }
                        existing.setModifiedProps(null);
                    }
                    stbToUpsert = existing;
                }
                try {
                    service.upsertStb(stbToUpsert);
                    counter++;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn("Could not load into MetaStb=" + stb.toString() + ", Token="+token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Could not load anything from the PopulatingClient, Token=" + token);
        }
        return counter;
    }

    /**
     * Get the {@link DawgShowClient} for updating dawg-show properties from the
     * dawg-house.
     *
     * @return {@link DawgShowClient}
     */
    protected DawgShowClient getDawgShowClient() {
        return new DawgShowClient(config.getDawgShowUrl());
    }

    protected String clean(String string) {
        return serverUtils.clean(string);
    }
}
