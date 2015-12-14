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
The directional pad for the mini remote
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String rem = (String) request.getAttribute("rem");
rem = rem == null ? "s" : rem;
%>

<img id="<%=rem%>up" src='<c:url value="/images/remotes/shared/keys/up.png" />' onmousedown="clickRemoteButton(event,'UP', true)" onmouseup="clickRemoteButton(event,'UP', false)" alt="" style="width:60%;left:20%;position:absolute"/>
<img id="<%=rem%>left" src='<c:url value="/images/remotes/shared/keys/left.png" />' onmousedown="clickRemoteButton(event,'LEFT', true)" onmouseup="clickRemoteButton(event,'LEFT', false)" alt="" style="width:25%;left:0%;top:20%;position:absolute"/>
<img id="<%=rem%>right" src='<c:url value="/images/remotes/shared/keys/right.png" />' onmousedown="clickRemoteButton(event,'RIGHT', true)" onmouseup="clickRemoteButton(event,'RIGHT', false)" alt="" style="width:25%;left:75%;top:20%;position:absolute"/>
<img id="<%=rem%>down" src='<c:url value="/images/remotes/shared/keys/down.png" />' onmousedown="clickRemoteButton(event,'DOWN', true)" onmouseup="clickRemoteButton(event,'DOWN', false)" alt="" style="width:60%;left:20%;top:70%;position:absolute;"/>
<img id="<%=rem%>select" src='<c:url value="/images/remotes/shared/keys/select.png" />' onmousedown="clickRemoteButton(event,'SELECT', true)" onmouseup="clickRemoteButton(event,'SELECT', false)" alt="" style="width:38%;left:31%;top:34%;position:absolute;"/>
