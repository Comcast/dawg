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
The bar at the top of the page when a browser is not supported
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Set"%>
<%@page import="eu.bitwalker.useragentutils.UserAgent"%>
<%@page import="eu.bitwalker.useragentutils.Browser"%>
<%@page import="eu.bitwalker.useragentutils.OperatingSystem"%>
<%@page import="com.comcast.video.dawg.show.BrowserSupport"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
UserAgent ua = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
OperatingSystem osGroup = ua.getOperatingSystem().getGroup();
Set<Browser> supported = BrowserSupport.SUPPORTED.get(osGroup);
Set<OperatingSystem> oses = BrowserSupport.SUPPORTED.keySet();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Browser is not supported</title>
    </head>
    <body>
        <div style="text-align:center;width:100%;padding:5px; background-color:#ff4444;border-style: solid; border-color: black; border-width: 2px; ">
            <img src='<c:url value="/images/error-icon.png" />' style="width:30px;height:30px"/>
            <font size="4">
                <%if (supported != null) { %>
                    I am sorry but your browser <b><%=ua.getBrowser().getName() %></b> running on <b><%= osGroup.getName() %></b> is
                    not supported.

                    Please try one of the following:
                    <% for (Browser browser : supported) {
                        String ctx = request.getContextPath();
                        String imgName = browser.getName().toLowerCase();
                        %>
                        <%= browser.getName() %>&nbsp;&nbsp;<img src='<%=ctx%>/images/browsers/<%=imgName%>.png' style="width:30px;height:30px"/>
                    <% } %>
                <% } else { %>
                    I am sorry but your operating system <b><%=osGroup %></b> is not supported, please try one of the following:<br/>

                    <% for (OperatingSystem os : oses) {%>
                        <%= os.getName() %>
                    <% } %>
                <% } %>
            </font>
        </div>
    </body>
</html>
