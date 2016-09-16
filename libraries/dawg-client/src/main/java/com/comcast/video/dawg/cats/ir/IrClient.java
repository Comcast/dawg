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
package com.comcast.video.dawg.cats.ir;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.comcast.drivethru.utils.Method;
import com.comcast.drivethru.utils.URL;
import com.comcast.video.dawg.DawgClient;
import com.comcast.video.stbio.Key;
import com.comcast.video.stbio.KeyInput;
import com.comcast.video.stbio.exceptions.KeyException;
import com.comcast.video.stbio.meta.IrMeta;
import com.comcast.video.stbio.meta.RemoteType;

/**
 * Client for sending IR keys to the CATS IR REST server
 * @author Kevin Pearson
 * @author Prasad Menon
 *
 */
public class IrClient extends DawgClient implements KeyInput {
    public static final Logger logger = Logger.getLogger(IrClient.class);

    public static final String IR_SERVICE = "ir-service";
    public static final String REST = "rest";
    public static final String DEFAULT_BLASTER_TYPE = "gc100";
    public static final String KEY_SET = "keySet";
    public static final String COMMAND = "command";
    public static final String COMMAND_LIST = "commandList";
    public static final String DELAY_IN_MILLIS = "delayInMillis";
    public static final String HOLD_TIME = "holdTime";
    public static final long DEFAULT_DELAY = 700;

    /** Effective time in milliseconds for a single key press. */
    public static final long SINGLE_KEY_PRESS_TIME = 100;

    private String catsHost;
    private String blasterType;
    private String irHost;
    private String port;
    private String remoteType;
    private String catsKeySetMapping;

    /**
     * Creates a client for the stb with the given metadata
     * @param irMeta The metadata about the stb
     */
    public IrClient(IrMeta irMeta) {
        Validate.notNull(irMeta);
        this.catsHost = irMeta.getCatsServerHost();
        this.blasterType = getIrBlasterType(irMeta);
        this.irHost = irMeta.getIrServiceUrl();
        this.port = irMeta.getIrServicePort();
        this.remoteType = irMeta.getRemoteType();
        this.catsKeySetMapping = irMeta.getCatsKeySetMapping();
    }

    /**
     * Creates a client for the stb with the given metadata and input remote type
     * @param irMeta The metadata about the stb
     * @param remoteType The current remote type
     */
    public IrClient(IrMeta irMeta, String remoteType) {
        this(irMeta);
        this.remoteType = remoteType;

    }

    /**
     * Creates an IR Client
     * @param catsHost The host of the CATS server
     * @param irHost The host of the IR server
     * @param port The port of the IR server
     * @param remoteType The type of remote
     */
    public IrClient(String catsHost, String irHost, String port, String remoteType) {
        this(catsHost, DEFAULT_BLASTER_TYPE, irHost, port, remoteType);
    }

    /**
     * Creates an IR Client
     * @param catsHost The host of the CATS server
     * @param blasterType The type of IR blaster that is used (such as gc100)
     * @param irHost The host of the IR server
     * @param port The port of the IR server
     * @param remoteType The type of remote
     */
    public IrClient(String catsHost, String blasterType, String irHost, String port, String remoteType) {
        this.catsHost = catsHost;
        this.blasterType = blasterType == null ? DEFAULT_BLASTER_TYPE : blasterType;
        this.irHost = irHost;
        this.port = port;
        this.remoteType = remoteType;
    }

    /**
     * Sends a single key
     * @param key The key to send
     * @throws KeyException
     */
    @Override
    public void pressKey(Key key) throws KeyException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(COMMAND, key.getKey());
        URL url = formKeyUrl(IrOperation.pressKey, params, null);
        executeRequest(url, key.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pressKey(Key key, RemoteType remoteType) throws KeyException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(COMMAND, key.getKey());
        URL url = formKeyUrl(IrOperation.pressKey, params, remoteType.name());
        executeRequest(url, key.name());
    }

