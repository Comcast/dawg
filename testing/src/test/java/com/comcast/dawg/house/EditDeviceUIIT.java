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
package com.comcast.dawg.house;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.dawg.MetaStbBuilder;
import com.comcast.dawg.TestServers;
import com.comcast.dawg.house.pages.IndexPage;
import com.comcast.dawg.selenium.Browser;
import com.comcast.dawg.selenium.BrowserServiceManager;
import com.comcast.pantry.test.TestList;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.house.DawgHouseClient;

public class EditDeviceUIIT {

    public DawgHouseClient client = new DawgHouseClient(TestServers.getHouse());
    public Map<String, MetaStb> addedStbs = new HashMap<String, MetaStb>();
    public static final String DEVICE_PREF = "edit";
    public static final String CXT_MENU = "cxtMenu";
    
    @Test
    public void testLaunchOverlay() throws IOException {
        MetaStb stb = MetaStbBuilder.build().uid(DEVICE_PREF).stb();
        addStb(stb);
        String token = MetaStbBuilder.getUID("testtoken");
        EditDeviceOverlay edOverlay = loadPageAndLaunchEditOverlay(BrowserServiceManager.getDriver(Browser.chrome), token, stb.getId());

        String[] propsToCheck = new String[] {
            MetaStb.NAME, MetaStb.CAPABILITIES, MetaStb.FAMILY, MetaStb.MAKE, MetaStb.MODEL, MetaStb.POWERTYPE,
                MetaStb.PROGRAM, MetaStb.REMOTETYPE, MetaStb.TAGS , MetaStb.CONTROLLER_IP_ADDRESS, MetaStb.HARDWARE_REVISION, MetaStb.SLOT_NAME, MetaStb.IRBLASTERTYPE
        };
        Map<String, Object> data = stb.getData();
        for (String prop : propsToCheck) {
            EditDeviceRow row = null;
            try {
                row = edOverlay.getRow(prop);
            } catch (NoSuchElementException e) {
                Assert.fail("No row found for property '" + prop + "'");
            }
            Object obj = data.get(prop);
            String objStr = null;
            if (obj != null) {
                if (obj instanceof Collection) {
                    objStr = StringUtils.join((Collection<?>) obj, ", ");
                } else {
                    objStr = obj.toString();
                }
            }
            Assert.assertEquals(row.getValueText(), objStr);
        }
    }

    @DataProvider(name = "testChangeValueData")
    public TestList testChangeValueData() {
        TestList tl = new TestList();

        tl.add(true, "nameChange", true);
        tl.add(false, MetaStbBuilder.NAME, false);

        return tl;
    }

    /**
     *
     * @param save true if the changes should be saved
     * @param refreshedName The value given for the property "name" when the page is refreshed
     * @param isModified If the property should be marked as modified when the page is refreshed
     * @author Kevin Pearson
     * @throws IOException
     */
    @Test(dataProvider = "testChangeValueData")
    public void testChangeValue(boolean save, String refreshedName, boolean isModified) throws IOException {
        RemoteWebDriver driver = BrowserServiceManager.getDriver(Browser.chrome);
        MetaStb stb = MetaStbBuilder.build().uid(DEVICE_PREF).stb();
        addStb(stb);
        String token = MetaStbBuilder.getUID("testtoken");
        EditDeviceOverlay edOverlay = loadPageAndLaunchEditOverlay(driver, token, stb.getId());
        String nameChange = "nameChange";

        EditDeviceRow row = edOverlay.getRow("name");
        Assert.assertFalse(row.isModified());
        row.changeValue(nameChange);
        Assert.assertTrue(row.isModified());
        EditDeviceOverlay edOverlayNext;
        if (save) {
            edOverlay.save();

            /** Wait until page loads */
            waitForNewPageToLoad(driver, edOverlay.getOverlayUI());
            edOverlayNext = launchEditOverlay(driver, stb.getId());
        } else {
            Assert.assertTrue(edOverlay.getOverlayUI().isDisplayed());
            edOverlay.close();
            Assert.assertFalse(edOverlay.getOverlayUI().isDisplayed());
            /** Refresh */
            edOverlayNext = loadPageAndLaunchEditOverlay(driver, token, stb.getId());
        }
        Assert.assertTrue(ExpectedConditions.stalenessOf(edOverlay.getOverlayUI()).apply(driver));
        row = edOverlayNext.getRow("name");
        Assert.assertEquals(row.isModified(), isModified);
        Assert.assertEquals(row.getValueText(), refreshedName);
    }

