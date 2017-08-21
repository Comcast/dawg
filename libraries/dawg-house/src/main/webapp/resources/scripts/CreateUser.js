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
var CreateUser = (function() {

    var module = {};

    module.bind = function(formUI) {
        $("[name='userId']", formUI).finishTyping(function() {
            var userId = $("[name='userId']", formUI).val();
            if ($.trim(userId) != '') {
                DAWGPOUND.userExists(userId, function(data) {
                    var av = $('.available', formUI);
                    av.removeClass('available-taken available-avail');
                    av.addClass('available-' + (data === true ? 'taken' : 'avail'));
                    av.html(data === true ? 'X Taken' : '&#10004; Available');
                    $('.create-btn', formUI).prop("disabled", data === true);
                })
            }
        });

        formUI.on('submit', function(e) {
            e.preventDefault();
            var data = formUI.serializeObject();
            var uid = data.userId;
            delete data.userId;
            DAWGPOUND.createUser(uid, data, function(data) {
                document.location.href = CXT_PATH + '/admin/view/user?c';
            });
            return false;
        });

        $("input[type='text']",formUI).on('input', function(e){
            /** If any fields are empty, disable the Create Button */
            var dis = false;
            $("input[type='text']", formUI).each(function(key, txt) {
                if ($.trim($(txt).val()) == '') {
                    dis = true;
                }
            });
            $('.create-btn', formUI).prop("disabled", dis);
        });
    }

    return module;
}());