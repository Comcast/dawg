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


import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.video.dawg.common.DawgModel;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.MetaStbBuilder;
import com.comcast.video.dawg.controller.house.mocks.MockHouseService;
import com.comcast.video.dawg.controller.house.mocks.MockParkService;
import com.comcast.video.stbio.meta.Capability;
import com.comcast.video.stbio.meta.Family;

public class StbModelControllerTest {

    /**
     * This adds two stbs, one DCT6412 and one DCT6416. Each of these stbs do not have family or capabilities.
     * Then the model DCT6412 is added. It is then verified that the DCT6412 stb should now have family and capabilities
     * populated. The DC6416 should not.
     */
    @Test
    public void testAddNewModel() {
        String idToChange = "000000000001";
        String idToNotChange = "000000000002";
        String model = "DCT6412";

        MockParkService parkService = new MockParkService();

        MockHouseService houseService = new MockHouseService();
        MetaStb stbShouldChange = createStb(idToChange, "00:00:00:00:00:01", model, new String[]{});
        houseService.upsertStb(stbShouldChange);

        MetaStb stbShouldNotChange = createStb(idToNotChange, "00:00:00:00:00:02", "DCT6416", new String[]{model});
        houseService.upsertStb(stbShouldNotChange);

        DawgModel dawgModel = createModel(model, "MAC_G", "HD", "DVR");

        StbModelController controller = new StbModelController();
        controller.houseService = houseService;
        controller.service = parkService;

        /** First verify that we don't have family or capabilities */
        MetaStb s = new MetaStb(houseService.getById(idToChange));
        Assert.assertNull(s.getFamily());
        Assert.assertTrue(s.getCapabilities().isEmpty());
        Assert.assertEquals(s.getId(), idToChange);

        /** First verify that we don't have the DCT6412 model */
        Assert.assertNull(parkService.getModelById(model));

        controller.addModel(dawgModel, model);

        /** Now verify that we DO have the DCT6412 model */
        Assert.assertNotNull(parkService.getModelById(model));

        /** Verify that we updated the DCT6412 model */
        MetaStb updatedStb = new MetaStb(houseService.getById(idToChange));
        Assert.assertEquals(updatedStb.getFamily(), Family.valueOf("MAC_G"));
        Assert.assertTrue(updatedStb.getCapabilities().contains(Capability.DVR));
        Assert.assertTrue(updatedStb.getCapabilities().contains(Capability.HD));

        /** Verify that the DCT6416 remains unchanged */
        MetaStb unupdatedStb = new MetaStb(houseService.getById(idToNotChange));
        Assert.assertNull(unupdatedStb.getFamily());
        Assert.assertTrue(unupdatedStb.getCapabilities().isEmpty());
    }

