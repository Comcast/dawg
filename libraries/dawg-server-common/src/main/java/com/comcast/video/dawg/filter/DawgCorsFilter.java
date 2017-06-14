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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an extremely simple CORS filter that allows requests to come from any domain.
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS#Overview">https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS#Overview</a>
 *
 * @author Val Apgar
 *
 */
public class DawgCorsFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DawgCorsFilter.class);
    private Set<String> domainWhitelist;
    
    public DawgCorsFilter() {
        this(null);
    }
    
    public DawgCorsFilter(Set<String> domainWhitelist) {
        this.domainWhitelist = domainWhitelist;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("Applying CORS headers to http response");
        HttpServletResponse response = (HttpServletResponse) res;
        String allowOrigin = null;
        if (this.domainWhitelist != null) {
            HttpServletRequest request = (HttpServletRequest) req;
            String origin = request.getHeader("Origin");
            if (!StringUtils.isEmpty(origin)) {
                try {
                    String originHost = new URI(origin).getHost(); 
                    if (originHost != null) {
                        for (String domain : domainWhitelist) {
                            if (originHost.endsWith(domain)) {
                                allowOrigin = origin;
                                break;
                            }
                        }
                    } else {
                        LOGGER.warn("Invalid host for origin '" + origin + "'");
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        } else {
            allowOrigin = "*";
        }
        if (allowOrigin != null) {
            response.setHeader("Access-Control-Allow-Origin", allowOrigin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods","PUT, POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, Accept");
        }
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
        // NOOP
    }

    public void destroy() {
        // NOOP
    }
}
