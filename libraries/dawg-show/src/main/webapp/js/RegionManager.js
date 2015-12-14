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
 * Object that manages all interaction with the region manager, which is the UI that allows adding, deleting,
 * and editing regions.
 */
var RegionManager = (function() {
    var module = {};
    var regions = new Array();
    var compareType = null;
    var managerUI;
    var workspace;

    /**
     * Called by the html page to bind the code to the UI elements
     * @param regionManagerUI The region manager user interface
     * @param imageWorkspace The ImageWorkspace object that handles all interaction with the snapped image
     * @param imageId The unique identifier of the image in the dawg-show image cache
     * @param operationsUI The html elements that are the buttons for creating new image/ocr comparison
     */
    module.bind = function(regionManagerUI, imageWorkspace, imageId, operationsUI, deviceId) {
        managerUI = regionManagerUI;
        workspace = imageWorkspace;

        /**
         * Click handler for the 'Add Region' button
         * This will check if the region exists in the region list, if not it will add it
         * and then update the region list UI
         */
        $('.btnAddRegion', regionManagerUI).click(function() {
            var rName = regionManagerUI.find('.inpRegionName');
            var regionName = $.trim(rName.val());
            if (regionName != '') {
                var region = module.getRegion(regionName);
                if (region != null) {
                    alert("'" + regionName + "' already exists");
                    rName.val('');
                    return;
                }
                region = new Region(regionName);
                $('.regionListDiv', regionManagerUI).hide();
                $('.regionDetails', regionManagerUI).show();
                var curRegion = RegionDetails.getCurrentRegion();
                if (curRegion != null) {
                    region.name = regionName;
                    var detailsUI = $('.regionManager', regionManagerUI);
                    region.x = RegionDetails.getRegionDetail('X', detailsUI);
                    region.y = RegionDetails.getRegionDetail('Y', detailsUI);
                    region.width = RegionDetails.getRegionDetail('W', detailsUI);
                    region.height = RegionDetails.getRegionDetail('H', detailsUI);
                    region.matchPercent = RegionDetails.getSliderVal('mp', detailsUI);
                    region.yTolerance = RegionDetails.getSliderVal('x', detailsUI);
                    region.xTolerance = RegionDetails.getSliderVal('y', detailsUI);
                    region.redTolerance = RegionDetails.getSliderVal('red', detailsUI);
                    region.greenTolerance = RegionDetails.getSliderVal('green', detailsUI);
                    region.blueTolerance = RegionDetails.getSliderVal('blue', detailsUI);
                    if (module.compareType === "ocr") {
                        region.expectedText = $('.inpExpText').val();
                    } else {
                        delete region.expectedText;
                    }
                }
                regions.push(region);
                module.populateRegionList(regionManagerUI, imageWorkspace);
                rName.val('');
                /** Create html element corresponding to new region  and assign values */
                var newDivRegion = document.createElement("div");
                newDivRegion.id = regionName;
                newDivRegion.innerHTML = module.compareType + ": " + regionName;
                $('.inpName', regionManagerUI).val(regionName);
                newDivRegion.classList.add('regionLabel');
                newDivRegion.value = module.compareType;
                if (module.compareType === "ocr") {
                    newDivRegion.classList.add('ocrRegion');
                } else {
                    newDivRegion.classList.add('icRegion');
                }
                $('#regionTable').append(newDivRegion);
                newDivRegion.addEventListener("click", module.selectedRegionHandler);
                RegionDetails.show(regionManagerUI, imageWorkspace, region, module.compareType);
            }
        });
        /**
         * Click handler for the 'Perform Comparison' button
         * Sends the compare data to the server to perform the comparison on the currently
         * running device
         */
        $('.btnPerformComparison', regionManagerUI).click(function() {
            RegionDetails.saveRegion();
            var curRegion = RegionDetails.getCurrentRegion();
            var regionToCompare = new Array();
            regionToCompare.push(curRegion);

            var compName = $.trim($('.inpCompareName', regionManagerUI).val());
            if (compName == '') {
                alert('You must set a comparison name');
                return;
            }
            if (!curRegion.isInitialized()) {
                alert("'" + curRegion.name + "' has not been initialized.");
                return;
            }
            if (curRegion.expectedText == null){
                delete curRegion.expectedText;
            }

            var payload = {
                regions: regionToCompare,
                imgBase64 : null,
                timeout: RegionDetails.getSliderVal('to', regionManagerUI),
                name: compName
            }
            module.performCompare(payload, regionManagerUI,imageId, deviceId);

        });

        /**
         * Click handler for clicking the 'Export' button
         * This will do a POST to save the compare into the dawg-show compare cache and then
         * open a window to do a GET on the zip file with the compare files
         */
        $('.btnExport', regionManagerUI).click(function() {
            RegionDetails.saveRegion();
            if (regions.length == 0) {
                alert('You need at least one region');
            } else {
                var compare = module.getCompare(regionManagerUI);
                if (compare != null) {

                    $('.config-file-options').dialog({
                            autoOpen : true,
                            width : '35%',
                            modal : true,
                            buttons : {
                                // Export button handler in overlay
                                "Export" : function() {
                                    var exportType = $('input[name=config-radio-group]:checked').val();
                                    var payload = {
                                            regionData : JSON.stringify(compare.regions),
                                            imgData : compare.imgBase64,
                                            compName : compare.name,
                                            type : exportType,
                                            cerealizable : true
                                    }
                                    $.post(CXT_PATH + '/compare/save', payload, function(id) {
                                        window.open(CXT_PATH + '/compare/get?cid=' + id + '&iid=' + imageId + '&type=' + exportType, '_parent');
                                    });

                                    $('.config-file-options').dialog("close");
                                },
                                // Cancel button handler in overlay
                                Cancel : function() {
                                    $('.config-file-options').dialog("close");
                                }
                            },
                            // Default close button for the overlay
                            close : function() {
                                $('.config-file-options').dialog("close");
                            }
                        });
                    }
                }
        });

        /** The timeout slide bar */
        RegionDetails.createSlider(regionManagerUI, 'to', 0, 300, 10, 10, 5);


        /**
         * Click handler for the save image button.
         * It saves the snapped image in jpg format.
         */
        $('.save',operationsUI).click(function() {
            window.open(CXT_PATH + '/video/snapped/save?imgId=' + imageId + '&deviceId=' + deviceId, '_parent');
        });

        /**
         * Click handler for the New Image Compare Button.
         * It creates a new Image Compare region.
         */
        $('.newic', operationsUI).click(function() {
            module.compareType = "ic";
            $('.regionListDiv', regionManagerUI).show();
            if (regions.length != 0) {
                RegionDetails.saveRegion();
            }
            $('.regionDetails', regionManagerUI).hide();
            $('.inpExpText').val('');
            module.show(regionManagerUI, imageWorkspace, 'ic', $.trim($('.inpCompareName', regionManagerUI).val()),(regions.length==0?null:regions));
        });

        /**
         *  Click handler for the New OCR Compare Button.
         *  It creates a new OCR region.
         */
        $('.newocr', operationsUI).click(function() {
             module.compareType = "ocr";
             $('.regionListDiv', regionManagerUI).show();
             if(regions.length !=0) {
                RegionDetails.saveRegion();
            }
             $('.regionDetails', regionManagerUI).hide();
            module.show(regionManagerUI, imageWorkspace, 'ocr', $.trim($('.inpCompareName', regionManagerUI).val()),(regions.length==0?null:regions));
        });

        /**
         *  Click handler for the Test all Image Compare Button.
         *  Sends the compare data to the server to perform the all image region comparison
         *  on the currently running device
         */
        $('.allIcCompare', operationsUI).click(function() {
            RegionDetails.saveRegion();
            if (regions.length == 0) {
                alert('You need at least one region');
                return;
            }
            var regionToCompare = new Array();
            for (var i = 0; i < regions.length; i++) {
                if (regions[i].expectedText == null ) {
                    delete(regions[i].expectedText);
                    regionToCompare.push(regions[i]);
                }
            }
            var compName = $.trim($('.inpCompareName', regionManagerUI).val());
            if (compName == '') {
                alert('You must set a comparison name');
                return;
            } else if (regionToCompare.length == 0) {
                alert('You must need atleast one image compare region');
                return;
            }
            var payload = {
                regions: regionToCompare,
                imgBase64: null,
                timeout: RegionDetails.getSliderVal('to', regionManagerUI),
                name: compName
            }
            module.performCompare(payload, regionManagerUI, imageId, deviceId);

        });

    };

    /**
     * Gets a Compare object from the regions, and compare fields
     */
    module.getCompare = function(regionManagerUI) {
        /** First check that there are no regions that do not have bounds defined */
        for (var r = 0; r < regions.length; r++) {
            if (!regions[r].isInitialized()) {
                alert("'" + regions[r].name + "' has not been initialized.");
                return;
            }
        }
        for (var r = 0; r < regions.length; r++) {
                if(regions[r].expectedText == null){
                delete regions[r].expectedText;
            }
        }
        var compName = $.trim($('.inpCompareName', regionManagerUI).val());
        if (compName == '') {
            alert('You must set a comparison name');
            return;
        }
        var payload = {
                regions: regions,
                imgBase64 : null,
                timeout: RegionDetails.getSliderVal('to', regionManagerUI),
                name: compName
        }
        return payload;
    }

    /** Sends the comparison data to the server to perform comparison
     * in the currently running device.
     * @param comparePayload payload to perform compare
     * @param regionManagerUI Region manager user interface
     * @param imageId The unique identifier of the image in the dawg-show image cache
     * @param deviceId The currently running device Id.
     */
    module.performCompare = function(comparePayload, regionManagerUI,imageId, deviceId) {
        var payload = {
            compareJson : JSON.stringify(comparePayload),
            iid : imageId,
            deviceId : deviceId
        };
        var success = function(result) {
            $('.compareResult', regionManagerUI).css('background-color', result ? 'green' : 'red');
            $('.compareResult', regionManagerUI).html(result ? 'PASS' : 'FAIL');
        }
        var error = function(result) {
            $('.compareResult', regionManagerUI).css('background-color', 'brown');
            $('.compareResult', regionManagerUI).html('SYSTEM FAILURE');
        }

        $('.compareResult', regionManagerUI).show();
        $('.compareResult', regionManagerUI).css('background-color', 'yellow');
        $('.compareResult', regionManagerUI).html('TESTING');
        $('.compareResult', regionManagerUI).css('display', 'inline-block');

        $.ajax({
            type: "POST",
            url: CXT_PATH + '/compare/perform',
            data: payload,
            success: success,
            error: error
            });
        }

    /**
     * Starts a new comparison
     * @param regionManagerUI The html elements for the region manager
     * @param imageWorkspace the ImageWorkspace object that displays the regions on the image
     * @param compType type of comparison (ic or ocr)
     * @param compName Name of the comparison
     * @param regionList list containing ic and ocr regions
     */
    module.show = function(regionManagerUI, imageWorkspace, compType, compName, regionList) {
            $('.compareResult', regionManagerUI).css('background-color', 'transparent');
            $('.compareResult', regionManagerUI).html('');
            if(regionList != null){
                regions = regionList;
            } else {
                regions = new Array();
            }
            var initialRegion = null;
            if (compType == 'ocr') {
                /**
                 * OCR does not have a region list because there can only be one region in an OCR comparison
                 * Create an initial region just to get it started
                 */
                initialRegion = new Region('');
                /** Show the uninitialized region in the details section */
                RegionDetails.show(regionManagerUI, imageWorkspace, initialRegion, compType);
                $('.btnDelete', regionManagerUI).show();
            } else {
                /**
                 * For image comparison, only show the regionList. Region details are shown when
                 * an item in the region list is selected
                 */
                RegionDetails.setCurrentRegion(null);
                $('.regionListDiv', regionManagerUI).show();
                initialRegion = new Region('');
                RegionDetails.show(regionManagerUI, imageWorkspace, initialRegion, compType);
                $('.btnDelete', regionManagerUI).show();
            }

            imageWorkspace.setRegion(initialRegion);
            module.populateRegionList(regionManagerUI, imageWorkspace);
            module.compareType = compType;
            $('.inpCompareName', regionManagerUI).val(compName);
            regionManagerUI.show();
    }

    /**
     * Used the regions variable to populate the list and then set the class of the selected
     * region to the current region in RegionDetails
     * @param regionManagerUI The html elements for the region manager
     * @param imageWorkspace the ImageWorkspace object that displays the regions on the image
     */
    module.populateRegionList = function(regionManagerUI, imageWorkspace) {
        var rList = $('.regionTable', regionManagerUI);
        rList.html('');
        for (var r = 0; r < regions.length; r++) {
            var rDiv = $('<div/>').prop('id', 'region' + regions[r].name).addClass('regionListItem');
            /** Determine if it is the region that is currently displayed in the region details */
            var curRegion = RegionDetails.getCurrentRegion();
            var isCurrent = (curRegion != null) && (regions[r].name == curRegion.name);
            rDiv.addClass('regionListItem' +  (isCurrent ? 'Selected' : 'Unselected'));
            rDiv.append(regions[r].name);
            rDiv.off('click');
            rDiv.click(function() {
                var regionName = $(this).html();
                var region = module.getRegion(regionName);
                // first save current
                RegionDetails.saveRegion();
                // repopulate region list in case the name changed
                RegionDetails.show(regionManagerUI, imageWorkspace, region, module.compareType);
                module.populateRegionList(regionManagerUI, imageWorkspace);
            });
            rList.append(rDiv);
        }
    }

    /**
     * Sets the regions
     */
    module.setRegions = function(_regions) {
        regions = _regions;
        for (var i = 0; i < regions.length; i++) {
            var newRegion = document.createElement("div");
            var region = regions[i];
            newRegion.id = region.name;
            newRegion.classList.add('regionLabel');
            if (region.expectedText == null) {
                module.compareType = 'ic';
                newRegion.classList.add('icRegion');
            } else {
                module.compareType = 'ocr';
                newRegion.classList.add('ocrRegion');
            }
            newRegion.innerHTML = module.compareType + ": " + region.name;
            newRegion.value = module.compareType;
            document.getElementById('regionTable').appendChild(newRegion);
            newRegion.addEventListener("click", module.selectedRegionHandler);
        }
    }

    /**
     * Click handler for regions in the region table.
     * It loads the region with saved values.
     */
    module.selectedRegionHandler = function() {
        RegionDetails.saveRegion();
        $('.inpExpText').val('');
        var regionName = $(this).text().substr($(this).text().indexOf(": ") + 2);
        var region = module.getRegion(regionName);
        var val = $(this).val();
        RegionDetails.show(managerUI, workspace, region, val);
        $('.regionDetails', managerUI).show();
        $('.regionListDiv', managerUI).hide();
        $('.compareResult', managerUI).css('background-color', 'transparent');
        $('.compareResult', managerUI).html('');
    }

    /**
     * Gets the region by name. Return null if there
     * is no region by that name
     */
    module.getRegion = function(regionName) {
        for (var r = 0; r < regions.length; r++) {
            if (regionName == regions[r].name) {
                return regions[r];
            }
        }
        return null;
    }

    /**
     * Deletes a region with the given name
     */
    module.deleteRegion = function(regionName) {
        $($('#' + regionName), $('#regionTable')).remove();
        for ( var r = 0; r < regions.length; r++) {
            if (regionName == regions[r].name) {
                regions.splice(r, 1);
                return;
            }
        }
    }

    return module;
}());

