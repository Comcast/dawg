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
The main page for viewing the stb metadata, trace, remote, and video.
-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@page import="com.comcast.video.stbio.Key"%>
<%@page import="com.comcast.video.dawg.show.ViewConstants"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false" %>

<%@include file="/views/stb_vars.jsp" %>

<c:set var="deviceId" value="${stb.id}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;width:100%;">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dawg Show</title>

        <jsp:include page="/views/head_scripts.jsp" />

       <jsp:include page="/views/remoteTypeSelector.jsp" />
       <script type="text/javascript">
           var deviceId = '${deviceId}';
       </script>

    </head>
    <body class="showBody" onresize="LayoutDelegator.draw()">

        <!-- prompt divs -->
        <div id="faded" class="promptFade"></div>

        <div id="loadComparisonPrompt" class="loadComparisonPrompt">
            <jsp:include page="/views/loadComparisonPrompt.jsp" />
        </div>

        <c:if test="${!supported}">
            <div id="notsupported">
                <jsp:include page="/views/bns-bar.jsp" />
            </div>
        </c:if>
        <div id="toolbar" class="toolbar">
            <jsp:include page="/views/componentToolbarSimplified.jsp" />
        </div>
        <div id="menu" class="menuContainer solid">
        </div>

        <div id="mainDiv" class="singleMainDiv" style="height: 100%">
            <!-- Video, metadata, trace div -->
            <div id="miniRemoteComp" class="miniRemoteComp" style="height: 0%;width: 0%">
                <div id="remoteMini" class="remoteMini" >
                    <jsp:include page="<%=miniRemotePage %>" />
                </div>
            </div>
            <div id="vmt" class="vmt" style="height: 100%">

                <!-- Video, metadata div -->
                <div id="vm" class="vm" style="height: 100%; width: 100%;">
                    <div id="videoDiv" class="videoDiv" style="height: 100%; width: 100%;">
                        <c:choose>
                        <c:when test="${hdVideoUrl != null}">
                            <canvas id="video" class="video" style="height: 100%; width: 100%;" data-videourl="${hdVideoUrl}"></canvas>
                        </c:when>
                        <c:otherwise>
                            <img id="video" class="video" style="height: 100%; width: 100%;"
                                            src="${videoUrl}"
                                            alt=""></img>
                            <c:if test="${audioUrl != null}" >
                                <div id="mutePrompt" class="mutePrompt">
                                    <jsp:include page="/views/mutePrompt.jsp" />
                                </div>

                                <audio id="audio" autoplay>
                                    <source src="${audioUrl}.ogg" type="audio/ogg">
                                    <source src="${audioUrl}.mp3" type="audio/mpeg">
                                </audio>
                            </c:if>

                            <c:if test="${!videoAvailable}" >
                                <canvas id="videoNotAvailableOverlay" class="videoNotAvailableOverlay" style="height: 100%; width: 100%; position: initial;"></canvas>
                            </c:if>
                        </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div id="traceDiv" class="traceDiv" data-deviceId="${deviceId}" data-tracehost="${traceHost}">
                    <jsp:include page="traceWindow.jsp" />
                </div>
            </div>
            <div id="standardRemoteDiv" class="standardRemoteDiv">
                <div id="remote" class="remoteContainer" style="width:90%;">
                    <jsp:include page="<%=stdRemotePage %>" />
                </div>
                <% if (!remote.getMergeWebControls()) {%>
                    <img id="mute" src='<c:url value="/images/remotes/xr2/keys/mute.png" />' alt="" style="width:10%;right:0%; top:0%; position:absolute"/>
                <% } %>
                <c:if test="${!irAvailable}">
                    <canvas id="stdRemoteNotAvailableOverlay" class="stdRemoteNotAvailableOverlay"></canvas>
                </c:if>
            </div>

            <div id="hold_panel" class="hold_panel"></div>
        </div>

        <script type="text/javascript">
            LayoutDelegator.bind(SingleLayoutManager);
            toolbar.handler = SingleLayoutManager;
            hw.callback = SingleLayoutManager;
            <c:if test="${!videoAvailable}">
            LayoutDelegator.addOverlay(new TextOverlay(document.getElementById('videoNotAvailableOverlay'), 'Video Unavailable'));
            </c:if>
            <c:if test="${!irAvailable}">
            LayoutDelegator.addOverlay(new TextOverlay(document.getElementById('stdRemoteNotAvailableOverlay'), 'IR Unavailable'));
            </c:if>

            DropDownMenu.bind($('#menu'), $('#menuBox'), $('#loadComparisonPrompt'), $('#faded'), '${deviceId}');
            LayoutDelegator.draw();
            VideoRenderer.bind($('#video'), '${deviceId}');
            VideoRenderer.startVideo();

            RemoteSelector.show(${remoteTypes}, '${remoteName}');
            <c:if test="${!irAvailable}">
            DirectTune.bind($('.tuneButton'),$('.directTuneDiv'));
            </c:if>
            StandardRemote.bind('${remoteName}');
            FrameRateOperator.bind($('.fpsButton'), $('.fpsDiv'), $('.fpsSelector'));
            HoverManager.bind([$('.directTunePanel'), $('.fpsPanel')]);

            var audio = document.getElementById('audio');
            var mute = document.getElementById('mute');
            if (mute) {
                mute.addEventListener('click', function (e)
                {
                    e = e || window.event;
                    audio.muted = !audio.muted;
                    e.preventDefault();
                },false);
            }
        </script>
    </body>
</html>
