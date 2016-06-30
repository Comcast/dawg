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
    <img id="sa" src='<c:url value="/images/remotes/xr2/keys/a.png" />' onmousedown="clickRemoteButton(event,'A', true)" onmouseup="clickRemoteButton(event,'A', false)" alt="" style="width:19.5%;left:0%;top:0%;position:absolute"/>
    <img id="sb" src='<c:url value="/images/remotes/xr2/keys/b.png" />' onmousedown="clickRemoteButton(event,'B', true)" onmouseup="clickRemoteButton(event,'B', false)" alt="" style="width:17.5%;left:27%;top:2%;position:absolute"/>
    <img id="sc" src='<c:url value="/images/remotes/xr2/keys/c.png" />' onmousedown="clickRemoteButton(event,'C', true)" onmouseup="clickRemoteButton(event,'C', false)" alt="" style="width:18.5%;left:53.5%;top:2%;position:absolute"/>
    <img id="sd" src='<c:url value="/images/remotes/xr2/keys/d.png" />' onmousedown="clickRemoteButton(event,'D', true)" onmouseup="clickRemoteButton(event,'D', false)" alt="" style="width:20.5%;left:79.5%;top:0%;position:absolute"/>
</html>
