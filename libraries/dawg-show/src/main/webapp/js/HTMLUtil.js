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
var HTMLUtil = (function() {

    var htmlUtil = {};
    var iframe = document.createElement("iframe");

    /**
     * Create a form and elements using javascript and do the form post.
     *
     * @param path
     *            url to which the request is to be sent
     * @param payload
     *            data to be sent
     * @param method
     *            http request type - 'get' or 'post'
     */
    htmlUtil.doFormSubmit = function(path, payload, method) {
        method = method || "post"; // If not specified, used POST method

        var form = document.createElement("form");
        form.setAttribute("method", method);
        form.setAttribute("action", path);
        form.target = iframe;

        for ( var key in payload) {
            if (payload.hasOwnProperty(key)) {

                var hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", key);
                var val = payload[key];
                if (val != null) {
                    hiddenField.setAttribute("value", val);
                }
                form.appendChild(hiddenField);
            }
        }

        document.body.appendChild(form);
        form.submit();
        document.body.removeChild(form);
    }

    /**
     *
     * Parses the current url and returns the value of specified parameter
     * @param name parameter to retrieve value
     * @returns value of specified parameter
     */
    htmlUtil.getParameterByName= function(name) {
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }

    /**
     * Method to parse the source URL and remove the parameter name and its
     * value, and returns the modified URL.For eg: if sourceUrl is
     * "http://10.255.218.125/axis-cgi/mjpg/video.cgi?camera=1&fps=8&resolution=1280x1024" ,
     * and parameterName is "fps", the method will return
     * "http://10.255.218.125/axis-cgi/mjpg/video.cgi?camera=1&resolution=1280x1024"
     *
     * @param sourceUrl
     *            the source URL from which need to remove the parameter
     * @param parameters
     *            the array of parameters, which is need to be remove from URL
     */
    htmlUtil.removeParameterByNameFromUrl = function(sourceUrl, parameters) {
        var modifiedUrl = sourceUrl;
        $.each(parameters, function(parameterArrayIndex, parameterName) {
            //Check whether specified parameter is present or not in source URL
            if(-1 != sourceUrl.indexOf(parameterName)){
                var splittedUrl = modifiedUrl.split("?");
                var parametersArray = [];
                //Check whether any parameter is present or not in source URL
                if (2 == splittedUrl.length) {
                    parametersArray = splittedUrl[1].split("&");
                    for (var index in parametersArray) {
                        //Check whether current parameter in parameter array is same as specified parameter
                        if (parameterName === parametersArray[index].split("=")[0]) {
                            parametersArray.splice(index, 1);
                        }
                    }
                    modifiedUrl = splittedUrl[0] + "?" + parametersArray.join("&");
                }
            }
        });
        return modifiedUrl;
    };

    return htmlUtil;
}());
