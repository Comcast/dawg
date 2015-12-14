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
package com.comcast.cats.video.service;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a patched version of the CATS class and is meant to override it. There was an incorrect
 * mapping of dimensions to resolution key strings.
 * @author Kevin Pearson
 *
 */
public enum AxisVideoSize {
    AXIS_D1             (720,480,"D1", false),
    AXIS_D1_SQUARE      (655,480,"D1", true),
    AXIS_4CIF           (704,480,"4CIF", false),
    AXIS_4CIF_SQUARE    (640,480,"4CIF", true),
    AXIS_2CIF           (704,240,"2CIF", false),
    AXIS_2CIF_SQUARE    (640,240,"2CIF", true),
    AXIS_CIF_SQUARE     (320,240,"CIF", true),
    AXIS_CIF            (352,240,"CIF", false),
    AXIS_QCIF_SQUARE    (160,120,"QCIF", true),
    AXIS_QCIF           (176,120,"QCIF", false);


    private Dimension dim;
    private String axisStr;
    private String dimStr;
    private boolean squareResolution;

    AxisVideoSize(int width,int height, String axisString, boolean squareRes) {
        this.axisStr = axisString;
        this.squareResolution = squareRes;
        this.dim = new Dimension(width,height);
        this.dimStr = createDimensionString(dim);
    }

    private static String createDimensionString(Dimension dim) {
        String dimStr = Integer.toString(dim.width) + "x" + Integer.toString(dim.height);
        return dimStr;
    }

    public static List<String> getVideoSizesAsString() {
        AxisVideoSize[] sizes = AxisVideoSize.values();
        List<String> videoSizes = new ArrayList<String>();
        for(int i=0;i<sizes.length;i++) {
            videoSizes.add(sizes[i].dimStr);
        }
        return videoSizes;
    }

    public Dimension getDimension() {
        return this.dim;
    }

    public String getDimensionString() {
        return this.dimStr;
    }

    public String getAxisResolution() {
        return this.axisStr;
    }

    public boolean isSquareResoultion() {
        return squareResolution;
    }

    public static Dimension getDimension(AxisVideoSize axisVideoSize) {
        return axisVideoSize.dim;
    }

    public static String getDimensionString(AxisVideoSize axisVideoSize) {
        return axisVideoSize.dimStr;
    }

    public static AxisVideoSize getAxisFormatFromDimension(Dimension dim) {
        return getAxisFormatFromDimension(createDimensionString(dim));
    }

    public static AxisVideoSize getAxisFormatFromDimension(String dim) {
        AxisVideoSize[] sizes = AxisVideoSize.values();
        for(int i=0;i<sizes.length;i++) {
            if(sizes[i].dimStr.equals(dim)) {
                return sizes[i];
            }
        }
        return null;
    }
}
