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
var SerialManager = (function() {

    var FLUSH_BUFFER_HEX = 'd ';
    var LIGHT_FLUSH_PACKET_LENGTH = 3;
    var HEAVY_FLUSH_PACKET_LENGTH = 7;
    var FLUSH_INTERVAL = 2000;
    var FLUSH_TIME_MAX = 20000;

    var tm = {
            connections : {},
            flushes: {}
    };

    /**
     * Called by the html page to bind the code to the UI elements
     */
    tm.bind = function() {
        $('.traceComp').each(function() {

            var serialHider = $('<canvas>').addClass('serialBtnHideOverlay');
            var badTrace = $('<canvas>').addClass('notAvailableOverlay');

            //Hiding the serial buttons when no Trace is available
            var serialBtnHideOverlay = new TextOverlay(serialHider[0], '');
            LayoutDelegator.addOverlay(serialBtnHideOverlay);
            $('.serialButtons', $(this)).append(serialHider);

            //Adding the Trace Unavailable overlay when trace is not available
            $('.traceBar', $(this)).append(badTrace);
            var txtOverlay = new TextOverlay(badTrace[0], 'Trace Unavailable');
            LayoutDelegator.addOverlay(txtOverlay);
            $('.traceDisp', $(this)).append(badTrace);

            var traceHost = $(this).parent().attr('data-tracehost');
            var deviceId = $(this).parent().attr('data-deviceId');
            try {
                var url = "ws://" + traceHost + ":15080/scat/trace/" + deviceId.toUpperCase();
                var webSocket = $.gracefulWebSocket( url );
                webSocket.traceDiv = $(this);
                webSocket.txtOverlay = txtOverlay;
                webSocket.serialBtnHideOverlay = serialBtnHideOverlay;
                webSocket.lineBuffer = "";
                webSocket.onmessage = function(event) {
                    var totalTrace = this.lineBuffer + event.data;
                    var trace = totalTrace.split(/\r\n|\r|\n/g);
                    var additional = "";
                    /** Do not use last line because it is an incomplete line, instead put it in the line buffer
                     * and use it on the next message that we get */
                    for (var i = 0; i < trace.length - 1; i++) {
                        additional += trace[i] + "<br/>";
                    }
                    this.lineBuffer = trace[trace.length - 1];
                    var traceDisp = $('.traceDisp', this.traceDiv);

                    traceDisp.append(additional);

                    var autoscrollOn = $('.autoscroll',  this.traceDiv).is(":checked");
                    if (autoscrollOn) {
                        traceDisp.scrollTop(traceDisp[0].scrollHeight);
                    }
                    var tracehost = this.traceDiv.parents('.traceDiv').attr('data-tracehost');
                    var flush = tm.flushes[tracehost];
                    if (flush != null) {
                        // we are currently flushing. Since we got trace we can stop now.
                        clearInterval(flush.flushTimer);
                        tm.flushes[tracehost] = null;
                        $('.flushBufferBtn', this.traceDiv).removeAttr('disabled');
                        $('.flushBufferBtn', this.traceDiv).val('Flush Buffer');
                        updateEventLog('Flush was successful.', [flush.deviceid]);
                    }

                }

                webSocket.onopen = function(event) {
                    // removes the not available overlay
                    LayoutDelegator.removeOverlay(this.serialBtnHideOverlay);
                    LayoutDelegator.removeOverlay(this.txtOverlay);

                }

            } catch (exc) {
            }

            tm.connections[deviceId] = webSocket;

            /**
             * Handler for clicking the send serial button. This will send the command that is in the
             * text field
             */
            $('.sendSerialBtn', $(this)).click(function() {

                var traceDiv = $(this).parents('.traceDiv');
                var msg = $('.serialCmd', traceDiv).val();
                var hex = msg;
                var asHex = $('.serialHex:checked', $(this).parent()).length == 1;
                if (!asHex) {
                    hex = SerialManager.convertSerialCommandToHex(msg);
                }
                SerialManager.sendHex(traceDiv.attr('data-deviceid'), msg, hex);
                $('.serialCmd', $(this).parent()).val('');
            });

            /**
             * Click handler for the Flush Buffer button. This will start the repeated sending
             * of the flush buffer packet until some trace is seen
             */
            $('.flushBufferBtn', $(this)).click(function(event) {
                var tracehost = $(this).parents('.traceDiv').attr('data-tracehost');
                var deviceid = $(this).parents('.traceDiv').attr('data-deviceid');
                /** Need to put this in a closure so we can pass the tracehost to the interval function */
                (function() {
                    var th = tracehost;
                    var flush = {
                        flushTimer: setInterval(function() {
                            SerialManager.flush(th);
                        }, FLUSH_INTERVAL),
                        flushStart: (new Date()).getTime(),
                        deviceid: deviceid
                    };
                    tm.flushes[tracehost] = flush;
                })();
                $(this).attr('disabled', 'disabled');
                $(this).val('Flushing...');
                updateEventLog('Flushing serial buffer', [deviceid]);
            });

            $('.serialCmd', $(this)).keydown(function(event) {
                if (event.which == 13) {
                    $('.sendSerialBtn', $(this).parent()).click();
                }
            });

            /**
             * Click handler for the Clear button in trace div. This will clear all the box trace and
             * event logs.
             */
            $('.clearLogs', $(this)).click(function() {
                tm.clearLogs($(this));
            });

            $('#export', $(this)).click(function(){
                tm.exportLogs(deviceId, $(this));
            });

        });

        /**
         * Handler for clicking the serial button. This will send the
         * command that is in the text field
         */
        $('.serialCommandSendButton').click(function() {
            $('.serialCmdDiv').toggle('slow');
            var channelNumTextBox = $('.serialCmdTextBox', '.serialCmdDiv');
            if (channelNumTextBox.is(":visible")) {
                channelNumTextBox.focus();
                channelNumTextBox.val('');
            }
        });

        $('.sendButton','.serialCmdDiv').click(function() {
            tm.sendSerialCommand();
        });

        /**
         * Handler for sending the serial command on ENTER key press
         */
        $('.serialCmdTextBox', '.serialCmdDiv').keydown(function(event) {
            var key = event.which || event.keyCode;
            if (key == 13) {
                tm.sendSerialCommand();
            }
        });

    }

    /**
     * Sends a serial command to a serial host
     */
    tm.sendSerialCommand = function() {
        var deviceIds = LayoutUtil.getCheckedDevices();
        var msg = $('.serialCmdTextBox').val();
        var hex = msg;
        var asHex = $('.serialCommandHex:checked','.serialCmdDiv').length == 1;
        if (!asHex) {
            hex = SerialManager.convertSerialCommandToHex(msg);
        }
        $.each(deviceIds, function(index, deviceId) {
            var webSocket = tm.connections[deviceId];
            // Check whether web socket connection is established
            // successfully or not
            if (webSocket.readyState == 1) {
                SerialManager.sendHex(deviceId, msg, hex);
            }else{
                updateEventLog("Failed to send serial command to: "+ deviceId, null);
            }
        });
        $('.serialCmdTextBox').val('');
    }

    /**
     * Convert the non-hex message entered by user to HEX value
     *
     * @param msg
     *         The non-hex msg entered by user
     *
     * @return the hex payload
     */
    tm.convertSerialCommandToHex = function(msg) {
        var hex = "";
        for (var i = 0; i < msg.length; i++) {
            hex += msg.charCodeAt(i).toString(16);
        }
        hex += '0a0d'; // 0a0d is \n\r
        return hex;
    }

    /**
     * Sends a hex payload to a serial host
     *
     * @param deviceId
     *            The id of box to which the hex payload need to send
     * @param msg
     *            the original non-hex msg (just meant for logging the event)
     * @param hex
     *            the hex payload to send
     */
    tm.sendHex = function(deviceId, msg, hex) {
        var ws = tm.connections[deviceId];

        updateEventLog("Sending Serial command :"+msg +" ("+hex+")", null);
        ws.send(hex);
        updateEventLog("Sent Serial command :"+msg +" ("+hex+")", null);

        ws.onerror = function (event) {
            updateEventLog("Failed to send serial command: "+ hex, null);
        };
    }

    /**
     * Called regularly to send a flush packet to the given trace
     * @param tracehost the trace to send the flush packet to
     */
    tm.flush = function(tracehost) {
        var flush = tm.flushes[tracehost];
        var timeElapsed = (new Date()).getTime() - flush.flushStart;
        if (timeElapsed > FLUSH_TIME_MAX) {
            clearInterval(flush.flushTimer);
            alert('Could not detect trace after ' + (FLUSH_TIME_MAX/1000) + ' seconds');
            var btn = $('.flushBufferBtn', tm.connections[tracehost].traceDiv);
            btn.removeAttr('disabled');
            btn.val('Flush Buffer');
            updateEventLog('Flush timed out after ' + (FLUSH_TIME_MAX/1000) + ' seconds', [flush.deviceid]);
            return;
        }
        var flushLength = timeElapsed > FLUSH_TIME_MAX/2 ? HEAVY_FLUSH_PACKET_LENGTH : LIGHT_FLUSH_PACKET_LENGTH;
        var flushPacket = "";
        for (var i = 0; i < flushLength; i++) {
            flushPacket += FLUSH_BUFFER_HEX;
        }
        SerialManager.sendHex(tracehost, flushPacket, flushPacket);
    }

    /**
     * Method to export the selected tab's logs.
     */
    tm.exportLogs = function(deviceId, exportBtn) {
        var exportDiv = tm.findActiveDiv(exportBtn);

        // Exporting the content only if there is no Trace Unavailable overlay
        // or no logs to export
        if (tm.isContentPresent(exportDiv)) {
            var exportContent = exportDiv.html();
            var payload = {
                trace : exportContent.replace(/<br\s*[\/]?>/gi, '\n'),
                device : deviceId
            };
            var url = CXT_PATH + '/trace/export';
            HTMLUtil.doFormSubmit(url, payload, 'post');
        } else {
            alert("No Logs available to export");
        }
    };

    /**
     * Method to clear content in selected tab's logs either box trace or event
     * logs.
     */
    tm.clearLogs = function(clearButton) {
        var clearDiv = tm.findActiveDiv(clearButton);
        if(tm.isContentPresent(clearDiv)){
            clearDiv.empty();
        }
    };

    /**
     * Method to find the selected tab in trace div, either Box trace or Event log
     *
     * @param button
     *            the button element either Export button or Clear button
     *
     * @return the selected tab element in trace div
     */
    tm.findActiveDiv = function(button) {
        var activeDiv;
        var activeTabIndex = button.parents('.traceTab').tabs("option", "active");
        if (0 === activeTabIndex) {
            activeDiv = $('.traceDisp:visible');
        } else {
            activeDiv = $('.eventDisp:visible');
        }
        return activeDiv;
    };

    /**
     * Method to check whether any content is present in Box trace or Event
     * log.
     *
     * @param contentDiv
     *            the div element whose content presence need to check
     *
     * @return false if no content is present inside the div user chooses,
     *         else true.
     */
    tm.isContentPresent = function(contentDiv) {
        var content = contentDiv.html();
        return (-1 == content.indexOf("notAvailableOverlay") && content.trim() != "") ? true
                : false;
    };

    return tm;
}());
