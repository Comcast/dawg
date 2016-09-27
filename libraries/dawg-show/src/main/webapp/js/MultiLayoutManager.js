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
 * Manages the layout for multi-view
 */
var MultiLayoutManager = (function() {

    var mlm = {
            overlays : new Array()
    };

    /**
     * Draws the layout. This will:
     * 1. Enlarge the video area if the trace area is hidden
     * 2. Fit the remote into the allotted area and maintain the same aspect ratio, then center the remote
     * 3. Fit all the video panes in the allotted space.
     * 4. Make room for the video header
     * 5. Fit the video into the allotted space and maintain aspect ratio
     * 6. Center the power prompt in the window
     */
    mlm.draw = function() {
        var v = $('#allvideos');
        var t = $('.traceDiv:visible');
        var r = $('#standardRemoteDiv');
        var w = $(window).width();

        if (t.length == 0) {
            v.css('height', '100%')
        } else {
            v.css('height', '65%')
            t.css('height', '35%')
        }

        $('#videoAndTrace').css('width', '75%');
        $('#videoAndTrace').css('height', '100%');
        r.css('width', '20%');

        LayoutUtil.fitElementMaintainAspectRatio($('#remote'), STD_REMOTE_ASPECT_RATIO, 1, 1);
        $('#remote').css('left', ($('#remote').parent().width()/2) - ($('#remote').width()/2));

        var numStbs = $('.videoDiv', v).length;
        var cols = numStbs;
        var rows = 1;
        if (numStbs > 3) {
            var root = Math.sqrt(numStbs);
            cols = Math.ceil(root);
            rows = Math.ceil(numStbs/cols);
        }
        var videoWidth = 708;
        var videoHeight = 480;
        $('.videoDiv', v).each(function() {
            $(this).css('width', (100/cols) + '%');
            $(this).css('height', (100/rows) + '%');
            var canvas = $('.video', $(this));
            var header = $('.videoHeader', $(this));
            var vBody = $('.videoBody', $(this));
            vBody.css('height', $(this).height() - header.height());
            LayoutUtil.fitElementMaintainAspectRatio(canvas, VIDEO_ASPECT_RATIO, 1, 1);
            /** center video in parent */
            var pWidth = canvas.parent().width();
            canvas.css('left', (pWidth/2) - (canvas.width()/2));
            videoWidth = canvas.width();
            videoHeight = canvas.height();
        });
        LayoutUtil.centerInParent($("#powerPrompt"));
        LayoutUtil.centerInParent($("#mutePrompt"));
        VideoRenderer.setResolutions(videoWidth, videoHeight);

        for (var i = 0; i < mlm.overlays.length; i++) {
            mlm.overlays[i].draw();
        }
    }

    mlm.addOverlay = function(overlay) {
        mlm.overlays.push(overlay);
    }

    mlm.removeOverlay = function(overlay) {
        overlay.canvas.remove();
        mlm.overlays.splice(mlm.overlays.indexOf(overlay), 1);
    }

    return mlm;
}());
