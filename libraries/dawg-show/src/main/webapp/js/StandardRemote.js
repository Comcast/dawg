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
 * Handles selection of MOTOROLA and XR2 remote's and show/hide's SKIPFWD button accordingly
 *
 */

var StandardRemote = (function() {

    var standardRemote = {};

    standardRemote.bind = function(remoteName) {
        standardRemote.remoteName = remoteName;
        if (remoteName == "MOTOROLA" || remoteName == "XR2") {
            $('#sskipfwd').addClass('showSkipFwd');

            if (remoteName == 'MOTOROLA') {
                $('#spause').addClass('resizeMotorolaPauseButton');
            } else {
                $('#splay').addClass('resizeXR2PlayButton');
            }
        }
    };

    /**
     * Returns the name of the currently selected remote type in
     * both single and multi view
     */
    standardRemote.getRemoteName = function () {
        return standardRemote.remoteName;
    }

    return standardRemote;
}());
