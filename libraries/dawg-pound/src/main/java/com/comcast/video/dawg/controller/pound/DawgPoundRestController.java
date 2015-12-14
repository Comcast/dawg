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
package com.comcast.video.dawg.controller.pound;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comcast.video.dawg.common.DawgDevice;
import com.comcast.video.dawg.common.ServerUtils;
import com.comcast.video.dawg.service.pound.DawgPoundMongoService;
import com.comcast.video.dawg.service.pound.IDawgPoundService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DawgPoundRestController} is the public REST API for Dawg Pound, the
 * reservation system used by Dawg.  For more details on DAWG, see
 * TODO add documentation
 *
 * These methods are exposed at:
 * http://\<host\>:\<port\>/dawg-pound/
 *
 * Please note that <code>/reservations/</code> is prefixed to all RequestMappings for the individual methods.
 *
 * So the effect contextPath for this interface is:
 * http://\<host\>:\<port\>/dawg-pound/reservations/
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
@RequestMapping(value = "reservations")
public class DawgPoundRestController {

    @Autowired
    IDawgPoundService service;
    @Autowired
    ServerUtils serverUtils;

    /** Log4j logger for {@link DawgPoundRestController} class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgPoundRestController.class);

    /**
     * Gets reserved Dawg deviceIDs for a given Dawg userId (aka token)
     *
     * @param token
     *            the identifier of the reservation holder.
     * @return An Array of {@link DawgDevice}s with the associated reservation
     *         token.
     */
    @RequestMapping(value = "reserved/token/{token}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object>[] getReservered(@PathVariable String token) {
        token = clean(token);
        LOGGER.info("Request for reserved devices for token="+token);
        return service.getByReserver(token);
    }

    /**
     * Unreserves a given deviceID from any associated reservations
     *
     * @param id
     *            the deviceId of the device to unreserve
     * @return The value of the token associated with the reservation at the
     *         time of unreserving or null if the device did not exist or was
     *         not reserved.
     */
    @RequestMapping(value = "unreserve/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String unreserve(@PathVariable String id) {
        id = clean(id);
        LOGGER.info("Request to unreserve device for id="+id);
        return service.unreserve(id);
    }

    /**
     * Reserves a given DAWG deviceID for a given DAWG userID with an expiration date
     *
     * @param token
     *            The token to identify the reservation holder
     * @param id
     *            The id of the device to reserve
     * @param expiration
     *            the date as a unix timestamp in milliseconds when the
     *            reservation should expire
     * @return If the device does not exist, returns null. Otherwise if the device is reserved, returns the token of the current reserver.
     *         If the device is unreserved, returns the token of the new reserver.
     */
    @RequestMapping(value = "reserve/{token}/{id}/{expiration}", method = RequestMethod.POST)
    @ResponseBody
    public String reservePolitely(@PathVariable String token,
            @PathVariable String id, @PathVariable String expiration) {
        token = clean(token);
        id = clean(id);
        LOGGER.info("Request to reserve a device for token="+token+", id="+id+", expiration="+expiration);
        return service.reserve(id, token, Long.valueOf(expiration));
    }

    /**
     * Steals a currently reserved deviceID and reserves it for the specified userID
     *
     * @param token
     *            The token to identify the reservation holder
     * @param id
     *            The id of the device to reserve
     * @param expiration
     *            the date as a unix timestamp in milliseconds when the
     *            reservation should expire
     * @return false if the device does not exist or the reservation cannot be
     *         made. Otherwise true. This will override any existing
     *         reservation.
     */
    @RequestMapping(value = "reserve/override/{token}/{id}/{expiration}", method = RequestMethod.POST)
    @ResponseBody
    public boolean reserve(@PathVariable String token, @PathVariable String id,
            @PathVariable String expiration) {
        token = clean(token);
        id = clean(id);
        LOGGER.info("Request to override reservation for a device for token="+token+", id="+id+", expiration="+expiration);
        return service.overrideReserve(id, token, Long.valueOf(expiration));
    }

    /**
     * Checks the reservation status of a given deviceID
     *
     * @param id
     *            the device id of the device to query
     * @return details of the reservation as a map in the format:
     *
     *         <pre>
     * {
     *   "reserver":"<token>",
     *   "expiration":"<unix time stamp>"
     * }
     * If the device is not reserved or does not exist, null is returned.
     * </pre>
     */
    @RequestMapping(value = "reserved/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> isReservered(@PathVariable String id) {
        id = clean(id);
        LOGGER.info("Request to check reservation for id="+id);
        return service.isReserved(id);
    }

    /**
     * Lists all devices in the datastore
     *
     * @return an Array of {@link DawgDevice} of every device in the datastore.
     *         This method is purely for administrative purposes and can be
     *         ignored for general operations.
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object>[] listAll() {
        DawgPoundMongoService mongo = (DawgPoundMongoService) service;
        LOGGER.info("Request to list all devices in the datastore.");
        return mongo.list();
    }

    private String clean(String string) {
        return serverUtils.clean(string);
    }
}
