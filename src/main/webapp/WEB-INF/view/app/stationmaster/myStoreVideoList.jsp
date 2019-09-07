<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/15 0015
  Time: 15:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>视频广场</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="<%=path%>/statics/css/videostyle.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/stationmaster.css">
    <link rel="stylesheet" href="<%=path%>/statics/search/style.css">
    <link type="text/css" rel="stylesheet" href="<%=path%>/statics/css/zdialog.css"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">


</head>
<body class="bg-square">

<div class="square-top">
    <ul>
        <li onclick="window.location.href='<%=path%>/app/video/myVideoList'">吸粉视频</li>
        <li class="square-top-active">商品视频</li>
    </ul>
</div>

<div class="cont" style="margin-top:5px;">
    <c:forEach var="video" items="${videoStoreList}" varStatus="status">
        <div class="cont-block four-radius margin-top-10">
            <div class="block-top"
                 onclick="window.location.href='<%=path%>/app/store/storeVideoPlay?type=myVideo&id=${video.id}'">
                <div class="top-coverImg img-square">
                    <img src="${video.coverImg}"/>
                </div>
                <div class="top-name">
                    <div class="title">${video.title}</div>
                    <div class="desc"></div>
                </div>
            </div>
            <div class="block-bottom">
                <ul>
                    <li>
                        <div class="textDiv"><fmt:formatDate value="${video.createdTime}" pattern="MM-dd"/></div>
                    </li>
                    <li>
                        <img src="<%=path%>/statics/img/video/manage/bg_33.png"/>
                        ${video.commentCount}
                    </li>
                    <li>
                        <img src="<%=path%>/statics/img/video/manage/bg_34.png"/>
                        ${video.forwardCount}
                    </li>
                </ul>
            </div>
            <div class="block-bottom">
                <ul style="margin-top:5px;">
                    <li>
                        <div class="btn btn-black"
                             onclick="isTop(${video.id})">置顶
                        </div>
                    </li>
                    <li>
                        <div class="btn btn-black"
                             onclick="window.location.href='<%=path%>/app/stationmaster/storeVideoEdit?type=myVideo&videoId=${video.id}'">
                            编辑
                        </div>
                    </li>
                    <li>
                        <div class="btn btn-red" onclick="videoDelete(this, ${video.id})">删除</div>
                    </li>
                </ul>
            </div>
            <c:if test="${video.checkState < 0}">
                <div class="check-cover">
                    <img src="<%=path%>/statics/img/stationmaster/bg_29.png" />
                </div>
            </c:if>
        </div>
    </c:forEach>
    <div class="record-finish">—————— 已经到底了 ——————</div>
</div>

<c:choose>
    <c:when test="${not empty stationCopyNo && stationCopyNo != ''}">
        <div class="stationBottom">
            <ul>
                <li onclick="window.location.href='<%=path%>/app/stationmaster/index'">
                    <div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_15.png"/></div>
                    <div class="textdiv">首页</div>
                </li>
                <li onclick="window.location.href='<%=path%>/app/video/square'">
                    <div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_15.png"/></div>
                    <div class="textdiv">视频广场</div>
                </li>
                <li onclick="showTypeSelect()">
                    <div class="picdiv"><img src="<%=path%>/statics/img/stationmaster/bg_25.png"/></div>
                    <div class="textdiv">发视频</div>
                </li>
                <li onclick="window.location.href='<%=path%>/app/manageCenter'">
                    <div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_18.png"/></div>
                    <div class="textdiv">管理中心</div>
                </li>
            </ul>
        </div>
    </c:when>
    <c:otherwise>
        <div class="bottom">
            <ul>
                <li onclick="window.location.href='<%=path%>/app/video/square'">
                    <div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_15.png"/></div>
                    <div class="textdiv">视频广场</div>
                </li>
                <li onclick="window.location.href='<%=path%>/app/video/drawVideo'">
                    <div class="publish"><img src="<%=path%>/statics/img/video/manage/bg_19.png"/></div>
                    <div class="textdiv" style="margin-top:22px;">发视频</div>
                </li>
                <li onclick="window.location.href='<%=path%>/app/manageCenter'">
                    <div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_18.png"/></div>
                    <div class="textdiv">管理中心</div>
                </li>
            </ul>
        </div>
    </c:otherwise>
</c:choose>

<div id="video-type-select" class="video-type-select">
    <div class="video-type-btn" onclick="window.location.href='<%=path%>/app/video/drawVideo'">吸粉视频</div>
    <div class="video-type-btn" onclick="window.location.href='<%=path%>/app/stationmaster/storeVideoEdit'">商品视频</div>
</div>

<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script src='<%=path%>/statics/js/zdialog.js'></script>
<script src="<%=path%>/statics/js/toast.js"></script>
<script>
    var videoObj;
    var videoId;

    function videoDelete(obj, id) {
        videoObj = obj;
        videoId = id;
        $.DialogByZ.Confirm({Title: "", Content: "确认要删除该视频吗？", FunL: confirmVideoDelete, FunR: cancel})
    };

    function confirmVideoDelete() {
        $.DialogByZ.Close();
        $.ajax({
            url: '${contextPath}/app/store/deleteStoreVideo?id=' + videoId,
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.code = 200) {
                    $(videoObj).parent().parent().parent().parent().remove();
                }
            }
        });
    }
    function cancel() {
        $.DialogByZ.Close();
    }

    function isTop(id) {
        $.ajax({
            url: '${contextPath}/app/store/setStoreVideoTop?id=' + id,
            dataType: 'json',
            success:function (res) {
                if (res.code == 200){
                    Toast("置顶成功！", 2000);
                }
            }
        })
    }
</script>
</body>
</html>