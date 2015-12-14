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
var LayoutDelegator = (function() {

    var ld = {
            delegate : null
    };

    ld.bind = function(delegate) {
        ld.delegate = delegate;
    }

    ld.draw = function() {
        ld.delegate.draw();
    }

    ld.addOverlay = function(overlay) {
        ld.delegate.addOverlay(overlay);
        ld.delegate.draw();
    }

    ld.removeOverlay = function(overlay) {
        ld.delegate.removeOverlay(overlay);
        ld.delegate.draw();
    }

    return ld;
}());
