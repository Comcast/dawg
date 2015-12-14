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
 * Singleton that manages Edit Device overlay
 */
var EditDevice = (function() {

    var ed = {
        deviceId : null,
        existingKeys : new Array(),
        originalData : null
    };

    /**
     * Binds this object to the html elements
     * @param overlayUI the div that holds the overlay
     */
    ed.bind = function(overlayUI) {
        ed.overlayUI = overlayUI;

        ed.overlayUI.dialog({
            autoOpen : false,
            minWidth : 800,
            width : 800,
            minHeight : 500,
            height : 500,
            resize : ed.sizeOverlay
        });

        /**
         * Click handler for the Save button
         */
        $('.btnSave', ed.overlayUI).click(function() {
            var updatedVals = {};
            var modifiedProps = new Array();

            /**
             * Gets all the updated values, if the textarea has multiple rows
             * that means it is an array type
             */
            $('.editRow', ed.overlayUI).each(function() {
                var inp = $('.editValueInput', $(this));
                var val;
                if (inp.attr('rows') == "1") {
                    val = inp.val();
                } else {
                    val = new Array();
                    var values = inp.val().split(',');
                    for (var v in values) {
                        var trimmed = $.trim(values[v]);
                        if (trimmed != "") {
                            val.push(trimmed);
                        }
                    }
                }
                var key = $(this).attr('data-key');
                var modIcon = $('.modIcon', $(this));
                if (modIcon.hasClass('modifiedIcon')) {
                    modifiedProps.push(key);
                }
                updatedVals[key] = val;
            });
            updatedVals['modifiedProps'] = modifiedProps;

            /**
             * Sends an update request to the server
             */
            $.ajax({
                url: CXT_PATH + '/devices/update?replace=true&id='  + ed.deviceId,
                type: "POST",
                data: JSON.stringify(updatedVals),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
            }).done(function(){
                document.location.href = document.location.href;
            });
        });

        /**
         * Click handler for the add property (+) button.
         * This will check for existing keys, if the key does not exist then it will add the property
         * to the table.
         */
        $('.btnAddProp', ed.overlayUI).click(function() {
            var keyInp = $('.newPropKey', ed.overlayUI);
            var setCb = $('.cbNewPropSet', ed.overlayUI);
            var key = $.trim(keyInp.val());
            if (key != '') {
                if ($.inArray(key, ed.existingKeys) > -1) {
                    alert("'" + key + "' already exists as a property.");
                } else {
                    var set = setCb.prop('checked');
                    EditDevice.addProperty(key, {value: (set ? new Array() : ""), modified: true});
                    var scrollable = $('.scrollableArea', ed.overlayUI);
                    scrollable.animate({ scrollTop: scrollable[0].scrollHeight}, 700);
                }
                keyInp.val('');
                setCb.removeAttr('checked');
            }
        });

        /**
         * Routes pushing enter when focused on the new prop text input to the click method of
         * the add button
         */
        $('.newPropKey', ed.overlayUI).keydown(function(event) {
            if (event.which == 13) {
                $('.btnAddProp', ed.overlayUI).click();
            }
        });
    }

    /**
     * Opens the overlay for the given deviceId
     * @param deviceId the id of the device to load the overlay for
     */
    ed.open = function(deviceId) {
        ed.deviceId = deviceId;
        /**
         * Gets the edit properties from the server and then populates the overlay
         */
        $.get(CXT_PATH + '/devices/edit/' + deviceId, null, function(data) {
            $('.scrollableArea', ed.overlayUI).html('');
            var table = $('<table>').addClass('editTable');
            $('.scrollableArea', ed.overlayUI).append(table);
            for (var key in data) {
                EditDevice.addProperty(key, data[key]);
            }
            ed.originalData = data;
            ed.overlayUI.dialog("open");
            EditDevice.sizeOverlay();
            ed.overlayUI.show();
        });
    }

    /**
     * Adds a property to the property table
     * @param key The key of the property
     * @val The value of the property
     */
    ed.addProperty = function(key, valAndMod) {
        var table = $('.editTable', ed.overlayUI);
        var keyDisp = EditDevice.formatKey(key);
        var propDisp = $('<td>').addClass('editKeyCell').append(keyDisp);
        var val = valAndMod.value;
        var disp = val;
        var rows = 1;
        if (val instanceof Array) {
            disp = val.join(', ');
            rows = 5;
        }
        var remoteTypesDropDown;
        var txt = $('<textarea>').attr('name', key).attr('rows', rows).attr('spellcheck', false).addClass('editValueInput').val(disp);
        if ("remoteType" == key) {
            remoteTypesDropDown = $("<select/>").attr('name', 'remoteTypesDropDown').attr(
                    'id', 'selectRemoteTypeDropDown').attr('rows', rows).change(function() {
                $('#editRemoteType').val(remoteTypesDropDown.val());
            });
            $.get(CXT_PATH+'/devices/remotetypes', null, function(allValidRemoteTypes){
                remoteTypesDropDown.append($("<option/>").attr("value", "").text(""));
                $.each(allValidRemoteTypes, function(index, value) {
                    remoteTypesDropDown.append($("<option/>").attr("value", value).text(value));
                });
            });
            txt.attr('id', 'editRemoteType');

            /*  Converting the user input in 'Change remote type text box' to
             *  upper case since all the remote types available in remote manager
             *  are in upper case.
             */
            txt.bind('input propertychange', function() {
                $(this).val($(this).val().toUpperCase());
            })
        }

        /** Called when any change happens to the text area (like when the user types
         * This will then check if the text is different than the original value.
         * Then it will update the modified icon
         */
        txt.bind('input propertychange', function() {
            var key = $(this).attr('name');
            var origVal = ed.originalData[key];
            var isMod = (origVal == null) || origVal.modified || ($(this).val() != origVal.value);
            var modIcon = $('.modIcon', $(this).parents('.editRow'));
            modIcon.removeClass(isMod ? 'unmodifiedIcon' : 'modifiedIcon');
            modIcon.addClass(isMod ? 'modifiedIcon' : 'unmodifiedIcon');
        });
        var propEdit = $('<td>').append(txt).addClass('editValueCell');
        if ("remoteType" == key) {
            propEdit.prepend(remoteTypesDropDown);
        }
        var iconClass = valAndMod.modified ? 'modifiedIcon' : 'unmodifiedIcon';
        var iconHoverMsg = 'Modified property: if this is highlighted then it will override' +
                            ' any values that get loaded from CHIMPS.';
        var modIcon = $('<div>').addClass(iconClass).addClass('modIcon').attr('title', iconHoverMsg);

        /**
         * Click handler for the modified icon, this will toggle if the icon is selected or not
         */
        modIcon.click(function() {
            $(this).toggleClass('modifiedIcon unmodifiedIcon');
        });
        var delBtn = $('<input>').attr('type', 'button').addClass('btnDelProp').val('-');

        /**
         * Click handler for the delete button (-). This will remove the property from the
         * table if the user confirms
         */
        delBtn.click(function() {
            var row = $(this).parents('.editRow');
            var key = row.attr('data-key');
            if (confirm("Are you sure you want to remove property '" + key + "'")) {
                row.remove();
            }
        });
        var modCell = $('<td>').append(modIcon).addClass('modifiedCell');
        var delCell = $('<td>').append(delBtn).addClass('delCell');
        var row = $('<tr>').append(propDisp).append(propEdit).append(modCell).append(delCell).addClass('editRow');
        row.attr('data-key', key);
        table.append(row);
        ed.existingKeys.push(key);
    }

    /**
     * Helper method that turns database keys into a cleaner display.
     * eg. irServiceUrl will become Ir Service Url
     */
    ed.formatKey = function(key) {
        if (key == null) {
            return null;
        }
        if (key.length == 0) {
            return key;
        }
        var chars = key.split('');
        var str = "" + chars[0].toUpperCase();
        for (var c = 1; c < chars.length; c++) {
            var upper = chars[c] == chars[c].toUpperCase();
            str += (upper ? " " : "") + chars[c];
        }
        return str;
    }

    /**
     * Resizes the scrollable area to take up all the room in the overlay besides the save button
     */
    ed.sizeOverlay = function() {
        var btnHeight = $('.btnSave', ed.overlayUI).height();
        $('.scrollableArea', ed.overlayUI).css('height', ed.overlayUI.height() - btnHeight - 90);
    }

    return ed;
}());
