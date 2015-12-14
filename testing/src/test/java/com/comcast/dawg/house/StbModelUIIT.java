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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.comcast.dawg.MetaStbBuilder;
import com.comcast.dawg.TestServers;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.constants.TestConstants.Capability;
import com.comcast.dawg.constants.TestConstants.Family;
import com.comcast.dawg.house.pages.ModelPage;
import com.comcast.dawg.test.base.UITestBase;
import com.comcast.video.dawg.common.DawgModel;
import com.comcast.video.dawg.house.DawgHouseClient;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * The UI Testing for Dawg house's STB model.
 *
 * @author  Pratheesh T.K.
 */
public class StbModelUIIT extends UITestBase {

    /** Model page object. */
    private ModelPage modelPage = null;

    /** Model name for deletion test. */
    private String existingModelNameForDeletion = null;

    /** Model name for editing test. */
    private String existingModelNameForEditing = null;

    /** Model name for non modifiable test. */
    private String existingModelName = null;

    /** Cached model name for final deletion. */
    private List<String> cachedDawgModelNames = new ArrayList<String>();

    @BeforeClass(description = "Method to initialize all prerequisite for testing.")
    protected void prepareComponents() {

        try {
            existingModelNameForDeletion = createTestStbModelAndCache().getName();
            existingModelNameForEditing = createTestStbModelAndCache().getName();
            existingModelName = createTestStbModelAndCache().getName();
        } catch (IOException e) {
            Assert.fail("Failed to add the required test STB model for deletion.", e);
        }

        modelPage = new ModelPage(driver);
    }

    @AfterClass(description = "Method to clear all the models that is added for the test cases.")
    protected void clearAllModels() {

        for (String modelName : cachedDawgModelNames) {

            try {
                logger.info("About to delete STB model : " + modelName);

                int responseStatus = deleteStbModel(modelName);
                logger.info("Delete response status : " + responseStatus);

                if (HttpServletResponse.SC_OK != responseStatus) {
                    logger.error(String.format("Failed to remove the STB model %s. Response returned is %s.",
                                modelName, responseStatus));
                }
            } catch (IOException e) {
                logger.error("Failed to remove the STB model : " + modelName, e);
            }
        }
    }

    @Test(description = "Test to validate the addition of new capability name on model overlay page.")
    protected void testAdditionOfCapabilities() {

        modelPage.get();
        modelPage.clickOnAddModelButton();

        // Getting all the list of capabilities available on model overlay.
        List<String> modelOverlayCapabilities = modelPage.getAllCapabilityNamesOnAlreadyLoadedModelOverlay();

        String neverPreviouslyAddedCapabilityName = Capability.NEVER_ADD_CAP.name();

        // Validating to see that planned addition of capability is not available on the model
        // overlay page.
        Assert.assertFalse(modelOverlayCapabilities.contains(neverPreviouslyAddedCapabilityName),
                "Capability planned for addition is already available : " +
                neverPreviouslyAddedCapabilityName);

        // Adding the new capability.
        modelPage.addCapabilityOnAlreadyLoadedModelOverlay(neverPreviouslyAddedCapabilityName);

        // Validating the newly added capability availability on the page.
        modelOverlayCapabilities = modelPage.getAllCapabilityNamesOnAlreadyLoadedModelOverlay();
        Assert.assertTrue(modelOverlayCapabilities.contains(neverPreviouslyAddedCapabilityName),
                "Failed to add the capability on model overlay page.");

        // Closing the model overlay.
        modelPage.clickOnModelOverlayCloseImage();

        // Reopening the overlay again to verify that the capability previously added does
        // persist on the page.
        modelPage.clickOnAddModelButton();
        modelOverlayCapabilities = modelPage.getAllCapabilityNamesOnAlreadyLoadedModelOverlay();
        Assert.assertTrue(modelOverlayCapabilities.contains(neverPreviouslyAddedCapabilityName),
                "Closing and reopening of model overlay does not persist the capability previously added : " +
                neverPreviouslyAddedCapabilityName);
    }

