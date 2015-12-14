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
 * Manages user interaction with the Multi-View elements
 */
var MultiView = (function() {

    var mv = {};

    /**
     * Binds the multi-view to the page elements
     */
    mv.bind = function() {
        LayoutDelegator.bind(MultiLayoutManager);

        $(".multi-snap-button,.changeRemoteButton").tooltip({
              show: {
                effect: "slideDown",
                delay: 1000
            },
            hide: {
                effect: "puff",
            }
        });

        $('.videoDiv').each(function() {
            /** For each video set it up to have its frames refresh */
            var canvas = $('.video', $(this));
            VideoRenderer.bind(canvas, $(this).attr('data-deviceId'));
            /**
             * Click handler for toggling which trace is visible
             */
            $('.traceToggle', $(this)).click(function() {
                if (!$(this).hasClass('traceToggleSelected')) {
                    /** If clicking one that is not already selected
                     * show the trace window for that device and hide the
                     * one that is already visible */
                    $('.traceDiv:visible').hide(400);
                    var deviceId = $(this).attr('data-deviceId');
                    var traceDiv = $(".traceDiv[data-deviceId='" + deviceId + "']");
                    traceDiv.show(400, function() {
                        var cmd = $(".serialCmd", traceDiv);
                        cmd.width(cmd.parent().width() - $(".sendSerialBtn", traceDiv).width() - $(".flushBufferBtn", traceDiv).width() - $(".hexContainer", traceDiv).width() - 80);

                        var traceTab = $(".traceTab", traceDiv);
                        traceTab.height($(".traceComp", traceDiv).height() - $(".traceBar").height());

                        /** Need to redraw so we can shrink the video space to make room for the trace window */
                        LayoutDelegator.draw();
                    });

                    $('.traceToggleSelected').removeClass('traceToggleSelected').addClass('traceToggleUnselected');
                    $(this).removeClass('traceToggleUnselected').addClass('traceToggleSelected');
                } else {
                    /** If clicking a toggle switch that is already selected, hide the current trace window
                     * since none are selected */
                    $(this).removeClass('traceToggleSelected').addClass('traceToggleUnselected');
                    $('.traceDiv:visible').hide(400, function() {
                        /** Need to redraw so we can expand the video space to fill where the trace window was */
                        LayoutDelegator.draw();
                    });
                }
            });
            $('.multiVisionLoadComparison', $(this)).click(function() {
                var deviceId = $(this).attr('data-deviceId');
                ComparisonPrompt.bind($('#loadComparisonPrompt'), $('#faded'), deviceId);
                ComparisonPrompt.loadComparison();
            });

            $('.multiVisionSnap', $(this)).click(function() {
                VideoSnapper.snapVideo($(this).attr('data-deviceId'));
            });

        });
        /** Start refreshing the video frames */
        VideoRenderer.startVideo();
        /** Start updating the trace window */
        SerialManager.bind();
        /** Draw the layout, so everything is sized correctly */
        LayoutDelegator.draw();
    }

    return mv;
}());
