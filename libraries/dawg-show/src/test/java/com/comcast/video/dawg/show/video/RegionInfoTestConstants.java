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

/**
 * Some constants we can use in multiple tests
 * @author Kevin Pearson
 *
 */
public interface RegionInfoTestConstants {

    public static final String NAME = "regionName";
    public static final String EXPECTED_TEXT = "Hello!";
    public static final int X = 50;
    public static final int Y = 60;
    public static final int WIDTH = 150;
    public static final int HEIGHT = 300;
    public static final int MATCH_PERCENT = 85;
    public static final int XTOLERANCE = 5;
    public static final int YTOLERANCE = 10;
    public static final int RED_TOLERANCE = 255;
    public static final int GREEN_TOLERANCE = 125;
    public static final int BLUE_TOLERANCE = 100;
}
