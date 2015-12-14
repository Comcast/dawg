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
package com.comcast.video.dawg.controller.house;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.drivethru.exception.HttpException;
import com.comcast.video.dawg.PopulatingClient;
import com.comcast.video.dawg.common.DawgDevice;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.MetaStbBuilder;
import com.comcast.video.dawg.common.ServerUtils;
import com.comcast.video.dawg.common.exceptions.DawgIllegalArgumentException;
import com.comcast.video.dawg.common.exceptions.DawgNotFoundException;
import com.comcast.video.dawg.controller.house.mocks.MockHouseService;
import com.comcast.video.dawg.service.house.HouseService;
import com.comcast.video.dawg.show.DawgShowClient;

/**
 * The TestNG test class for HouseRestController.
 *
 * @author  TATA
 */
public class HouseRestControllerTest {

    ServerUtils utils = new ServerUtils();

    @SuppressWarnings("unchecked")
    @Test
    public void testListAll() {
        HouseService mockService = EasyMock.createMock(HouseService.class);
        HashMap[] abc = new HashMap[1];
        EasyMock.expect(mockService.getAll()).andReturn(abc).anyTimes();
        EasyMock.replay(mockService);

        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "service", mockService);
        Assert.assertNotNull(controller.listAll());
        Assert.assertEquals(controller.listAll(), abc);

    }

    @Test(expectedExceptions = DawgNotFoundException.class)
    public void testGetStbsByIdWithException() {
        HouseService mockService = EasyMock.createMock(HouseService.class);
        EasyMock.expect(mockService.getById((String) EasyMock.anyObject())).andReturn(null);
        EasyMock.replay(mockService);

        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "service", mockService);
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        String id = "id";
        controller.getStbsById(id);
    }

    @Test
    public void testGetStbsById() {
        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        String id = "id";
        HouseService mockService = EasyMock.createMock(HouseService.class);
        EasyMock.expect(mockService.getById((String) EasyMock.anyObject())).andReturn(new HashMap<String, Object>());

        EasyMock.replay(mockService);
        ReflectionTestUtils.setField(controller, "service", mockService);
        Assert.assertNotNull(controller.getStbsById(id));
    }

    @Test
    public void testDeleteSingle() {
        HouseService mockService = EasyMock.createMock(HouseService.class);
        mockService.deleteStbById((String) EasyMock.anyObject());
        EasyMock.expectLastCall();
        EasyMock.replay(mockService);

        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "service", mockService);
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        String id = "id";

        try {
            controller.deleteSingle(id);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expectedExceptions = DawgIllegalArgumentException.class)
    public void testDeleteSingleWithException() {
        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        HouseService mockService = EasyMock.createMock(HouseService.class);
        mockService.deleteStbById((String) EasyMock.anyObject());
        EasyMock.expectLastCall().andThrow(new MappingException(""));
        EasyMock.replay(mockService);
        ReflectionTestUtils.setField(controller, "service", mockService);

        String id = "id";
        controller.deleteSingle(id);
    }

    @Test
    public void testAdd() {
        HouseService mockService = EasyMock.createMock(HouseService.class);
        mockService.upsertStb((DawgDevice) EasyMock.anyObject());
        EasyMock.expectLastCall();
        EasyMock.replay(mockService);

        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "service", mockService);
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        String id = "id";
        Map<String, Object> data = new HashMap<String, Object>();

        try {
            controller.add(id, data);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expectedExceptions = DawgNotFoundException.class)
