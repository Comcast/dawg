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
    <img id="spgup" src='<c:url value="/images/remotes/shared/keys/pgup.png" />' onmousedown="clickRemoteButton(event,'PGUP', true)" onmouseup="clickRemoteButton(event,'PGUP', false)" alt="" style="width:15%;left:0%;top:15%;position:absolute"/>
    <div id="sabc" style="width:64%;left:18%;top:0%;position:absolute">
        <jsp:include page="/views/remotes/shared/standard/abc.jsp" />
    </div>
    <img id="spgdn" src='<c:url value="/images/remotes/shared/keys/pgdn.png" />' onmousedown="clickRemoteButton(event,'PGDN', true)" onmouseup="clickRemoteButton(event,'PGDN', false)" alt="" style="width:15%;left:85%;top:15%;position:absolute"/>
    <div id="sdirpad" style="width:70%;height:68%;left:15%;top:24%;position:absolute">
        <% request.setAttribute("rem", "s"); %>
        <jsp:include page="/views/remotes/shared/dirpad.jsp" />
    </div>
    <img id="sguide" src='<c:url value="/images/remotes/shared/keys/guide-s.png" />' onmousedown="clickRemoteButton(event,'GUIDE', true)" onmouseup="clickRemoteButton(event,'GUIDE', false)" alt="" style="width:18%;left:2.5%;top:76%;position:absolute"/>
    <img id="smenu" src='<c:url value="/images/remotes/shared/keys/menu-s.png" />' onmousedown="clickRemoteButton(event,'MENU', true)" onmouseup="clickRemoteButton(event,'MENU', false)" alt="" style="width:18%;left:79%;top:76%;position:absolute"/>
</html>
