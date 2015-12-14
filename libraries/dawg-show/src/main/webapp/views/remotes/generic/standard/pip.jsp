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
<%--The PIP-On/Off, PIP Swap, PIP Move, PIP Channel Up, Setup, PIP Channel Down keys of generic remote --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
    <div>
        <table class="genericRemoteKeyTable">
            <tbody>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="spiponoff" onmousedown="clickRemoteButton(event,'PIPONOFF', true)" onmouseup="clickRemoteButton(event,'PIPONOFF', false)"> PIP-ON/OFF</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="spipswap" onmousedown="clickRemoteButton(event,'PIPSWAP', true)" onmouseup="clickRemoteButton(event,'PIPSWAP', false)"> PIP SWAP</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="spipmove" onmousedown="clickRemoteButton(event,'PIPMOVE', true)" onmouseup="clickRemoteButton(event,'PIPMOVE', false)"> PIP MOVE</a>
                    </td>
                </tr>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="spipchup" onmousedown="clickRemoteButton(event,'PIPCHUP', true)" onmouseup="clickRemoteButton(event,'PIPCHUP', false)"> PIP CH UP</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="ssetup" onmousedown="clickRemoteButton(event,'SETUP', true)" onmouseup="clickRemoteButton(event,'SETUP', false)"> SETUP</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="spipchdn" onmousedown="clickRemoteButton(event,'PIPCHDN', true)" onmouseup="clickRemoteButton(event,'PIPCHDN', false)"> PIP CH DN</a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</html>