    @DataProvider(name = "testChangeModelNameData")
    public TestList testChangeModelNameData() {
        TestList testList = new TestList();

        testList.add("", null, null, false, false);
        testList.add("SA3000", "VOD", "MAC_S", true, false);
        testList.add("SA4250HDC", "HD, DVR, NOT68K, VOD, ASTB", "MAC_S", true, false);
        testList.add("SA4250HDC", "TestCap1, TestCap2", "TestFamily", true, true);
        testList.add("InvalidModelName", "TestCap1, TestCap2", "TestFamily", false, true);
        testList.add("InvalidModelName", null, null, false, false);

        return testList;
    }

    /**
     * This method checks editing the Stb model name with valid, invalid names
     * and verifies the populated family and capabilities based on modified
     * model name
     *
     * @param newModelName
     *            the new model name
     * @param expectedCapabilities
     *            expected capabilities that should be populated
     * @param expectedFamily
     *            expected family that should be populated
     * @param validModelName
     *            set true if the given model name is valid and present in database
     * @param modifiedProps
     *            if set true, will change the isModified property of family and
     *            capabilities to true
     * @throws IOException
     */
    @Test(dataProvider = "testChangeModelNameData")
    public void testChangeModelName(String newModelName, String expectedCapabilities, String expectedFamily, boolean validModelName, boolean modifiedProps) throws IOException {
        RemoteWebDriver driver = BrowserServiceManager.getDriver(Browser.chrome);
        MetaStb stb = MetaStbBuilder.build().uid(DEVICE_PREF).stb();
        addStb(stb);
        String token = MetaStbBuilder.getUID("testtoken");
        EditDeviceOverlay edOverlay = loadPageAndLaunchEditOverlay(driver, token, stb.getId());
        EditDeviceRow model = edOverlay.getRow("model");
        EditDeviceRow family = edOverlay.getRow("family");
        EditDeviceRow capabilities = edOverlay.getRow("capabilities");
        Assert.assertFalse(model.isModified());
        if (modifiedProps) {
            family.changeValue("TestFamily");
            capabilities.changeValue("TestCap1, TestCap2");
        }
        model.changeValue(newModelName);
        EditDeviceOverlay edOverlayNext;
        edOverlay.save();

        /** Wait until page loads */
        waitForNewPageToLoad(driver, edOverlay.getOverlayUI());
        edOverlayNext = launchEditOverlay(driver, stb.getId());
        Assert.assertTrue(ExpectedConditions.stalenessOf(edOverlay.getOverlayUI()).apply(driver));
        if (!validModelName && !modifiedProps) {
            MetaStb updated = client.getById(stb.getId());
            Map<String, Object> data = updated.getData();
            Assert.assertNull(data.get("family"));
            Assert.assertNull(data.get("capabilities"));
        } else {
            model = edOverlayNext.getRow("model");
            Assert.assertEquals(model.getValueText(), newModelName);
            family = edOverlayNext.getRow("family");
            Assert.assertEquals(family.getValueText(), expectedFamily);
            capabilities = edOverlayNext.getRow("capabilities");
            Assert.assertEquals(capabilities.getValueText(), expectedCapabilities);
        }
    }

    @DataProvider(name = "testAddPropertyNonSetData")
    public TestList testAddPropertyNonSetData() {
        TestList tl = new TestList();

        tl.add("myVal");
        tl.add("");
        tl.add("\"?<>!@#$%^&*(){}+_:.,;'[]=-`~'|\\");

        return tl;
    }

