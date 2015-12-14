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
 * Controls snapshot download operations in dawgshow multi-view
 */
var MultiSnapDownloadManager = (function() {

    var snapDownloadManager = {};

    /**
     * Bind the root path of dawgshow application and the map of device id with
     * corresponding capture image ids.
     *
     * @param rootPath
     *            root path of dawgshow application
     * @param deviceIdImageIdMap
     *            the map of device id with corresponding capture image ids
     */
    snapDownloadManager.bind = function(rootPath, deviceIdImageIdMap) {

        HTMLUtil.doFormSubmit(rootPath + '/snap/multi', deviceIdImageIdMap, 'get');
        var snapFailedDevices = [];
        $.each(deviceIdImageIdMap, function(deviceId, imageId) {
            if (!imageId) {
                snapFailedDevices.push(deviceId)
            }
        });
        if (0 != snapFailedDevices.length) {
            alert("Cant create snapshot of device[s] " + snapFailedDevices
                    + ", because of lack of video feed ");
        }
        //Need to close the tab forcefully
        setTimeout(function() {
            window.close();
        }, 1000);
    };
    return snapDownloadManager;
}());
