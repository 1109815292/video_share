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
            <c:when test="${param.type eq 1}">管理推广商品</c:when>
            <c:when test="${param.type eq 2}">管理京东商品</c:when>
            <c:when test="${param.type eq 3}">管理微店商品</c:when>
            <c:when test="${param.type eq 4}">管理有赞商品</c:when>
            <c:when test="${param.type eq 5}">管理淘宝商品</c:when>
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
    <c:forEach var="item" items="${products}" varStatus="status">
        <div class="cont-block four-radius">
            <div class="block-top margin-top-20">
                <div class="top-coverImg img-square">
                    <img src="${item.picUrl}"/>
                </div>
                <div class="top-name">
                    <div class="title">${item.productName}</div>
                    <div class="price">${item.price}</div>
                </div>
            </div>
            <div class="block-bottom">
                <ul>
                    <li><img src="<%=path%>/statics/img/video/manage/bg_33.png"/><span>${item.viewCount}</span></li>
                    <li>
                        <div class="btn btn-black" onclick="window.location.href='<%=path%>/app/advProductSet?id=${item.id}'">编辑
                        </div>
                    </li>
                    <li>
                        <div class="btn btn-red" onclick="productDelete(this, ${item.id})">
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
            <c:when test="${param.type eq 1}">添加普通商品</c:when>
            <c:when test="${param.type eq 2}">添加京东商品</c:when>
            <c:when test="${param.type eq 3}">添加微店商品</c:when>
            <c:when test="${param.type eq 4}">添加有赞商品</c:when>
            <c:when test="${param.type eq 5}">添加淘宝商品</c:when>
        </c:choose>
    </button>
</div>

<div class="back" style="bottom:80px;" onclick="window.location.href='<%=path%>/app/promotionSet'">
    返回
</div>
<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script src='<%=path%>/statics/js/zdialog.js'></script>
<script>
    var productObj;
    var productId;
    function saveHomePage() {
        window.location.href = '${contextPath}/app/advProductSet?type=${param.type}';
    }
    function productDelete(obj, id) {
        productObj = obj;
        productId = id;
        $.DialogByZ.Confirm({Title: "", Content: "确定要删除该商品吗？",FunL:confirmProductDelete,FunR:cancel})
    };
    function confirmProductDelete(){
        $.DialogByZ.Close();
        $.ajax({
            url: '${contextPath}/app/advProductDelete?id=' + productId,
            type: "get",
            dataType: "json",
            success: function(data) {
                if (data.code = 200){
                    $(productObj).parent().parent().parent().parent().remove();
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