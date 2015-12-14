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
The mini remote image
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
    <script type="text/javascript">
        var MINI_REMOTE_ASPECT_RATIO = 2.7;
    </script>

    <img id="bg" src='<c:url value="/images/remotes/xmp/miniremotebg.png" />' alt="" style="width:100%;height:100%;left:0%;top:0%;position:absolute"/>
    <div style="left:3%;top:7%;width:34%;height:86%;position:absolute">
        <jsp:include page="/views/remotes/shared/mini/numberpad.jsp"></jsp:include>
    </div>
    <div style="left:40%;top:19%;width:30%;height:62%;position:absolute">
        <% request.setAttribute("rem", "m"); %>
        <jsp:include page="/views/remotes/shared/dirpad.jsp"></jsp:include>
    </div>

    <img id="mexit" src='<c:url value="/images/remotes/shared/keys/exit.png" />' onmousedown="clickRemoteButton(event,'EXIT', true)" onmouseup="clickRemoteButton(event,'EXIT', false)" alt="" style="width:9%;left:74%;top:7%;position:absolute;"/>
    <img id="minfo" src='<c:url value="/images/remotes/shared/keys/info.png" />' onmousedown="clickRemoteButton(event,'INFO', true)" onmouseup="clickRemoteButton(event,'INFO', false)" alt="" style="width:9%;left:74%;top:41%;position:absolute;"/>
    <img id="mlast" src='<c:url value="/images/remotes/shared/keys/last.png" />' onmousedown="clickRemoteButton(event,'LAST', true)" onmouseup="clickRemoteButton(event,'LAST', false)" alt="" style="width:9%;left:74%;top:75%;position:absolute;"/>
    <img id="mguide" src='<c:url value="/images/remotes/shared/keys/guide.png" />' onmousedown="clickRemoteButton(event,'GUIDE', true)" onmouseup="clickRemoteButton(event,'GUIDE', false)" alt="" style="width:9%;left:87%;top:7%;position:absolute;"/>
    <img id="mpower" src='<c:url value="/images/remotes/shared/keys/power.png" />' onmousedown="clickRemoteButton(event,'POWER', true)" onmouseup="clickRemoteButton(event,'POWER', false)" alt="" style="width:4%;left:90%;top:39%;position:absolute;"/>
    <img id="mmenu" src='<c:url value="/images/remotes/shared/keys/menu.png" />' onmousedown="clickRemoteButton(event,'MENU', true)" onmouseup="clickRemoteButton(event,'MENU', false)" alt="" style="width:9%;left:87%;top:75%;position:absolute;"/>
</html>
