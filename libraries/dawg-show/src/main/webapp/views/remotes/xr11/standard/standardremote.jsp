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
The mini remote image
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%; height:100%">
    <script type="text/javascript">
            var STD_REMOTE_ASPECT_RATIO = .313;
    </script>

    <img id="bg" src='<c:url value="/images/remotes/xr11/bg.png" />' alt="" style="width:100%;height:100%;left:0%;top:0%;position:absolute" />

    <img id="sone"   src='<c:url value="/images/remotes/xr11/keys/1.png" />' onmousedown="clickRemoteButton(event,'ONE' ,  true)" onmouseup="clickRemoteButton(event,'ONE',   false)" alt="" style="width:19.7%;left:15.3%;top:70.0%;position:absolute"/>
    <img id="stwo"   src='<c:url value="/images/remotes/xr11/keys/2.png" />' onmousedown="clickRemoteButton(event,'TWO',   true)" onmouseup="clickRemoteButton(event,'TWO',   false)" alt="" style="width:19.7%;left:40.1%;top:70.0%;position:absolute"/>
    <img id="sthree" src='<c:url value="/images/remotes/xr11/keys/3.png" />' onmousedown="clickRemoteButton(event,'THREE', true)" onmouseup="clickRemoteButton(event,'THREE', false)" alt="" style="width:19.7%;left:65.0%;top:70.0%;position:absolute"/>
    <img id="sfour"  src='<c:url value="/images/remotes/xr11/keys/4.png" />' onmousedown="clickRemoteButton(event,'FOUR',  true)" onmouseup="clickRemoteButton(event,'FOUR',  false)" alt="" style="width:19.7%;left:15.3%;top:75.3%;position:absolute"/>
    <img id="sfive"  src='<c:url value="/images/remotes/xr11/keys/5.png" />' onmousedown="clickRemoteButton(event,'FIVE',  true)" onmouseup="clickRemoteButton(event,'FIVE',  false)" alt="" style="width:19.7%;left:40.1%;top:75.3%;position:absolute"/>
    <img id="ssix"   src='<c:url value="/images/remotes/xr11/keys/6.png" />' onmousedown="clickRemoteButton(event,'SIX',   true)" onmouseup="clickRemoteButton(event,'SIX',   false)" alt="" style="width:19.7%;left:65.0%;top:75.3%;position:absolute"/>
    <img id="sseven" src='<c:url value="/images/remotes/xr11/keys/7.png" />' onmousedown="clickRemoteButton(event,'SEVEN', true)" onmouseup="clickRemoteButton(event,'SEVEN', false)" alt="" style="width:19.7%;left:15.3%;top:80.5%;position:absolute"/>
    <img id="seight" src='<c:url value="/images/remotes/xr11/keys/8.png" />' onmousedown="clickRemoteButton(event,'EIGHT', true)" onmouseup="clickRemoteButton(event,'EIGHT', false)" alt="" style="width:19.7%;left:40.1%;top:80.5%;position:absolute"/>
    <img id="snine"  src='<c:url value="/images/remotes/xr11/keys/9.png" />' onmousedown="clickRemoteButton(event,'NINE',  true)" onmouseup="clickRemoteButton(event,'NINE',  false)" alt="" style="width:19.7%;left:65.0%;top:80.5%;position:absolute"/>
    <img id="szero"  src='<c:url value="/images/remotes/xr11/keys/0.png" />' onmousedown="clickRemoteButton(event,'ZERO',  true)" onmouseup="clickRemoteButton(event,'ZERO',  false)" alt="" style="width:19.7%;left:40.3%;top:85.8%;position:absolute"/>

    <img id="skeyboard" src='<c:url value="/images/keyboardSel.png" />' onclick="clickKeyboard(this)" alt="" title="Click here to toggle keyboard control of the remote" style="width:14.6%;height:4%;left:17.5%;top:6.3%;position:absolute;"/>
    <img id="skeyhelp"  src='<c:url value="/images/key_help.png" />' title="Click to view the keyboard to remote key mapping" onclick="displayKeyMappingOverlay(event, true)" alt="" style="width:8.2%;height:2.2%;left:70.7%;top:86.7%;position:absolute"/>
    <!-- TODO: Add a hard power button -->

    <img id="sa"  src='<c:url value="/images/remotes/xr11/keys/A.png" />' onmousedown="clickRemoteButton(event,'A',  true)" onmouseup="clickRemoteButton(event,'A',  false)" alt="" style="width:11.8%;left:15.8%;top:65.6%;position:absolute"/>
    <img id="sb"  src='<c:url value="/images/remotes/xr11/keys/B.png" />' onmousedown="clickRemoteButton(event,'B',  true)" onmouseup="clickRemoteButton(event,'B',  false)" alt="" style="width:10.3%;left:35.3%;top:65.8%;position:absolute"/>
    <img id="sc"  src='<c:url value="/images/remotes/xr11/keys/C.png" />' onmousedown="clickRemoteButton(event,'C',  true)" onmouseup="clickRemoteButton(event,'C',  false)" alt="" style="width:09.5%;left:55.2%;top:65.8%;position:absolute"/>
    <img id="sd"  src='<c:url value="/images/remotes/xr11/keys/D.png" />' onmousedown="clickRemoteButton(event,'D',  true)" onmouseup="clickRemoteButton(event,'D',  false)" alt="" style="width:12.5%;left:71.7%;top:65.3%;position:absolute"/>

    <img id="sallpower"  src='<c:url value="/images/remotes/xr11/keys/AllPower.png" />' onclick="clickPower(this)" alt="" style="width:14.7%;left:68.3%;top:6.2%;position:absolute"/>

    <img id="sguide"  src='<c:url value="/images/remotes/xr11/keys/Guide.png" />' onmousedown="clickRemoteButton(event,'GUIDE', true)" onmouseup="clickRemoteButton(event,'GUIDE', false)" alt="" style="width:20.2%;left:15.3%;top:35.6%;position:absolute"/>
    <img id="spgup"   src='<c:url value="/images/remotes/xr11/keys/Page up.png" />' onmousedown="clickRemoteButton(event,'PGUP', true)" onmouseup="clickRemoteButton(event,'PGUP', false)" alt="" style="width:20.2%;left:64.5%;top:35.6%;position:absolute"/>
    <img id="sup"     src='<c:url value="/images/remotes/xr11/keys/Up arrow.png" />' onmousedown="clickRemoteButton(event,'UP', true)" onmouseup="clickRemoteButton(event,'UP', false)" alt="" style="width:58.8%;left:20.6%;top:40.9%;position:absolute"/>
    <img id="sleft"   src='<c:url value="/images/remotes/xr11/keys/Left Arrow.png" />' onmousedown="clickRemoteButton(event,'LEFT', true)" onmouseup="clickRemoteButton(event,'LEFT', false)" alt="" style="width:20.8%;left:18.3%;top:41.5%;position:absolute"/>
    <img id="sright"  src='<c:url value="/images/remotes/xr11/keys/Right Arrow.png" />' onmousedown="clickRemoteButton(event,'RIGHT', true)" onmouseup="clickRemoteButton(event,'RIGHT', false)" alt="" style="width:20.8%;left:60.0%;top:41.5%;position:absolute"/>
    <img id="sdown"   src='<c:url value="/images/remotes/xr11/keys/Down arrow.png" />' onmousedown="clickRemoteButton(event,'DOWN', true)" onmouseup="clickRemoteButton(event,'DOWN', false)" alt="" style="width:58.8%;left:20.6%;top:53.3%;position:absolute"/>

    <img id="slast"   src='<c:url value="/images/remotes/xr11/keys/Last.png" />' onmousedown="clickRemoteButton(event,'LAST', true)" onmouseup="clickRemoteButton(event,'LAST', false)"alt="" style="width:20.2%;left:15.3%;top:59.8%;position:absolute"/>
    <img id="spgdn"   src='<c:url value="/images/remotes/xr11/keys/Page Down.png" />' onmousedown="clickRemoteButton(event,'PGDN', true)" onmouseup="clickRemoteButton(event,'PGDN', false)" alt="" style="width:20.2%;left:64.5%;top:59.8%;position:absolute"/>
    <img id="sinfo"   src='<c:url value="/images/remotes/xr11/keys/Info.png" />' onmousedown="clickRemoteButton(event,'INFO', true)" onmouseup="clickRemoteButton(event,'INFO', false)" alt="" style="width:29%;left:35.5%;top:60.7%;position:absolute"/>
    <img id="sselect" src='<c:url value="/images/remotes/xr11/keys/Ok.png" />' onmousedown="clickRemoteButton(event,'SELECT', true)" onmouseup="clickRemoteButton(event,'SELECT', false)" alt="" style="width:25.4%;left:37.2%;top:45.7%;position:absolute"/>


    <img id="srew" src='<c:url value="/images/remotes/xr11/keys/Rewind.png" />' onmousedown="clickRemoteButton(event,'REW', true)" onmouseup="clickRemoteButton(event,'REW', false)" alt="" style="width:20.2%;left:15.3%;top:25%;position:absolute"/>
    <img id="splay" src='<c:url value="/images/remotes/xr11/keys/Play-Pause.png" />' onmousedown="clickRemoteButton(event,'PLAY', true)" onmouseup="clickRemoteButton(event,'PLAY', false)" alt="" style="width:29%;left:35.5%;top:25%;position:absolute"/>
    <img id="sff" src='<c:url value="/images/remotes/xr11/keys/Fast Forward.png" />' onmousedown="clickRemoteButton(event,'SKIPFWD', true)" onmouseup="clickRemoteButton(event,'FF', false)" alt="" style="width:20.2%;left:64.5%;top:25%;position:absolute"/>
    <img id="sexit" src='<c:url value="/images/remotes/xr11/keys/Exit.png" />' onmousedown="clickRemoteButton(event,'EXIT', true)" onmouseup="clickRemoteButton(event,'EXIT', false)" alt="" style="width:20.2%;left:15.3%;top:30.2%;position:absolute"/>
    <img id="srec" src='<c:url value="/images/remotes/xr11/keys/Record.png" />' onmousedown="clickRemoteButton(event,'REC', true)" onmouseup="clickRemoteButton(event,'REC', false)" alt="" style="width:20.2%;left:64.5%;top:30.2%;position:absolute"/>
    <!-- Need to look into voice button -->
    <img id="svoice" src='<c:url value="/images/remotes/xr11/keys/Voice.png" />' alt="" style="width:29%;left:35.5%;top:30.2%;position:absolute"/>
    <img id="smenu" src='<c:url value="/images/remotes/xr11/keys/Xfinity.png" />' onmousedown="clickRemoteButton(event,'MENU', true)" onmouseup="clickRemoteButton(event,'MENU', false)" alt="" style="width:29%;left:35.5%;top:35.5%;position:absolute"/>

    <img id="svolup" src='<c:url value="/images/remotes/xr11/keys/Volume up.png" />'   onmousedown="clickRemoteButton(event,'VOLUP', true)" onmouseup="clickRemoteButton(event,'VOLUP', false)" alt="" style="width:20.4%;left:14.4%;top:12.4%;position:absolute"/>
    <img id="schup" src='<c:url value="/images/remotes/xr11/keys/Channel up.png" />'   onmousedown="clickRemoteButton(event,'CHUP', true)"  onmouseup="clickRemoteButton(event,'CHUP', false)"  alt="" style="width:20.4%;left:65.2%;top:12.4%;position:absolute"/>
    <img id="svoldn" src='<c:url value="/images/remotes/xr11/keys/Volume down.png" />' onmousedown="clickRemoteButton(event,'VOLDN', true)" onmouseup="clickRemoteButton(event,'VOLDN', false)" alt="" style="width:20.8%;left:14.4%;top:19.9%;position:absolute"/>
    <img id="schdn" src='<c:url value="/images/remotes/xr11/keys/Channel down.png" />' onmousedown="clickRemoteButton(event,'CHDN', true)"  onmouseup="clickRemoteButton(event,'CHDN', false)"  alt="" style="width:20.4%;left:65.2%;top:19.9%;position:absolute"/>
    <img id="smute" src='<c:url value="/images/remotes/xr11/keys/Mute.png" />' onclick="clickMute(this)" alt="" style="width:14.6%;left:42.7%;top:12.2%;position:absolute"/>
    <img id="sreplay" src='<c:url value="/images/remotes/xr11/keys/Replay.png" />' onmousedown="clickRemoteButton(event,'REPLAY', true)" onmouseup="clickRemoteButton(event,'REPLAY', false)" alt="" style="width:14.6%;left:42.7%;top:18.9%;position:absolute"/>

    <img id="ssetup" src='<c:url value="/images/remotes/xr11/keys/Setup.png" />' alt="" style="width:8.2%;left:21.1%;top:86.7%;position:absolute"/>

    <!-- labels -->
    <img id="sapower" src='<c:url value="/images/remotes/xr11/keys/AllPower TEXT.png" />' style="width:19.4%;left:66.2%;top:10.8%;position:absolute;" />
    <img id="shelp" src='<c:url value="/images/remotes/xr11/keys/Help TEXT.png" />' style="width:8.9%;left:17.7%;top:68.6%;position:absolute;" />
    <img id="smenu" src='<c:url value="/images/remotes/xr11/keys/Menu TEXT.png" />' style="width:12.2%;left:44.1%;top:39.5%;position:absolute;" />
    <img id="smute" src='<c:url value="/images/remotes/xr11/keys/Mute TEXT.png" />' style="width:11.0%;left:44.6%;top:16.6%;position:absolute;" />
    <img id="sreplay" src='<c:url value="/images/remotes/xr11/keys/Replay TEXT.png" />' style="width:14.9%;left:42.7%;top:23.3%;position:absolute;" />
    <img id="ssetup" src='<c:url value="/images/remotes/xr11/keys/Setup TEXT.png" />' style="width:10.8%;left:20.1%;top:89.0%;position:absolute;" />
    <img id="sinput" src='<c:url value="/images/remotes/xr11/keys/TV Input TEXT.png" />' style="width:16.8%;left:66.9%;top:89.0%;position:absolute;" />

    <img id="svol" src='<c:url value="/images/remotes/xr11/keys/Volume TEXT.png" />' style="width:20.4%;left:14.4%;top:16.5%;position:absolute;" />
    <img id="sch" src='<c:url value="/images/remotes/xr11/keys/Channel TEXT.png" />' style="width:20.4%;left:65.2%;top:16.5%;position:absolute;" />

</html>
