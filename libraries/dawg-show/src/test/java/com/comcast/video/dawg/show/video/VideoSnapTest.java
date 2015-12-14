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
package com.comcast.video.dawg.show.video;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.video.dawg.cats.video.DawgVideoOutput;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.show.cache.ClientCache;
import com.comcast.video.dawg.show.cache.MetaStbCache;
import com.comcast.video.dawg.show.cache.UniqueIndexedCache;

/**
 * Unit tests for VideoSnap class
 * @author divyarajam
 *
 */

public class VideoSnapTest {
    private static final BufferedImage PC_IMG;

    /** Load some resources */
    static {
        InputStream is = null;
        try {
            is = VideoSnapTest.class
                .getResourceAsStream("parentalChallenge.jpg");
            PC_IMG = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @Test
    public void testCaptureImage() throws IOException {
        String deviceId = "000000000001";

        final DawgVideoOutput mockDVO = EasyMock
            .createMock(DawgVideoOutput.class);
        EasyMock.expect(mockDVO.captureScreen()).andReturn(PC_IMG);
        VideoSnap videoSnap = new VideoSnap() {
            @Override
            protected DawgVideoOutput createDawgVideoOutput(MetaStb stb) {
                return mockDVO;
            }
        };
        MetaStbCache stbCache = new MetaStbCache();
        MetaStb stb = new MetaStb();
        stbCache.putMetaStb(deviceId, stb);
        MockHttpSession session = new MockHttpSession();

        EasyMock.replay(mockDVO);
        String iid = videoSnap.captureImage(deviceId, session, stbCache);
        Assert.assertNotNull(iid);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(PC_IMG, "jpg", bao);

        EasyMock.verify(mockDVO);

        UniqueIndexedCache<BufferedImage> imgCache = ClientCache
            .getClientCache(session).getImgCache();
        Assert.assertSame(imgCache.getItem(iid), PC_IMG, "Captured image is not matching with expected image");

    }

    @Test
    public void testAddImagesToZipFile() throws IOException {
        String imageIds[] = { "", "", "" };
        MockHttpSession session = new MockHttpSession();
        MockHttpServletResponse response = new MockHttpServletResponse();
        UniqueIndexedCache<BufferedImage> imgCache = ClientCache
            .getClientCache(session).getImgCache();
        imageIds[0] = imgCache.storeItem(PC_IMG);
        imageIds[1] = imgCache.storeItem(PC_IMG);
        imageIds[2] = null;
        String deviceIds[] = { "000000000001", "000000000002", "000000000003" };
        int expectedZipSizeInBytes = 28954;

        VideoSnap videoSnap = new VideoSnap();

        videoSnap.addImagesToZipFile(imageIds, deviceIds, response, session);
        Assert.assertEquals(response.getContentType(), "application/zip", "Content type is not in zip format");
        String header = response.getHeader("Content-Disposition");
        Assert.assertTrue(header.contains("attachment"), "Response header does not indicates attachment");
        Assert.assertTrue(header.contains(".zip"), "Attached file is not in zip format");
        Assert.assertEquals(response.getContentAsByteArray().length,
                expectedZipSizeInBytes, "Zip file size in http response is not matching with expected zip size.");

    }

    @Test
    public void testSaveSnappedImage() throws IOException {
        String deviceId = "000000000001";
        MockHttpSession session = new MockHttpSession();
        MockHttpServletResponse response = new MockHttpServletResponse();
        UniqueIndexedCache<BufferedImage> imgCache = ClientCache
            .getClientCache(session).getImgCache();
        String imageId = imgCache.storeItem(PC_IMG);

        VideoSnap videoSnap = new VideoSnap();
        videoSnap.saveSnappedImage(imageId, deviceId, response, session);

        String header = response.getHeader("Content-Disposition");
        Assert.assertTrue(header.contains("attachment"),"Response header does not indicates attachment");
        Assert.assertTrue(header.contains(".jpg"),"Attached file is not in jpg format");

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(PC_IMG, "jpg", bao);
        Assert.assertEquals(response.getContentAsByteArray(),
                bao.toByteArray(), "Saved image size is not matching with expected size");
    }

    /**
     * Test to verify getCaptureImageIds() method. It verifies whether the image id getting on capturing screenshot, are
     * matching with corresponding devices or not.
     */
    @Test
    public void testGetCaptureImageIds() {
        String deviceIds[] = {"000000000001", "000000000002"};
        String capturedImageIds[] = {"Image001", "Image002"};
        MockHttpSession session = new MockHttpSession();
        VideoSnap videoSnap = EasyMock.createMockBuilder(VideoSnap.class)
            .addMockedMethod("captureImage", String.class, HttpSession.class, MetaStbCache.class).createMock();
        MetaStbCache metaStbCache = new MetaStbCache();
        for (int i = 0; i < deviceIds.length; i++) {
            EasyMock.expect(videoSnap.captureImage(deviceIds[i], session, metaStbCache)).andReturn(capturedImageIds[i]);
        }
        EasyMock.replay(videoSnap);
        videoSnap.getCaptureImageIds(deviceIds, session, metaStbCache);
        EasyMock.verify(videoSnap);
    }

}