    @Test(description = "Test to validate the alert message while trying to duplicate the capability on model overlay page.")
    protected void testAlertOnDuplicationOfCapabilities() {

        modelPage.get();
        modelPage.clickOnAddModelButton();

        String alreadyAvailableCapability = Capability.AVAILABLE_TEST_CAP.name();

        // Validating the capability planned for testing is already available on the page.
        List<String> modelOverlayCapabilities = modelPage.getAllCapabilityNamesOnAlreadyLoadedModelOverlay();
        Assert.assertTrue(modelOverlayCapabilities.contains(alreadyAvailableCapability),
                "Capability required as a precondition is not available on the model overlay page.");

        // Entering the already available capability.
        modelPage.typeCapabilityOnAlreadyLoadedModelOverlay(alreadyAvailableCapability);

        // Clicking on add capability button.
        modelPage.clickAddCapabilityButtonOnAlreadyLoadedModelOverlay();

        Assert.assertTrue(isAlertPresent(),
                "No alert message displayed while trying to add a duplicate capability entry.");

        Alert alert = driver.switchTo().alert();

        // Validates the alert message text.
        Assert.assertEquals(alert.getText(),
                alreadyAvailableCapability + TestConstants.CAPABILITY_EXIST_ALERT_MSG_SUFFIX,
                "Alert message while trying to duplicate capability is different from what expected.");

    }

    @Test(description = "Test to validate addition of new family name on model overlay page.")
    protected void testAdditionOfFamily() {
        modelPage.get();
        modelPage.clickOnAddModelButton();

        List<String> familyList = modelPage.getAllFamilyNamesOnAlreadyLoadedModelOverlay();

        String neverPreviouslyAddedFamilyName = Family.NEVER_ADD_FAMILY.name();

        // Validating to see that planned addition of family is not available on the model
        // overlay page.
        Assert.assertFalse(familyList.contains(neverPreviouslyAddedFamilyName),
                "Family planned for addition is already available : " + neverPreviouslyAddedFamilyName);

        modelPage.addFamilyOnAlreadyLoadedModelOverlay(neverPreviouslyAddedFamilyName);

        // Validating the newly added family availability on the page.
        familyList = modelPage.getAllFamilyNamesOnAlreadyLoadedModelOverlay();
        Assert.assertTrue(familyList.contains(neverPreviouslyAddedFamilyName),
                "Failed to add the new family on model overlay page.");

        // Closing the model overlay.
        modelPage.clickOnModelOverlayCloseImage();

        // Reopening the overlay again to verify that the family previously added does
        // persist on the page.
        modelPage.clickOnAddModelButton();
        familyList = modelPage.getAllFamilyNamesOnAlreadyLoadedModelOverlay();
        Assert.assertTrue(familyList.contains(neverPreviouslyAddedFamilyName),
                "Closing and reopening of model overlay does not persist the family previously added : " +
                neverPreviouslyAddedFamilyName);
    }

    @Test(description = "Test to validate alert message while trying to duplicate the family on model overlay page.")
    protected void testAlertOnDuplicationOfFamily() {

        modelPage.get();
        modelPage.clickOnAddModelButton();

        String alreadyExistingFamilyName = Family.AVAILABLE_TEST_FAMILY.name();

        // Validating the family planned for testing already exist on the page.
        List<String> modelOverlayFamilies = modelPage.getAllFamilyNamesOnAlreadyLoadedModelOverlay();
        Assert.assertTrue(modelOverlayFamilies.contains(alreadyExistingFamilyName),
                "Family required as a precondition is not available on the model overlay page.");

        // Entering the already available family.
        modelPage.typeFamilyOnAlreadyLoadedModelOverlay(alreadyExistingFamilyName);

        // Clicking on add family button.
        modelPage.clickAddFamilyButtonOnAlreadyLoadedModelOverlay();

        Assert.assertTrue(isAlertPresent(),
                "No alert message displayed while trying to add a duplicate family entry.");

        Alert alert = driver.switchTo().alert();

        // Validates the alert message text.
        Assert.assertEquals(alert.getText(),
                alreadyExistingFamilyName + TestConstants.FAMILY_EXIST_ALERT_MSG_SUFFIX,
                "Alert message while trying to duplicate family is different from what expected.");
    }

