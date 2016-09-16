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
function clickPower(image) {
    blink(image);
    $('#powerPrompt').show();
    $('#faded').show();
}

/**
 * Sends the power command to the stb
 * @param command
 */
function sendPowerCommand(command) {
    var deviceIds = LayoutUtil.getCheckedDevices();

    var cmdPattern = "command:"+ command;
    updateEventLog("Sending POWER "+ cmdPattern, deviceIds);

    var powerPayload = {
            deviceIds : deviceIds,
            command : command
    };
    dismissPrompt();
    var posting = $.post(CXT_PATH + '/power', powerPayload);
    validatePostEvent(posting, cmdPattern);
}

/**
 * Sends a command to the server to reboot the stb
 */
function reboot() {
    sendPowerCommand('reboot');
}

/**
 * Sends a command to the server to power off the stb
 */
function powerOff() {
    sendPowerCommand('off');
}

/**
 * Sends a command to the server to power on the stb
 */
function powerOn() {
    sendPowerCommand('on');
}

/**
 * Dismisses the power prompt
 */
function dismissPrompt() {
    $('#powerPrompt').hide();
    $('#faded').hide();
}

function sendPowerKey() {
    dismissPrompt();
    sendKey('POWER');
}
