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
package com.comcast.video.dawg.house;

import java.util.Collection;

import com.comcast.video.dawg.common.MetaStb;

/**
 * Interface defining methods for communicating with dawg-pound.
 * @author Val Apgar
 *
 */
public interface IDawgPoundClient {

    /**
     * Return all MetaStb objects reserved by the specified token.
     */
    public Collection<MetaStb> getReservedDevices(String token);

    /**
     * Reserves a device using values inside the request object
     * @param reservation The object that holds all the information necessary to make a reservation
     */
    void reserve(Reservation reservation);

    Reservation getReservation(String deviceId);
}
