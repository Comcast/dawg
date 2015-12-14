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
var MultiFileSelector = (function() {
    var selector = {};
    selector.bind = function(singleFileSelectionInput, multiFileSelectionInput,
        singleFileSelectionDiv, multipleFileSelectionDiv, multiChooseFileInput, fileList) {

        selector.singleFileSelectionDiv = singleFileSelectionDiv;
        selector.multipleFileSelectionDiv = multipleFileSelectionDiv;
        selector.multiChooseFileInput = multiChooseFileInput;
        selector.fileList = fileList;
        singleFileSelectionInput.click(function() {
            selector.showSingle();
        });
        multiFileSelectionInput.click(function() {
            selector.showMulti();
        });
        multiChooseFileInput.change(function() {
            selector.showFiles();
        });
    };
    /**
     * Function to show the multiple file section and hide single file selection
     */
    selector.showMulti = function() {
        selector.singleFileSelectionDiv.hide();
        selector.multipleFileSelectionDiv.show();

    };

    /**
     * Function to show the single file section and hide multiple file selection
     */
    selector.showSingle = function() {
        selector.singleFileSelectionDiv.show();
        selector.multipleFileSelectionDiv.hide();
    };

    /**
     * To list out the name of files that selected for IC and OCR.
     */
    selector.showFiles = function() {
        var files = selector.multiChooseFileInput[0].files;
        var list = selector.fileList[0];
        list.innerHTML = '';
        if (files != null) {
            for (var i = 0; i < files.length; i++) {
                list.innerHTML += files[i].name + '\n';
            }
        }
        if (list.innerHTML == '') {
            list.style.display = 'none';
        } else {
            list.style.display = 'block';
        }
    };
    return selector;
})();