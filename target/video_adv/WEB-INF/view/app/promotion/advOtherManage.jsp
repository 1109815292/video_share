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
    <title>
        <c:choose>
            <c:when test="${param.type eq 1}">管理公众号</c:when>
            <c:when test="${param.type eq 2}">管理小程序</c:when>
            <c:when test="${param.type eq 3}">管理个人微信</c:when>
            <c:when test="${param.type eq 4}">管理微信群</c:when>
            <c:when test="${param.type eq 5}">管理APP</c:when>
            <c:when test="${param.type eq 6}">管理官网</c:when>
            <c:when test="${param.type eq 7}">管理链接</c:when>
            <c:when test="${param.type eq 8}">管理二维码</c:when>
        </c:choose>
    </title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="<%=path%>/statics/css/zdialog.css" />

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->


</head>
<body class="bg-square">
<div class="cont">
    <c:forEach var="item" items="${others}" varStatus="status">
        <div class="cont-block four-radius margin-top-20">
            <div class="block-top">
                <div class="top-coverImg img-square">
                    <img src="${item.picUrl}"/>
                </div>
                <div class="top-name">
                    <div class="title">${item.name}</div>
                </div>
            </div>
            <div class="block-bottom">
                <ul>
                    <li><img src="<%=path%>/statics/img/video/manage/bg_33.png"/><span>${item.viewCount}</span></li>
                    <li>
                        <div class="btn btn-black" onclick="window.location.href='<%=path%>/app/advOtherSet?id=${item.id}'">编辑
                        </div>
                    </li>
                    <li>
                        <div class="btn btn-red" onclick="advOtherDelete(this,${item.id})">
                            删除
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </c:forEach>
</div>
<div class="saveBtn">
    <button type="button" id="btn-save" onclick="saveHomePage()">
        <c:choose>
            <c:when test="${param.type eq 1}">添加公众号</c:when>
            <c:when test="${param.type eq 2}">添加小程序</c:when>
            <c:when test="${param.type eq 3}">添加个人微信</c:when>
            <c:when test="${param.type eq 4}">添加微信群</c:when>
            <c:when test="${param.type eq 5}">添加APP</c:when>
            <c:when test="${param.type eq 6}">添加官网</c:when>
            <c:when test="${param.type eq 7}">添加链接</c:when>
            <c:when test="${param.type eq 8}">添加二维码</c:when>
        </c:choose>
    </button>
</div>
</form>
<div class="back" style="bottom:80px;" onclick="window.location.href='<%=path%>/app/promotionSet'">
    返回
</div>
<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script src='<%=path%>/statics/js/zdialog.js'></script>
<script>
    var otherObj;
    var otherId;
    function saveHomePage() {
        window.location.href = '${contextPath}/app/advOtherSet?type=${param.type}';
    }
    function advOtherDelete(obj, id) {
        otherObj = obj;
        otherId = id;
        $.DialogByZ.Confirm({Title: "", Content: "确定要删除该广告吗？",FunL:confirmProductDelete,FunR:cancel})
    };
    function confirmProductDelete(){
        $.DialogByZ.Close();
        $.ajax({
            url: '${contextPath}/app/advOtherDelete?id=' + otherId,
            type: "get",
            dataType: "json",
            success: function(data) {
                if (data.code = 200){
                    $(otherObj).parent().parent().parent().parent().remove();
                };
            }
        });
    }
    function cancel() {
        $.DialogByZ.Close();
    }
</script>
</body>
</html>