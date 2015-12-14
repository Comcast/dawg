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
The red, green, blue, yellow buttons in the remote
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
    <img id="sred" src='<c:url value="/images/remotes/bcm-7437-1/keys/red.png" />' onmousedown="clickRemoteButton(event,'RED', true)" onmouseup="clickRemoteButton(event,'RED', false)" alt="" style="width: 20%; left: 0%; position: absolute" />
    <img id="sgreen" src='<c:url value="/images/remotes/bcm-7437-1/keys/green.png" />' onmousedown="clickRemoteButton(event,'GREEN', true)" onmouseup="clickRemoteButton(event,'GREEN', false)" alt="" style="width: 20%; left: 25%; position: absolute" />
    <img id="syellow" src='<c:url value="/images/remotes/bcm-7437-1/keys/yellow.png" />' onmousedown="clickRemoteButton(event,'YELLOW', true)" onmouseup="clickRemoteButton(event,'YELLOW', false)" alt="" style="width: 20%; left: 50%; position: absolute" />
    <img id="sblue" src='<c:url value="/images/remotes/bcm-7437-1/keys/blue.png" />' onmousedown="clickRemoteButton(event,'BLUE', true)" onmouseup="clickRemoteButton(event,'BLUE', false)" alt="" style="width: 20%; left: 75%; position: absolute" />
</html>
