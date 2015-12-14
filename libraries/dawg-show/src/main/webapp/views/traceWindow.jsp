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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script type="text/javascript" src='<c:url value="/js/HTMLUtil.js"/>'></script>
<div class="traceComp">
    <div class="traceBar">
        <div style="float: left">
            <input type="checkbox" class="autoscroll" checked="checked" />Autoscroll<br />
        </div>
        <div class = "serialButtons" style="float: right; width: 50%">
            <div class="hexContainer">
                <input type="checkbox" class="serialHex" />Hex
            </div>
            <input class="serialCmd" type="text"/>
            <input class="serialBtn sendSerialBtn" type="button" value="Send to serial" />
            <input class="serialBtn flushBufferBtn" type="button" value="Flush Buffer" title="This will continuously send serial commands until trace is received back" />
        </div>
    </div>

    <div class="traceTab">
        <ul>
            <li><a href="#trace">Box Trace</a></li>
            <li><a href="#eventLog">Event log</a></li>
            <li id = export> <a title="Click to export the selected tab's logs"><img src='<c:url value="/images/export.png" />'> </a></li>
            <li class="clearLogs"><a><img src="images/clear_log_icon.png" width="25px" height="25px" title="Click to clear the selected tab's log"></a></li>
        </ul>
        <div id="trace" class="traceDisp">
        </div>
        <div id="eventLog" class="eventDisp">
        </div>
    </div>
</div>
