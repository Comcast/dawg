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
 * Manages the drop down menu and the functionality that can occur
 * when clicking menu elements
 */
var DropDownMenu = (function() {

    var module = {};

    /**
     * Takes a snapshot of the video and stores it in the image cache. Then opens up a window
     * to the comparison page with that image
     */
    module.snapVideo = function() {
        VideoSnapper.snapVideo(module.deviceId);
    };

    module.navigateToFileBug = function() {
        //window.open("FIXME-LINK");
        alert('There is no registered bug report page with the site.');
    };
    module.navigateToHelp = function() {
        //TODO: Point this to a document link on https://github.com/Comcast/dawg/
        window.open("https://github.com/Comcast/dawg");
    };

    /** Menu items with their text and their call back methods */
    var items = [
        {text : 'Snap Video', onclick : module.snapVideo},
        {text : 'Load Comparison', onclick : ComparisonPrompt.loadComparison},
        {text : 'File a bug', onclick : module.navigateToFileBug},
        {text : 'Help', onclick : module.navigateToHelp}
    ];

    /**
     * Binds the ui elements to their javascript functionality
     * @param uiDiv the menu div that is displayed when it is opened
     * @param menuOpener the ui element that when clicked, opens the menu
     * @param loadComparisonPrompt the ui element that is displayed when the user clicks the Load Comparison menu
     * @param fadeUI The white transparent background that is overlayed over the whole page when a prompt is up
     * @param deviceId the id the of the device that is open in dawg-show
     */
    module.bind = function(uiDiv, menuOpener, loadComparisonPrompt, fadeUI, deviceId) {
        module.loadComparisonPrompt = loadComparisonPrompt;
        module.fadeUI = fadeUI;
        module.deviceId = deviceId;

        ComparisonPrompt.bind(loadComparisonPrompt, fadeUI, deviceId);

        /**
         * Creates the table UI. And maps all the menu items
         * with their click handlers
         */
        var table = $('<table/>').addClass('menu').css('width', '100%');
        for (var i = 0; i < items.length; i++) {
            var cell = $('<td/>');
            cell.append(items[i].text);
            cell.click(items[i].onclick);
            table.append($('<tr/>').append(cell));
        }
        uiDiv.append(table);

        /**
         * Click handler for the down arrow in the component toolbar. This
         * will toggle the menu
         */
        menuOpener.click(function(e) {
            if (uiDiv.is(':hidden')) {
                /** Open up the menu right under the menuOpener element */
                var x = menuOpener.position().left;
                var y = menuOpener.position().top + menuOpener.height();
                uiDiv.css('left', x).css('top', y);
                uiDiv.show();
            } else {
                uiDiv.hide();
            }
        });

        /**
         * If anywhere but the menu is clicked then the menu is hidden
         */
        $('html').click(function(e) {
            if(!$(e.target).hasClass('solid')) {
                uiDiv.hide();
                menuOpener.addClass('solid');
            }
        });
    };

    return module;
}());
