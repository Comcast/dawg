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
 * Manages the layout for single-view
 */
var SingleLayoutManager = (function() {

    var slm = {
        buttonToCompMap : new Array(),
        toolbar : null,
        menuBox : null,
        overlays : new Array()
    };

    slm.buttonToCompMap['videoBox'] = 'videoDiv';
    slm.buttonToCompMap['traceBox'] = 'traceDiv';
    slm.buttonToCompMap['remoteStdBox'] = 'standardRemoteDiv';
    slm.buttonToCompMap['remoteMiniBox'] = 'miniRemoteComp';
    slm.buttonToCompMap['metadataBox'] = 'metaDiv';


    /**
     * Called by the ComponentToolbar when the user clicks on a component selector
     * @param boxesOn returns the components that are "turned on"
     */
    slm.handle = function(boxesOn) {
        for (var id in this.buttonToCompMap) {
            $('#' + slm.buttonToCompMap[id]).hide();
        }
        for (var b = 0; b < boxesOn.length; b++) {
            var comp = slm.buttonToCompMap[boxesOn[b]];
            $('#' + comp).parents().filter(':hidden').show();
            $('#' + comp).show();
        }
        slm.smartResize();
    }

    slm.onhide = function() {
        slm.draw();
    }

    slm.smartResize = function() {
        var v = $('#videoDiv');
        var t = $('#traceDiv');
        var m = $('#metaDiv');
        var r = $('#standardRemoteDiv');
        var mr = $('#miniRemoteComp');
        var w = $(window).width();

        if (h(v) && h(t) && h(m)) {
            /** Everything in the vmt is hidden so hide the entire div, the expand the remotes to 100% */
            $('#vmt').hide();
            r.css('width', '100%');
            mr.css('height', '100%');
        } else {
            /** Define the width percentages for both the vmt div and the standard remote div
             * Also define the height percentages between the vmt div and the mini remote div */
            var vmtWidth = h(r) ? 100 : 75;
            var vmtHeight = h(mr) ? 100 : 65;
            $('#vmt').css('width', vmtWidth + '%');
            $('#vmt').css('height', vmtHeight + '%');
            r.css('width', (95 - vmtWidth) + '%');
            mr.css('height', (100 - vmtHeight) + '%');
        }

        /** Define the relationship between video and metadata inside the vm div based on which are hidden */
        var videoWidth = h(m) ? 100 : 75;
        v.css('width', videoWidth + '%');
        var metadataWidth = h(v) ? 100 : 25;
        m.css('width', metadataWidth + '%');

        if (h(v) && h(m)) {
            /** If both video and metadata are hidden then hide the entire
             * vm div and expand trace to take up all of vmt */
            $('#vm').hide();
            t.css('height', '100%');
        } else {
            /**
             * Define the proportions between vm and the trace div inside of vmt
             */
            var vmHeight = h(t) ? 100 : 65;
            t.css('height', (100 - vmHeight) + '%');
            $('#vm').css('height', vmHeight + '%');
        }

        /** Resize the components according to the new proportions */
        slm.draw();
    }

    slm.draw = function() {
        if (slm.toolbar != null) {
            slm.toolbar.draw();
        }
        if (slm.menuBox != null) {
            slm.menuBox.draw();
        }
        for (var i = 0; i < slm.overlays.length; i++) {
            slm.overlays[i].draw();
        }
        var toolbarHeight = (hw.hidden ? 0 : toolbar.rows * toolbar.height) + 40;
        $('#toolbar').height(toolbarHeight);
        var notSupportedHeight = $('#notsupported').length > 0 ? $('#notsupported').height() : 0;
        $('#mainDiv').height($(window).height() - toolbarHeight - notSupportedHeight);
        $('#remoteMiniDiv').height($('#miniRemoteComp').height() - $('#anyKeyDiv').height() - 5);
        slm.sizeRemote();
        slm.sizeVideo();
        LayoutUtil.centerInParent($("#powerPrompt"));
        $(".traceTab").height($(".traceComp").height() - $(".traceBar").height());
        $(".serialCmd").width($(".serialCmd").parent().width() - $(".sendSerialBtn").width() - $(".flushBufferBtn").width() - $(".hexContainer").width() - 80);
    }

    slm.sizeRemote = function() {
        LayoutUtil.fitElementMaintainAspectRatio($('#remoteMini'), MINI_REMOTE_ASPECT_RATIO, 1, 1);
        LayoutUtil.fitElementMaintainAspectRatio($('#remote'), STD_REMOTE_ASPECT_RATIO, 1, 1);
        $('#remote').css('left', ($('#remote').parent().width()/2) - ($('#remote').width()/2));

        var akdw = $('#remoteMini').width();
        $('#anyKeyDiv').css('width', akdw);
        var marginLeft = (($('#anyKeyDiv').parent().width() / 2) - (akdw/2)) + 'px';
        $('#anyKeyDiv').css('margin-left', marginLeft);
        $('#remoteMini').css('margin-left', marginLeft);
    }

    slm.sizeVideo = function() {
        LayoutUtil.fitElementMaintainAspectRatio($('#video'), VIDEO_ASPECT_RATIO, 1, 1);
        /** center video in parent */
        var pWidth = $('#video').parent().width();
        $('#video').css('left', (pWidth/2) - ($('#video').width()/2));
    }

    slm.addOverlay = function(overlay) {
        slm.overlays.push(overlay);
    }

    slm.removeOverlay = function(overlay) {
        overlay.canvas.remove();
        slm.overlays.splice(slm.overlays.indexOf(overlay), 1);
    }

    return slm;
}());

/**
 * Just some short hand for checking if an element is hidden
 * @param ele a jquery element object
 */
function h(ele) {
    return ele.is(':hidden');
}
