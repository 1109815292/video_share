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
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>邀请好友</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->


</head>
<body class="bg-invitation">


    <div id="poster">
        <div id="invitationCont" class="invitation-cont">

        </div>
    </div>
    <div id="link" style="display: none;">
        <div class="invitation-cont">

        </div>
        <div class="invitation-copyBtn">复制推广链接</div>
        <div class="invitation-cont">

        </div>
        <div class="invitation-copyBtn">复制推广链接</div>
    </div>

    <div class="invitation-btn">
        <ul>
            <li onclick="btnChange(1)" id="li-link">专属推广链接</li>
            <li onclick="btnChange(2)" id="li-poster" class="active">专属推广海报</li>
        </ul>
    </div>

    <script src="<%=path%>/statics/js/jquery.min.js"></script>
<script>
    $(function () {
        $("#invitationCont").height(window.innerHeight - 240);
    })

    function btnChange(index) {
        if (index == 1){
            $("#poster").hide();
            $("#li-poster").removeClass("active");
            $("#link").show();
            $("#li-link").addClass("active");
        }else if (index == 2){
            $("#link").hide();
            $("#li-link").removeClass("active");
            $("#poster").show();
            $("#li-poster").addClass("active");
        }
    }
</script>
</body>
</html>