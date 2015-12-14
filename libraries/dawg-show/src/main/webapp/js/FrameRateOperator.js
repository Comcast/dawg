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
 * Controls fps related operations
 */
var FrameRateOperator = (function() {

    var fpsOperator = {
            FPS_DEFAULT_STRING : "Default"
    };

    /**
     * Bind the fps button, fps div and frame rate drop down menu.
     *
     * @param fpsButton
     *            the element to represent fps button
     * @param fpsDiv
     *            the div element that show the current fps value
     * @param fpsSelector
     *            the drop down element to show the fps values
     */
    fpsOperator.bind = function(fpsButton, fpsDiv, fpsSelector) {
        // Set "Default" as the default value for fps drop down menu
        fpsOperator.addElementToDropDown(fpsSelector, fpsOperator.FPS_DEFAULT_STRING, true);
        // Adding values 1 to 30 on the fps drop down menu
        for (var fpsValue = 1; fpsValue <= 30; fpsValue++) {
            fpsOperator.addElementToDropDown(fpsSelector, fpsValue, false);
        }

        /**
         * Handler for clicking the fps button. This will toggle the visibility
         * of fps drop down div.
         */
        fpsButton.click(function() {
            fpsDiv.toggle('slow');
        });

        /**
         * Handler for selecting a value in fps drop down menu. This will change
         * the frame rate of all devices
         */
        fpsSelector.change(function() {
            fpsOperator.changeBoxFrameRate(fpsSelector.val());
        });
    };

    /**
     * Handler for changing the frame rate of all devices.
     *
     * @param frameRate
     *            the frame rate value which user selects in the fps drop down
     *            menu
     */
    fpsOperator.changeBoxFrameRate = function(frameRate) {

        // Represents the video div element
        var videoDivElement = null;
        // Represents the video source URL of devices
        var source = null;

        $('.videoDiv').each(function() {
            videoDivElement = $(this);
            videoElement = $('img[class=video]', videoDivElement);
            source = videoElement.attr('src');
            // Check whether user selects fps value as "Default" or not
            if (fpsOperator.FPS_DEFAULT_STRING != frameRate) {
                //Adding a parameter timeStamp to URL, to force the browser to reload the image
                source = HTMLUtil.removeParameterByNameFromUrl(source, ["fps", "timeStamp"]) + "&fps=" + frameRate
                            + "&timeStamp=" + new Date().getTime();
            } else {
                source = HTMLUtil.removeParameterByNameFromUrl(source, ["fps", "timeStamp"]);
            }
            videoElement.attr('src', source);
        });
        updateEventLog("Successfully changed the frame rate to "+ frameRate +"fps" , null);
    };

    /**
     * Method to add value to drop down menu in fps button
     *
     * @param dropDownElement
     *            the drop down element to show the fps values
     * @param value
     *            the value we need to add to drop down menu
     * @param isSelected
     *            it will be true, if the value need to mark as selected in drop
     *            down menu, otherwise false.
     */
    fpsOperator.addElementToDropDown = function(dropDownElement, value, isSelected) {
        $('<option />', {
            value : value,
            text : value,
        }).prop("selected", isSelected).appendTo(dropDownElement);
    };

    return fpsOperator;
}());
