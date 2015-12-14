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
<!--
This page is returned when an attempt to take snapshot of device in dawgshow multi view
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src='<c:url value="/js/jquery-1.7.2.min.js" />'></script>>
        <script type="text/javascript" src='<c:url value="/js/MultiSnapDownloadManager.js" />'></script>
        <script type="text/javascript" src='<c:url value="/js/HTMLUtil.js" />'></script>
    </head>
    <body>
        <script type="text/javascript">
            MultiSnapDownloadManager.bind('${pageContext.request.contextPath}', ${deviceIdImageIdMap});
        </script>
    </body>
</html>
