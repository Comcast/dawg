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
 * Controls Generic remote option in dawg show multi-view.
 */
var GenericRemote = (function() {

    var genericRemote = {};

    /**
     * Bind the remote change button, remote name and generic remote keys.
     *
     * @param changeRemoteButton the button element to show/hide Generic remote
     * @param remoteName the name of the remote in dawg show multi view
     * @param genericRemoteKeys the remote keys need to present in generic remote
     */
    genericRemote.bind = function(changeRemoteButton, remoteName, genericRemoteKeys) {

        var genericRemoteKeyArray = JSON.parse(genericRemoteKeys);
        for (var key in genericRemoteKeyArray) {
            var keyId = 's' + genericRemoteKeyArray[key].toLowerCase();
            $('#' + keyId).addClass('showGenericRemoteKey');
        }

        changeRemoteButton.click(function() {
            var deviceStr = HTMLUtil.getParameterByName("deviceIds");
            var url = CXT_PATH + '/multi?deviceIds=' + deviceStr
                    + "&isGenericRemote=";
            if('GENERIC' == remoteName) {
                url += "false";
            }else{
                url += "true";
            }
            window.open(url, '_self');
        });
    };
    return genericRemote;
}());
