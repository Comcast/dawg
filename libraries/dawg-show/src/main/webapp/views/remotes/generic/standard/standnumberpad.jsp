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
<%--The number pad for the generic remote--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <div>
        <table class="genericRemoteKeyTable">
            <tbody>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sone" onmousedown="clickRemoteButton(event,'ONE', true)" onmouseup="clickRemoteButton(event,'ONE', false)" class="numberPadStyle">1</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="stwo" onmousedown="clickRemoteButton(event,'TWO', true)" onmouseup="clickRemoteButton(event,'TWO', false)" class="numberPadStyle">2</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sthree" onmousedown="clickRemoteButton(event,'THREE', true)" onmouseup="clickRemoteButton(event,'THREE', false)" class="numberPadStyle">3</a>
                    </td>
                </tr>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sfour" onmousedown="clickRemoteButton(event,'FOUR', true)" onmouseup="clickRemoteButton(event,'FOUR', false)" class="numberPadStyle">4</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sfive" onmousedown="clickRemoteButton(event,'FIVE', true)" onmouseup="clickRemoteButton(event,'FIVE', false)" class="numberPadStyle">5</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="ssix" onmousedown="clickRemoteButton(event,'SIX', true)" onmouseup="clickRemoteButton(event,'SIX', false)" class="numberPadStyle">6</a>
                    </td>
                </tr>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sseven" onmousedown="clickRemoteButton(event,'SEVEN', true)" onmouseup="clickRemoteButton(event,'SEVEN', false)" class="numberPadStyle">7</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="seight" onmousedown="clickRemoteButton(event,'EIGHT', true)" onmouseup="clickRemoteButton(event,'EIGHT', false)" class="numberPadStyle">8</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="snine" onmousedown="clickRemoteButton(event,'NINE', true)" onmouseup="clickRemoteButton(event,'NINE', false)" class="numberPadStyle">9</a>
                    </td>
                </tr>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sdot" onmousedown="clickRemoteButton(event,'DOT', true)" onmouseup="clickRemoteButton(event,'DOT', false)"> DOT</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="szero" onmousedown="clickRemoteButton(event,'ZERO', true)" onmouseup="clickRemoteButton(event,'ZERO', false)" class="numberPadStyle">0</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="shdzoom" onmousedown="clickRemoteButton(event,'HDZOOM', true)" onmouseup="clickRemoteButton(event,'HDZOOM', false)"> HD ZOOM</a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</html>
