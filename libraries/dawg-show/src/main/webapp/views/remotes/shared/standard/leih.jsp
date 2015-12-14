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
    <img id="sexit" src='<c:url value="/images/remotes/shared/keys/exit-s.png" />' onmousedown="clickRemoteButton(event,'EXIT', true)" onmouseup="clickRemoteButton(event,'EXIT', false)" alt="" style="width:22%;left:0%;top:22%;position:absolute"/>
    <img id="sinfo" src='<c:url value="/images/remotes/shared/keys/info.png" />' onmousedown="clickRemoteButton(event,'INFO', true)" onmouseup="clickRemoteButton(event,'INFO', false)" alt="" style="width:23%;left:39%;top:0%;position:absolute"/>
    <img id="shelp" src='<c:url value="/images/remotes/shared/keys/help.png" />' onmousedown="clickRemoteButton(event,'HELP', true)" onmouseup="clickRemoteButton(event,'HELP', false)" alt="" style="width:23%;left:39%;top:60%;position:absolute"/>
    <img id="slast" src='<c:url value="/images/remotes/shared/keys/last-s.png" />' onmousedown="clickRemoteButton(event,'LAST', true)" onmouseup="clickRemoteButton(event,'LAST', false)" alt="" style="width:22%;left:78%;top:22%;position:absolute"/>
</html>
