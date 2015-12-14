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
<%--Previous, Next, Reverse, Freeze, Photo, Music, Video, Subtitle, Sleep, Audio keys of genetic remote --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
    <div>
        <table class="genericRemoteKeyTable">
            <tbody>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sprev" onmousedown="clickRemoteButton(event,'PREV', true)" onmouseup="clickRemoteButton(event,'PREV', false)"> PREV</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="snext" onmousedown="clickRemoteButton(event,'NEXT', true)" onmouseup="clickRemoteButton(event,'NEXT', false)" > NEXT</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sreverse" onmousedown="clickRemoteButton(event,'REVERSE', true)" onmouseup="clickRemoteButton(event,'REVERSE', false)">REVERSE</a>
                    </td>
                </tr>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a  id="sfreeze" onmousedown="clickRemoteButton(event,'FREEZE', true)" onmouseup="clickRemoteButton(event,'FREEZE', false)">FREEZE</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="sphoto" onmousedown="clickRemoteButton(event,'PHOTO', true)" onmouseup="clickRemoteButton(event,'PHOTO', false)">PHOTO</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="smusic" onmousedown="clickRemoteButton(event,'MUSIC', true)" onmouseup="clickRemoteButton(event,'MUSIC', false)">MUSIC</a>
                    </td>
                </tr>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a  id="svideo" onmousedown="clickRemoteButton(event,'VIDEO', true)" onmouseup="clickRemoteButton(event,'VIDEO', false)">VIDEO</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="ssubtitle" onmousedown="clickRemoteButton(event,'SUBTITLE', true)" onmouseup="clickRemoteButton(event,'SUBTITLE', false)">SUBTITLE</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="ssleep" onmousedown="clickRemoteButton(event,'SLEEP', true)" onmouseup="clickRemoteButton(event,'SLEEP', false)">SLEEP</a>
                    </td>
                </tr>
                <tr class="genericRemoteKeyRow">
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a  id="saudio" onmousedown="clickRemoteButton(event,'AUDIO', true)" onmouseup="clickRemoteButton(event,'AUDIO', false)">AUDIO</a>
                    </td>
                    <td class="genericRemoteButtonStyle genericRemoteKeyCol">
                        <a id="skeyMapHelp" class="skeyMapHelp">
                            <img id="skeyHelp" class="skeyHelp" src="/dawg-show/images/key_help.png" title="Click to view the keyboard to remote key mapping" onclick="displayKeyMappingOverlay(event, true)" />
                        </a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</html>
