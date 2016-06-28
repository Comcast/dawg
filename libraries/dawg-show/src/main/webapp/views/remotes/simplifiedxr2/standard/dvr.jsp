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
    <img id="srew" src='<c:url value="/images/remotes/xr2/keys/rew.png" />' onmousedown="clickRemoteButton(event,'REW', true)" onmouseup="clickRemoteButton(event,'REW', false)" alt="" style="width:21%;left:0%;top:0%;position:absolute"/>
    <img id="splay" src='<c:url value="/images/remotes/xr2/keys/play.png" />' onmousedown="clickRemoteButton(event,'PLAY', true)" onmouseup="clickRemoteButton(event,'PLAY', false)" alt="" style="width:38%;left:31%;top:0%;position:absolute"/>
    <img id="sskipfwd" src='<c:url value="/images/remotes/xr2/keys/skipfwd.png" />' onmousedown="clickRemoteButton(event,'SKIPFWD', true)" onmouseup="clickRemoteButton(event,'SKIPFWD', false)" alt="" style="width:25%;height:38%;left:52%;top:0%;position:absolute;display:none"/>
    <img id="sff" src='<c:url value="/images/remotes/xr2/keys/ff.png" />' onmousedown="clickRemoteButton(event,'FF', true)" onmouseup="clickRemoteButton(event,'FF', false)" alt="" style="width:21%;left:79%;top:0%;position:absolute"/>
    <img id="sexit" src='<c:url value="/images/remotes/xr2/keys/exit.png" />' onmousedown="clickRemoteButton(event,'EXIT', true)" onmouseup="clickRemoteButton(event,'EXIT', false)" alt="" style="width:21%;left:0%;top:56%;position:absolute"/>
    <img id="smenu" src='<c:url value="/images/remotes/xr2/keys/xfinity.png" />' onmousedown="clickRemoteButton(event,'MENU', true)" onmouseup="clickRemoteButton(event,'MENU', false)" alt="" style="width:43.5%;left:28.5%;top:47.5%;position:absolute"/>
    <img id="srec" src='<c:url value="/images/remotes/xr2/keys/rec.png" />' onmousedown="clickRemoteButton(event,'REC', true)" onmouseup="clickRemoteButton(event,'REC', false)" alt="" style="width:21%;left:79%;top:54.5%;position:absolute"/>
</html>
