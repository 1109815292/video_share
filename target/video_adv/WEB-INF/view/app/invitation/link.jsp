<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/15 0015
  Time: 15:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>邀请好友</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=no, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->

</head>
<body class="bg-invitation">

<div class="cont">
    <div id="link" style="display: block;">
        <div class="invitation-cont">
            <div class="link-text">
                <textarea id="copyText1" readonly cols="10" rows="8">${inv_text_1}${inv_url}</textarea>
            </div>
        </div>
        <div class="btn invitation-copyBtn" onclick="" data-clipboard-target="#copyText1"  data-clipboard-action="copy" >
            复制推广链接
        </div>
        <div class="invitation-cont">
            <div class="link-text">
                <textarea id="copyText2" readonly cols="10" rows="8">${inv_text_2}${inv_url}</textarea></div>
        </div>
        <div class="btn invitation-copyBtn" onclick="" data-clipboard-target="#copyText2"  data-clipboard-action="copy" >
            复制推广链接
        </div>
    </div>
</div>
<div class="invitation-btn">
    <ul>
        <li id="li-link" class="active">专属推广链接</li>
        <li onclick="window.location.href='${contextPath}/app/invitation?tabIndex=1'" id="li-poster">专属推广海报</li>
    </ul>
</div>

<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script src="<%=path%>/statics/js/toast.js"></script>
<script src="<%=path%>/statics/js/clipboard.min.js"></script>
<script type="text/javascript">

    $(function () {
        var clipboard = new ClipboardJS('.btn');
        clipboard.on('success', function(e) {
            console.info('Action:', e.action);
            console.info('Text:', e.text);
            console.info('Trigger:', e.trigger);
            Toast('推广链接复制成功', 2000);
            e.clearSelection();
        });

        clipboard.on('error', function(e) {
            console.error('Action:', e.action);
            console.error('Trigger:', e.trigger);
        });
    })



</script>
</body>
</html>