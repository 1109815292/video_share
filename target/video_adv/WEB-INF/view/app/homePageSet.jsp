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
    <title>设置主页</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link href="<%=path%>/statics/layui/css/layui.css" rel="stylesheet">

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->


</head>
<body class="bg-square" style="padding-top:9px;">

<form id="form_homePage" action="<%=path%>/app/saveHomePage" method="post">
    <div class="homePageSet">
        <ul>
            <li>
                <div class="textLabel">主页名称</div>
                <div class="textInput"><input type="text" name="pageName" value="${homePage.pageName}"/></div>
            </li>
            <li>
                <div class="textLabel">公司名称</div>
                <div class="textInput"><input type="text" name="companyName" value="${homePage.companyName}"/></div>
            </li>
        </ul>
    </div>
    <div class="homePageSetBar">名片信息</div>
    <div class="homePageSet">
        <ul>
            <li>
                <div class="textLabel">姓名</div>
                <div class="textInput"><input type="text" name="name" value="${homePage.name}"/></div>
            </li>
            <li>
                <div class="textLabel">手机</div>
                <div class="textInput"><input type="text" name="mobile" value="${homePage.mobile}"/></div>
            </li>
            <li>
                <div class="textLabel">微信号</div>
                <div class="textInput"><input type="text" name="wx" value="${homePage.wx}"/></div>
            </li>
            <li>
                <div class="textLabel">签名</div>
                <div class="textInput"><input type="text" name="sign" value="${homePage.sign}"/></div>
            </li>
        </ul>
    </div>
    <input type="hidden" id="inp-headImg" name="headImg" value="${appUser.headImg}" />
    <input type="hidden" id="inp-qrcode" name="wxQRCode" value="${homePage.wxQRCode}">
</form>
    <div class="homePageSetBar">重要信息</div>
    <div class="homePageSet">
        <ul style="position: relative;">
            <li>
                <span>个人头像</span>
                <form action="" id="uploadImg1" method="post" enctype="multipart/form-data">
                    <input style="position:absolute;opacity:0;width:50px;right: 20px;top:10px;z-index:100;" accept="image/*" mutiple="mutiple" onchange="uploadImgFile(1)" type="file"
                           name="uploadFile">
                    <div class="img headImg"><img id="img-headImg" src="${appUser.headImg}" /></div>
                </form>
            </li>
            <li><span>个人微信二维码</span>
                <form action="" id="uploadImg2" method="post" enctype="multipart/form-data">
                    <input style="position:absolute;opacity:0;width:50px;right: 20px;top:50px;z-index:100;" accept="image/*" mutiple="mutiple" onchange="uploadImgFile(2)" type="file"
                           name="uploadFile">
                <div class="img qrCodeImg" >
                    <c:choose>
                        <c:when test="${empty homePage.wxQRCode}">
                            <img id="img-qrcode" src="<%=path%>/statics/img/video/manage/bg_27.png" />
                        </c:when>
                        <c:otherwise>
                            <img id="img-qrcode" src="${homePage.wxQRCode}" />
                        </c:otherwise>
                    </c:choose>
                </div>
                </form>
            </li>
        </ul>
    </div>
    <div class="saveBtn">
        <button type="button" id="btn-save" onclick="saveHomePage()">保 存</button>
    </div>
    <input type="hidden" name="id" value="${homePage.id}" >


<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/statics/layui/layui.all.js" charset="utf-8"></script>
<script>
    function saveHomePage() {
        $.ajax({
            url: '${contextPath}/app/saveHomePage',
            method: 'post',
            data: $('#form_homePage').serialize(),
            success: function (data) {
                if (data.code === 200) {
                    layer.msg('保存成功！!', {icon: 1, time: 3000});
                    setTimeout(function () {
                        window.location.href = "${contextPath}/app/manageCenter";
                    }, 500);
                }
            }
        })
    }

    function uploadImgFile(index) {
        var formData = new FormData($("#uploadImg" + index)[0]);
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
                if (index == 1){
                    $("#img-headImg").attr("src", res.picUrl);
                    $("#inp-headImg").val(res.picUrl);
                }else if (index == 2){
                    $("#img-qrcode").attr("src", res.picUrl);
                    $("#inp-qrcode").val(res.picUrl);
                }
            },
        });
    }
</script>
</body>
</html>