    /**
     * We have three stbs:
     * 000000000001 : DCT6412, MAC_G, HD DVR VOD
     * 000000000002 : DCT6412, null, null
     * 000000000003 : DCT6416, null, null
     *
     * We already have the DCT6412 model defined with capabilities HD and DVR.
     *
     * We are updating the DCT6412 model to have capabilities HD and DUAL_TUNER and family MAC_S
     * This should do a diff and delete DVR from all DCT6412 stbs and add DUAL_TUNER to all DCT6412 stbs
     */
    @Test
    public void testUpdateExistingModel() {
        String idToChange = "000000000001";
        String idToChange2 = "000000000003";
        String idToNotChange = "000000000002";
        String model = "DCT6412";

        MockParkService parkService = new MockParkService();

        MockHouseService houseService = new MockHouseService();
        MetaStb stbShouldChange = createStb(idToChange, "00:00:00:00:00:01", model,
                "MAC_G", new String[]{"HD", "DVR", "VOD"}, new String[]{});
        houseService.upsertStb(stbShouldChange);
        MetaStb stbShouldChange2 = createStb(idToChange2, "00:00:00:00:00:03", model, new String[]{});
        houseService.upsertStb(stbShouldChange2);

        /** The DCT6416 has the tag DCT6412, this is to prove that it will still be returned from the query, but
         * it won't be updated. */
        MetaStb stbShouldNotChange = createStb(idToNotChange, "00:00:00:00:00:02", "DCT6416", new String[]{model});
        houseService.upsertStb(stbShouldNotChange);

        DawgModel existing = createModel(model, "MAC_G", "HD", "DVR");
        DawgModel updated = createModel(model, "MAC_S", "HD", "DUAL_TUNER");

        parkService.upsertModel(existing);

        StbModelController controller = new StbModelController();
        controller.houseService = houseService;
        controller.service = parkService;

        controller.addModel(updated, model);

        Assert.assertNotNull(parkService.getModelById(model));

        MetaStb updatedStb = new MetaStb(houseService.getById(idToChange));
        Assert.assertEquals(updatedStb.getFamily(), Family.valueOf("MAC_S"));
        /** DVR capability should be removed */
        Assert.assertFalse(updatedStb.getCapabilities().contains(Capability.DVR));
        Assert.assertTrue(updatedStb.getCapabilities().contains(Capability.HD));
        Assert.assertTrue(updatedStb.getCapabilities().contains(Capability.valueOf("DUAL_TUNER")));
        /** Make sure it still have the VOD capability */
        Assert.assertTrue(updatedStb.getCapabilities().contains(Capability.VOD));

        MetaStb updatedStb2 = new MetaStb(houseService.getById(idToChange2));
        Assert.assertEquals(updatedStb2.getFamily(), Family.valueOf("MAC_S"));
        Assert.assertEquals(updatedStb2.getCapabilities().size(), 2);
        /** Make sure it added both HD and DUAL_ TUNER */
        Assert.assertTrue(updatedStb2.getCapabilities().contains(Capability.HD));
        Assert.assertTrue(updatedStb2.getCapabilities().contains(Capability.valueOf("DUAL_TUNER")));

        /** Make sure the DCT6416 remained unchanged */
        MetaStb unupdatedStb = new MetaStb(houseService.getById(idToNotChange));
        Assert.assertNull(unupdatedStb.getFamily());
        Assert.assertTrue(unupdatedStb.getCapabilities().isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testModelsConfig() {
        StbModelController controller = new StbModelController();
        controller.service = new MockParkService();
        DawgModel model1 = createModel("DCT6412", "MAC_G", "HD", "DVR");
        DawgModel model2 = createModel("DCT6416", "MAC_G", "HD", "DVR", "VOD");
        DawgModel model3 = createModel("SA2200", "MAC_S");

        controller.service.upsertModel(model1, model2, model3);
        ModelAndView mav = controller.models();
        List<DawgModel> models = (List<DawgModel>) mav.getModelMap().get("models");
        Set<String> caps = (Set<String>) mav.getModelMap().get("capabilities");
        Set<String> fams = (Set<String>) mav.getModelMap().get("families");

        Assert.assertEquals(models.size(), 3);
        Assert.assertEquals(caps.size(), 3);
        Assert.assertEquals(fams.size(), 2);
        Assert.assertTrue(caps.contains("HD"));
        Assert.assertTrue(caps.contains("DVR"));
        Assert.assertTrue(caps.contains("VOD"));
        Assert.assertTrue(fams.contains("MAC_G"));
        Assert.assertTrue(fams.contains("MAC_S"));
    }

    @Test
    public void testAssignModels() {
        StbModelController controller = new StbModelController();
        controller.houseService = new MockHouseService();
        controller.service = new MockParkService();
        controller.houseService.upsertStb(createStb("abc", "00:00:00:00:00:02", "DCT6412", "MAC_G",
                    new String[] {"HD"}, new String[]{}));

        controller.assignModels();

        // stb should not have changed because it already has family and capabilities
        MetaStb stb = new MetaStb(controller.houseService.getById("abc"));
        Assert.assertEquals(stb.getCapabilities().size(), 1);
        Assert.assertTrue(stb.getCapabilities().contains(Capability.HD));
        Assert.assertEquals(stb.getFamily(), Family.MAC_G);
    }

    @Test
    public void testDeleteAndGet() {
        StbModelController controller = new StbModelController();
        controller.service = new MockParkService();
        DawgModel model = createModel("DCT6412", "MAC_G", "HD");
        controller.service.upsertModel(model);
        /** Gets the model information, then deletes it, then tries to get it again. */
        List<DawgModel> models = controller.getModels(new String[] {"DCT6412"});
        Assert.assertEquals(models.size(), 1);
        Assert.assertEquals(models.get(0), model);
        controller.deleteModel("DCT6412");
        models = controller.getModels(new String[] {"DCT6412"});
        Assert.assertEquals(models.size(), 0);
    }

    private DawgModel createModel(String name, String family, String... capabilities) {
        DawgModel model = new DawgModel();
        model.setName(name);
        model.setCapabilities(Arrays.asList(capabilities));
        model.setFamily(family);
        return model;
    }

    private MetaStb createStb(String id, String mac, String model, String[] tags) {
        return createStb(id, mac, model, null, null, tags);
    }

    private MetaStb createStb(String id, String mac, String model, String family, String[] capabilities, String[] tags) {
        MetaStbBuilder metaStbBuilder = new MetaStbBuilder();
        metaStbBuilder.id(id).mac(mac).model(model).family(family).tags(tags);
        if (capabilities != null) {
            metaStbBuilder.caps(capabilities);
        }
        return metaStbBuilder.stb();
    }
}
