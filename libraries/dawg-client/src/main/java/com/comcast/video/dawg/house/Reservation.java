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

/**
 * Represents a reservation in dawg-pound
 * @author Kevin Pearson
 *
 */
public class Reservation {
    public static final long DEFAULT_EXPIRATION = -1;
    public static final boolean DEFAULT_FORCE = false;

    private String deviceId;
    private String token;
    private long expiration;
    private boolean force;

    /**
     * Creates a Reserveration
     * @param deviceId The id of the device that is reserved
     * @param token The token that has reserved the device
     */
    public Reservation(String deviceId, String token) {
        this(deviceId, token, DEFAULT_EXPIRATION, DEFAULT_FORCE);
    }

    /**
     * Creates a Reserveration
     * @param deviceId The id of the device that is reserved
     * @param token The token that has reserved the device
     * @param expiration The unix time in milliseconds when the reservation will expire
     * @param force true if the reservation should override the existing
     */
    public Reservation(String deviceId, String token, long expiration,
            boolean force) {
        this.deviceId = deviceId;
        this.token = token;
        this.expiration = expiration;
        this.force = force;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }
}
