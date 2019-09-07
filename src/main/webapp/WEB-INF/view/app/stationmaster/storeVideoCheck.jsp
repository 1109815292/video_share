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
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <title>视频审核</title>

    <link rel="stylesheet" href="<%=path%>/statics/waterfall2/css/main.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/videostyle.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/videoPlay.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/stationmaster.css">
    <link rel="stylesheet" href="<%=path%>/statics/search/style.css">
    <link rel="stylesheet" href="<%=path%>/statics/waterfall2/dist/sortable.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

    <link href="<%=path%>/statics/css/swiper.min.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/statics/css/reset.css" type="text/css" rel="stylesheet"/>
    <link href="<%=path%>/statics/css/find.css" type="text/css" rel="stylesheet"/>

    <style>
        #wraper {
            float: left;
            width: 100%;
        }
    </style>
</head>
<body style="background: #161922;color:#fff;">
<div class="storeDetail">
    <div class="storeTitle">${advStore.storeName}</div>
    <div class="storeTagRow">
        <div class="count">${advStore.viewCount}人来过</div>
        <div class="tag">${industry.industry}</div>
    </div>
    <div class="storeAddress">
        <div class="addressImg"><img src="<%=path%>/statics/img/video/manage/map.png"/></div>
        <div class="">${advStore.address}</div>
        <div class="distance">距离您当前的位置7.2KM</div>
    </div>
</div>

    <div class="video-wrapper" style="margin-bottom:80px;">
        <div class="video-top">
            <div class="userHeadImg"><img src="${storeUserMap.headImg}"/></div>
            <div class="userName">${storeUserMap.userName}</div>
            <div class="createdTime"><fmt:formatDate value="${videoStore.createdTime}" pattern="MM-dd"/></div>
            <div class="clear"></div>
        </div>
        <div class="videoTitle">${videoStore.title}</div>
        <div class="video-coverImg" style="position: relative;">
            <img src="${videoStore.coverImg}"/>
            <div id="play-btn" class="play-btn" onclick="window.location.href='<%=path%>/app/store/storeVideoPlay?id=${videoStore.id}'">
                <img src="<%=path%>/statics/img/video/manage/bg_37.png"/>
            </div>
        </div>

    </div>
    <input type="hidden" id="inp-id" value="${videoStore.id}" />
    <input type="hidden" id="inp-storeId" value="${videoStore.storeId}" />
    <div class="check-bar">
        <div class="check-btn" onclick="check(-1)">屏蔽</div>
        <div class="check-btn" onclick="check(1)">通过</div>
    </div>


</body>

<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script src='<%=path%>/statics/js/toast.js'></script>
<script type="text/javascript">
    function check(state) {
        $.ajax({
            url: '${contextPath}/app/store/storeVideoCheck',
            data: {'id': $("#inp-id").val(),'state':state},
            method: 'get',
            dataType: 'json',
            success: function (res) {
                if (res.code = 200){
                    Toast("审核完成！", 3000);
                    window.location.href = '${contextPath}/app/stationmaster/storeDetail/' + $("#inp-storeId").val();
                }
            }
        })
    }
</script>

</body>
</html>