/**
 * Function to serialize a form into an object.
 * @param $
 * @param undefined
 */
(function($, undefined) {
    $.fn.serializeObject = function() {
        var obj = {};

        $.each( this.serializeArray(), function(i,o) {
            var n = o.name;
            var v = o.value;

            obj[n] = obj[n] === undefined ? v
                    : $.isArray( obj[n] ) ? obj[n].concat( v )
                            : [ obj[n], v ];
        });

        return obj;
    };

})(jQuery);