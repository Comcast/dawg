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
<div class="filterConstructor">
<select class="fieldSelect">

    <c:forEach items="${filterFields}" var="f">
    <option value="${f.field}" data-array="${f.array}">${f.display}</option>
    </c:forEach>
</select>
<select class="opSelect">
    <c:forEach items="${operators}" var="op">
    <option value="${op}">${op}</option>
    </c:forEach>
</select>
<input type="text" class="fieldValueInput" />
<input type="button" class="btnAddCondition" value="Add"/>
<br/>
<br/>
<div class="conditionControl">
    <input type="button" class="controlBtn btnAnd" disabled="disabled" value="AND" title="Device must meet all selected conditions"/>
    <input type="button" class="controlBtn btnOr" disabled="disabled" value="OR" title="Device must meet some of the selected conditions"/>
    <input type="button" class="controlBtn btnNot" disabled="disabled" value="NOT" title="Negates all selected conditions"/>
    <input type="button" class="controlBtn btnDel" disabled="disabled" value="DEL" title="Deletes the selected condition from the list"/>
    <input type="button" class="controlBtn btnBreak" disabled="disabled" value="BREAK" title="Breaks up a logical grouping (AND or OR) back into its individual pieces"/>
</div>
<br/>
<div class="conditionList">

</div>
<br/><br/>
<input type="button" class="controlBtn btnSearch" disabled="disabled" value="Search"/>
</div>
<br><div class="ui-widget"><u>Search History</u></div>
<div class="searchHistory"></div>
