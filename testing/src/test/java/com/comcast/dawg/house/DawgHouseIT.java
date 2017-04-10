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
package com.comcast.dawg.house;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.seleniumhq.jetty7.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.dawg.MetaStbBuilder;
import com.comcast.dawg.TestServers;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.house.DawgHouseClient;
import com.comcast.video.dawg.house.DawgPoundClient;
import com.comcast.video.dawg.house.Reservation;

/**
 * The API Functional tests for the DAWG House
 *
 * @author TATA
 *
 */
public class DawgHouseIT {
    private static final String ID_PREF = "resttest";

    /**
     * The valid id for an stb
     */
    private static final String ID = "aabbccddeeff";
    /**
     * The valid Mac id for an stb
     */
    private static final String MAC = "AA:BB:CC:DD:EE:FF";
    private Set<String> addedDeviceIds = new HashSet<String>();

    @BeforeClass(groups="rest")
    public void setup() throws ClientProtocolException, IOException {
        // add stb AA:BB:CC:DD:EE:FF
        int status = addStb(ID, MAC);
        Assert.assertTrue(status == HttpStatus.OK_200, "could not add the stb " + MAC);
    }

    @AfterClass(groups="rest")
    public void tearDown() {
        // remove all devices that were used in these tests
        for (String id : addedDeviceIds) {
            try {
                removeStb(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method tests the @link{HouseRestController#listAll()} API , and verifies the
     * response body that the added stbs are present
     *
     * @throws IOException
     * @throws ClientProtocolException
     *
     */
    @Test(groups="rest")
    public void listAllTest() throws ClientProtocolException, IOException {
        String expectedStb = "00:00:00:00:00:AA";
        addStb("0000000000aa", expectedStb);
        String url = TestServers.getHouse() + "devices/list*";
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains(MAC));
        Assert.assertTrue(responseBody.contains(expectedStb));
    }

    /**
     * This method tests the @link{HouseRestController#getStbsById()} API with an invalid
     * id - 'xyz'
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(expectedExceptions = HttpResponseException.class, groups="rest")
    public void getStbsByIdWithInvalidIdTest() throws ClientProtocolException, IOException {
        String url = TestServers.getHouse() + "devices/id/xyz";
        HttpGet method = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        httpClient.execute(method, responseHandler);
    }

    /**
     * This method tests the @link{HouseRestController#getStbsById()} API with a valid id
     * - 'aabbccddeeff'
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(groups="rest")
    public void getStbsByIdWithValidIdTest() throws ClientProtocolException, IOException {
        String url = TestServers.getHouse() + "devices/id/" + ID;
        HttpGet method = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpClient.execute(method, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains(MAC));
    }

    /**
     * This method tests the @link{HouseRestController#add()} API with a valid id -
     * 'aabbccddeeff'
     */
    @Test(groups="rest")
    public void addTest() {
        String id = "ffeeddccbbaa";
        String mac = "FF:EE:DD:CC:BB:AA";
        try {
            int statusCode = addStb(id, mac);
            Assert.assertEquals(statusCode, 200);
            // verify the response of getStbsById
            String url = TestServers.getHouse() + "devices/id/" + id;
            HttpGet method = new HttpGet(url);
            HttpClient httpClient = new DefaultHttpClient();

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpClient.execute(method, responseHandler);
            Assert.assertNotNull(responseBody);
            Assert.assertTrue(responseBody.contains(mac));
        } catch (Exception e) {
            Assert.fail("Caught exception " + e);
        }
    }


    @DataProvider(name = "addStbWithInvalidModelParams")
    public Object[][] addStbWithInvalidModelKeyProvider() {
        return new Object[][] {
            { "[]", "" },
                { "[]", "TEST FAMILY" },
                { "[\"CAPS\"]", "" },
                { "[\"CAPS\"]", "TEST FAMILY" }
        };
    }

    /**
     * This method tests the @link{HouseRestController#add()} addition of Stb's
     * with invalid model names and verifies that user given family and
     * capabilities gets populated
     *
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(dataProvider = "addStbWithInvalidModelParams")
    public void testAddStbWithInvalidModelName(String stbInitialCaps, String stbInitialFamily) throws ClientProtocolException, IOException {
        String modelName = "InvalidModel";
        String id = MetaStbBuilder.getUID("modeltest");
        String mac = "00:00:00:00:00:BB";

        // Add stb with an invalid model name that doesn't exist
        int status = addStb(id, mac, modelName, stbInitialCaps,
                stbInitialFamily);

        Assert.assertTrue(status == HttpStatus.OK_200, "could not add the stb "
                + id + " with invalid model name");

        HttpClient httpclient = new DefaultHttpClient();
        String url = TestServers.getHouse() + "/devices/id/" + id;
        HttpGet httpget = new HttpGet(url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains(modelName));
        Assert.assertTrue(responseBody.contains(stbInitialCaps));
        Assert.assertTrue(responseBody.contains(stbInitialFamily));
    }

    /**
     * This method tests adding multiple boxes and verifies by calling the  @link{HouseRestController#listAll()}
     *
     */
    @Test(groups="rest")
    public void addMultipleBoxesTest() {
        String deviceId1 = "112233445566";
        String macId1 = "11:22:33:44:55:66";
        String deviceId2 = "665544332211";
        String macId2 = "66:55:44:33:22:11";

        try {
            int statusCode = addStb(deviceId1, macId1);
            Assert.assertEquals(statusCode, HttpStatus.OK_200);

            statusCode = addStb(deviceId2, macId2);
            Assert.assertEquals(statusCode, HttpStatus.OK_200);

            // verify the response of listAll
            String url = TestServers.getHouse() + "devices/list*";
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            Assert.assertNotNull(responseBody);
            Assert.assertTrue(responseBody.contains(macId1));
            Assert.assertTrue(responseBody.contains(macId2));
        } catch (Exception e) {
            Assert.fail("Caught exception " + e);
        }

    }

    /**
     * This tests the @link{HouseRestController#deleteSingle()} API with an invalid id -
     * 'nobox'
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(groups="rest")
    public void deleteSingleInvalidIdTest() throws ClientProtocolException, IOException {
        String id = "nobox";

        int statusCode = removeStb(id);
        Assert.assertEquals(statusCode, HttpStatus.OK_200);
    }

    /**
     * This method tests the @link{HouseRestController#deleteSingle()} API with a valid random id
     * @throws IOException
     * @throws ClientProtocolException
     */
    @Test(groups="rest")
    public void deleteSingleValidIdTest() throws ClientProtocolException, IOException {
        String id = MetaStbBuilder.getUID(ID_PREF);
        int status = addStb(id, MAC);
        Assert.assertTrue(status == HttpStatus.OK_200, "could not add the stb " + MAC);
        String url = TestServers.getHouse() + "devices/id/" + id;

        int statusCode = removeStb(id);
        Assert.assertEquals(statusCode, HttpStatus.OK_200);

        // verify that the box is deleted

        try {
            HttpGet httpGet = new HttpGet(url);
            HttpClient httpClient = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            httpClient.execute(httpGet, responseHandler);
            Assert.fail("Did not throw exception for the removed stb");
        } catch (Exception exp) {
            Assert.assertTrue(exp instanceof HttpResponseException);
        }

    }

    /**
     * This tests the @link{HouseRestController#deleteStb()} API with a invalid id - 'xyz'
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(groups="rest")
    public void deleteStbInvalidTest() throws ClientProtocolException, IOException {

        String[] id = { "xyz" };
        String url = TestServers.getHouse() + "devices/delete?id=" + id;
        HttpGet method = new HttpGet(url);

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(method);
        Assert.assertEquals(getStatus(response), HttpStatus.OK_200);
    }

    /**
     * This tests the @link{HouseRestController#deleteStb()} API with a valid id -
     * '000000000011'
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(groups="rest")
    public void deleteStbTest() throws ClientProtocolException, IOException {
        String id = MetaStbBuilder.getUID(ID_PREF);
        String mac = "00:00:00:00:00:11";
        addStb(id, mac);

        String url = TestServers.getHouse() + "devices/delete?id=" + id;

        HttpGet method = new HttpGet(url);

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(method);

        Assert.assertEquals(getStatus(response), HttpStatus.OK_200);

        try {
            url = TestServers.getHouse() + "devices/id/" + id;
            HttpGet httpGet = new HttpGet(url);
            httpClient = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            httpClient.execute(httpGet, responseHandler);
            Assert.fail("Did not throw HttpResponseException for the deleted stb");
        } catch (Exception exp) {
            Assert.assertTrue(exp instanceof HttpResponseException);
        }
    }

    /**
     * This method tests the @link{HouseRestController#getStbsByIds()} API with a valid
     * param aabbccddeeff
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(groups="rest")
    public void getStbsByIdsTest() throws ClientProtocolException, IOException {
        String url = TestServers.getHouse() + "devices/id";
        HttpPost method = new HttpPost(url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("id", ID));
        method.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        HttpClient httpClient = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpClient.execute(method, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains(MAC));
    }

    /**
     * This method tests the @link{HouseRestController#getStbsByIds()} API with an invalid
     * param 'xyz'
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(groups="rest", expectedExceptions={HttpResponseException.class})
    public void getStbsByIdsWithInvalidIdsTest() throws ClientProtocolException, IOException {

        String url = TestServers.getHouse() + "devices/id";
        HttpPost method = new HttpPost(url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("id", "xyz"));
        method.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        HttpClient httpClient = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpClient.execute(method, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertFalse(responseBody.contains("xyz"));

    }

    /**
     * This method tests the @link{HouseRestController#getStbsByQuery()} API with a the given property
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(groups="rest")
    public void getStbsByQueryTest() throws ClientProtocolException, IOException {
        //add an stb with model 'newmodel'
        String model = "newmodel";
        String id = "000000000000";
        String url = TestServers.getHouse() + "devices/id/" + id;
        HttpEntity entity = new StringEntity("{\"model\":\"" + model +"\"}");

        HttpPut method = new HttpPut(url);
        method.addHeader("Content-Type", "application/json");

        method.setEntity(entity);
        HttpClient httpClient = new DefaultHttpClient();

        HttpResponse response = httpClient.execute(method);
        //get the added stb
        url = TestServers.getHouse() + "devices/query?q=" + model;
        HttpGet httpGet = new HttpGet(url);

        httpClient = new DefaultHttpClient();

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpClient.execute(httpGet, responseHandler);
        Assert.assertTrue(responseBody.contains(model));
    }

    /**
     * This method tests the @link{HouseRestController#getStbsByQuery()} API with an valid
     * query
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(dataProvider = "validQuery", groups="rest")
    public void getStbsByQueryTest(String q) throws ClientProtocolException, IOException {
        String url = TestServers.getHouse() + "devices/query?q=" + q;
        HttpGet method = new HttpGet(url);

        HttpClient httpClient = new DefaultHttpClient();

        HttpResponse response = httpClient.execute(method);

        Assert.assertEquals(getStatus(response), HttpStatus.OK_200, q);
    }

    /**
     * This method tests the @link{HouseRestController#getStbsByQuery()} API with an
     * invalid param
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(dataProvider = "invalidQuery", groups="rest")
    public void getStbsByQueryInvalidTest(String q) throws ClientProtocolException, IOException {

        String url = TestServers.getHouse() + "devices/query?q=" + q;
        HttpGet method = new HttpGet(url);

        HttpClient httpClient = new DefaultHttpClient();

        HttpResponse response = httpClient.execute(method);

        Assert.assertEquals(getStatus(response), 500, q);
    }

    /**
     * This method tests the @link{HouseRestController#getStbsByQuery()} API with a
     * invalid param
     *
     */
    @Test(dataProvider = "illegalQuery", expectedExceptions = IllegalArgumentException.class, groups="rest")
    public void getStbsByQueryWithIAETest(String q) throws ClientProtocolException, IOException {
        String url = TestServers.getHouse() + "devices/query?q=" + q;
        HttpGet method = new HttpGet(url);

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(method);
    }

    /**
     * This method tests the @link{HouseRestController#getStbsByQuery()} API with an
     * invalid param
     *
     */
    @Test(dataProvider = "query", expectedExceptions = NullPointerException.class, groups="rest")
    public void getStbsByQueryWithNullTest(String q) throws ClientProtocolException, IOException {
        String url = TestServers.getHouse() + "devices/query?q=" + q;
        HttpGet method = new HttpGet(url);

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(method);
    }

    /**
     * This method tests the @link{HouseRestController#populate()} API with a valid client token
     * token
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(groups="rest")
    public void populateTest() throws ClientProtocolException, IOException {
        String token = "abcebb42-af86-11e1-ad74-005056b4006f";
        String url = TestServers.getHouse() + "devices/populate/" + token;
        HttpPost method = new HttpPost(url);
        HttpEntity entity = new SerializableEntity(
                (Serializable) new HashMap<String, Object>(), false);
        method.setEntity(entity);

        HttpClient httpClient = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpClient.execute(method, responseHandler);
        Assert.assertTrue(!responseBody.isEmpty());
    }

    /**
     * This method tests the @link{HouseRestController#populate()} API with a invalid
     * client token
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(groups="rest")
    public void populateInvalidTest() throws ClientProtocolException, IOException {
        String invalidClientToken = "xyz";
        String url = TestServers.getHouse() + "devices/populate/" + invalidClientToken;
        HttpPost method = new HttpPost(url);
        HttpEntity entity = new SerializableEntity(
                (Serializable) new HashMap<String, Object>(), false);
        method.setEntity(entity);

        HttpClient httpClient = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpClient.execute(method, responseHandler);
        Assert.assertEquals(responseBody, "0");
    }

    /**
     * This method tests the @link{HouseRestController#update()} API with op = add
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(groups="rest")
    public void updateAddTagsTest() throws ClientProtocolException, IOException {
        String id = MetaStbBuilder.getUID(ID_PREF);
        addStb(id, MAC);
        String tag = "testtag";
        String url = TestServers.getHouse() + "devices/update/tags/add";
        HttpEntity entity = new StringEntity(
                "{\"id\": [\"" + id + "\"],\"tag\":[\"" + tag +"\"]}");
        HttpPost method = new HttpPost(url);

        method.addHeader("Content-Type", "application/json");
        method.setEntity(entity);

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(method);

        Assert.assertEquals(getStatus(response), HttpStatus.OK_200);
        // verify that the tag added by getting the stb
        url = TestServers.getHouse() + "devices/id";
        method = new HttpPost(url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("id", id));
        method.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        httpClient = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpClient.execute(method, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains(tag));
    }

    /**
     * This method tests the @link{HouseRestController#update()} API with op = remove
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(groups="rest")
    public void updateDeleteTagsTest() throws ClientProtocolException, IOException {
        String tag = "testtag";

        String url = TestServers.getHouse() + "devices/update/tags/remove";
        HttpEntity entity = new StringEntity(
                "{\"id\": [\"aabbccddeeff\"],\"tag\":[\"" + tag + "\"]}");
        HttpPost method = new HttpPost(url);

        method.addHeader("Content-Type", "application/json");
        method.setEntity(entity);

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(method);

        Assert.assertEquals(getStatus(response), HttpStatus.OK_200);
        //verify that the tag deleted
        url = TestServers.getHouse() + "devices/id";
        method = new HttpPost(url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("id", ID));
        method.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        httpClient = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpClient.execute(method, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertFalse(responseBody.contains(tag));
    }

    @Test(groups="rest")
    public void updateDevice() {
        DawgHouseClient houseClient = new DawgHouseClient(TestServers.getHouse());
        DawgPoundClient poundClient = new DawgPoundClient(TestServers.getPound());
        String token = MetaStbBuilder.getUID("testuser");

        MetaStb stb = MetaStbBuilder.build().uid(ID_PREF).stb();
        String deviceId = stb.getId();
        addedDeviceIds.add(deviceId);

        houseClient.add(stb);
        poundClient.reserve(new Reservation(deviceId, token, System.currentTimeMillis() + TimeUnit.DAYS.toMillis(365), true));

        MetaStb orig = houseClient.getById(deviceId);
        Assert.assertNotNull(orig);
        Assert.assertEquals(orig.getMake(), MetaStbBuilder.MAKE);
        Assert.assertEquals(poundClient.getReservation(deviceId).getToken(), token);

        String newMake = MetaStbBuilder.getUID("newMake");
        stb.setMake(newMake);
        houseClient.update(stb);

        MetaStb updated = houseClient.getById(deviceId);
        Assert.assertNotNull(updated);
        Assert.assertEquals(updated.getMake(), newMake);
        Assert.assertEquals(poundClient.getReservation(deviceId).getToken(), token);
    }

    @DataProvider(name = "validQuery")
    public Object[][] keyProvider() {
        return new Object[][] { { TestHelper.STR_NORMAL },
            { TestHelper.STR_LONG_NUMBER },
            { TestHelper.STR_EMPTY },
            { TestHelper.STR_URL_PARAM_2 },
            { TestHelper.STR_URL_PARAM_2_ENC },
            { TestHelper.STR_SQL_ENC },
            { TestHelper.STR_HASH },
            { TestHelper.STR_HASH_ENC },
            { TestHelper.STR_BLANK_ENC },
            { TestHelper.STR_LEAD_SPACES_ENC },
            { TestHelper.STR_UTF8 },
            { TestHelper.STR_TRAIL_SPACES_ENC },
            { TestHelper.STR_URL_ENC },
            { TestHelper.STR_NEWLINE_ENC },
            { TestHelper.STR_QUOTE_ENC },
            { null }

        };
    }

    @DataProvider(name = "illegalQuery")
    public Object[][] keyProvider3() {
        return new Object[][] {

            { TestHelper.STR_BYTE_ARRAY },
                { TestHelper.STR_NULL_ENC },
                { TestHelper.STR_BLANK },
                { TestHelper.STR_NEWLINE },
                { TestHelper.STR_LEAD_SPACES },
                { TestHelper.STR_TRAIL_SPACES },
                { TestHelper.STR_MID_SPACES },
                { TestHelper.STR_QUOTE },
                { TestHelper.STR_QUOTE_NESTED },
                { TestHelper.STR_QUOTE_PAIR },
                { TestHelper.STR_UTF8_2 },

        };
    }

    @DataProvider(name = "query")
    public Object[][] keyProvider4() {
        return new Object[][] {

            { TestHelper.STR_UTF8_HIGH },

        };
    }

    @DataProvider(name = "invalidQuery")
    public Object[][] keyProvider5() {
        return new Object[][] {
            { TestHelper.STR_URL_PARAM_1 },
                { TestHelper.STR_URL_PARAM_1_ENC },

        };
    }

    /**
     * Adds the Stb to the list
     *
     * @param mac
     *            the mac id of the box
     * @param id
     *            the device id
     * @return the http status code
     * @throws ClientProtocolException
     * @throws IOException
     */
    private int addStb(String id, String mac) throws ClientProtocolException, IOException {
        addedDeviceIds.add(id);
        String url = TestServers.getHouse() + "devices/id/" + id;
        HttpEntity entity = new StringEntity("{\"macAddress\":\"" + mac + "\", \"name\":\"" + id + "\"}");

        HttpPut method = new HttpPut(url);
        method.addHeader("Content-Type", "application/json");

        method.setEntity(entity);
        HttpClient httpClient = new DefaultHttpClient();

        HttpResponse response = httpClient.execute(method);

        return getStatus(response);
    }

    /**
     * Adds the stb to the list
     * @param id the unique id for the box
     * @param mac the mac address
     * @param model the model of the box
     * @param capability the capability of the box
     * @param family the family of the box
     * @return the status of the operation
     * @throws ClientProtocolException
     * @throws IOException
     */
    private int addStb(String id, String mac, String model, String capability, String family) throws ClientProtocolException, IOException {
        addedDeviceIds.add(id);
        String url = TestServers.getHouse() + "/devices/id/" + id;
        HttpEntity entity = null;
        entity = new StringEntity("{\"macAddress\":\"" + mac
                + "\",\"model\":\"" + model + "\",\"capabilities\":"
                + capability + ",\"family\":\"" + family + "\"}");
        HttpPut method = new HttpPut(url);
        method.addHeader("Content-Type", "application/json");
        method.setEntity(entity);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(method);
        int status = getStatus(response);
        return status;
    }

    /**
     * Deletes the stb from the list
     *
     * @param id
     *            the device id of the box
     * @return the status code
     * @throws ClientProtocolException
     * @throws IOException
     */
    private int removeStb(String id) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        String url = TestServers.getHouse() + "devices/id/" + id;
        HttpDelete delMethod = new HttpDelete(url);

        HttpResponse response = httpClient.execute(delMethod);

        return  getStatus(response);
    }

    /**
     * Gets the status of the HttpResponse
     * @param response the http response
     * @return the status
     */
    private int getStatus(HttpResponse response) {
        int status = 0;
        if (null != response) {
            StatusLine statusLine = response.getStatusLine();
            if (null != statusLine) {
                status = statusLine.getStatusCode();
            }

        }
        return status;
    }
}
