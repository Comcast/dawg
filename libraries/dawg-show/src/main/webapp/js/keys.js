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
/*-------------------------------------------------------------------------
 * Script contains key operations for the Remote Control.
 *
 * DEPENDENCIES
 *  - Logger.js
 *-------------------------------------------------------------------------*/

/** Flag to indicate whether the remote button is pressed or not.*/
var isMouseDown = false;

/** Latest key to be send.*/
var latest_key;

/** Latest key event which is to be send. */
var latest_event;


/** Function invoked when the DOM is ready.*/
$(function() {
    $("#skeyboard,#shard-power,#skeyhelp").tooltip({
          show: {
            effect: "slideDown",
            delay: 2000
        },
        hide: {
            effect: "puff",
        }
    });

    /* Captures mouse out event for the DOM body and if holdkey action is in-progress (isMouseDown = true), it will generate
     * a mouseup event on the latest key to complete the hold action.*/
    $('body').mouseout(function() {
        if (isMouseDown) {
            clickRemoteButton(latest_event, latest_key, false);
        }
    });

    /* Creates a tabbed interface for trace and event logs. */
    $('.traceTab').tabs({

        /* Invoked when a tab widget is selected. */
        activate : function(event, ui) {
            var container;
            var index = ui.newTab.index();

            if( index === 0) {
                container = $(getVisibleTraceContainer()+' .traceDisp');
            } else if(index === 1) {
                container = $(getVisibleTraceContainer()+' .eventDisp');
            }
            doAutoScrolling(container);
        }
    });

});

/** Constant regex pattern for Non blinking keys. Applicable for hold actions. */
var non_blinking_key_pattern = /VOL(UP|DN)|CH(UP|DN)/;

/**
 * Sends a key to the server to be forwarded to the stb
 * @param key The name of the key to send
 */
function sendKey(key) {
    if ($('#faded').css('display') == 'none') {
        var deviceIds = LayoutUtil.getCheckedDevices();
        var keyPattern = "Key:"+ key;
        updateEventLog("Pressed remote key ["+ keyPattern +", Action:SENDKEY]", deviceIds);
        var remoteType = HTMLUtil.getParameterByName("remoteType");
        remoteType = (remoteType != null) ? remoteType.toUpperCase() : null;
        var keyPayload = {
                deviceIds : deviceIds,
                key : key,
                remoteType : remoteType
        };
        var posting = $.post(CXT_PATH + '/sendKey', keyPayload);
        validatePostEvent(posting, keyPattern);
    }
}

/**
 * Called when a key is clicked. This will blink the image and then send the key to the stb
 * @param image The image that was clicked and will blink
 * @param key The key to send to the stb
 */
function clickKey(image, key) {
    if(isBlinkAllowed(key)) {
        blink(image);
    }
    sendKey(key);
}

/**
 * Called when a key is holded for a duration. The image for the key will blink and then send the key along with
 * hold duration to the stb.
 *
 * @param image The image that was clicked and will blink
 * @param key The key to send to the stb
 * @param holdTime time in milliseconds the key is pressed.
 */
function holdIRKey(image, key, holdTime) {
    if(isBlinkAllowed(key)) {
        blink(image);
    }
    holdKey(key, holdTime);
}

/**
 * Blink keys only for key patterns which are NOT defined by
 * 'non_blinking_key_pattern' variable. For those keys flashing of button is
 * handled separately. Unlike other remote types BCM-7437-1 remote has separate
 * image for Chup, Chdn, Volup and Voldn. So these key patterns are also handled
 * here (ignoring non_blinking_key_pattern) if the remote type is BCM-7437-1.
 * Since the blinking of GENERIC remote is handled separately, isBlinkingAllowed
 * should return false, if remote type is GENERIC.
 *
 * @param keyName
 *            currently sending key name.
 * @returns true if flashing of button is allowed. False otherwise.
 */
function isBlinkAllowed(keyName) {
    var remoteType = StandardRemote.getRemoteName();
    var isBlinkingKey = non_blinking_key_pattern.test(keyName) ? false : true;
    var status = isBlinkingKey;
    if ('BCM-7437-1' === remoteType || 'XR11' === remoteType) {
        status = true;
    } else if ('GENERIC' === remoteType) {
        status = false;
    }
    return status;
}

/**
 * Sends a key to the server to be forwarded to the stb
 * @param key The name of the key to send
 * @param holdTime time in milliseconds the key is holded
 */
function holdKey(key, holdTime) {
    var deviceIds = LayoutUtil.getCheckedDevices();
    var keyPattern = "Key:"+ key;
    updateEventLog("Pressed remote key ["+ keyPattern +", Action:HOLDKEY, TimeInMillis:"+ holdTime+"]", deviceIds)
    var remoteType = HTMLUtil.getParameterByName("remoteType");
    remoteType = (remoteType != null) ? remoteType.toUpperCase() : null;
    if ($('#faded').css('display') == 'none') {
        var keyPayload = {
                deviceIds : deviceIds,
                key : key,
                holdTime : holdTime,
                remoteType : remoteType
        };
        var posting  = $.post(CXT_PATH + '/holdKey', keyPayload);
        validatePostEvent(posting, keyPattern);
    }
}

