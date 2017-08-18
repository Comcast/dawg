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
    DAWGPOUND.reservationHandler();
});

var DAWGPOUND = (function() {

    var dawgApi = {};

//  dawgApi.reservationDialogSetup = function() {
//      $("#reservation-lock-dialog-date-picker").datepicker();
//      $("#reservation-lock-dialog").dialog({
//          buttons : [ {
//              text : "Reserve Device(s).",
//              click : function() {
//                  var originId = $("#reservation-lock-dialog").data("origin");
//                  var element = $(originId);
//                  var deviceId = $(element).data("deviceid");
//                  var token = $(element).data("token");
//                  var date = $("#datepicker").datepicker( 'getDate' ).getTime();
//                  dawgApi.reserve(origin, deviceId, token, date);
//                  $(this).dialog("close");
//              }
//          } ]
//      });
//  };

    dawgApi.reservationHandler = function() {
        $('.lockable').click(function(event) {
            var element = this;
            var deviceId = $(element).data("deviceid");
            var loginUser = $(element).data("loginuser");
            var locked = $(element).data("locked");
            var expiration = -1;
            //dialog.data("origin",this.id);


            if (locked) {
                dawgApi.unreserve(element, deviceId, loginUser);
            } else {
                dawgApi.reserve(element, deviceId, loginUser, expiration);
            }
        });

        $('#multilock').click(function(event) {
            var elements = $('.bulk-checkbox:checkbox:checked');
            $.each(elements, function(index, element) {
                var deviceId = $(element).data("deviceid");
                var sourceElement = $("#td-"+deviceId);
                var loginUser = $(sourceElement).data("loginuser");
                dawgApi.reserve(sourceElement, deviceId, loginUser, -1);
            });
        });

        $('#multiunlock').click(function(event) {
            var elements = $('.bulk-checkbox:checkbox:checked');
            $.each(elements, function(index, element) {
                var deviceId = $(element).data("deviceid");
                var sourceElement = $("#td-"+deviceId);
                var loginUser = $(sourceElement).data("loginuser");
                dawgApi.unreserve(sourceElement, deviceId, loginUser);
            });
        });
    };

    dawgApi.reserve = function(element, deviceId, token, expiration) {
        var posturl = DAWG_POUND + "reservations/reserve/" + token + "/" + deviceId + "/" + expiration;
    	dawgApi.changeReservation(posturl, function(data) {
            if (data) {
                $(element).html('<i class="icon-lock"></i> ' + token + " - " + expiration);
                $(element).data("locked",true);
                $(element).data("token",token);
            } else {
                console.log(data + "\ncould not reserve!");
            }
        });
    };

    dawgApi.unreserve = function(element, deviceId, token) {
        var posturl = DAWG_POUND + "reservations/unreserve/" + deviceId;
    	dawgApi.changeReservation(posturl, function(data) {
	            if (data) {
	                $(element).html('<i class="icon-unlock"></i> old reserver: ' + data);
	                $(element).data("locked",false);
	                console.log(data);
	            } else {
	                console.log(data + "\ncould not unreserve!");
	            }
        });
    };

    dawgApi.changeReservation = function(url, success) {
        $.ajax({
            url: url,
            type: 'POST',
            xhrFields: {
                withCredentials: true
            },
            success: success
        });
    };

    dawgApi.userExists = function(userId, callback) {
        var url = DAWG_POUND + 'admin/user/exists/' + userId;
        $.ajax({
               type: "GET",
               url: url,
               xhrFields: {
                   withCredentials: true
               },
               success: callback
        });
    }

    dawgApi.createUser = function(userId, user, callback) {
        user.roles = user.roles ? (user.roles.constructor === Array ? user.roles : [user.roles]) : null;
        var url = DAWG_POUND + 'admin/user/' + userId;
        $.ajax({
               type: "PUT",
               url: url,
               data: JSON.stringify(user),
               xhrFields: {
                   withCredentials: true
               },
               contentType: "application/json",
               success: callback
        });
    }

    return dawgApi;
}());
