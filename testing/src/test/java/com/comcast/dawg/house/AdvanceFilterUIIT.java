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
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.dawg.MetaStbBuilder;
import com.comcast.dawg.TestServers;
import com.comcast.dawg.house.pages.IndexPage;
import com.comcast.pantry.test.TestList;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.house.DawgHouseClient;

public class AdvanceFilterUIIT extends SeleniumTest {
    public static final String ID_PREF = "testadvf";
    public static final String TOKEN = "testtoken" + System.currentTimeMillis();
    public Map<String, MetaStb> stbs;
    public DawgHouseClient client = new DawgHouseClient(TestServers.getHouse());

    public MetaStb defaultStb;
    public MetaStb testStb;

    @BeforeClass(groups="uitest")
    public void populateStbs() throws IOException {
        stbs = new HashMap<String, MetaStb>();
        defaultStb = MetaStbBuilder.build().uid(ID_PREF).stb();
        testStb = MetaStbBuilder.build().uid(ID_PREF).model("TestModel").caps(
                new String[] {"TestCap"}).make("TestMake").power("TestPower").rackName("TestRack").controllerIpAddress("TestIpAddress").hardwareRevision("TestHardwareRevision").slotName("TestSlotName").irBlasterType("TestIrBlasterType").stb();
        addStb(defaultStb, testStb);
        client.add(stbs.values());
    }

    private void addStb(MetaStb... s) {
        for (MetaStb stb : s) {
            stbs.put(stb.getId(), stb);
        }
    }

    @DataProvider(name="testSingleFilterData")
    public TestList testSingleFilterData() {
        TestList tl = new TestList();

        String defDid = defaultStb.getId();
        String testDid = testStb.getId();
        String[] defDidArr = new String[] {defDid};
        String[] testDidArr = new String[] {testDid};
        tl.add("Id", "equals", testDid, false, testDidArr, defDidArr);
        tl.add("Model", "matches", "Test.*", false, testDidArr, defDidArr);
        tl.add("Make", "contains", "Test", false, testDidArr, defDidArr);
        tl.add("Rack Name", "contains", "Test", false, testDidArr, defDidArr);
        tl.add("Controller Ip Address", "contains", "TestIp", false, testDidArr, defDidArr);
        tl.add("Slot Name", "contains", "TestSlotName", false, testDidArr, defDidArr);
        tl.add("IR Blaster Type", "contains", "TestIrBlasterType", false, testDidArr, defDidArr);
        tl.add("Hardware Revision", "contains", "TestHardwareRevision", false, testDidArr, defDidArr);
        tl.add("Capabilities", "contains", "TestCap", false, testDidArr, defDidArr);
        tl.add("Capabilities", "contains", "SomeOtherCapability", false, null, new String[] {defDid, testDid}); // matches none

        tl.add("Id", "equals", testDid, true, defDidArr, testDidArr);
        tl.add("Model", "matches", "Test.*", true, defDidArr, testDidArr);
        tl.add("Make", "contains", "Test", true, defDidArr, testDidArr);
        tl.add("Rack Name", "contains", "Test", true, defDidArr, testDidArr);
        tl.add("Controller Ip Address", "contains", "TestIp", true, defDidArr, testDidArr);
        tl.add("Slot Name", "contains", "TestSlotName", true, defDidArr, testDidArr);
        tl.add("IR Blaster Type", "contains", "TestIrBlasterType", true, defDidArr, testDidArr);
        tl.add("Hardware Revision", "contains", "TestHardwareRevision", true, defDidArr, testDidArr);
        tl.add("Capabilities", "contains", "TestCap", true, defDidArr, testDidArr);
        tl.add("Capabilities", "contains", "SomeOtherCapability", true, new String[] {defDid, testDid}, null);

        return tl;
    }

    /**
     * @author Kevin Pearson
     */
    @Test(
        description="Tests if can return an stb with a single filter",
        dataProvider="testSingleFilterData", groups="uitest",
        threadPoolSize=4
    )
    public void testSingleFilter(String field, String op, String value,
            boolean not, String[] deviceIds, String[] notDeviceIds) throws IOException, InterruptedException {
        AdvanceFilterNavigator afn = launchAdvancedFilter();

        afn.addCondition(field, op, value);
        afn.checkButtonsEnabled(false, false, true, true, false, true);
        if (not) {
            afn.not();
        }
        String display = (not ? "!" : "") + "(" + field + " " + op + " " + value + ")";
        afn.verifyConditions(new String[] {display}, new String[] {});

        performSearchAndCheckDevices(afn.getDriver(), deviceIds, notDeviceIds);
    }