function sendKeyFromList() {
    var key = $('#keySelection').find(":selected").text();
    sendKey(key);
}

/**
 * Since chup and chdn and volup and voldn share an image then this method is called and determines which region is clicked.
 * @param img The image that was clicked
 * @param key The name of the key to send (VOL or CH)
 * @param clickY user clicked position(from top) in the page
 * @param isMouseDown flag to indicate whether key pressed or not. The button will blink only during the
 * release of the key.
 * @returns key pattern to be sent to the Rack.
 */
function getKeyForVolCh(img, key, clickY, isMouseDown) {
    var iconHeight = $(img).height()/2;

    // Actual image mid pixel in the page.
    var boundary = $(img).offset().top + iconHeight;

    var clickedTop = clickY < boundary;
    var top =  clickedTop ? 0 : iconHeight;

    var keySuffix = clickedTop ? 'UP' : 'DN';

    if(!isMouseDown) {
        blinkRegion($(img)[0], 0, top, $(img).width(), iconHeight);
    }
    return (key + keySuffix);
}


var keyMap = new Array();
keyMap[13] = 'SELECT'; // enter
keyMap[38] = 'UP'; // up
keyMap[40] = 'DOWN'; // down
keyMap[37] = 'LEFT'; // left
keyMap[39] = 'RIGHT'; // right
keyMap[65] = 'A'; // a
keyMap[66] = 'B'; // b
keyMap[67] = 'C'; // c
keyMap[68] = 'D'; // d
keyMap[8] = 'LAST'; // backspace
keyMap[189] = 'CHDN'; // +
keyMap[187] = 'CHUP'; // -
keyMap[33] = 'PGUP'; // Page Up
keyMap[34] = 'PGDN'; // Page Down
keyMap[71] = 'GUIDE'; // g
keyMap[77] = 'MENU'; // m
keyMap[73] = 'INFO'; // i
keyMap[27] = 'EXIT'; // esc
keyMap[48] = 'ZERO'; // 0
keyMap[49] = 'ONE'; // 1
keyMap[50] = 'TWO'; // 2
keyMap[51] = 'THREE'; // 3
keyMap[52] = 'FOUR'; // 4
keyMap[53] = 'FIVE'; // 5
keyMap[54] = 'SIX'; // 6
keyMap[55] = 'SEVEN'; // 7
keyMap[56] = 'EIGHT'; // 8
keyMap[57] = 'NINE'; // 9
keyMap[112] = 'FAV_1'; // Fav 1
keyMap[113] = 'FAV_2'; // Fav 2
keyMap[114] = 'FAV_3'; // Fav 3
keyMap[115] = 'FAV_4'; // Fav 4

/** Key code for '=' which is mapped to CHUP  */
var CHUP_KEY_CODE = 187;

/** Key code for '-' which is mapped to CHDN  */
var CHDN_KEY_CODE = 189;

/** Key codes of Fav1, Fav2,Fav3 and Fav4 which are mapped to FAV_1, FAV_2, FAV_3, FAV_4 */
var FAV_CHANNEL_CODES = [112, 113, 114, 115];

/** Key codes of buttons that are not available in BCM-7437-1 remote (PGUP, PGDN, A, B, C, D) */
var KEY_CODES_NOT_SUPPORTED_BY_BCM_7437_1_REMOTE = [33, 34, 65, 66, 67, 68];

$(window).keydown(function(event) {
    var editingText = $(document.activeElement).attr("type") == "text" || $(document.activeElement).attr("type") == "textarea";
    if (!editingText && hotKeysOn && !event.ctrlKey) {
        var keyCode = event.which;
        var key = keyMap[keyCode];
        if (key != null) {
            var rem = $('#standardRemoteDiv').is(':hidden') ? 'm' : 's';
            var remoteType = StandardRemote.getRemoteName();

            /** For CHUP and CHDN, one single image is used. So blink the proper area in UI depends on the Pressed key.*/
            if((keyCode === CHUP_KEY_CODE || keyCode === CHDN_KEY_CODE) && (remoteType != 'BCM-7437-1')) {
                var chElement = $("#sch")[0];
                var iconHeight = $(chElement).height()/2;
                var iconWidth = $(chElement).width();

                if(keyCode === CHUP_KEY_CODE) {
                    blinkRegion($(chElement)[0], 0, 0, iconWidth, iconHeight);
                } else {
                    blinkRegion($(chElement)[0], 0, iconHeight, iconWidth, iconHeight);
                }
            }

            validateClickedKey(rem, key, keyCode, remoteType);
            event.preventDefault();
        }
    }
});

function validateClickedKey (rem, key, keyCode, remoteType){
    if ((FAV_CHANNEL_CODES.indexOf(keyCode) > -1) && remoteType != 'BCM-7437-1') {
        // Do nothing, do not send key press
    } else if ((KEY_CODES_NOT_SUPPORTED_BY_BCM_7437_1_REMOTE.indexOf(keyCode) > -1) && remoteType =='BCM-7437-1'){
        // Do nothing, do not send key press
    } else {
        clickKey(document.getElementById(rem + key.toLowerCase()), key);
    }
}

