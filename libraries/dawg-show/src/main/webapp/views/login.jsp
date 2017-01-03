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
<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
  <meta charset="utf-8">
  <title>Dawg Show</title>
  <link rel="stylesheet"
        href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
        integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
        crossorigin="anonymous">
  <link rel="stylesheet" type="text/css" href='<c:url value="/css/login.css" />'>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
</head>
<body>
<div class="container">
  <div class="row">
    <div class="col-md-4 center-block login-form-warp">
      <section class="login-form">
        <form method="post" action="#" role="login">
          <div class="form-title">
            <img src='<c:url value="/images/dawg-logo.png"/>' class="img-responsive" alt="">
            Dawg Show
          </div>
          <input class="form-control input-lg" type="text" placeholder="User Name" required/>
          <input class="form-control input-lg" type="password" placeholder="Password" required/>
          <ul class="error-list">
            <c:forEach items="${loginErrors}" var="error">
              <li class="text-danger"><span>${error}</span></li>
            </c:forEach>
          </ul>
          <button type="submit" name="go" class="btn btn-lg btn-primary btn-block">
            Sign in
          </button>
        </form>
      </section>
    </div>
  </div>
</div>
</body>
</html>