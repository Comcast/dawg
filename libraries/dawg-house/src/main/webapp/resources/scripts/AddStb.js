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
var AddStb = (function() {

    var module = {};

    var idLength = 12;
    var defaultRemoteType = "XR2";

    function makeid() {
        var id = "";
        var possible = "abcdefghijklmnopqrstuvwxyz0123456789";
        for (var i = 0; i < idLength; i++)
            id += possible.charAt(Math.floor(Math.random() * possible.length));
        return id;
    }

    function isValidName(val) {
        return $.trim(val) !== '';
    }

    function isValidMacAddress(val) {
        return /^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$/.test(val);
    }

    module.bind = function(formUI) {
        $('#submit-btn').prop('disabled', true);

        formUI.on('submit', function(e) {
            e.preventDefault();
            $('#submit-btn').button('loading');
            var data = formUI.serializeObject();
            var id = makeid();
            data.id = id;
            data.remoteType = defaultRemoteType;
            DAWGHOUSE.addStb(id, data, function() {
                $('#submit-btn').button('reset');
                $("#alerts").before(
                    '<div class="alert alert-success alert-dismissible" role="alert">' +
                        '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
                            'Stb <strong>' + data.name + '(' + id + ')' + '</strong> successfully added!' +
                    '</div>'
                );
            });
            return false;
        });
        $("#name,#macAddress",formUI).finishTyping(function() {
            /** Only enable the Submit Button with valid name and MAC address */
            var name = $("#name").val();
            var macAddress = $("#macAddress").val();
            $('#submit-btn').prop('disabled', !(isValidName(name) && isValidMacAddress(macAddress)));
        });
    };

    return module;
}());