    @Test(description = "Test to validate the addition of a new model on model config page.")
    public void testAddModel() {
        modelPage.get();

        // Get a unique model name for addition.
        String testModelName = getATestModelName();

        String newToAddCapability = Capability.NEW_TEST_CAP1.name();
        String newToAddFamily = Family.NEW_TEST_FAMILY1.name();

        // Launches the model dialog box and fill in all the required field and click on save
        // button.
        modelPage.addModel(testModelName, newToAddFamily,
                newToAddCapability);

        // Caching the model name for later deletion.
        cachedDawgModelNames.add(testModelName);

        // Validate whether the model page is loaded or not.
        modelPage.isLoaded();
        Assert.assertTrue(
                modelPage.getAllModelNames().contains(testModelName),
                String.format(
                    "Newly added model(%s) does not get displayed after successful submission of model dialog box.",
                    testModelName));

        // Validates the capabilities specified get reflected against the model added in the UI.
        String capabilities = modelPage.getCapabilitiesOfStbModel(testModelName);
        Assert.assertTrue(
                capabilities.contains(newToAddCapability),
                String.format(
                    "Capability(%s) of newly added model does not get reflected in the model configuration page." +
                    " Capabilities displayed are %s.",
                    newToAddCapability,
                    capabilities));

        // Validates the family name specified get reflected against the model added in the UI.
        String family = modelPage.getFamilyOfStbModel(testModelName);
        Assert.assertTrue(
                family.contains(newToAddFamily),
                String.format("Family(%s) of newly added model does not get reflected in the model configuration page." +
                    " Family displayed in UI is %s.",
                    newToAddFamily,
                    family));

        // Validation to see that model information got added to the DB.
        failIfExpectedModelInfoDoesNotMatchesWithDb(testModelName, newToAddFamily, newToAddCapability);
    }

    @Test(description = "Test to validate the edit option on model config page.")
    public void testEditModel() {
        modelPage.get();

        // Click on model row to open the edit dialog box.
        modelPage.clickOnStbModelRowElement(existingModelNameForEditing);
        Assert.assertTrue(modelPage.isModelOverlayDisplayed(),
                "Failed to launch model dialog box on clicking the model name on the config page.");
        Assert.assertTrue(modelPage.isModelNameTextFieldDisabledOnModelOverlay(),
                "On model dialog box, model name text field is in enabled state.");
        Assert.assertEquals(modelPage.getModelNameTextInputElement().getAttribute("value"), existingModelNameForEditing,
                "Opened edit window does not belong to expected model name : " +
                existingModelNameForEditing);

        String newToIncludeCapability = Capability.NEW_TEST_CAP2.name();
        String newToIncludeFamily = Family.NEW_TEST_FAMILY2.name();

        // Fill in all the editable elements.
        modelPage.fillFamilyAndCapabilitiesOnAlreadyLoadedModelOverlay(newToIncludeFamily,
                newToIncludeCapability);

        // Click on save button to update the changes.
        modelPage.clickSaveButtonOnAlreadyLoadedModelOverlay();

        // Validate the newly included capability get reflected in the UI.
        String capabilities = modelPage.getCapabilitiesOfStbModel(existingModelNameForEditing);
        Assert.assertTrue(capabilities.contains(newToIncludeCapability),
                String.format(
                    "Addition of new capability(%s) does not get reflected in the model configuration page." +
                    " Capabilities currently available are %s.",
                    newToIncludeCapability,
                    capabilities));

        // Validate the newly selected family get reflected in the UI.
        String family = modelPage.getFamilyOfStbModel(existingModelNameForEditing);
        Assert.assertTrue(family.contains(newToIncludeFamily),
                String.format(
                    "Addition of new family(%s) does not get reflected in the model configuration page." +
                    " Family of STB model is %s.",
                    newToIncludeFamily,
                    family));

        String alreadyPresentCapability = Capability.AVAILABLE_TEST_CAP.name();

        // Validation to see that model information got added to the DB.
        failIfExpectedModelInfoDoesNotMatchesWithDb(existingModelNameForEditing, newToIncludeFamily,
                newToIncludeCapability, alreadyPresentCapability);

    }