    @DataProvider(name="testGroupData")
    public TestList testGroupData() {
        TestList tl = new TestList();

        String deviceId = testStb.getId();
        tl.add(false, false, new String[] {deviceId}, null);
        tl.add(true, false, null, new String[] {deviceId});

        /** Not */
        tl.add(false, true, null, new String[] {deviceId});
        tl.add(true, true, new String[] {deviceId}, null);

        return tl;
    }

    /**
     * @author Kevin Pearson
     */
    @Test(
        description="Tests if can return an stb with a simple one level group filter",
        dataProvider="testGroupData",
        groups="uitest"
    )
    public void testSimpleGroupFilter(boolean and, boolean not, String[] deviceIds,
            String[] notDeviceIds) throws IOException, InterruptedException {
        AdvanceFilterNavigator afn = launchAdvancedFilter();

        afn.addCondition("Model", "equals", "TestModel");
        afn.addCondition("Capabilities", "contains", "SomeRandomCap");
        afn.checkButtonsEnabled(true, true, true, true, false, false);
        String mConditionDisp = "(Model equals TestModel)";
        String cConditionDisp = "(Capabilities contains SomeRandomCap)";
        String[] displays = new String[] {
            mConditionDisp,
                cConditionDisp
        };
        afn.verifyConditions(displays, new String[] {});
        if (and) {
            afn.and();
        } else {
            afn.or();
        }
        if (not) {
            afn.not();
        }
        afn.checkButtonsEnabled(false, false, true, true, true, true);
        String groupDisp = (not ? "!" : "") + "(" + mConditionDisp + " " + (and ? "and" : "or") + " " + cConditionDisp + ")";
        afn.verifyConditions(new String[] {groupDisp}, new String[] {});
        performSearchAndCheckDevices(afn.getDriver(), deviceIds, notDeviceIds);
    }

    /**
     * @author Kevin Pearson
     */
    @Test(
        description="Tests if can return an stb with a simple one level group filter",
        dataProvider="testGroupData",
        groups="uitest"
    )
    public void testNestedGroupFilter(boolean and, boolean not, String[] deviceIds,
            String[] notDeviceIds) throws IOException, InterruptedException {
        AdvanceFilterNavigator afn = launchAdvancedFilter();

        afn.addCondition("Model", "equals", "TestModel"); //true
        afn.addCondition("Capabilities", "contains", "SomeRandomCap"); //false

        afn.addCondition("Make", "matches", "BadMake"); //false
        afn.addCondition("Power Type", "equals", "TestPower"); //true

        afn.checkButtonsEnabled(true, true, true, true, false, false);
        String modelDisp = "(Model equals TestModel)";
        String capDisp = "(Capabilities contains SomeRandomCap)";
        String makeDisp = "(Make matches BadMake)";
        String powDisp = "(Power Type equals TestPower)";
        String modAndCap = "(" + modelDisp + " and " + capDisp + ")";
        String makeOrPower = "(" + makeDisp + " or " + powDisp + ")";
        String[] displays = new String[] {
            modelDisp, capDisp, makeDisp, powDisp
        };
        afn.verifyConditions(displays, new String[] {});
        afn.checkConditions(true, modelDisp, capDisp);
        afn.checkConditions(false, powDisp, makeDisp);
        afn.and();
        afn.checkConditions(true, powDisp, makeDisp);
        afn.checkConditions(false, modAndCap);
        afn.or();
        afn.checkConditions(true, modAndCap, makeOrPower);
        if (and) {
            afn.and();
        } else {
            afn.or();
        }
        if (not) {
            afn.not();
        }
        afn.checkButtonsEnabled(false, false, true, true, true, true);
        String groupDisp = (not ? "!" : "") + "(" + modAndCap + " " + (and ? "and" : "or") + " " + makeOrPower + ")";
        afn.verifyConditions(new String[] {groupDisp}, new String[] {});
        performSearchAndCheckDevices(afn.getDriver(), deviceIds, notDeviceIds);
    }

    @DataProvider(name="testDelButtonData")
    public TestList testDelButtonData() {
        TestList tl = new TestList();

        tl.add(false);
        tl.add(true);

        return tl;
    }

