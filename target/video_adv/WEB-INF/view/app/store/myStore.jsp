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
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>我的店铺</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="<%=path%>/statics/css/zdialog.css" />
    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->


</head>
<body class="bg-profile">

<div class="myStore-top">
    <div class="storeImg">
        <img src="${store.picUrl}" />
    </div>
    <div class="storeName">${store.storeName}</div>
    <div class="storeDesc">${store.storeDesc}</div>
    <div class="address">
        <div class="row">
            <div class="left-img"><img src="<%=path%>/statics/img/video/manage/map.png" /></div>
            <div class="addressCont">${store.address}</div>
            <div class="right-arrow"><img src="<%=path%>/statics/img/video/manage/arrow-right.png" /></div>
        </div>
        <div class="row">
            <div class="left-img"><img src="<%=path%>/statics/img/video/manage/phone.png" /></div>
            <div class="addressCont" id="storeTel" onclick="showTel()">${store.tel}</div>
            <div class="right-arrow"><img src="<%=path%>/statics/img/video/manage/arrow-right.png" /></div>
        </div>
    </div>
    <div class="more">
        <div class="more-div">
            <ul>
                <li onclick="openLocation()"><img src="<%=path%>/statics/img/video/manage/store.png"/><span>去店里</span></li>
                <li onclick="showTel()"><img src="<%=path%>/statics/img/video/manage/consult.png"/><span>咨询</span></li>
                <li onclick="more()"><img src="<%=path%>/statics/img/video/manage/more.png"/><span>更多</span></li>
            </ul>
        </div>
    </div>

    <div class="myStore-menuBar">
        <ul id="menubar">
            <li class="myStoreBarSelected">门店视频</li>
            <li>门店图片</li>
        </ul>
    </div>
</div>


<div class="cont" style="margin-top:0;">
    <div id="div0" class="userProfile-video">
        <c:forEach var="video" items="${videoList}">
            <div class="video" onclick="window.location.href='<%=path%>/app/video/play?id=${video.id}'">
                <img src="${video.coverImg}"/>
                <div class="viewCount">
                    <div class="playImg" style="margin-top:5px;"><img src="<%=path%>/statics/img/video/manage/bg_37.png" /></div>
                    <div class="count">${video.viewCount}</div>
                </div>
            </div>
        </c:forEach>
    </div>

    <div id="div1"  class="userProfile-video" style="display: none;">
        <c:forEach var="image" items="${store.images}">
            <div class="picture">
                <img src="${image.picUrl}" />
            </div>
        </c:forEach>
    </div>
</div>

<input type="hidden" id="inp-storeUrl" value="${store.storeUrl}" />
<input type="hidden" id="inp-latitude" value="${store.latitude}" />
<input type="hidden" id="inp-longitude" value="${store.longitude}" />
<input type="hidden" id="inp-address" value="${store.address}" />
<input type="hidden" id="inp-addressName" value="${store.addressName}" />

<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script src='<%=path%>/statics/js/zdialog.js'></script>
<script src='<%=path%>/statics/js/toast.js'></script>
<script src='https://res.wx.qq.com/open/js/jweixin-1.4.0.js' type="text/javascript"></script>
<script>

    $(function () {
        $("#menubar li").click(function () {
            $(this).siblings('li').removeClass('myStoreBarSelected');
            $(this).addClass('myStoreBarSelected');
            for (var i = 0; i < 4; i++) {
                if ($(this).index() == i) {
                    $("#div" + i).show();
                } else {
                    $("#div" + i).hide();
                }
            }
        });

        $.ajax({
            async: false,
            url: '${contextPath}/app/video/getWxConfig',//后台请求接口，找后台要
            data:{url:window.location.href},
            type: "get",
            dataType: "json",
            success: function(data) {
                wx.config({
                    debug: false,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。true为调用开启
                    appId: data.appid,//appid// 必填，公众号的唯一标识
                    timestamp: data.timestamp, // 必填，生成签名的时间戳
                    nonceStr: data.nonceStr,// 必填，生成签名的随机串
                    signature: data.signature,// 必填，签名，
                    jsApiList: ['openLocation']
                });

                wx.ready(function() {
                    wx.onMenuShareAppMessage({
                        title: sharedTitle, // 分享标题
                        desc: sharedWords, // 分享描述
                        link: sharedLink, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                        imgUrl: sharedImg, // 分享图标
                        type: '', // 分享类型,music、video或link，不填默认为link
                        dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                        success: function () {
                            // 用户点击了分享后执行的回调函数
                        }
                    });

                    //qq空间或者朋友圈
                    wx.onMenuShareTimeline({
                        title: sharedTitle, // 分享标题
                        link: sharedLink, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                        imgUrl: sharedImg, // 分享图标
                        success: function() {
                            // 用户确认分享后执行的回调函数

                        }
                    });
                });
            },
            error: function(xhr) {
                console.log("error");
            }
        });
    });


    function showTel() {
        $.DialogByZ.Confirm({Title: "", Content: "确定要拨打电话吗？",FunL:confirmTel,FunR:cancel})
    };
    function confirmTel(){
        $.DialogByZ.Close();
        window.location.href='tel:' + $("#storeTel").html();
    }
    function cancel() {
        $.DialogByZ.Close();
    }

    function more() {
        var linkUrl = $("#inp-storeUrl").val();
        if (linkUrl.substr(0, 4) != 'http'){
            linkUrl = 'http://' + linkUrl;
        };
        window.location.href = linkUrl;
    }

    //打开地图
    function openLocation() {
        var latitude = $("#inp-latitude").val();
        var longitude = $("#inp-longitude").val();
        window.location.href='${contextPath}/app/map/showMap?longitude=' + longitude + '&latitude=' + latitude;
    };

</script>
</body>
</html>