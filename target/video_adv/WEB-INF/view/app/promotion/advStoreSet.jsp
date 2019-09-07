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
    <title>添加实体店</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link href="<%=path%>/statics/layui/css/layui.css" rel="stylesheet">
    <link href="<%=path%>/statics/css/xadmin.css" rel="stylesheet">

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->

</head>
<body class="bg-square" style="width: 100%;">

<div class="advProduct-topPic">
    <form action="" id="uploadImg" method="post" enctype="multipart/form-data">
        <div class="advPic productWidth">
            <label>
                <input style="position:absolute;opacity:0;width:110px;" accept="image/*" mutiple="mutiple"
                       onchange="uploadImgFile(1)" type="file" id="file"
                       name="uploadFile">
                <c:choose>
                    <c:when test="${empty store.picUrl}">
                        <img id="picUrl" src="<%=path%>/statics/img/video/manage/bg_41.png">
                    </c:when>
                    <c:otherwise>
                        <img id="picUrl" src="${store.picUrl}">
                    </c:otherwise>
                </c:choose>
            </label>
        </div>
        <div class="advPic-label">点击上传实体店图片</div>
        <div class="advPic-size">尺寸：750*750</div>
    </form>
</div>
    <div class="homePageSetBar">实体店信息</div>
    <div class="homePageSet">
        <ul>
            <form id="form_store" method="post">
            <li>
                <div class="textLabel">实体店名称</div>
                <div class="textInput"><input type="text" name="storeName" placeholder="请输入实体店名称"
                                              value="${store.storeName}"/></div>
            </li>
            <li style="position: relative;">
                <div class="textLabel">实体店地址</div>
                <div class="textInput" style="padding-right:30px;"><input type="text" id="inp-address" name="address" placeholder="请输入实体店地址"
                                               value="${store.address}"/></div>
                <div class="storeSetMap" onclick="openBaiduMap()"><img src="<%=path%>/statics/img/video/manage/map_2.png" /></div>
            </li>
            <li>
                <div class="textLabel">联系电话</div>
                <div class="textInput"><input type="text" name="tel" placeholder="请输入联系电话" value="${store.tel}"/></div>
            </li>
            <li>
                <div class="textLabel">实体店链接</div>
                <div class="textInput"><input type="text" name="storeUrl" placeholder="请输入实体店链接"
                                              value="${store.storeUrl}"/></div>
            </li>
            <li>
                <div class="textLabel">实体店介绍</div>
                <div class="textInput"><textarea type="text" name="storeDesc" placeholder="一句话介绍实体店" rows="3"
                                                 cols="5">${store.storeDesc}</textarea></div>
            </li>
            <li>
                <div class="textLabel">店主微信</div>
                <div class="textInput"><input type="text" name="wx" value="${store.wx}"/></div>
            </li>
                <input type="hidden" id="inp-storeId" name="id" value="${store.id}">
                <input type="hidden" id="inp-picUrl" name="picUrl" value="${store.picUrl}"/>
                <input type="hidden" id="inp-longitude" name="longitude" value="${store.longitude}">
                <input type="hidden" id="inp-latitude" name="latitude" value="${store.latitude}">
                <input type="hidden" id="deletedStoreImageIds" name="deletedStoreImageIds" value="">
                <div class="saveBtn">
                    <button type="button" id="btn-save" onclick="saveHomePage()">保 存</button>
                </div>
            </form>
            <li style="position: relative;margin-bottom:10px;"><span>微信二维码</span>
                <form action="" id="uploadImg3" method="post" enctype="multipart/form-data">
                <input style="position:absolute;opacity:0;width:50px;right: 20px;top:0px;z-index:100;" accept="image/*"
                       mutiple="mutiple" onchange="uploadImgFile(3)" type="file"
                       name="uploadFile">
                <div class="img qrCodeImg" style="top:0px;">
                    <c:choose>
                        <c:when test="${empty store.wxQRCode}">
                            <img id="img-qrcode" src="<%=path%>/statics/img/video/manage/bg_27.png"/>
                        </c:when>
                        <c:otherwise>
                            <img id="img-qrcode" src="${store.wxQRCode}"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                </form>
            </li>
        </ul>
    </div>




<div class="homePageSetBar">店铺图片
    <div class="hint" id="count-hint">0/15</div>
</div>
<div class="advStore-pic">
    <div id="pic-cont">
        <c:forEach var="image" items="${store.images}">
            <div class="div-pic">
                <img class="layui-upload-img" src="${image.picUrl}">
                <button type="button" onclick="pic_del(this, ${image.id})"
                        class="layui-btn layui-btn-sm layui-btn-primary">删除
                </button>
                <div style="display:none;" class="store-image-info">
                    <input type="hidden" class="store-img-url" value="${image.picUrl}">
                    <input type="hidden" class="store-img-id" value="${image.id}">
                </div>
            </div>
        </c:forEach>
    </div>
    <div id="div-update" class="layui-upload">
        <div class="div-pic btn-update">
            <form action="" id="uploadImg2" method="post" enctype="multipart/form-data">
                <input style="position:absolute;opacity:0;width:110px;height: 100px;" accept="image/*"
                       mutiple="mutiple" onchange="uploadImgFile(2)" type="file"
                       name="uploadFile">
                <img class="layui-upload-img" src="<%=path%>/statics/img/video/manage/bg_41.png">
            </form>
        </div>
    </div>
