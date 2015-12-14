/*
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
/** Validates the input before sending to controller */
var ValidateInput = (function() {

    var input = {};
    /** Validates the entry in text box and calls the function given as activateFunction,
     *  when  enter key is pressed */
    input.forceNumeric = function(textBox, activateFunction) {

        textBox.numeric({
            // Disable the - sign
            allowMinus : false,
            // Disable the thousands separator, default is the comma eg 12,000
            allowThouSep : false,
            // Disable the decimal separator, default is the fullstop eg 3.141
            allowDecSep : false
        });

        textBox.keydown(function(e) {
            var key = e.which || e.keyCode;
            if (key == 13) {
                activateFunction();
            }
        });
    };
    return input;
}());
