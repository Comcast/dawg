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
 * Represents the toolbar for selecting components
 * @param widthPercent The % of the window width that the tool bar should take up
 * @param height The height of each row in the toolbar
 * @param rows The number of rows the tool bar should have
 * @returns {ComponentToolbar}
 */
function ComponentToolbar(widthPercent, height, rows) {
    this.handler = null;
    this.boxes = new Array();
    this.boxTies = new Array();
    this.widthPercent = widthPercent;
    this.height = height;
    this.rows = rows;

    /**
     * Adds a component selection box
     * @param canvasId The id of the canvas to draw the box on
     * @param text the text that is displayed inside the box
     */
    this.addBox = function(canvasId, text) {
        var canvas = document.getElementById(canvasId);
        box = new ComponentBox(text, canvas);
        this.boxes.push(box);
        return box;
    }

    /**
     * Adds a component selection box
     * @param canvasId The id of the canvas to draw the box on
     * @param text the text that is displayed inside the box
     */
    this.addImageBox = function(canvasId, imgSrc) {
        var canvas = document.getElementById(canvasId);
        var box = new ComponentBox(null, canvas);
        box.imgSrc = imgSrc;
        this.boxes.push(box);
        return box;
    }

    /**
     * Adds a box tie
     * A box tie means that only one of the boxes in the given array can be selected at one time.
     * When one is selected, any that are currently selected are deselected
     * @param boxes A list of ComponentBox objects
     */
    this.addBoxTie = function(boxes) {
        this.boxTies.push(boxes);
    }

    /**
     * Draws the toolbar. This will iterate through each of the boxes and draw on their canvases
     */
    this.draw = function() {
        var numPerRow = Math.ceil(this.boxes.length / this.rows);
        var numInFirstRow = this.boxes.length % numPerRow;
        numInFirstRow = numInFirstRow == 0 ? numPerRow : numInFirstRow;
        var boxHeight = this.height;
        var minFontSize = 32;
        for (var i = 0; i < this.boxes.length; i++) {
            /** Iterate through each of the boxes to find the appropriate font size to apply to all boxes */
            var numInCurrentRow = i < numInFirstRow ? numInFirstRow : numPerRow;
            var boxWidth = ((this.widthPercent / 100) * $(window).width()) / numInCurrentRow;
            this.boxes[i].canvas.width = boxWidth;
            this.boxes[i].canvas.height = boxHeight;
            minFontSize = Math.min(this.boxes[i].fitText(), minFontSize);
        }

        for (var i = 0; i < this.boxes.length; i++) {
            /** Draw each of the component selection boxes */
            this.boxes[i].draw(minFontSize);
        }
    }

    /**
     * The onclick handler invoked when any of the canvases are clicked
     * @param canvas The canvas that was clicked
     */
    this.onclick = function(canvas) {
        var boxesOn = new Array();
        for (var i = 0; i < this.boxes.length; i++) {
            if (canvas == this.boxes[i].canvas) {
                /** Find the box that was clicked and invoke its onlick */
                this.boxes[i].onclick();
                for (var j = 0; j < this.boxTies.length; j++) {
                    /** Check if this box was tied to any boxes and then turn them off */
                    var tied = this.boxTies[j];
                    for (var k = 0; k < tied.length; k++) {
                        if (tied[k] == this.boxes[i]) {
                            for (var p = 0; p < tied.length; p++) {
                                if (tied[p] != this.boxes[i]) {
                                    tied[p].off();
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            }
        }
        for (var i = 0; i < this.boxes.length; i++) {
            /** Find all the boxes that are on */
            if (this.boxes[i].componentLight.on) {
                boxesOn.push(this.boxes[i].canvas.id);
            }
        }
        /** Invoke the callback handler if there is one */
        if (this.handler != null) {
            this.handler.handle(boxesOn);
        }
    }

}

/**
 * Represents a single component selecting box
 * @param text The text to appear in the box
 * @param canvas The canvas to draw the box on
 * @returns {ComponentBox}
 */
function ComponentBox(text, canvas) {
    this.text = text;
    this.canvas = canvas;
    this.w = 0;
    this.h = 0;
    this.circR = 0;
    this.paddingX = 0;
    this.textSpace = 0;
    this.imgSrc = null;

    this.lw = 4;
    this.shineW = 6;
    this.componentLight = new ComponentLight(canvas);
    this.shadowedBox = new ShadowedBox(canvas);

    /**
     * Sets some measurement variables with reference to the canvas that pertain to drawing
     */
    this.setDrawVars = function() {
        this.w = canvas.width;
        this.h = canvas.height;
        this.circR = this.h / 6;
        this.paddingX = 30;
        this.componentLight.setPosition(this.paddingX + this.circR, this.h / 2, this.circR);
        this.textSpace =  this.w - 2 * this.circR - 2 * this.lw - 2 * this.shineW - this.paddingX;
    }

    /**
     * Draws the box with the given font size
     * @param fontSize The font size to use for the text
     */
    this.draw = function(fontSize) {
        this.setDrawVars();
        ctx = this.canvas.getContext("2d");

        this.shadowedBox.draw();

        if (this.imgSrc == null) {
            /** Draws the text */
            ctx.textAlign = 'left';
            ctx.textBaseline = 'middle';
            ctx.fillStyle = "#000000";
            ctx.font = fontSize + 'px sans-serif';
            ctx.fillText(this.text, this.paddingX + 2 * this.circR + this.lw + this.shineW, this.h/2);
        } else {
            /** Draw an image */
            var img = new Image();
            var canvas = this.canvas;
            var box = this;
            img.onload = function() {
                var imgW = (3 * box.textSpace) / 4;
                var imgH = box.h/2;
                var aspectRatio = img.width / img.height;
                var containerAr = imgW / imgH;
                if (containerAr < aspectRatio) {
                    /** Need to adjust height */
                    imgH = imgW / aspectRatio;
                } else {
                    /** Need to adjust width */
                    imgW = imgH * aspectRatio;
                }
                var x = (canvas.width/2) - (imgW/2);
                var y = (canvas.height/2) - (imgH/2);
                ctx.drawImage(img, x, y, imgW, imgH);
            };
            img.src = this.imgSrc;
        }

        /** Draw the on/off light */
        this.componentLight.draw();
    }

    /**
     * Iterates through some font sizes to see if they would fit inside the text area.
     * @returns the font size that fits
     */
    this.fitText = function() {
        var ctx = canvas.getContext("2d");
        this.setDrawVars();
        var fontSizes = [30, 25, 20, 14, 12, 10, 8, 6, 4];
        for (var i = 0 ; i < fontSizes.length; i++) {
            ctx.font = fontSizes[i] + 'px sans-serif';
            textDimensions = ctx.measureText(this.text);
            if (textDimensions.width < this.textSpace) {
                return fontSizes[i];
            }
        }
    }

    /**
     * Called when the user clicks this box. Toggles the on/off light
     */
    this.onclick = function() {
        this.componentLight.toggle();
    }

    /**
     * Deselects this box
     */
    this.off = function() {
        this.componentLight.off();
    }

}

/**
 * Represents the on/off light inside the ComponentBox
 * @param canvas The canvas this is being drawn on
 * @returns {ComponentLight}
 */
function ComponentLight(canvas) {
    this.canvas = canvas;
    this.on = false;

    /**
     * Sets the position of the light
     * @param x The x coordinate of the center of the light
     * @param y The y coordinate of the center of the light
     * @param radius The radius of the light
     */
    this.setPosition = function(x, y, radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    /**
     * Draws the light
     */
    this.draw = function() {
        var ctx = this.canvas.getContext("2d");

        /** Creates the color gradient */
        var grd = ctx.createRadialGradient(this.x, this.y, 0, this.x, this.y, this.radius);
        var color = this.on ? '#255514' : '#791616';
        grd.addColorStop(0, '#ffffff');
        grd.addColorStop(1, color);

        /** Actually draws the circle */
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.radius, 0, 2 * Math.PI, false);
        ctx.fillStyle = grd;
        ctx.fill();
        ctx.lineWidth = 3;
        ctx.strokeStyle = '#000000';
        ctx.stroke();
    }

    /**
     * Toggles this light
     */
    this.toggle = function() {
        this.on = !this.on;
        this.draw();
    }

    /**
     * Turns this light off
     */
    this.off = function() {
        this.on = false;
        this.draw();
    }
}

/**
 * Widget that can be clicked to hide the ComponentToolbar
 * @param canvas
 * @returns {HideWidget}
 */
function HideWidget(canvas) {
    this.canvas = canvas;
    this.hidden = false;
    this.callback = null;
    this.arrow = new Arrow(canvas);

    /**
     * Draws the widget
     */
    this.draw = function() {
        var ctx = this.canvas.getContext("2d");
        ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        this.drawCircle();
        this.arrow.draw(!this.hidden);
    }

    /**
     * Draws and fills the half circle
     */
    this.drawCircle = function() {
    	if (this.canvas.height > 0) {
	        var ctx = this.canvas.getContext("2d");
	        var centerX = this.canvas.width / 2;
	        var centerY = 0;
	        var radius = this.canvas.height - 3;
	
	        var grd = ctx.createRadialGradient(centerX, centerY, radius / 2, centerX, centerY, radius);
	        grd.addColorStop(0, '#ffffff');
	        grd.addColorStop(1, '#888888');
	
	        ctx.beginPath();
	        ctx.arc(centerX, centerY, radius , 0, Math.PI, false);
	        ctx.fillStyle = grd;
	        ctx.fill();
	        ctx.lineWidth = 2;
	        ctx.strokeStyle = '#000000';
	        ctx.stroke();
	        ctx.closePath();
    	}
    }

    /**
     * Called when the user clicks the widget. This will animate hiding/showing the ComponentToolbar
     */
    this.onclick = function() {
        var h = $( "#toolbarDiv" ).height();
        var _this = this;
        var newPos = this.hidden ? '0' : '-' + (h + 10);
        if (this.hidden) {
            $( "#toolbarDiv" ).show();
        }
        var funct = function() {
            if (!_this.hidden) {
                $(this).hide();
            }
            _this.animateComplete();
            if (_this.callback != null) {
                _this.callback.onhide();
            }
        }
        var animateProps = { marginTop: newPos };

        $( "#toolbarDiv" ).animate(animateProps, 1000,  funct);
    }

    /**
     * Called when animation finishes hiding the ComponentToolbar. Redraws the canvas to reverse the arrow
     */
    this.animateComplete = function() {
        this.hidden = !this.hidden;
        this.draw();
    }
}

/**
 * Draws the ui element that opens up the menu
 * @param canvas
 * @returns {MenuBox}
 */
function MenuBox(canvas) {
    this.canvas = canvas;
    this.shadowedBox = new ShadowedBox(canvas);
    var aW = .5 * canvas.width;
    var aH = .5 * canvas.height;
    var aX = .25 * canvas.width;
    var aY = .25 * canvas.height;
    this.arrow = new Arrow(canvas, aX, aY, aW, aH);

    this.draw = function() {
        this.shadowedBox.draw();
        this.arrow.draw(false);
    }
}

/**
 * Draws a box that has highlighting on the top and a shadow on the bottom
 * @param canvas
 * @returns {ShadowedBox}
 */
function ShadowedBox(canvas) {
    this.canvas = canvas;
    this.lw = 4;
    this.shineW = 6;

    this.draw = function() {
        var ctx = this.canvas.getContext("2d");
        var w = canvas.width;
        var h = canvas.height;

        /** Draws the actual box */
        ctx.clearRect(0, 0, w, h);
        ctx.fillStyle = "#d8dadc";
        ctx.strokeStyle = "#000000";
        ctx.fillRect(0, 0, w, h);
        ctx.lineWidth = this.lw;
        ctx.strokeRect(this.lw/2, this.lw/2, w - this.lw, h - this.lw);

        /** Applies light shine to the top and left */
        ctx.fillStyle = "#FFFFFF";
        ctx.globalAlpha = 0.5;
        ctx.fillRect(this.lw, this.lw, this.shineW, h - 2 * this.lw);
        ctx.fillRect(this.lw + this.shineW, this.lw, w - 2 * this.lw - this.shineW, this.shineW);

        /** Applies dark shadow to the bottom and right */
        ctx.fillStyle = "#000000";
        ctx.fillRect(w - this.shineW - this.lw, this.lw + this.shineW, this.shineW, h - this.lw - this.shineW);
        ctx.fillRect(this.lw + this.shineW, h - this.shineW - this.lw, w - 2 * this.lw - 2 * this.shineW, this.shineW);
        ctx.globalAlpha = 1;
    }
}

/**
 * Draws an arrow
 */
function Arrow(canvas, x, y, w, h) {
    this.canvas = canvas;
    this.x = x == null ? 0 : x;
    this.y = y == null ? 0 : y;
    this.w = w == null ? canvas.width : w;
    this.h = h == null ? canvas.height : h;

    /**
     * Draws a triangle for an arrow
     * @param up up if the triangle should face up, false if it should face down
     */
    this.draw = function(up) {
        var ctx = this.canvas.getContext("2d");
        var arrowPoint = this.h/6 + this.y;
        var arrowHeight = this.h/2;
        var arrowBase = arrowPoint + arrowHeight;
        var radius = this.h - 3;
        var arrowXOffset = this.w / 2 - radius + this.x;
        if (!up) {
            var tmp = arrowBase;
            arrowBase = arrowPoint;
            arrowPoint = tmp;
        }

        ctx.beginPath();
        ctx.moveTo(arrowXOffset + radius/2, arrowBase);
        ctx.lineTo(arrowXOffset + 3 * radius/2, arrowBase);
        ctx.lineTo(arrowXOffset + radius, arrowPoint);
        ctx.lineTo(arrowXOffset + radius/2, arrowBase);
        ctx.fillStyle = '#000000';
        ctx.fill();
        ctx.lineWidth = 3;
        ctx.closePath();
    }
}
