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
 * Controls Generic remote operations in dawg show multi-view.
 */
var GenericRemoteOps = (function() {

    var genericRemoteOps = {};

    /**
     * Bind the generic remote container.
     * @param generciRemoteContainer the generic remote container div
     */
    genericRemoteOps.bind = function(generciRemoteContainer) {
        /** Method to make a flash effect on button click in generic remote */
        $('td',generciRemoteContainer).on('click', 'a', function() {
            $("a").removeClass('blinkButton');
            $(this).addClass('blinkButton');
            setTimeout(function() {
                $("a").removeClass('blinkButton');
            }, 150);
        });
    };
    return genericRemoteOps;
}());
