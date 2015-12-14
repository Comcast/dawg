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
Populates the javascript remotes array from the RemoteManager on the server.
-->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.comcast.video.dawg.show.key.RemoteManager"%>
<%@page import="com.comcast.video.dawg.show.key.Remote"%>
<%@page import="com.comcast.video.dawg.show.key.RemoteButton"%>
<%@page import="com.comcast.video.dawg.show.ViewConstants"%>
<script>
        var remotes = new Array();
        <%
        RemoteManager rm = (RemoteManager) request.getAttribute(ViewConstants.REMOTE_MANAGER);
        for (Remote remote : rm.getRemotes()) {
            %>
            rName = '<%=remote.getName() %>';
            r = new Remote(rName);
            <% for (RemoteButton button : remote.getButtons()) { %>
                k = '<%= button.getKey().name() %>';
                x = <%= button.getPos().x %>;
                y = <%= button.getPos().y %>;
                w = <%= button.getSize().x %>;
                h = <%= button.getSize().y %>;
                b = new RemoteButton(k, x, y, w, h);
                r.addButton(b);
            <% } %>
            remotes[rName] = r;
        <% } %>
</script>
