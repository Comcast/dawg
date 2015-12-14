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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%@ page session="false"%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/styles/style.css">
        <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400' rel='stylesheet" type="text/css">

        <title>Dawg House</title>
    </head>
    <body>
        <form method="get">
            <input type="text" class="text" id="search" value="${searchTerm}" name="q" autofocus>
            <img alt="search" src="${contextPath}/resources/images/search.png" height="22" width="22" />
        </form>
        <table class="list">
            <thead>
                <tr>
                    <td>Name</td>
                    <td>Make</td>
                    <td>Model</td>
                    <td>Plant</td>
                    <td>Content</td>
                    <td>IP Address</td>
                    <td>MAC Address</td>
                    <td>Trace</td>
                    <td>Video</td>
                    <td></td><td></td><td></td>
                    <td></td>
                </tr>
            </thead>
            <tbody>
                <c:if test="${! empty stbList}">
                <c:forEach items="${stbList}" var="stb">
                <tr>
                    <td>${stb.name}</td>
                    <td>${stb.make}</td>
                    <td>${stb.model}</td>
                    <td>${stb.plant}</td>
                    <td>${stb.content}</td>
                    <td>${stb.ipAddress}</td>
                    <td>${stb.macAddress}</td>
                    <td><a href="${stb.traceServiceUrl}">Open</a></td>
                    <td><a href="${stb.videoSourceUrl}">Open</a></td>
                    <td>Delete</td>
                    <td><a href="${contextPath}/home/metaStb/${stb.id}">Edit</a></td>
                    <td><a href="">Dog Show</a><td>
                </tr>
                </c:forEach>
                </c:if>
            </tbody>
        </table>
    </body>
</html>
