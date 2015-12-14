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
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/styles/style.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/styles/css/font-awesome.min.css">
        <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400' rel='stylesheet" type="text/css">
        <jsp:include page="/WEB-INF/views/globalVars.jsp" />

        <script src="${contextPath}/resources/scripts/jquery-1.11.3.min.js"></script>
        <script src="${contextPath}/resources/scripts/dawg-house.js"></script>

        <title>Dawg House Login</title>
        <script>
            $(function() {
            $('#login').css({
            position : 'absolute',
            top : '50%',
            left : '50%',
            width : '600px', // adjust width
            height : '400px', // adjust height
            zIndex : 1000,
            marginTop : '-150px', // half of height
            marginLeft : '-300px' // half of width
            });
            });
        </script>
        <style type="text/css">
body {
    margin: 0;
    padding: 0;
    background-color: white;
    text-align: center;
    font-size: x-large;
}

.login {
    background-color: white;
}

.mainwrapper {

}

.innerwrap {
    display: table-cell;
    vertical-align: middle;
    text-align: center;
}

.suggestion {
    font-size: medium;
    border: thin;
}
        </style>

    </head>
    <body>
        <a class="notificationRowWhiteColored" href="FIXME-LINK">Help</a>
        <a class="notificationRowWhiteColored separator" href="FIXME-LINK" target="_blank">File a bug</a>
        <section id="login" class="login innerwrap">
            <h2 class="title">Dawg House</h2>
            <div class="label">Please enter your username</div>
            <div class="input">
                <input type="text" id="changeUserInput" autofocus="autofocus" placeholder="login" value="${token}" />
            </div>
            <div class="suggestion" id="badlogin">Usernames may only contain alphanumeric keys.</div>
            <div class="suggestion">
                <p>It is recommended to use your NT login name, but there are no restriction.</p>
                <p>No password is required.</p>
            </div>
        </section>
    </body>
    <%-- Redirects to dawg-house login page, when url contains an invalid token (eg: localhost:8080/dawg-house/User@123). --%>
    <script type="text/javascript">
        DAWGHOUSE.validate('${token}');
    </script>
</html>
