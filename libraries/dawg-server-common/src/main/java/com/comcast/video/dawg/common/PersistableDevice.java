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
package com.comcast.video.dawg.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * This wrapper of MetaStb exists to create an indexed array of strings that
 * will facilitate searching the database. Rather than looking for keywords in
 * an entire document, this will allow the keys afield to be searched for
 * arbitrary things.
 *
 * @author Val Apgar
 *
 */
@Document
public class PersistableDevice implements ReservationHandler, DawgDevice {

    public static final String RESERVER = "reserver_key";
    public static final String EXPIRATION_KEY = "expiration_key";

    @Transient
    protected MapScanner scanner = MapScanner.instance();
    @Transient
    protected static ServerUtils serverUtils = new ServerUtils();

    @Id
    String id;
    Map<String,Object> data;

    @Indexed
    private String[] keys;

    @Indexed
    String reserver;

    public PersistableDevice() {
        this(new HashMap<String,Object>());
    }

    public PersistableDevice(DawgDevice device) {
        this(device.getData());
    }

    public PersistableDevice(Map<String, Object> data) {
        init(data);
        this.data = data;
        this.keys = scanner.scan(data);
    }

    protected void init(Map<String, Object> data) {

        String mac = (String) data.get(MetaStb.MACADDRESS);
        String deviceId = (String) data.get(MetaStb.ID);

        if (deviceId != null) {
            this.id = deviceId;
        } else if (null != mac) {
            this.id = serverUtils.toLowerAlphaNumeric(mac);
        } else {
            this.id = UUID.randomUUID().toString();
        }
        data.put("id", this.id);
    }



    @Override
    public Long getExpiration() {
        Number num = (Number) data.get(EXPIRATION_KEY);
        return num == null ? null : num.longValue();
    }

    @Override
    public void setExpiration(Long expiration) {
        data.put(EXPIRATION_KEY, expiration);
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public void setData(Map<String,Object> data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PersistableDevice other = (PersistableDevice) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String reserve(String token) {
        if (null == reserver) {
            reserver = token;
            data.put(RESERVER, token);
        }
        return reserver;
    }
    @Override
    public boolean isReserved() {
        return (null != reserver);
    }
    @Override
    public String unreserve() {
        reserver = null;
        return (String) data.remove(RESERVER);
    }
    @Override
    public String getReserver() {
        return reserver;
    }



}
