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
package com.comcast.video.dawg.filter;

import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DawgCorsFilterTest {

    @Mock
    ServletRequest req;

    @Mock
    HttpServletResponse res;

    @Mock
    FilterChain chain;

    @BeforeMethod
    public void initTheMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void doFilter() throws IOException, ServletException {

        DawgCorsFilter filter = new DawgCorsFilter();

        filter.doFilter(req, res, chain);

        verify(res).setHeader("Access-Control-Allow-Origin", "*");
        verify(res).setHeader("Access-Control-Allow-Methods","PUT, POST, GET, OPTIONS, DELETE");
        verify(res).setHeader("Access-Control-Max-Age", "3600");
        verify(res).setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, Accept");
        verify(chain).doFilter(req, res);

    }
}
