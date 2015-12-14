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

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import com.comcast.video.dawg.common.exceptions.DawgIllegalArgumentException;

/**
 * The TestNG test class for ServerUtils
 *
 * @author TATA
 *
 */
public class ServerUtilsTest {
    private ServerUtils utils = new ServerUtils();
    private String str = " ABCDefgh1234 !@#$%^&*(){}<>?";
    private String expected = "abcdefgh1234";

    @Test
    public void testToLowerAlphaNumeric() {
        Assert.assertEquals(utils.toLowerAlphaNumeric(str), expected);
    }

    @Test
    public void testClean() {
        Assert.assertEquals(utils.clean(str), expected);
    }

    @Test(expectedExceptions = DawgIllegalArgumentException.class)
        public void testCleanWithNull() {
            String str = null;
            utils.clean(str);
        }

    @DataProvider(name = "testIsValidData")
    public Object[][] testIsValidData() {
        return new Object[][] { { "username", true }, { "username123", true },
            { "UserName123", true }, { "1234567", true },
            { "user@123", false }, { "User$%^&", false },
            { "$%^&$#)_+", false }, { "", false }, { null, false } };
    }

    @Test(dataProvider = "testIsValidData")
    public void testIsValid(String string, boolean expected) {
        Assert.assertEquals(utils.isValid(string), expected);
    }

}
