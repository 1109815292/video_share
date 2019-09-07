<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>我的客户</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->


</head>
<body class="bg-square">

<div class="customer-btn">
    <ul>
        <li class="active" onclick="btnChange(1)" id="li-1">最近活跃客户</li>
        <li onclick="btnChange(2)" id="li-2">累积客户</li>
    </ul>
</div>

    <div class="cont">
        <c:if test="${vipFlag == 'Y'}">
        <c:forEach items="${activeClients}" var="client">
        <div class="customer-div" onclick="window.location.href='<%=path%>/app/customer/customerTrack?copyNo=${client.clientUser.copyNo}'">
            <div class="customerImg">
                <img src="${client.clientUser.headImg}" />
            </div>
            <div class="customer-user">
                <div class="userName">${client.clientUser.userName}</div>
                <div class="lastlogin">最近活跃：<fmt:formatDate value="${client.lastViewTime}" pattern="yyyy-MM-dd"/></div>

            </div>
            <div class="customer-times">
                <div class="count">${client.viewCount}</div>
                <div class="label">查看次数</div>
            </div>
        </div>
        </c:forEach>
        </c:if>
    </div>

<div class="back" onclick="javascript:history.back(-1);">
    返回
</div>

<div class="noVipCover" id="noVipCover">
    <input type="hidden" id="vipFlag" value="${vipFlag}" />
    <div class="coverImg"><img src="<%=path%>/statics/img/video/manage/information.png" /></div>
    <div class="hintText">
        <div class="">您还未开通VIP会员</div>
        <div class="">开通VIP后查看详细数据</div>
    </div>
    <div class="openVip" onclick="window.location.href='<%=path%>/app/advPay/openVip'">立即开通</div>
</div>

<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script>
    $(function () {
        if ($("#vipFlag").val() != 'Y'){
            $("#noVipCover").attr("height", window.innerHeight);
            $("#noVipCover").show();
        }
    });

    function btnChange(index) {
        if (index == 1){
            $("#li-1").addClass("active");
            $("#li-2").removeClass("active");
        }else if (index == 2){
            $("#li-2").addClass("active");
            $("#li-1").removeClass("active");
        }
    }
</script>
</body>
</html>