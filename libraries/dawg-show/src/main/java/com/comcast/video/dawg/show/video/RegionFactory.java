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

import org.springframework.stereotype.Component;

import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.image.OCRRegionInfo;
import com.comcast.cats.image.RegionInfo;

/**
 * Creates {@link ImageCompareRegionInfo} and {@link OCRRegionInfo} from a {@link CerealizableRegionInfo}
 * @author Kevin Pearson
 *
 */
@Component
public class RegionFactory {

    /**
     * Creates the region infos from a cerealizable region info.
     * This determines if this is an ocr comparison or ic comparison based
     * on if the expectedText is defined
     * @param ri The info to get the data from.
     * @return
     */
    public RegionInfo createRegion(CerealizableRegionInfo ri) {
        boolean ic = ri.getExpectedText() == null;
        RegionInfo rv;
        if (ic) {

            ImageCompareRegionInfo icri = new ImageCompareRegionInfo(ri.getName(), ri.getX(), ri.getY(), ri.getWidth(),
                    ri.getHeight());
            icri.setRedTolerance(ri.getRedTolerance());
            icri.setGreenTolerance(ri.getGreenTolerance());
            icri.setBlueTolerance(ri.getBlueTolerance());
            icri.setMatchPct(ri.getMatchPercent());
            rv = icri;
        } else {
            OCRRegionInfo regionInfo = new OCRRegionInfo(ri.getName(),ri.getX(), ri.getY(),
                    ri.getWidth(), ri.getHeight());
            regionInfo.setSuccessTolerance(ri.getMatchPercent());
            regionInfo.setExpectedResult(ri.getExpectedText());
            rv = regionInfo;
        }
        rv.setXTolerance(ri.getxTolerance());
        rv.setYTolerance(ri.getyTolerance());
        return rv;
    }

    /**
     * Creates a cerealizable region info from a cats region info
     * @param ri The cats region ingo
     * @return
     */
    public CerealizableRegionInfo createCerealizable(RegionInfo ri) {
        CerealizableRegionInfo cri = new CerealizableRegionInfo(ri.getName(), ri.getX(), ri.getY(),
                ri.getWidth(), ri.getHeight());
        cri.setxTolerance(ri.getXTolerance());
        cri.setyTolerance(ri.getYTolerance());

        if (ri instanceof ImageCompareRegionInfo) {
            ImageCompareRegionInfo icri = (ImageCompareRegionInfo) ri;

            cri.setRedTolerance(icri.getRedTolerance());
            cri.setGreenTolerance(icri.getGreenTolerance());
            cri.setBlueTolerance(icri.getBlueTolerance());
            cri.setMatchPercent((int) icri.getMatchPct());
        } else {
            OCRRegionInfo ori = (OCRRegionInfo) ri;
            cri.setMatchPercent(ori.getSuccessTolerance());
            cri.setExpectedText(ori.getExpectedResult());
        }
        return cri;
    }
}