    @Test(description = "Test to validate the duplication of model name is not allowed on model config page.")
    public void testAlertOnDuplicationOfModelName() {
        modelPage.get();

        String familyName = Family.NEW_TEST_FAMILY2.name();
        String capabilityName = Capability.NEW_TEST_CAP2.name();

        // Trying to add a duplicate model.
        modelPage.addModel(existingModelName, familyName,
                capabilityName);

        // Fail the test if no alert message displayed on attempt of duplication of model name.
        Assert.assertTrue(isAlertPresent(),
                "No alert message displayed on attempting to duplicate the model name : " +
                existingModelName);

        Alert alert = driver.switchTo().alert();

        // Validates the alert message.
        Assert.assertEquals(alert.getText(),
                existingModelName + TestConstants.MODEL_EXIST_ALERT_MSG_SUFFIX,
                "The alert message on attempting duplication of model is different from what expected.");
    }

    @Test(description = "Test to validate the deletion of a model from the model config page.")
    public void testDeleteModel() {
        modelPage.get();
        modelPage.clickOnModelDeleteElement(existingModelNameForDeletion);

        // Fail the test if no alert message displayed.
        Assert.assertTrue(isAlertPresent(), "No alert message launched on clicking on model delete element.");

        Alert deleteAlert = driver.switchTo().alert();

        // Validates the alert message.
        Assert.assertEquals(deleteAlert.getText(),
                TestConstants.DELETE_ALERT_MSG_PREFIX + existingModelNameForDeletion +
                TestConstants.DELETE_ALERT_MSG_SUFFIX,
                "The alert message on deletion of model is different from what expected.");

        // Accepting the alert so it proceed with deletion.
        deleteAlert.accept();

        // Wait till the element get deleted successfully.
        modelPage.waitForStbModelTrElementDeletion(existingModelNameForDeletion);

        // Validate whether the model page gets the focus back.
        modelPage.isLoaded();

        // Validation to see that model name is not displayed in the model config page after
        // deletion.
        Assert.assertFalse(modelPage.getAllModelNames().contains(existingModelNameForDeletion),
                String.format(
                    "Model name(%s) does not get deleted from the UI after clicking on delete icon.",
                    existingModelNameForDeletion));

        // Validation to see that model name gets deleted from the DB.
        Assert.assertNull(getDawgModelUsingRestCall(existingModelNameForDeletion),
                String.format("Model(%s) does not get removed from the backend only UI got updated.",
                    existingModelNameForDeletion));
    }

    /**
     * Validate and fail test if the expected model info is not found in DB.
     *
     * @param  expectedModelName     Model name expected in the DB.
     * @param  expectedFamily        Expected family to which model need to belong.
     * @param  expectedCapabilities  Expected capabilities enabled for the model.
     */
    private void failIfExpectedModelInfoDoesNotMatchesWithDb(String expectedModelName, String expectedFamily, String... expectedCapabilities) {
        DawgModel model = getDawgModelUsingRestCall(expectedModelName);
        Assert.assertNotNull(
                model,
                "Model info does not get updated to DB. Rest call failed to get the model details corresponding to model : " +
                expectedModelName);
        Assert.assertEquals(model.getFamily(), expectedFamily,
                "Family updated in the DB differs from what expected.");

        for (String expectedCapability : expectedCapabilities) {
            Assert.assertTrue(model.getCapabilities().contains(expectedCapability),
                    String.format("Expected capability(%s) does not get updated in the DB.",
                        expectedCapability));
        }
    }

