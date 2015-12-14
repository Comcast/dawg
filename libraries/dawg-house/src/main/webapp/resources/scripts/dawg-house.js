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
    DAWGHOUSE.changeUserHandler();
    DAWGHOUSE.searchHandler();
    DAWGHOUSE.bulkTagHandler();
    $("#searchInput").focus();
});


var DAWGHOUSE = (function() {

    var dawgApi = {};

    dawgApi.changeUser = function() {
        var newUser = $("#changeUserInput").val();
        if(dawgApi.validate(newUser)) {
            var target = CXT_PATH + '/' + newUser.toLowerCase() +"/" + location.search;
            window.location = target;
        }
    };

    dawgApi.changeUserHandler = function() {
        dawgApi.inputHandler("changeUser", dawgApi.changeUser);
    };

    dawgApi.validate = function(username) {
        if(null!=username && "" != username){
          if (/[^a-zA-Z0-9]/.test(username)) {
            $("#badlogin").show("highlight");
            return false;
          } else {
            return true;
          }
        }
    };

    dawgApi.bulkTag = function() {

        var tagString = $('#bulkTagInput').val().split(" ");
        var ids = DeviceTable.getSelectedDevices();
        if (ids.length != 0) {
            var target = CXT_PATH + "/devices/update/tags/add";
            var payload = {"id":ids,"tag":tagString};

            $.ajax({
                url:target,
                type:"POST",
                data:JSON.stringify(payload),
                contentType:"application/json; charset=utf-8",
                success: function() {
                    /** After the tags have been added to the database, populate the DeviceTable
                     * and the TagCloud with the changes */
                    DeviceTable.addOrRemoveTags(tagString, ids, true);
                    TagCloud.addOrRemoveTags(tagString, ids, true);
                    /** On successful tagging the tag name provided by user is cleared. */
                    $('#bulkTagInput').val("");
                }
            });
        } else {
            alert('No devices selected');
        }

    };

    dawgApi.bulkTagHandler = function() {
        dawgApi.inputHandler("bulkTag", dawgApi.bulkTag);
    };

    dawgApi.search = function() {
        // requestParams comes from the controller.  It's added as an attribute to the request
        var params = requestParams;
        var searchValue = $("#searchInput").val();
        var query = "?q="+encodeURIComponent(searchValue);
        $.each(params, function(key, value) {
            console.log(key+"="+value);
            if (key != "q") {
                if (value) {
                    if (Array.isArray(value)) {
                        $.each(value, function(i,v) {
                            if(v) {
                                query += "&" + key +"="+encodeURIComponent(v);
                            }
                        });
                    } else {
                        query += "&" + key +"="+encodeURIComponent(value);
                    }
                }
            }
        });
        console.log(query);
        var target = window.location.origin + window.location.pathname + query;
        console.log(target);
        window.location = target;
    };

    dawgApi.searchHandler = function() {
        dawgApi.inputHandler("search", dawgApi.search);
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

    dawgApi.openDawgShow = function(name, deviceId, user) {
        var reserver = $('#td-' + deviceId).attr('data-token');
        var locked = $('#td-' + deviceId).attr('data-locked') == 'true';
        var needConfirm = locked && (reserver != user);
        var confirmMsg = name + ' is reserved by ' + reserver + '. Are you sure you want to open it?';
        if (!needConfirm || confirm(confirmMsg)) {
            window.open(DAWG_SHOW + 'stb?deviceId=' + deviceId);
        }
    };

    dawgApi.bindTable = function() {
        var start = (new Date()).getTime();
        $( ".collapsableRow", $(document) ).accordion({
            collapsible: true,
            active: true,
            heightStyle: 'content',

            // scroll the list up if meta data gets cutoff when expanded
            activate: function( event, ui ) {

                // only scroll if expanding
                if (ui.newPanel != null) {
                    var tableBottom = $(".filteredTable").offset().top + $(".filteredTable").height();
                    var scrollTop = $(".filteredTable").scrollTop();
                    var relativeRowTop = this.offsetTop - scrollTop;

                    //var expandedOffset = ui.newPanel.height() + 45;
                    var expandedOffset = this.offsetHeight;

                    // only scroll if row expand will be cut off
                    if (relativeRowTop + expandedOffset > tableBottom) {

                        // scroll the window
                        $(".filteredTable").animate({
                            scrollTop: scrollTop + (expandedOffset - (tableBottom - relativeRowTop))
                        });
                    }
                }
            }
        });

        $(window).resize(dawgApi.sizeTable);
    }

    /**
     * Sizes the table so that it fills up the remaining space besides each of the
     * other elements
     */
    dawgApi.sizeTable = function() {
        var wHeight = $(window).height();
        var hHeight = $('header', $(document.body)).outerHeight();
        var uHeight = $('.user', $(document.body)).outerHeight();
        var fHeight = $('footer', $(document.body)).outerHeight();
        var newHeight = wHeight - hHeight - uHeight - fHeight;
        var content = $('.content', $(document.body));
        content.css('height', newHeight);
        var sHeight = $('.stbTableHead', content).outerHeight();
        var tHeight = $('.bulk-tag', content).outerHeight();
        $('.filteredTable', content).css('height', newHeight - sHeight - tHeight);

        /** Have the table head match the table rows */
        var margin = parseInt($('.stbMain', content).css('margin-left'));
        $('.stbMain', content).css('width',$('.stbTableHead', content).width());
        $('.stbTableHead', content).css('margin-left', margin + 12.67); // adds size of expand icon subtracts size of scroll bar
    }

    return dawgApi;
}());