    /**
     * Sends multiple keys with a default delay between the keys of 700 ms
     * @param keys The list of keys to send
     * @throws KeyException
     */
    @Override
    public void pressKeys(Key... keys) throws KeyException {
        pressKeys(DEFAULT_DELAY, keys);
    }

    /**
     * Sends multiple keys with a given delay between the keys
     * @param delay The amount of time to wait between keys in milliseconds
     * @param keys The keys to send
     * @throws KeyException
     */
    @Override
    public void pressKeys(long delay, Key... keys) throws KeyException {
        StringBuilder keyStr =  new StringBuilder();
        for (Key key : keys) {
            if (!keyStr.toString().isEmpty()) {
                keyStr.append(",");
            }
            keyStr.append(key.getKey());
        }
        Map<String, Object> params = new HashMap<String, Object>();

        params.put(COMMAND_LIST, keyStr.toString());
        params.put(DELAY_IN_MILLIS, delay);
        URL url = formKeyUrl(IrOperation.pressKeys, params, null);
        executeRequest(url, keyStr.toString());
    }

    @Override
    public void pressKeys(boolean validate, Key... keys) throws KeyException {
        logger.warn("No validation implemented for IrClient");
        pressKeys(keys);
    }

    /**
     * Holds a single key for a given amount of time
     * @param key The key to hold
     * @param holdTime The time in milliseconds to hold the key
     * @throws KeyException
     */
    @Override
    public void holdKey(Key key, long holdTime) throws KeyException {
        Map<String, Object> params = new HashMap<String, Object>();

        // 1 keypress in every 100 milliseconds. So dividing by 100 to get the repeat count for keys.
        int repeatCount = Long.valueOf(holdTime / SINGLE_KEY_PRESS_TIME).intValue();
        logger.info("[Hold Key] Key: "+ key.name()+ " with repeat count: "+ repeatCount);

        params.put(COMMAND, key.getKey());
        params.put(HOLD_TIME, repeatCount);
        URL url = formKeyUrl(IrOperation.pressKeyAndHold, params, null);
        executeRequest(url, key.name());
    }

    /**
     * Actually sends the request through a rest client
     * @param url
     * @param keyStr
     * @throws KeyException
     */
    private void executeRequest(URL url, String keyStr) throws KeyException {
        try {
            executeRequest(url, Method.POST);
        } catch (Exception e) {
            throw new KeyException("Failed to send keys '" + keyStr + "'", e);
        }
    }

    /**
     * Forms the URL to send the key
     * @param op The type of ir operation to use
     * @param params The url query parameters
     * @param overRiddenRemoteType The overridden remote type if a user requires to send some keys via a specific remote type which don't work in the default remote of the box.
     * @return The url to send the request through a rest client
     */
    private URL formKeyUrl(IrOperation op, Map<String, Object> params, String overRiddenRemoteType) {
        URL url = new URL(formCatsServerBaseUrl(catsHost));
        url.addPath(IR_SERVICE).addPath(REST);
        url.addPath(blasterType).addPath(irHost).addPath("" + port).addPath(op.name());

        if (StringUtils.isEmpty(this.catsKeySetMapping)) {
            if (StringUtils.isEmpty(overRiddenRemoteType)) {
                url.addQuery(KEY_SET, remoteType);
            }
            else {
                url.addQuery(KEY_SET, overRiddenRemoteType);
            }
        }
        else {
            url.addQuery(KEY_SET, this.catsKeySetMapping);
        }

        for (String k : params.keySet()) {
            url.addQuery(k, params.get(k));
        }
        return url;
    }

    /**
     * Gets the IR blaster type examples are : gc100 and irnetboxpro3
     * If no IR blaster type has been specified by the user, it would take gc100 as the default blaster type.
     *
     * @param irMeta   metadata necessary to send IR keys via CATS
     * @return         The blaster type to use while sending IR requests
     */
    private String getIrBlasterType(IrMeta irMeta) {
        String blasterType = irMeta.getBlasterType();
        if(StringUtils.isBlank(blasterType)) {
            blasterType = DEFAULT_BLASTER_TYPE;
        }
        return blasterType;
    }
}
