/**
 * Copyright 2017 Comcast Cable Communications Management, LLC
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
package com.comcast.dawg.glue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.helper.DawgHouseRestHelper;
import com.comcast.dawg.helper.DawgPoundRestHelper;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.dawg.MetaStbBuilder;
import com.comcast.video.dawg.house.Reservation;
import com.comcast.zucchini.TestContext;
import com.jayway.restassured.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * This class contains implementation of dawg house REST services specific to STB Device 
 * @author Jeeson
 *
 */
public class DawgHouseSTBDeviceRestGlue {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgHouseSTBDeviceRestGlue.class);

    /**
     * Step definition to 'add an STB' to dawg house via PUT request
     * @param deviceId
     *          STB device id
     * @param mac
     *          STB MAC address
     * @throws DawgTestException 
     */
    @When("^I send PUT request to 'add an STB' with device id (.*) and mac address (.*)$")
    public void addSTBDeviceToDawgViaPutReq(String deviceId, String mac) throws DawgTestException {
        LOGGER.info("Going to add an STB to dawg house with device id {} mac address {}", deviceId, mac);
        boolean result = DawgHouseRestHelper.getInstance().addStbToDawg(deviceId, mac, null, null, null, null,
            TestConstants.RestReqType.BRIEF_STB_DEVICE_PARAM);
        Assert.assertTrue(result, "Failed to add STB to dawg house");
        LOGGER.info("Has successfully sent PUT request to 'add an STB' to dawg house");
    }

    /**
     * Step definition to verify presence of added STBs in dawg house
     * @throws DawgTestException
     */
    @And("^I should verify all the STB devices added are available in the dawg house$")
    public void verifyAddedStbs() throws DawgTestException {
        LOGGER.info("Going to verify presence of added STBs in dawg house");
        ArrayList<String> stbsAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STBS);
        boolean result = DawgHouseRestHelper.getInstance().sendGetReqForSTBDeviceList();
        Assert.assertTrue(result, "Failed to get list of stbs from dawg house");
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        for (String stbId : stbsAdded) {
            Assert.assertTrue(response.asString().contains(stbId), "Failed to verify presence of added STB " + stbId);
        }
        LOGGER.info("Successfully verified presence of added STBs in dawg house");
    }

    /**
     * Step definition to 'delete STB' from dawg house with invalid device id via GET request
     * @throws DawgTestException
     */
    @When("^I send GET request to 'delete STB' with invalid device id as query param$")
    public void removeSTBwithInvalidIdAsQuery() throws DawgTestException {
        LOGGER.info("Going to remove STB with invalid device id via GET request");
        boolean result = DawgHouseRestHelper.getInstance().removeSTBFromDawgByQuery(
            DawgHouseConstants.INVALID_STB_DEVICE_ID);
        Assert.assertFalse(result, "Failed to check remove STB with invalid device id");
        LOGGER.info("Successfully sent GET request to remove STB with invalid device id");
    }

    /**
     * Step definition to perform (get STB/delete STB/retrieve STB) from dawg house with invalid device id
     * @param httpMethod
     *          (GET/DELETE/POST)
     * @param restMethod
     *          dawg house rest request 
     * @throws DawgTestException
     * @throws UnsupportedEncodingException 
     */
    @When("^I send (.*) request to '(.*) STB' with invalid device id$")
    public void sendGetOrDeleteStbReq(String httpMethod, String restMethod) throws DawgTestException {
        LOGGER.info("Going to send {} request to {} with invalid device id", httpMethod, restMethod);
        boolean result = false;
        switch (restMethod) {
            case "get":
                result = DawgHouseRestHelper.getInstance().stbDeviceExistsInDawg(
                    DawgHouseConstants.INVALID_STB_DEVICE_ID);
                break;
            case "delete":
                result = DawgHouseRestHelper.getInstance().removeStbFromDawg(DawgHouseConstants.INVALID_STB_DEVICE_ID);
                break;
            case "retrieve":
                result = DawgHouseRestHelper.getInstance().sendPostReqToRetrieveSTB(
                    DawgHouseConstants.INVALID_STB_DEVICE_ID);
                break;
            default:
                LOGGER.error("Received invalid Rest request " + restMethod);
        }
        Assert.assertFalse(result, "Failed to check " + restMethod + "with invalid device id");
        LOGGER.info("Successfully sent {} request to {} with invalid device id", httpMethod, restMethod);
    }

    /**
     * Step definition to 'add an STB' to dawg house via PUT request
     * @param data
     *          DataTable with STB device details
     * @throws DawgTestException
     */
    @Given("^I send PUT request to 'add an STB' with following details$")
    public void sendPutReqToAddSTBDevice(DataTable data) throws DawgTestException {
        LOGGER.info("Going to send PUT request to 'add an STB' with details {}", data.raw().get(1));
        List<String> stbParam = data.raw().get(1);
        MetaStb stbDevice = new MetaStbBuilder().id(stbParam.get(0)).model(stbParam.get(1)).stb();

        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_DEVICE, stbDevice);
        boolean result = DawgHouseRestHelper.getInstance().addStbToDawg(stbParam.get(0), null, stbParam.get(1), null,
            null, stbDevice.getMake(), TestConstants.RestReqType.BRIEF_STB_DEVICE_PARAM);
        Assert.assertTrue(result, "Failed to add STB device to dawg house");
        LOGGER.info("Has successfully sent PUT request to 'add an STB' with details {}", data.raw().get(1));
    }

    /**
     * Step definition to 'get STB' from dawg house via GET request 
     * with model name given as query param
     * @throws DawgTestException
     */
    @When("^I send GET request to 'get STB' with same model name as query param$")
    public void sendGetReqForStbDevice() throws DawgTestException {
        LOGGER.info("Going to send GET request to 'get STB' with valid model name");
        MetaStb modelSTB = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        boolean result = DawgHouseRestHelper.getInstance().sendGetReqByQueryForSTBDevice(modelSTB.getModel().name());
        Assert.assertTrue(result, "Failed to get STB by passing model name as query from dawg house");
        LOGGER.info("Has successfully sent GET request to 'get STB' with valid model name");
    }

    /**
     * Step definition to verify 'get STB' by query response
     */
    @And("^I verify that the response contains expected STB model name$")
    public void verifyGetStbByQueryRes() {
        LOGGER.info("Going to verify 'get STB' by query response");
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        MetaStb modelSTB = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        Assert.assertTrue(response.asString().contains(modelSTB.getModel().name()),
            "Failed to to verify 'get STB' by query response");
        LOGGER.info("Successfully verified 'get STB' by query response");
    }

    /**
     * Step definition to 'get STB' by query with (valid/invalid/illegal) model name
     * @param modelType
     *          Whether model name is (valid/invalid/illegal)
     * @param modelName
     *          STB model name
     * @throws DawgTestException
     */
    @When("^I send GET request to 'get STB' with (valid|invalid|illegal) model name (.*) as query param$")
    public void getStbByQueryViaGetReq(String modelType, String modelName) throws DawgTestException {
        LOGGER.info("Going to send GET request to 'get STB' with {} model name {}", modelType, modelName);
        boolean result = DawgHouseRestHelper.getInstance().sendGetReqByQueryForSTBDevice(modelName);
        if ("valid".equalsIgnoreCase(modelType)) {
            Assert.assertTrue(result, "Failed to get STB by passing model name as query from dawg house");
        } else if ("invalid".equalsIgnoreCase(modelType) || "illegal".equalsIgnoreCase(modelType)) {
            Assert.assertFalse(result,
                "Failed to get STB by passing " + modelType + " model name as query from dawg house");
        }
        LOGGER.info("Has successfully sent GET request to 'get STB' with {} model name {}", modelType, modelName);
    }

    /**
     * Step definition to populate STB with (valid/invalid) client token
     * @param clientTokenType
     *          Whether client token is valid or not
     * @throws DawgTestException
     */
    @When("^I send POST request to 'populate' with (valid|invalid) client token$")
    public void populateStbViaPostReq(String clientTokenType) throws DawgTestException {
        LOGGER.info("Going to send POST request to populate STB with {} client token", clientTokenType);
        boolean result = false;
        if ("valid".equalsIgnoreCase(clientTokenType)) {
            result = DawgHouseRestHelper.getInstance().populateStbInDawg(DawgHouseConstants.VALID_CLIENT_TOKEN);
        } else if ("invalid".equalsIgnoreCase(clientTokenType)) {
            result = DawgHouseRestHelper.getInstance().populateStbInDawg(DawgHouseConstants.INVALID_CLIENT_TOKEN);
        }
        Assert.assertTrue(result, "Failed to send POST request to populate STB ");
        LOGGER.info("Successfully sent POST request to populate STB with {} client token", clientTokenType);
    }

    /**
     * Step definition to 'reserve an STB' in dawg pound
     * @throws DawgTestException
     */
    @When("^I send POST request to 'reserve an STB' in dawg pound$")
    public void sendPostReqToReserveStb() throws DawgTestException {
        LOGGER.info("Going to send POST request to reserve STB in dawg pound");
        MetaStb testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);

        // Setting reservation parameters
        Reservation reservation = new Reservation(testStb.getId(), DawgHouseConstants.VALID_CLIENT_TOKEN, System.currentTimeMillis() + TimeUnit.DAYS.toMillis(
            365), true);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_RESERVATION, reservation);
        boolean result = DawgPoundRestHelper.getInstance().reserveStbInDawg(reservation);
        Assert.assertTrue(result, "Failed to reserve STB in dawg pound");
        LOGGER.info("Successfully sent POST request to reserve STB {} ", testStb.getId());
    }

    /**
     * Step definition to verify reserved STB parameters in dawg house via POST request
     * @throws DawgTestException
     */
    @Then("^I should verify that reserved STB contains expected reservation token and (?:updated |)STB device make$")
    public void verifyStbReservation() throws DawgTestException {
        LOGGER.info("Going to verify STB reservation in dawg pound");
        MetaStb testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        boolean result = DawgHouseRestHelper.getInstance().sendGetReqByQueryForSTBDevice(testStb.getId());
        Assert.assertTrue(result, "Failed to get STB by passing model name as query from dawg house");
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        Reservation reservation = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_RESERVATION);
        Assert.assertTrue(response.asString().contains(testStb.getMake()),
            "Failed to verify presence of reserved STB " + testStb.getId());
        Assert.assertTrue(response.asString().contains(reservation.getToken()),
            "Failed to verify presence of reservation token " + reservation.getToken());
        LOGGER.info("Successfully verified STB reservation in dawg pound");
    }

    /**
     * Step definition to update parameters of an already existing STB in dawg house
     * @throws DawgTestException
     */
    @When("^I send POST request to 'update STB' with same STB device id and new STB device make$")
    public void sendPostReqToUpdateStb() throws DawgTestException {
        LOGGER.info("Going to update already existing STB parameters in dawg house");
        MetaStb testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        testStb.setMake(DawgHouseConstants.STB_MAKE);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_DEVICE, testStb);
        boolean result = DawgHouseRestHelper.getInstance().updateStbInDawg(testStb);
        Assert.assertTrue(result, "Failed to update STB parameters in dawg house");
        LOGGER.info("Successfully updated already existing STB parameters in dawg house");
    }
}
