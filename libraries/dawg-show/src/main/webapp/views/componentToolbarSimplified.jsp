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
<!--  The toolbar that allows selecting and de-selecting components -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
Boolean mobile = (Boolean) request.getAttribute("mobile");
if (mobile == null) {
mobile = false;
}
int rows = mobile ? 2 : 1;
int toolbarHeight = 0;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background-color: white;width:100%" >
    <head>
        <script type="text/javascript" src='<c:url value="/js/ComponentToolbar.js" />'></script>
    </head>

    <div id ="toolbarDiv" style="width:0%;height:<%=toolbarHeight %>px">
        <canvas width="<%=toolbarHeight %>" height="<%=toolbarHeight %>" id="menuBox" style="float:left;height:<%=toolbarHeight %>px;width:<%=toolbarHeight %>px" onclick="toolbar.onclick(this);" class="solid"></canvas>
        <canvas id="videoBox" style="float:left" onclick="toolbar.onclick(this);"></canvas>
        <canvas id="traceBox" style="float:left" onclick="toolbar.onclick(this);"></canvas>
        <canvas id="remoteStdBox" style="float:left" onclick="toolbar.onclick(this);"></canvas>
        <canvas id="remoteMiniBox" style="float:left" onclick="toolbar.onclick(this);"></canvas>
        <canvas id="metadataBox" style="float:left" onclick="toolbar.onclick(this);"></canvas>
    </div>
    <div id="hideButtonDiv" style="width:0%;height:0px; text-align:center">
        <canvas id="hideButton" width="0px" height="0px" onclick="hw.onclick()"></canvas>
    </div>

    <script type="text/javascript">
            var menuBox = new MenuBox($('#menuBox')[0]);
            var toolbar = new ComponentToolbar(0, 0, <%= rows %>);
            toolbar.addBox('videoBox', 'Video');
            toolbar.addBox('traceBox', 'Trace');
            var remote = toolbar.addBox('remoteStdBox', 'Remote');
            var remoteMini = toolbar.addBox('remoteMiniBox', 'Mini Remote');
            toolbar.addBox('metadataBox', 'Metadata');
            toolbar.addBoxTie([remote, remoteMini]);

            menuBox.draw();
            toolbar.draw();
            hw = new HideWidget(document.getElementById('hideButton'));
            hw.draw();
    </script>
</html>
