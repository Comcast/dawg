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
The standard remote image
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
    <script type="text/javascript">
        var STD_REMOTE_ASPECT_RATIO = .27;
    </script>

    <img id="bg" src='<c:url value="/images/remotes/bcm-7437-1/bg.png" />' alt="" style="width: 100%; height: 100%; left: 0%; top: 0%; position: absolute" />
    <img id="skeyboard" src='<c:url value="/images/keyboardSel.png" />' onclick="clickKeyboard(this)" alt="" title="Click here to toggle keyboard control of the remote" style="width: 20%; left: 10%; top: 3.5%; position: absolute;" />
    <img id="spower" src='<c:url value="/images/remotes/bcm-7437-1/keys/power.png" />' onmousedown="clickRemoteButton(event,'POWER', true)" onmouseup="clickRemoteButton(event,'POWER', false)" alt=""
                                                                         style="width: 20%; left: 40%; top: 2.8%; position: absolute;" />
    <img id="shard-power" src='<c:url value="/images/remotes/shared/keys/hard-power.png" />' onclick="clickPower(this)" alt="" title="Click here to change power state" style="width: 15%; left: 73%; top: 2.8%; position: absolute;" />
    <img id="ssource" src='<c:url value="/images/remotes/bcm-7437-1/keys/source.png" />' onmousedown="clickRemoteButton(event,'SOURCE', true)" onmouseup="clickRemoteButton(event,'SOURCE', false)" alt="" style="width: 20%; left: 48%; top: 8%; position: absolute;" />
    <img id="swatchtv" src='<c:url value="/images/remotes/bcm-7437-1/keys/watch_tv.png" />' onmousedown="clickRemoteButton(event,'WATCH_TV', true)" onmouseup="clickRemoteButton(event,'WATCH_TV', false)" alt="" style="width: 20%; left: 72%; top: 8%; position: absolute;" />
    <div id="numberpad" style="left: 10%; top: 12%; width: 85%; height: 23%; position: absolute">
        <jsp:include page="/views/remotes/bcm-7437-1/standard/numberpad.jsp"></jsp:include>
    </div>
    <div id="rgyb" style="left: 10%; top: 34%; width: 85%; height: 4%; position: absolute">
        <jsp:include page="/views/remotes/bcm-7437-1/standard/rgyb.jsp"></jsp:include>
    </div>
    <% request.setAttribute("rem", "s"); %>
    <div id="guidenav" style="left: 12%; right: 12%; top: 40%; width: 76%; height: 20%; position: absolute">
        <jsp:include page="/views/remotes/bcm-7437-1/guidenav.jsp"></jsp:include>
    </div>
    <div id="dvr" style="left: 13%; top: 62%; width: 85%; height: 10%; position: absolute">
        <jsp:include page="/views/remotes/bcm-7437-1/standard/dvr.jsp"></jsp:include>
    </div>
    <div id="fav" style="left: 13%; top: 74%; width: 85%; height: 10%; position: absolute">
        <jsp:include page="/views/remotes/bcm-7437-1/standard/fav.jsp"></jsp:include>
    </div>
    <div id="keymap_panel" class="keymap_panel">
        <jsp:include page="/views/remotes/bcm-7437-1/standard/keyMap.jsp" />
    </div>
</html>
