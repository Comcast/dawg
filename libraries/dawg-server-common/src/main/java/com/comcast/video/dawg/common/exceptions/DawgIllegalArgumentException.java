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
package com.comcast.video.dawg.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A {@link DawgIllegalArgumentException} is simply an
 * {@link IllegalArgumentException} associated with dawg. This exception is
 * designed for controller methods that want to return HTTP 400 responses when
 * the parameters passed to them couldn't be handled.
 *
 * @author Val Apgar
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DawgIllegalArgumentException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public DawgIllegalArgumentException() {

    }

    public DawgIllegalArgumentException(String s) {
        super(s);
    }

    public DawgIllegalArgumentException(Throwable cause) {
        super(cause);
    }

    public DawgIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

}
