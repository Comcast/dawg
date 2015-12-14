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
<%-- A, B, C, D, Page Up, Page Down keys of generic remote --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
    <table class="genericRemoteKeyTable">
        <tbody>
            <tr class="genericRemoteKeyRow">
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="sa" onmousedown="clickRemoteButton(event,'A', true)" onmouseup="clickRemoteButton(event,'A', false)" style="background-color: yellow;"> A</a>
                </td>
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="sb" onmousedown="clickRemoteButton(event,'B', true)" onmouseup="clickRemoteButton(event,'B', false)" style="background-color: #70B8FF;"> B</a>
                </td>
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="sc" onmousedown="clickRemoteButton(event,'C', true)" onmouseup="clickRemoteButton(event,'C', false)" style="background-color: red;"> C</a>
                </td>
            </tr>
            <tr class="genericRemoteKeyRow">
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="spgup" onmousedown="clickRemoteButton(event,'PGUP', true)" onmouseup="clickRemoteButton(event,'PGUP', false)"> PAGE UP </a>
                </td>
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="sd" onmousedown="clickRemoteButton(event,'D', true)" onmouseup="clickRemoteButton(event,'D', false)" style="background-color: green;"> D</a>
                </td>
                <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    <a id="spgdn" onmousedown="clickRemoteButton(event,'PGDN', true)" onmouseup="clickRemoteButton(event,'PGDN', false)"> PAGE DOWN</a>
                </td>
            </tr>
        </tbody>
    </table>
</html>
