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
package com.comcast.video.dawg.controller.park;

import java.util.List;

import org.easymock.EasyMock;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.video.dawg.controller.house.ChimpsToken;
import com.comcast.video.dawg.controller.house.HouseRestController;
import com.comcast.video.dawg.controller.house.mocks.MockParkService;
import com.comcast.video.dawg.service.park.ParkService;

/**
 * The TestNG test class for PopulateController.
 *
 * @author  TATA
 */
public class PopulateControllerTest {

    @Test
    public void testPopulate() {
        HouseRestController houseController = EasyMock.createMock(HouseRestController.class);
        EasyMock.expect(houseController.populate((String) EasyMock.anyObject(),(Boolean) EasyMock.anyObject())).andReturn(1);

        PopulateController controller = new PopulateController();

        ParkService mockParkService = new MockParkService();
        mockParkService.saveToken(new ChimpsToken(""));
        EasyMock.replay(houseController);
        ReflectionTestUtils.setField(controller, "parkService", mockParkService);
        ReflectionTestUtils.setField(controller, "houseController", houseController);

        try {
            controller.populate();
        } catch (Exception e) {
            Assert.fail(e.getStackTrace().toString());
        }
    }

    /**
     * Method that provides boolean data
     *
     * @return Object[][] that contains a boolean data, if true CHIMPS meta
     *         data will override DAWG meta data ignoring the modified
     *         properties in DAWG.
     */
    @DataProvider(name = "populateTestDataProvider")
    public Object[][] populateTestDataProvider() {
        return new Object[][] { { true }, { false } };
    }

    @Test(dataProvider = "populateTestDataProvider")
    public void testPopulateWithToken(Boolean isToOverrideMetaData) {
        HouseRestController houseController = EasyMock.createMock(HouseRestController.class);
        EasyMock.expect(houseController.populate((String) EasyMock.anyObject(), (Boolean) EasyMock.anyObject())).andReturn(1);
        EasyMock.replay(houseController);
        String token = "abc";

        ParkService mockParkService = new MockParkService();
        Assert.assertTrue(mockParkService.getSavedTokens().isEmpty());

        PopulateController controller = new PopulateController();
        ReflectionTestUtils.setField(controller, "parkService", mockParkService);
        ReflectionTestUtils.setField(controller, "houseController", houseController);
        Assert.assertEquals(controller.populate(token, isToOverrideMetaData), 1);

        Assert.assertFalse(mockParkService.getSavedTokens().isEmpty());
        Assert.assertEquals(mockParkService.getSavedTokens().get(0).getToken(), token);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPopulateWithModel() {
        PopulateController controller = new PopulateController();
        ParkService mockParkService = new MockParkService();
        ChimpsToken myToken = new ChimpsToken("mytoken");
        mockParkService.saveToken(myToken);
        ReflectionTestUtils.setField(controller, "parkService", mockParkService);
        Model model = new BindingAwareModelMap();
        Assert.assertEquals(controller.populate(model), "populate");
        List<ChimpsToken> tokens = (List<ChimpsToken>) model.asMap().get("population");
        Assert.assertNotNull(tokens);
        Assert.assertTrue(tokens.contains(myToken));
    }

    @Test
    public void testPopulateTable() {
        PopulateController controller = new PopulateController();
        ParkService mockParkService = new MockParkService();
        ReflectionTestUtils.setField(controller, "parkService", mockParkService);

        Model model = new BindingAwareModelMap();
        Assert.assertEquals(controller.populateTable(model), "populateTable");
        Assert.assertNotNull(model.asMap().get("population"));
    }
}
