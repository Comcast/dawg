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

            obj[n] = obj[n] === undefined ? v : $.isArray( obj[n] ) ? obj[n].concat( v ) : [ obj[n], v ];
        });

        return obj;
    };

})(jQuery);