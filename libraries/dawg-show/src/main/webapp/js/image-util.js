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
 * Overlays another image over the given image that has its colors inverted. After .2 seconds the
 * inverted image is removed
 * @param img The image to blink
 */
function blink(img) {
    blinkRegion(img, 0, 0, img.width, img.height);
}

function blinkRegion(img, x, y, w, h) {
    if (!img) {
        console.warn("'img' is either null or undefined");
        return;
    }

    if (img instanceof HTMLImageElement) {
        blinkImage(img, x, y, w, h);
    }
    else {
        console.warn("img is neither HTMLImageElement nor HTMLAreaElement");
    }
}

function blinkImage(img, x, y, w, h) {
    var canvasJ = $('<canvas/>');
    var canvas = canvasJ[0];
    canvas.width = img.width;
    canvas.height = img.height;
    canvas.left = img.left;
    canvas.top = img.top;
    var context = canvas.getContext('2d');
    context.drawImage(img, 0, 0, canvas.width, canvas.height);
    var imgd = context.getImageData(Math.floor(x), Math.floor(y), Math.floor(w), Math.floor(h));
    var pix = imgd.data;
    for (var i = 0, n = pix.length; i < n; i += 4) {
        pix[i  ] = 255 - pix[i  ]; // red
        pix[i+1] = 255 - pix[i+1]; // green
        pix[i+2] = 255 - pix[i+2]; // blue
    }

    canvasJ.css('top', $(img).css('top'));
    canvasJ.css('left', $(img).css('left'));
    canvasJ.css('position', $(img).css('position'));
    context.clearRect(0, 0, canvas.width, canvas.height);
    context.putImageData(imgd, x, y);
    $(img).parent().append(canvas);

    setTimeout(function() {canvasJ.remove();}, 200);
}
