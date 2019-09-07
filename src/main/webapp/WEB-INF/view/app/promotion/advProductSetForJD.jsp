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
            <c:when test="${not empty product and not empty product.id}">
                编辑京东商品
            </c:when>
            <c:otherwise>
                添加京东商品
            </c:otherwise>
        </c:choose>
    </title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link href="<%=path%>/statics/layui/css/layui.css" rel="stylesheet">

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->


</head>
<body class="bg-square">

<div class="advProduct-topPic">
    <form action="" id="uploadImg" method="post" enctype="multipart/form-data">
        <div class="advPic productWidth">
            <label>
                <input style="position:absolute;opacity:0;width:110px;" accept="image/*" mutiple="mutiple" onchange="uploadImgFile()" type="file" id="file"
                       name="uploadFile">
                <c:choose>
                    <c:when test="${empty product.picUrl}">
                        <img id="picUrl" src="<%=path%>/statics/img/video/manage/bg_41.png">
                    </c:when>
                    <c:otherwise>
                        <img id="picUrl" src="${product.picUrl}">
                    </c:otherwise>
                </c:choose>
            </label>
        </div>
        <div class="advPic-label">点击上传商品图片</div>
        <div class="advPic-size">尺寸：750*750</div>
    </form>
</div>
<form id="form_product" method="post">
    <div class="homePageSetBar">普通商品</div>
    <div class="homePageSet">
        <ul>
            <li>
                <div class="textLabel">商品名称</div>
                <div class="textInput"><input type="text" name="productName" placeholder="请输入商品名称" value="${product.productName}"/></div>
            </li>
            <li>
                <div class="textLabel">商品价格</div>
                <div class="textInput"><input type="text" name="price" placeholder="请输入商品价格（元）" value="${product.price}"/></div>
            </li>
            <li>
                <div class="textLabel">商品链接</div>
                <div class="textInput"><input type="text" name="productUrl" placeholder="请输入商品链接" value="${product.productUrl}" /></div>
            </li>
            <li>
                <div class="textLabel">排序</div>
                <div class="textInput"><input type="text" name="sort" placeholder="数字越大越靠前" value="${product.sort}"/></div>
            </li>
        </ul>
    </div>
    <c:choose>
        <c:when test="${not empty product.id}">
            <input type="hidden" name="type" value="${product.type}" />
        </c:when>
        <c:otherwise>
            <input type="hidden" name="type" value="${param.type}" />
        </c:otherwise>
    </c:choose>
    <input type="hidden" name="id" value="${product.id}" >
    <input type="hidden" id="inp-picUrl" name="picUrl" value="${product.picUrl}"/>
    <div class="saveBtn">
        <button type="button" id="btn-save" onclick="saveHomePage()">保 存</button>
    </div>
</form>

<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/statics/layui/layui.all.js" charset="utf-8"></script>
<script>
    function saveHomePage() {
        $.ajax({
            url: '${contextPath}/app/saveProduct',
            method: 'post',
            data:$('#form_product').serialize(),
            success: function (data) {
                if (data.code === 200) {
                    layer.msg('保存成功！!', {icon: 1, time: 3000});
                    setTimeout(function () {
                        window.location.href = '${contextPath}/app/advProductManage?type=2';
                    }, 1500);
                }
            }
        })
    }

    function uploadImgFile() {
        var formData = new FormData($("#uploadImg")[0]);
        $.ajax({
            url: '${contextPath}/app/uploadFile',
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            dataType: 'json',
            success: function (res) {
                $("#picUrl").attr("src", res.picUrl);
                $("#inp-picUrl").val(res.picUrl);
            },
        });

        $('#file').val('');
    }
</script>
</body>
</html>