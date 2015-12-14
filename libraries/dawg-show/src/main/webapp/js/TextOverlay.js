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
 * Draws an overlay the area of a canvas with a see through background and text
 * @param canvas The canvas to draw the overlay in
 * @param text The text to display
 * @returns {TextOverlay}
 */
function TextOverlay(canvas, text) {
    this.canvas = canvas;
    this.text = text;

    /**
     * Draws the actual overlay
     */
    this.draw = function() {
        $(canvas).css('width', $(canvas).parent().width());
        $(canvas).css('height', $(canvas).parent().height());
        this.canvas.width = $(canvas).width();
        this.canvas.height = $(canvas).height();
        var ctx = this.canvas.getContext("2d");
        var w = this.canvas.width;
        var h = this.canvas.height;

        /** Draws the actual box */
        ctx.clearRect(0, 0, w, h);
        ctx.fillStyle = "#ffffff";
        ctx.globalAlpha = 0.5;
        ctx.fillRect(0, 0, w, h);
        ctx.globalAlpha = 1;

        /** Draws the text */
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillStyle = "#000000";
        ctx.font = 20 + 'px sans-serif';
        ctx.fillText(this.text, w/2, h/2);
    }
}