    /**
     * Retrieves the model information using rest call.
     *
     * @param   modelName  Model name.
     *
     * @return  model information corresponding to model name, null if no model information
     *          available.
     */
    private DawgModel getDawgModelUsingRestCall(String modelName) {
        DawgHouseClient dawgHouseClient = new DawgHouseClient(TestServers.getHouse());
        DawgModel dawgModel = null;
        DawgModel[] dawgModels = dawgHouseClient.getDawgModels(modelName);

        if (null != dawgModels && dawgModels.length > 0) {
            dawgModel = dawgModels[0];
            logger.info("Model name returned in rest call is : " + dawgModel.getName());
        }

        return dawgModel;
    }

    /**
     * Creates a test STB model and cache the model name for later deletion.
     *
     * @return  Successfully created STB model.
     *
     * @throws  IOException  If fails to create the STB model.
     */
    private DawgModel createTestStbModelAndCache() throws IOException {
        DawgModel dawgModel = new DawgModel();
        dawgModel.setName(MetaStbBuilder.getUID(TestConstants.MODEL_NAME_PREF));
        dawgModel.setFamily(Family.AVAILABLE_TEST_FAMILY.name());
        dawgModel.setCapabilities(new ArrayList<String>(Arrays.asList(Capability.AVAILABLE_TEST_CAP
                        .name())));

        int response = addStbModel(dawgModel);
        logger.info("Response of adding STB model : " + response);

        // Fails if the response returned is not a success one.
        Assert.assertEquals(HttpServletResponse.SC_OK, response,
                "Failed to add the model, the response returned is : " + response);
        cachedDawgModelNames.add(dawgModel.getName());

        return dawgModel;
    }

    /**
     * Provide a unique model name for every call.
     *
     * @return  test model name.
     */
    private String getATestModelName() {
        return MetaStbBuilder.getUID(TestConstants.MODEL_NAME_PREF);
    }

    /**
     * To add STB model using rest call.
     *
     * @param   model  Dawg model object which need to be added.
     *
     * @return  http response status of addition of model.
     *
     * @throws  IOException  On failure of rest call for addition.
     */
    private int addStbModel(DawgModel model) throws IOException {
        StringBuilder capabilities = new StringBuilder();

        if (null != model.getCapabilities()) {

            for (String capability : model.getCapabilities()) {

                if (capabilities.length() > 0) {
                    capabilities.append(',');
                }

                capabilities.append('"').append(capability).append('"');
            }
        }

        HttpPost request = new HttpPost(TestConstants.MODEL_UPDATE_REST_URI + model.getName());
        request.addHeader(TestConstants.CONTENT_TYPE_HEADER_NAME, TestConstants.JSON_CONTENT_TYPE_HEADER_VALUE);

        String content =
            "{\"name\": \"" + model.getName() + "\",\"capabilities\":[" + capabilities +
            "],\"family\": \"" + model.getFamily() + "\"}";
        logger.info("Entity content : " + content);

        HttpEntity entity = new StringEntity(content, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        HttpClient httpClient = new DefaultHttpClient();

        return getStatus(httpClient.execute(request));
    }

    /**
     * To delete the STB model using rest call.
     *
     * @param   modelName  Name of model to be deleted.
     *
     * @return  http response status of deletion of model.
     *
     * @throws  IOException  On failure of rest call for deletion.
     */
    private int deleteStbModel(String modelName) throws IOException {
        HttpDelete request = new HttpDelete(TestConstants.MODEL_UPDATE_REST_URI + modelName);
        request.addHeader(TestConstants.CONTENT_TYPE_HEADER_NAME, TestConstants.JSON_CONTENT_TYPE_HEADER_VALUE);

        HttpClient httpClient = new DefaultHttpClient();

        return getStatus(httpClient.execute(request));
    }
}
