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