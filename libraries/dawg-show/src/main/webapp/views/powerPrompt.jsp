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
Prompt for giving different options concerning power to an stb
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.comcast.video.dawg.show.ViewConstants"%>
<%@page import="com.comcast.video.dawg.show.key.Remote"%>
<%
Remote remote = (Remote) request.getAttribute(ViewConstants.REMOTE);
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <div style="background-color:#5a5d5f; text-align:right">
        <img src='<c:url value="/images/close.png" />' style="width: 30px; height: 30px" alt="" onclick="dismissPrompt()"/>
    </div>
    <div style="background-color:#5a5d5f; border-bottom:2px solid black;padding-bottom:5px">
        <font size="5px" color="#dedbdb"><b>What do you want to do?</b></font>
    </div>
    <div style="padding:30px;background-color:#dedbdb">
        <input style='font-size:17px; display: <%= remote.getMergeWebControls() ? "block;" : "none;" %>' type="button" value="Send Power Key" onclick="sendPowerKey()" />
        <input style="font-size:17px" type="button" value="Reboot" onclick="reboot()"/>
        <input style="font-size:17px" type="button" value="Power Off" onclick="powerOff()"/>
        <input style="font-size:17px" type="button" value="Power On" onclick="powerOn()"/>
    </div>
</html>
