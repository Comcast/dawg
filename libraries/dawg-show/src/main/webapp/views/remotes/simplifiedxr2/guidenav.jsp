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
The number pad for the standard remote
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
    <%
    String rem = (String) request.getAttribute("rem");
    rem = rem == null ? "s" : rem;
    %>
    <img id="<%=rem%>guide" src='<c:url value="/images/remotes/xr2/keys/guide.png" />' onmousedown="clickRemoteButton(event,'GUIDE', true)" onmouseup="clickRemoteButton(event,'GUIDE', false)" alt="" style="width:29%;left:0%;top:0%;position:absolute"/>
    <img id="<%=rem%>pgup" src='<c:url value="/images/remotes/xr2/keys/pgup.png" />' onmousedown="clickRemoteButton(event,'PGUP', true)" onmouseup="clickRemoteButton(event,'PGUP', false)" alt="" style="width:29%;left:71%;top:0%;position:absolute"/>
    <img id="<%=rem%>up" src='<c:url value="/images/remotes/xr2/keys/up.png" />' onmousedown="clickRemoteButton(event,'UP', true)" onmouseup="clickRemoteButton(event,'UP', false)" alt="" style="width:52%;left:25.5%;top:12%;position:absolute"/>
    <img id="<%=rem%>left" src='<c:url value="/images/remotes/xr2/keys/left.png" />' onmousedown="clickRemoteButton(event,'LEFT', true)" onmouseup="clickRemoteButton(event,'LEFT', false)" alt="" style="width:21%;left:15%;top:21%;position:absolute"/>
    <img id="<%=rem%>right" src='<c:url value="/images/remotes/xr2/keys/right.png" />' onmousedown="clickRemoteButton(event,'RIGHT', true)" onmouseup="clickRemoteButton(event,'RIGHT', false)" alt="" style="width:21%;left:67%;top:21%;position:absolute"/>
    <img id="<%=rem%>down" src='<c:url value="/images/remotes/xr2/keys/down.png" />' onmousedown="clickRemoteButton(event,'DOWN', true)" onmouseup="clickRemoteButton(event,'DOWN', false)" alt="" style="width:52%;left:25.5%;top:59.5%;position:absolute"/>
    <img id="<%=rem%>last" src='<c:url value="/images/remotes/xr2/keys/last.png" />' onmousedown="clickRemoteButton(event,'LAST', true)" onmouseup="clickRemoteButton(event,'LAST', false)"alt="" style="width:30%;left:0%;top:65%;position:absolute"/>
    <img id="<%=rem%>pgdn" src='<c:url value="/images/remotes/xr2/keys/pgdn.png" />' onmousedown="clickRemoteButton(event,'PGDN', true)" onmouseup="clickRemoteButton(event,'PGDN', false)" alt="" style="width:30%;left:70%;top:65%;position:absolute"/>
    <img id="<%=rem%>info" src='<c:url value="/images/remotes/xr2/keys/info.png" />' onmousedown="clickRemoteButton(event,'INFO', true)" onmouseup="clickRemoteButton(event,'INFO', false)" alt="" style="width:26.5%;left:37.75%;top:85%;position:absolute"/>
    <img id="<%=rem%>select" src='<c:url value="/images/remotes/xr2/keys/ok.png" />' onmousedown="clickRemoteButton(event,'SELECT', true)" onmouseup="clickRemoteButton(event,'SELECT', false)" alt="" style="width:22.5%;left:39.5%;top:34.5%;position:absolute"/>
</html>
