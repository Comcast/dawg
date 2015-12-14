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
package com.comcast.video.dawg.exception;


import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * The TestNG test class for HttpRuntimeException
 * @author TATA
 *
 */
public class HttpRuntimeExceptionTest {
    @Test
    public void testException() {
        HttpRuntimeException exp = new HttpRuntimeException();
        Assert.assertNotNull(exp);
    }

    @Test
    public void testExceptionWithString() {
        String msg = "error message";
        HttpRuntimeException exp =new HttpRuntimeException(msg );
        Assert.assertEquals(exp.getMessage(), msg);
    }

    @Test
    public void testExceptionWithThrowable() {
        Throwable t = EasyMock.createMock(Throwable.class);
        HttpRuntimeException exp = new HttpRuntimeException(t);
        Assert.assertTrue(exp.toString().contains(t.toString()));
    }

    @Test
    public void testExceptionTwoParams() {
        String msg = "error message";
        Throwable t = EasyMock.createMock(Throwable.class);
        HttpRuntimeException exp = new HttpRuntimeException(msg,t);
        Assert.assertEquals(exp.getMessage(), msg);
    }

    @Test
    public void testExceptionFourParams() {
        String msg = "error message";
        Throwable t = EasyMock.createMock(Throwable.class);
        HttpRuntimeException exp = new HttpRuntimeException(msg,t,true,true);
        Assert.assertEquals(exp.getMessage(), msg);
        exp = new HttpRuntimeException(msg,t,false,false);
        Assert.assertEquals(exp.getMessage(), msg);
    }
}
