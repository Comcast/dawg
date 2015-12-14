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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.comcast.cereal.CerealException;
import com.comcast.cereal.Cerealizer;
import com.comcast.cereal.ObjectCache;

/**
 * Cerealizes a {@link BufferedImage} into a base64 encoded jpg
 * @author Kevin Pearson
 *
 */
public class BufferedImageCerealizer implements Cerealizer<BufferedImage, String> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage deCerealize(String cereal, ObjectCache objectCache) throws CerealException {
        byte[] data = Base64.decodeBase64(cereal);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        try {
            try {
                return ImageIO.read(bais);
            } catch (IOException e) {
                throw new CerealException("Failed to deCerealize BufferedImage", e);
            }
        } finally {
            IOUtils.closeQuietly(bais);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String cerealize(BufferedImage object, ObjectCache objectCache) throws CerealException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            try {
                ImageIO.write(object, "jpg", baos);
            } catch (IOException e) {
                throw new CerealException("Failed to cerealize BufferedImage", e);
            }
            return Base64.encodeBase64String(baos.toByteArray());
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }
}
