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
The main page for viewing the stb metadata, trace, remote, and video.
-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@page import="com.comcast.video.dawg.show.ViewConstants"%>
<%@page import="com.comcast.video.dawg.show.key.Remote"%>

<%@page isELIgnored="false" %>

<%
Remote remote = (Remote) request.getAttribute(ViewConstants.REMOTE);
String stdRemotePage = "/views/remotes/" + remote.getImageSubpath() + "/standard/standardremote.jsp";
String miniRemotePage = "/views/remotes/" + remote.getImageSubpath() + "/mini/miniremote.jsp";
%>
