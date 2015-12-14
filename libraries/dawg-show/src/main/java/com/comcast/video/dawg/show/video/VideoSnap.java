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
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.comcast.video.dawg.cats.video.DawgVideoOutput;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.show.cache.ClientCache;
import com.comcast.video.dawg.show.cache.MetaStbCache;
import com.comcast.video.dawg.show.cache.UniqueIndexedCache;

/**
 * Video snap helper methods like capture image, store it in zip file etc.
 *
 * @author Divya R
 *
 */
@Component
public class VideoSnap {

    private static final Logger LOGGER = Logger.getLogger(VideoSnap.class);
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
            "dd-MM-YY_HH-mm-ss");
    private static final String IMG_FORMAT = "jpg";

    /**
     * Retrieve the images with input device ids from cache and stores it in zip
     * output stream.
     *
     * @param capturedImageIds
     *            Ids of captures images
     * @param deviceMacs
     *            Mac address of selected devices
     * @param response
     *            http servelet response
     * @param session
     *            http session.
     */
    public void addImagesToZipFile(String[] capturedImageIds, String[] deviceMacs, HttpServletResponse response, HttpSession session) {

        UniqueIndexedCache<BufferedImage> imgCache = getClientCache(session)
            .getImgCache();

        response.setHeader("Content-Disposition", "attachment; filename=\""
                + "Images_" + getCurrentDateAndTime() + ".zip\"");
        response.setContentType("application/zip");

        ServletOutputStream serveletOutputStream = null;
        ZipOutputStream zipOutputStream = null;

        try {
            serveletOutputStream = response.getOutputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

            BufferedImage image;
            ZipEntry zipEntry;
            ByteArrayOutputStream imgByteArrayOutputStream = null;

            for (int i = 0; i < deviceMacs.length; i++) {
                // Check whether id of captured image is null or not
                if (!StringUtils.isEmpty(capturedImageIds[i])) {
                    image = imgCache.getItem(capturedImageIds[i]);
                    zipEntry = new ZipEntry(deviceMacs[i].toUpperCase() + "." + IMG_FORMAT);
                    zipOutputStream.putNextEntry(zipEntry);
                    imgByteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(image, IMG_FORMAT, imgByteArrayOutputStream);
                    imgByteArrayOutputStream.flush();
                    zipOutputStream.write(imgByteArrayOutputStream.toByteArray());
                    zipOutputStream.closeEntry();
                }
            }

            zipOutputStream.flush();
            zipOutputStream.close();
            serveletOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch (IOException ioe) {
            LOGGER.error("Image zipping failed !!!", ioe);
        } finally {
            IOUtils.closeQuietly(zipOutputStream);
        }
    }

    /**
     * Retrieve the image with input image id from cache and stores it in output
     * stream.
     *
     * @param imgId
     *            Id of captured image
     * @param deviceId
     *            Device mac address
     * @param response
     *            httpservelet response
     * @param session
     *            http sssion
     * @throws IOException
     */
    public void saveSnappedImage(String imgId, String deviceId,
            HttpServletResponse response, HttpSession session)
        throws IOException {

        String suffixName = getCurrentDateAndTime();
        String fileName = deviceId.toUpperCase() + "_" + suffixName + "."
            + IMG_FORMAT;

        response.setHeader("Content-Disposition", "attachment;filename="
                + fileName);

        UniqueIndexedCache<BufferedImage> imgCache = getClientCache(session)
            .getImgCache();

        BufferedImage img = imgCache.getItem(imgId);
        if (img != null) {
            OutputStream stream = response.getOutputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(img, IMG_FORMAT, byteArrayOutputStream);
            byteArrayOutputStream.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            stream.write(bytes);
            response.flushBuffer();
        } else {
            LOGGER.error("Could not retrieve the captured image!!!");
        }

    }

    /**
     * Helper method to get the cache that specific to the given session
     *
     * @param session
     *            http session
     * @return
     */
    private ClientCache getClientCache(HttpSession session) {
        return ClientCache.getClientCache(session);
    }

    /**
     * Method for creating a {@link DawgVideoOutput}. This is meant to be
     * overridden during tests
     *
     * @param stb
     *            The stb to create a DawgVideoOutput for
     * @return
     */
    protected DawgVideoOutput createDawgVideoOutput(MetaStb stb) {
        return new DawgVideoOutput(stb);
    }

    /**
     * This method takes the current screen shot of selected device, saves it in stb cache and returns the unique string
     * corresponding to that image.
     *
     * @param deviceId
     *            Device Id
     * @param session
     *            Http session
     * @param metaStbCache
     *            Meta stb cache object for that session
     * @return unique string corresponding to captured image if screen capturing is successful, otherwise return null on
     *         failure of screen capture
     */
    public String captureImage(String deviceId, HttpSession session,
            MetaStbCache metaStbCache) {
        LOGGER.debug("Snapping external video frame - BEGIN");
        DawgVideoOutput dawgVideoOutput = createDawgVideoOutput(metaStbCache
                .getMetaStb(deviceId));
        BufferedImage img = dawgVideoOutput.captureScreen();
        LOGGER.debug("Snapping external video frame - COMPLETE");
        return (null == img) ? null : getClientCache(session).getImgCache().storeItem(img);

    }

    /**
     * Returns String containing current date and time in format
     * "DD-MM-YY_hh-mm-ss"
     *
     * @return current date and time in string format.
     */
    private String getCurrentDateAndTime() {
        String currentDateAndTime = DATE_FORMATTER.format(new Date());
        return currentDateAndTime;

    }

    /**
     * Method that take snapshot of given devices given and return a map containing device ids with corresponding
     * snapshot image ids
     *
     * @param devices
     *            the array of settops to take snapshot
     * @param session
     *            hold session details for the client
     * @return a map containing device ids with corresponding snapshot image ids
     */
    public Map<String, String> getCaptureImageIds(String[] deviceIds, HttpSession session, MetaStbCache metaStbCache) {
        Map<String, String> deviceIdImageIdMap = new HashMap<String, String>();
        if (null != deviceIds) {
            for (String deviceId : deviceIds) {
                deviceIdImageIdMap.put(deviceId, captureImage(deviceId, session, metaStbCache));
            }
        }
        return deviceIdImageIdMap;
    }
}
