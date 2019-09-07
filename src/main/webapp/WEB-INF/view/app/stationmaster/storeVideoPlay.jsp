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
    <title>视频广场</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link href="<%=path%>/statics/css/videoPlay.css" rel="stylesheet">
    <link href="<%=path%>/statics/css/video-js.min.css" rel="stylesheet">
    <link href="<%=path%>/statics/css/swiper.min.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="<%=path%>/statics/css/zdialog.css" />

    <style>
        .video_wraps{position: absolute; z-index: 1; left:0; top:0; right:0; height:100%; visibility:visible; display:flex; justify-content:center; align-items: center; background:none; overflow: hidden;}
        .video_wraps video{ width:100%; height:auto; display:block; position:relative; z-index:2;}
    </style>

</head>
<body style="padding:0;margin:0;background: #000;">
<div id="div-video" class="video_wraps" style="position: absolute;top:0;left:0;width:100%;display: none;">
    <video id="my-video"
           preload="auto" loop
           webkit-playsinline="true" playsinline="true" x5-video-player-type="h5"
           x5-video-orientation="portrait"
           style="object-fit:cover;display: block;object-position: center center;">
        <c:if test="${video.videoType == 1}">
            <source id="video-source" src="${video.videoUrl}" type="video/mp4">
        </c:if>
    </video>
</div>
<%--店铺图片切换--%>
<div id="div-storePic" class="storePic-wraps"  style="position: absolute;top:0;left:0;width:100%;display: none;">
    <div class="swiper-wrapper" id="swiper-storePic">
        <div class="swiper-slide">
            <div class="video-poster">

            </div>
        </div>
    </div>
</div>
<div class="swiper-container">
    <div class="swiper-wrapper" id="swiper-wrapper">
        <div class="swiper-slide">
            <input id="inp-videoUrl0" type="hidden" value="${video.videoUrl}" />
            <input id="inp-videoId0" type="hidden" value="${video.id}" />
            <input id="inp-title0" type="hidden" value="${video.title}" />
            <input id="inp-coverImg0" type="hidden" value="${video.coverImg}" />
            <input id="inp-sharedWords0" type="hidden" value="${video.sharedWords}" />
            <input id="inp-copyNo0" type="hidden" value="${video.copyNo}" />
            <div id="video-poster0" class="video-poster">
                <img src="${video.coverImg}"/>
            </div>
            <div class="video-play-bottom" >
                <div class="bottom-author" >
                    <div class="headImg"  onclick="window.location.href='<%=path%>/app/stationmaster/storeDetail/${video.storeId}'"><img src="${headImg}"/></div>
                    <div class="userName" onclick="window.location.href='<%=path%>/app/stationmaster/storeDetail/${video.storeId}'">${pageName}</div>
                    <c:choose>
                        <c:when test="${userCopyNo != video.copyNo}">
                            <div class="changeBtn" onclick="window.location.href='<%=path%>/app/stationmaster/storeDetail/${video.storeId}'">进入主页</div>
                        </c:when>
                        <c:otherwise>
                            <div class="changeBtn" onclick="window.location.href='<%=path%>/app/store/myStoreVideoList'">我的视频</div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="bottom-title"><span>${video.title}</span></div>
            </div>
        </div>
    </div>
</div>

<div id="play-btn" class="play-btn" onclick="play()">
    <img src="<%=path%>/statics/img/video/manage/bg_37.png"/>
</div>
<div id="arrow" class="video-pley-arrow">
    <img src="<%=path%>/statics/img/video/manage/bg_38.png"/>
</div>

<input type="hidden" id="inp-videoType" value="${video.videoType}"/>
<input type="hidden" id="inp-title" value="${video.title}"/>
<input type="hidden" id="inp-id" value="${video.id}"/>
<input type="hidden" id="inp-index" value="${index}"/>
<input type="hidden" id="inp-type" value="${type}"/>
<input type="hidden" id="inp-coverImg" value="${video.coverImg}"/>
<input type="hidden" id="inp-sharedWords" value="${video.sharedWords}"/>
<input type="hidden" id="inp-copyNo" value="${video.copyNo}"/>
<input type="hidden" id="inp-userCopyNo" value="${userCopyNo}" />
<input type="hidden" id="inp-subscribeFlag" value="${subscribeFlag}" />
<input type="hidden" id="inp-videoType" value="${video.videoType}" />

