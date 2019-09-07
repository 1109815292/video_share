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
    <title>实体店管理</title>
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
    <c:forEach var="item" items="${stores}" varStatus="status">
        <div class="cont-block four-radius margin-top-20">
            <div class="block-top">
                <div class="top-coverImg img-square">
                    <img src="${item.picUrl}"/>
                </div>
                <div class="top-name">
                    <div class="title">${item.storeName}</div>
                </div>
            </div>
            <div class="block-bottom">
                <ul>
                    <li style="width: 50%;">
                        <img src="<%=path%>/statics/img/video/manage/bg_33.png"/><span>${item.viewCount}</span></li>
                    <li style="width: 50%;">
                        <div class="btn btn-black" style="height: 26px;line-height: 26px;" onclick="window.location.href='<%=path%>/app/advStoreSet?id=${item.id}'">编辑
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </c:forEach>
</div>
<%--<div class="saveBtn">
    <button type="button" id="btn-save" onclick="saveHomePage()">添加实体门店</button>
</div>--%>
</form>
<div class="back" style="bottom:80px;" onclick="window.location.href='<%=path%>/app/promotionSet'">
    返回
</div>
<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script src='<%=path%>/statics/js/zdialog.js'></script>
<script>
    var storeObj;
    var storeId;
    function saveHomePage() {
        window.location.href = '${contextPath}/app/advStoreSet';
    }
    function storeDelete(obj, id) {
        storeObj = obj;
        storeId = id;
        $.DialogByZ.Confirm({Title: "", Content: "确定要删除该实体店吗？",FunL:confirmProductDelete,FunR:cancel})
    };
    function confirmProductDelete(){
        $.DialogByZ.Close();
        $.ajax({
            url: '${contextPath}/app/advStoreDelete?id=' + storeId,
            type: "get",
            dataType: "json",
            success: function(data) {
                if (data.code = 200){
                    $(storeObj).parent().parent().parent().parent().remove();
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