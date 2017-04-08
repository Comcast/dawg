/* 
 * ===========================================================================
 * This file is the intellectual property of Comcast Corp.  It
 * may not be copied in whole or in part without the express written
 * permission of Comcast or its designees.
 * ===========================================================================
 * Copyright (c) 2017 Comcast Corp. All rights reserved.
 * ===========================================================================
 */

package com.comcast.dawg.saucelab;

/**
 * Exception specific to running xDawg selenium tests to run in sauce labs
 *
 * @author  Priyanka
 */

public class SauceTestException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     * @param string
     * @param t
     */
    public SauceTestException(String string, Throwable t) {
        super(string, t);
    }

    /**
     * Constructs a new exception with the specified detail message.
     * @param string
     */
    public SauceTestException(String string) {
        super(string);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of cause
     * @param throwable
     */
    public SauceTestException(Throwable throwable) {
        super(throwable);
    }
}
