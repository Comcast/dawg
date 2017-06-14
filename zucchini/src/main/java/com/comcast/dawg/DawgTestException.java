/**
 * Copyright 2017 Comcast Cable Communications Management, LLC
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
package com.comcast.dawg;

/**
 * Defines custom exception for Dawg House Tests
 * 
 * @author priyanka
 */
public class DawgTestException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message
     * 
     * @param msg
     *            the detailed message to be displayed while the exception is
     *            thrown
     */
    public DawgTestException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * exception message.
     * 
     * @param msg
     *            the detailed message to be displayed while the exception is
     *            thrown
     * @param e
     *            the exception to be thrown
     */
    public DawgTestException(String msg, Exception e) {
        super(msg, e);
    }

    /**
     * Constructs a new exception with the specified detail message and cause
     * 
     * @param msg
     *            the detailed message to be displayed while the exception is
     *            thrown
     * @param t
     *            the cause of the exception
     */
    public DawgTestException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of cause
     * 
     * @param throwable
     *            the cause of the exception
     */
    public DawgTestException(Throwable throwable) {
        super(throwable);
    }
}
