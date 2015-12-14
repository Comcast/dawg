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
The guide keys and navigation for the standard remote
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%">
<%
String rem = (String) request.getAttribute("rem");
rem = rem == null ? "s" : rem;
%>
<img id="<%=rem%>guide" src='<c:url value="/images/remotes/bcm-7437-1/keys/guide.png" />' onmousedown="clickRemoteButton(event,'GUIDE', true)" onmouseup="clickRemoteButton(event,'GUIDE', false)" alt="" style="width: 23%; left: 1%; top: 0%; position: absolute" />
<img id="<%=rem%>menu" src='<c:url value="/images/remotes/bcm-7437-1/keys/menu.png" />' onmousedown="clickRemoteButton(event,'MENU', true)" onmouseup="clickRemoteButton(event,'MENU', false)" alt="" style="width: 23%; left: 76%; top: 0%; position: absolute" />
<img id="<%=rem%>up" src='<c:url value="/images/remotes/bcm-7437-1/keys/up.png" />' onmousedown="clickRemoteButton(event,'UP', true)" onmouseup="clickRemoteButton(event,'UP', false)" alt="" style="width: 12%; height: 30%; left: 45%; top: 0%; position: absolute" />
<img id="<%=rem%>left" src='<c:url value="/images/remotes/bcm-7437-1/keys/left.png" />' onmousedown="clickRemoteButton(event,'LEFT', true)" onmouseup="clickRemoteButton(event,'LEFT', false)" alt="" style="width: 23%; left: 11%; top: 39%; position: absolute" />
<img id="<%=rem%>right" src='<c:url value="/images/remotes/bcm-7437-1/keys/right.png" />' onmousedown="clickRemoteButton(event,'RIGHT', true)" onmouseup="clickRemoteButton(event,'RIGHT', false)" alt="" style="width: 23%; left: 67%; top: 39%; position: absolute" />
<img id="<%=rem%>down" src='<c:url value="/images/remotes/bcm-7437-1/keys/down.png" />' onmousedown="clickRemoteButton(event,'DOWN', true)" onmouseup="clickRemoteButton(event,'DOWN', false)" alt="" style="width: 12%; height: 30%; left: 45%; top: 65%; position: absolute" />
<img id="<%=rem%>info" src='<c:url value="/images/remotes/bcm-7437-1/keys/info.png" />' onmousedown="clickRemoteButton(event,'INFO', true)" onmouseup="clickRemoteButton(event,'INFO', false)" alt="" style="width: 23%; left: 0%; top: 68%; position: absolute" />
<img id="<%=rem%>exit" src='<c:url value="/images/remotes/bcm-7437-1/keys/clear.png" />' onmousedown="clickRemoteButton(event,'EXIT', true)" onmouseup="clickRemoteButton(event,'EXIT', false)" alt="" style="width: 23%; left: 76%; top: 68%; position: absolute" />
<img id="<%=rem%>select" src='<c:url value="/images/remotes/bcm-7437-1/keys/select.png" />' onmousedown="clickRemoteButton(event,'SELECT', true)" onmouseup="clickRemoteButton(event,'SELECT', false)" alt="" style="width: 22.5%; left: 39.5%; top: 34%; position: absolute" />
<img id="<%=rem%>volup" src='<c:url value="/images/remotes/bcm-7437-1/keys/volup.png" />' onmousedown="clickRemoteButton(event,'VOLUP', true)" onmouseup="clickRemoteButton(event,'VOLUP', false)" alt="" style="width: 25%; left: 23%; top: 15%; position: absolute" />
<img id="<%=rem%>chup" src='<c:url value="/images/remotes/bcm-7437-1/keys/chup.png" />' onmousedown="clickRemoteButton(event,'CHUP', true)" onmouseup="clickRemoteButton(event,'CHUP', false)" alt="" style="width: 25%; left: 54%; top: 15%; position: absolute" />
<img id="<%=rem%>voldn" src='<c:url value="/images/remotes/bcm-7437-1/keys/voldn.png" />' onmousedown="clickRemoteButton(event,'VOLDN', true)" onmouseup="clickRemoteButton(event,'VOLDN', false)" alt="" style="width: 25%; left: 23%; top: 48%; position: absolute" />
<img id="<%=rem%>chdn" src='<c:url value="/images/remotes/bcm-7437-1/keys/chdn.png" />' onmousedown="clickRemoteButton(event,'CHDN', true)" onmouseup="clickRemoteButton(event,'CHDN', false)" alt="" style="width: 25%; left: 54%; top: 48%; position: absolute" />
</html>
