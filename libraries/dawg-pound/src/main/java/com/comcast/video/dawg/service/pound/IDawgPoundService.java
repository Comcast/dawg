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
package com.comcast.video.dawg.service.pound;

import java.util.Map;

import com.comcast.video.dawg.common.DawgDevice;

/**
 * The {@link IDawgPoundService} describes all the methods for interacting with
 * the reservation data store.
 *
 * @author Val Apgar
 *
 */
public interface IDawgPoundService {

    /**
     * @param token
     *            The identifier for a set of reserved devices
     * @return a array of devices that are associated with the provided
     *         token
     */
    public Map<String,Object>[] getByReserver(String token);

    /**
     * Reserve is used to attempt to reserve a device by id for a specific token
     * until the expiration time. This method will not break an existing
     * reservation.
     *
     * @param deviceid
     *            The identifier of a particular device
     * @param token
     *            The token to associate with the device
     * @param expiration
     *            When the token should expire, in unix time.
     * @return If the device does not exist, returns null. Otherwise if the
     *         device is reserved, returns the token of the current reserver. If
     *         the device is unreserved, returns the token of the new reserver.
     */
    public String reserve(String deviceid, String token, long expiration);

    /**
     * Unreserve a device by the device's id. The reservation will be cleared
     * and if a previous reservation token was associated with the device, that
     * token will be returned.
     *
     * @param id
     *            the id of the device.
     * @return If the device does not exist, null is returned. If the device
     *         exists and was not reserved, null is returned. If the device
     *         exists and had a reservation token, the reservation token is
     *         returned.
     */
    public String unreserve(String id);

    /**
     * overrideReserve is used to disregard any existing reservations and create
     * a new reservation.
     *
     * @param id
     *            The id of the device
     * @param token
     *            The new token to place on the device.
     * @param expiration
     *            When the token should expire. In millisecond unix time.
     * @return return true if the device existed and the reservation was
     *         created. Otherwise false.
     */
    public boolean overrideReserve(String id, String token, long expiration);

    /**
     * Query the datastore to determine if a device is reserved and return the
     * details of the reservation.
     * @param The id of the device
     * @return
     *
     * <pre>
     * {
     *   "reserver":<token>,
     *   "expiration":<unix timestamp>
     * }
     * </pre>
     *
     * If the device does not exist or is not reserved, null is returned.
     */
    public Map<String,String> isReserved(String id);

    /**
     * getById exists as a utility method for easily gaining access to the
     * devices in the datastore. This does not need to be exposed at the
     * interface layer, but is for simplicity.
     *
     * @param The
     *            device's id
     * @return the device if it exists, otherwise null.
     */
    public DawgDevice getById(String id);
}
