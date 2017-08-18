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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.MetaStbBuilder;

import com.comcast.dawg.config.TestServerConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.helper.DawgHouseRestHelper;
import com.comcast.dawg.helper.DawgPoundRestHelper;
import com.comcast.video.dawg.common.MetaStb;
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
    public void addSTBDeviceToDawgViaPutReq(String deviceId, String macAddress) throws DawgTestException {
        LOGGER.info("Going to add an STB to dawg house with device id {} mac address {}", deviceId, macAddress);
        boolean result = DawgHouseRestHelper.getInstance().addStbToDawg(deviceId, macAddress, null, null, null, null,
            TestConstants.RestReqType.BRIEF_STB_DEVICE_PARAM);
        Assert.assertTrue(result, "Failed to add STB to dawg house");
        LOGGER.info("Has successfully sent PUT request to 'add an STB' to dawg house");
    }

    /**
     * Step definition to verify presence of added STBs in dawg house
     * @throws DawgTestException
     */
    @And("^I should verify all the STB devices added are available in the dawg house$")
    public void verifyAddedStbsInDawg() throws DawgTestException {
        LOGGER.info("Going to verify presence of added STBs in dawg house");
        ArrayList<String> stbsAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STBS);
        boolean result = DawgHouseRestHelper.getInstance().sendGetReqForSTBDeviceList(TestServerConfig.getHouse());
        Assert.assertTrue(result, "Failed to get list of stbs from dawg house");
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        for (String stbId : stbsAdded) {
            Assert.assertTrue(response.asString().contains(stbId), "Failed to verify presence of added STB " + stbId);
        }
        LOGGER.info("Successfully verified presence of added STBs in dawg house");
    }

    /**
     * Step definition to perform (get/delete/retrieve STB) from dawg house with (invalid/valid) device id passed as (path/query) param
     * @param httpMethod
     *          (GET/DELETE/POST)
     * @param restMethod
     *          dawg house rest request 
     * @param validity
     *          Whether STB device id is valid or not
     * @param paramPassAs
     *          Whether parameter is passed as (path/query) param
     * @throws DawgTestException
     */
    @When("^I send (.*) request to '(.*) STB' with (invalid|valid)? device id( as query param)?$")
    public void sendVarReqsForStb(String httpMethod, String restMethod, String validity, String paramPassAs) throws DawgTestException {
        LOGGER.info("Going to send {} request to {} STB with {} device id", httpMethod, restMethod, validity);
        MetaStb testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        boolean result = false;
        String deviceId = null;
        if ("valid".equalsIgnoreCase(validity)) {
            deviceId = testStb.getId();
        } else if ("invalid".equalsIgnoreCase(validity)) {
            deviceId = DawgHouseConstants.INVALID_STB_DEVICE_ID;
        }
        switch (restMethod) {
            case "get":
                // Sending GET request to 'get an STB'
                result = DawgHouseRestHelper.getInstance().stbDeviceExistsInDawg(deviceId);
                break;
            case "delete":
                // Sending (DELETE/GET) request to 'delete STB' by passing param as (path/query)
                DawgHouseRestHelper restHelper = DawgHouseRestHelper.getInstance();
                result = null!= paramPassAs && paramPassAs.contains("query") ? restHelper.removeSTBFromDawgByQuery(
                    deviceId) : restHelper.removeStbFromDawg(deviceId);                
                break;
            case "retrieve":
                // Sending POST request to 'retrieve STB'
                result = DawgHouseRestHelper.getInstance().sendPostReqToRetrieveSTB(deviceId);
                break;
            default:
                throw new DawgTestException("Received invalid Rest request " + restMethod);
        }     
        Assert.assertTrue(("valid".equalsIgnoreCase(validity)) ? result : !result,
            "Failed to check " + restMethod + " with " + validity + " device id");
        LOGGER.info("Successfully sent {} request to {} STB with {} device id", httpMethod, restMethod, validity);
    }

    /**
     * Step definition to 'add an STB' to dawg house via PUT request
     * @param stbDetails
     *          DataTable with STB device details
     * @throws DawgTestException
     */
    @Given("^I send PUT request to 'add an STB' with following details$")
    public void sendPutReqToAddSTBDevice(DataTable stbDetails) throws DawgTestException {
        LOGGER.info("Going to send PUT request to 'add an STB' with details {}", stbDetails.raw().get(1));
        List<String> stbParamVal = stbDetails.raw().get(1);
        List<String> stbParamKey = stbDetails.raw().get(0);
        String stbDeviceId = stbParamVal.get(0);
        MetaStb stbDevice = null;
        boolean result = false;
        if (DawgHouseConstants.MODEL.equalsIgnoreCase(stbParamKey.get(1))) {
            // Setting id and model for the STB
            String stbModel = stbParamVal.get(1);
            stbDevice = new MetaStbBuilder().id(stbDeviceId).model(stbModel).stb();
            result = DawgHouseRestHelper.getInstance().addStbToDawg(stbDeviceId, null, stbModel, null, null,
                stbDevice.getMake(), TestConstants.RestReqType.BRIEF_STB_DEVICE_PARAM);
        } else if (DawgHouseConstants.MAC.equalsIgnoreCase(stbParamKey.get(1))) {
            // Setting id and MAC for the STB
            String macAddress = stbParamVal.get(1);
            stbDevice = new MetaStbBuilder().id(stbDeviceId).mac(macAddress).stb();
            result = DawgHouseRestHelper.getInstance().addStbToDawg(stbDeviceId, macAddress, null, null, null,
                stbDevice.getMake(), TestConstants.RestReqType.BRIEF_STB_DEVICE_PARAM);
        }
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_DEVICE, stbDevice);
        Assert.assertTrue(result, "Failed to add STB device to dawg house");
        LOGGER.info("Has successfully sent PUT request to 'add an STB' with details {}", stbParamVal);
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
                "Failed to get STB by passing " + modelName + " model name as query from dawg house");
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
     * Step definition to verify the presence of STB device id and MAC address 
     * in 'get STB device list' response
     */
    @And("^I should verify that the response contains expected STB device id and MAC address$")
    public void verifyStbDevListPropVal() {
        LOGGER.info("Going to verify presence of STB device id and mac address in response");
        MetaStb testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        // creating the expected fields in response JSON
        String stbIdKeyValPair = DawgHouseConstants.STB_ID_KEY_VAL_PAIR.replace(DawgHouseConstants.JSON_RES_FIELD_VALUE,
            testStb.getId());
        String stbMacKeyValPair = DawgHouseConstants.STB_MAC_KEY_VAL_PAIR.replace(
            DawgHouseConstants.JSON_RES_FIELD_VALUE, testStb.getMacAddress());
        // verifying the presence of expected fields in response JSON
        Assert.assertTrue(
            (response.asString().contains(stbIdKeyValPair)) && (response.asString().contains(stbMacKeyValPair)),
            "Failed to verify presence of STB device id and mac in response");
        LOGGER.info("Successfully verified presence of STB device id and mac address in response");
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
        boolean result = DawgHouseRestHelper.getInstance().updateStbInDawg(testStb);
        Assert.assertTrue(result, "Failed to update STB parameters in dawg house");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_DEVICE, testStb);
        LOGGER.info("Successfully updated already existing STB parameters in dawg house");
    } 

    /**
     * Step definition to verify removal of STB with given device id from dawg house 
     * @throws DawgTestException 
     */
    @And("^I should verify that STB device is removed from dawg house$")
    public void verifyStbRemovalFmDawg() throws DawgTestException {
        LOGGER.info("Going to verify removal of STB from dawg house");
        MetaStb testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        DawgHouseRestHelper.getInstance().sendGetReqForSTBDeviceList(TestServerConfig.getHouse());       
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        Assert.assertFalse(response.asString().contains(testStb.getId()),
            "Failed to verify removal of STB " + testStb.getId());
        LOGGER.info("Successfully verified removal of STB from dawg house");
    }

    /**
     * Step definition to add tags to STB via POST request 
     * @param stbDetails
     *          DataTable with STB device details
     * @throws DawgTestException
     */
    @When("^I send POST request to 'add tag to STB' with below details$")
    public void sendPostReqForTagAdd(DataTable stbDetails) throws DawgTestException {
        LOGGER.info("Going to add tag for STB in dawg");
        List<String> stbParams = stbDetails.raw().get(1);
        String stbTags = stbParams.get(1);
        String stbDeviceId = stbParams.get(0);
        boolean result = DawgHouseRestHelper.getInstance().sendPostReqToAddStbTags(stbTags, stbDeviceId);
        Assert.assertTrue(result, "Failed to add tag for given STB");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_TAGS, stbTags);
        LOGGER.info("Successfully added tag for STB in dawg");
    }

    /**
     * Step definition to verify whether tags are (added/removed/updated) or not 
     * @param tagOperation
     *          Whether tag is added/removed/updated
     * @throws DawgTestException
     */
    @And("^I should verify that tags are (added|updated|removed) for the same STB$")
    public void verifySTBTagging(String tagOperation) throws DawgTestException {
        LOGGER.info("Going to verify that tags are {} for STB in dawg", tagOperation);
        MetaStb testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        boolean result = DawgHouseRestHelper.getInstance().stbDeviceExistsInDawg(testStb.getId());
        Assert.assertTrue(result, "Failed to verify presence of STB in dawg");
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        String responseStr = response.asString();
        String expTag = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_TAGS);
        switch (tagOperation) {
            case "added":
                Assert.assertTrue(responseStr.contains(expTag),
                    "Failed to verify addition of expected tag " + expTag + " in STB");
                break;
            case "updated":
                String[] existingTags = expTag.split(",");
                StringBuilder stbTags = new StringBuilder();
                for (String stbTag : existingTags) {
                    if (!responseStr.contains(stbTag)) {
                        stbTags.append(stbTag).append(",");
                    }
                }
                Assert.assertTrue((0 == stbTags.toString().trim().length()),
                    "Failed to update tag/s " + stbTags + " for given stb " + testStb.getId());
                break;
            case "removed":
                Assert.assertFalse(responseStr.contains(expTag), "Failed to verify tag removal from given STB");
                break;
            default:
                throw new DawgTestException("Invalid tag operation " + tagOperation + "passed");
        }
        LOGGER.info("Successfully verified that tags are {} for STB in dawg", tagOperation);
    }

    /**
     * Step definition to remove tags from STB via POST request 
     * @throws DawgTestException
     */
    @When("^I send POST request to 'remove tag from STB' with same STB device id and tag name$")
    public void sendPostReqForTagRemoval() throws DawgTestException {
        LOGGER.info("Going to remove tag from STB in dawg");
        MetaStb testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        String testTag = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_TAGS);
        boolean result = DawgHouseRestHelper.getInstance().sendPostReqToRemoveStbTags(testTag, testStb.getId());
        Assert.assertTrue(result, "Failed to remove tag from STB" + testStb.getId());
        LOGGER.info("Successfully removed tag for STB in dawg");
    }

    /**
     * Step definition to update tags for existing STB via POST request 
     * @param stbDetails
     *          DataTable with STB device details
     * @throws DawgTestException
     */
    @When("^I send POST request to 'add tag to same STB' with below details$")
    public void sendPostReqToUpdateStbTag(DataTable stbDetails) throws DawgTestException {
        LOGGER.info("Going to update STB tag in dawg");
        List<String> stbParams = stbDetails.asList(String.class);
        MetaStb testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        String testTag = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_TAGS);
        // Storing the already existing STB tags
        StringBuilder stbTags = new StringBuilder();
        String tagsToUPdate = stbParams.get(1);
        stbTags.append(testTag).append(",");
        boolean result = DawgHouseRestHelper.getInstance().sendPostReqToAddStbTags(tagsToUPdate, testStb.getId());
        Assert.assertTrue(result, "Failed to update tag for given STB");
        // Appending the updated tag to existing tags
        stbTags.append(tagsToUPdate);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_TAGS, stbTags.toString());
        LOGGER.info("Successfully updated STB tag in dawg");
    }
}
