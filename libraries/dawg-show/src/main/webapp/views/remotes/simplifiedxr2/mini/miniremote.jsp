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
        var MINI_REMOTE_ASPECT_RATIO = 2.18;
    </script>

    <img id="bg" src='<c:url value="/images/remotes/xr2/minibg.png" />' alt="" style="width:100%;height:100%;left:0%;top:0%;position:absolute"/>
    <div style="left:5%;top:5%;width:90%;height:85%;position:absolute">
        <div id="numberpad" style="left:0%;top:0%;width:38.7%;height:100%;position:absolute">
            <jsp:include page="/views/remotes/xr2/mini/numberpad.jsp"></jsp:include>
        </div>
        <% request.setAttribute("rem", "m"); %>
        <div id="guidenav" style="left:42.6%;top:3%;width:42.6%;height:96%;position:absolute">
            <jsp:include page="/views/remotes/xr2/guidenav.jsp"></jsp:include>
        </div>
        <img id="mpower" src='<c:url value="/images/remotes/xr2/keys/power.png" />' onmousedown="clickRemoteButton(event,'POWER', true)" onmouseup="clickRemoteButton(event,'POWER', false)" alt="" style="width:8.5%;left:89%;top:4.5%;position:absolute"/>
        <img id="mch" src='<c:url value="/images/remotes/xr2/keys/ch.png" />' alt="" style="width:9.5%;left:89%;top:28.5%;position:absolute"/>
        <img id="mmenu" src='<c:url value="/images/remotes/xr2/keys/xfinity.png" />' onmousedown="clickRemoteButton(event,'MENU', true)" onmouseup="clickRemoteButton(event,'MENU', false)" alt="" style="width:12%;left:87.5%;top:76%;position:absolute"/>
    </div>

    <script type="text/javascript">
        $("#mch").mousedown(function(evt) {
        var keyString = getKeyForVolCh($("#mch")[0], 'CH', evt.pageY, true);
        clickRemoteButton(evt, keyString, true);
        });

        $("#mch").mouseup(function(evt) {
        var keyString = getKeyForVolCh($("#mch")[0], 'CH', evt.pageY, false);
        clickRemoteButton(evt, keyString, false);
        });
    </script>

</html>
