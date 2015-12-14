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
Displays the table of meta data information for an stb
-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@page import="com.comcast.video.dawg.common.MetaStb"%>
<%@page import="com.comcast.video.dawg.show.MetaStbTable"%>
<%@page import="com.comcast.video.dawg.show.ViewConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
MetaStbTable stb = new MetaStbTable((MetaStb) request.getAttribute(ViewConstants.STB_PARAM));
%>
<table id="meta" class="StbInfo"
                 style="border-collapse: collapse; height: 100%">
    <tr>
        <th>Name</th>
        <td><%=stb.getName() %></td>
    </tr>
    <tr>
        <th>MAC Address</th>
        <td><%=stb.getMacAddress() %></td>
    </tr>
    <tr>
        <th>IP</th>
        <td><%=stb.getIpAddress() %></td>
    </tr>
    <tr>
        <th>Make</th>
        <td><%=stb.getMake() %></td>
    </tr>
    <tr>
        <th>Model</th>
        <td><%=stb.getModel() %></td>
    </tr>
    <tr>
        <th>Plant</th>
        <td><%=stb.getPlant() %></td>
    </tr>
    <tr>
        <th>IR</th>
        <td><%=stb.getIrServiceUrl() %></td>
    </tr>
    <tr>
        <th>Remote Type</th>
        <td><%=stb.getRemoteType() %></td>
    </tr>
    <tr>
        <th>Power</th>
        <td><%=stb.getPowerServiceUrl() %></td>
    </tr>
    <tr>
        <th>Serial</th>
        <td><%=stb.getSerialHost() %></td>
    </tr>
    <tr>
        <th>Video</th>
        <td><%=stb.getVideoSourceUrl() %></td>
    </tr>
</table>
