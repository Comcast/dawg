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
import com.comcast.dawg.config.TestServerConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
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
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * This class contains implementation of dawg pound REST services specific to STB reservation 
 * @author Jeeson
 *
 */
public class DawgPoundSTBReservationRestGlue {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgPoundSTBReservationRestGlue.class);

    /**
     * Step definition to 'get STB (device/reservation) list' from dawg (house/pound) via GET request
     * @param listType
     *          Whether list is of type reservation/device 
     * @throws DawgTestException
     */
    @When("^I send GET request to 'get STB (.*) list'$")
    public void sendGetReqForStbDevOrResList(String listType) throws DawgTestException {
        LOGGER.info("Going to send GET request to 'get STB {} list'", listType);
        if ("reservation".equals(listType)) {
            Assert.assertTrue(DawgHouseRestHelper.getInstance().sendGetReqForSTBDeviceList(TestServerConfig.getPound()),
                "Failed to display STB reservation list");
        } else if ("device".equals(listType)) {
            Assert.assertTrue(DawgHouseRestHelper.getInstance().sendGetReqForSTBDeviceList(TestServerConfig.getHouse()),
                "Failed to display STB device list");
        }
        LOGGER.info("Has successfully sent GET request to 'get STB {} list'", listType);
    }

    /**
     * Step definition to '(reserve/unreserve) an STB' in dawg pound
     * @param reserveType
     *          Whether STB is to be reserved/unreserved
     * @param override
     *          Whether reservation is by (override/politely)
     * @param stbDetails
     *          STB details
     * @throws DawgTestException
     */
    @When("^I send POST request to '(.*) STB'( politely)? with following details$")
    public void sendPostReqToReserveOrUnreserveStb(String reserveType, String override, DataTable stbDetails) throws DawgTestException {
        LOGGER.info("Going to send POST request to {} STB in dawg pound", reserveType);
        List<String> stbParams = stbDetails.raw().get(1);
        boolean result = false;
        if ("reserve".equalsIgnoreCase(reserveType)) {
            // Setting reservation parameters
            String stbDeviceID = stbParams.get(0);
            String reserveToken = stbParams.get(1);
            boolean forceReserve = false;
            if ((null != override) && (override.contains("politely"))) {
                forceReserve = false;
                TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_RESERVATION_TOKEN, reserveToken);
            }
            Reservation reservation = new Reservation(stbDeviceID, reserveToken, DawgHouseConstants.VALID_EXPIRATION_TIME, forceReserve);
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_RESERVATION, reservation);
            // Sending POST request for STB reservation
            result = DawgPoundRestHelper.getInstance().reserveStbInDawg(reservation);
        } else if ("unreserve".equalsIgnoreCase(reserveType)) {
            // Setting STB device id
            String stbDeviceID = stbParams.get(0);
            // Sending POST request for STB unreservation
            result = DawgPoundRestHelper.getInstance().unreserveStbInDawg(stbDeviceID);
        }
        Assert.assertTrue(result, "Failed to " + reserveType + " STB in dawg pound");
        LOGGER.info("Successfully sent POST request to {} STB {} ", reserveType, stbParams);
    }

    /**
     * Step definition to get STB reservation token from dawg pound via GET request
     * @param validity
     *          Whether reservation token is valid or not 
     * @throws DawgTestException 
     * 
     */
    @When("^I send GET request to 'get STB reservation token' with (same|invalid)? reservation token$")
    public void sendReqToGetStbReserveToken(String validity) throws DawgTestException {
        LOGGER.info("Going to send GET request to get STB reservation token with {} reservation token", validity);
        Reservation reservation = null;
        MetaStb testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        if ("same".equalsIgnoreCase(validity)) {
            // Setting valid reservation token for STB
            reservation = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_RESERVATION);
        } else if ("invalid".equalsIgnoreCase(validity)) {
            // Setting invalid reservation token for STB 
            reservation = new Reservation(testStb.getId(), DawgHouseConstants.INVALID_CLIENT_TOKEN, DawgHouseConstants.VALID_EXPIRATION_TIME, true);
        }
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_RESERVATION, reservation);
        boolean result = DawgPoundRestHelper.getInstance().sendGetReqForReservationDetail(reservation.getToken());
        Assert.assertTrue("same".equalsIgnoreCase(validity) ? result : !result,
            "Failed to get STB reservation token from dawg pound");
        LOGGER.info("Successfully sent GET request to get STB reservation token with {} reservation token for STB {} ",
            validity, reservation.getDeviceId());
    }

    /**
     * Step definition to verify get STB reservation token response
     * @param isParamPresent
     *          Whether response contains the specified parameters or not
     */
    @Then("^I verify that the response( doesn't)? contain(?:s|)(?: expected STB device id and|) reservation token$")
    public void verifyGetReserveTokenResponse(String isParamPresent) {
        LOGGER.info("Going to verify get STB reservation token response");
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        Reservation reservation = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_RESERVATION);
        String responseStr = response.asString();
        boolean isTokenPresent = ((null != isParamPresent) && (isParamPresent.contains("doesn't"))) ? false : true;
        if (isTokenPresent) {
            // Verifying that response contains STB device id and reservation token
            Assert.assertTrue(
                (responseStr.contains(reservation.getDeviceId()) && (responseStr.contains(reservation.getToken()))),
                "Failed to verify presence of  STB reservation token and device id in response");
        } else {
            // Verifying that response doesn't contain STB device id and reservation token
            Assert.assertTrue((!(responseStr.contains(reservation.getToken()))),
                "Failed to verify absence of  STB reservation token in response");
        }
        LOGGER.info("Successfully verified get STB reservation token response");
    }

    /**
     * Step definition to verify content in response
     * @param responseContent
     *          Content in the response
     */
    @And("^I verify that the response contains (true|false|(?:reserved|unreserved|first reservation) token)$")
    public void verifyResponseContent(String responseContent) {
        LOGGER.info("Going to verify that response contains {}", responseContent);
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        if ("true".equalsIgnoreCase(responseContent) || "false".equalsIgnoreCase(responseContent)) {
            Assert.assertTrue(response.asString().contains(responseContent),
                "Failed to verify that response contains " + responseContent);
        } else if (responseContent.contains("reserved") || responseContent.contains("unreserved")) {
            Reservation reservation = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_RESERVATION);
            Assert.assertTrue(response.asString().contains(reservation.getToken()),
                "Failed to verify that response contains " + responseContent);
        } else if (responseContent.contains("first reservation")) {
            String reservationToken = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_RESERVATION_TOKEN);
            Assert.assertTrue(response.asString().contains(reservationToken),
                "Failed to verify that response contains " + responseContent);
        }
        LOGGER.info("Successfully verified that response contains {}", responseContent);
    }

    /**
     * Step definition to reserve STB with invalid params
     * @param stbParam
     *          STB parameters (device id/expiration time/reservation token)
     * @throws DawgTestException
     */
    @When("^I send POST request to 'reserve STB' with invalid (.*)$")
    public void sendPostReqToReserveWithInvalidParam(String stbParam) throws DawgTestException {
        LOGGER.info("Going to send POST request to reserve STB with invalid {}", stbParam);
        Reservation reservation = null;
        MetaStb stbDevice = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        switch (stbParam) {
            case "device id":
                reservation = new Reservation(DawgHouseConstants.INVALID_STB_DEVICE_ID, DawgHouseConstants.VALID_CLIENT_TOKEN, DawgHouseConstants.VALID_EXPIRATION_TIME, true);
                break;
            case "expiration time":
                reservation = new Reservation(stbDevice.getId(), DawgHouseConstants.VALID_CLIENT_TOKEN, DawgHouseConstants.INVALID_EXPIRATION_TIME, true);
                break;
            case "reservation token":
                reservation = new Reservation(stbDevice.getId(), DawgHouseConstants.INVALID_CLIENT_TOKEN, DawgHouseConstants.VALID_EXPIRATION_TIME, true);
                break;
        }
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_RESERVATION, reservation);
        boolean result = DawgPoundRestHelper.getInstance().reserveStbInDawg(reservation);
        Assert.assertTrue(result, "Failed to send POST request to reserve STB in dawg pound");
        LOGGER.info("Successfully sent POST request to reserve STB with invalid {} ", stbParam);
    }

    /**
     * Step definition to check if STB is reserved in dawg pound
     * @throws DawgTestException 
     */
    @And("^I should verify that the STB is reserved with expected reservation token$")
    public void verifySTBReservation() throws DawgTestException {
        LOGGER.info("Going to verify that STB is reserved");
        Reservation reservation = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_RESERVATION);
        boolean result = DawgPoundRestHelper.getInstance().isStbReserved(reservation.getDeviceId());
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        Assert.assertTrue(result && (response.asString().contains(reservation.getToken())),
            "Failed to reserve STB in dawg pound");
        LOGGER.info("Successfully verified that STB is reserved");
    }

    /**
     * Step definition to send GET request to check if STB is reserved in dawg pound with invalid device id 
     * @throws DawgTestException
     */
    @When("^I send GET request to 'check if STB is reserved' with invalid device id$")
    public void checkIfStbReserved() throws DawgTestException {
        LOGGER.info("Going to send GET request to 'check if STB is reserved' with invalid device id");
        boolean result = DawgPoundRestHelper.getInstance().isStbReserved(DawgHouseConstants.INVALID_STB_DEVICE_ID);
        Assert.assertFalse(result, "STB got reserved with invalid id in dawg pound");
        LOGGER.info("Successfully sent GET request to 'check if STB is reserved' with invalid device id");
    }

    /**
     * Step definition to verify that response received is empty
     */
    @Then("^I should receive the response which is empty$")
    public void verifyResponseAsEmpty() {
        LOGGER.info("Going to verify that reponse as empty");
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);
        Assert.assertTrue(response.asString().isEmpty(), "Failed to verify response as empty");
        LOGGER.info("Successfully verified response as empty");
    }

    /**
     * Step definition to send POST request to reserve same STB politely with different reservation token.
     * @throws DawgTestException
     */
    @When("^I send POST request to reserve same STB politely with different reservation token$")
    public void sendPostReqWithDiffReserveToken() throws DawgTestException {
        LOGGER.info("Going to send POST request to reserve same STB politely with different reservation token");
        MetaStb testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_DEVICE);
        Reservation reservation = new Reservation(testStb.getId(), DawgHouseConstants.VALID_CLIENT_TOKEN, DawgHouseConstants.VALID_EXPIRATION_TIME, false);
        boolean result = DawgPoundRestHelper.getInstance().reserveStbInDawg(reservation);
        Assert.assertTrue(result,
            "Failed to send POST request to reserve same STB politely with different reservation token");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_RESERVATION, reservation);
        LOGGER.info("Successfully POST request to reserve same STB politely with different reservation token");
    }

    /**
     * Step definition to unreserve STB with (valid/invalid) device id
     * @param stbType
     *          Whether STB device id is valid or not
     * @throws DawgTestException 
     */
    @When("^I send POST request to unreserve same STB( with invalid device id|)?$")
    public void sendPostReqToUnreserveStb(String stbType) throws DawgTestException {
        LOGGER.info("Going to send POST request to unreserve same STB {} ", stbType);
        String stbDeviceId = null;
        if ((null != stbType) && (stbType.contains("invalid"))) {
            stbDeviceId = DawgHouseConstants.INVALID_STB_DEVICE_ID;
        } else {
            Reservation reservation = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_RESERVATION);
            stbDeviceId = reservation.getDeviceId();
        }
        boolean result = DawgPoundRestHelper.getInstance().unreserveStbInDawg(stbDeviceId);
        Assert.assertTrue(result, "Failed to unreserve STB " + stbDeviceId);
        LOGGER.info("Successfully sent POST request to unreserve same STB {} ", stbType);
    }

}
