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
/*-------------------------------------------------------------------------
 * Script used to log the user events to the 'Event Log' tab in the
 * DAWG-SHOW UI.
 *
 * DEPENDENCIES
 *  - Date.js
 *-------------------------------------------------------------------------*/

/** Standard MAC id character length */
var mac_id_char_length = 12;

/**
 * Update the user event logs in the tab area in dawg-show.
 *
 * @param log
 *            event log to be added to UI
 * @param devices
 *            deviceIds for the boxes to which user events are sent.
 */
function updateEventLog(log, devices) {
    var date = new Date();
    var dateFormat = "["+ formatDate(date)+ "] ";

    /* Select the 'Event Log' tab associated with the currently visible Trace container. */
    var eventLog = $(getVisibleTraceContainer()+' .eventDisp');
    eventLog.append(dateFormat).append(log).append("<br>");
    if(devices != null) {
        eventLog.append(dateFormat).append("Box:[").append(getMacIds(devices).toString()).append("]<br>");
    }

    /* Get the tab index and if it is for Event log(value=1) process auto scrolling feature.*/
    var tabIndex = getSelectedTabIndex();
    if(tabIndex == 1) {
        doAutoScrolling(eventLog);
    }
}

/**
 * Method to get properly formatted MAC Information.
 * @param deviceIds raw device ids in lower case with out colon(:) delimiter.
 * @returns {Array} of properly formatted MAC ids for the input device ids.
 */
function getMacIds(deviceIds){
    var macAddress = new Array();
    for(var i=0; i < deviceIds.length; i++) {
        var device = deviceIds[i];
        macAddress[i] = formatDeviceId(device);
    }
    return macAddress;
}

/**
 * Format the given Device id. First it will convert characters to upper case then add colon delimiter.
 * @param deviceId raw device id.
 * @returns {String} formatted MAC id.
 */
function formatDeviceId(deviceId) {
    var mac = "";
    if(deviceId.length === mac_id_char_length) {
        deviceId = deviceId.toUpperCase();
        for(var index=0; index < mac_id_char_length; index += 2) {
            var character = deviceId.substring(index, index + 2);
            mac = index === (mac_id_char_length - 2) ? mac.concat(character) :  mac.concat(character).concat(":")
        }
    }
    return mac;
}

/**
 * Get selected tab index.
 * @returns 0 - Box Trace tab
 *          1 - Event Log tab.
 */
function getSelectedTabIndex() {
    return $(getVisibleTraceContainer()+ ' .traceTab').tabs('option', 'active');
}

/**
 * Get currently visible trace container class selector
 * @returns {String} visible trace class selector.
 */
function getVisibleTraceContainer() {
    return '.traceDiv:visible';
}

/**
 * Does the auto scrolling of content the specified container.
 * First verifies whether the 'autoscroll' option is checked. If enabled, make it visible
 * the latest event logs
 *
 * @param container the container in which the auto scrolling is to be made.
 */
function doAutoScrolling(container) {
    var autoscrollOn = $(getVisibleTraceContainer() + ' .autoscroll').is(":checked");
    if (autoscrollOn) {
        container.scrollTop(container[0].scrollHeight);
    }
}
