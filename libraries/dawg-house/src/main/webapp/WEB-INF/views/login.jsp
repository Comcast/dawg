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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
  <meta charset="utf-8">
  <title>Dawg House</title>
  <link rel="stylesheet"
        href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
        integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
        crossorigin="anonymous">
  <link rel="stylesheet" type="text/css" href='<c:url value="/resources/public/css/login.css" />'>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
</head>
<body>
<div class="container">
  <div class="row">
    <div class="col-md-4 center-block login-form-warp">
      <section class="login-form">
        <form name='loginForm' method="post" action="<c:url value='login' />" role="login">
          <div class="form-title">
            <img src='<c:url value="/resources/public/images/dawg-logo.png"/>' class="img-responsive" alt="">
            Dawg House
          </div>
          <c:if test="${error}">
            <div class="alert alert-danger" role="alert">Invalid credentials</div>
          </c:if>
          <c:if test="${unauthorized}">
            <div class="alert alert-warning" role="alert">You are not authorized to see this page</div>
          </c:if>
          <c:if test="${logout}">
            <div class="alert alert-success" role="alert">you have successfully logged out</div>
          </c:if>
          <c:if test="${!(unauthorized||logout)}">
            <input class="form-control input-lg" name='username' type="text" placeholder="User Name" required/>
            <input class="form-control input-lg" name='password' type="password" placeholder="Password" required/>
            <button type="submit" name="go" class="btn btn-lg btn-primary btn-block">
              Sign in
            </button>
          </c:if>
        </form>
      </section>
    </div>
  </div>
</div>
</body>
</html>