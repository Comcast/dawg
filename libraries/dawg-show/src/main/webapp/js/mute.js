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
 * Shows the power prompt
 */
function clickMute(image) {
    blink(image);
    $('#mutePrompt').show();
    $('#faded').show();
}

/**
 * Dismisses the power prompt
 */
function dismissMutePrompt() {
    $('#mutePrompt').hide();
    $('#faded').hide();
}

/**
 * Sends the mute command to the stb
 * @param command
 */
function sendMuteCommand() {
    dismissMutePrompt();
    sendKey('MUTE');
}

function muteAudio() {
    dismissMutePrompt();
    var audioElements = $("#audio");
    if (audioElements.length > 0) {
        var audio = audioElements[0];
        audio.muted = !audio.muted;
    }
}


