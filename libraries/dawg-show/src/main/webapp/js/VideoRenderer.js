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

var MAX_FPS = 20;
var MIN_FPS = 3;

var VideoRenderer = (function() {

    var vr = {
            feeds : new Array(),
            fps: MAX_FPS,
            lastStamp : -1,
            lastSuccess : -1
    };

    /**
     * Called by the html page to bind the code to the UI elements
     */
    vr.bind = function(videoCanvas, deviceId) {
        var hd = videoCanvas.prop('tagName').toLowerCase() == 'canvas';
        if (hd) {
            var videoUrl = videoCanvas.attr('data-videourl');
            var feed = new VideoFeed(videoCanvas[0], deviceId, videoUrl);
            vr.feeds[deviceId] = feed;
            feed.image = new Image();
            feed.image.onload = function() {
                feed.canvas.width = feed.image.width;
                feed.canvas.height = feed.image.height;
                feed.draw();
            }
        }
    }

    vr.startVideo = function() {
        for (var deviceId in vr.feeds) {
            vr.feeds[deviceId].connect();
        }

    }

    vr.setResolutions = function() {
        for (var deviceId in vr.feeds) {
            if (vr.feeds[deviceId].connected) {
                vr.feeds[deviceId].setResolution();
            }
        }
    }

    vr.showError = function(error) {
        if (!vr.errorShown) {
            vr.errorShown = true;
            alert('Failed to connect to video at ' + error.target.url);
        }
    }

    return vr;
}());

function VideoFeed(canvas, deviceId, videoUrl) {
    this.canvas = canvas;
    this.deviceId = deviceId;
    this.videoUrl = videoUrl;
    this.image = null;
    this.lastStamp = -1;
    this.connection = null;
    this.connected = false;
    this.firstFrame = true;
    this.player = null;

    this.draw = function() {
        var cxt = this.canvas.getContext('2d');

        cxt.clearRect(0, 0, this.canvas.width, this.canvas.height);
        /** Draws the snapped image */
        cxt.drawImage(this.image, 0, 0);
    }

    this.connect = function() {
        var badVideo = $('<canvas>').addClass('notAvailableOverlay');
        $(this.canvas).parent().append(badVideo);
        var txtOverlay = new TextOverlay(badVideo[0], 'Video Unavailable');
        LayoutDelegator.addOverlay(txtOverlay);

        this.connection = new WebSocket(this.videoUrl);
        this.player = new jsmpeg(this.connection, {canvas:this.canvas});
        this.connection.feed = this;
        this.connection.txtOverlay = txtOverlay;

        this.connection.onopen = function () {
            LayoutDelegator.removeOverlay(this.txtOverlay);
            this.feed.player.initSocketClient(this);
        }

        this.connection.onerror = function (error) {
            /** When the connection closes */
            this.feed.connected = false;
            VideoRenderer.showError(error);
        };

        this.connection.onclose = function(event) {
            this.feed.connected = false;
            LayoutDelegator.addOverlay(this.txtOverlay);
        }
    }
}
