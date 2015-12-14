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
 * Module for creating an ImageWorkspace with references to the UI
 * elements inside the HTML page
 */
var ImageWorkspaceFactory = (function() {

    var module = {};

    module.create = function(canvas, imageId, regionInfoUI) {
        return new ImageWorkspace(canvas, imageId, regionInfoUI);
    };

    return module;
}());

/**
 * Handles the drawing of the ImageWorkspace canvas and the interaction with it
 * @param canvas The canvas to draw on
 * @param imageId The id of the image used to load the image from the dawg-show server
 * @param regionInfoUI The div that contains <input>s for modifying the region info
 * @returns {ImageWorkspace}
 */
function ImageWorkspace(canvas, imageId, regionInfoUI) {
    this.canvas = canvas;
    this.regionInfoUI = regionInfoUI;
    this.image = new Image();
    var _this = this;
    this.image.onload = function() {
        canvas.width = this.width;
        canvas.height = this.height;
        _this.draw();
    };
    this.image.src = CXT_PATH + "/video/image?i=" + imageId;

    this.markers = new Array();
    this.startMarker = null;
    this.mouseX = 0;
    this.mouseY = 0;
    this.draggedInInitial = false;

    /**
     * Sets the region. If the region is initialized then it will create the markers
     * @param region the Region object
     */
    this.setRegion = function(region) {
        this.markers = new Array();
        this.startMarker = null;
        if ((region != null) && region.isInitialized()) {
            this.markers.push(new Marker(canvas, region.x, region.y));
            this.markers.push(new Marker(canvas, region.x + region.width, region.y));
            this.markers.push(new Marker(canvas, region.x, region.y + region.height));
            this.markers.push(new Marker(canvas, region.x + region.width, region.y + region.height));
        }
        this.draw();
    }

    /**
     * Creates the intial rectangle based on mouse clicks
     * @param e the mouse event
     */
    this.createInitialRect = function(e) {
        if (!regionInfoUI.is(':hidden')) {
            if ((_this.startMarker == null) || (_this.markers.length == 0)) {
                /** Click function only sets up intial rect */
                if (_this.startMarker == null) {
                    /** Create the initial point, the mouse will be the other point of the rect */
                    var point = _this.getRelPos(e);
                    _this.startMarker = new Marker(canvas, point.x, point.y);
                    _this.mouseX = point.x;
                    _this.mouseY = point.y;
                } else {
                    /** Completed the initial rect, now populate all the markers */
                    var point = _this.getRelPos(e);
                    var xi = _this.startMarker.cx;
                    var yi = _this.startMarker.cy;
                    var rectX = Math.min(xi, point.x);
                    var rectY = Math.min(yi, point.y);
                    var rectW = Math.abs(xi - point.x);
                    var rectH = Math.abs(yi - point.y);

                    _this.markers[0] = new Marker(canvas, rectX, rectY);
                    _this.markers[1] = new Marker(canvas, rectX + rectW, rectY);
                    _this.markers[2] = new Marker(canvas, rectX + rectW, rectY + rectH);
                    _this.markers[3] = new Marker(canvas, rectX, rectY + rectH);
                }
                _this.draw();
            }
        }
    }

    /**
     * Helper function to get the x y coordinates of a mouse click inside
     * of the canvas
     */
    this.getRelPos = function(e) {
        var parentOffset = $(canvas).offset();
        var relX = e.pageX - parentOffset.left;
        var relY = e.pageY - parentOffset.top;
        return {x : Math.round(relX), y : Math.round(relY)};
    }

    $(canvas).mousedown(function (e) {
        if ((_this.startMarker != null) && (_this.markers.length != 0)) {
            /** Only process dragging if the initial rect is set up */
            var point = _this.getRelPos(e);
            for (var i = 0; i < 4; i++) {
                /** Check all markers to see if we clicked one and then mark it as being dragged */
                if (_this.markers[i].didClick(point.x, point.y)) {
                    _this.markers[i].dragging = true;
                    break;
                }
            }
        } else {
            _this.createInitialRect(e);
        }
    });

    $(canvas).mouseup(function (e) {
        if ((_this.startMarker != null) && (_this.markers.length != 0)) {
            /** Mark all markers as no longer being dragged */
            for (var i = 0; i < 4; i++) {
                _this.markers[i].dragging = false;
            }
        } else if (_this.draggedInInitial) {
            _this.createInitialRect(e);
        }
    });

    $(canvas).mousemove(function (e) {
        var point = _this.getRelPos(e);
        if ((_this.startMarker != null) && (_this.markers.length != 0)) {
            /** Only perform drag if a marker was clicked on before */
            var isDragged = false;
            for (var i = 0; i < 4; i++) {
                /** Determine if any markers are currently being dragged */
                if (_this.markers[i].dragging) {
                    isDragged = true;
                    break;
                }
            }
            if (isDragged) {
                _this.sortMarkers();
                var di;
                for (var i = 0; i < 4; i++) {
                    /** Get the marker that was being dragged */
                    if (_this.markers[i].dragging) {
                        di = i;
                        break;
                    }
                }
                /** Sort the markers array so we can easily find who the neighbors are.
                 *  Move the marker that is on the same x-axis up and down with the
                 *  one being dragged. Move the marker that is on the same y-axis
                 *  left and right with the one being dragged
                 */
                var xAxis = (di == 0) || (di == 2) ? di + 1 : di - 1;
                var yAxis = (di == 0) || (di == 1) ? di + 2 : di - 2;
                _this.markers[di].setPosition(point.x, point.y);
                _this.markers[yAxis].setPosition(point.x,_this.markers[yAxis].cy);
                _this.markers[xAxis].setPosition(_this.markers[xAxis].cx, point.y);
                _this.draw();
            }
        } else if ((_this.startMarker != null) && (_this.markers.length == 0)) {
            /** If we have made the initial point but have
             * not completed the initial rect then draw the rectangle up until
             * the current mouse position
             */
            _this.draggedInInitial = true;
            _this.mouseX = point.x;
            _this.mouseY = point.y;
            _this.draw();
        }

    });

    /**
     * Performs all draw operations for the workspace
     */
    this.draw = function() {
        var cxt = canvas.getContext('2d');

        cxt.clearRect(0, 0, canvas.width, canvas.height);
        /** Draws the snapped image */
        cxt.drawImage(this.image, 0, 0);

        if ((this.startMarker != null) || (this.markers.length != 0)) {
            /** If we have at least make the initial point, draw the rect */
            var l = Number.MAX_VALUE;
            var r = Number.MIN_VALUE;
            var t = Number.MAX_VALUE;
            var b = Number.MIN_VALUE;

            /** Find the bounds of the rect */
            if (this.markers.length != 0) {
                /** If we have four markers find the bounds according to them */
                for (var i = 0; i < this.markers.length; i++) {
                    l = Math.min(l, this.markers[i].cx);
                    r = Math.max(r, this.markers[i].cx);
                    t = Math.min(t, this.markers[i].cy);
                    b = Math.max(b, this.markers[i].cy);
                }
            } else {

                /** If we have the initial point but no markers, then use the initial
                 * point and the mouse to find the bounds
                 */
                l = Math.min(this.startMarker.cx, this.mouseX);
                r = Math.max(this.startMarker.cx, this.mouseX);
                t = Math.min(this.startMarker.cy, this.mouseY);
                b = Math.max(this.startMarker.cy, this.mouseY);
            }

            /** Fill the rectangle with a transparent fill */
            cxt.fillStyle = "#ffffff";
            cxt.strokeStyle = "#000000";
            cxt.globalAlpha = 0.5;
            cxt.fillRect(l, t, r - l, b - t);
            cxt.globalAlpha = 1;
            cxt.lineWidth = 1;
            cxt.strokeRect(l, t, r - l, b - t);

            /** Update the regionInfoUI with the new bounds of the rect */
            $('.regionX', regionInfoUI).val(l);
            $('.regionY', regionInfoUI).val(t);
            $('.regionW', regionInfoUI).val(r - l);
            $('.regionH', regionInfoUI).val(b - t);
        }
        /** Draw all the markers */
        for (var i = 0; i < this.markers.length; i++) {
            this.markers[i].draw();
        }
    };

    /**
     * Sorts the markers so we can easily know which marker represents which
     * corner of the rect. The markers will be sorted as:
     * 0 : Upper Left
     * 1 : Upper Right
     * 2 : Bottom Left
     * 3 : Bottom Right
     */
    this.sortMarkers = function() {
        var tmp = [];

        var minX = Number.MAX_VALUE;
        var minY = Number.MAX_VALUE;

        var found = -1;

        for (var i = 0; i < _this.markers.length; i++) {
            var x = _this.markers[i].cx;
            var y = _this.markers[i].cy;
            //get upper left
            if ((x <= minX) && (y <= minY)) {
                minX = x;
                minY = y;
                found = i;
            }
        }
        /** We remove this marker from the original array
         * so that if two markers are exactly on top
         * of each other we won't use the same marker twice
         */
        tmp[0] = _this.markers.splice(found, 1)[0];

        minY = Number.MAX_VALUE;
        var maxX = Number.MIN_VALUE;

        found = -1;
        for (var i = 0; i < _this.markers.length; i++) {
            var x = _this.markers[i].cx;
            var y = _this.markers[i].cy;
            //get upper right
            if ((x >= maxX) && (y <= minY)) {
                maxX = x;
                minY = y;
                found = i;
            }
        }
        tmp[1] = _this.markers.splice(found, 1)[0];

        minX = Number.MAX_VALUE;

        found = -1;
        for (var i = 0; i < _this.markers.length; i++) {
            var x = _this.markers[i].cx;
            //get lower left
            if (x <= minX) {
                minX = x;
                found = i;
            }
        }
        tmp[2] = _this.markers.splice(found, 1)[0];
        tmp[3] = _this.markers[0];
        _this.markers = tmp;
    }

    /**
     * Called when the user makes a change to the x value in the regionInfoUI.
     */
    $('.regionX', regionInfoUI).blur(function() {
        if (_this.markers.length != 0) {
            _this.sortMarkers();

            var newVal = parseInt($(this).val());
            /** Cannot go below 0 and cannot make the region go off the canvas */
            newVal = newVal < 0 ? 0 : newVal;
            var maxX = canvas.width - (_this.markers[1].cx - _this.markers[0].cx);
            newVal = newVal > maxX ? maxX : newVal;
            $(this).val(newVal);

            var dx = newVal - _this.markers[0].cx;
            for (var i = 0; i < _this.markers.length; i++) {
                /** Move every marker the change in the x value */
                _this.markers[i].cx += dx;
            }
            _this.draw();
        } else {
            _this.checkCreateNewMarkers();
        }
    });

    $('.regionY', regionInfoUI).blur(function() {
        if (_this.markers.length != 0) {
            _this.sortMarkers();
            var newVal = parseInt($(this).val());
            /** Cannot go below 0 and cannot make the region go off the canvas */
            newVal = newVal < 0 ? 0 : newVal;
            var maxY = canvas.height - (_this.markers[2].cy - _this.markers[0].cy);
            newVal = newVal > maxY ? maxY : newVal;
            $(this).val(newVal);

            var dy =  newVal - _this.markers[0].cy;
            for (var i = 0; i < _this.markers.length; i++) {
                /** Move every marker the change in the y value */
                _this.markers[i].cy += dy;
            }
            _this.draw();
        } else {
            _this.checkCreateNewMarkers();
        }
    });

    $('.regionW', regionInfoUI).blur(function() {
        if (_this.markers.length != 0) {
            _this.sortMarkers();
            var newVal = parseInt($(this).val());
            /** Cannot go below 0 and cannot make the region go off the canvas */
            newVal = newVal < 0 ? 0 : newVal;
            var maxWidth = canvas.width - _this.markers[0].cx;
            newVal = newVal > maxWidth ? maxWidth : newVal;
            $(this).val(newVal);

            var dw =  newVal - (_this.markers[1].cx - _this.markers[0].cx);
            /** Move the top right and the bottom right markers left or right according
             * to the change in width */
            _this.markers[1].cx += dw;
            _this.markers[3].cx += dw;
            _this.draw();
        } else {
            _this.checkCreateNewMarkers();
        }
    });

    $('.regionH', regionInfoUI).blur(function() {
        if (_this.markers.length != 0) {
            _this.sortMarkers();
            var newVal = parseInt($(this).val());
            newVal = newVal < 0 ? 0 : newVal;
            var maxHeight = canvas.height - _this.markers[0].cy;
            newVal = newVal > maxHeight ? maxHeight : newVal;
            $(this).val(newVal);

            var dh =  newVal - (_this.markers[2].cy - _this.markers[0].cy);
            /** Move the top left and the bottom left markers up or down
             * according to the change in height */
            _this.markers[2].cy += dh;
            _this.markers[3].cy += dh;
            _this.draw();
        } else {
            _this.checkCreateNewMarkers();
        }
    });

    /**
     * Checks if the region UI fields were filled out completely
     * and the markers should now be created
     */
    this.checkCreateNewMarkers = function(regionInfoUI) {
        var x = parseInt($('.regionX', regionInfoUI).val());
        var y = parseInt($('.regionY', regionInfoUI).val());
        var w = parseInt($('.regionW', regionInfoUI).val());
        var h = parseInt($('.regionH', regionInfoUI).val());
        if (!isNaN(x) && !isNaN(y) && !isNaN(w) && !isNaN(h)) {
            RegionDetails.saveRegion(regionInfoUI);
            this.setRegion(RegionDetails.getCurrentRegion());
        }
    }
}

/**
 * Draws a marker, just a simple square
 */
function Marker(canvas, cx, cy) {
    this.canvas = canvas;
    this.cx = cx == null ? 0 : cx;
    this.cy = cy == null ? 0 : cy;
    this.w = 10;
    this.dragging = false;

    /**
     * Does the drawing
     * */
    this.draw = function() {
        var cxt = canvas.getContext('2d');

        cxt.fillStyle = "#ffffff";
        cxt.strokeStyle = "#000000";
        cxt.fillRect(this.cx - this.w/2, this.cy - this.w/2, this.w, this.w);
        cxt.lineWidth = 1;
        cxt.strokeRect(this.cx - this.w/2, this.cy - this.w/2, this.w, this.w);
    }

    /**
     * Sets the position of this marker
     */
    this.setPosition = function(cx, cy) {
        this.cx = cx;
        this.cy = cy;
    }

    /**
     * Checks if the given x,y coordinates are inside this marker
     */
    this.didClick = function(x, y) {
        var l = this.cx - this.w;
        var r = this.cx + this.w;
        var t = this.cy - this.w;
        var b = this.cy + this.w;
        return (x > l) && (x < r) && (y > t) && (y < b);
    }

}
