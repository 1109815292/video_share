<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <title>视频访问统计详情</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="<%=path%>/statics/css/videostyle.css">
    <link rel="stylesheet" href="<%=path%>/statics/search/style.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">


</head>
<body class="bg-square">
<div class="videoViewLog-top">
    <div class="cont-block" style="width: 100%;">
        <div class="block-top" onclick="window.location.href='<%=path%>/app/video/play?type=myVideo&id=${video.id}'">
            <div class="top-coverImg img-square">
                <img src="${video.coverImg}" />
            </div>
            <div class="top-name">
                <div class="title">${video.title}</div>
                <div class="desc">${video.tag}</div>
            </div>
        </div>
        <div class="block-bottom">
            <ul>
                <li><img src="<%=path%>/statics/img/video/manage/bg_33.png" />${video.viewCount}</li>
                <li><img src="<%=path%>/statics/img/video/lookMe/bg_01.png" />${video.forwardCount}</li>
                <li><img src="<%=path%>/statics/img/video/manage/bg_34.png" />${video.peopleCount}</li>
            </ul>
        </div>
    </div>
</div>

<%--<div class="videoViewLog-bar">
    <ul id="menubar">
        <li class="active">观看记录</li>
        <li>转发记录</li>
    </ul>
</div>--%>

<div class="cont">
<c:forEach var="resultUser" items="${resultList}">
<div class="videoViewLog-view">
    <div class="videoViewLog-viewTop">
        <div class="headImg"><img src="${resultUser.headImg}" /> </div>
        <div class="centerBlock">
            <div class="userName">${resultUser.userName}</div>
            <div class="view">观看${resultUser.viewCount}次</div>
        </div>
        <div class="rightBtn" onclick="window.location.href='<%=path%>/app/customer/customerTrack?copyNo=${resultUser.copyNo}'">
            <div>客户详情</div>
        </div>
    </div>
    <ul class="viewList">
        <c:forEach var="view" items="${resultUser.viewList}" varStatus="status">
            <c:choose>
                <c:when test="${status.index > 1}">
                    <li style="display: none;">
                        <div class="leftViewCount">第${status.index + 1}次查看</div>
                        <div class="rightViewTime"><fmt:formatDate value="${view.viewTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                    </li>
                </c:when>
                <c:otherwise>
                    <li>
                        <div class="leftViewCount">第${status.index + 1}次查看</div>
                        <div class="rightViewTime"><fmt:formatDate value="${view.viewTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </div>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <li>
            <div class="centerText" onclick="showAll(this)">查看全部</div>
        </li>
    </ul>
</div>
</c:forEach>

<div class="record-finish">—————— 已经到底了 ——————</div>

</div>

<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script>
    $(function(){
        $("#menubar li").click(function() {
            $(this).siblings('li').removeClass('active');
            $(this).addClass('active');
        });
    });

    function showAll(obj) {
        if ($(obj).html() == '查看全部'){
            $(obj).parent().parent().find("li").show();
            $(obj).html('<img src="${contextPath}/statics/img/video/manage/collect.png" />');
        }else{
            var oLi = $(obj).parent().parent().find("li");
            oLi.each(function (index, e) {
                if (index > 1 && index < (oLi.length - 1)){
                    $(e).hide();
                }
            })
            $(obj).html("查看全部");
        }
    }
</script>
</body>
</html>