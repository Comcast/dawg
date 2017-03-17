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
package com.comcast.video.dawg.show;

import java.io.IOException;

import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.pantry.test.TestList;
import com.comcast.pantry.test.Wiring;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.security.jwt.JwtDeviceAccessValidator;
import com.comcast.video.dawg.show.cache.MetaStbCache;
import com.comcast.video.dawg.show.key.Remote;
import com.comcast.video.dawg.show.key.RemoteManager;
import com.comcast.video.dawg.show.util.MockMetaStbCache;
import com.comcast.video.dawg.show.util.TestUtils;

public class ViewControllerTest {
    public static final String DEVICE_ID = "000041b7cd52";

    @DataProvider(name="testStbViewNoStbData")
    public TestList testStbViewNoStbData() {
        TestList list = new TestList();

        list.add(true);
        list.add(false);

        return list;
    }

    @Test(dataProvider="testStbViewNoStbData")
    public void testStbViewNoStb(boolean byExc) throws IOException {
        MockMetaStbCache cache = new MockMetaStbCache();
        cache.throwExc = byExc;
        ViewController controller = getViewController(cache);

        ModelAndView mav = controller.stbView(DEVICE_ID, null, null, null, null, null, null);
        Assert.assertEquals(mav.getViewName(), ViewConstants.NOSTB);
        Assert.assertEquals(mav.getModelMap().get(ViewConstants.DEVICE_ID), DEVICE_ID);
    }

    @DataProvider(name="testStbViewData")
    public TestList testStbViewData() {
        TestList list = new TestList();

        list.add("BCM-7437-1", "BCM-7437-1", "true", BrowserSupportTest.SAFARI_IPAD, true, TestUtils.SERIALURL, true);
        list.add("HD-DTA", "XR2", "true", BrowserSupportTest.SAFARI_IPAD, true, TestUtils.SERIALURL, true);
        list.add("COMCAST_XMP", "XMP", "true", BrowserSupportTest.SAFARI_IPAD, true, TestUtils.SERIALURL, true);
        list.add("XR2", "XR2", "true", BrowserSupportTest.SAFARI_IPAD, true, TestUtils.SERIALURL, true);
        list.add("XR2_LEGACY_MOTOROLA", "XR2", "true", BrowserSupportTest.SAFARI_IPAD, true, TestUtils.SERIALURL, true);
        list.add("BAD", "MOTOROLA", null, BrowserSupportTest.SAFARI_IPAD, true, null, false);

        return list;
    }

    @Test(dataProvider="testStbViewData")
    public void testStbView(String remoteType, String actualRemote, String mobileParam,
            String ua, boolean isMobile, String traceUrl, boolean traceAvailable) throws IOException {
        MetaStb stb = TestUtils.createGenericMetaStb();
        stb.setRemoteType(remoteType);
        stb.setSerialHost(traceUrl);

        MockMetaStbCache cache = new MockMetaStbCache();
        cache.cache.put(TestUtils.ID, stb);
        ViewController controller = getViewController(cache);

        ModelAndView mav = controller.stbView(TestUtils.ID, mobileParam, null, null, ua, null, null);

        Assert.assertEquals(mav.getModel().get(ViewConstants.MOBILE), isMobile);
        Assert.assertEquals(mav.getModel().get(ViewConstants.TRACE_AVAILABLE), traceAvailable);
        Remote remote = (Remote) mav.getModel().get(ViewConstants.REMOTE);
        Assert.assertEquals(remote.getName(), actualRemote);
    }
    
    @Test(dataProvider="testStbSimplifiedViewData")
    public void testStbECPView(String remoteType, String actualRemote, String mobileParam,
            String ua, boolean isMobile, String traceUrl, boolean traceAvailable) throws IOException {
        MetaStb stb = TestUtils.createGenericMetaStb();
        stb.setRemoteType(remoteType);
        stb.setSerialHost(traceUrl);

        MockMetaStbCache cache = new MockMetaStbCache();
        cache.cache.put(TestUtils.ID, stb);
        ViewController controller = getViewController(cache);

        ModelAndView mav = controller.stbSimplifiedView(TestUtils.ID, mobileParam, null, null, ua, null, null);

        Assert.assertEquals(mav.getModel().get(ViewConstants.MOBILE), isMobile);
        Assert.assertEquals(mav.getModel().get(ViewConstants.TRACE_AVAILABLE), traceAvailable);
        Remote remote = (Remote) mav.getModel().get(ViewConstants.REMOTE);
        Assert.assertEquals(remote.getName(), actualRemote);
    }
    
    private ViewController getViewController(MetaStbCache cache) throws IOException {
        RemoteManager remoteMan = new RemoteManager();
        remoteMan.load();
        ViewController controller = new ViewController();
        Wiring.autowire(controller, cache);
        Wiring.autowire(controller, remoteMan);
        JwtDeviceAccessValidator validator = new JwtDeviceAccessValidator(null, null, false);
        Wiring.autowire(controller, validator);
        DawgShowConfiguration config = new DawgShowConfiguration();
        Wiring.autowire(controller, config);
        return controller;
    }
}
