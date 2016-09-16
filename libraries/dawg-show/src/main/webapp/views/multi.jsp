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
The main page for multi-viewing the stb metadata, trace, remote, and video.
-->

<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@page import="com.comcast.video.dawg.show.ViewConstants"%>
<%@page import="com.comcast.video.dawg.show.key.Remote"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page isELIgnored="false" %>

<%

Remote remote = (Remote) request.getAttribute(ViewConstants.REMOTE);
String remoteName = (String) remote.getName();
boolean supported = (Boolean) request.getAttribute(ViewConstants.SUPPORTED);
String stdRemotePage = "/views/remotes/" + remote.getImageSubpath() + "/standard/standardremote.jsp";
String miniRemotePage = "/views/remotes/" + remote.getImageSubpath() + "/mini/miniremote.jsp";
String genericRemoteKeys = (String)request.getAttribute(ViewConstants.GENERIC_REMOTE_KEYS);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;width:100%;">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dawg Show</title>

        <jsp:include page="/views/head_scripts.jsp" />

    </head>
    <body class="showBody" onresize="MultiLayoutManager.draw();">

        <!-- prompt divs -->
        <div id="faded" class="promptFade"></div>
        <div id="powerPrompt" class="powerPrompt">
            <jsp:include page="/views/powerPrompt.jsp" />
        </div>
        <div id="loadComparisonPrompt" class="loadComparisonPrompt">
            <jsp:include page="/views/loadComparisonPrompt.jsp" />
        </div>

        <c:if test="${!supported}">
        <div id="notsupported">
            <jsp:include page="/views/bns-bar.jsp" />
        </div>
        </c:if>

        <div id="mainDiv" class="mainDiv">
            <!-- Video and trace div -->
            <div id="videoAndTrace" class="videoAndTrace">
                <div id="allvideos" class="allvideos">
                    <c:forEach items="${stbs}" var="stb">
                    <div class="videoDiv" data-deviceId="${stb.id}">
                        <div class="videoHeader">
                            <img class="multiVisionSnap" alt="Snap video" src="/dawg-show/images/snapvideo.png" data-deviceid="${stb.id}"/>
                            <span class="multiVisionLoadComparison" data-deviceid="${stb.id}">I</span>
                            <input type="checkbox" class="cbStb" checked="checked" data-deviceid="${stb.id}"/>
                            <span class="stbHeaderText">${stb.name}</span>
                            <span class="traceToggle traceToggleUnselected" data-deviceid="${stb.id}">T</span>
                        </div>
                        <div class="videoBody">
                            <c:choose>
                            <c:when test="${empty stb.hdVideoUrl}">
                            <img id="video" class="video" src="${stb.videoSourceUrl}/axis-cgi/mjpg/video.cgi?camera=${stb.videoCamera}" alt=""></img>
                            </c:when>
                            <c:otherwise>
                            <canvas id="video" class="video" data-videourl="${stb.hdVideoUrl}"></canvas>
                            </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    </c:forEach>
                </div>
                <c:forEach items="${stbs}" var="stb">
                <c:set var="deviceId" value="${stb.id}" />
                <div class="traceDiv" data-deviceId="${deviceId}" data-tracehost="${stb.serialHost}">
                    <jsp:include page="traceWindow.jsp" />
                </div>
                </c:forEach>
            </div>
            <div id="standardRemoteDiv" class="standardRemoteDiv">
                <input type="checkbox" class="selectAllDevicesCheckbox" id="select_all_devices_checkbox" checked="checked" title="Check to select all devices" />
                <div id="remote" class="remoteContainer">
                    <img alt="changeRemote" src="images/remote.png" title="Click to show/hide Generic remote" class="changeRemoteButton"><br>
                    <jsp:include page="<%=stdRemotePage %>" />
                    <img alt="snap" src="images/camera.png" title="Click to take snapshot of all selected devices" class="multi-snap-button">
                </div>
            </div>
            <div id="hold_panel" class="hold_panel"></div>
            <div id="hoverButtons" class="hoverButtons">
                <div id="fpsPanel" class="fpsPanel">
                    <div class="verticalAlign fpsButton pointerCursor">fps</div>
                    <div id="fpsDiv" class="fpsDiv">
                        <select class="fpsSelector"></select>
                    </div>
                </div>
                <div id="directTunePanel" class="directTunePanel">
                    <div class="verticalAlign tuneButton pointerCursor">Tune</div>
                    <div id="directTuneDiv" class="directTuneDiv">
                        <input type="text" id="channelNumTextBox" class="channelNumTextBox" name="channelNum" autofocus="autofocus"/>
                        <img alt="tune" src="images/go.png" title="Tune to channel" class="goButton">
                    </div>
                </div>
                <div id="serialCommandPanel" class="serialCommandPanel absolutePosition">
                    <div class="verticalAlign serialCommandSendButton pointerCursor">Serial</div>
                    <div id="serialCmdDiv" class="serialCmdDiv">
                        <input type="checkbox" class="serialCommandHex"/>Hex
                        <input type="text" id="serialCmdTextBox" class="serialCmdTextBox" name="serialCmd" autofocus="autofocus"/>
                        <img alt="serial command" src="images/go.png" title="Send serial command" class="sendButton">
                    </div>
                </div>
            </div>

            <script type="text/javascript">
                    MultiView.bind();
                    MultiSave.bind('.multi-snap-button');
                    MultiCheckboxSelector.bind($('.selectAllDevicesCheckbox'), $('.cbStb'));
                    DirectTune.bind($('.tuneButton'),$('.directTuneDiv'));
                    StandardRemote.bind('<%= remoteName %>');
                    HoverManager.bind([$('.directTunePanel'), $('.serialCommandPanel'), $('.fpsPanel')]);
                    GenericRemote.bind($('.changeRemoteButton'), '<%= remoteName %>', '<%= genericRemoteKeys %>');
                    FrameRateOperator.bind($('.fpsButton'), $('.fpsDiv'), $('.fpsSelector'));
            </script>
    </body>
</html>
