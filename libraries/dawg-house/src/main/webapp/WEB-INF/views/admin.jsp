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
        <link rel="stylesheet"
            href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
            integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
            crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/styles/admin.css">
        <script src="${contextPath}/resources/scripts/jquery-1.11.3.min.js"></script>
        <script src="${contextPath}/resources/scripts/jquery-util.js"></script>
        <script src="${contextPath}/resources/scripts/dawg-pound.js"></script>
        <script src="${contextPath}/resources/scripts/finish-typing.js"></script>
        <script src="${contextPath}/resources/scripts/CreateUser.js"></script>
        <title>Dawg House - Admin</title>
    </head>
    <body style="width:100%;height:100%">
        <c:if test = "${created}">
            <div class="succ-overlay">User successfully created!</div>
        </c:if>
        <img src='<c:url value="/resources/public/images/dawg-logo.png"/>' class="img-responsive dawg-logo" alt="">
        <span class="create-user-title">Create User</span>
        <div class="user-form">
            <form id="form-create-user" >
                <input type="text" class="form-full input-lg" name="userId" placeholder="User Id"></input>
                <span class="available"></span>
                <input type="password" class="form-full input-lg" name="password" placeholder="Password"></input>
                <input type="text" class="form-first input-lg" name="firstName" placeholder="First Name"></input>
                <input type="text"  class="form-last input-lg" name="lastName"placeholder="Last Name"></input>
                <input type="text" class="form-full input-lg" name="mail" placeholder="Email"></input>
                <div class="roles-div">
                    <h4 class="roles-title">Roles</h4>
                    <label class="role-checkbox-label"><input type="checkbox" class="role-checkbox" name="roles" value="ADMIN">ADMIN</label>
                    <label class="role-checkbox-label"><input type="checkbox" class="role-checkbox" name="roles" value="COLLAR">COLLAR</label>
                    <label class="role-checkbox-label"><input type="checkbox" class="role-checkbox" name="roles" value="SHOW">SHOW</label>
                    <label class="role-checkbox-label"><input type="checkbox" class="role-checkbox" name="roles" value="HOUSE">HOUSE</label>
                    <label class="role-checkbox-label"><input type="checkbox" class="role-checkbox" name="roles" value="POUND">POUND</label>
                </div>
                <button type="submit" name="go" class="btn btn-lg btn-primary btn-block create-btn">
                  Create
                </button>
            </form>
        </div>
    </body>

    <script type="text/javascript">
        CreateUser.bind($("#form-create-user"));
    </script>
</html>
