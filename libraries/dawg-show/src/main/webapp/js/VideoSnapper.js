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
 * Loads the video snap.
 */
var VideoSnapper = (function() {
    var snapper = {};

    /**
     * Takes a snapshot of the video and stores it in the image cache. Then opens up a window
     * to the comparison page with that image
     */
    snapper.snapVideo = function(id) {

        $.get(CXT_PATH + '/video/snap', {deviceId : id}, function(imageId) {
            if (imageId) {
                window.open(CXT_PATH + '/video/snapped?i=' + imageId + '&deviceId=' + id);
            } else {
                alert("Cant create snapshot of device " + id + ", because of lack of video feed.");
            }
        });
    };

    return snapper;
}());
