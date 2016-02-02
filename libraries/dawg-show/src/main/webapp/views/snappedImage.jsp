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
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/taglib/stringEscapeUtil.tld" prefix="seu"%>
<%@page isELIgnored="false" %>

<html>
    <head>
        <title>Image</title>
        <jsp:include page="/views/globalVars.jsp" />
        <jsp:include page="/views/configFileTypeSelector.jsp" />
        <script type="text/javascript" src="<c:url value="/js/jquery-1.7.2.min.js" />"></script>
        <script src="https://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
        <script type="text/javascript" src="<c:url value="/js/ImageWorkspace.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/RegionManager.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/HTMLUtil.js" />"></script>
        <link rel="stylesheet" type="text/css" href='<c:url value="/css/operations.css" />'>
        <link rel="stylesheet" href="https://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
        <link rel="stylesheet" type="text/css" href='<c:url value="/css/regionManager.css" />'>
        <link rel="stylesheet" type="text/css" href='<c:url value="/css/regionDisplay.css" />'>
    </head>
    <body class="snappedImageBody">
        <div>
            <div id="operationsDiv" class="operationsDiv">
                <div class="operations save">Save Image</div>
                <div class="operations newic">New Image Compare</div>
                <div class="operations newocr">New OCR Compare</div>
                <div class="operations allIcCompare">Test all Image Compares</div>
            </div>
            <div id="imageDiv" class="imageDiv">
                <canvas id="imgWorkspace" class="imgWorkspace" width="704" height="480" />
            </div>
            <div id="regionContainer" class="regionContainer">
                <div class="regionManager" id="regionManager" style="display:none">
                    <input type="button" class="btnExport" value="Export" />
                    <br>
                    <input class="btnPerformComparison" type="button" value="Compare" />
                    <div class="compareResult"></div>
                    <br>
                    <div class="compareNameDiv">
                        <div class="compareNameLabel">Compare Name:</div>
                        <input type="text" class="inpCompareName" />
                    </div>
                    <br/>
                    <br/>
                    <div class="sliderDiv">
                        <div class="sliderLabel">Timeout:</div>
                        <div class="slider toSlider"></div>
                        <div class="sliderValue toSliderVal"></div>
                    </div>
                    <div class="regionListDiv" style="display:none">
                        <div class="regionNameLabel">Region Name:</div>
                        <input class="inpRegionName" type="text" id="inpRegionName" />
                        <input class="btnAddRegion" type="button" id="btnAddRegion" value="Add Region" />
                        <br>
                    </div>
                    <br>
                    <div class="regionDetails" style="display:none">
                        <div style="width:100%, padding-bottom:2px">
                            <div class="regionNameLabel">Name:</div>
                            <input type="text" class="inpName" />
                            <input type="button" class="btnDelete" value="Delete Region" />
                            <br>
                        </div>
                        <br>
                        <div id="boundInfo" class="boundInfo">
                            <div class="boundsLabel"><b>Bounds</b>
                            </div>
                            <label class="detailLabel">x:</label>
                            <input class="regionX detailSection" type="text" id="rx" />
                            <label class="detailLabel">y:</label>
                            <input class="regionY detailSection" type="text" id="ry" />
                            <br/>
                            <br/>
                            <label class="detailLabel">width:</label>
                            <input class="regionW detailSection" type="text" id="rw" />
                            <label class="detailLabel">height:</label>
                            <input class="regionH detailSection" type="text" id="rh">
                        </div>
                        <br/>
                        <div id="tolerance" class="tolerance">
                            <div align="center"><b>Tolerance</b>
                            </div>
                            <div class="sliderDiv">
                                <div class="sliderLabel">Match %:</div>
                                <div class="slider mpSlider"></div>
                                <div class="sliderValue mpSliderVal"></div>
                            </div>
                            <div class="sliderDiv xSliderDiv">
                                <div class="sliderLabel">x:</div>
                                <div class="slider xSlider"></div>
                                <div class="sliderValue xSliderVal"></div>
                            </div>
                            <div class="sliderDiv ySliderDiv">
                                <div class="sliderLabel">y:</div>
                                <div class="slider ySlider"></div>
                                <div class="sliderValue ySliderVal"></div>
                            </div>
                            <div class="sliderDiv redSliderDiv">
                                <div class="sliderLabel">Red:</div>
                                <div class="slider redSlider"></div>
                                <div class="sliderValue redSliderVal"></div>
                            </div>
                            <div class="sliderDiv greenSliderDiv">
                                <div class="sliderLabel">Green:</div>
                                <div class="slider greenSlider"></div>
                                <div class="sliderValue greenSliderVal"></div>
                            </div>
                            <div class="sliderDiv blueSliderDiv">
                                <div class="sliderLabel">Blue:</div>
                                <div class="slider blueSlider"></div>
                                <div class="sliderValue blueSliderVal"></div>
                            </div>
                        </div>
                        <br/>
                        <div class="divExpText">
                            <span>Expected Text:</span>
                            <input class="inpExpText" type="text" />
                        </div>
                    </div>
                </div>
            </div>
            <div class="regionTable" id="regionTable"></div>
        </div>
    </body>
    <script type="text/javascript">
            var imgWorkspace = ImageWorkspaceFactory.create($('#imgWorkspace')[0], '${imageId}', $('#boundInfo'));
            RegionManager.bind($('.regionContainer'), imgWorkspace, '${imageId}', $('#operationsDiv'), '${deviceId}');
            <c:if test="${loadCompare}">
                  var loadedRegions = new Array();
                  <c:forEach items="${regions}" var="region">
                                                var region = new Region();
                                                region.name = '${region.name}';
                                                region.x = ${region.x};
                                                region.y = ${region.y};
                                                region.width = ${region.width};
                                                region.height = ${region.height};
                                                region.matchPercent = ${region.matchPercent};
                                                region.yTolerance = ${region.yTolerance};
                                                region.xTolerance = ${region.xTolerance};
                                                region.redTolerance = ${region.redTolerance};
                                                region.greenTolerance = ${region.greenTolerance};
                                                region.blueTolerance = ${region.blueTolerance};
            <c:if test="${region.expectedText != null}">
                  region.expectedText = '${seu:escapeJS(region.expectedText)}';
                  </c:if>
                  loadedRegions.push(region);
                  </c:forEach>
                  RegionManager.show($('.regionContainer'), imgWorkspace, '${compType}', '${compName}');
                  RegionManager.setRegions(loadedRegions);
                  RegionManager.populateRegionList($('.regionContainer'), imgWorkspace);
                  </c:if>
    </script>
</html>