public void testGetStbsByIds() {
    HouseService mockService = EasyMock.createMock(HouseService.class);
        EasyMock.expect(mockService.getStbsById((String) EasyMock.anyObject())).andReturn(null);
        EasyMock.replay(mockService);

        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "service", mockService);
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        String[] id = new String[1];
        id[0] = "id";

        controller.getStbsByIds(id);
    }

    /**
     * Verify that STB list get populated successfully based on tag name given.
     *
     */
    @Test
    public void testGetStbsByTag() {
        MockHouseService houseService = new MockHouseService();
        HouseRestController controller = new HouseRestController();
        MetaStbBuilder metaStbBuilder = new MetaStbBuilder();
        metaStbBuilder.tags(new String[] { "validTag" });
        MetaStb stb = metaStbBuilder.stb();
        houseService.upsertStb(stb);
        ReflectionTestUtils.setField(controller, "service", houseService);
        Map<String, Object>[] stbs = controller.getStbsByTag("validTag", null);
        Assert.assertNotNull(stbs, "No result found on quering with tag value");
        Assert.assertTrue(stbs[0].get(MetaStb.TAGS).toString().contains("validTag"),
                " Expected tag not found in the MetaStbs returned");
    }

    @Test
    public void testUpdate() throws NoSuchMethodException, SecurityException {
        HouseService mockService = EasyMock.createMock(HouseService.class);
        HouseRestController controller = EasyMock.createMockBuilder(HouseRestController.class)
            .addMockedMethod(HouseRestController.class.getDeclaredMethod("getDawgShowClient")).createMock();
        ReflectionTestUtils.setField(controller, "service", mockService);
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        String[] id = new String[1];
        id[0] = "id";

        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object>[] matches = getMatches();

        EasyMock.expect(mockService.getStbsById((String[]) EasyMock.anyObject())).andReturn(matches);
        mockService.replace((DawgDevice) EasyMock.anyObject());
        EasyMock.expectLastCall();
        EasyMock.replay(mockService);
        ReflectionTestUtils.setField(controller, "service", mockService);

        DawgShowClient dawgShowClient = EasyMock.createMock(DawgShowClient.class);
        EasyMock.expect(controller.getDawgShowClient()).andReturn(dawgShowClient);
        EasyMock.replay(controller);

        try {
            dawgShowClient.updateCache((String[]) EasyMock.anyObject());
            EasyMock.expectLastCall().once();
        } catch (HttpException e) {
            Assert.fail("Failed to mock DawgShowClient.updateCache() method.", e);
        }

        Assert.assertEquals(controller.update(id, data, true), 1);

        EasyMock.verify(controller);
    }

    @Test(expectedExceptions = DawgIllegalArgumentException.class)
    public void testUpdatePayload() {
        HouseService mockService = EasyMock.createMock(HouseService.class);
        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "service", mockService);
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        Map<String, Object> payload = new HashMap<String, Object>();

        controller.updateTags(payload,"add");
    }

    @Test(expectedExceptions = DawgIllegalArgumentException.class)
    public void testUpdatePayloadWithIds() {
        HouseService mockService = EasyMock.createMock(HouseService.class);
        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "service", mockService);
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        Map<String, Object> payload = new HashMap<String, Object>();
        List<String> ids = new ArrayList<String>();
        ids.add("id-1");
        payload.put("id", ids);
        controller.updateTags(payload,"add");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdatePayloadWithIdsAndTags() {
        HouseRestController controller = new HouseRestController();
        HouseService mockService = EasyMock.createMock(HouseService.class);
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        Map<String, Object> payload = new HashMap<String, Object>();
        List<String> ids = new ArrayList<String>();
        ids.add("id1");
        payload.put("id", ids);

        List<String> tags = new ArrayList<String>();
        tags.add("tag-1");
        payload.put("tag", tags);

        Map<String, Object>[] matches = getMatches();

        EasyMock.expect(mockService.getStbsById((String[]) EasyMock.anyObject())).andReturn(matches);
        mockService.upsertStb((DawgDevice) EasyMock.anyObject());
        EasyMock.expectLastCall();
        EasyMock.replay(mockService);
        ReflectionTestUtils.setField(controller, "service", mockService);
        controller.updateTags(payload,"add");

    }

    @Test
    public void testGetStbsByQuery() {
        String query = "a,b";
        HouseRestController controller = new HouseRestController();
        HouseService mockService = EasyMock.createMock(HouseService.class);
        EasyMock.expect(mockService.getStbsByQuery(query)).andReturn(new HashMap[1]);
        EasyMock.replay(mockService);
        ReflectionTestUtils.setField(controller, "service", mockService);

        Assert.assertNotNull(controller.getStbsByQuery(query));
    }

    @Test
    public void testDeleteStb() {
        String[] id = new String[1];
        id[0] = "id1";

        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        HouseService mockService = EasyMock.createMock(HouseService.class);
        mockService.deleteStbById(id);
        EasyMock.expectLastCall();
        EasyMock.replay(mockService);
        ReflectionTestUtils.setField(controller, "service", mockService);

        try {
            controller.deleteStb(id);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Method that provides boolean data
     *
     * @return Object[][] that contains a boolean data, if true client meta
     *         data will override DAWG meta data ignoring the modified
     *         properties in DAWG.
     */
    @DataProvider(name = "populateTestDataProvider")
    public Object[][] populateTestDataProvider() {
        return new Object[][] { { true }, { false } };
    }

    /**
     * Method to test populating boxes from the client to DAWG using the token.
     *
     * @param isToOverrideMetaData if true client meta data will override DAWG meta data ignoring the modified properties in DAWG.
     */

    @Test(dataProvider = "populateTestDataProvider")
    public void testPopulate(Boolean isToOverrideMetaData) {
        List<MetaStb> list = new ArrayList<MetaStb>();
        MetaStb stb1 = new MetaStb();
        stb1.setId("id1");
        MetaStb stb2 = new MetaStb();
        stb2.setId("id2");
        list.add(stb1);
        list.add(stb2);

        String token = "4cb2e04d-5fed-4f52-83e7-c05f29acb432";
        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        PopulatingClient client = EasyMock.createMock(PopulatingClient.class);

        try {
            EasyMock.expect(client.populate((String) EasyMock.anyObject())).andReturn(list).anyTimes();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        MockHouseService mockService = new MockHouseService();
        EasyMock.replay(client);
        ReflectionTestUtils.setField(controller, "service", mockService);
        ReflectionTestUtils.setField(controller, "client", client);

        Assert.assertEquals(controller.populate(token, isToOverrideMetaData), 2);
        Assert.assertNotNull(mockService.getById("id1"));
        Assert.assertNotNull(mockService.getById("id2"));
        Assert.assertNull(mockService.getById("id3"));
    }

    @Test(dataProvider = "populateTestDataProvider")
    public void testPopulateWithClientRequestException(Boolean isToOverrideMetaData) throws Exception {
        List<MetaStb> list = new ArrayList<MetaStb>();
        list.add(new MetaStb());

        String token = "4cb2e04d-5fed-4f52-83e7-c05f29acb432";
        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        PopulatingClient client = EasyMock.createMock(PopulatingClient.class);
        EasyMock.expect(client.populate((String) EasyMock.anyObject())).andThrow(
                new Exception("Could not load from client"));
        EasyMock.replay(client);
        ReflectionTestUtils.setField(controller, "client", client);

        Assert.assertEquals(controller.populate(token, isToOverrideMetaData), 0);
    }

    @Test(dataProvider = "populateTestDataProvider")
    public void testPopulateWithException(Boolean isToOverrideMetaData) {
        List<MetaStb> list = new ArrayList<MetaStb>();
        list.add(new MetaStb());
        list.add(new MetaStb());

        String token = "4cb2e04d-5fed-4f52-83e7-c05f29acb432";
        HouseRestController controller = new HouseRestController();
        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        PopulatingClient client = EasyMock.createMock(PopulatingClient.class);
        MockHouseService mockService = new MockHouseService();
        mockService.setWriteExc(true);

        try {
            EasyMock.expect(client.populate((String) EasyMock.anyObject())).andReturn(list).anyTimes();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        EasyMock.replay(client);
        ReflectionTestUtils.setField(controller, "client", client);
        ReflectionTestUtils.setField(controller, "service", mockService);

        Assert.assertEquals(controller.populate(token, isToOverrideMetaData), 0);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object>[] getMatches() {
        Map<String, Object>[] matches = new HashMap[1];
        Map<String, Object> match = new HashMap<String, Object>();
        List<String> tags = new ArrayList<String>();
        tags.add("tag-1");
        match.put("tags", tags);
        match.put("id", "4cb2e04d-5fed-4f52-83e7-c05f29acb432");
        match.put("tag", tags);
        matches[0] = match;

        return matches;
    }
}
