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
    <img id="spiponoff" src='<c:url value="/images/remotes/shared/keys/piponoff.png" />' onmousedown="clickRemoteButton(event,'PIPONOFF', true)" onmouseup="clickRemoteButton(event,'PIPONOFF', false)" alt="" style="width:19%;left:0%;top:0%;position:absolute" onmouseover="this.style.filter='progid:DXImageTransform.Microsoft.BasicImage(invert=1)'"/>
    <img id="sswap" src='<c:url value="/images/remotes/shared/keys/swap.png" />' onmousedown="clickRemoteButton(event,'PIPSWAP', true)" onmouseup="clickRemoteButton(event,'PIPSWAP', false)" alt="" style="width:19%;left:27%;top:0%;position:absolute"/>
    <img id="smove" src='<c:url value="/images/remotes/shared/keys/move.png" />' onmousedown="clickRemoteButton(event,'PIPMOVE', true)" onmouseup="clickRemoteButton(event,'PIPMOVE', false)" alt="" style="width:19%;left:54%;top:0%;position:absolute"/>
    <img id="spipchup" src='<c:url value="/images/remotes/shared/keys/pipchup.png" />' onmousedown="clickRemoteButton(event,'PIPCHUP', true)" onmouseup="clickRemoteButton(event,'PIPCHUP', false)" alt="" style="width:19%;left:81%;top:0%;position:absolute"/>
    <img id="spipchdn" src='<c:url value="/images/remotes/shared/keys/pipchdn.png" />' onmousedown="clickRemoteButton(event,'PIPCHDN', true)" onmouseup="clickRemoteButton(event,'PIPCHDN', false)" alt="" style="width:19%;left:81%;top:55%;position:absolute"/>
    <img id="skeyhelp" src='<c:url value="/images/key_help.png" />' title="Click to view the keyboard to remote key mapping" onclick="displayKeyMappingOverlay(event, true)" alt="" style="width:19%;left:54%;top:55%;position:absolute"/>
</html>
