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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.comcast.video.dawg.filter.gzip.GZipServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter provides a mechanism to compress the response body content. This type of filter would common be applied
 * to any text content, such as HTML, js, css, but not to most media formats, such as PNG or MPEG, because these are
 * already compressed.
 *
 * @author Pratheesh TK
 */
public class DawgCompressionFilter implements Filter {

    /** Logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgCompressionFilter.class);

    /** Browser accepted encoding header value for compression. */
    private static final String ACCEPT_ENCODING_REQUEST_HEADER_VALUE = "gzip";

    /** Accept encoding header name. */
    private static final String ACCEPT_ENCODING_REQUEST_HEADER_NAME = "Accept-Encoding";

    /** Content encoding header value for compressed response. */
    private static final String CONTENT_ENCODING_RESPONSE_HEADER_VALUE = ACCEPT_ENCODING_REQUEST_HEADER_VALUE;

    /** Content encoding header name. */
    private static final String CONTENT_ENCODING_RESPONSE_HEADER_NAME = "Content-Encoding";

    /** Servlet include request URI attribute name. */
    private static final String REQUEST_URI_ATTRIBUTE_NAME = "javax.servlet.include.request_uri";

    /** Compressed font format. */
    private static final String COMPRESSED_FONT_FORMAT = ".woff";

    /** Default image path. */
    private static final String IMAGES_URI_PATH = "/images";

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!httpResponse.isCommitted() && !skipCompression(httpRequest) && !isIncluded(httpRequest)
                && supportsCompression(httpRequest)) {

            LOGGER.debug(httpRequest.getRequestURL() + " written with compression.");
            httpResponse.addHeader(CONTENT_ENCODING_RESPONSE_HEADER_NAME, CONTENT_ENCODING_RESPONSE_HEADER_VALUE);
            GZipServletResponseWrapper gzipResponse = new GZipServletResponseWrapper(httpResponse);
            chain.doFilter(request, gzipResponse);
            gzipResponse.close();
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
    }

    /**
     * Checks if the request uri is an include. If the uri is an include it cannot be compressed.
     *
     * @param request
     *            http servlet request.
     * @return true if request uri is an include, false otherwise.
     */
    private boolean isIncluded(HttpServletRequest request) {
        String uri = (String) request.getAttribute(REQUEST_URI_ATTRIBUTE_NAME);
        boolean includeRequest = null != uri;
        if (includeRequest) {
            LOGGER.debug(request.getRequestURL() + " resulted in an include request. This is unusable, because "
                    + "the response will be assembled into the overall response. Not compressing.");
        }
        return includeRequest;
    }

    /**
     * Validates whether the response to the request be skipped from compression. Images and WOFF font format are
     * already compressed, so it need to be skipped from compressing.
     *
     * @param request
     *            http servlet request.
     * @return true if the compression to be skipped, false otherwise.
     */
    private boolean skipCompression(HttpServletRequest request) {
        boolean skip = false;
        String requestUri = request.getRequestURI();
        if (requestUri.contains(IMAGES_URI_PATH) || requestUri.contains(COMPRESSED_FONT_FORMAT)) {
            skip = true;
            LOGGER.debug(request.getRequestURL() + " skipped from compressing.");
        }
        return skip;
    }

    /**
     * Verify the request from browser whether it accept compression. true is returned if the browser support
     * compression.
     *
     * @param httpServletRequest
     *            http servlet request.
     * @return true if compression is supported, false otherwise.
     */
    private boolean supportsCompression(HttpServletRequest httpServletRequest) {
        String acceptEncoding = httpServletRequest.getHeader(ACCEPT_ENCODING_REQUEST_HEADER_NAME);
        return acceptEncoding != null && acceptEncoding.contains(ACCEPT_ENCODING_REQUEST_HEADER_VALUE);
    }
}
