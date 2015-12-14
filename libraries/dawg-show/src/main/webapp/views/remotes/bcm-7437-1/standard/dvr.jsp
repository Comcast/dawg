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
The dvr keys for the standard remote
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
    <img id="sstop" src='<c:url value="/images/remotes/bcm-7437-1/keys/stop.png" />' onmousedown="clickRemoteButton(event,'STOP', true)" onmouseup="clickRemoteButton(event,'STOP', false)" alt="" style="width: 20%; left: 0%; top: 0%; position: absolute" />
    <img id="spause" src='<c:url value="/images/remotes/bcm-7437-1/keys/pause.png" />' onmousedown="clickRemoteButton(event,'PAUSE', true)" onmouseup="clickRemoteButton(event,'PAUSE', false)" alt="" style="width: 20%; left: 22%; top: 0%; position: absolute" />
    <img id="splay" src='<c:url value="/images/remotes/bcm-7437-1/keys/play.png" />' onmousedown="clickRemoteButton(event,'PLAY', true)" onmouseup="clickRemoteButton(event,'PLAY', false)" alt="" style="width: 20%; left: 44%; top: 0%; position: absolute" />
    <img id="sreverse" src='<c:url value="/images/remotes/bcm-7437-1/keys/reverse.png" />' onmousedown="clickRemoteButton(event,'REVERSE', true)" onmouseup="clickRemoteButton(event,'REVERSE', false)" alt="" style="width: 20%; left: 66%; top: 0%; position: absolute" />
    <img id="sprev" src='<c:url value="/images/remotes/bcm-7437-1/keys/prev.png" />' onmousedown="clickRemoteButton(event,'PREV', true)" onmouseup="clickRemoteButton(event,'PREV', false)" alt="" style="width: 20%; left: 0%; top: 33%; position: absolute" />
    <img id="snext" src='<c:url value="/images/remotes/bcm-7437-1/keys/next.png" />' onmousedown="clickRemoteButton(event,'NEXT', true)" onmouseup="clickRemoteButton(event,'NEXT', false)" alt="" style="width: 20%; left: 22%; top: 33%; position: absolute" />
    <img id="srew" src='<c:url value="/images/remotes/bcm-7437-1/keys/rew.png" />' onmousedown="clickRemoteButton(event,'REW', true)" onmouseup="clickRemoteButton(event,'REW', false)" alt="" style="width: 20%; left: 44%; top: 33%; position: absolute" />
    <img id="sff" src='<c:url value="/images/remotes/bcm-7437-1/keys/ff.png" />' onmousedown="clickRemoteButton(event,'FF', true)" onmouseup="clickRemoteButton(event,'FF', false)" alt="" style="width: 20%; left: 66%; top: 33%; position: absolute" />
    <img id="srec" src='<c:url value="/images/remotes/bcm-7437-1/keys/rec.png" />' onmousedown="clickRemoteButton(event,'REC', true)" onmouseup="clickRemoteButton(event,'REC', false)" alt="" style="width: 20%; left: 0%; top: 66%; position: absolute" />
</html>
