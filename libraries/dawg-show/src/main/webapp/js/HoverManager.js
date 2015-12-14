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
/**
 * Controls hover operations.
 */
var HoverManager = (function() {

    var hoverManager = {};
    /**
     * Bind the elements whose hover operations need to control. The visibility
     * of the second child of any of the bounded elements can be toggled on the
     * click operation of other elements.
     *
     * @param hoverButtonsDivs
     *            the array of div elements whose hover operations need to
     *            control
     */
    hoverManager.bind = function(hoverButtonsDivs) {
        $.each(hoverButtonsDivs, function(index, hoverButtonsDiv) {
            $(hoverButtonsDiv.selector).click(function() {
                $.each(hoverButtonsDivs, function(divIndex, hoverButtonDiv) {
                    /** Checks for div other than the current one */
                    if (index != divIndex) {
                        /**
                         * Checks other div is visible or not. If
                         * visible toggle the visibility
                         */
                        if ($(hoverButtonDiv.selector).children().eq(1).is(':visible')) {
                                $(hoverButtonDiv.selector).children().eq(1).toggle('slow');
                        }
                    }
                });
            });
        });
    };
    return hoverManager;
}());