</div>



<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script src="<%=path%>/statics/js/xadmin.js"></script>
<script src="<%=path%>/statics/js/toast.js"></script>
<script src='https://res.wx.qq.com/open/js/jweixin-1.4.0.js' type="text/javascript"></script>
<script type="text/javascript" src="<%=path%>/statics/layui/layui.all.js" charset="utf-8"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=cvBBimq5SKq85gBAmKzTCTkn9EbvzB93"></script>
<script>
    var pictureCount = 0;
    $(function () {
        pictureCount = $("#pic-cont > div[class=div-pic]").length;
        $("#count-hint").html(pictureCount + "/15");
        if (pictureCount > 14) {
            $("#div-update").hide();
        } else {
            $("#div-update").show();
        }
        setStoreLocation();
    })

    function saveHomePage() {
        var storeImageInfo = $('.store-image-info');
        $.each(storeImageInfo, function (index, item) {
            var url = $(item).children('input[class="store-img-url"]').val();
            var id = $(item).children('input[class="store-img-id"]').val();
            $('#form_store').append('<input type="hidden" name="images[' + index + '].picUrl" value="' + url + '">');
            if (id !== null && id !== undefined) {
                $('#form_store').append('<input type="hidden" name="images[' + index + '].id" value="' + id + '">');
            }
        });
        $.ajax({
            url: '${contextPath}/app/saveStore',
            method: 'post',
            data: $('#form_store').serialize(),
            success: function (data) {
                if (data.code === 200) {
                    layer.msg('保存成功！!', {icon: 1, time: 3000});
                    setTimeout(function () {
                        window.location.href = '${contextPath}/app/advStoreManage';
                    }, 1500);
                }
            }
        })
    }

    function uploadImgFile(index) {
        var formData;
        if (index === 1) {
            formData = new FormData($("#uploadImg")[0]);
        } else if (index === 2) {
            formData = new FormData($("#uploadImg2")[0]);
        }else if (index === 3) {
            formData = new FormData($("#uploadImg3")[0]);
        }
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
                if (index === 1) {
                    $("#picUrl").attr("src", res.picUrl);
                    $("#inp-picUrl").val(res.picUrl);
                } else if (index === 2) {
                    var str = "<div class='div-pic'><img class='layui-upload-img' src='" + res.picUrl + "'>";
                    str += "<button type='button' onclick='pic_del(this, " + res.id + ")' class='layui-btn layui-btn-sm layui-btn-primary' >删除</button>";
                    str += "<div style='display:none;' class='store-image-info'><input type='hidden' class='store-img-url' value='" + res.picUrl + "' ></div>";
                    str += "</div>";
                    $("#pic-cont").append(str);
                    pictureCount++;
                    $("#count-hint").html(pictureCount + "/15");
                    if (pictureCount > 14) {
                        $("#div-update").hide();
                    } else {
                        $("#div-update").show();
                    }
                }else if (index == 3){
                    $.ajax({
                        url: '${contextPath}/app/store/modifyWxQRCode',
                        data: {'wxQRCode': res.picUrl, 'storeId':$("#inp-storeId").val()},
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == 200){
                                $("#img-qrcode").attr("src", res.picUrl);
                                Toast("修改成功！", 2000);
                            }
                        }
                    })
                }
            },
        });
        $('#file').val('');
    }

    function pic_del(obj, id) {
        var ids = $('#deletedStoreImageIds').val();
        if (id !== null && id !== undefined && id !== "") {
            if (ids !== "") {
                $('#deletedStoreImageIds').val(ids + "," + id);
            } else {
                $('#deletedStoreImageIds').val(id);
            }
        }
        $(obj).parent().remove();
        pictureCount--;
        $("#count-hint").html(pictureCount + "/15");
        if (pictureCount > 14) {
            $("#div-update").hide();
        } else {
            $("#div-update").show();
        }
    }


    function setStoreLocation() {
        $.ajax({
            async: false,
            url: '${contextPath}/app/video/getWxConfig',//后台请求接口，找后台要
            data: {url: window.location.href},
            type: "get",
            dataType: "json",
            success: function (data) {
                wx.config({
                    debug: false,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。true为调用开启
                    appId: data.appid,//appid// 必填，公众号的唯一标识
                    timestamp: data.timestamp, // 必填，生成签名的时间戳
                    nonceStr: data.nonceStr,// 必填，生成签名的随机串
                    signature: data.signature,// 必填，签名，
                    jsApiList: ['openLocation']
                });
                wx.ready(function () {
                    wx.getLocation({
                        type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                        success: function (res) {
                            $("#inp-longitude").val(res.longitude);
                            $("#inp-latitude").val(res.latitude);
                        }
                    });
                });
            },
            error: function (xhr) {
                console.log("error");
            }
        });
    }

    function openBaiduMap() {
        x_admin_show("百度地图",'${contextPath}/app/store/getBaiduMap', $(window).width(), $(window).height());
    }

    function setAddress(longitude, latitude, address){
        if ($("#inp-address").val() == ''){
            $("#inp-address").val(address);
        }
        $("#inp-longitude").val(longitude);
        $("#inp-latitude").val(latitude);
    }

</script>
</body>
</html>