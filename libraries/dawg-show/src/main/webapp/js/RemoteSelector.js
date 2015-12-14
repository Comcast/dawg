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
 * Manages the selection of different types of remotes
 *
 */
var RemoteSelector = (function() {
    var remoteSelector = {};

    remoteSelector.bind = function(changeRemotebtn, viewType) {
        /**
         * Click handler for change remote button
         */
        $(changeRemotebtn).click(function() {
            var url = null;
            if(viewType == 'multiView')  {
                var deviceStr = HTMLUtil.getParameterByName("deviceIds");
                url = CXT_PATH + '/multi?deviceIds=' + deviceStr + "&remoteType=";

            } else { //view type single view
                 var deviceStr = HTMLUtil.getParameterByName("deviceId");
                 url = CXT_PATH + '/stb?deviceId=' + deviceStr + "&remoteType=";
            }
            remoteSelector.loadPage(url);
        });
    };

    /**
     * Loads a new webpage with input url.
     * @param url The url to be loaded
     */
    remoteSelector.loadPage = function (url){
        $('.remoteTypeSelectorDiv').dialog({
            autoOpen: true,
            width: '30%',
            modal: true,
            buttons: {
                // Change button handler in overlay
                "Change": function() {
                    var remoteType = remoteSelector.getSelectedRemoteType();
                    url = url + remoteType.trim();
                    window.open(url, '_self');
                    $('.remoteTypeSelectorDiv').dialog("close");
                },
                // Cancel button handler in overlay
                Cancel: function() {
                    $('.remoteTypeSelectorDiv').dialog("close");
                }
            },
            // Default close button for the overlay
            close: function() {
                $('.remoteTypeSelectorDiv').dialog("close");
            }
        });
    };

    /** Adds all the remote types available in remote manager to drop down list
     *  and selects the value of 'remoteName' as selected item in the dropdown list.
     */

    remoteSelector.show = function(remoteTypes, remoteName) {
        remoteSelector.populate(remoteTypes, remoteName);
    };

    /**
     * Returns the selected remote type from the drop down list.
     */
    remoteSelector.getSelectedRemoteType = function() {
        return $('.remoteTypeTextBox').val();
    };

    /**
     * Displays all the remote types available in remote manager and
     * sets the selcted remote as value of 'remoteName'
     *
     * @param remotes list of remote types
     * @param remoteName remote type to be selected in the dropdown list.
     */
    remoteSelector.populate = function(remotes, remoteName) {
        $('.remoteTypeTextBox').val(remoteName);
        var select = $(".remoteTypeSelector");
        select.change( function(){
            $('.remoteTypeTextBox').val(select.val());
        });
        select.append($("<option/>").attr("value", "").text(""));
        for (var i = 0; i < remotes.length; i++) {
            /*
             * Need to list only remotes other than Generic remote for remote
             * change feature in dawg-show single view
             */
            if (remotes[i] != "GENERIC") {
                $('<option />', {
                    value: remotes[i],
                    text: remotes[i],
                    selected: (remotes[i].trim() == remoteName)
                }).appendTo(select);
            }
        }

        /** Converting the user input in 'Change remote type text box' to
         *  upper case since all the remote types available in remote manager
         *  are in upper case.
         */
        $('.remoteTypeTextBox').bind('input propertychange', function(){
            $(this).val($(this).val().toUpperCase());
        })
    };

    return remoteSelector;
}());
