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
 * Controls direct tune option in dawg show.
 */
var DirectTune = (function() {

    var directTune = {
            directTuneDiv : null
    };
    directTune.bind = function( tuneButton, channelTuneDiv) {

        directTune.directTuneDiv = channelTuneDiv;

        tuneButton.click(function(){
            channelTuneDiv.toggle('slow');
            channelNumTextBox = $('.channelNumTextBox', channelTuneDiv);
            if (channelNumTextBox.is(":visible")) {
                channelNumTextBox.focus();
                channelNumTextBox.val('');

            }
        });

        $('.goButton', channelTuneDiv).click(function() {
            directTune.tuneToChannel();
        });

        ValidateInput.forceNumeric($('.channelNumTextBox', channelTuneDiv), directTune.tuneToChannel );
    }

    /** Performs direct tune to an input channel */
    directTune.tuneToChannel = function() {
        var channelNumber = $('.channelNumTextBox', directTune.directTuneDiv).val();
        $('.channelNumTextBox', directTune.directTuneDiv).val('');
        var maxlength = 4;
        if (channelNumber.length > maxlength) {
            channelNumber = channelNumber.substr(0, maxlength);
        }

        if (channelNumber != '') {
            var deviceIds = LayoutUtil.getCheckedDevices();
            updateEventLog("sent direct tune request to " + channelNumber,
                    deviceIds);
            var remoteType = HTMLUtil.getParameterByName("remoteType");
            remoteType = (remoteType != null) ? remoteType.toUpperCase() : null;
            var keyPayload = {
                deviceIds : deviceIds,
                remoteType : remoteType,
                channelNum : channelNumber
            };
            var posting = $.post(CXT_PATH + '/directtune', keyPayload);
            posting.done(function(data) {
                updateEventLog("Successfully sent direct tune request to "
                        + channelNumber, null);
            });
            posting
                    .fail(function(jqXHR, textStatus, errorThrown) {
                        updateEventLog("Error while tuning to" + channelNumber,
                                null);
                        updateEventLog(errorThrown + ". Status: "
                                + jqXHR.status, null);
                    });
        } else {
            alert("Enter a valid channel number");
        }
    }
    return directTune;
}());
