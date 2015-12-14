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
 * Singleton that manages the context menu (menu that appears when right clicking a row)
 */
var ContextMenu = (function() {

    var cm = {
            /** The device row that the context menu was launched from */
            rowContext : null
    };

    /**
     * Binds this object to the html elements
     * @param tableUI the div that holds the table
     * @param menuUI the div that holds the menu
     * @param token the token of the user that opened this context menu
     */
    cm.bind = function(tableUI, menuUI, token) {
        cm.menuUI = menuUI;
        cm.token = token;

        menuUI.menu();

        /**
         * Intercepts the contextmenu command and shows the menu
         */
        $(".collapsableRow", tableUI).on("contextmenu", function (event) {
            cm.rowContext = $(event.currentTarget);
            menuUI.css('left', event.clientX);
            menuUI.css('top', event.clientY);

            menuUI.show();
            return false;
        });

        $('.cxtDawgShow', menuUI).click(cm.cxtDawgShow);
        $('.cxtEdit', menuUI).click(cm.cxtEdit);

        /**
         * If the user clicks anywhere, the context menu is hidden
         */
        $('html').click(function() {
            menuUI.hide();
        });
    }

    /**
     * Launches dawg show from the context menu
     */
    cm.cxtDawgShow = function() {
        var name = $('.stbName', cm.rowContext).html();
        var deviceId = cm.rowContext.attr('data-deviceid');
        DAWGHOUSE.openDawgShow(name, deviceId, cm.token);
    }

    /**
     * Launches the edit overlay for the device that this context menu was launched for
     */
    cm.cxtEdit = function() {
        var deviceId = cm.rowContext.attr('data-deviceid');
        EditDevice.open(deviceId);
    }

    return cm;
}());