    /**
     *
     * @param val The value to give for the new property
     * @author Kevin Pearson
     * @throws IOException
     */
    @Test(dataProvider = "testAddPropertyNonSetData")
    public void testAddPropertyNonSet(String val) throws IOException {
        RemoteWebDriver driver = BrowserServiceManager.getDriver(Browser.chrome);
        MetaStb stb = MetaStbBuilder.build().uid(DEVICE_PREF).stb();
        addStb(stb);
        String token = MetaStbBuilder.getUID("testtoken");
        EditDeviceOverlay edOverlay = loadPageAndLaunchEditOverlay(driver, token, stb.getId());

        String myKey = "myKey";
        edOverlay.addProp(myKey, false, true);
        EditDeviceRow row = edOverlay.getRow(myKey);
        Assert.assertNotNull(row);
        Assert.assertEquals(row.getPropDisp(), "My Key");
        Assert.assertTrue(row.isModified());
        row.changeValue(val);
        Assert.assertEquals(row.getValueText(), val);
        edOverlay.save();
        waitForNewPageToLoad(driver, edOverlay.getOverlayUI());

        MetaStb updated = client.getById(stb.getId());
        Object newVal = updated.getData().get(myKey);
        Assert.assertNotNull(newVal);
        Assert.assertTrue(newVal instanceof String);
        Assert.assertEquals(newVal, val);
    }

    @DataProvider(name = "testAddPropertySetData")
    public TestList testAddPropertySetData() {
        TestList tl = new TestList();

        tl.add("", new String[]{});
        tl.add("val1", new String[]{"val1" });
        tl.add("val1,val2", new String[]{"val1", "val2" });
        tl.add("val1, val2", new String[]{"val1", "val2" });
        tl.add("val1, val2,", new String[]{"val1", "val2" });

        return tl;
    }

    /**
     *
     * @param val The value to give for the new property
     * @author Kevin Pearson
     * @throws IOException
     */
    @Test(dataProvider = "testAddPropertySetData")
    public void testAddPropertySet(String val, String[] contained) throws IOException {
        RemoteWebDriver driver = BrowserServiceManager.getDriver(Browser.chrome);
        MetaStb stb = MetaStbBuilder.build().uid(DEVICE_PREF).stb();
        addStb(stb);
        String token = MetaStbBuilder.getUID("testtoken");
        EditDeviceOverlay edOverlay = loadPageAndLaunchEditOverlay(driver, token, stb.getId());

        String myKey = "myKeySet";
        edOverlay.addProp(myKey, true, false);
        EditDeviceRow row = edOverlay.getRow(myKey);
        Assert.assertNotNull(row);
        Assert.assertEquals(row.getPropDisp(), "My Key Set");
        Assert.assertTrue(row.isModified());
        row.changeValue(val);
        edOverlay.save();
        waitForNewPageToLoad(driver, edOverlay.getOverlayUI());

        MetaStb updated = client.getById(stb.getId());
        Object newVal = updated.getData().get(myKey);
        Assert.assertNotNull(newVal);
        Assert.assertTrue(newVal instanceof Collection);
        Collection<?> coll = (Collection<?>) newVal;
        Assert.assertEquals(coll.size(), contained.length);
        for (String contain : contained) {
            Assert.assertTrue(coll.contains(contain),
                "Backend did not contain '" + contain + "', had values: " + coll.toString());
        }
    }

    @Test
    public void testAddPropertyKeyExists() throws IOException {
        RemoteWebDriver driver = BrowserServiceManager.getDriver(Browser.chrome);
        MetaStb stb = MetaStbBuilder.build().uid(DEVICE_PREF).stb();
        addStb(stb);
        String token = MetaStbBuilder.getUID("testtoken");
        EditDeviceOverlay edOverlay = loadPageAndLaunchEditOverlay(driver, token, stb.getId());

        Assert.assertEquals(edOverlay.getRow(MetaStb.NAME).getValueText(), MetaStbBuilder.NAME);
        edOverlay.addProp(MetaStb.NAME, false);
        Alert alert = driver.switchTo().alert();
        alert.accept();
        Assert.assertEquals(edOverlay.getRow(MetaStb.NAME).getValueText(), MetaStbBuilder.NAME);
    }

    @Test
    public void testAddPropertyEmptyKey() throws IOException {
        RemoteWebDriver driver = BrowserServiceManager.getDriver(Browser.chrome);
        MetaStb stb = MetaStbBuilder.build().uid(DEVICE_PREF).stb();
        addStb(stb);
        String token = MetaStbBuilder.getUID("testtoken");
        EditDeviceOverlay edOverlay = loadPageAndLaunchEditOverlay(driver, token, stb.getId());

        int numProps = edOverlay.numRows();
        Assert.assertTrue(numProps > 0);
        edOverlay.addProp(" ", false);
        Assert.assertEquals(edOverlay.numRows(), numProps);
    }

