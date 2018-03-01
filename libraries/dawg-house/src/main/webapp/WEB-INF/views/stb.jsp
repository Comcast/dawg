+<%--

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
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
        <script src="${contextPath}/resources/scripts/jquery-util.js"></script>
        <script src="${contextPath}/resources/scripts/dawg-house.js"></script>
        <script src="${contextPath}/resources/scripts/finish-typing.js"></script>
        <script src="${contextPath}/resources/scripts/AddStb.js"></script>
        <title>Dawg House - Admin</title>
    </head>
    <body class="container">
        <div class="text-center heading">
    			<img src='<c:url value="/resources/public/images/dawg-logo.png"/>' class="img-responsive dawg-logo" alt="dawg-logo">
    			<div class="heading-text">Add Stb</div>
		</div>
		
		<form class="form-horizontal stb-form" id="form-add-stb">
			<div class="form-group">
				<label for="name" class="col-sm-3 control-label">Name</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" name="name" placeholder="Name" id="name">
				</div>
			</div>
			<div class="form-group">
				<label for="make" class="col-sm-3 control-label">Make</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" name="make" placeholder="Make" id="make">
				</div>
			</div>
			<div class="form-group">
				<label for="model" class="col-sm-3 control-label">Model</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" name="model" placeholder="Model" id="model">
				</div>
			</div>
			<div class="form-group">
				<label for="macAddress" class="col-sm-3 control-label">MAC Address</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" name="macAddress" placeholder="MAC Address" id="macAddress">
				</div>
			</div>
			<div class="form-group">
				<label for="deviceId" class="col-sm-3 control-label">DeviceId</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" name="deviceId" placeholder="DeviceId" id="deviceId">
				</div>
			</div>
			<div class="form-group">
				<label for="ipAddress" class="col-sm-3 control-label">IP Address</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" name="ipAddress" placeholder="IP Address" id="ipAddress">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-8 col-sm-4">
					<button type="submit" class="btn btn-lg btn-primary btn-block" data-loading-text="Submitting..." id="submit-btn">Add STB</button>
				</div>
			</div>
			<div id="alerts"></div>
		</form>
    </body>

    <script type="text/javascript">
    		AddStb.bind($("#form-add-stb"));
    </script>
</html>