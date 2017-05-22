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

public class ImageCapture {


    private static BufferedImage captureScreenShot() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        File screenshot = (File) ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        return readImageFile(screenshot);
    }

    private static BufferedImage readImageFile(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void addImage() {
        captureScreenShot();
    }

}
