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
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.dawg.MetaStbBuilder;
import com.comcast.video.dawg.common.Config;

/**
 * The API Functional tests for the DAWG House - StbModelController
 * @author TATA
 *
 */
public class StbModelIT {
    /**
     * The Dawg-house development server base url
     */
    private static final String BASE_URL = Config.get("testing", "dawg-house", "http://localhost/dawg-house");

    /**
     * The model id
     */
    private static final String MODEL_ID = "DTA30";
    /**
     * The model name
     */
    private static final String MODEL_NAME = "DTA30";
    /**
     * The capabilities of the model
     */
    private static final String CAPABILITIES = "[\"SINGLE_TUNER\"]";
    /**
     * The box family
     */
    private static final String FAMILY = "DTA";

    /**
     * Initial capabilites
     */
    private static final String INIT_CAP = "[\"CAP\"]";

    /**
     * Initial family
     */
    private static final String INIT_FAMILY = "FAMILY";

    /**
     * The model
     */
    private static final String MODEL = "MODEL";
    private Set<String> addedDeviceIds = new HashSet<String>();
    private Set<String> addedModelIds = new HashSet<String>();

    @BeforeClass(groups = "rest")
    public void setup() throws ClientProtocolException, IOException {
        // Add a new model
        int status = addModel(MODEL_NAME, CAPABILITIES, FAMILY);
        Assert.assertTrue(status == HttpStatus.SC_OK, "could not add new model");
    }

