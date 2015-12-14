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
$(function() {
    $("#populateInput").focus();
    DAWGPOPULATE.populateHandler();
});

var DAWGPOPULATE = (function() {

    var dawgApi = {};

    dawgApi.populate = function() {
        var value = $("#populateInput").val();
        var isToOverrideDawgMetaData = $(".overrideDawgMetaDataCheckbox").prop('checked');

        target = CXT_PATH + "/devices/populateAndStore/" + value
                + "?isToOverride=" + isToOverrideDawgMetaData;
        table = CXT_PATH + "/populateTable";
        $.post(target, function(data) {
            $("#response").text("Devices added: " + data);
            if (data >0 ) {
                $("#tokenTable").fadeOut();
                $.post(table, function(response) {
                    $("#tokenTable").html(response);
                    $("#tokenTable").fadeIn();
                });
            }
        });
    };

    dawgApi.populateHandler = function() {
        dawgApi.inputHandler("populate", dawgApi.populate);
    };

    dawgApi.inputHandler = function(id, callback) {
        $("#"+id+"Input").keypress(function(event) {
            if ( event.which == 13 ) {
                event.preventDefault();
                callback();
            }
        });
        $("#"+id+"Button").click(function(event) {
            callback();
        });
    };

    return dawgApi;
}());