var hotKeysOn = true;

function clickKeyboard(img) {
    hotKeysOn = !hotKeysOn;
    img.src = CXT_PATH + "/images/keyboard" + (hotKeysOn ? "Sel" : "") + ".png";
    updateKeyboardQHelp(hotKeysOn);
}

/** Constant to store the time in milliseconds for a single key press. */
var single_keypress_time = 100;

/** Delay in milliseconds used to distinguish between a normal click from the hold key event. If a key is
 * is pressed for more than 500ms, then it is assumed as a hold action.*/
var delay_for_hold_action = 500;

/** Interval timer variable for hold action */
var intervalTimer;

/** Timeout timer variable for hold action.*/
var timeOut;

/** Holds the time at which the key is pressed. */
var startTime = 0;

/** Holds the time at which the key is released. */
var endTime = 0;

/**
 * Method which handles the click events and distinguish between normal click and hold action.
 * If a key is pressed for < 500ms, then it is assumed as a normal single click action. If the click,
 * duration is more than 500ms then it will identify it as a hold event and compute the hold time in milliseconds.
 *
 * General rule for computation is : <b>For every 100ms there will be 1 key press.</b>
 *
 * @param e click event
 * @param key key value to be sent to the IRClient
 * @param isDown Flag to indicate whehter key press or key release.
 */

function clickRemoteButton(e, key, isDown) {
    latest_event = e;
    latest_key = key;

    var image = latest_event.target;

    if (isDown) {
        isMouseDown = true;
        startTime = Date.now();
        clearTimers();
        $('#hold_panel').text('');

        clickTime = Date.now() - startTime;

        // Setup a timeout timer to update the repeat count values.
        // The counter updation starts ONLY AFTER (500 - current clickTime)
            timeOut = setTimeout(function () {
                updateCounter(e);
            }, (delay_for_hold_action - clickTime));
    } // end of key down action
    else {

        // Trigger the key up action only if valid mouse down action exists.
        if(isMouseDown){
            isMouseDown = false;
            endTime = Date.now();

            // clear all timers which are asynchronously updating the repeat counts.
            clearTimers();

            // Compute the key hold time.
            var holdTimeInMillis = endTime - startTime;

            $('#hold_panel').hide("slow");

            if (holdTimeInMillis > delay_for_hold_action) {
                // Press and hold key
                holdIRKey(image, key, holdTimeInMillis);
            } else {
                // Normal click
                clickKey(image, key);
            }
            // reset starttime and endtime variables.
            startTime = 0;
            endTime = 0;
        }
    }
}

/**
 * Method to asynchronously update the repeat counter for the hold action.
 * @param e click event
 */

function updateCounter(e) {
    // Default repeat count value. In order to have a hold event there should be a minimum of
    // 500 ms. For every 100ms there will be 1 key. So setting 5 as default repeat count.
    repeatCount = 5;

    // Computing currently clicked position in the screen to draw the counter panel.
    posX = e.pageX;
    posY = e.pageY;

    // Increment counter in every 100ms, until the timers are cleared.
    intervalTimer = setInterval(function () {
        repeatCount++;
        drawOverlay(posX, posY, repeatCount);
    }, single_keypress_time);
}

/**
 * Draw the counter panel overlay.
 * @param posX currently clicked X position
 * @param posY currently clicked Y position
 * @param repeatCount repeat count for the key.
 */

function drawOverlay(posX, posY, repeatCount) {
    $('#hold_panel').css('left', posX + 10);
    $('#hold_panel').css('top', posY + 5);
    $('#hold_panel').show("fast");
    $('#hold_panel').text(repeatCount + ' repeats ');
}

/**
 * Clear all the previously existing timeout and interval timers.
 */

function clearTimers() {
    clearInterval(intervalTimer);
    clearTimeout(timeOut);
}

/**
 * Used to display or hide the keyboard to remote key mapping overlay
 * @param e click event
 * @param status determines whether to show or hide the overlay.
 */
function displayKeyMappingOverlay(e, status) {
    var image = e.target;
    if(status) {
        blink(image);
        $("#keymap_panel").show("slow")
    } else {
        $("#keymap_panel").hide("slow")
    }
}

/**
 * Display keyboard quick help button only if the keyboard mode is active.
 * @param isVisible indicate whether keyboard mode is currently active or not.
 */
function updateKeyboardQHelp(isVisible) {
    if(isVisible) {
        $("#skeyhelp").show();
    } else {
        $("#skeyhelp").hide();
    }
}

/**
 * Helper method to verify the post event response.
 * @param posting event response
 * @param keyPattern key pattern sent to the server.
 */
function validatePostEvent(posting, keyPattern) {
    posting.done(function( data ) {
        updateEventLog( "Successfully sent ["+ keyPattern +"]", null);
        });
    posting.fail(function(jqXHR, textStatus, errorThrown){
        updateEventLog( "Error while sending ["+ keyPattern + "]", null);
        updateEventLog( errorThrown +". Status: "+ jqXHR.status, null);
    });
}
