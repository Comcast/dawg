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
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
    <img id="spicture" src='<c:url value="/images/remotes/bcm-7437-1/keys/picture.png" />' onmousedown="clickRemoteButton(event,'PICTURE', true)" onmouseup="clickRemoteButton(event,'PICTURE', false)" alt="" style="width: 20%; left: 0%; top: 0%; position: absolute" />
    <img id="sone" src='<c:url value="/images/remotes/bcm-7437-1/keys/one.png" />' onmousedown="clickRemoteButton(event,'ONE', true)" onmouseup="clickRemoteButton(event,'ONE', false)" alt="" style="width: 18%; height: 15%; left: 25%; top: 0%; position: absolute" />
    <img id="stwo" src='<c:url value="/images/remotes/bcm-7437-1/keys/two.png" />' onmousedown="clickRemoteButton(event,'TWO', true)" onmouseup="clickRemoteButton(event,'TWO', false)" alt="" style="width: 18%; height: 15%; left: 50%; top: 0%; position: absolute" />
    <img id="sthree" src='<c:url value="/images/remotes/bcm-7437-1/keys/three.png" />' onmousedown="clickRemoteButton(event,'THREE', true)" onmouseup="clickRemoteButton(event,'THREE', false)" alt="" style="width: 18%; height: 15%; left: 75%; top: 0%; position: absolute" />
    <img id="saspect" src='<c:url value="/images/remotes/bcm-7437-1/keys/aspect.png" />' onmousedown="clickRemoteButton(event,'ASPECT', true)" onmouseup="clickRemoteButton(event,'ASPECT', false)" alt="" style="width: 20%; left: 0%; top: 18%; position: absolute" />
    <img id="sfour" src='<c:url value="/images/remotes/bcm-7437-1/keys/four.png" />' onmousedown="clickRemoteButton(event,'FOUR', true)" onmouseup="clickRemoteButton(event,'FOUR', false)" alt="" style="width: 18%; height: 15%; left: 25%; top: 18%; position: absolute" />
    <img id="sfive" src='<c:url value="/images/remotes/bcm-7437-1/keys/five.png" />' onmousedown="clickRemoteButton(event,'FIVE', true)" onmouseup="clickRemoteButton(event,'FIVE', false)" alt="" style="width: 18%; height: 15%; left: 50%; top: 18%; position: absolute" />
    <img id="ssix" src='<c:url value="/images/remotes/bcm-7437-1/keys/six.png" />' onmousedown="clickRemoteButton(event,'SIX', true)" onmouseup="clickRemoteButton(event,'SIX', false)" alt="" style="width: 18%; height: 15%; left: 75%; top: 18%; position: absolute" />
    <img id="spip" src='<c:url value="/images/remotes/bcm-7437-1/keys/pip.png" />' onmousedown="clickRemoteButton(event,'PIP', true)" onmouseup="clickRemoteButton(event,'PIP', false)" alt="" style="width: 20%; left: 0%; top: 36%; position: absolute" />
    <img id="sseven" src='<c:url value="/images/remotes/bcm-7437-1/keys/seven.png" />' onmousedown="clickRemoteButton(event,'SEVEN', true)" onmouseup="clickRemoteButton(event,'SEVEN', false)" alt="" style="width: 18%; height: 15%; left: 25%; top: 36%; position: absolute" />
    <img id="seight" src='<c:url value="/images/remotes/bcm-7437-1/keys/eight.png" />' onmousedown="clickRemoteButton(event,'EIGHT', true)" onmouseup="clickRemoteButton(event,'EIGHT', false)" alt="" style="width: 18%; height: 15%; left: 50%; top: 36%; position: absolute" />
    <img id="snine" src='<c:url value="/images/remotes/bcm-7437-1/keys/nine.png" />' onmousedown="clickRemoteButton(event,'NINE', true)" onmouseup="clickRemoteButton(event,'NINE', false)" alt="" style="width: 18%; height: 15%; left: 75%; top: 36%; position: absolute" />
    <img id="sswap" src='<c:url value="/images/remotes/bcm-7437-1/keys/swap.png" />' onmousedown="clickRemoteButton(event,'SWAP', true)" onmouseup="clickRemoteButton(event,'SWAP', false)" alt="" style="width: 20%; left: 0%; top: 56%; position: absolute" />
    <img id="senter" src='<c:url value="/images/remotes/bcm-7437-1/keys/enter.png" />' onmousedown="clickRemoteButton(event,'ENTER', true)" onmouseup="clickRemoteButton(event,'ENTER', false)" alt="" style="width: 20%; left: 25%; top: 56%; position: absolute" />
    <img id="szero" src='<c:url value="/images/remotes/bcm-7437-1/keys/zero.png" />' onmousedown="clickRemoteButton(event,'ZERO', true)" onmouseup="clickRemoteButton(event,'ZERO', false)" alt="" style="width: 18%; height: 15%; left: 50%; top: 56%; position: absolute" />
    <img id="sdot" src='<c:url value="/images/remotes/bcm-7437-1/keys/dot.png" />' onmousedown="clickRemoteButton(event,'DOT', true)" onmouseup="clickRemoteButton(event,'DOT', false)" alt="" style="width: 18%; height: 15%; left: 75%; top: 56%; position: absolute" />
    <img id="slast" src='<c:url value="/images/remotes/bcm-7437-1/keys/last.png" />' onmousedown="clickRemoteButton(event,'LAST', true)" onmouseup="clickRemoteButton(event,'LAST', false)" alt="" style="width: 20%; left: 0%; top: 78%; position: absolute" />
    <img id="sapplist" src='<c:url value="/images/remotes/bcm-7437-1/keys/applist.png" />' onmousedown="clickRemoteButton(event,'APPLIST', true)" onmouseup="clickRemoteButton(event,'APPLIST', false)" alt="" style="width: 20%; left: 25%; top: 78%; position: absolute" />
    <img id="sfun_a" src='<c:url value="/images/remotes/bcm-7437-1/keys/fun_a.png" />' onmousedown="clickRemoteButton(event,'FUN_A', true)" onmouseup="clickRemoteButton(event,'FUN_A', false)" alt="" style="width: 20%; left: 50%; top: 78%; position: absolute" />
    <img id="stext" src='<c:url value="/images/remotes/bcm-7437-1/keys/text.png" />' onmousedown="clickRemoteButton(event,'TEXT', true)" onmouseup="clickRemoteButton(event,'TEXT', false)" alt="" style="width: 20%; left: 75%; top: 78%; position: absolute" />
</html>
