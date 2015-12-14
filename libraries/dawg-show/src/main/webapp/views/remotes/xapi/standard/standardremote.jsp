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
            var STD_REMOTE_ASPECT_RATIO = .313;
    </script>

    <img id="bg" src='<c:url value="/images/remotes/xr2/bg.png" />' alt="" style="width:100%;height:100%;left:0%;top:0%;position:absolute"/>
    <img id="skeyboard" src='<c:url value="/images/keyboardSel.png" />' onclick="clickKeyboard(this)" alt="" title="Click here to toggle keyboard control of the remote" style="width:20%;left:15%;top:3.5%;position:absolute;"/>
    <img id="spower" src='<c:url value="/images/remotes/xr2/keys/power.png" />' onmousedown="clickRemoteButton(event,'POWER', true)" onmouseup="clickRemoteButton(event,'POWER', false)" alt="" style="width:13.5%;left:43.5%;top:1.9%;position:absolute;"/>
    <img id="shard-power" src='<c:url value="/images/remotes/shared/keys/hard-power.png" />' onclick="clickPower(this)" alt="" title="Click here to change power state" style="width:15%;left:65.5%;top:2.8%;position:absolute;"/>
    <div id="volch" style="left:16.5%;top:9%;width:67%;height:12%;position:absolute">
        <jsp:include page="/views/remotes/xapi/standard/volch.jsp"></jsp:include>
    </div>
    <div id="dvr" style="left:19%;top:23%;width:63%;height:12%;position:absolute">
        <jsp:include page="/views/remotes/xapi/standard/dvr.jsp"></jsp:include>
    </div>
    <% request.setAttribute("rem", "s"); %>
    <div id="guidenav" style="left:14%;top:35.8%;width:70.7%;height:24.2%;position:absolute">
        <jsp:include page="/views/remotes/xapi/guidenav.jsp"></jsp:include>
    </div>
    <div id="abcd" style="left:18%;top:60.8%;width:65.3%;height:4.2%;position:absolute">
        <jsp:include page="/views/remotes/xapi/standard/abcd.jsp"></jsp:include>
    </div>
    <div id="numberpad" style="left:18%;top:65.8%;width:64.7%;height:25.3%;position:absolute">
        <jsp:include page="/views/remotes/xapi/standard/numberpad.jsp"></jsp:include>
    </div>
    <div id="keymap_panel" class="keymap_panel">
        <jsp:include page="/views/remotes/shared/standard/keyMap.jsp" />
    </div>

</html>
