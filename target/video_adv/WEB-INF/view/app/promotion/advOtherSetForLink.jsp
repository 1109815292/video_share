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
    <title>添加链接</title>
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
                <input style="position:absolute;opacity:0;" accept="image/*" mutiple="mutiple" onchange="uploadImgFile()" type="file" id="file"
                       name="uploadFile">
                <c:choose>
                    <c:when test="${empty other.picUrl}">
                        <img id="picUrl" src="<%=path%>/statics/img/video/manage/bg_41.png">
                    </c:when>
                    <c:otherwise>
                        <img id="picUrl" src="${other.picUrl}">
                    </c:otherwise>
                </c:choose>
            </label>
        </div>
    </form>
</div>
<form id="form_other" method="post">
    <div class="homePageSet" style="float:left;border-top:1px solid #eee;">
        <ul>
            <li>
                <div class="textLabel">链接名称</div>
                <div class="textInput"><input type="text" name="name" placeholder="请输入链接名称" value="${other.name}"/></div>
            </li>
            <li>
                <div class="textLabel">推广链接</div>
                <div class="textInput"><input type="text" name="url" placeholder="请输入推广链接" value="${other.url}"/></div>
            </li>
            <li>
                <div class="textLabel">链接介绍</div>
                <div class="textInput"><textarea name="desc" placeholder="一句话介绍链接" rows="3" cols="5">${other.desc}</textarea></div>
            </li>
        </ul>
    </div>
    <input type="hidden" name="id" value="${other.id}" >
    <input type="hidden" id="inp-picUrl" name="picUrl" value="${other.picUrl}"/>
    <c:choose>
        <c:when test="${not empty other and not empty other.id}">
            <input type="hidden" name="type" value="${other.type}">
        </c:when>
        <c:otherwise>
            <input type="hidden" name="type" value="${param.type}">
        </c:otherwise>
    </c:choose>
    <div class="saveBtn">
        <button type="button" id="btn-save" onclick="saveHomePage()">保 存</button>
    </div>
</form>

<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/statics/layui/layui.all.js" charset="utf-8"></script>
<script>
    function saveHomePage() {
        $.ajax({
            url: '${contextPath}/app/saveOther',
            method: 'post',
            data:$('#form_other').serialize(),
            success: function (data) {
                if (data.code === 200) {
                    layer.msg('保存成功！!', {icon: 1, time: 3000});
                    setTimeout(function () {
                        window.location.href = '${contextPath}/app/advOtherManage?type=7';
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