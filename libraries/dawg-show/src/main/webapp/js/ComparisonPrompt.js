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
 * Image/OCR comparison prompter.
 */
var ComparisonPrompt = (function() {
    var prompt = {};

    /**
     * Binds the element essential for comparison.
     *
     * @param loadComparisonPrompt
     *            Comparison prompt element.
     * @param fadeUI
     *            Fade UI element.
     * @param deviceId
     *            Device id of box.
     */
    prompt.bind = function(loadComparisonPrompt, fadeUI, deviceId) {
        prompt.loadComparisonPrompt = loadComparisonPrompt;
        prompt.fadeUI = fadeUI;
        prompt.deviceId = deviceId;

        /**
         * Called when the close image is clicked on the prompt. This will hide
         * the prompt and the white faded overlay
         */
        $('.closeImage', loadComparisonPrompt).unbind("click").click(
                function() {
                    loadComparisonPrompt.hide();
                    fadeUI.hide();
                });

        /**
         * Called when the Load Comparison button is clicked in the load
         * comparison prompt.
         */
        $('.btnLoadComparison', loadComparisonPrompt).unbind("click").click(
                function() {
                    prompt.loadPerformComparison();
                });
    };

    /**
     * This will get the data from the files that are selected and then save
     * that into the dawg-show compare cache. Then it will open up a window that
     * references that compare and load it into the snapped image page.
     */
    prompt.loadPerformComparison = function() {

        if ($(".singleFileSelectionInput").prop("checked")) {
            var configInp = $('.inpConfigFile', prompt.loadComparisonPrompt);
            var imgInp = $('.inpImageFile', prompt.loadComparisonPrompt);
            var configFormat = prompt.getFileFormat(configInp.val());
            if  (!prompt.isConfigFile(configFormat)) {
                alert('Configuration file must be of type json or xml');
                return;
            }
            var loader = prompt.loaderMethod(configInp[0].files[0],
                imgInp[0].files[0], prompt.getFileName(configInp.val()));
            /**
             * Loads both of the files and then calls the callback method
             */
            loader.loadCompare();
        } else if ($(".multiFileSelectionInput").prop("checked")) {
            var files = $(".multipleConfigFilesInput")[0].files;
            var firstFile = files[0];
            var secondFile = files[1];
            var alertMessage = "In multiselect mode,you have to select one configuration file (XML or JSON )and one image file.";
            if (files.length == 2) {
                var uploadedFile1Format = prompt.getFileFormat(firstFile.name);
                var uploadedFile2Format = prompt.getFileFormat(secondFile.name);
                if (!(prompt.isconfigFilePresent(uploadedFile1Format,uploadedFile2Format))) {
                    alert(alertMessage);
                    return;
                } else {
                    if (prompt.isConfigFileFormatEqual(uploadedFile1Format,
                        uploadedFile2Format)) {
                        alert(alertMessage);
                        return;
                    } else
                    if (prompt.isConfigFile(uploadedFile1Format)) {
                        var loader = prompt.loaderMethod(firstFile, secondFile,
                                prompt.getFileName(firstFile.name));
                        loader.loadCompare();
                    } else {
                        var loader = prompt.loaderMethod(secondFile, firstFile,
                                prompt.getFileName(secondFile.name));
                        loader.loadCompare();
                    }
                }
            } else {
                alert(alertMessage);
                return;
            }
        }
    };
    /**
     * This will get the name of file and return the file format extention
     * (Either jpg, XML or JSON).
     */
    prompt.getFileFormat = function(fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    };
    /**
     * Called to check the file presence of configuration file.
     * @return  false - If any of the files are not JSON or XML.
     *          true  - If any of the files is JSON or XML.
     */
    prompt.isconfigFilePresent = function(uploadedFile1Format, uploadedFile2Format) {
        if (!prompt.isConfigFile(uploadedFile1Format) && !prompt.isConfigFile(uploadedFile2Format)) {
            return false;
        } else {
            return true;
        }
    };
    /**
     * Check whether two files are of same type
     * @return  true  - If two configuration files are similar type.
     *          false - If two configuration files are different type.
     */
    prompt.isConfigFileFormatEqual = function(uploadedFile1Format,
            uploadedFile2Format) {
        if (uploadedFile1Format == uploadedFile2Format) {
            return true;
        } else if (prompt.isConfigFile(uploadedFile1Format) && prompt.isConfigFile(uploadedFile2Format)) {
            return true;
        } else {
            return false;
        }
    };
    /**
     * Return the filename without extension
     */
    prompt.getFileName = function(fileName) {
        return fileName.substring(fileName.lastIndexOf('\\') + 1, fileName
                .lastIndexOf('.'));
    };
    /**
     * Check whether the file is JSON or XML
     *
     * @return true - If file is JSON or XML type. false - If file is not JSON type.
     */
    prompt.isConfigFile = function(fileFormat) {
        var configFileFormats = [ 'json', 'xml' ];
        return (configFileFormats.indexOf(fileFormat) != -1) ? true : false;
    };
    prompt.loaderMethod = function(configInp, imgInp, compName) {
        var loader = new CompareLoader(configInp, imgInp, function(configData,
                imgData) {
            var payload = {
                regionData : configData,
                imgData : imgData,
                compName : compName,
                cerealizable : false
            };
            $.post(CXT_PATH + '/compare/save', payload, function(cid) {
                window.open(CXT_PATH + '/video/snapped?cid=' + cid
                        + '&deviceId=' + prompt.deviceId);
                prompt.loadComparisonPrompt.hide();
                prompt.fadeUI.hide();
            });
        })
        return loader;
    };

    /**
     * Called when the load comparison menu item is clicked. This will launch
     * the overlay that prompts the user for the files needed for the comparison
     */
    prompt.loadComparison = function() {
        /** Centers the comparison prompt */
        var left = ($(window).width() - prompt.loadComparisonPrompt.width()) / 2;
        var top = ($(window).height() - prompt.loadComparisonPrompt.height()) / 2;
        prompt.loadComparisonPrompt.css('left', left).css('top', top);
        prompt.loadComparisonPrompt.show();
        prompt.fadeUI.show();
    };
    return prompt;

}());

