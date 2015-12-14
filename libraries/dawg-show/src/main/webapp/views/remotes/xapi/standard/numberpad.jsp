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
    <img id="sone" src='<c:url value="/images/remotes/xr2/keys/one.png" />' onmousedown="clickRemoteButton(event,'ONE', true)" onmouseup="clickRemoteButton(event,'ONE', false)" alt="" style="width:27%;left:0%;top:0%;position:absolute"/>
    <img id="stwo" src='<c:url value="/images/remotes/xr2/keys/two.png" />' onmousedown="clickRemoteButton(event,'TWO', true)" onmouseup="clickRemoteButton(event,'TWO', false)" alt="" style="width:27%;left:37%;top:0%;position:absolute"/>
    <img id="sthree" src='<c:url value="/images/remotes/xr2/keys/three.png" />' onmousedown="clickRemoteButton(event,'THREE', true)" onmouseup="clickRemoteButton(event,'THREE', false)" alt="" style="width:27%;left:73%;top:0%;position:absolute"/>
    <img id="sfour" src='<c:url value="/images/remotes/xr2/keys/four.png" />' onmousedown="clickRemoteButton(event,'FOUR', true)" onmouseup="clickRemoteButton(event,'FOUR', false)" alt="" style="width:27%;left:0%;top:27%;position:absolute"/>
    <img id="sfive" src='<c:url value="/images/remotes/xr2/keys/five.png" />' onmousedown="clickRemoteButton(event,'FIVE', true)" onmouseup="clickRemoteButton(event,'FIVE', false)" alt="" style="width:27%;left:37%;top:27%;position:absolute"/>
    <img id="ssix" src='<c:url value="/images/remotes/xr2/keys/six.png" />' onmousedown="clickRemoteButton(event,'SIX', true)" onmouseup="clickRemoteButton(event,'SIX', false)" alt="" style="width:27%;left:73%;top:27%;position:absolute"/>
    <img id="sseven" src='<c:url value="/images/remotes/xr2/keys/seven.png" />' onmousedown="clickRemoteButton(event,'SEVEN', true)" onmouseup="clickRemoteButton(event,'SEVEN', false)" alt="" style="width:27%;left:0%;top:54%;position:absolute"/>
    <img id="seight" src='<c:url value="/images/remotes/xr2/keys/eight.png" />' onmousedown="clickRemoteButton(event,'EIGHT', true)" onmouseup="clickRemoteButton(event,'EIGHT', false)" alt="" style="width:27%;left:37%;top:54%;position:absolute"/>
    <img id="snine" src='<c:url value="/images/remotes/xr2/keys/nine.png" />' onmousedown="clickRemoteButton(event,'NINE', true)" onmouseup="clickRemoteButton(event,'NINE', false)" alt="" style="width:27%;left:73%;top:54%;position:absolute"/>
    <img id="ssetup" src='<c:url value="/images/remotes/xr2/keys/setup.png" />' onmousedown="clickRemoteButton(event,'SETUP', true)" onmouseup="clickRemoteButton(event,'SETUP', false)" alt="" style="width:14.5%;left:6%;top:86%;position:absolute"/>
    <img id="szero" src='<c:url value="/images/remotes/xr2/keys/zero.png" />' onmousedown="clickRemoteButton(event,'ZERO', true)" onmouseup="clickRemoteButton(event,'ZERO', false)" alt="" style="width:27%;left:37%;top:81%;position:absolute"/>
    <img id="skeyhelp" src='<c:url value="/images/key_help.png" />' title="Click to view the keyboard to remote key mapping" onclick="displayKeyMappingOverlay(event, true)" alt="" style="width:14.5%;left:78.5%;top:86%;position:absolute"/>
</html>
