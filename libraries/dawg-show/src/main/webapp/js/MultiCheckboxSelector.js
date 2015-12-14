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
 * Handles 'select all' checkbox option.
 */

var MultiCheckboxSelector = (function() {

    var multiCheckboxSelector = {};

    multiCheckboxSelector.bind = function(multiSelectCheckbox, toBeSelectedCheckboxes) {
        multiCheckboxSelector.multiSelectCheckbox = multiSelectCheckbox;
        multiCheckboxSelector.toBeSelectedCheckboxes = toBeSelectedCheckboxes;

        /** Displays the title of the checkbox during mouse hover over it */
        multiSelectCheckbox.tooltip({
            show: {
                effect: "slideDown",
                delay: 1000
            },
            hide: {
                effect: "puff",
            }
        });

        /**
        * By default the 'select all' check box will be enabled while Dawg show loads.
        * This click function checks the state change of individual device's checkbox's
        * and changes the state of the 'select all' check box accordingly.
        */
        toBeSelectedCheckboxes.click( function () {
            if (this.checked) {
                var allDevicesChecked = true;
                toBeSelectedCheckboxes.each ( function () {
                    allDevicesChecked = $(this).prop('checked') ? allDevicesChecked : false;
                    if ( !allDevicesChecked ) {
                        return false;
                    }
                });
                multiSelectCheckbox.prop('checked',allDevicesChecked);
            } else {
                multiSelectCheckbox.prop('checked', false);
            }
        });

        multiSelectCheckbox.click(function() {
            multiCheckboxSelector.changeState();
        });
    };

    /**
     * Handler for selecting and deselecting all the devices.
     * By default all devices will be selected while the page loads.
     */
    multiCheckboxSelector.changeState = function() {
        var multiSelectCheckboxStatus = multiCheckboxSelector.multiSelectCheckbox.prop('checked');
        multiCheckboxSelector.toBeSelectedCheckboxes.each ( function() {
            $(this).prop('checked', multiSelectCheckboxStatus);
        });
    };
    return multiCheckboxSelector;
}());
