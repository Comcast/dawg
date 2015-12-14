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
package com.comcast.video.dawg.cats.video;

import java.awt.image.BufferedImage;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.cats.video.service.VideoController;
import com.comcast.pantry.test.TestList;
import com.comcast.pantry.test.Wiring;
import com.comcast.video.dawg.common.MetaStb;

public class DawgVideoOutputTest {

    @DataProvider(name="testDawgVideoOutputData")
    public TestList testDawgVideoOutputData() {
        TestList tl = new TestList();

        tl.add("http://10.253.1.2", "2", false);
        tl.add("10.253.1.2", null, false);
        tl.add("!@#$%^&*()", null, true);

        return tl;
    }

    @Test(dataProvider="testDawgVideoOutputData")
    public void testDawgVideoOutput(String videoHost, String camera, boolean throwExc) {
        MetaStb stb = new MetaStb();
        stb.setVideoSourceUrl(videoHost);
        stb.setVideoCamera(camera);
        RuntimeException exc = null;
        DawgVideoOutput dvo = null;
        try {
            dvo = new DawgVideoOutput(stb);
        } catch (RuntimeException e) {
            exc = e;
        }
        if (throwExc) {
            Assert.assertNotNull(exc);
        } else {
            Assert.assertNull(exc);
            VideoController mockVC = EasyMock.createMock(VideoController.class);
            BufferedImage img = new BufferedImage(720, 408, BufferedImage.TYPE_INT_RGB);
            EasyMock.expect(mockVC.getImage()).andReturn(img);

            Wiring.wire(dvo, "videoController", mockVC);

            EasyMock.replay(mockVC);

            Assert.assertEquals(dvo.captureScreen(), img);
            Assert.assertTrue(dvo.getDispatcher() instanceof DawgEventDispatcher);
            Assert.assertEquals(dvo.getVideoController(), mockVC);

            EasyMock.verify(mockVC);
        }
    }
}
