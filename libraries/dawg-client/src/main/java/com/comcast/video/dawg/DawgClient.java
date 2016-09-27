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
package com.comcast.video.dawg;

import java.io.Closeable;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.comcast.drivethru.RestClient;
import com.comcast.drivethru.client.DefaultRestClient;
import com.comcast.drivethru.exception.HttpException;
import com.comcast.drivethru.utils.Method;
import com.comcast.drivethru.utils.RestRequest;
import com.comcast.drivethru.utils.URL;

/**
 * Base class for dawg clients
 * @author Kevin Pearson
 *
 */
public abstract class DawgClient implements Closeable {
    public static final Logger logger = Logger.getLogger(DawgClient.class);

    /** Constant to store the millisecond value for 1 second.*/
    public static final int ONE_SEC_IN_MILLIS = 1000;

    /** Pattern to distinguish whether the currently requested url is for holdkey or not. */
    private static final Pattern HOLD_KEY_PATTERN = Pattern.compile("holdTime=(\\d+)");

    private static final String DEFAULT_CATS_SERVER = "";

    protected RestClient client;

    private static HttpClient defaultClient() {
        return HttpClientBuilder.create().setDefaultSocketConfig(SocketConfig.custom()
                .setSoTimeout(RestClient.DEFAULT_TIMEOUT).build())
            .disableContentCompression().build();
    }

    private static RestClient defaultRestClient(String defaultBaseUrl) {
        return new DefaultRestClient(defaultBaseUrl, defaultClient());
    }

    public DawgClient() {
        this((String)null);
    }

    public DawgClient(String defaultBaseUrl) {
        this(defaultRestClient(defaultBaseUrl));
    }

    public DawgClient(RestClient client) {
        this.client = client;
    }

    /**
     * Actually sends the request through a rest client
     * @param url
     * @param method
     * @throws HttpException
     */
    protected void executeRequest(URL url, Method method) throws HttpException {
        int timeout = getRestSocketTimeOut(url);
        RestRequest request = new RestRequest(url, method);
        request.setTimeout(timeout);
        int status = client.execute(request).getStatusCode();
        if ((status != HttpStatus.SC_OK) && (status != HttpStatus.SC_ACCEPTED)) {
            throw new HttpException("Received an error http status code (" + status + ")");
        }
    }

    /**
     * Get the socket timeout for the current <code>REST</code> operation.
     * @param url <code>REST</code> request url.
     * @return timeout value in milliseconds. If hold key action, it will return timeout based on the number of
     * keys to be sent. Otherwise it will return the default timeout(10 sec).
     */
    private int getRestSocketTimeOut(URL url) {
        String buildUrl = null;
        // Default socket timeout
        int timeout = DefaultRestClient.DEFAULT_TIMEOUT;

        try {
            // Build the request url. It involves appending path and query strings properly to the base url.
            buildUrl = url.build();
        } catch (HttpException e) {
            logger.warn("Exception while building URL.",e);
        }

        if(null != buildUrl) {
            Matcher matcher = HOLD_KEY_PATTERN.matcher(buildUrl);
            if(matcher.find() && matcher.groupCount() == 1) {
                timeout = Integer.valueOf(matcher.group(1)) * ONE_SEC_IN_MILLIS;
                logger.info("REST communication socket timeout overriden to "+ timeout + " milliseconds");
            }
        }
        return timeout;
    }

    /**
     * Helper method to do some verification on the cats server url
     * @param catsHost The host of the cats server
     * @return
     */
    protected String formCatsServerBaseUrl(String catsHost) {
        if (catsHost == null) {
            logger.warn("CATS server url was set to null, using default '" + DEFAULT_CATS_SERVER + "'");
            catsHost = DEFAULT_CATS_SERVER;
        }
        String protocol = catsHost.startsWith("http://") || catsHost.startsWith("https://") ? "" : "http://";
        return protocol + catsHost;
    }

    public void close() throws IOException {
        if (this.client != null) this.client.close();
    }

    public RestClient getClient() {
        return client;
    }

    public void setClient(RestClient client) {
        this.client = client;
    }

}
