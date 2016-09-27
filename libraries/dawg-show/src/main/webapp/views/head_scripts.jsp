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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" type="text/css" href='<c:url value="/css/stb.css" />'>
<link rel="stylesheet" type="text/css" href='<c:url value="/css/menu.css" />'>
<link rel="stylesheet" type="text/css" href='<c:url value="/css/keymap.css" />'>
<link rel="stylesheet" type="text/css" href='<c:url value="/css/trace.css" />'>

<link rel="stylesheet" href="https://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.9.1.js" type="text/javascript"></script>
<script src="https://code.jquery.com/ui/1.10.2/jquery-ui.js" type="text/javascript"></script>

<script type="text/javascript" src='<c:url value="/js/jquery.gracefulWebSocket.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/jquery.alphanum.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/image-util.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/RemoteSelector.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/Date.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/Logger.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/StandardRemote.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/keys.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/ComparisonPrompt.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/VideoSnapper.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/SingleLayoutManager.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/LayoutDelegator.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/LayoutUtil.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/TextOverlay.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/DropDownMenu.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/VideoRenderer.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/SerialManager.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/directTune.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/validateInput.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/power.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/mute.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/jsmpg.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/FrameRateOperator.js" />'></script>
<script type="text/javascript" src='<c:url value="/js/HoverManager.js" />'></script>

<!-- Populate javascript variables. Will set the remote to the correct remote for this stb -->
<jsp:include page="/views/globalVars.jsp" />
