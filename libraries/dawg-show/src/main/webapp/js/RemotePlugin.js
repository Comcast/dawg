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

var RemotePlugin = (function() {

    var module = {};
    module.bind = function(uiDiv, components, data) {

        $('.btn-upload-plugin', uiDiv).click(function() {
            var overlay = $('.overlay-upload', components).clone();
            overlay.dialog({
                width: "60%",
                modal: true,
                close: function(event, ui) {
                    $(this).dialog('destroy').remove();
                }
            });
            $('.btn-upload', overlay).click(function() {
                module.readData($('.inp-file', overlay), function(data) {
                    $.ajax({
                          url: CXT_PATH + '/plugins/remote',
                          type: "POST",
                          data: data,
                          contentType:"plain/text; charset=utf-8",
                          dataType:"json",
                          success: function() {
                              document.location.href = document.location.href;
                          }
                    });
                });

            });
        });
        var remotes = Object.keys(data);
        var baseRow = $('.row-plugin', uiDiv);
        for (var i = 0; i < remotes.length; i++) {
            var templateRow = baseRow.clone();
            $('.row-plugin-name', templateRow).text(remotes[i]);
            $('.row-plugin-class', templateRow).text(data[remotes[i]]);
            $('.btn-configure-plugin', templateRow).attr('data-remote', remotes[i]);
            $('.btn-configure-plugin', templateRow).click(function() {
                var remote = $(this).attr('data-remote');

                $.ajax({
                      url: CXT_PATH + '/plugins/remote/cdm/' + remote,
                      type: "GET",
                      contentType:"application/json; charset=utf-8",
                      dataType:"json",
                      success: function(data){
                            var overlay = $('.overlay-config', components).clone();
                            PluginConfig.bind(overlay, data, remote);
                            overlay.dialog({
                                width: "60%",
                                modal: true,
                                close: function(event, ui) {
                                    $(this).dialog('destroy').remove();
                                }
                            });
                      }
                });

            });
            $('.btn-delete-plugin', templateRow).click(function() {
                var remote = $('.row-plugin-name',$(this).parents('tr')).text();
                if (confirm("Are you sure you want to delete the plugin for '" + remote + "'")) {
                    $.ajax({
                          url: CXT_PATH + '/plugins/remote/' + remote,
                          type: "DELETE",
                          success: function(data){
                              document.location.href = document.location.href;
                          }
                    });
                }

            });
            $('.table-plugins', uiDiv).append(templateRow);
        }
        baseRow.remove();
    }

    module.readData = function(inp, callback) {
        var file = $(inp)[0].files[0];
        if (file) {
            var reader = new FileReader();

            /**
             * Once the image has been loaded get the base 64 data and then
             * check if the config has been loaded
             */
            reader.onload = function(e) {
                try {
                    var data = e.target.result;
                    var base64 = data.substring(data.indexOf('base64,')
                            + 'base64,'.length);
                    callback(base64);
                } catch (e) {
                    console.log(e);
                    alert('Unable to load jar');
                }
            }

            reader.readAsDataURL(file);
        }
    }

    return module;
}());