    @AfterClass(groups = "rest")
    public void tearDown() throws ClientProtocolException, IOException {
        //  remove all added models
        for (String modelId : addedModelIds) {
            try {
                removeModel(modelId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (String deviceId : addedDeviceIds) {
            try {
                removeStb(deviceId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method tests the @link{StbModelController#getModels()} API , and verifies the
     * response body that the added stbs are present
     * @throws IOException
     * @throws ClientProtocolException
     *
     */
    @Test(groups = {"rest", "smoke" })
    public void testGetModels() throws ClientProtocolException, IOException {
        String expectedFamily = "MAC_S";
        String[] id = {"SA4250HDC" };
        String url = BASE_URL + "/models?id=" + id;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        Assert.assertNotNull(responseBody);
        responseBody.contains(expectedFamily);
        responseBody.contains(id[0]);
    }

    /**
     * This method tests the @link{StbModelController#addModel()} API , and verifies the
     * response body that the added stbs are present
     * @throws IOException
     * @throws ClientProtocolException
     *
     */
    @Test(groups = "rest")
    public void testAddModel() throws ClientProtocolException, IOException {

        String id = "SA3000";
        String url = BASE_URL + "/models/" + id;
        String name = "SA3000";
        String cap = "[\"VOD\"]";
        String family = "MAC_S";
        HttpEntity entity = new StringEntity("{\"name\":\"" + name + "\", \"capabilities\":" + cap + ", \"family\":\"" + family + "\"}");

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(entity);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpPost, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains("true"));
        //verify that the added model is there in the list
        url = BASE_URL + "/models?id=" + id;
        httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        responseHandler = new BasicResponseHandler();
        responseBody = httpclient.execute(httpget, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains(name));
    }

    /**
     * This method tests the @link{StbModelController#addModel()} API , and verifies the
     * response body that the added stbs are present
     * @throws IOException
     * @throws ClientProtocolException
     *
     */
    @Test(groups = {"rest", "smoke" })
    public void testAddExistingModel() throws ClientProtocolException, IOException {
        String cap = "[\"VOD\"]";
        String url = BASE_URL + "/models/" + MODEL_ID;
        HttpEntity entity = new StringEntity("{\"name\":\"" + MODEL_NAME + "\", \"capabilities\":" + cap + ", \"family\":\"" + "newfamily" + "\"}");

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(entity);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpPost, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains("true"));
        //verify that the added capability is there in the list

        url = BASE_URL + "/models?id=" + MODEL_ID;
        httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        responseHandler = new BasicResponseHandler();
        responseBody = httpclient.execute(httpget, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains(cap));
    }

    /**
     * This method tests the @link{StbModelController#delete()} API , and verifies the
     * response body that the added stbs are present
     * @throws IOException
     * @throws ClientProtocolException
     *
     */
    @Test(groups = "rest")
    public void testDelete() throws ClientProtocolException, IOException {
        String id = MetaStbBuilder.getUID("SA3200");
        addModel(id, INIT_CAP, INIT_FAMILY);
        Assert.assertTrue(modelExists(id));

        int status = removeModel(id);
        Assert.assertTrue(status == HttpStatus.SC_OK, "could not remove the added model");
        //verify that the models() api does not list the removed model
        Assert.assertFalse(modelExists(id));
    }

    /**
     * This method tests the @link{StbModelController#models()} API , and verifies the
     * response body that the added stbs are present
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(groups = "rest")
    public void testModels() throws ClientProtocolException, IOException {
        String url = BASE_URL + "/models?id=" + MODEL_ID;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        Assert.assertNotNull(responseBody);
        // verify that the added model is present

        Assert.assertTrue(responseBody.contains(MODEL_NAME));
        Assert.assertTrue(responseBody.contains(FAMILY));
    }

    /**
     * This method tests the @link{StbModelController#assignModels()} API , and verifies that
     * the family and capabilities get populated
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test(dataProvider = "params")
    public void testAssignModels(String stbInitialCaps, String stbInitialFamily, String stbExpectedFinalCaps, String stbExpectedFinalFamily) throws ClientProtocolException, IOException {
        String id = MetaStbBuilder.getUID("modeltest");
        String modelName = MetaStbBuilder.getUID("mod");
        String mac = "00:00:00:00:00:BB";

        //Add model MODEL with family FAMILY and capabilities CAPABILITIES.

        int status = addModel(modelName, INIT_CAP, INIT_FAMILY);
        Assert.assertTrue(status == HttpStatus.SC_OK, "could not add the model" + modelName);

        // Add stb with model MODEL and null family and null capabilities.

        status = addStb(id, mac, modelName, stbInitialCaps, stbInitialFamily);

        Assert.assertTrue(status == HttpStatus.SC_OK, "could not add the stb" + id);

        //Peform the assignmodels API
        doAssignModels();

        // verify that the stb is now populated with FAMILY AND CAPABILITIES
        verifyResult(id, modelName, stbExpectedFinalCaps, stbExpectedFinalFamily);
    }

    private boolean modelExists(String modelName) throws ClientProtocolException, IOException {
        String url = BASE_URL + "/models?id=" + modelName;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        return (responseBody != null) && responseBody.contains(modelName);
    }

    /**
     * Verifies the stb has the expected values for model, capabilities and family
     * @param id the unique id
     * @param expectedModel the model
     * @param expectedCapabilities the capabilities
     * @param expectedFamily the family
     * @throws ClientProtocolException
     * @throws IOException
     */
    private void verifyResult(String id, String expectedModel, String expectedCapabilities, String expectedFamily) throws ClientProtocolException, IOException {
        HttpClient httpclient = new DefaultHttpClient();
        String url = BASE_URL + "/devices/id/" + id;
        HttpGet httpget = new HttpGet(url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.contains(expectedModel));
        Assert.assertTrue(responseBody.contains(expectedCapabilities));
        Assert.assertTrue(responseBody.contains(expectedFamily));
    }

    /**
     * Adds the stb to the list
     * @param id the unique id for the box
     * @param mac the mac address
     * @param model the model of tthe box
     * @param capability the capability of the box
     * @param family the family of the box
     * @return the status of the operation
     * @throws ClientProtocolException
     * @throws IOException
     */
    private int addStb(String id, String mac, String model, String capability, String family) throws ClientProtocolException, IOException {
        addedDeviceIds.add(id);
        String url = BASE_URL + "/devices/id/" + id;
        HttpEntity entity = null;
        if (null == capability && null == family) {
            entity = new StringEntity("{\"macAddress\":\"" + mac + "\",\"model\":\"" + model + "\",\"capabilities\":null,\"family\":null}");
        } else if (null == capability) {
            entity = new StringEntity("{\"macAddress\":\"" + mac + "\",\"model\":\"" + model + "\",\"capabilities\":null,\"family\":\"" + family + "\"}");
        } else if (null == family) {
            entity = new StringEntity("{\"macAddress\":\"" + mac + "\",\"model\":\"" + model + "\",\"capabilities\":" + capability + ",\"family\":null}");
        }
        HttpPut method = new HttpPut(url);
        method.addHeader("Content-Type", "application/json");
        method.setEntity(entity);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(method);
        int status = getStatus(response);
        return status;
    }

    /**
     * Creates a string entity
     * @param expectedModel the model
     * @param expectedCapabilities the capabilities
     * @param expectedFamily the family
     * @return the string entity
     * @throws UnsupportedEncodingException
     */
    private HttpEntity createStringEntity(String expectedModel, String expectedCapabilities, String expectedFamily) throws UnsupportedEncodingException {
        return new StringEntity("{\"name\":\"" + expectedModel + "\",\"capabilities\":" + expectedCapabilities + ",\"family\":\"" + expectedFamily + "\"}");
    }

    /**
     * Removes the stb from the list
     * @param url the dawg url
     * @return the status of the operation
     * @throws ClientProtocolException
     * @throws IOException
     */
    private int removeStb(String id) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        String url = BASE_URL + "devices/id/" + id;
        HttpDelete delMethod = new HttpDelete(url);
        HttpResponse response = httpClient.execute(delMethod);
        return getStatus(response);
    }

    /**
     * Performs assignmodels
     * @throws ClientProtocolException
     * @throws IOException
     */
    private void doAssignModels() throws ClientProtocolException, IOException {
        String url = BASE_URL + "/assignmodels";
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        Assert.assertNotNull(responseBody);
    }

    /**
     * Adds a new model to the list
     * @param modelName the model name
     * @param capabilities the model capabilities
     * @param family the box family
     * @return the http status of the add function
     * @throws ClientProtocolException
     * @throws IOException
     */
    private int addModel(String modelName, String capabilities, String family) throws ClientProtocolException, IOException {
        addedModelIds.add(modelName);
        String url = BASE_URL + "/models/" + modelName;
        HttpEntity entity = createStringEntity(modelName, capabilities, family);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(entity);
        HttpResponse response = httpclient.execute(httpPost);

        return getStatus(response);
    }

    /**
     * Removes the added model from the list
     * @param id the model id
     * @return the http status of the delete function
     * @throws ClientProtocolException
     * @throws IOException
     */
    private int removeModel(String id) throws ClientProtocolException, IOException {
        String url = BASE_URL + "/models/" + id;

        HttpClient httpclient = new DefaultHttpClient();
        HttpDelete httpDel = new HttpDelete(url);
        httpDel.addHeader("Content-Type", "application/json");

        HttpResponse response = httpclient.execute(httpDel);

        return getStatus(response);
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

    @DataProvider(name = "params")
    public Object[][] keyProvider() {
        return new Object[][]{{null, null, INIT_CAP, INIT_FAMILY }, {null, "FAMILY1", INIT_CAP, "FAMILY1" }, {"[\"CAP2\"]", null, "[\"CAP2\"]", INIT_FAMILY } };
    }
}
