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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%@ page session="false"%>
<html style="width:100%;height:100%">
    <head>
        <jsp:include page="/WEB-INF/views/globalVars.jsp" />
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/styles/style.css">
        <script src="${contextPath}/resources/scripts/jquery-1.11.3.min.js"></script>
        <script src="${contextPath}/resources/scripts/ModelTable.js"></script>
        <title>Dawg House - Models</title>
    </head>
    <body style="width:100%;height:100%">
        <table id="modelsTable" class="list">
            <thead>
                <tr><th>Name</th><th>Capabilities</th><th>Family</th><th><input class="bAddModel" type="button" value="+"/></th></tr>
            </thead>
            <c:forEach items="${models}" var="model">
            <tr class="modelRow">
                <td class="tModelName">${model.name}</td>
                <td class="tModelCaps">${model.capabilities}</td>
                <td class="tModelFamily">${model.family}</td>
                <td><input type="button" value="-" class="bDeleteModel"/></td>
            </tr>
            </c:forEach>
        </table>
        <div id="overlay" style="display:none;position: fixed; height: 100%; width: 100%;left:0%;top:0%">
            <div class="transparentbg" style="position: fixed; height: 100%; width: 100%;left:0%;top:0%;z-index:40">
                <img src='<c:url value="/resources/images/transpbg.png" />' style="width: 100%; height: 100%;" alt="" />
            </div>
            <div class="addModelDiv" style="width:50%;position:fixed;left:25%;top:10%;z-index:41;border:2px solid">
                <div style="width:100%;height:25px;background-color:#336699;border-bottom:2px solid">
                    <img style="float:right;width:25px;height:25px" src="<c:url value="/resources/images/close.png" />" class="closeImage">
                </div>
                <jsp:include page="addmodel.jsp"></jsp:include>
            </div>
        </div>
    </body>

    <script>
        ModelTable.bind($('#modelsTable'), $('#overlay'));
    </script>
</html>
