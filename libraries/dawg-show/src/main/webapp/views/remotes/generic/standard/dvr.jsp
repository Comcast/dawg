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
<%--ON Demand, XFINITY, My DVR, Rewind, Play, Fast Forward, Stop. Pause, Skip Forward, Reply, Live key of generic remote --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <div>
        <table class="genericRemoteKeyTable">
            <tbody>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sondemand" onmousedown="clickRemoteButton(event,'ONDEMAND', true)" onmouseup="clickRemoteButton(event,'ONDEMAND',false)">ON DEMAND</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sxfinity" onmousedown="clickRemoteButton(event,'XFINITY', true)" onmouseup="clickRemoteButton(event,'XFINITY', false)">XFINITY</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="smydvr" onmousedown="clickRemoteButton(event,'MYDVR', true)" onmouseup="clickRemoteButton(event,'MYDVR', false)"> MY DVR</a>
                    </td>
                </tr>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="srew" onmousedown="clickRemoteButton(event,'REW', true)" onmouseup="clickRemoteButton(event,'REW', false)" > REWIND</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="splay" onmousedown="clickRemoteButton(event,'PLAY', true)" onmouseup="clickRemoteButton(event,'PLAY', false)"> PLAY</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sff" onmousedown="clickRemoteButton(event,'FF', true)" onmouseup="clickRemoteButton(event,'FF', false)"> FAST FWD</a>
                    </td>
                </tr>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="srec" class="genericRemoteKey" onmousedown="clickRemoteButton(event,'REC', true)" onmouseup="clickRemoteButton(event,'REC', false)">RECORD</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sstop" onmousedown="clickRemoteButton(event,'STOP', true)" onmouseup="clickRemoteButton(event,'STOP', false)" > STOP</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="spause" onmousedown="clickRemoteButton(event,'PAUSE', true)" onmouseup="clickRemoteButton(event,'PAUSE', false)"> PAUSE</a>
                    </td>
                </tr>
                <tr class="genericRemoteKeyRow">

                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sskipfwd" onmousedown="clickRemoteButton(event,'SKIPFWD', true)" onmouseup="clickRemoteButton(event,'SKIPFWD',false)"> SKIP FWD</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sreplay" onmousedown="clickRemoteButton(event,'REPLAY', true)" onmouseup="clickRemoteButton(event,'REPLAY', false)"> REPLAY</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="slive" class="genericRemoteKey" onmousedown="clickRemoteButton(event,'LIVE', true)" onmouseup="clickRemoteButton(event,'LIVE', false)"> LIVE</a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</html>
