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
 * The {@link DawgNotFoundException} exception is used to indicate that a particular page
 * could not be found.  This method is to be use by controllers whenever they wish to generate
 * an HTTP 404 response.
 *
 * @author Val Apgar
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class DawgNotFoundException extends RuntimeException {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public DawgNotFoundException() {
            // TODO Auto-generated constructor stub
        }

        public DawgNotFoundException(String message) {
            super(message);
            // TODO Auto-generated constructor stub
        }

        public DawgNotFoundException(Throwable cause) {
            super(cause);
            // TODO Auto-generated constructor stub
        }

        public DawgNotFoundException(String message, Throwable cause) {
            super(message, cause);
            // TODO Auto-generated constructor stub
        }

        public DawgNotFoundException(String message, Throwable cause,
                boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
            // TODO Auto-generated constructor stub
        }

    }