/**
 * Class to handle loading a compare config and a compare image file
 *
 * @param configFileInp
 *            The input element that references the config file on the client
 * @param imgFileInp
 *            The input element that references the image file on the client
 * @param handler
 *            The handler that is called once both files are loaded
 * @returns {CompareLoader}
 */
function CompareLoader(configFileInp, imgFileInp, handler) {
    this.configFileInp = configFileInp;
    this.imgFileInp = imgFileInp;

    this.handler = handler;
    this.configData = null;
    this.imgData = null;
    this.configLoaded = false;
    this.imgLoaded = false;

    /**
     * Loads both of the images
     */
    this.loadCompare = function() {
        this.readJsonConfig();
        this.readImage();
    }

    /**
     * Loads the config file if it is in the json format
     */
    this.readJsonConfig = function() {
        if (this.configFileInp && this.configFileInp) {
            var reader = new FileReader();

            var _this = this;

            /**
             * Once the file has been loaded then convert the base64 data into
             * json. Then check if the image has been loaded.
             */
            reader.onload = function(e) {
                try {
                    var data = e.target.result;
                    var base64 = data.substring(data.indexOf('base64,')
                            + 'base64,'.length);

                    _this.configData = atob(base64);
                } catch (e) {
                    alert('File did not contain valid comparison data');
                }

                _this.configLoaded = true;
                _this.checkAllLoaded();
            }

            reader.readAsDataURL(this.configFileInp);
        }
    }

    /**
     * Loads in the image data
     */
    this.readImage = function() {
        if (this.imgFileInp && this.imgFileInp) {
            var reader = new FileReader();
            var _this = this;

            /**
             * Once the image has been loaded get the base 64 data and then
             * check if the config has been loaded
             */
            reader.onload = function(e) {
                try {
                    var data = e.target.result;
                    _this.imgData = data.substring(data.indexOf('base64,')
                            + 'base64,'.length);
                } catch (e) {
                    alert('File did not contain valid comparison data');
                }
                _this.imgLoaded = true;
                _this.checkAllLoaded();
            }

            reader.readAsDataURL(this.imgFileInp);
        }
    }

    /**
     * Called when each of the resources finish loading. Then checks if the
     * other one has finished. If it has, then call the handler
     */
    this.checkAllLoaded = function() {
        if (this.configLoaded && this.imgLoaded) {
            this.handler.call(this, this.configData, this.imgData);
        }
    }
}
