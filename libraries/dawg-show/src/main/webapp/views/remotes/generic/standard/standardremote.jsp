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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
    <script type="text/javascript">
        var STD_REMOTE_ASPECT_RATIO = .25;
    </script>
    <script type="text/javascript" src='<c:url value="/js/GenericRemoteOps.js" />'></script>

    <div id="generciRemoteContainer" style="width:100%;height:100%;left:0%;top:0%;position:absolute;background-color:#CFCFCF;">
        <img id="skeyboard" src='<c:url value="/images/keyboardSel.png" />' onclick="clickKeyboard(this)" alt="" title="Click here to toggle keyboard control of the remote" style="width:18%;left:12%;top:4%;position:absolute;"/>
        <img id="spower" title="POWER" src='<c:url value="/images/remotes/xr2/keys/power.png" />' onmousedown="clickRemoteButton(event,'POWER', true)" onmouseup="clickRemoteButton(event,'POWER', false)"  alt="" style="width:12%;left:45%;top:4%;position:absolute;"/>
        <img id="shard-power" src='<c:url value="/images/remotes/shared/keys/hard-power.png"/>' onclick="clickPower(this)" alt="" title="Click here to change power state" style="width:12%;left:75%;top:4%;position:absolute;"/>
        <div id="dvr" style="top:9%;width:100%;height:12%;position:absolute">
            <jsp:include page="/views/remotes/generic/standard/dvr.jsp"></jsp:include>
        </div>
        <div style="top:22%;width:100%;height:12%;position:absolute">
            <jsp:include page="/views/remotes/generic/standard/pap.jsp"></jsp:include>
        </div>
        <div style="top:25%;width:100%;height:12%;position:absolute">
            <jsp:include page="/views/remotes/generic/standard/saftw.jsp"></jsp:include>
        </div>
        <div style="top:32%;width:100%;height:20%;position:absolute">
            <jsp:include page="/views/remotes/generic/standard/guidenav.jsp"></jsp:include>
        </div>
        <div style="top:52%;width:100%;height:4%;position:absolute">
            <jsp:include page="/views/remotes/generic/standard/emf.jsp"></jsp:include>
        </div>
        <div style="top:55%;width:100%;height:7.5%;position:absolute">
            <jsp:include page="/views/remotes/generic/standard/ceihl.jsp"></jsp:include>
        </div>
        <div style="top:61%;width:100%;height:4%;position:absolute">
            <jsp:include page="/views/remotes/generic/standard/fav.jsp"></jsp:include>
        </div>
        <div style="top:68%;width:100%;height:12%;position:absolute">
            <jsp:include page="/views/remotes/generic/standard/standnumberpad.jsp"></jsp:include>
        </div>
        <div style="top:80%;width:100%;height:6.5%;position:absolute">
            <jsp:include page="/views/remotes/generic/standard/pip.jsp"></jsp:include>
        </div>
        <div style="top:87%;width:100%;height:9%;position:absolute">
            <jsp:include page="/views/remotes/generic/standard/miscellaneousKeys.jsp"></jsp:include>
        </div>
        <div id="keymap_panel" class="keymap_panel">
            <jsp:include page="/views/remotes/generic/standard/keyMap.jsp"></jsp:include>
        </div>
    </div>

    <script type="text/javascript">
        GenericRemoteOps.bind("#generciRemoteContainer");
    </script>
</html>
