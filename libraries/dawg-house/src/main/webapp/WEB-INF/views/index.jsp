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

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%@ page session="false"%>
<html>
    <head>
        <jsp:include page="/WEB-INF/views/globalVars.jsp" />

        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/styles/css/jquery-ui.min.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/styles/style.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/styles/tagcloud.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/styles/css/font-awesome.min.css">
        <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400' rel='stylesheet" type="text/css">

        <script src="${contextPath}/resources/scripts/jquery-1.11.3.min.js"></script>
        <script src="${contextPath}/resources/scripts/jquery-ui-1.10.2.min.js"></script>
        <script src="${contextPath}/resources/scripts/dawg-house.js"></script>
        <script src="${contextPath}/resources/scripts/dawg-pound.js"></script>
        <script src="${contextPath}/resources/scripts/TagCloud.js"></script>
        <script src="${contextPath}/resources/scripts/DeviceTable.js"></script>
        <script type="text/javascript" src='<c:url value="/resources/scripts/Condition.js" />'></script>
        <script type="text/javascript" src='<c:url value="/resources/scripts/FilterConstructor.js" />'></script>
        <script type="text/javascript" src='<c:url value="/resources/scripts/FilterHandler.js" />'></script>
        <script type="text/javascript" src='<c:url value="/resources/scripts/ContextMenu.js" />'></script>
        <script type="text/javascript" src='<c:url value="/resources/scripts/EditDevice.js" />'></script>
        <script>
            var token = '${token}';
            var requestParams = ${requestParams};
        </script>
        <title>Dawg House</title>
    </head>
    <body>
        <section class="user">
            <i class="icon-user"></i> <span>${token}</span> <input type="text" id="changeUserInput"  />
            <button class="btn" id="changeUserButton">Change User</button>
            <span class="suggestion" id="badlogin">Usernames may only contain alphanumeric keys.</span>
            <button class="btn" id="bModel" onclick="window.location='${contextPath}/modelsConfig/'">Configure Models</button>
            <c:if test="${admin}">
            	<button class="btn" id="bAdmin" onclick="window.location='${contextPath}/admin/view/user'">Manage Users</button>
            </c:if>
            <c:if test="${admin}">
            <button class="btn" id="bStb" onclick="window.location='${contextPath}/admin/view/stb'">Add Stb</button>
            </c:if>
        </section>

        <header>
            <div class="header">
                <h2 class="title">Dawg House</h2>
                <div class="titleAndSearch">
                    <input class="text" type="text" id="searchInput" value="${search}" />
                    <button class="searchTagBtn" id="searchButton">
                        <i class="icon-filter"></i> Filter
                    </button>
                    <button class="advSearchBtn">
                        <i class="icon-filter"></i> Advanced
                    </button>
                </div>
            </div>
            <div id="tagCloud" class="tagCloud">
            </div>
        </header>

        <div class="advSearchOverlay" title="Advanced Filter">
            <%@ include file="/WEB-INF/views/filterConstructor.jsp" %>
        </div>

        <section class="content">
            <table class="list stbTableHead">
                <thead>
                    <tr>
                        <td class="stbName">Name</td>
                        <td class="stbMac">MAC Address</td>
                        <td class="stbCb"><input class="toggle-all-checkbox" type="checkbox" /></td>
                        <td class="stbReserve">
                            <button class="btnMine <c:if test="${anyReserved}">btnSelected</c:if>">Mine</button>
                            <button class="btnUnreserved">Unreserved</button>
                            <button class="btn" id="multilock">
                                <i class="icon-lock"></i> Lock
                            </button>
                            <button class="btn" id="multiunlock">
                                <i class="icon-unlock"></i> Unlock
                            </button>
                        </td>
                        <td class="stbDawgShow"><button class="btn">
                                <i class="icon-external-link"></i> Dawg Show
                            </button></td>
                    </tr>
                </thead>
            </table>
            <div class="filteredTable"></div>
            <div class="stbTable">
                <c:if test="${! empty data}">
                <c:forEach items="${data}" var="stb">
                <div class="collapsableRow" data-deviceId="${stb.id}">
                    <div>
                        <table class="list stbMain">
                            <tr class="anStb">
                                <td class="stbName mdv">${stb.name}</td>
                                <td class="stbMac mdv">${stb.macAddress}</td>
                                <td class="cbStb mdv"><input type="checkbox" class="bulk-checkbox" data-deviceId="${stb.id}" onclick="event.stopPropagation()"/></td>
                                <c:choose>
                                <c:when test="${null == stb.reserver_key}">
                                <td id="td-${stb.id}" data-token="${token}" data-loginuser="${token}" data-deviceid="${stb.id}" data-locked=false class="lockable stbReserve">
                                    <i class="icon-unlock"></i>
                                </td>
                                </c:when>
                                <c:otherwise>
                                <td id="td-${stb.id}" data-token="${stb.reserver_key}" data-loginuser="${token}"    data-deviceid="${stb.id}" data-locked=true class="lockable stbReserve">
                                    <i class="icon-lock"></i> ${stb.reserver_key}
                                    ${stb.expiration_key}
                                </td>
                                </c:otherwise>
                                </c:choose>
                                <!-- <td><i class="icon-edit"></i></td> -->
                                <td class="stbDawgShow"><span class="fake-link" onclick="DAWGHOUSE.openDawgShow('${stb.name}', '${stb.id}', '${token}');event.stopPropagation();">Open <i class="icon-external-link"></i></span></td>
                            </tr>
                        </table>
                    </div>
                    <c:set var="icon" value="<span class='mdDiv'>&#8226</span>" />
                    <div class="stbMeta">
                        <div class="metadata">
                            <span class="mdDetail"><b>ID:</b> <span class="mdv">${stb.id}</span>${icon}</span>
                            <span class="mdDetail"><b>Make:</b> <span class="mdv">${stb.make}</span>${icon}</span>
                            <span class="mdDetail"><b>Model:</b> <span class="mdv">${stb.model}</span>${icon}</span>
                            <span class="mdDetail"><b>Family:</b> <span class="mdv">${stb.family}</span>${icon}</span>
                            <span class="mdDetail"><b>Capabilities:</b> <span class="mdv">${stb.capabilities}</span>${icon}</span>
                            <span class="mdDetail"><b>IP Address:</b> <span class="mdv">${stb.ipAddress}</span>${icon}</span>
                            <span class="mdDetail"><b>Tags:</b> <span class="stbTags mdv">${stb.tags}</span>${icon}</span>
                            <span class="mdDetail"><b>IR Service Url:</b> <span class="mdv">${stb.irServiceUrl}</span>${icon}</span>
                            <span class="mdDetail"><b>IR Service Port:</b> <span class="mdv">${stb.irServicePort}</span>${icon}</span>
                            <span class="mdDetail"><b>Power Service Url:</b> <span class="mdv">${stb.powerServiceUrl}</span>${icon}</span>
                            <span class="mdDetail"><b>Power Outlet:</b> <span class="mdv">${stb.powerOutlet}</span>${icon}</span>
                            <span class="mdDetail"><b>Video Service Url:</b> <span class="mdv">${stb.videoSourceUrl}</span>${icon}</span>
                            <span class="mdDetail"><b>Video Camera:</b> <span class="mdv">${stb.videoCamera}</span>${icon}</span>
                            <span class="mdDetail"><b>Trace Server Url:</b> <span class="mdv">${stb.traceServiceUrl}</span>${icon}</span>
                            <span class="mdDetail"><b>Remote Type:</b> <span class="mdv">${stb.remoteType}</span>${icon}</span>
                            <span class="mdDetail"><b>Channel Map Id:</b> <span class="mdv">${stb.channelMapId}</span>${icon}</span>
                            <span class="mdDetail"><b>Plant:</b> <span class="mdv">${stb.plant}</span>${icon}</span>
                            <span class="mdDetail"><b>Controller Name:</b> <span class="mdv">${stb.controllerName}</span>${icon}</span>
                            <span class="mdDetail"><b>Controller Id:</b> <span class="mdv">${stb.controllerId}</span>${icon}</span>
                            <span class="mdDetail"><b>Controller Ip Address:</b> <span class="mdv">${stb.controllerIpAddress}</span>${icon}</span>
                            <span class="mdDetail"><b>Rack Name:</b> <span class="mdv">${stb.rackName}</span>${icon}</span>
                            <span class="mdDetail"><b>Hardware Revision:</b> <span class="mdv">${stb.hardwareRevision}</span>${icon}</span>
                            <span class="mdDetail"><b>Slot Name:</b> <span class="mdv">${stb.slotName}</span></span>
                        </div>
                    </div>
                </div>
                    </c:forEach>
                </c:if>
            </div>
            <section class="bulk-tag">
                <p></p>
                <input id="bulkTagInput" class="text" type="text" />
                <button id="bulkTagButton" class="searchTagBtn">
                    <i class="icon-tag"></i> Tag
                </button>
                <div id="bulkTagResponse"></div>
            </section>
        </section>

        <ul class="cxtMenu">
            <li><a class="cxtDawgShow">Dawg Show</a></li>
            <li><a class="cxtEdit">Edit</a></li>
        </ul>


        <div class="editDeviceOverlay" title="Edit Device">
            <div class="newPropDiv">
                <input class="newPropKey" type="text"/>
                <input class="cbNewPropSet" type="checkbox" /> Set
                <input class="btnAddProp" type="button" value="+"/>
            </div>
            <div class="scrollableArea"></div>
            <br/><br/>
            <input class="btnSave" type="button" value="Save"/>
        </div>

        <footer>
            <!-- <nav>site map</nav> -->
        </footer>
        <script type="text/javascript">
            DAWGHOUSE.bindTable();
            TagCloud.bind($('#tagCloud'), ${deviceTags}, ${otherTags});
            DeviceTable.bind($('.content'), '${token}');
            DAWGHOUSE.sizeTable();
            FilterConstructor.bind($('.advSearchOverlay'), $('.advSearchBtn'), '${token}');
            FilterHandler.bind(${latestSearchCondition},${searchConditions});
            EditDevice.bind($('.editDeviceOverlay'));
            ContextMenu.bind($('.content'), $('.cxtMenu'), '${token}');
        </script>
    </body>
</html>