<div id="video-play-productbtn" class="video-play-productbtn" onclick="showAdv()">
    <div class="cart"><img id="showAdvImg" src="<%=path%>/statics/img/video/manage/goods.png"></div>
    <div class="text" id="showAdvText">心仪好物</div>
    <div id="productbtn-arraw" class="arraw"><img src="<%=path%>/statics/img/video/manage/bg_43.png"></div>
</div>
<div id="video-play-advProduct" class="video-play-advProduct">
</div>

<div id="video-play-advStore" class="video-play-advStore">
    <div class="store-top">
        <div class="store-img"><img id="storeImg" src="" /></div>
        <div class="store-name">
            <div class="name" id="storeName"></div>
            <div class="store-desc">
                <div class="storeflag"><img src="<%=path%>/statics/img/video/manage/store01.png" /></div>
                <div class="desc">实体店推广</div>
            </div>
        </div>
    </div>
    <div class="advStore-address">
        <div class="advStoreAddress" id="storeAddress">地址：</div>
        <div class="advStoreMap"><img src="<%=path%>/statics/img/video/manage/map_2.png" /></div>
    </div>
    <div class="store-tel">
        <div class="tel" onclick="showTel()">电话：<span id="storeTel"></span></div>
        <div class="enterStoreBtn" onclick="enterStore()">进店看看</div>
    </div>
    <div class="store-wx" onclick="openStoreWxDialog()">
        <div class="wxlogo"><img src="<%=path%>/statics/img/promotion/advWx.png" /></div>
        <div class="text">店家微信</div>
    </div>
    <div class="closeBtn" onclick="closeStore()"><img src="<%=path%>/statics/img/video/manage/close.png" /></div>
</div>

<div class="adv-top">
    <div class="adv-top-img">
        <img src="${stationUserMap.headImg}"/>
    </div>
    <div class="adv-top-label">${stationUserMap.userName}</div>
    <div class="adv-top-button" onclick="openStoreWxDialog()">关注</div>
</div>

<div id="video-play-advOther" class="video-play-advOther">
    <div class="advOther">
        <div class="headImg"><img id="otherAdv-img" src="<%=path%>/statics/img/test/001.jpg" /></div>
        <div class="userName" id="otherAdv-userName"></div>
        <div class="desc" id="otherAdv-desc"></div>
        <div class="otherQrcode"><img id="otherAdv-qrcode" src="<%=path%>/statics/img/test/002.jpg" /></div>
        <div class="desc" id="otherAdv-hint">长按识别，关注公众号</div>
        <div class="closeBtn" onclick="closeOther()"><img src="<%=path%>/statics/img/video/manage/close.png" /></div>
    </div>
</div>

<div id="subscribe" class="video-play-advOther">
    <div class="advOther">
        <div class="headImg"><img src="<%=path%>/statics/img/promotion/advPublic.png" /></div>
        <div class="userName">您已成功注册</div>
        <div class="desc" style="height: 30px;">关注公众号，开始使用</div>
        <div class="otherQrcode"><img src="<%=path%>/statics/img/video/manage/stbQRCode.jpg" /></div>
        <div class="desc">长按识别，关注视推宝</div>
        <div class="closeBtn" onclick="closeSubscribe()"><img src="<%=path%>/statics/img/video/manage/close.png" /></div>
    </div>
</div>

<div id="storeWx-dialog" class="video-play-advOther">
    <div class="advOther" style="height: 335px;">
        <div class="otherQrcode" style="margin-top:30px;"><img id="img-qrcode" src="${stationUserMap.WxQRCode}" /></div>
        <div class="storeWx wxNo" id="inp-wx">${stationUserMap.wx}</div>
        <div class="storeWx copyWxNo" id="copyBtn">复制微信号</div>
        <div class="closeBtn" onclick="closeStoreWx()"><img src="<%=path%>/statics/img/video/manage/close.png" /></div>
    </div>
