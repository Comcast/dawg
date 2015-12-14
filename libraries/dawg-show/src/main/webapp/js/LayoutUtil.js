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

var VIDEO_ASPECT_RATIO = 1.47;

var LayoutUtil = (function() {

    var lu = {};

    lu.centerInParent = function(ele) {
        var x = (ele.parent().width()/2) - (ele.width()/2);
        var y = (ele.parent().height()/2) - (ele.height()/2);
        ele.css('left', x);
        ele.css('top', y);
    }

    lu.fitElementMaintainAspectRatio = function(element, aspectRatio, fitWidthPercent, fitHeightPercent) {
        var container = element.parent();
        var cWidth = container.width() * fitWidthPercent;
        var cHeight = container.height() * fitHeightPercent;
        var contAR = cWidth / cHeight;
        var width = element.width();
        var height = element.height();
        if (contAR < aspectRatio) {
            /** Need to adjust height */
            width = cWidth;
            height = width / aspectRatio;
        } else {
            /** Need to adjust width */
            height = cHeight;
            width = height * aspectRatio;
        }
        element.css('width', width);
        element.css('height', height);
    }

    lu.getCheckedDevices = function() {
        var deviceIds = new Array();
        if(typeof deviceId === 'undefined') {
            /** multi-vision, we can probably do this better by putting all this code into a singleton
             *  Find all the stbs that are checked  */
            $('.cbStb:checked').each(function() {
                deviceIds.push($(this).attr('data-deviceid'));
            });
        } else {
            /** Single vision, deviceId will be defined in global space (need to change that sometime) */
            deviceIds.push(deviceId);
        }
        return deviceIds;
    }

    return lu;
}());
