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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.comcast.drivethru.exception.HttpException;

import com.comcast.video.dawg.DawgClient;
import com.comcast.video.dawg.common.Config;
import com.comcast.video.dawg.common.DeviceProvider;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.exception.HttpRuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DawgPoundClient extends DawgClient implements IDawgPoundClient, DeviceProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DawgHouseClient.class);
    public static final String DEFAULT_BASE_URL = Config.get("dawg", "dawg-pound-reservations", "http://localhost:8080/dawg-pound/reservations");

    public DawgPoundClient() {
        this(DEFAULT_BASE_URL);
    }

    public DawgPoundClient(String baseURL) {
        super(fixBaseUrl(baseURL));
    }

    private static final String fixBaseUrl(String baseURL) {
        String url = baseURL + (baseURL.endsWith("/") ? "" : "/");
        return url + (url.endsWith("reservations/") ? "" : "reservations/");
    }

    /* (non-Javadoc)
     * @see com.comcast.video.dawg.house.IDawgPoundClient#getReservedDevices(java.lang.String)
     */
    @Override
    public Collection<MetaStb> getReservedDevices(String token) {
        List<MetaStb> devices = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object>[] maps = client.get("/reserved/token/"+token, Map[].class);
            for (Map<String,Object> map : maps) {
                devices.add(new MetaStb(map));
            }
        } catch (HttpException e) {
            e.printStackTrace();
            throw new HttpRuntimeException(e);
        }
        return devices;
    }

    @Override
    public void reserve(Reservation reservation) {
        String token = reservation.getToken();
        String id = reservation.getDeviceId();
        long expiration = reservation.getExpiration();
        String override = reservation.isForce() ? "/override" : "";
        String path = "/reserve" + override + "/" + token + "/" + id + "/" + expiration;
        try {
            boolean success = client.post(path, Boolean.class);
            if (!success) {
                String res = reservation.isForce() ? "" : " and is not reserved";
                String err = "Could not reserve '" + id + "' for '" + token + "', check that device exists" + res;
                throw new HttpRuntimeException(err);
            }
        } catch (HttpException e) {
            e.printStackTrace();
            throw new HttpRuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Reservation getReservation(String deviceId) {
        String path = "/reserved/id/" + deviceId;
        Map<String, String> map;
        try {
            map = client.get(path, Map.class);
        } catch (HttpException e) {
            e.printStackTrace();
            throw new HttpRuntimeException(e);
        }
        return new Reservation(deviceId, map.get("reserver"), Long.parseLong(map.get("expiration")), false);
    }

    @Override
    public Collection<MetaStb> getDevices(String token) {
        return getReservedDevices(token);
    }
}
