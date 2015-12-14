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
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/styles/style.css">
        <title>Dawg House - Edit stb metadata</title>
    </head>
    <body>
        <h1>Editing stb: ${stb.id}</h1>
        <table class="list">
            <thead>
                <tr>
                    <td>name</td>
                    <td>make</td>
                    <td>model</td>
                    <td>plant</td>
                    <td>content</td>
                    <td>ipAddress</td>
                    <td>macAddress</td>
                    <td></td>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>${stb.name}</td>
                    <td>${stb.make}</td>
                    <td>${stb.model}</td>
                    <td>${stb.plant}</td>
                    <td>${stb.content}</td>
                    <td>${stb.ipAddress}</td>
                    <td>${stb.macAddress}</td>
                    <td>Dog Show</td>
                </tr>
            </tbody>
        </table>
        <br />
        <table class="list horiz">
            <thead><tr><td colspan="2">IO Services</td></tr></thead>
            <tbody>
                <tr>
                    <td>IR Service </td>
                    <td><a href="${stb.irServiceUrl}">${stb.irServiceUrl}</a></td>
                </tr>
                <tr>
                    <td>Power Service </td>
                    <td><a href="${stb.powerServiceUrl}">${stb.powerServiceUrl}</a></td>
                </tr>

                <tr>
                    <td>Trace Service </td>
                    <td><a href="${stb.traceServiceUrl}">${stb.traceServiceUrl}</a></td>
                </tr>
                <tr>
                    <td>Video Service </td>
                    <td><a href="${stb.videoSourceUrl}">${stb.videoSourceUrl}</a></td>
                </tr>
            </tbody>
        </table>
        <br />

        <h1>Data:</h1>
        <c:forEach items="${stb.data}" var="data">
            ${data.key}: ${data.value}
            <br />
        </c:forEach>

        <h1>Tags:</h1>
        <c:forEach items="${stb.tags}" var="tags">
            ${tags}
            <br />
        </c:forEach>

    </body>
</html>
