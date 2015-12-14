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
The fav keys for the standard remote
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
    <img id="sfav_1" src='<c:url value="/images/remotes/bcm-7437-1/keys/fav_1.png" />' onmousedown="clickRemoteButton(event,'FAV_1', true)" onmouseup="clickRemoteButton(event,'FAV_1', false)" alt="" style="width: 20%; left: 0%; top: 0%; position: absolute" />
    <img id="sfav_2" src='<c:url value="/images/remotes/bcm-7437-1/keys/fav_2.png" />' onmousedown="clickRemoteButton(event,'FAV_2', true)" onmouseup="clickRemoteButton(event,'FAV_2', false)" alt="" style="width: 20%; left: 22%; top: 0%; position: absolute" />
    <img id="sfav_3" src='<c:url value="/images/remotes/bcm-7437-1/keys/fav_3.png" />' onmousedown="clickRemoteButton(event,'FAV_3', true)" onmouseup="clickRemoteButton(event,'FAV_3', false)" alt="" style="width: 20%; left: 44%; top: 0%; position: absolute" />
    <img id="sfav_4" src='<c:url value="/images/remotes/bcm-7437-1/keys/fav_4.png" />' onmousedown="clickRemoteButton(event,'FAV_4', true)" onmouseup="clickRemoteButton(event,'FAV_4', false)" alt="" style="width: 20%; left: 66%; top: 0%; position: absolute" />

    <img id="sfreeze" src='<c:url value="/images/remotes/bcm-7437-1/keys/freeze.png" />' onmousedown="clickRemoteButton(event,'FREEZE', true)" onmouseup="clickRemoteButton(event,'FREEZE', false)" alt="" style="width: 20%; left: 0%; top: 33%; position: absolute" />
    <img id="sphoto" src='<c:url value="/images/remotes/bcm-7437-1/keys/photo.png" />' onmousedown="clickRemoteButton(event,'PHOTO', true)" onmouseup="clickRemoteButton(event,'PHOTO', false)" alt="" style="width: 20%; left: 22%; top: 33%; position: absolute" />
    <img id="smusic" src='<c:url value="/images/remotes/bcm-7437-1/keys/music.png" />' onmousedown="clickRemoteButton(event,'MUSIC', true)" onmouseup="clickRemoteButton(event,'MUSIC', false)" alt="" style="width: 20%; left: 44%; top: 33%; position: absolute" />
    <img id="svideo" src='<c:url value="/images/remotes/bcm-7437-1/keys/video.png" />' onmousedown="clickRemoteButton(event,'VIDEO', true)" onmouseup="clickRemoteButton(event,'VIDEO', false)" alt="" style="width: 20%; left: 66%; top: 33%; position: absolute" />

    <img id="ssubtitle" src='<c:url value="/images/remotes/bcm-7437-1/keys/subtitle.png" />' onmousedown="clickRemoteButton(event,'SUBTITLE', true)" onmouseup="clickRemoteButton(event,'SUBTITLE', false)" alt="" style="width: 20%; left: 0%; top: 66%; position: absolute" />
    <img id="ssleep" src='<c:url value="/images/remotes/bcm-7437-1/keys/sleep.png" />' onmousedown="clickRemoteButton(event,'SLEEP', true)" onmouseup="clickRemoteButton(event,'SLEEP', false)" alt="" style="width: 20%; left: 22%; top: 66%; position: absolute" />
    <img id="smute" src='<c:url value="/images/remotes/bcm-7437-1/keys/mute.png" />' onmousedown="clickRemoteButton(event,'MUTE', true)" onmouseup="clickRemoteButton(event,'MUTE', false)" alt="" style="width: 20%; left: 44%; top: 66%; position: absolute" />
    <img id="saudio" src='<c:url value="/images/remotes/bcm-7437-1/keys/audio.png" />' onmousedown="clickRemoteButton(event,'AUDIO', true)" onmouseup="clickRemoteButton(event,'AUDIO', false)" alt="" style="width: 20%; left: 66%; top: 66%; position: absolute" />

    <img id="skeyhelp" src="/dawg-show/images/key_help.png" title="Click to view the keyboard to remote key mapping" onclick="displayKeyMappingOverlay(event, true)" alt="" style="width: 12%; left: 70%; top: 98%; position: absolute" />
</html>
