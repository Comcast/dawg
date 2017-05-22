package com.comcast.dawg.helper;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.dawg.config.TestServerConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.zucchini.TestContext;

/**
 * Helper functionalities for Dawg House login 
 * @author priyanka.sl
 */
public class LoginHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginHelper.class);

    /** lock object */
    private static Object lock = new Object();
    private static LoginHelper loginHelper;
    private static String username = null;
    private static String password = null;
    private static String dawgUrl = null;

    /**
     * Creates single instance of loginHelper
     * @return loginHelper
     */
    public static LoginHelper getInstance() {
        synchronized (lock) {
            if (null == loginHelper) {
                loginHelper = new LoginHelper();
            }
        }
        return loginHelper;
    }

    /**
     * Get dawg - house login user name  
     * @return String - User name
     */
    public static String getDawgUserName() {
        if (null == username) {
            username = TestServerConfig.getUsername();
        }
        return username;
    }

    /**
     * Get dawg -houselogin password  
     * @return String - Password
     */
    public static String getDawgPassword() {
        if (null == password) {
            password = TestServerConfig.getPassword();
        }
        return password;
    }

    /**
     * Get dawg -house url  
     * @return String - dawg url
     */
    public static String getDawgUrl() {
        if (null == dawgUrl) {
            dawgUrl = TestServerConfig.getHouse();
        }
        return dawgUrl;
    }

    /**
     * Enter login credentials to dawg-house login form  
     * @param- username
     * @param -password
     */
    public void enterLoginCredentials(String username, String password) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
    }

}
