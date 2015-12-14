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
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<table id="modelOverlayTable" class="list" style="width:100%">
    <tbody>
        <tr>
            <td>Name</td>
            <td><input type="text" class="modelName"/></td>
        </tr>
        <tr>
            <td>Capabilities</td>
            <td>
                <input type="text" class="addCapInp"/><input type="button" class="bAddCap" value="+"/><br/>
                <div id="capList" class="capList" style="max-height:400px;overflow-y:auto"></div>
            </td>
        </tr>
        <tr>
            <td>Family</td>
            <td>
                <input type="text" class="addFamInp"/><input type="button" class="bAddFam" value="+"/><br/>
                <select id="modelFamily" class="modelFamily"></select>
            </td>
        </tr>
        <tr>
            <td colspan="2"><input class="bSave" type="button" value="Save" style="width:100%" /></td>
        </tr>
    </tbody>
</table>
<script src="${contextPath}/resources/scripts/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/resources/scripts/ModelOverlay.js"></script>
<script type="text/javascript">
    <c:forEach items="${capabilities}" var="cap">
        ModelOverlay.addCapability('${cap}', false, $('#capList'));
    </c:forEach>

    <c:forEach items="${families}" var="fam">
        ModelOverlay.addFamily('${fam}', $('#modelFamily'));
    </c:forEach>

    <c:forEach items="${models}" var="model">
        ModelOverlay.addModel('${model.name}');
    </c:forEach>

    ModelOverlay.bind($('#modelOverlayTable'));
</script>
