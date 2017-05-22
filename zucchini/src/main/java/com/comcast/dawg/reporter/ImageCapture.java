/**
 * Copyright 2017 Comcast Cable Communications Management, LLC
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
package com.comcast.dawg.reporter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.zucchini.TestContext;

/**
 *Capture screen shot using web driver 
 */
public class ImageCapture {

    /**
     * Captures the UI screenshot     
     * @return BufferedImage   
     */
    private static BufferedImage captureScreenShot() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        File screenshot = (File) ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        return readImageFile(screenshot);
    }

    /**
     * Gets the buffered image  
     * @param image file  
     * @return BufferedImage   
     */
    private static BufferedImage readImageFile(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Add image 
     */
    public static void addImage() {
        captureScreenShot();
    }

}
