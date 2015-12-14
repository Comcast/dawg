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
 * Singleton that manages interaction with the table
 * of devices
 */
var DeviceTable = (function() {

    var dt = {};

    /**
     * Binds this object to the html elements
     * @param tableUI the div that holds the table
     */
    dt.bind = function(tableUI, user) {
        dt.tableUI = tableUI;
        dt.user = user;

        /**
         * Updates the tags in the tag cloud with the devices that are currently selected
         */
        $('.bulk-checkbox', tableUI).change(function() {
            TagCloud.selectSources(DeviceTable.getSelectedDevices());
        });

        /**
         * Toggles all the checkboxes when clicking the master checkbox at the top of the table
         */
        $('.toggle-all-checkbox', tableUI).change(function() {
            var checkAll = this.checked;
            $('.bulk-checkbox', $('.filteredTable', tableUI)).each(function() {
                this.checked = checkAll;
            });
            TagCloud.selectSources(DeviceTable.getSelectedDevices());
        });

        /** Handle all empty values */
        $('.metadata', tableUI).each(function() {
            $(this).find('span.mdDetail > span.mdv').each(function() {
                var mdvSpan = $(this);
                var val = $.trim(mdvSpan.html());
                if ((val === '') || (val === 'null') || (val === '[]')) {
                    mdvSpan.html('&lt;none&gt;');
                    mdvSpan.addClass('emptyMetadata');
                }
            });
        });

        dt.rowHeight = 29; //default

        /** Create a map of deviceId to the row for easy look up */
        dt.deviceIdToRows = new Array();
        $('.collapsableRow', tableUI).each(function() {
            var did = $(this).attr('data-deviceId');
            dt.deviceIdToRows[did] = $(this);
            dt.rowHeight = $(this).height() != 0 ? $(this).height() : dt.rowHeight;
        });

        /** Click handler for launching multi-show */
        $('.stbDawgShow', tableUI).click(function() {
            var deviceIds = DeviceTable.getSelectedDevices();
            if (deviceIds.length != 0) {
                var lockedByOther = '';
                var devicesStr = '';
                for (var d = 0; d < deviceIds.length; d++) {
                    var row = $(".collapsableRow[data-deviceId='" + deviceIds[d] + "']", tableUI);
                    var reserver = $('.stbReserve', row).attr('data-token');
                    var locked = $('.stbReserve', row).attr('data-locked') == 'true';
                    if (locked && (reserver != user)) {
                        var name = $('.stbName', row).html();
                        lockedByOther += (lockedByOther != '' ? ', ' : '') + name;
                    }
                    devicesStr += (devicesStr != '' ? ',' : '') + deviceIds[d];
                }
                if ((lockedByOther == '') || confirm(lockedByOther + " are already reserved. Launch anyway?") ) {
                    window.open(DAWG_SHOW + 'multi?deviceIds=' + devicesStr);
                }
            }

        });

        $('.btnMine', tableUI).click(function() {
            DeviceTable.toggleButtons($(this), $('.btnUnreserved', tableUI));
        });

        $('.btnUnreserved', tableUI).click(function() {
            DeviceTable.toggleButtons($(this), $('.btnMine', tableUI));
        });

        DeviceTable.showDevicesOfSelectedTags();
    }

    /**
     * Toggles the state of the buttons, then refreshes the devices that are shown in the table
     * @param clicked the button that was clicked. If this button is already selected then it will be unselected/
     * @param other the button that should be unselected when the clicked button is selected.
     */
    dt.toggleButtons = function(clicked, other) {
        if (clicked.hasClass('btnSelected')) {
            clicked.removeClass('btnSelected');
        } else {
            clicked.addClass('btnSelected');
            other.removeClass('btnSelected');
        }
        DeviceTable.showDevicesOfSelectedTags();
    }

    /**
     * Gets a list of deviceIds that are not currently reserved
     */
    dt.getUnreservedDevices = function() {
        var unlockedDevices = new Array();
        $("td[data-locked='false']", dt.tableUI).each(function() {
            unlockedDevices.push($(this).attr('data-deviceId'));
        });
        return unlockedDevices;
    }

    /**
     * Gets the list of deviceIds that are reserved by the given use
     * @param user the user to get the list of reserved devices for
     */
    dt.getReservedDevicesForUser = function(user) {
        var myDevices = new Array();
        $("td[data-locked='true']", dt.tableUI).each(function() {
            var reserver = $(this).attr('data-token');
            if (reserver == user) {
                myDevices.push($(this).attr('data-deviceId'));
            }
        });
        return myDevices;
    }

    /**
     * Gets the list of deviceIds that are currently selected with the checkbox
     */
    dt.getSelectedDevices = function() {
        var devices = new Array();
        $('.bulk-checkbox:checked', dt.tableUI).each(function() {
            var deviceId = $(this).attr('data-deviceId');
            devices.push(deviceId);
        });
        return devices;
    }

    /**
     * Adds or removes tags from the device metadata
     * @param tags The tags to add or remove
     * @param devices The devices to add or remove the tags from
     * @param add true if adding tags, false if removing tags
     */
    dt.addOrRemoveTags = function(tags, devices, add) {
        for (var s = 0; s < devices.length; s++) {
            /** Gets the stb row for the stb with the given deviceId */
            var stbRow = $(".collapsableRow[data-deviceId='" + devices[s] + "']");
            var tagHtml = $('.stbTags', stbRow).html();
            if (tagHtml == '&lt;none&gt;') {
                curTags = [];
            } else {
                var curTagVal = tagHtml.substring(1, tagHtml.length - 1); //remove []
                curTags = curTagVal.replace(', ', ',').split(',');
            }

            for (var t = 0; t < tags.length; t++) {
                var i = curTags.indexOf(tags[t]);
                if (add && (i == -1)) {
                    /** If we are adding a tag and the tag doesn't exist */
                    curTags.push(tags[t]);
                } else if (!add && (i != -1)) {
                    /** If we are removing a tag and the tag exists */
                    curTags.splice(i, 1);
                }
            }
            /** Update the tags metadata section with the new tags */
            if (curTags.length == 0) {
                $('.stbTags', stbRow).html('&lt;none&gt;').addClass('emptyMetadata');
            } else {
                $('.stbTags', stbRow).html('[' + curTags + ']').removeClass('emptyMetadata');
            }
        }
    }

    /**
     * Change the UI of the device table to only show the devices
     * that have at least one of the tags that are currently selected in the TagCloud
     */
    dt.showDevicesOfSelectedTags = function() {
        var selectedTags = TagCloud.getSelectedTags();
        var showMyDevices = $('.btnMine', dt.tableUI).hasClass('btnSelected');
        var showUnreserved =  $('.btnUnreserved', dt.tableUI).hasClass('btnSelected');

        var sourceToTags = TagCloud.getSourceToTags();
        var filteredTable = $('.filteredTable', dt.tableUI);
        var hiddenTable = $('.stbTable', dt.tableUI);
        var numToAnimate = filteredTable.height() / DeviceTable.getRowHeight();
        var devicesToShow = new Array();
        var devicesToShowAndAnimate = new Array();
        var devicesToHide = new Array();
        var devicesToHideAndAnimate = new Array();

        var myDevices = showMyDevices ? DeviceTable.getReservedDevicesForUser(dt.user) : null;
        var unlockedDevices = showUnreserved ? DeviceTable.getUnreservedDevices() : null;

        /** Look through all the devices and determine that ones that should be shown and not shown
         * based on the tags that are selected
         */
        for (var source in sourceToTags) {
            var sel = false;

            if (selectedTags.length != 0) {
                for (var t = 0; t < selectedTags.length; t++) {
                    if ($.inArray(selectedTags[t], sourceToTags[source]) != -1) {
                        sel = true;
                        break;
                    }
                }
            } else {
                sel = true;
            }

            var returnDevice = sel;
            if (showMyDevices) {
                var mine = $.inArray(source, myDevices) != -1;
                returnDevice = mine && sel;
            } else if (showUnreserved) {
                var unlocked = $.inArray(source, unlockedDevices) != -1;
                returnDevice = unlocked && sel;
            }

            var row = dt.deviceIdToRows[source];
            var alreadyShown = (filteredTable.has(row).length > 0) && (hiddenTable.children().length != 0);
            var shouldAnimateShow = (devicesToShowAndAnimate.length < numToAnimate);
            var shouldAnimateHide = (devicesToHideAndAnimate.length < numToAnimate);
            if (returnDevice) {
                /** Only animate if we have less than the number we are animating and that the element
                 * is not already seen on screen. The exception is on the first tag selected,
                 * then animate it the element, even though it techincally was already shown
                 */
                if (!alreadyShown && shouldAnimateShow) {
                    devicesToShowAndAnimate.push(row);
                } else {
                    devicesToShow.push(row);
                }
            } else {
                /** If the check box is in selected state, deselect the check box before hiding it. */
                row.find('.bulk-checkbox:checked').prop('checked', false);

                if (alreadyShown && shouldAnimateHide) {
                    devicesToHideAndAnimate.push(row);
                } else {
                    devicesToHide.push(row);
                }

            }
        }
        for (var i = 0; i < devicesToHideAndAnimate.length; i++) {
            devicesToHideAndAnimate[i].hide('slide', {
                duration: 600,
                complete: function() {
                    $('.stbTable', dt.tableUI).append($(this));
                    $(this).show();
                }
            });
        }
        /** First hide all the devices that shouldn't be shown by moving them to the hidden table */
        $('.stbTable', dt.tableUI).append(devicesToHide);

        /** Next hide all the ones that should be animated so they can be reshown as animated */
        for (var i = 0; i < devicesToShowAndAnimate.length; i++) {
            devicesToShowAndAnimate[i].hide();
        }
        /** Move all the devices to animate over to the visible table */
        $('.filteredTable', dt.tableUI).append(devicesToShowAndAnimate);
        /** Show and animate all the ones that should be animated */
        for (var i = 0; i < devicesToShowAndAnimate.length; i++) {
            devicesToShowAndAnimate[i].show('slide', {duration: 600});
        }
        $('.filteredTable', dt.tableUI).append(devicesToShow);

        /**
         * Since hidden check boxes are deselected a call to highlight the tag
         * of checked check box element is necessary.
         */
        TagCloud.selectSources(DeviceTable.getSelectedDevices());

        /**
         * Since the elements moved from display div to hidden div and vice versa the
         * toggle all check box should not be kept as checked. So unchecking the checked
         * toggle all check box.
         */
        $('.toggle-all-checkbox:checked').prop('checked', false);
    }

    dt.getRowHeight = function() {
        return dt.rowHeight;
    }

    return dt;
}());