/**
 * Handles interaction with the container that displays the details of a region
 */
var RegionDetails = (function() {

    var module = {};
    module.curRegion = null;

    /**
     * Shows the region details UI and populates the values
     */
    module.show = function(regionManagerUI, imageWorkspace, region, compType) {
        var detailsUI = $('.regionManager', regionManagerUI);
        $('.inpName').val(region.name);
        $('.regionX', detailsUI).val(region.x);
        $('.regionY', detailsUI).val(region.y);
        $('.regionW', detailsUI).val(region.width);
        $('.regionH', detailsUI).val(region.height);

        module.createSlider(detailsUI, 'mp', 0, 100, region.matchPercent, 75, 1);
        module.createSlider(detailsUI, 'x', 0, 20, region.xTolerance, 0, 1);
        module.createSlider(detailsUI, 'y', 0, 20, region.yTolerance, 0, 1);
        module.createSlider(detailsUI, 'red', 0, 255, region.redTolerance, 60, 1);
        module.createSlider(detailsUI, 'green', 0, 255, region.greenTolerance, 40, 1);
        module.createSlider(detailsUI, 'blue', 0, 255, region.blueTolerance, 40, 1);
        if(compType === "ocr") {
            $('.inpExpText').val(region.expectedText);
        }
        imgWorkspace.setRegion(region);

        /**
         * The click handler for deteting a region
         */
        $('.btnDelete', detailsUI).off('click');
        $('.btnDelete', detailsUI).click(function() {
            if (confirm("Are you sure you want to delete '" + region.name + "'")) {
                RegionManager.deleteRegion(region.name);
                $('.regionDetails',detailsUI).hide();
                imageWorkspace.setRegion(null);
            }
        });

        /**
         * The handler when the name of the region is changed
         */
        $('.inpName', detailsUI).off('blur');
        $('.inpName', detailsUI).blur(function() {
            var reg = RegionManager.getRegion($(this).val());
            var element = document.getElementById(module.curRegion.name);
            /** First check if we are not changing the name to a region that already exists */
            if (reg != null && (reg != module.curRegion)) {
                alert("'" + $(this).val() + "' already exists");
                $(this).val(region.name);
                return;
            }
            element.id = $('.inpName', detailsUI).val();
            element.innerHTML = compType + ": " + $('.inpName', detailsUI).val();
            module.saveRegion();
            /** Since the name of the region changed we need to repopulate the list */
            RegionManager.populateRegionList(regionManagerUI, imageWorkspace);
        });

        module.curRegion = region;

        /**
         * Determine which details should be shown or hidden based on if
         * the compare type is ocr or image
         */
        var details = new Array();
        details['ocr'] = ['.divExpText'];
        details['ic'] = ['.redSliderDiv', '.blueSliderDiv', '.greenSliderDiv'];
        var detailsShow = details[compType];
        var detailsHide = details[compType == 'ocr' ? 'ic' : 'ocr'];

        for (var i = 0; i < detailsShow.length; i++) {
            $(detailsShow[i], detailsUI).show();
        }
        for (var i = 0; i < detailsHide.length; i++) {
            $(detailsHide[i], detailsUI).hide();
        }

        detailsUI.show();
    };

    /**
     * Helper method for creating the jquery UI element of a slider
     * @param detailsUI The div that holds all the region details
     * @param the name of the class of the slide we are creating
     * @param min the minimum number the slider can reach
     * @param max the maximum number the slider can reach
     * @param initial the value that it should be set to initially
     * @param defaultVal the value used if initial is set to null
     * @param step the value that the slider should step up increments
     */
    module.createSlider = function(detailsUI, sliderClass, min, max, initial, defaultVal, step) {
        var val = initial == null ? defaultVal : initial;
        var sClass = '.' + sliderClass + 'slider';
        var sValClass = sClass + 'Val';
        $(sClass, detailsUI).slider({
            min: min,
            max: max,
            value: val,
            step: step,
            slide: function(event, ui) {
                /** Changes the val div to the value of the slider */
                $(sValClass, detailsUI).html(ui.value);
            }
        });
        $(sValClass, detailsUI).html($(sClass, detailsUI).slider('value'));
    }

    /**
     * Saves the region. This will take all values in the UI elements and put them in
     * the javascript object of the current region
     * @param detailsUI The div that holds all the region details
     */
    module.saveRegion = function(detailsUI) {
        var cr = module.curRegion;
        if (cr != null) {
            cr.name = $('.inpName').val();
            cr.x = module.getRegionDetail('X', detailsUI);
            cr.y = module.getRegionDetail('Y', detailsUI);
            cr.width = module.getRegionDetail('W', detailsUI);
            cr.height = module.getRegionDetail('H', detailsUI);

            cr.matchPercent = module.getSliderVal('mp', detailsUI);
            cr.yTolerance = module.getSliderVal('x', detailsUI);
            cr.xTolerance = module.getSliderVal('y', detailsUI);

            cr.redTolerance = module.getSliderVal('red', detailsUI);
            cr.greenTolerance = module.getSliderVal('green', detailsUI);
            cr.blueTolerance = module.getSliderVal('blue', detailsUI);
            if( cr.expectedText != null) {
                cr.expectedText = $('.inpExpText').val();
            }
        }
    }

    /**
     * Helper method to parse an integer for a region detail
     * @param the name of the detail
     * @param detailsUI The div that holds all the region details
     */
    module.getRegionDetail = function(detail, detailsUI) {
        var val = $('.region' + detail, detailsUI).val();
        var valI = parseInt(val);
        return isNaN(valI) ? null : valI;
    }

    /**
     * Helper method to get an integer from a slider
     * @param slider the name of the slider
     * @param detailsUI The div that holds all the region details
     */
    module.getSliderVal = function(slider, detailsUI) {
        var val = $('.' + slider + 'SliderVal', detailsUI).html();
        var valI = parseInt(val);
        return isNaN(valI) ? null : valI;
    }

    /**
     * Gets the region that is currently selected
     */
    module.getCurrentRegion = function() {
        return module.curRegion;
    }

    /**
     * Sets the region to be selected
     */
    module.setCurrentRegion = function(curRegion) {
        module.curRegion = curRegion;
    }

    return module;
}());

/**
 * Holds all the information for a region
 */
function Region(name) {
    this.name = name;
    this.x = null;
    this.y = null;
    this.width = null;
    this.height = null;

    this.matchPercent = null;
    this.yTolerance = null;
    this.xTolerance = null;
    this.redTolerance = null;
    this.greenTolerance = null;
    this.blueTolerance = null;
    this.expectedText = null;

    /**
     * Checks if this region has bounds
     */
    this.isInitialized = function() {
        return (this.x != null) && (this.y != null) && (this.width != null) && (this.height != null);
    }
}
