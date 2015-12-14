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

var ModelOverlay = (function() {

    var module = {};

    var caps = new Array();
    var fams = new Array();
    var models = new Array();
    var editing = false;

    module.bind = function(overlayUI) {

        /**
         * Called when the user clicks the + button next to the capability entry field.
         * This check if the capability already exists otherwise it will add it to the list
         * @returns
         */
        $('.bAddCap', overlayUI).click(function() {
            var capInput = $('.addCapInp', overlayUI);
            var newCap = capInput.val();
            if ($.trim(newCap) != '') {
                if ($.inArray(newCap, caps) == -1) {
                    module.addCapability(newCap, true, $('.capList', overlayUI));
                } else {
                    alert(newCap + ' already exists');
                }
            }
            capInput.val('');
            capInput.focus();
        });

        /**
         * When pressing enter inside the textbox, click the button
         */
        $('.addCapInp', overlayUI).keyup(function(event){
            if(event.keyCode == 13){
                $('.bAddCap', overlayUI).click();
            }
        });

        $('.addFamInp', overlayUI).keyup(function(event){
            if(event.keyCode == 13){
                $('.bAddFam', overlayUI).click();
            }
        });

        /**
         * Called when the user clicks the + button next to the family entry method.
         * This will check if it exists and then add it to the drop down menu if it doesn't
         * @returns
         */
        $('.bAddFam', overlayUI).click(function() {
            var famInput = $('.addFamInp', overlayUI);
            var newFamily = famInput.val();
            if ($.trim(newFamily) != '') {
                if ($.inArray(newFamily, fams) == -1) {
                    module.addFamily(newFamily, $('.modelFamily', overlayUI));
                } else {
                    alert(newFamily + ' already exists');
                }
            }
            famInput.val('');
            famInput.focus();
        });


        /**
         * Called when the user clicks the Save button. This will send an ajax post to dawg-house telling it to add
         * the model with the values that were in the input fields.
         * @returns
         */
        $('.bSave', overlayUI).click(function() {
            var modelName = $('.modelName', overlayUI).val();
            if (($.inArray(modelName, models) == -1) || editing) {
                var checkedCaps = new Array();
                $('.capList', overlayUI).find('input:checked').each(function() {
                    checkedCaps.push($(this).attr('id'));
                });
                var selFamily = $('.modelFamily', overlayUI).find(":selected").text();
                var target = CXT_PATH + "/models/" + modelName;
                var payload = {
                        "name" : modelName,
                        "capabilities" : checkedCaps,
                        "family" : selFamily,
                };

                $.ajax({
                      url:target,
                      type:"POST",
                      data:JSON.stringify(payload),
                      contentType:"application/json; charset=utf-8",
                      dataType:"json",
                      success: function() {
                          document.location.href=document.location.href;
                      }
                });
            } else {
                alert(modelName + ' already exists');
            }
        });
    }

    /**
     * Adds the capability to the list of capabilities and then adds it to the array
     * @param newCap The capability to add
     * @returns
     */
    module.addCapability = function(newCap, checked, capList) {
        var newInp = $('<input/>', {
            id : newCap,
            class : 'cb' + newCap,
            type : 'checkbox'
        });
        if (checked) {
            newInp.prop('checked', true);
        }
        var newDiv = $('<div/>', {
            style : "display:inline"
        });
        newDiv.append(newCap);
        capList.append(newInp);
        capList.append(newDiv);
        capList.append($('<br/>'));
        caps.push(newCap);
    }

    /**
     * Adds the family to the drop down menu
     * @param newFamily The family to add
     * @returns
     */
    module.addFamily = function(newFamily, familySelection) {
        var newOption = $('<option/>').prop('selected', true).addClass('opt' + newFamily);
        newOption.append(newFamily);
        familySelection.append(newOption);
        fams.push(newFamily);
    }

    module.addModel = function(model) {
        models.push(model);
    }

    module.setEditing = function(_editing) {
        editing = _editing;
    }

    return module;
}());
