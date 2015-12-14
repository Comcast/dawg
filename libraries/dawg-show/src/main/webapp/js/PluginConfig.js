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
var PluginConfig = (function() {

    var module = {};
    module.bind = function(uiDiv, data, remoteType) {
        var config = data.config;
        var displayMap = data.displayMap;
        var keys = Object.keys(config);
        var baseRow = $('.row-config', uiDiv);
        for (var i = 0; i < keys.length; i++) {
            var templateRow = baseRow.clone();
            $('.row-config-key', templateRow).text(displayMap[keys[i]]);
            var val = config[keys[i]] == null ? '' : config[keys[i]];
            $('.row-config-text-val', templateRow).val(val).attr('name', keys[i]);
            $('table', uiDiv).append(templateRow);
        }
        baseRow.remove();

        $('.btn-save-config', uiDiv).click(function() {
            var payload = {};
            $('input', $('table', uiDiv)).each(function() {
                var name = $(this).attr('name');
                payload[name] = $(this).val();
            });
            $.ajax({
                  url: CXT_PATH + '/plugins/remote/config/' + remoteType,
                  type: "POST",
                  data: JSON.stringify(payload),
                  contentType:"application/json; charset=utf-8",
                  dataType:"json",
                  success: function() {
                      document.location.href = document.location.href;
                  }
            });
        });
    }

    return module;
}());
