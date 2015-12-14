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
package com.comcast.video.dawg.filter.gzip;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.IOUtils;

/**
 * GZip servlet response wrapper.
 */
public class GZipServletResponseWrapper extends HttpServletResponseWrapper {

    /** Output stream for sending compressed data in GZIP format to the client */
    private GZipServletOutputStream gzipOutputStream = null;
    /** Prints formatted representation of object to out put stream. */
    private PrintWriter printWriter = null;

    public GZipServletResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
    }

    /**
     * Closes the stream and releases any system resources associated with it. Closing a previously closed stream has no
     * effect.
     */
    public void close() {

        // PrintWriter.close does not throw exceptions. Thus, the call does not need
        // be inside a try-catch block.
        if (this.printWriter != null) {
            this.printWriter.close();
        }

        IOUtils.closeQuietly(gzipOutputStream);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void flushBuffer() throws IOException {

        // PrintWriter.flush() does not throw exception
        if (this.printWriter != null) {
            this.printWriter.flush();
        }

        IOException exception1 = null;
        try {
            if (this.gzipOutputStream != null) {
                this.gzipOutputStream.flush();
            }
        } catch (IOException e) {
            exception1 = e;
        }

        IOException exception2 = null;
        try {
            super.flushBuffer();
        } catch (IOException e) {
            exception2 = e;
        }

        if (null != exception1) {
            throw exception1;
        }
        if (null != exception2) {
            throw exception2;
        }
    }

    /**
     *
     * {@inheritDoc}
     *
     * @throws when
     *             output steam not initialized.
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (null != this.printWriter) {
            throw new IllegalStateException("PrintWriter obtained already - cannot get OutputStream");
        }
        if (null == this.gzipOutputStream) {
            this.gzipOutputStream = new GZipServletOutputStream(getResponse().getOutputStream());
        }
        return this.gzipOutputStream;
    }

    /**
     *
     * {@inheritDoc}
     *
     * @throws IllegalStateException
     *             when steam object not initialized.
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        if (null == this.printWriter && null != this.gzipOutputStream) {
            throw new IllegalStateException("OutputStream obtained already - cannot get PrintWriter");
        }
        if (null == this.printWriter) {
            this.gzipOutputStream = new GZipServletOutputStream(getResponse().getOutputStream());
            this.printWriter = new PrintWriter(new OutputStreamWriter(this.gzipOutputStream, getResponse()
                        .getCharacterEncoding()));
        }
        return this.printWriter;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void setContentLength(int len) {
        // ignore, since content length of zipped content
        // does not match content length of unzipped content.
    }
}
