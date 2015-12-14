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
 * Handles image save option for multiple boxes.
 */
var MultiSave = (function() {

    var ms = {};

    ms.bind = function(multi_save_btn) {
        $(multi_save_btn).click(function() {
            ms.saveMultiImages();
        });
    };

    /**
     * Handler for saving images from selected devices.
     */
    ms.saveMultiImages = function() {
        var deviceList = LayoutUtil.getCheckedDevices();
        var devices = "";

        if (deviceList != null && deviceList.length != 0) {
            for ( var i = 0; i < deviceList.length; i++) {
                devices = devices + deviceList[i];
                if ((i + 1) < deviceList.length) {
                    devices = devices + ",";
                }
            }
            window.open(CXT_PATH + '/video/multi/snap?deviceIds=' + devices, '_blank');
        } else {
            alert('Please select any device to take snapshot');
        }
    };

    return ms;
}());
