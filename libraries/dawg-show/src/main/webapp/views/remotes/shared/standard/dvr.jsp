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
    <div>
        <img id="srew" src='<c:url value="/images/remotes/shared/keys/rew.png" />' onmousedown="clickRemoteButton(event,'REW', true)" onmouseup="clickRemoteButton(event,'REW', false)" alt="" style="width:26%;left:0%;top:0%;position:absolute"/>
        <img id="splay" src='<c:url value="/images/remotes/shared/keys/play.png" />' onmousedown="clickRemoteButton(event,'PLAY', true)" onmouseup="clickRemoteButton(event,'PLAY', false)" alt="" style="width:28%;left:36%;top:4%;position:absolute"/>
        <img id="sff" src='<c:url value="/images/remotes/shared/keys/ff.png" />' onmousedown="clickRemoteButton(event,'FF', true)" onmouseup="clickRemoteButton(event,'FF', false)" alt="" style="width:26%;left:74%;top:0%;position:absolute"/>
    </div>
    <div>
        <img id="sstop" src='<c:url value="/images/remotes/shared/keys/stop.png" />' onmousedown="clickRemoteButton(event,'STOP', true)" onmouseup="clickRemoteButton(event,'STOP', false)" alt="" style="width:16%;left:2%;top:38%;position:absolute"/>
        <img id="spause" src='<c:url value="/images/remotes/shared/keys/pause.png" />' onmousedown="clickRemoteButton(event,'PAUSE', true)" onmouseup="clickRemoteButton(event,'PAUSE', false)" alt="" style="width:33%;left:34%;top:38%;position:absolute"/>
        <img id="sskipfwd" src='<c:url value="/images/remotes/shared/keys/skipfwd.png" />' onmousedown="clickRemoteButton(event,'SKIPFWD', true)" onmouseup="clickRemoteButton(event,'SKIPFWD', false)" alt="" style="width:25%;height:25%;left:52%;top:38%;position:absolute;display:none"/>
        <img id="srec" src='<c:url value="/images/remotes/shared/keys/rec.png" />' onmousedown="clickRemoteButton(event,'REC', true)" onmouseup="clickRemoteButton(event,'REC', false)" alt="" style="width:18%;left:82%;top:38%;position:absolute"/>
    </div>
    <div>
        <img id="sreplay" src='<c:url value="/images/remotes/shared/keys/replay.png" />' onmousedown="clickRemoteButton(event,'REPLAY', true)" onmouseup="clickRemoteButton(event,'REPLAY', false)" alt="" style="width:28%;left:1%;top:73%;position:absolute;"/>
        <img id="smydvr" src='<c:url value="/images/remotes/shared/keys/mydvr.png" />' onmousedown="clickRemoteButton(event,'MYDVR', true)" onmouseup="clickRemoteButton(event,'MYDVR', false)" alt="" style="width:32%;left:35%;top:74%;position:absolute;"/>
        <img id="slive" src='<c:url value="/images/remotes/shared/keys/live.png" />' onmousedown="clickRemoteButton(event,'LIVE', true)" onmouseup="clickRemoteButton(event,'LIVE', false)" alt="" style="width:26%;left:73%;top:73%;position:absolute;"/>
    </div>
</html>