    /**
     * @author Kevin Pearson
     * @throws IOException
     */
    @Test(
        description="Tests if DEL button will delete conditions from the list",
        dataProvider="testDelButtonData",
        groups="uitest"
    )
        public void testDelButton(boolean multiple) throws IOException {
            AdvanceFilterNavigator afn = launchAdvancedFilter();

            afn.addCondition("Model", "equals", "TestModel");
            afn.addCondition("Capabilities", "contains", "TestCap");
            String modDisp = "(Model equals TestModel)";
            String capDisp = "(Capabilities contains TestCap)";
            if (multiple) {
                afn.checkConditions(true, modDisp, capDisp);
                afn.verifyConditions(new String[] {modDisp, capDisp}, new String[] {});
                afn.del();
                afn.verifyConditions(new String[] {}, new String[] {});
            } else {
                //uncheck one of them
                afn.checkConditions(true, modDisp);
                afn.checkConditions(false, capDisp);
                afn.verifyConditions(new String[] {modDisp}, new String[] {capDisp});
                afn.del();
                afn.verifyConditions(new String[] {}, new String[] {capDisp});
            }
        }

    /**
     * @author Kevin Pearson
     * @throws IOException
     */
    @Test(
        description="Tests if BREAK button will break group conditions up",
        dataProvider="testDelButtonData",
        groups="uitest"
    )
    public void testBreakButton(boolean multiple) throws IOException {
        AdvanceFilterNavigator afn = launchAdvancedFilter();

        afn.addCondition("Model", "equals", "TestModel");
        afn.addCondition("Capabilities", "contains", "TestCap");
        afn.addCondition("Make", "matches", "Test.*");
        afn.addCondition("Family", "contains", "MAC");
        String modDisp = "(Model equals TestModel)";
        String capDisp = "(Capabilities contains TestCap)";
        String makeDisp = "(Make matches Test.*)";
        String famDisp = "(Family contains MAC)";
        String makeOrFam = "(" + makeDisp + " or " + famDisp + ")";
        String modAndCap = "(" + modDisp + " and " + capDisp + ")";
        afn.checkConditions(true, modDisp, capDisp);
        afn.checkConditions(false, makeDisp, famDisp);
        afn.and();
        afn.checkConditions(true, makeDisp, famDisp);
        afn.checkConditions(false, modAndCap);
        afn.or();
        if (multiple) {
            afn.checkConditions(true, modAndCap, makeOrFam);
            afn.verifyConditions(new String[] {modAndCap, makeOrFam}, new String[] {});
            /** Break modAndCap and makeOrFam */
            afn.brk();
            /** Should now have the 4 individual conditions */
            afn.verifyConditions(new String[] {modDisp, capDisp, makeDisp, famDisp}, new String[] {});
        } else {
            afn.checkConditions(true, makeOrFam);
            afn.checkConditions(false, modAndCap);
            afn.verifyConditions(new String[] {makeOrFam}, new String[] {modAndCap});
            afn.brk();
            /** Should now have the 2 individual conditions (make and fam), and one group condition modAndCap */
            afn.verifyConditions(new String[] {makeDisp, famDisp}, new String[] {modAndCap});
        }
    }

    private AdvanceFilterNavigator launchAdvancedFilter() {
        RemoteWebDriver driver = drivers.get();
        driver.get(TestServers.getHouse() + TOKEN + "?q=return_nothing_query");
        driver.findElementByClassName(IndexPage.ADV_SEARCH_BUTTON).click();

        return new AdvanceFilterNavigator(driver);
    }

    private void performSearchAndCheckDevices(RemoteWebDriver driver, String[] deviceIdsToHave, String[] deviceIdsNotToHave) {
        WebElement filteredTable = driver.findElementByClassName(IndexPage.FILTERED_TABLE);

        WebDriverWait wait = new WebDriverWait(driver, 20);
        driver.findElementByClassName(IndexPage.BTN_SEARCH).click();

        /** Need two waits... first for the original table to be removed from the DOM, then for it
         * to be present again */
        wait.until(ExpectedConditions.stalenessOf(filteredTable));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(IndexPage.FILTERED_TABLE)));
        filteredTable = driver.findElementByClassName(IndexPage.FILTERED_TABLE);

        if (deviceIdsToHave != null) {
            hasDevices(filteredTable, deviceIdsToHave, true);
        }
        if (deviceIdsNotToHave != null) {
            hasDevices(filteredTable, deviceIdsNotToHave, false);
        }
    }

    private void hasDevices(WebElement filteredTable, String[] deviceIds, boolean haveAll) {
        for (String deviceId : deviceIds) {
            boolean has = true;
            try {
                filteredTable.findElement(By.xpath("div[@data-deviceid='" + deviceId + "']"));
            } catch (NoSuchElementException e) {
                has = false;
            }
            Assert.assertEquals(has, haveAll);
        }
    }

    @AfterClass
    public void deleteStbs() throws IOException {
        client.deleteByIds(stbs.keySet());
    }
}
