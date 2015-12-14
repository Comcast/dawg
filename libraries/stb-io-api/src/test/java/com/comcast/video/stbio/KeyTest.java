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
package com.comcast.video.stbio;


import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class KeyTest {

    @DataProvider(name="testKeysForChannelData")
    public Object[][] testKeysForChannelData() {
        return new Object[][] {
            {"9", new Key[] {Key.NINE}},
            {"20", new Key[] {Key.TWO, Key.ZERO}},
            {"123", new Key[] {Key.ONE, Key.TWO, Key.THREE}},
        };
    }

    @Test(dataProvider="testKeysForChannelData")
    public void testKeysForChannel(String channel, Key[] expected) {
        Key[] actual = Key.keysForChannel(channel);
        Assert.assertEquals(actual.length, expected.length);
        for (int k = 0; k < expected.length; k++) {
            Assert.assertEquals(actual[k], expected[k]);
        }
    }

    @DataProvider(name="testKeysForChannelInvalidData")
    public Object[][] testKeysForChannelInvalidData() {
        return new Object[][] {
            {"-1"},
            {"ABC"},
            {"9B"},
        };
    }

    @Test(dataProvider="testKeysForChannelInvalidData", expectedExceptions=IllegalArgumentException.class)
    public void testKeysForChannelInvalid(String channel) {
        Key.keysForChannel(channel);
    }
}
