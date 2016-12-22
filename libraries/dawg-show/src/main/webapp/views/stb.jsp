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

<%
Boolean mobile = (Boolean) request.getAttribute(ViewConstants.MOBILE);
boolean videoAvail = (Boolean) request.getAttribute(ViewConstants.VIDEO_AVAILABLE);
boolean traceAvail = (Boolean) request.getAttribute(ViewConstants.TRACE_AVAILABLE);
boolean irAvail = (Boolean) request.getAttribute(ViewConstants.IR_AVAILABLE);
%>
<%@include file="/views/stb_vars.jsp" %>


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
        <div id="toolbar" class="toolbar">
            <jsp:include page="/views/componentToolbar.jsp" />
        </div>
        <div id="menu" class="menuContainer solid">
        </div>

        <div id="mainDiv" class="singleMainDiv">
            <!-- Video, metadata, trace div -->
            <div id="miniRemoteComp" class="miniRemoteComp">
                <div class="miniRemoteAndAnyKey">
                    <div id="anyKeyDiv" class="anyKeyDiv">
                        <select id="keySelection" class="keySelection">
                            <% for (Key key : remote.getKeys()) { %>
                                <option value="<%= key %>"><%= key %></option>
                            <% } %>
                        </select>
                        <input class="btnSendKey" type="button" value="send" onclick="sendKeyFromList()" />
                    </div>
                    <div id="remoteMiniDiv" class="miniRemoteContainer">
                        <div id="remoteMini" class="remoteMini">
                            <jsp:include page="<%=miniRemotePage %>" />
                        </div>
                    </div>
                </div>
                <c:if test="${!irAvailable}">
                    <canvas id="miniRemoteNotAvailableOverlay" class="miniRemoteNotAvailableOverlay"></canvas>
                </c:if>
            </div>
            <div id="vmt" class="vmt">

                <!-- Video, metadata div -->
                <div id="vm" class="vm">
                    <div id="videoDiv" class="videoDiv">
                        <c:choose>
                        <c:when test="${hdVideoUrl != null}">
                            <canvas id="video" class="video" data-videourl="${hdVideoUrl}"></canvas>
                        </c:when>
                        <c:otherwise>
                            <img id="video" class="video"
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
                                <canvas id="videoNotAvailableOverlay" class="videoNotAvailableOverlay"></canvas>
                            </c:if>
                        </c:otherwise>
                        </c:choose>
                    </div>
                    <div id="metaDiv" class="metaDiv">
                        <jsp:include page="/views/metastb.jsp" />
                    </div>
                </div>
                <div id="traceDiv" class="traceDiv" data-deviceId="${deviceId}" data-tracehost="${traceHost}">
                    <jsp:include page="traceWindow.jsp" />
                </div>
            </div>
            <div id="standardRemoteDiv" class="standardRemoteDiv">
                <div id="remote" class="remoteContainer">
                    <img alt="remote" src="images/remote.png" title="Click to change the remote type" class="changeRemoteButton"><br>
                    <jsp:include page="<%=stdRemotePage %>" />
                </div>
                <% if (!remote.getMergeWebControls()) { %>
                    <img id="mute" src='<c:url value="/images/remotes/xr2/keys/mute.png" />' alt="" style="width:10%;right:0%; top:10%; position:absolute"/>
                <% } %>
                <c:if test="${!irAvailable}">
                    <canvas id="stdRemoteNotAvailableOverlay" class="stdRemoteNotAvailableOverlay"></canvas>
                </c:if>
            </div>
            <div id="hold_panel" class="hold_panel"></div>
            <div id="hoverButtons" class="hoverButtons">
                <div id="fpsPanel" class="fpsPanel">
                    <div class="verticalAlign fpsButton pointerCursor">fps</div>
                    <div id="fpsDiv" class="fpsDiv">
                        <select class="fpsSelector"></select>
                    </div>
                </div>
                <% if (irAvail) {%>
                <div id="directTunePanel" class="directTunePanel">
                    <div class="verticalAlign tuneButton pointerCursor">Tune</div>
                    <div id="directTuneDiv" class="directTuneDiv">
                        <input type="text" id="channelNumTextBox" class="channelNumTextBox"
                                                                  name="channelNum" autofocus="autofocus"/>
                        <img alt="tune" src="images/go.png" title="Tune to channel" class="goButton">
                    </div>
                </div>
                <% } %>
            </div>
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
            LayoutDelegator.addOverlay(new TextOverlay(document.getElementById('miniRemoteNotAvailableOverlay'), 'IR Unavailable'));
            </c:if>

            SingleLayoutManager.draw();
            toolbar.onclick(document.getElementById('videoBox'));
            <c:choose>
            <c:when test="${mobile}">
            toolbar.onclick(document.getElementById('remoteMiniBox'));
            </c:when>
            <c:otherwise>
            toolbar.onclick(document.getElementById('traceBox'));
            toolbar.onclick(document.getElementById('remoteStdBox'));
            toolbar.onclick(document.getElementById('metadataBox'));
            </c:otherwise>
            </c:choose>

            DropDownMenu.bind($('#menu'), $('#menuBox'), $('#loadComparisonPrompt'), $('#faded'), '${deviceId}');
            SerialManager.bind();
            VideoRenderer.bind($('#video'), '${deviceId}');
            VideoRenderer.startVideo();
            RemoteSelector.bind('.changeRemoteButton','singleView');
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
