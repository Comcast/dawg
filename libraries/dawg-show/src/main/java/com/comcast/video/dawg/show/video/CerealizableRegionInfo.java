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

import com.comcast.cereal.annotations.Cereal;

/**
 * Region info that correlates with the data that is created and sent to the javascript through
 * cereal
 * @author Kevin Pearson
 *
 */
public class CerealizableRegionInfo {
    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private int matchPercent;
    private int yTolerance;
    private int xTolerance;
    private int redTolerance;
    private int greenTolerance;
    private int blueTolerance;

    public CerealizableRegionInfo() {

    }

    public CerealizableRegionInfo(String name, int x, int y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Cereal(defaultValue="null")
    private String expectedText;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMatchPercent() {
        return matchPercent;
    }

    public void setMatchPercent(int matchPercent) {
        this.matchPercent = matchPercent;
    }

    public int getyTolerance() {
        return yTolerance;
    }

    public void setyTolerance(int yTolerance) {
        this.yTolerance = yTolerance;
    }

    public int getxTolerance() {
        return xTolerance;
    }

    public void setxTolerance(int xTolerance) {
        this.xTolerance = xTolerance;
    }

    public int getRedTolerance() {
        return redTolerance;
    }

    public void setRedTolerance(int redTolerance) {
        this.redTolerance = redTolerance;
    }

    public int getGreenTolerance() {
        return greenTolerance;
    }

    public void setGreenTolerance(int greenTolerance) {
        this.greenTolerance = greenTolerance;
    }

    public int getBlueTolerance() {
        return blueTolerance;
    }

    public void setBlueTolerance(int blueTolerance) {
        this.blueTolerance = blueTolerance;
    }

    public String getExpectedText() {
        return expectedText;
    }

    public void setExpectedText(String expectedText) {
        this.expectedText = expectedText;
    }
}
