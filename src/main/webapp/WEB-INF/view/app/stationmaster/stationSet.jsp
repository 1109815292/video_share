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
    <title>站点设置</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link href="<%=path%>/statics/css/stationmaster.css" rel="stylesheet">
    <link href="<%=path%>/statics/layui/css/layui.css" rel="stylesheet">

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->


</head>
<body class="bg-square" style="padding-top:9px;">
<div class="stationSet-top">
    <div class="publicImg"><img src="${stationmaster.publicImg}" /></div>
    <div class="stationName">${stationmaster.stationName}</div>
    <div class="district">${stationmaster.districtStr}</div>
</div>



<div class="homePageSet">
    <ul>
        <li style="">
            <div class="textLabel">公众号头像</div>
            <div class="rightArrow"><img id="img-topImg" src="<%=path%>/statics/img/stationmaster/more.png" /></div>
            <form action="" id="uploadImg1" method="post" enctype="multipart/form-data">
                <input style="position:absolute;opacity:0;width:50px;height:35px;right: 50px;top:5px;z-index:200;" accept="image/*"
                       mutiple="mutiple" onchange="uploadImgFile(1)" type="file"
                       name="uploadFile">
                <div class="img headImg"  style="right:50px;top:7px;margin:0;padding:0;width:32px;height: 32px;border-radius: 16px;line-height:0;overflow: hidden;z-index:100;">
                    <img id="img-publicImg" src="${stationmaster.publicImg}"/></div>
            </form>
        </li>
        <li>
            <div class="textLabel">站点名字</div>
            <div class="rightArrow" onclick="openSetDialog(1)"><img src="<%=path%>/statics/img/stationmaster/more.png" /></div>
            <div class="textShow" onclick="openSetDialog(1)" id="div-stationName">${stationmaster.stationName}</div>
        </li>
        <li>
            <div class="textLabel">微信号</div>
            <div class="rightArrow" onclick="openSetDialog(2)"><img src="<%=path%>/statics/img/stationmaster/more.png" /></div>
            <div class="textShow" onclick="openSetDialog(2)" id="div-publicWx">${stationmaster.publicWx}</div>
        </li>
        <li>
            <div class="textLabel">二维码</div>
            <div class="rightArrow"><img src="<%=path%>/statics/img/stationmaster/more.png" /></div>
            <form action="" id="uploadImg2" method="post" enctype="multipart/form-data">
                <input style="position:absolute;opacity:0;width:50px;height:35px;right: 50px;top:150px;z-index:100;" accept="image/*"
                       mutiple="mutiple" onchange="uploadImgFile(2)" type="file"
                       name="uploadFile">
                <div class="img qrCodeImg" style="right:50px;top:140px;">
                            <img id="img-qrcode" src="<%=path%>/statics/img/stationmaster/qrcode.png"/>
                </div>
            </form>
        </li>
    </ul>
</div>

<input type="hidden" id="inp-stationName" value="${stationmaster.stationName}" />
<input type="hidden" id="inp-publicWx" value="${stationmaster.publicWx}" />

<div id="dialog-set" class="dialog-set">
    <div class="dialog-title">站点名字</div>
    <div class="dialog-input">
        <input type="text" id="inp-dialogSet" />
    </div>
    <div class="btn-bar">
    <div class="btn-div">
        <div class="btn btn-cancel" onclick="closeSetDialog()">取消</div>
    </div>
    <div class="btn-div">
        <div class="btn btn-save" onclick="save()">保存</div>
    </div>
    </div>
</div>


<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script src="<%=path%>/statics/js/toast.js"></script>
<script type="text/javascript" src="<%=path%>/statics/layui/layui.all.js" charset="utf-8"></script>
<script>
    var modifyIndex;
    function openSetDialog(index) {
        modifyIndex = index;
        if (modifyIndex == 1){
            $(".dialog-title").html("站点名字");
            $("#inp-dialogSet").val($("#inp-stationName").val());
        }else if(modifyIndex == 2){
            $(".dialog-title").html("微信号");
            $("#inp-dialogSet").val($("#inp-publicWx").val());
        }
        $("#dialog-set").show();
    }
    function closeSetDialog() {
        $("#dialog-set").hide();
    }

    function save() {
        closeSetDialog();
        if (modifyIndex == 1){
            $.ajax({
                url: '${contextPath}/app/stationmaster/updateStationInfo',
                data: {'stationName': $("#inp-dialogSet").val()},
                method: 'post',
                dataType: 'json',
                success: function (res) {
                    if (res.code == 200){
                        $("#div-stationName").html($("#inp-dialogSet").val());
                        $("#inp-stationName").val($("#inp-dialogSet").val());
                        Toast("修改成功！", 2000);
                    }
                }
            })
        }else if (modifyIndex == 2){
            $.ajax({
                url: '${contextPath}/app/stationmaster/updateStationInfo',
                data: {'publicWx': $("#inp-dialogSet").val()},
                method: 'post',
                dataType: 'json',
                success: function (res) {
                    if (res.code == 200){
                        $("#div-publicWx").html($("#inp-dialogSet").val());
                        $("#inp-publicWx").val($("#inp-dialogSet").val());
                        Toast("修改成功！", 2000);
                    }
                }
            })
        }
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
                if (index == 1) {
                    $.ajax({
                        url: '${contextPath}/app/stationmaster/updateStationInfo',
                        data: {'publicImg': res.picUrl},
                        method: 'post',
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == 200){
                                $("#img-topImg").attr("src", res.picUrl);
                                $("#img-publicImg").attr("src", res.picUrl);
                                Toast("修改成功！", 2000);
                            }
                        }
                    })
                } else if (index == 2) {
                    $.ajax({
                        url: '${contextPath}/app/stationmaster/updateStationInfo',
                        data: {'publicQRCode': res.picUrl},
                        method: 'post',
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == 200){
                                Toast("修改成功！", 2000);
                            }
                        }
                    })
                }
            },
        });
    }
</script>
</body>
</html>