    @Test
    public void testDeleteProp() throws IOException {
        RemoteWebDriver driver = BrowserServiceManager.getDriver(Browser.chrome);
        MetaStb stb = MetaStbBuilder.build().uid(DEVICE_PREF).stb();
        addStb(stb);
        String token = MetaStbBuilder.getUID("testtoken");
        EditDeviceOverlay edOverlay = loadPageAndLaunchEditOverlay(driver, token, stb.getId());

        MetaStb stored = client.getById(stb.getId());
        Assert.assertEquals(stored.getMake(), MetaStbBuilder.MAKE);

        edOverlay.getRow(MetaStb.MAKE).delete();
        edOverlay.save();
        waitForNewPageToLoad(driver, edOverlay.getOverlayUI());

        MetaStb updated = client.getById(stb.getId());
        Assert.assertEquals(stored.getMacAddress(), MetaStbBuilder.MAC_ADDRESS);
        Assert.assertEquals(stored.getRemoteType(), MetaStbBuilder.REMOTE_TYPE);
        Assert.assertNull(updated.getMake());

    }

    /**
     * Launch overlay, mark modified, refresh page, launch overlay, verify it is still marked modified,
     * mark unmodified, refresh page, verify it is still marked unmodified
     * @author Kevin Pearson
     * @throws IOException
     */
    @Test
    public void testMarkPropModified() throws IOException {
        RemoteWebDriver driver = BrowserServiceManager.getDriver(Browser.chrome);
        MetaStb stb = MetaStbBuilder.build().uid(DEVICE_PREF).stb();
        addStb(stb);
        String token = MetaStbBuilder.getUID("testtoken");
        EditDeviceOverlay edOverlay = loadPageAndLaunchEditOverlay(driver, token, stb.getId());

        EditDeviceRow row = edOverlay.getRow(MetaStb.MAKE);
        Assert.assertFalse(row.isModified());
        row.toggleModified();
        Assert.assertTrue(row.isModified());
        edOverlay.save();
        waitForNewPageToLoad(driver, edOverlay.getOverlayUI());

        // make sure page was refreshed by checking staleness
        Assert.assertTrue(ExpectedConditions.stalenessOf(edOverlay.getOverlayUI()).apply(driver));
        edOverlay = launchEditOverlay(driver, stb.getId());
        row = edOverlay.getRow(MetaStb.MAKE);
        Assert.assertTrue(row.isModified());
        row.toggleModified();
        Assert.assertFalse(row.isModified());
        edOverlay.save();
        waitForNewPageToLoad(driver, edOverlay.getOverlayUI());

        // make sure page was refreshed by checking staleness
        Assert.assertTrue(ExpectedConditions.stalenessOf(edOverlay.getOverlayUI()).apply(driver));
        edOverlay = launchEditOverlay(driver, stb.getId());
        row = edOverlay.getRow(MetaStb.MAKE);
        Assert.assertFalse(row.isModified());
    }

    private EditDeviceOverlay loadPageAndLaunchEditOverlay(RemoteWebDriver driver, String token, String deviceId) {
        driver.get(TestServers.getHouse() + token + "?q=" + deviceId);
        return launchEditOverlay(driver, deviceId);
    }

    private EditDeviceOverlay launchEditOverlay(RemoteWebDriver driver, String deviceId) {
        WebElement filteredTable = driver.findElementByClassName(IndexPage.FILTERED_TABLE);
        WebElement deviceRow = filteredTable.findElement(By.xpath("div[@data-deviceid='" + deviceId + "']"));
        (new Actions(driver)).contextClick(deviceRow).perform();
        ContextMenu cxtMenu = new ContextMenu(driver, driver.findElementByClassName(CXT_MENU));
        return cxtMenu.launchEditDeviceOverlay();
    }

    private void waitForNewPageToLoad(RemoteWebDriver driver, WebElement oldPageElement) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.stalenessOf(oldPageElement));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(IndexPage.FILTERED_TABLE)));
    }

    private void addStb(MetaStb stb) {
        client.add(stb);
        addedStbs.put(stb.getId(), stb);
    }

    @AfterClass
    public void deleteStbs() throws IOException {
        client.deleteByIds(addedStbs.keySet());
    }
}
