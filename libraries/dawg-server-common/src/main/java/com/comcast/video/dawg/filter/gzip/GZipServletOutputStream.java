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
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;

/**
 * Provides an output stream for sending compressed data in GZIP format to the client.
 */
public class GZipServletOutputStream extends ServletOutputStream {

    /** GZIP output stream. */
    private GZIPOutputStream gzipOutputStream = null;

    /**
     * Creates a GZip servlet output stream.
     *
     * @param outputStream
     *            the output stream.
     * @throws IOException
     *             if an I/O error has occurred.
     */
    public GZipServletOutputStream(OutputStream outputStream) throws IOException {
        super();
        this.gzipOutputStream = new GZIPOutputStream(outputStream);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        this.gzipOutputStream.close();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void flush() throws IOException {
        this.gzipOutputStream.flush();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void write(byte b[]) throws IOException {
        this.gzipOutputStream.write(b);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void write(byte b[], int off, int len) throws IOException {
        this.gzipOutputStream.write(b, off, len);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void write(int b) throws IOException {
        this.gzipOutputStream.write(b);
    }
}
