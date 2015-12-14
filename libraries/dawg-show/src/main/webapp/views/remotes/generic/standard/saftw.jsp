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
<%--Source, Applist, Fun A, Text, Watch TV keys of generic remote --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
    <div>
        <table class="genericRemoteKeyTable">
            <tbody>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="ssource" onmousedown="clickRemoteButton(event,'SOURCE', true)" onmouseup="clickRemoteButton(event,'SOURCE', false)"> SOURCE</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sapplist" onmousedown="clickRemoteButton(event,'APPLIST', true)" onmouseup="clickRemoteButton(event,'APPLIST',false)"> APPS</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sfun_a" onmousedown="clickRemoteButton(event,'FUN_A', true)" onmouseup="clickRemoteButton(event,'FUN_A', false)"> FUN A</a>
                    </td>
                </tr>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="stext" onmousedown="clickRemoteButton(event,'TEXT', true)" onmouseup="clickRemoteButton(event,'TEXT', false)"> TEXT </a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="swatch_tv" onmousedown="clickRemoteButton(event,'WATCH_TV', true)" onmouseup="clickRemoteButton(event,'WATCH_TV', false)"> WATCH TV</a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</html>
