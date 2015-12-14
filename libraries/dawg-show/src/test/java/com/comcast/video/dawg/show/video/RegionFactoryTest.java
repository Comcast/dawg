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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.image.OCRRegionInfo;
import com.comcast.cats.image.RegionInfo;

public class RegionFactoryTest implements RegionInfoTestConstants {

    @Test
    public void testCreateImageRegion() {
        CerealizableRegionInfo cri = createBaseCRI();
        cri.setRedTolerance(RED_TOLERANCE);
        cri.setGreenTolerance(GREEN_TOLERANCE);
        cri.setBlueTolerance(BLUE_TOLERANCE);

        RegionFactory regionFactory = new RegionFactory();
        RegionInfo regInfo = regionFactory.createRegion(cri);

        Assert.assertTrue(regInfo instanceof ImageCompareRegionInfo);
        ImageCompareRegionInfo icri = (ImageCompareRegionInfo) regInfo;
        assertBaseProps(icri);
        Assert.assertEquals(icri.getMatchPct(), Float.valueOf(MATCH_PERCENT));
        Assert.assertEquals(icri.getRedTolerance(), RED_TOLERANCE);
        Assert.assertEquals(icri.getGreenTolerance(), GREEN_TOLERANCE);
        Assert.assertEquals(icri.getBlueTolerance(), BLUE_TOLERANCE);
    }

    @Test
    public void testCreateOCRRegion() {
        CerealizableRegionInfo cri = createBaseCRI();
        cri.setExpectedText(EXPECTED_TEXT);

        RegionFactory regionFactory = new RegionFactory();
        RegionInfo regInfo = regionFactory.createRegion(cri);

        Assert.assertTrue(regInfo instanceof OCRRegionInfo);
        OCRRegionInfo ori = (OCRRegionInfo) regInfo;
        assertBaseProps(ori);
        Assert.assertEquals(ori.getSuccessTolerance(), MATCH_PERCENT);
        Assert.assertEquals(ori.getExpectedResult(), EXPECTED_TEXT);
    }

    @Test
    public void testCreateCerealizableImageRegion() {
        ImageCompareRegionInfo icri = new ImageCompareRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        icri.setXTolerance(XTOLERANCE);
        icri.setYTolerance(YTOLERANCE);
        icri.setMatchPct(MATCH_PERCENT);
        icri.setRedTolerance(RED_TOLERANCE);
        icri.setGreenTolerance(GREEN_TOLERANCE);
        icri.setBlueTolerance(BLUE_TOLERANCE);

        RegionFactory regionFactory = new RegionFactory();
        CerealizableRegionInfo cri = regionFactory.createCerealizable(icri);
        assertBaseProps(cri);
        Assert.assertEquals(cri.getMatchPercent(), MATCH_PERCENT);
        Assert.assertEquals(cri.getRedTolerance(), RED_TOLERANCE);
        Assert.assertEquals(cri.getGreenTolerance(), GREEN_TOLERANCE);
        Assert.assertEquals(cri.getBlueTolerance(), BLUE_TOLERANCE);
    }

    @Test
    public void testCreateCerealizableOCRRegion() {

        OCRRegionInfo ori = new OCRRegionInfo(NAME, X, Y, WIDTH, HEIGHT);
        ori.setXTolerance(XTOLERANCE);
        ori.setYTolerance(YTOLERANCE);
        ori.setSuccessTolerance(MATCH_PERCENT);
        ori.setExpectedResult(EXPECTED_TEXT);

        RegionFactory regionFactory = new RegionFactory();
        CerealizableRegionInfo cri = regionFactory.createCerealizable(ori);
        assertBaseProps(cri);
        Assert.assertEquals(cri.getMatchPercent(), MATCH_PERCENT);
        Assert.assertEquals(cri.getExpectedText(), EXPECTED_TEXT);
    }

    @Test
    public void testCreateRegionNullValues() {
        CerealizableRegionInfo cri = new CerealizableRegionInfo();

        RegionFactory regionFactory = new RegionFactory();
        RegionInfo regInfo = regionFactory.createRegion(cri);

        Assert.assertNull(regInfo.getName());
    }

    @Test
    public void testCreateCerealizableNullValues() {
        ImageCompareRegionInfo icri = new ImageCompareRegionInfo(null, 0, 0, 0, 0);

        RegionFactory regionFactory = new RegionFactory();
        CerealizableRegionInfo cri = regionFactory.createCerealizable(icri);

        Assert.assertNull(cri.getName());
    }

    private void assertBaseProps(RegionInfo regInfo) {
        Assert.assertEquals(regInfo.getName(), NAME);
        Assert.assertEquals(regInfo.getX(), X);
        Assert.assertEquals(regInfo.getY(), Y);
        Assert.assertEquals(regInfo.getWidth(), WIDTH);
        Assert.assertEquals(regInfo.getHeight(), HEIGHT);
        Assert.assertEquals(regInfo.getXTolerance(), XTOLERANCE);
        Assert.assertEquals(regInfo.getYTolerance(), YTOLERANCE);
    }

    private void assertBaseProps(CerealizableRegionInfo cri) {
        Assert.assertEquals(cri.getName(), NAME);
        Assert.assertEquals(cri.getX(), X);
        Assert.assertEquals(cri.getY(), Y);
        Assert.assertEquals(cri.getWidth(), WIDTH);
        Assert.assertEquals(cri.getHeight(), HEIGHT);
        Assert.assertEquals(cri.getxTolerance(), XTOLERANCE);
        Assert.assertEquals(cri.getyTolerance(), YTOLERANCE);
    }

    private CerealizableRegionInfo createBaseCRI() {
        CerealizableRegionInfo cri = new CerealizableRegionInfo();
        cri.setName(NAME);
        cri.setX(X);
        cri.setY(Y);
        cri.setWidth(WIDTH);
        cri.setHeight(HEIGHT);
        cri.setxTolerance(XTOLERANCE);
        cri.setyTolerance(YTOLERANCE);
        cri.setMatchPercent(MATCH_PERCENT);
        return cri;
    }
}
