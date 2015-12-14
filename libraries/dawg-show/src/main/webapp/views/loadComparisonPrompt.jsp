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
Prompt for loading a comparison
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
    <script type="text/javascript" src="<c:url value="/js/MultiFileSelector.js" />"></script>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/loadComparisonPrompt.css" />" />
</head>
<div style="background-color:#5a5d5f; text-align:right">
    <img class="closeImage" src='<c:url value="/images/close.png" />' style="width: 30px; height: 30px" alt=""/>
</div>
<div style="background-color:#5a5d5f; border-bottom:2px solid black;padding-bottom:5px">
    <font size="5px" color="#dedbdb"><b>Choose the config file and the image file.</b></font>
</div>
<div style="padding:30px;background-color:#dedbdb;overflow:hidden;padding-top: 10px;">
    <label>
        <input type="radio" name="fileSelect" class="singleFileSelectionInput" checked>Single File Selection
    </label>
    <label>
        <input type="radio" name="fileSelect" class="multiFileSelectionInput">Multiple File Selection
    </label>
    <div class="multipleFileSelectionDiv">
        <div style="float: left">Choose multiple files:</div>
        <input class="multipleConfigFilesInput" type="file" multiple />
        <br/>
        <pre class="fileList"></pre>
    </div>
    <div class="singleFileSelectionDiv">
        <div style="width:100%;overflow:hidden"" >
            <div style="float:left">Configuration file:</div> <input class="inpConfigFile" style="font-size:17px;float:right;padding-left:10px" type="file" />
        </div>
        <br/>
        <div style="width:100%;overflow:hidden"">
            <div style="float:left">Image file:</div> <input class="inpImageFile" style="font-size:17px;float:right;padding-left:10px" type="file" />
        </div>
    </div>
    <input type="button" value="Load" class="btnLoadComparison" style="width:100%"/>
</div>
<script type="text/javascript">
        MultiFileSelector.bind($('.singleFileSelectionInput'),$('.multiFileSelectionInput'),$('.singleFileSelectionDiv'), $('.multipleFileSelectionDiv'), $('.multipleConfigFilesInput'),$('.fileList'));
</script>
