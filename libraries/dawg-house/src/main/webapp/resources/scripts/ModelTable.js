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

var ModelTable = (function() {

    var module = {};

    module.bind = function(tableUI, overlayUI) {

        /**
         * Shows the add model overlay. This will reset all the fields in the prompt
         * @returns
         */
        $('.bAddModel', tableUI).click(function() {

            ModelOverlay.setEditing(false);
            var modelName = $('.modelName', overlayUI);
            modelName.val('');
            modelName.removeAttr('disabled');
            $('.capList', overlayUI).find('input:checked').each(function() {
                $(this).removeAttr('checked');
            });

            overlayUI.show();
            modelName.focus();
        });

        $('.modelRow', tableUI).each(function(index, row) {
            /**
             * Sends an ajax post to dawg-house to delete the given model
             * @param model The name of the model to delete
             * @returns
             */
            $('.bDeleteModel', row).click(function(event) {
                event.stopPropagation();
                var model = $('.tModelName', row).html();
                if (confirm("Are you sure you want to delete '" + model + "'?")) {

                    var target = CXT_PATH + "/models/" + model;

                    $.ajax({
                          url:target,
                          type:"DELETE",
                          contentType:"application/json; charset=utf-8",
                          dataType:"json",
                          success: function() {
                              document.location.href=document.location.href;
                          }
                    });
                }
            });

            /**
             * Shows the edit model overlay and populates it with the given values and disables the name entry
             * @param model The name of the model
             * @param capabilities The list of the capabilities
             * @param family The family
             * @returns
             */
            $(this).click(function() {
                ModelOverlay.setEditing(true);
                var modelName = $('.modelName', overlayUI);
                modelName.val($('.tModelName', $(this)).html());
                modelName.attr('disabled','disabled');
                $('.capList', overlayUI).find('input:checked').each(function() {
                    $(this).removeAttr('checked');
                });
                var caps = $('.tModelCaps', $(this)).html();
                var capabilities = caps.substring(1,caps.length - 1).split(',')
                for (var i = 0; i < capabilities.length; i++) {
                    var cap = $.trim(capabilities[i]);
                    $('.cb' + cap, overlayUI).prop('checked', true);
                }
                var fam = $('.tModelFamily', $(this)).html();
                $('.opt' + fam, overlayUI).prop('selected', true);

                overlayUI.show();
            });
        });

        /**
         * Hides the model overlay
         * @returns
         */
        $('.closeImage', overlayUI).click(function() {
            overlayUI.hide();
        });
    }

    return module;
}());
