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

import java.util.List;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.helper.DawgHouseRestHelper;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.zucchini.TestContext;
import com.jayway.restassured.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

/**
 * This class contains implementation of dawg house REST services specific to STB Model 
 * @author Jeeson
 *
 */
public class DawgHouseSTBModelRestGlue {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgHouseSTBModelRestGlue.class);

    /**
     * Step definition to 'add an STB model' to dawg house via POST request
     * @param data
     *          DataTable with STB model details
     * @throws DawgTestException
     */
    @Given("^I send POST request to 'add STB model' with following properties$")
    public void sendPostReqToAddSTBModel(DataTable data) throws DawgTestException {
        LOGGER.info("Going to send POST request to 'add STB model' with details {}", data.raw().get(1));
        List<String> stbParam = data.raw().get(1);
        MetaStb modelSTB = DawgHouseRestHelper.getInstance().createTestStb(stbParam.get(0), stbParam.get(1),
            stbParam.get(2), stbParam.get(3), null);

        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_MODEL, modelSTB);
        boolean result = DawgHouseRestHelper.getInstance().addSTBModelToDawg(stbParam.get(1), stbParam.get(2),
            stbParam.get(3));
        Assert.assertTrue(result, "Failed to add STB model to dawg house");
        LOGGER.info("Has successfully sent POST request to 'add STB model' with details {}", data.raw().get(1));
    }

    /**
     * Step definition to verify that STB model is added to dawg house
     * @throws DawgTestException
     */
    @And("^I should verify that added STB model is available in the dawg house$")
    public void verifySTBModelAdditionToDawgHouse() throws DawgTestException {
        LOGGER.info("Going to verify added STB model");
        MetaStb modelSTB = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_MODEL);
        boolean result = DawgHouseRestHelper.getInstance().stbModelExistsInDawg(modelSTB.getModel().name());
        Assert.assertTrue(result, "Failed to verify presence of STB model in dawg house");
        LOGGER.info("Successfully verified added STB model");
    }

    /**
     * Step definition to 'get STB model' from dawg house via GET request
     * @throws DawgTestException
     */
    @When("^I send GET request to 'get STB model' with valid model id$")
    public void sendGetReqForSTBModel() throws DawgTestException {
        LOGGER.info("Going to send GET request to 'get STB model' with valid model id");
        MetaStb modelSTB = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_MODEL);
        boolean result = DawgHouseRestHelper.getInstance().sendGetReqForSTBModel(modelSTB.getModel().name());
        Assert.assertTrue(result, "Failed to get STB model from dawg house");
        LOGGER.info("Has successfully sent GET request to 'get STB model' with valid model id");
    }

    /**
     * Step defintion to verify 'get STB model' response
     */
    @And("^I should verify that the response contains expected model and family name$")
    public void verifyGetSTBModelResponse() {
        LOGGER.info("Going to verify 'get STB model' response");
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        MetaStb modelSTB = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_MODEL);   
     
        Assert.assertTrue(response.asString().contains(modelSTB.getModel().name()),
            "Failed to verify the STB model name");
        Assert.assertTrue(response.asString().contains(modelSTB.getFamily().name()),
            "Failed to verify the STB model family name");      
        LOGGER.info("Successfully verified 'get STB model' response");
    }

    /**
     * Step definition to 'delete STB model' from dawg house via DELETE request
     * @throws DawgTestException
     */
    @When("^I send DELETE request to 'delete STB model' with valid STB model id as path param$")
    public void sendDeleteReqToRemoveSTBModel() throws DawgTestException {
        LOGGER.info("Going to send DELETE request to 'delete STB model' with valid model id");
        MetaStb modelSTB = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_MODEL);
        boolean result = DawgHouseRestHelper.getInstance().removeSTBModelFromDawg(modelSTB.getId());
        Assert.assertTrue(result, "Failed to remove STB model from dawg house");
        LOGGER.info("Has successfully sent DELETE request to 'delete STB model' with valid model id");
    }

    /**
     * Step definition to verify that STB model is removed from dawg house
     * @throws DawgTestException
     */
    @And("^I should verify that the STB is removed from dawg house$")
    public void verifySTBModelRemovalFromDawg() throws DawgTestException {
        LOGGER.info("Going to verify that the STB is removed from dawg house");
        MetaStb modelSTB = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_MODEL);
        boolean result = DawgHouseRestHelper.getInstance().stbModelExistsInDawg(modelSTB.getId());
        Assert.assertFalse(result, "Failed to remove STB model from dawg house");
        LOGGER.info("Successfully verified that the STB is removed from dawg house");
    }

    /**
     * Step definition to update existing STB model details via POST request
     * @param data
     *          DataTable with STB model details
     * @throws DawgTestException
     */
    @When("^I send POST request to update same STB model with following properties$")
    public void sendPostReqToUpdateSTBModel(DataTable data) throws DawgTestException {
        LOGGER.info("Going to send POST request to update same STB model with details {}", data.raw().get(1));
        List<String> stbParam = data.raw().get(1);
        MetaStb existingModelSTB = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_MODEL);
        MetaStb newModelSTB = DawgHouseRestHelper.getInstance().createTestStb(existingModelSTB.getId(), stbParam.get(0),
            stbParam.get(1), stbParam.get(2), null);

        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_MODEL, newModelSTB);
        boolean result = DawgHouseRestHelper.getInstance().addSTBModelToDawg(stbParam.get(0), stbParam.get(1),
            stbParam.get(2));
        Assert.assertTrue(result, "Failed to add STB model to dawg house");
        LOGGER.info("Has successfully sent POST request to update same STB model with details {}", data);
    }

    /**
     * Step definition to verify that STB model details updated are reflected in dawg house
     * @throws DawgTestException
     */
    @And("^I should verify that the STB model fields are updated in the dawg house$")
    public void verifyUpdatedSTBModel() throws DawgTestException {
        LOGGER.info("Going to verify updated STB model");
        MetaStb modelSTB = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_MODEL);
        boolean result = DawgHouseRestHelper.getInstance().stbModelExistsInDawg(modelSTB.getId());
        Assert.assertTrue(result, "Failed to verify updates of STB model in dawg house");
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        Assert.assertTrue(response.asString().contains(modelSTB.getModel().name()),
            "Failed to verify the STB model name");
        Assert.assertTrue(response.asString().contains(modelSTB.getFamily().name()),
            "Failed to verify the STB model family name");
        Assert.assertTrue(response.asString().contains(modelSTB.getCapabilities().toString().replaceAll("\\[|\\]", "")),
            "Failed to verify the STB model capabilities");
        LOGGER.info("Successfully verified the presence of fields in updated STB model");
    }

    /**
     * Step definition to 'add an STB' to dawg house via PUT request
     * @param id
     *          STB device id
     * @param mac
     *          STB MAC address
     * @param model
     *          STB model name
     * @param capability
     *          STB model capabilities
     * @param family
     *          STB model family name
     * @throws DawgTestException 
     */
    @When("^I send PUT request to 'add an STB' with STB device id (.*), mac address (.*), STB model name (.*), model capabilities (.*) and model family (.*)$")
    public void sendPutReqToAddStbToDawg(String id, String mac, String model, String capability, String family) throws DawgTestException {
        LOGGER.info("Going to send PUT request to add STB to dawg house");
        boolean result = DawgHouseRestHelper.getInstance().addStbToDawg(id, mac, model, capability, family, null,
            TestConstants.RestReqType.DETAILED_STB_DEVICE_PARAM);
        Assert.assertTrue(result, "Failed to add STB to dawg house");
        LOGGER.info("Has successfully sent PUT request to 'add an STB' to dawg house");
    }

    /**
     * Step definition to assign STB models to dawg house
     * @throws DawgTestException
     */
    @When("^I send GET request to 'assign models' service$")
    public void sendGetReqToAssignModels() throws DawgTestException {
        LOGGER.info("Going to send GET request to assign STB models to dawg house");
        boolean result = DawgHouseRestHelper.getInstance().assignModelDawg();
        Assert.assertTrue(result, "Failed to assign STB models to dawg house");
        LOGGER.info("Has successfully sent GET request to assign STB models to dawg house");
    }

    /**
     * Step definition to verify assign models service in dawg house
     * @param id
     *          STB device id
     * @param mac
     *          STB MAC address
     * @param expectedModel
     *          expected STB model name
     * @param expectedCaps
     *          expected STB model capabilities
     * @param expectedFamily
     *          expected STB model family name
     * @throws DawgTestException 
     */
    @And("^I should verify that added STB device contains STB device id (.*), mac address (.*), STB model name (.*), expected model capabilities (.*) and model family (.*)$")
    public void verifyAssignModels(String id, String mac, String expectedModel, String expectedCaps, String expectedFamily) throws DawgTestException {
        LOGGER.info("Going to verify assign models service in dawg house");
        boolean result = DawgHouseRestHelper.getInstance().stbDeviceExistsInDawg(id);       
        Assert.assertTrue(result, "Failed to assign STB models to dawg house");
        Response getModelRes = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        Assert.assertTrue(getModelRes.asString().contains(expectedModel), "Failed to verify the STB model name");
        Assert.assertTrue(getModelRes.asString().contains(expectedCaps), "Failed to verify the STB model capabilities");
        Assert.assertTrue(getModelRes.asString().contains(expectedFamily),
            "Failed to verify the STB model family name");
        LOGGER.info("Successfully verified assign models service in dawg house");
    }

    /**
     * Step definition to 'add an STB model' to dawg house via POST request with given params
     * @param id
     *          STB device id
     * @param model
     *          STB model
     * @throws DawgTestException
     */
    @Given("^I send POST request to 'add STB model' with STB device id (.*), STB model name (.*)$")
    public void sendPostReqToAddSTBModelWithGivenParams(String id, String model) throws DawgTestException {
        LOGGER.info("Going to send POST request to 'add STB model' with STB id {}, STB model {}", id, model);
        MetaStb modelSTB = DawgHouseRestHelper.getInstance().createTestStb(id, model, TestConstants.INIT_MODEL_CAPS,
            TestConstants.INIT_MODEL_FAMILY, null);

        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_MODEL, modelSTB);
        boolean result = DawgHouseRestHelper.getInstance().addSTBModelToDawg(model, TestConstants.INIT_MODEL_CAPS,
            TestConstants.INIT_MODEL_FAMILY);
        Assert.assertTrue(result, "Failed to add STB model to dawg house");
        LOGGER.info("Has successfully sent POST request to 'add STB model' with STB id {}, STB model {}", id, model);
    }
}
