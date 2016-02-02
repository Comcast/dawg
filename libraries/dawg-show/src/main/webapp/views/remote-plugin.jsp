<%--

    Copyright 2010 Comcast Cable Communications Management, LLC

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>


<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false" %>


<html>
    <head>
        <jsp:include page="/views/globalVars.jsp" />
        <link rel="stylesheet" href="https://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css">
        <link rel="stylesheet" href='<c:url value="/css/plugin.css" />'>
        <script type="text/javascript" src='<c:url value="/js/jquery-1.7.2.min.js" />'></script>
        <script type="text/javascript" src='<c:url value="/js/jquery-ui-1.11.4.min.js" />'></script>
        <script type="text/javascript" src='<c:url value="/js/RemotePlugin.js" />'></script>
        <script type="text/javascript" src='<c:url value="/js/PluginConfig.js" />'></script>
    </head>
    <body>
        <div id="components" style="display:none">
            <div class="overlay-config">
                <table class="config-table">
                    <tr><th>Key</th><th>Value</th></tr>
                    <tr class="row-config">
                        <td class="row-config-key">Key</td>
                        <td class="row-config-val">
                            <input type="text" class="row-config-text-val"/>
                        </td>
                    </tr>
                </table>
                <input type="button" class="btn-save-config" value="Save"/>
            </div>
            <div class="overlay-upload">
                <span>Upload a JAR file that contains a DawgPlugin</span><br/><br/>
                <input type="file" class="inp-file"></input>
                <input type="button" value="Upload" class="btn-upload"></input>
            </div>
        </div>
        <div class="header">
            <div class="header-text-container">
                <div class="header-text">Dawg Show Plugins</div>
            </div>
        </div>
        <div id="div-upload">
            <table class="table-plugins plugin-table">
                <tr>
                    <td>Name</td>
                    <td>Class</td>
                    <td>&nbsp;</td>
                    <td><input type="button" class="btn-upload-plugin" value="Upload Plugin"/></td>
                </tr>
                <tr class="row-plugin">
                    <td class="row-plugin-name"></td>
                    <td class="row-plugin-class"></td>
                    <td class="row-plugin-configure">
                        <input type="button" class="btn-configure-plugin" value="Configure" />
                    </td>
                    <td class="row-plugin-delete">
                        <input type="button" class="btn-delete-plugin" value="Delete" />
                    </td>
                </tr>
            </table>
        </div>

        <script type="text/javascript">
            RemotePlugin.bind($('#div-upload'), $('#components'), ${plugins});
        </script>
    </body>
</html>
