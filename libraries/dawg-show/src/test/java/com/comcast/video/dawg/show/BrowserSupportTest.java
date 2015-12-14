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
package com.comcast.video.dawg.show;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.pantry.test.TestList;

public class BrowserSupportTest {
    public static final String CHROME_LINUX = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11";
    public static final String FF_LINUX = "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.23) Gecko/20110921 Ubuntu/10.10 (maverick) Firefox/3.6.23";
    public static final String IE_WINDOWS = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Win64; x64; Trident/4.0; .NET CLR 2.0.50727; SLCC2; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C)";
    public static final String FF_WINDOWS = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:14.0) Gecko/20100101 Firefox/14.0.1";
    public static final String FF_ANDROID = "Mozilla/5.0 (Android; Mobile; rv:20.0) Gecko/20.0 Firefox/20.0";
    public static final String SAFARI_ANDROID = "Mozilla/5.0 (Linux; U; Android 2.3.6; en-us; SAMSUNG-SGH-I777 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
    public static final String SAFARI_IPAD = "Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A403 Safari/8536.25";
    public static final String CHROME_IPAD = "Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) CriOS/26.0.1410.53 Mobile/10A403 Safari/8536.25";


    @DataProvider(name="testBrowserSupportData")
    public TestList testBrowserSupportData() {
        TestList list = new TestList();

        list.add(CHROME_LINUX, true);
        list.add(FF_LINUX, true);
        list.add(IE_WINDOWS, false);
        list.add(FF_WINDOWS, true);
        list.add(FF_ANDROID, true);
        list.add(SAFARI_ANDROID, false);
        list.add(SAFARI_IPAD, true);
        list.add(CHROME_IPAD, false);
        return list;
    }

    @Test(dataProvider="testBrowserSupportData")
    public void testBrowserSupport(String userAgentString, boolean supported) {
        Assert.assertEquals(BrowserSupport.isBrowserSupported(userAgentString), supported);
    }
}
