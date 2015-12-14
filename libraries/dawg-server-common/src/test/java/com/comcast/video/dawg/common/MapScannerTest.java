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
package com.comcast.video.dawg.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * The TestNG test class for MapScanner
 * @author TATA
 *
 */
public class MapScannerTest {
    @Test(expectedExceptions=NullPointerException.class)
    public void testScanWith() {
        MapScanner scanner = MapScanner.instance();
        Map<String, Object> map = null;
        scanner.scan(map);

    }
    @Test
    public void testScanString() {
        MapScanner scanner = MapScanner.instance();
        Map<String, Object> map = new HashMap<String, Object>();
        String value = "value";
        map.put("key", value);
        String[]  array = scanner.scan(map);
        Assert.assertNotNull(array);
        Assert.assertEquals(array[0],value);
    }
    @Test
    public void testScanMap() {
        MapScanner scanner = MapScanner.instance();
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> value = new HashMap<String, Object>();
        map.put("key", value);
        String[]  array = scanner.scan(map);
        Assert.assertNotNull(array);
        Assert.assertEquals(array[0], "key");
    }
    @Test
    public void testScanList() {
        MapScanner scanner = MapScanner.instance();
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> value = new ArrayList<String>();
        map.put("key1", value);
        String[]  array = scanner.scan(map);

        Assert.assertNotNull(array);
        Assert.assertEquals(array[0].toString(), "key1");

    }

}
