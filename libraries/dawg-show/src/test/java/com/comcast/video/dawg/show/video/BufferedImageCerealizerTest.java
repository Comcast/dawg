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
import java.io.IOException;

import javax.imageio.ImageIO;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.cereal.CerealException;
import com.comcast.cereal.ObjectCache;

public class BufferedImageCerealizerTest {

    @Test
    public void testCerealize() throws IOException, CerealException {
        BufferedImageCerealizer cerealizer = new BufferedImageCerealizer();

        BufferedImage image = ImageIO.read(BufferedImageCerealizerTest.class.getResourceAsStream("comcast.jpg"));
        String base64 = cerealizer.cerealize(image, new ObjectCache());
        BufferedImage image2 = cerealizer.deCerealize(base64, new ObjectCache());

        /** Verifying the height and width of the two images it probably good enough.
         * Since all the bytes are not exact because one is chunked differently */
        Assert.assertEquals(image.getWidth(), image2.getWidth());
        Assert.assertEquals(image.getHeight(), image2.getHeight());
    }
}
