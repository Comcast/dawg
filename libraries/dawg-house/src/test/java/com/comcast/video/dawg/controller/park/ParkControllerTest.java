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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.cereal.CerealException;
import com.comcast.cereal.engines.JsonCerealEngine;
import com.comcast.video.dawg.common.DawgConfiguration;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.ServerUtils;
import com.comcast.video.dawg.controller.house.DawgHouseConfiguration;
import com.comcast.video.dawg.service.park.ParkService;


/**
 * The TestNG test class for ParkController.
 *
 * @author  TATA
 */
public class ParkControllerTest {

    ServerUtils utils = new ServerUtils();

    @DataProvider(name="testUserFrontWithValidTokenData")
    public Object[][] testUserFrontWithValidTokenData() {
        return new Object[][] {
            {"username", null, null, null, new String[]{}},
                {"Username" , "validTag", "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"USERNAME", null, null, null, new String[]{}},
                {"USERNAME" , "validTag", "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"UserName", null, null, null, new String[]{}},
                {"UserName" , "validTag", "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"username123", null, null, null, new String[]{}},
                {"Username123" , "validTag", "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"USERNAME123", null, null, null, new String[]{}},
                {"USERNAME123" ,"validTag", "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"UserName123", null, null, null, new String[]{}},
                {"UserName123" ,"validTag", "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"1234", null, null, null, new String[]{}},
                {"1234" , "validTag", "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}}
        };
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider="testUserFrontWithValidTokenData")
    public void testUserFrontWithValidToken(String token, String tag,String q, String[] sort, String[] otherTagsExp) {
        ParkController controller = new ParkController() {
            @Override
            protected Collection<SimpleGrantedAuthority> getAuths() {
                Collection<SimpleGrantedAuthority> auths = new ArrayList<SimpleGrantedAuthority>();
                auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                return auths;
            }
        };
        Map<String, Object>[] filteredStbs = new HashMap[1];
        Map<String, Object>[] allStbs = new HashMap[2];
        Map<String, Object> stb1 = new HashMap<String, Object>();
        stb1.put(MetaStb.ID, "sample");
        stb1.put(MetaStb.TAGS, Arrays.asList("tag1"));
        stb1.put(MetaStb.MACADDRESS, "00:00:00:00:00");

        Map<String, Object> stb2 = new HashMap<String, Object>();
        stb2.put(MetaStb.ID, "otherDevice");
        stb2.put(MetaStb.TAGS, Arrays.asList("tag2", "tag3"));
        stb2.put(MetaStb.MACADDRESS, "00:00:00:00:01");

        filteredStbs[0] = stb1;
        allStbs[0] = stb1;
        allStbs[1] = stb2;

        ReflectionTestUtils.setField(controller, "serverUtils", utils);

        ParkService mockService = EasyMock.createMock(ParkService.class);
        if (q == null) {
            EasyMock.expect(mockService.findAll((Pageable) EasyMock.anyObject())).andReturn(allStbs);
        } else {
            EasyMock.expect(mockService.findByKeys((String[]) EasyMock.anyObject(), (Pageable) EasyMock.anyObject())).andReturn(
                    filteredStbs);
            EasyMock.expect(mockService.findAll()).andReturn(allStbs);
        }
        if (tag != null) {
            EasyMock.expect(
                    mockService.findByCriteria(
                        (Criteria) EasyMock.anyObject(),
                        (Pageable) EasyMock.anyObject())).andReturn(
                    filteredStbs);
        }
        EasyMock.replay(mockService);
        ReflectionTestUtils.setField(controller, "service", mockService);
        ReflectionTestUtils.setField(controller, "config", createConfig());

        Integer page = Integer.valueOf(1);
        Integer size = Integer.valueOf(1);
        Boolean asc = Boolean.TRUE;

        try {
            Model model = new BindingAwareModelMap();
            MockHttpSession session = new MockHttpSession();
            Assert.assertEquals("index",
                    controller.userFront(token, tag,q, page, size, asc, sort, model, session));
            Assert.assertEquals(model.asMap().get("search"), q == null ? "" : q);
            String otherTagsJson = (String) model.asMap().get("otherTags");
            String deviceTagsJson = (String) model.asMap().get("deviceTags");
            JsonCerealEngine engine = new JsonCerealEngine();
            List<String> otherTags = engine.readFromString(otherTagsJson, List.class);
            Map<String, List<String>> deviceTags = engine.readFromString(deviceTagsJson, Map.class);

            Assert.assertEquals(otherTags.size(), otherTagsExp.length);
            for (String tags : otherTagsExp) {
                Assert.assertTrue(otherTags.contains(tags));
            }
            Assert.assertEquals(deviceTags.size(), q != null ? 1 : 2);
            Assert.assertTrue(deviceTags.containsKey("sample"));
            Assert.assertTrue(deviceTags.get("sample").contains("tag1"));
        } catch (CerealException e) {

            Assert.fail(e.getMessage());
        }
    }

    @DataProvider(name="testUserFrontWithInvalidTokenData")
    public Object[][] testUserFrontWithInvalidTokenData() {
        return new Object[][] {
            {"username@#$%^", null, null, null, new String[]{}},
                {"Username%^&#$%" , null, "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"@@#$%^USERNAME", null,  null, null, new String[]{}},
                {"USERNAME^%&))" , null, "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"UserName%^%", null,  null, null, new String[]{}},
                {"UserName%E&E^$R(" ,null,  "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"username123%&%E$&^$", null, null, null, new String[]{}},
                {"Username123{{}{{}&^(" , null, "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"USERNAME123(&%)&%%", null,  null, null, new String[]{}},
                {"USERNAME123$^^(&^(^$^&" ,null,  "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"UserName123%$#@%^@", null,  null, null, new String[]{}},
                {"UserName123$&$&%$" , null, "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"1234&$&$$(())",  null, null, null, new String[]{}},
                {"1234%(^%(^$R(" , null,  "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {null, null, null,  null, new String[]{}},
                {null, null,  "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"", null, null, null,  new String[]{}},
                {"" , null,  "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}},
                {"@!$#@%$#%$", null,  null, null, new String[]{}},
                {"!#@%^#^%)(&_)" , null, "filter1 filter2", new String[] {MetaStb.ID}, new String[]{"tag2", "tag3"}}
        };
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider="testUserFrontWithInvalidTokenData")
    public void testUserFrontWithInvalidToken(String token,String tags, String q, String[] sort, String[] otherTagsExp) {
        ParkController controller = new ParkController();
        Integer page = Integer.valueOf(1);
        Integer size = Integer.valueOf(1);
        Boolean asc = Boolean.TRUE;
        try {
            Model model = new BindingAwareModelMap();
            MockHttpSession session = new MockHttpSession();
            ReflectionTestUtils.setField(controller, "serverUtils", utils);
            Assert.assertEquals("login", controller.userFront(token, q,tags, page, size, asc, sort, model, session));
        }catch (CerealException e) {
            Assert.fail(e.getMessage());
        }
    }

    private DawgHouseConfiguration createConfig() {
        DawgHouseConfiguration config = new DawgHouseConfiguration();
        config.put(DawgConfiguration.DAWG_SHOW_URL, "http://www.dawg-show.com");
        config.put(DawgConfiguration.DAWG_POUND_URL, "http://www.dawg-pound.com");
        return config;
    }
}
