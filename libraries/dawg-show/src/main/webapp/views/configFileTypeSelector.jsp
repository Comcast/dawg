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
<!--  The export option selector to select the required type for property file -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<div class="config-file-options" title="Export configuration file as">
    <input type="radio" name="config-radio-group" class="json-radio-button" value="json">
    <label>JSON</label>
    <br>
    <input type="radio" name="config-radio-group" class="catsxml-radio-button" value="catsvisionxml">
    <label>CatsVision XML</label>
    <br>
    <input type="radio" name="config-radio-group" class="toolxml-radio-button" value="toolboxxml">
    <label>ToolBox XMl</label>
</div>
