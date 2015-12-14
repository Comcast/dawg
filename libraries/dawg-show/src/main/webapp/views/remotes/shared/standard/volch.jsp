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
    <img id="svol" src='<c:url value="/images/remotes/shared/keys/vol.png" />' alt="" style="width:20%;left:0%;top:0%;position:absolute" onmouseover="this.style.filter='progid:DXImageTransform.Microsoft.BasicImage(invert=1)'"/>
    <img id="smute" src='<c:url value="/images/remotes/shared/keys/mute.png" />' onmousedown="clickRemoteButton(event,'MUTE', true)" onmouseup="clickRemoteButton(event,'MUTE', false)" alt="" style="width:16%;left:30%;top:63%;position:absolute"/>
    <img id="sfav" src='<c:url value="/images/remotes/shared/keys/fav.png" />' onmousedown="clickRemoteButton(event,'FAV', true)" onmouseup="clickRemoteButton(event,'FAV', false)" alt="" style="width:16%;left:53%;top:63%;position:absolute"/>
    <img id="sch" src='<c:url value="/images/remotes/shared/keys/ch.png" />' alt="" style="width:20%;left:79.5%;top:.4%;position:absolute"/>

    <script type="text/javascript">
        $("#sch").mousedown(function(evt) {
        var keyString = getKeyForVolCh($("#sch")[0], 'CH', evt.pageY, true);
        clickRemoteButton(evt, keyString, true);
        });

        $("#sch").mouseup(function(evt) {
        var keyString = getKeyForVolCh($("#sch")[0], 'CH', evt.pageY, false);
        clickRemoteButton(evt, keyString, false);
        });

        $("#svol").mousedown(function(evt) {
        var keyString = getKeyForVolCh($("#svol")[0], 'VOL', evt.pageY, true);
        clickRemoteButton(evt, keyString, true);
        });

        $("#svol").mouseup(function(evt) {
        var keyString = getKeyForVolCh($("#svol")[0], 'VOL', evt.pageY, false);
        clickRemoteButton(evt, keyString, false);
        });
    </script>

</html>
