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
    <title>客户的足迹</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=no, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->


</head>
<body class="bg-square">

    <div class="customerTrack-top">
        <div class="headImg"><img src="${data.headImg}" /> </div>
        <div class="centerBlock">
            <div class="userName" id="userName">${data.userName}</div>
            <div class="btn addWx" id="copyBtn" onclick="" data-clipboard-target="#wx"  data-clipboard-action="copy">加我微信</div>
            <input type="hidden" id="wx" value="${data.wx}" />
        </div>
        <div class="rightBlock">
            <div>活跃时间：${data.activeDays}天内</div>
            <div>${data.city}</div>
        </div>
    </div>
    <div class="customerTrack-bar">
        <ul>
            <li>
                <div><span>${data.videoViewCount}</span></div>
                <div>视频查看（次）</div>
            </li>
            <li>
                <div><span>${data.productViewCount}</span></div>
                <div>产品查看（次）</div>
            </li>
            <li>
                <div><span>${data.advViewCount}</span></div>
                <div>广告查看（次）</div>
            </li>
        </ul>
    </div>

    <div class="customerTrack-homePage">
        <div class="leftTitle">访问我的主页</div>
        <div class="rightCount">
            <div><span>${data.homePageViewCount}</span></div>
            <div>查看次数</div>
        </div>
    </div>

    <div class="cont">
    <c:forEach var="video" items="${data.resultList}">
    <div class="customerTrack-view">
        <div class="viewTop">
            <div class="headImg">
                <img src="${video.coverImg}" />
            </div>
            <div class="titleBlock">
                <div class="viewTitle">${video.title}</div>
                <div class="track-viewCount">查看${video.viewCount}次</div>
            </div>
        </div>
        <ul class="viewList">
            <c:forEach var="view" items="${video.viewList}" varStatus="status">
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
    </div>


<div class="back" onclick="javascript:history.back(-1);">
    返回
</div>

<script src="<%=path%>/statics/js/jquery.min.js"></script>
    <script src="<%=path%>/statics/js/toast.js"></script>
    <script src="<%=path%>/statics/js/clipboard.min.js"></script>
<script>
    $(function () {
        document.title = $("#userName").html() + '的足迹';
    })

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

    $(function () {
        var clipboard2 = new ClipboardJS('#copyBtn', {
            text: function() {
                return $("#wx").val();
            }
        });
        clipboard2.on('success', function(e) {
            Toast('微信号复制成功', 2000);
        });
        clipboard2.on('error', function (e) {
        });
    })

</script>
</body>
</html>