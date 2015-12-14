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
<%--The directional pad for the generic remote --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div>
    <table class="genericRemoteKeyTable">
        <tbody>
            <tr class="genericRemoteKeyRow">
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="svolup" onmousedown="clickRemoteButton(event,'VOLUP', true)" onmouseup="clickRemoteButton(event,'VOLUP', false)"> VOL UP</a>
                </td>
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="sup" onmousedown="clickRemoteButton(event,'UP', true)" onmouseup="clickRemoteButton(event,'UP', false)" style="background-color:#2EB8E6">UP</a>
                </td>
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="schup" onmousedown="clickRemoteButton(event,'CHUP', true)" onmouseup="clickRemoteButton(event,'CHUP', false)"> CH UP</a>
                </td>
            </tr>
            <tr class="genericRemoteKeyRow">
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="sleft" onmousedown="clickRemoteButton(event,'LEFT', true)" onmouseup="clickRemoteButton(event,'LEFT', false)" style="background-color:#2EB8E6">LEFT</a>
                </td>
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="sselect" onmousedown="clickRemoteButton(event,'SELECT', true)" onmouseup="clickRemoteButton(event,'SELECT', false)"> SELECT/OK</a>
                </td>
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="sright" onmousedown="clickRemoteButton(event,'RIGHT', true)" onmouseup="clickRemoteButton(event,'RIGHT', false)" style="background-color:#2EB8E6">RIGHT</a>
                </td>
            </tr>
            <tr class="genericRemoteKeyRow">
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="svoldn" onmousedown="clickRemoteButton(event,'VOLDN', true)" onmouseup="clickRemoteButton(event,'VOLDN', false)">VOL DWN</a>
                </td>
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="sdown" onmousedown="clickRemoteButton(event,'DOWN', true)" onmouseup="clickRemoteButton(event,'DOWN', false)" style="background-color:#2EB8E6">DOWN</a>
                </td>
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="schdn" onmousedown="clickRemoteButton(event,'CHDN', true)" onmouseup="clickRemoteButton(event,'CHDN', false)">CH DWN</a>
                </td>
            </tr>
            <tr class="genericRemoteKeyRow">
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="sguide" onmousedown="clickRemoteButton(event,'GUIDE', true)" onmouseup="clickRemoteButton(event,'GUIDE', false)">GUIDE</a>
                </td>
                <td class="genericRemoteButtonStyle genericRemoteKeyCol"></td>
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="smenu" onmousedown="clickRemoteButton(event,'MENU', true)" onmouseup="clickRemoteButton(event,'MENU', false)"> MENU</a>
                </td>
            </tr>
        </tbody>
    </table>
</div>
