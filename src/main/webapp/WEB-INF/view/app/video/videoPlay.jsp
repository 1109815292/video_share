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
        <source id="video-source" src="${video.videoUrl}" type="video/mp4">
    </video>
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
                    <div class="headImg"  onclick="window.location.href='<%=path%>/app/userProfile?copyNo=${video.copyNo}'"><img src="${headImg}"/></div>
                    <div class="userName" onclick="window.location.href='<%=path%>/app/userProfile?copyNo=${video.copyNo}'">${pageName}</div>
                    <c:choose>
                        <c:when test="${userCopyNo != video.copyNo}">
                            <c:choose>
                                <c:when test="${video.showBtnType eq 1}">
                                    <div class="changeBtn" onclick="changeMyAdv(${video.id})">换成我的广告</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="changeBtn" onclick="window.location.href='<%=path%>/app/userProfile?copyNo=${video.copyNo}'">进入主页</div>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <div class="changeBtn" onclick="window.location.href='<%=path%>/app/video/myVideoList'">我的视频</div>
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


<input type="hidden" id="inp-title" value="${video.title}"/>
<input type="hidden" id="inp-id" value="${video.id}"/>
<input type="hidden" id="inp-index" value="${index}"/>
<input type="hidden" id="inp-type" value="${type}"/>
<input type="hidden" id="inp-coverImg" value="${video.coverImg}"/>
<input type="hidden" id="inp-sharedWords" value="${video.sharedWords}"/>
<input type="hidden" id="inp-copyNo" value="${video.copyNo}"/>
<input type="hidden" id="inp-userCopyNo" value="${userCopyNo}" />
<input type="hidden" id="inp-subscribeFlag" value="${subscribeFlag}" />

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
        <div class="advStoreMap" onclick="openLocation()"><img src="<%=path%>/statics/img/video/manage/map_2.png" /></div>
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
    <div class="adv-top-button">关注</div>
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
        <div class="otherQrcode" style="margin-top:30px;"><img id="img-wxQRCode" src="" /></div>
        <div class="storeWx wxNo" id="div-wx"></div>
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
        videoId: 0,
        latitude: 0,
        longitude: 0,
        address: '',
        wx: '',
        wxQRCode: '',

        getDataListFlag: 0,
        userCopyNo: $("#inp-userCopyNo").val(),
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
    }
    data.videoList.push(initVideo);
    getDataList();

    $("#my-video").attr("width", window.innerWidth);

    window.onresize = function(){
        $("#my-video").width(window.innerWidth + "px");
        $("#my-video").height(window.innerHeight + "px");
    }

    function getDataList() {
        data.getDataListFlag = 1;
        $.ajax({
            url: "${contextPath}/app/video/getPlayVideoList",
            data: {"playVideoId": data.playVideoId ,"playVideoIndex": data.playVideoIndex, "type": data.type, "page": data.page, "limit": data.limit},
            success: function (res) {
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
                        str += '<div class="userName" onclick="window.location.href=\'${contextPath}/app/userProfile?copyNo=' + video.copyNo + '\'">' + video.appUser.userName + '</div>';
                    }else{
                        str += '<div class="userName" onclick="window.location.href=\'${contextPath}/app/userProfile?copyNo=' + video.copyNo + '\'">' + video.appUser.userPage.pageName + '</div>';
                    }

                    if (video.copyNo == data.userCopyNo){
                        str += '<div class="changeBtn" onclick="window.location.href=\'${contextPath}/app/video/myVideoList\'">我的视频</div>'
                    }else{
                        if (video.showBtnType == 1){
                            str += '<div class="changeBtn" onclick="changeMyAdv(' + video.id +')">换成我的广告</div>';
                        }else{
                            str += '<div class="changeBtn" onclick="window.location.href=\'${contextPath}/app/userProfile?copyNo=' + video.copyNo + '\'">进入主页</div>';
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
                    myPlayer.pause();
                    data.playIndex = this.activeIndex;
                    data.videoId = data.videoList[this.activeIndex].id;

                    $("#video-play-advProduct").html("");
                    $("#video-play-advProduct").hide();
                    $("#video-play-productbtn").hide();
                    $(".video-play-advStore").hide();

                    var videoUrl = data.videoList[this.activeIndex].videoUrl;
                    $("#my-video").attr("src", videoUrl);
                    $("#my-video_html5_api").attr("src", videoUrl);
                    $("#video-source").attr("src", videoUrl);

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


    $(function () {
        var clipboard2 = new ClipboardJS('#copyBtn', {
            text: function() {
                return $("#div-wx").html();
            }
        });
        clipboard2.on('success', function(e) {
            Toast('微信号复制成功', 2000);
        });
        clipboard2.on('error', function (e) {
        });
        wxShare();
    });

    function wxShare() {
        sharedWords = data.videoList[data.playIndex].sharedWords;
        if (sharedWords === null || sharedWords === ''){
            sharedWords = '这个视频很有趣，点击查看☞';
        }
        sharedTitle = data.videoList[data.playIndex].title;
        sharedLink = 'http://wx.stb.pkddz.cn/app/video/play?type=myVideo&id=' + data.videoList[data.playIndex].id
            + '&copyNo=' + data.videoList[data.playIndex].copyNo;
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
        })
        $(".video-poster").each(function () {
            $(this).show();
        })
        $("#swiper-poster-back" + data.playIndex).hide();
        $("#video-poster" + data.playIndex).hide();
        $("#div-video").show();
        $(".swiper-container").attr("opacity", 0);
        $("#play-btn").hide();
        autoPlay = 1;

        videojs("my-video").ready(function(){
            var myPlayer = this;
            myPlayer.play();
        });

        //增加客户观看记录
        addViewCount(0, 0, 1);

        $.ajax({
            url: '${contextPath}/app/video/getVideoInfo?id=' + data.videoList[data.playIndex].id,
            type: "get",
            dataType: "json",
            success: function (res) {
                $("#video-play-advProduct").html("");
                if (res.data.advType != null){
                    advType = res.data.advType;
                    if (advType == 1) {
                        var advProductList = res.data.advProductList;
                        var str = "";
                        for (var i = 0; i < advProductList.length; i++) {
                            str += '<div class="cont-block four-radius"><div class="block-top"><div class="top-coverImg img-square">';
                            str += '<img src="' + advProductList[i].picUrl + '" /></div><div class="top-name">';
                            str += '<div class="title">' + advProductList[i].productName + '</div>';
                            str += '<div class="price">' + advProductList[i].price + '</div>';
                            str += '<div class="closeBtn" onclick="closeAdv()"><img src="<%=path%>/statics/img/video/manage/close.png" /></div>';
                            if (advProductList[i].type==5){
                                watchword = advProductList[i].watchword;
                                str += '<div class="video-play-copyTBBtn" onclick="copyWatchWord(\'' + watchword + '\',' +  advProductList[i].id +')">复制淘宝口令</div>';
                            }else{
                                linkUrl = advProductList[i].productUrl;
                                str += '<div class="viewBtn" onclick="linkAdv(\'' + linkUrl + '\',' + advProductList[i].id + ')">去看看</div>';
                            }
                            str += '</div></div></div>';
                        }
                        $("#video-play-advProduct").append(str);
                        $("#video-play-advProduct").css("bottom", "-150px");
                        $("#video-play-advProduct").show();

                        $("#showAdvImg").attr("src", '${contextPath}/statics/img/video/manage/goods.png');
                        $("#showAdvText").html('心仪好物');

                        showAdvOpenBtn();
                        setTimeout(function (){openProductAdv();}, 5000);
                    }else if(advType == 2){
                        storeId = res.data.advStore.id;
                        data.longitude = res.data.advStore.longitude;
                        data.latitude = res.data.advStore.latitude;
                        data.address = res.data.advStore.address;
                        data.wxQRCode = res.data.advStore.wxQRCode;
                        data.wx = res.data.advStore.wx;
                        advId = storeId;
                        $("#storeImg").attr("src", res.data.advStore.picUrl);
                        $("#storeName").html(res.data.advStore.storeName);
                        $("#storeAddress").html("地址：" + res.data.advStore.address);
                        $("#storeTel").html(res.data.advStore.tel);

                        $("#showAdvImg").attr("src", '${contextPath}/statics/img/video/manage/store01.png');
                        $("#showAdvText").html(res.data.advStore.storeName);
                        showAdvOpenBtn();
                        setTimeout(function (){openStoreAdv();}, 5000);
                    }else if(advType == 3){
                        otherAdvType = res.data.advOther.type;
                        advId = res.data.advOther.id;
                        if (otherAdvType == 1){
                            $("#showAdvImg").attr('src', '${contextPath}/statics/img/promotion/advPublic.png');
                            $("#otherAdv-hint").html("长按识别，关注公众号");
                        }else if (otherAdvType == 2){
                            $("#showAdvImg").attr('src', '${contextPath}/statics/img/promotion/advWx-applet.png');
                            $("#otherAdv-hint").html("长按识别，进入小程序");
                        }else if (otherAdvType == 3){
                            $("#showAdvImg").attr('src', '${contextPath}/statics/img/promotion/advWx.png');
                            $("#otherAdv-hint").html("长按识别，加TA为好友");
                        }else if (otherAdvType == 4){
                            $("#showAdvImg").attr('src', '${contextPath}/statics/img/promotion/advWx.png');
                            $("#otherAdv-hint").html("长按识别，进群看看");
                        }else if (otherAdvType == 5){
                            $("#showAdvImg").attr('src', '${contextPath}/statics/img/promotion/advOther-app.png');
                        }else if (otherAdvType == 6){
                            $("#showAdvImg").attr('src', '${contextPath}/statics/img/promotion/advWeb.png');
                        }else if (otherAdvType == 7){
                            $("#showAdvImg").attr('src', '${contextPath}/statics/img/promotion/advLink.png');
                        }else if (otherAdvType == 8){
                            $("#showAdvImg").attr('src', '${contextPath}/statics/img/promotion/advOther-qrcode.png');
                            $("#otherAdv-hint").html("长按识别，进入瞧瞧");
                        }

                        if (otherAdvType == 5 || otherAdvType == 6 || otherAdvType == 7){
                            $("#showAdvText").html(res.data.advOther.name);
                            linkUrl = res.data.advOther.url;
                        }else {
                            $("#otherAdv-img").attr("src", res.data.advOther.picUrl);
                            $("#otherAdv-userName").html(res.data.advOther.name);
                            $("#otherAdv-desc").html(res.data.advOther.desc);
                            $("#otherAdv-qrcode").attr("src", res.data.advOther.qrcode);
                            $("#showAdvText").html(res.data.advOther.name);
                        }
                        showAdvOpenBtn();
                    }
                }
            }
        });

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
        $("#img-wxQRCode").attr("src", data.wxQRCode);
        $("#div-wx").html(data.wx);
        $("#storeWx-dialog").show();
    }

    //打开地图
    function openLocation() {
        window.location.href='${contextPath}/app/map/showMap?longitude=' + data.longitude + '&latitude=' + data.latitude;
    };

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