</div>

<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script src='<%=path%>/statics/js/video.min.js'></script>
<script src='<%=path%>/statics/js/swiper.min.js'></script>
<script src='https://res.wx.qq.com/open/js/jweixin-1.4.0.js' type="text/javascript"></script>
<script src="<%=path%>/statics/js/toast.js"></script>
<script src="<%=path%>/statics/js/clipboard.min.js"></script>
<script src='<%=path%>/statics/js/zdialog.js'></script>
<script type="text/javascript">

    var data = {
        playVideoId: $("#inp-id").val(),
        playVideoIndex: $("#inp-index").val(),
        type:$("#inp-type").val(),
        page: 1,
        limit: 10,
        videoCount: 1,
        autoLoadLimt: 2,
        videoList: [],

        playIndex: 0,
        videoId: $("#inp-id").val(),

        getDataListFlag: 0,
        userCopyNo: $("#inp-userCopyNo").val(),
        videoType: $("#inp-videoType").val(),
    };
    var initAppUser = {

    }
    var initVideo = {
        id: $("#inp-id").val(),
        videoUrl: $("#inp-videoUrl0").val(),
        title: $("#inp-title0").val(),
        coverImg: $("#inp-coverImg0").val(),
        sharedWords: $("#inp-sharedWords0").val(),
        copyNo: $("#inp-copyNo0").val(),
        appUser: initAppUser,
        videoType: $("#inp-videoType").val(),
    }
    data.videoList.push(initVideo);
    getDataList();

    $(function () {
        if (initVideo.videoType == 1){
            $("#my-video").attr("src", initVideo.videoUrl);
            $("#my-video_html5_api").attr("src", initVideo.videoUrl);
            $("#video-source").attr("src",initVideo.videoUrl);
        }
        var clipboard2 = new ClipboardJS('#copyBtn', {
            text: function() {
                return $("#inp-wx").html();
            }
        });
        clipboard2.on('success', function(e) {
            Toast('微信号复制成功', 2000);
        });
        clipboard2.on('error', function (e) {
        });
        wxShare();
        $("#div-storePic").height(window.innerHeight + "px");
        $(".swiper-slide").height(window.innerHeight + "px");
    });

    $("#my-video").attr("width", window.innerWidth);

    window.onresize = function(){
        $("#my-video").width(window.innerWidth + "px");
        $("#my-video").height(window.innerHeight + "px");
        $("#div-storePic").height(window.innerHeight + "px");
        $(".swiper-slide").height(window.innerHeight + "px");
    }

    function getDataList() {
        if (data.getDataListFlag == 1){
            return;
        }
        data.getDataListFlag = 1;
        $.ajax({
            url: "${contextPath}/app/store/getStoreVideoList",
            data: {"playVideoId": data.playVideoId ,"playVideoIndex": data.playVideoIndex, "type": data.type, "page": data.page, "limit": data.limit},
            success: function (res) {
                if (res.data.length == 0){
                    data.getDataListFlag = 1;
                    return;
                }
                var str = "";
                for (var i=0;i<res.data.length;i++){
                    var video = res.data[i];
                    data.videoList.push(video);
                    str += '<div class="swiper-slide"><div id="swiper-poster-back' + (data.videoCount + i) + '" class="swiper-poster-back"><div id="video-poster'
                        + (data.videoCount + i) + '" class="video-poster"><img src="'+ video.coverImg + '"></div></div>';
                    str += '<div class="video-play-bottom"><div class="bottom-author">';
                    str += '<div class="headImg" onclick="window.location.href=\'${contextPath}/app/userProfile?copyNo=' + video.copyNo
                        + '\'"><img src="' + video.appUser.headImg + '"/></div>';

                    if (video.appUser.userPage == null || video.appUser.userPage.pageName == null || video.appUser.userPage.pageName == ''){
                        str += '<div class="userName" onclick="window.location.href=\'${contextPath}/app/stationmaster/storeDetail/' + video.storeId + '\'">' + video.appUser.userName + '</div>';
                    }else{
                        str += '<div class="userName" onclick="window.location.href=\'${contextPath}/app/stationmaster/storeDetail/' + video.storeId + '\'">' + video.appUser.userPage.pageName + '</div>';
                    }

                    if (video.copyNo == data.userCopyNo){
                        str += '<div class="changeBtn" onclick="window.location.href=\'${contextPath}/app/video/myVideoList\'">我的视频</div>'
                    }else{
                        if (video.showBtnType == 1){
                            str += '<div class="changeBtn" onclick="changeMyAdv(' + video.id +')">换成我的广告</div>';
                        }else{
                            str += '<div class="changeBtn" onclick="window.location.href=\'${contextPath}/app/stationmaster/storeDetail/' + video.storeId + '\'">进入主页</div>';
                        }
                    }
                    str += '</div><div class="bottom-title"><span>' + video.title + '</span></div></div></div>';
                }
                $("#swiper-wrapper").append(str);
                data.videoCount += res.data.length;
                data.page ++;
                initSwiper();
            }
        })
    }

    //打开地图
    function openLocation(e) {
        var latitude = e.currentTarget.dataset.latitude;
        var longitude = e.currentTarget.dataset.longitude;
        var name = e.currentTarget.dataset.name;
        var address = e.currentTarget.dataset.address;
        if(latitude && longitude){
            wx.openLocation({
                latitude: parseFloat(latitude),
                longitude: parseFloat(longitude), // 经度，浮点数，范围为180 ~ -180。
                name: name || address, // 位置名
                address: address, // 地址详情说明
                scale: 14,
                infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
            });
        }else{
            $.toast("未设置地址", "forbidden");
        }
    };


    var bottom = 0;
    var autoPlay = 0;
    var playFlag = 0;
    var playIndex = 0;
    var storeId;
    var advType;
    var advId;
    var watchword = '';
    var linkUrl = '';
    var otherAdvType;
    var advArrow;
    var sharedTitle = '';
    var sharedWords ='';
    var sharedLink = '';
    var sharedImg = '';
    document.title = $("#inp-title").val();

    var videoId = $("#inp-id").val();
    var int = self.setInterval("arrowRoll()", 50);
    var myPlayer = videojs('my-video');
    var userCopyNo = $("#inp-userCopyNo").val();

    var mySwiper;

    function arrowRoll() {
        bottom = bottom + 1;
        if (bottom == 20) {
            bottom = 0;
        }
        $("#arrow").css("bottom", bottom + "px");
        $("#arrow").css("opacity", 1 - bottom / 20);
    }

    function initSwiper() {
        mySwiper && mySwiper.destroy();
        mySwiper = new Swiper('.swiper-container', {
            initialSlide: data.playIndex,
            direction: 'vertical', // 垂直切换选项
            loop: false, // 循环模式选项
            on: {
                slideChangeTransitionStart: function(){
                    $("#video-play-advProduct").hide();
                    $("#video-play-productbtn").hide();
                    $(".video-play-advStore").hide();
                },
                click: function(){
                    if (playFlag == 1){
                        myPlayer.pause();
                        $("#play-btn").show();
                        playFlag = 2;
                    }else if (playFlag == 2){
                        $("#play-btn").hide();
                        myPlayer.play();
                        playFlag = 1;
                    }
                },
                slideChangeTransitionEnd: function(){
                    $("#swiper-storePic").html('');
                    $("#swiper-storePic").hide();
                    $("#video-play-advProduct").html("");
                    $("#video-play-advProduct").hide();
                    $("#video-play-productbtn").hide();
                    $(".video-play-advStore").hide();

                    myPlayer.pause();
                    data.playIndex = this.activeIndex;
                    data.videoId = data.videoList[this.activeIndex].id;
                    data.videoType = data.videoList[this.activeIndex].videoType;

                    if (data.videoType == 1){
                        var videoUrl = data.videoList[this.activeIndex].videoUrl;
                        $("#my-video").attr("src", videoUrl);
                        $("#my-video_html5_api").attr("src", videoUrl);
                        $("#video-source").attr("src", videoUrl);
                    }

                    document.title = data.videoList[this.activeIndex].title;
                    wxShare();

                    if ((this.activeIndex + data.autoLoadLimt) >= data.videoCount){
                        getDataList();
                    }

                    if (autoPlay == 1){
                        setTimeout(play(this.activeIndex), 1000);
                    }
                },
            },
        })
        data.getDataListFlag = 0;
    }



    function wxShare() {
        sharedWords = data.videoList[data.playIndex].sharedWords;
        if (sharedWords === null || sharedWords === ''){
            sharedWords = '这个视频很有趣，点击查看☞';
        }
        sharedTitle = data.videoList[data.playIndex].title;
        sharedLink = 'http://wx.stb.pkddz.cn/app/store/storeVideoPlay?type=myVideo&id=' + data.videoList[data.playIndex].id
            + '&copyNo=' + data.videoList[data.playIndex].copyNo+'&stationCopyNo=${stationCopyNo}';
        sharedImg = data.videoList[data.playIndex].coverImg;
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
                    jsApiList: ['updateAppMessageShareData','updateTimelineShareData','onMenuShareAppMessage','onMenuShareTimeline','openLocation']
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
    }


    function play() {
        $(".swiper-poster-back").each(function () {
            $(this).show();
        });
        $(".video-poster").each(function () {
            $(this).show();
        });
        $("#swiper-poster-back" + data.playIndex).hide();
        $("#video-poster" + data.playIndex).hide();
        $(".swiper-container").attr("opacity", 0);
        $("#play-btn").hide();

        if (data.videoType == 2){
            $("#div-video").hide();
            $.ajax({
                url: '${contextPath}/app/store/getStoreImage?videoId=' + data.videoId,
                dataType: 'json',
                beforeSend: function(){
                    $("#swiper-storePic").html('');
                },
                success: function(res){
                    var imageList = res.data;
                    var str = '';
                    for (var i=0;i<imageList.length;i++){
                        str += '<div class="swiper-slide" style="width:100%;height:100%;"><div class="videoStorePic" style="width:100%;height:100%;">' +
                            '<img src="' + imageList[i].picUrl +  '" /></div></div>';
                    }
                    $("#swiper-storePic").html(str);
                    $("#div-storePic").show();
                    $("#swiper-storePic").show();
                    storePicSwiper && storePicSwiper.destroy();
                    var storePicSwiper = new Swiper('.storePic-wraps',{
                        autoplay: {
                            delay: 2000,//2秒切换一次
                        },
                        speed:600,
                        autoHeight:true,
                        loop: true,
                        effect : 'flip',
                        flipEffect: {
                            slideShadows : true,
                            limitRotation : true,
                        }
                    });
                    setTimeout(function(){
                        $(".videoStorePic").each(function () {
                            var oImg = $(this).find("img")[0];
                            if ($(oImg).height() < $(this).height()){
                                var mh = ($(this).height() - $(oImg).height()) / 2;
                                $(oImg).css("margin-top", mh + "px");
                            }
                        })
                    }, 2000);
                }
            })
        }else {

            $(".adv-top").show();
            $("#div-video").show();
            autoPlay = 1;

            videojs("my-video").ready(function () {
                var myPlayer = this;
                myPlayer.play();
            });

            //增加客户观看记录
/*            addViewCount(0, 0, 1);*/

        }

        setTimeout(function(){ playFlag = 1; }, 1000);
    }

    function showAdvOpenBtn(index) {
        clearInterval(advArrow);
        $("#video-play-productbtn").show();
        var productArrawLeft = 16;
        advArrow = self.setInterval(function () {
            if (productArrawLeft > 2) {
                productArrawLeft = productArrawLeft - 1;
            } else {
                productArrawLeft = 16;
            }
            $("#productbtn-arraw").css("margin-left", productArrawLeft + "px");
        }, 50);
    }

    function showAdv() {
        if (advType == 1){
            openProductAdv();
        }else if (advType == 2){
            openStoreAdv();
        }else if (advType == 3){
            if (otherAdvType == 5 || otherAdvType == 6 || otherAdvType == 7){
                linkAdv(linkUrl, advId);
            }else{
                openOtherAdv();
            }
        }
    }
    function openProductAdv() {
        var advProductBottom = -150;
        $("#video-play-advProduct").css("bottom", advProductBottom + "px");
        $("#video-play-advProduct").show();
        var myVar = self.setInterval(function() {
            if (advProductBottom < 70) {
                advProductBottom = advProductBottom + 5;
            }else{
                clearInterval(myVar);
            }
            $("#video-play-advProduct").css("bottom", advProductBottom + "px");
        }, 20);
    }

    function openStoreAdv() {
        var advProductBottom = -150;
        $("#video-play-advStore").css("bottom", advProductBottom + "px");
        $("#video-play-advStore").show();
        var myVar = self.setInterval(function() {
            if (advProductBottom < 0) {
                advProductBottom = advProductBottom + 5;
            }else{
                clearInterval(myVar);
            }
            $("#video-play-advStore").css("bottom", advProductBottom + "px");
        }, 20);
    }
    function openOtherAdv() {
        addViewCount(advType, advId, 3);
        $("#video-play-advOther").show();
    }
    function openStoreWxDialog() {
        $("#storeWx-dialog").show();
    }

    function enterStore() {
        addViewCount(advType, storeId, 3);
        window.location.href = '${contextPath}/app/store/myStore?id=' + storeId;
    }
    function closeAdv() {
        $("#video-play-advProduct").hide();
    }
    function closeStore() {
        $("#video-play-advStore").hide();
    }
    function closeOther() {
        $("#video-play-advOther").hide();
    }
    function closeSubscribe() {
        $("#subscribe").hide();
    }
    function closeStoreWx() {
        $("#storeWx-dialog").hide();
    }

    function copyWatchWord(tbWatchword, tbAdvId) {
        addViewCount(advType, tbAdvId, 2);
        var save = function (e){
            e.clipboardData.setData('text/plain',tbWatchword);//下面会说到clipboardData对象
            e.preventDefault();//阻止默认行为
        }
        document.addEventListener('copy',save);
        document.execCommand("copy");//使文档处于可编辑状态，否则无效
        Toast('淘宝口令复制成功!',2000);
    }
    function linkAdv(linkUrl, advId) {
        if (advType = 1){
            addViewCount(advType, advId , 2);
        }else{
            addViewCount(advType, advId , 3);
        }

        if (linkUrl.substr(0, 4) != 'http'){
            linkUrl = 'http://' + linkUrl;
        };
        window.location.href = linkUrl;
    }
    function changeMyAdv(id) {
        $.ajax({
            url: '${contextPath}/app/video/checkVideoCount',
            dataType: 'json',
            success: function(res){
                if (res.data){
                    window.location.href='${contextPath}/app/video/originalVideoEdit?type=replace&id=' + id;
                }else{
                    $.DialogByZ.Confirm({Title: "", Content: "非会员只能生成8条视频！</br>是否立即开通会员？",FunL:confirmOpen,FunR:cancel})
                }
            }
        })
    }
    function confirmOpen(){
        window.location.href = '${contextPath}/app/advPay/openVip';
    }
    function cancel() {
        $.DialogByZ.Close();
    }

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

    function addViewCount(advType, advId, viewType) {
        $.ajax({
            url: '${contextPath}/app/customer/viewAdv',
            data: {'advType':advType, 'advId':advId, 'viewType':viewType, 'copyNo':data.videoList[data.playIndex].copyNo},
            method: 'post',
            success:function (res) {
            }
        })
    }


</script>
</body>
</html>