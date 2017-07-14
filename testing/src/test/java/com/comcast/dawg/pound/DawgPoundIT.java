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
package com.comcast.dawg.pound;

import java.io.IOException;

import com.comcast.dawg.MetaStbBuilder;
import com.comcast.dawg.test.base.TestBase;

import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.Config;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * The API Functional tests for the DAWG Pound.
 *
 * @author  TATA
 */
public class DawgPoundIT extends TestBase {

    /** The Dawg-pound development server url. */
    private static final String BASE_URL = Config.get("testing", "dawg-pound-reservations", "http://localhost/dawg-pound/reservations/");

    /** Time offset in millisecond for reservation expiration. */
    private static final long RESERVE_EXPIRATION_TIME_OFFSET = 500000;

    private static final String UID_PREF = "poundrest";

    /** The valid device id. */
    private String deviceId = "aabbccddeeff";

    /** The valid mac id. */
    private String mac = "AA:BB:CC:DD:EE:FF";

    /** The invalid deviceId. */
    private String invalidDeviceId = "0000000000";

    @BeforeClass(groups = "rest", description = "Setting up the test case prerequisites.")
    public void setup() {

        // add stb AA:BB:CC:DD:EE:FF
        addStb(deviceId, mac);
    }

    /**
     * This method tests the @link{DawgPound#listAll()} , and verifies the response body.
     *
     * @throws  IOException
     * @throws  ClientProtocolException
     */
    @Test(groups = "rest")
    public void listAllTest() throws ClientProtocolException, IOException {
        String expectedStb2 = "00:00:00:00:00:AA";
        addStb("0000000000aa", expectedStb2);

        String url = BASE_URL + "list";
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains(mac));
        Assert.assertTrue(responseBody.contains(expectedStb2));
    }

    /**
     * This method tests the @link{DawgPound#getReservered()} API with valid token, and verifies the
     * response body.
     *
     * @throws  IOException
     * @throws  ClientProtocolException
     */
    @Test(groups = "rest")
    public void getReserveredTest() throws ClientProtocolException, IOException {
        String deviceId = MetaStbBuilder.getUID(UID_PREF);
        addStb(deviceId, mac);

        String token = "sathyv";
        String expectedStr = "\"reserver_key\":\"" + token + "\"";

        int status = reserveTheBox(token, deviceId);
        Assert.assertEquals(status, 200, "Could not reserve the box.");

        String url = BASE_URL + "reserved/token/" + token;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        Assert.assertNotNull(responseBody, "While trying to get reserved status, response received is null.");
        Assert.assertTrue(responseBody.contains(expectedStr),
                String.format("Failed to find the reserver key(%s) in response(%s).", expectedStr,
                    responseBody));
        Assert.assertTrue(responseBody.contains(deviceId),
                String.format("Failed to find the device ID(%s) in response(%s).", deviceId, responseBody));
    }

    /**
     * This method tests the @link{DawgPound#getReservered()} API with invalid token, and verifies
     * the response body.
     */
    @Test(groups = "rest")
    public void getReserveredInvalidTokenTest() throws ClientProtocolException, IOException {
        String token = "xyz";
        String url = BASE_URL + "reserved/token/" + token;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(!responseBody.contains("reserver"));
    }

    /**
     * This method tests the @link{DawgPound#isReservered()} API with valid id, and verifies the
     * response body.
     */
    @Test(groups = "rest")
    public void isReserveredTest() throws ClientProtocolException, IOException {
        String deviceId = MetaStbBuilder.getUID(UID_PREF);
        addStb(deviceId, mac);

        String reserver = "sathyv";
        int status = reserveTheBox(reserver, deviceId);
        Assert.assertEquals(status, 200, "Could not reserve the box");

        String responseBody = isReserved(deviceId);
        Assert.assertNotNull(responseBody, "While validating the reserved status of device response received is null.");
        Assert.assertTrue(responseBody.contains(reserver),
                String.format(
                    "Failed to find the reserver name(%s) in response(%s) of device reserve detail.",
                    reserver,
                    responseBody));
    }

    /**
     * This method tests the @link{DawgPound#isReservered API()} with invalid id, and verifies the
     * response body.
     */
    @Test(groups = "rest")
    public void isReserveredWithInvalidIdTest() throws ClientProtocolException, IOException {
        String deviceId = "xyz";

        String responseBody = isReserved(deviceId);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.isEmpty());
    }

    /**
     * This method tests the @link{DawgPound#reserve()} API with valid id, and verifies that the
     * response is true.
     */
    @Test(groups = "rest")
    public void reserveTest() throws ClientProtocolException, IOException {
        String deviceId = MetaStbBuilder.getUID(UID_PREF);
        addStb(deviceId, mac);

        String token = "sathy";
        long exp = System.currentTimeMillis() + RESERVE_EXPIRATION_TIME_OFFSET;
        String expiration = String.valueOf(exp);
        String url = BASE_URL + "reserve/override/" + token + "/" + deviceId +
            "/" + expiration;
        logger.info("Reservation URL : " + url);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httppost, responseHandler);
        Assert.assertNotNull(responseBody, "While trying to reserve a device, response body received is null.");
        Assert.assertTrue(responseBody.contains("true"),
                String.format("Failed to find 'true' on the response body(%s).", responseBody));

        // confirm reserved by the given token
        responseBody = isReserved(deviceId);
        Assert.assertTrue(responseBody.contains(token),
                String.format("Failed to reserve the device. Response body(%s) does not contain token(%s).",
                    responseBody, token));
    }

    /**
     * This method tests the @link{DawgPound#reserve()} API with invalid id, and verifies that the
     * response is false.
     */
    @Test(groups = "rest")
    public void reserveWithInvalidIdTest() throws ClientProtocolException, IOException {
        String token = "xyz";
        long exp = System.currentTimeMillis() + RESERVE_EXPIRATION_TIME_OFFSET;
        String expiration = String.valueOf(exp);
        String url = BASE_URL + "reserve/override/" + token + "/" +
            invalidDeviceId + "/" + expiration;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httppost, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains("false"));
    }

    /**
     * This method tests the @link{DawgPound#reserve()} API with invalid expiration, and verifies
     * that the response is false.
     */
    @Test(groups = "rest")
    public void reserveWithInvalidExpirationTest() throws ClientProtocolException, IOException {
        String deviceId = MetaStbBuilder.getUID(UID_PREF);
        addStb(deviceId, mac);

        String token = "xyz";
        long exp = System.currentTimeMillis() - RESERVE_EXPIRATION_TIME_OFFSET;
        String expiration = String.valueOf(exp); // less than current time
        String url = BASE_URL + "reserve/override/" + token + "/" +
            invalidDeviceId + "/" + expiration;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httppost, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains("false"));

        responseBody = isReserved(deviceId);
        Assert.assertTrue(!responseBody.contains(token));
    }

    /**
     * This method tests the @link{DawgPound#reservePolitely()} API with already reserved device ,
     * and verifies the response.
     */
    @Test(groups = "rest")
    public void reservePolitelyWithExistingReservationTest() throws ClientProtocolException, IOException {
        String deviceId = MetaStbBuilder.getUID(UID_PREF);
        addStb(deviceId, mac);

        String token = "abcd";
        String expectedToken = "sathy";
        int status = reserve(expectedToken, deviceId);
        Assert.assertEquals(status, 200, "Could not reserve the box.");

        long exp = System.currentTimeMillis() + RESERVE_EXPIRATION_TIME_OFFSET;
        String expiration = String.valueOf(exp);
        String url = BASE_URL + "reserve/" + token + "/" + deviceId + "/" +
            expiration;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httppost, responseHandler);
        Assert.assertNotNull(responseBody,
                "Response received is null while trying to reserve an already reserved device id.");
        Assert.assertTrue(responseBody.contains(expectedToken),
                String.format(
                    "Failed to find token(%s) presence in the response body(%s) of reserve politely.",
                    expectedToken,
                    responseBody));

        // confirm reserved by the given token
        responseBody = isReserved(deviceId);
        Assert.assertTrue(responseBody.contains(expectedToken),
                String.format("Failed to find the token(%s) in response(%s) of device id reserve details.",
                    expectedToken, responseBody));

    }

    /**
     * This method tests the @link{DawgPound#reservePolitely()} API with non reserved device , and
     * verifies the response.
     */
    @Test(groups = "rest")
    public void reservePolitelyTest() throws ClientProtocolException, IOException {
        String deviceId = MetaStbBuilder.getUID(UID_PREF);
        addStb(deviceId, mac);

        int status = unreserve(deviceId);
        Assert.assertTrue(status == 200, "could not unreserve the box");

        String token = "abcd";
        String expectedToken = "abcd";
        long exp = System.currentTimeMillis() + RESERVE_EXPIRATION_TIME_OFFSET;
        String expiration = String.valueOf(exp);
        String url = BASE_URL + "reserve/" + token + "/" + deviceId + "/" +
            expiration;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httppost, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains(expectedToken));

        // confirm reserved by the given token
        responseBody = isReserved(deviceId);
        Assert.assertTrue(responseBody.contains(expectedToken));
    }

    /**
     * This method tests the @link{DawgPound#unreserve()} API , and verifies the response.
     */
    @Test(groups = "rest")
    public void unreserveTest() throws ClientProtocolException, IOException {
        String deviceId = MetaStbBuilder.getUID(UID_PREF);
        addStb(deviceId, mac);

        String expectedToken = "sathy";
        reserve(expectedToken, deviceId);
        Assert.assertTrue(isReserved(deviceId).contains(expectedToken));

        String url = BASE_URL + "unreserve/" + deviceId;
        logger.info("Unreserve URL : " + url);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httppost, responseHandler);

        Assert.assertNotNull(responseBody, "While trying to unreserve a device ID, response received is null.");
        Assert.assertTrue(responseBody.contains(expectedToken),
                String.format("Unreserve response body(%s) does not contains the token(%s).", responseBody,
                    expectedToken));

        responseBody = isReserved(deviceId);
        Assert.assertFalse(responseBody.contains(expectedToken),
                String.format(
                    "The device ID does not get unreserved. Response body(%s) returned with token(%s).",
                    responseBody,
                    expectedToken));
    }

    /**
     * This method tests the @link{DawgPound#unreserve()} API with invalid id , and verifies the
     * response.
     */
    @Test(groups = "rest")
    public void unreserveWithInvalidIdTest() throws ClientProtocolException, IOException {

        String url = BASE_URL + "unreserve/" + invalidDeviceId;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httppost, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.isEmpty());
    }

    /**
     * This method reserves the box AA:BB:CC:DD:EE:FF using token sathyv.
     *
     * @throws  ClientProtocolException
     * @throws  IOException
     */
    private int reserveTheBox(String token, String deviceId) throws ClientProtocolException, IOException {
        int statusCode = 0;
        long exp = System.currentTimeMillis() + RESERVE_EXPIRATION_TIME_OFFSET;
        String expiration = String.valueOf(exp);
        String url = BASE_URL + "reserve/override/" + token + "/" + deviceId +
            "/" + expiration;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        HttpResponse response = httpclient.execute(httppost);

        if (null != response) {
            StatusLine statusLine = response.getStatusLine();

            if (null != statusLine) {
                statusCode = statusLine.getStatusCode();
            }

        }

        return statusCode;
    }

    /**
     * Add the Stb detail to DAWG and cache the STB detail for later deletion.
     *
     * @param  deviceId  Device id with which STB to be created.
     * @param  mac       MAC address with which STB to be created.
     */
    private void addStb(String deviceId, String mac) {

        MetaStb testStb = MetaStbBuilder.build().id(deviceId).mac(mac).stb();
        addStb(testStb);
    }

    /**
     * Unreserve the box.
     *
     * @throws  ClientProtocolException
     * @throws  IOException
     */
    private int unreserve(String deviceId) throws ClientProtocolException, IOException {
        int statusCode = 0;
        String url = BASE_URL + "unreserve/" + deviceId;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        HttpResponse response = httpclient.execute(httppost);

        if (null != response) {
            StatusLine statusLine = response.getStatusLine();

            if (null != statusLine) {
                statusCode = statusLine.getStatusCode();
            }
        }

        return statusCode;

    }

    /**
     * Reserves politely the stb.
     *
     * @throws  ClientProtocolException
     * @throws  IOException
     */
    private int reserve(String token, String id) throws ClientProtocolException, IOException {
        int statusCode = 0;
        long exp = System.currentTimeMillis() + RESERVE_EXPIRATION_TIME_OFFSET;
        String expiration = String.valueOf(exp);
        String url = BASE_URL + "reserve/override/" + token + "/" + id + "/" +
            expiration;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        HttpResponse response = httpclient.execute(httppost);

        if (null != response) {
            StatusLine statusLine = response.getStatusLine();

            if (null != statusLine) {
                statusCode = statusLine.getStatusCode();
            }
        }

        return statusCode;
    }

    /**
     * Calls the @link{DawgPound#isReserved()} API and returns the response string.
     *
     * @param   deviceId
     *
     * @return  the response string
     *
     * @throws  ClientProtocolException
     * @throws  IOException
     */
    private String isReserved(String deviceId) throws ClientProtocolException, IOException {
        String url = BASE_URL + "reserved/id/" + deviceId;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        logger.info(String.format("Reservation rest call(%s) response body is : %s.", url, responseBody));

        return responseBody;
    }

}
