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
    <title>我的主页</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link href="<%=path%>/statics/layui/css/layui.css" rel="stylesheet">
    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->


</head>
<body class="bg-profile">

<div class="userProfile-top">
        <div class="headImg">
            <img id="img-headImg" src="${userPage.headImg}"/>
        </div>
        <ul>
            <li onclick="showTel()"><img src="<%=path%>/statics/img/video/manage/bg_30.png"/><span style="font-size:13px;">打电话</span></li>
            <li><div id="copyBtn"><img src="<%=path%>/statics/img/video/manage/bg_31.png"/><span style="font-size:13px;">加微信</span></div></li>
            <li onclick="showQrcode()"><img src="<%=path%>/statics/img/video/manage/bg_26.png"/><span style="font-size:13px;">二维码</span></li>
        </ul>
    <div class="profile">
        <div class="userName" id="userName">${userPage.userName}</div>
        <div class="copyNo">视推号：${userPage.copyNo}</div>
    </div>
    <div class="orther">
        <div class="copyNo" id="userSign">${userPage.sign}</div>
        <div class="vip">
            <c:choose>
                <c:when test="${userPage.vipFlag eq 'Y'}">VIP会员</c:when>
                <c:otherwise>普通用户</c:otherwise>
            </c:choose>
        </div>
        <div class="product">
            <span>${videoCount} 视频</span>
            <span>${advProductCount} 产品</span>
            <span>0 访客</span>
        </div>
    </div>
    <div class="bar">
        <ul id="menubar">
            <li class="profileSelected">视频</li>
            <li>商品</li>
            <li>广告</li>
            <li>实体店</li>
        </ul>
    </div>
</div>

<div id="qrcode" class="userProfile-qrcode">
    <img src="${userPage.wxQRCode}" />
</div>
<input type="hidden" id="inp-mobile" value="${userPage.mobile}" />
<input type="hidden" id="inp-wx" value="${userPage.wx}" />
<input type="hidden" id="inp-qrcode" value="${userPage.wxQRCode}" />
<input type="hidden" id="inp-pageName" value="${userPage.pageName}" />
<input type="hidden" id="inp-copyNo" value="${userPage.copyNo}">

<div id="div0" class="userProfile-video">
    <c:forEach var="video" items="${videoList}">
        <div class="video" onclick="window.location.href='<%=path%>/app/video/play?id=${video.id}'">
            <img src="${video.coverImg}" />
            <div class="viewCount">
                <div class="playImg"><img src="<%=path%>/statics/img/video/manage/bg_37.png" /></div>
                <div class="count">${video.viewCount}</div>
            </div>
        </div>
    </c:forEach>
</div>
<div class="cont" style="margin-top:0px;">
<div id="div1" style="display: none;"></div>
<div id="div2" style="display: none;"></div>
<div id="div3" style="display: none;"></div>
</div>

<div class="back" onclick="javascript:history.back(-1);">
    返回
</div>

<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script src='<%=path%>/statics/layui/layui.all.js'></script>
<script src='<%=path%>/statics/js/toast.js'></script>
<script src="<%=path%>/statics/js/clipboard.min.js"></script>
<script src='https://res.wx.qq.com/open/js/jweixin-1.4.0.js' type="text/javascript"></script>
<script>

    $(function(){
        $("#menubar li").click(function() {
            $(this).siblings('li').removeClass('profileSelected');
            $(this).addClass('profileSelected');
            for (var i=0;i<4;i++){
                if ($(this).index() == i){
                    $("#div" + i).show();
                }else{
                    $("#div" + i).hide();
                }
            }
            if ($(this).index() == 0){
                getVideoData();
            }else if  ($(this).index() == 1){
                getAdvData(1, $(this).index());
            }else if  ($(this).index() == 2){
                getAdvData(3, $(this).index());
            }else if  ($(this).index() == 3){
                getAdvData(2, $(this).index());
            }
        });

        var clipboard2 = new ClipboardJS('#copyBtn', {
            text: function() {
                return $("#inp-wx").val();
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
        var pageName = $("#inp-pageName").val();
        if (pageName == null || pageName == ''){
            pageName = $("#userName").html();
        }
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
                    jsApiList: ['updateAppMessageShareData','updateTimelineShareData','onMenuShareAppMessage','onMenuShareTimeline']
                });

                wx.ready(function() {
                    wx.onMenuShareAppMessage({
                        title: pageName, // 分享标题
                        desc: $("#userSign").html(), // 分享描述
                        link: 'http://wx.stb.pkddz.cn/app/userProfile?copyNo=' + $("#inp-copyNo").val(),
                        imgUrl: $("#img-headImg").attr("src"), // 分享图标
                        type: '', // 分享类型,music、video或link，不填默认为link
                        dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                        success: function () {
                            // 用户点击了分享后执行的回调函数
                        }
                    });

                    //qq空间或者朋友圈
                    wx.onMenuShareTimeline({
                        title: pageName, // 分享标题
                        desc: $("#userSign").html(), // 分享描述
                        link: 'http://wx.stb.pkddz.cn/app/userProfile?copyNo=' + $("#inp-copyNo").val(),
                        imgUrl: $("#img-headImg").attr("src"), // 分享图标
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

    function showTel() {
        layer.open({
            type: 1
            ,title: false //不显示标题栏
            ,closeBtn: false
            ,area: '300px;'
            ,shade: 0.8
            ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
            ,btn: ['确定', '取消']
            ,btnAlign: 'c'
            ,moveType: 1 //拖拽模式，0或者1
            ,content: '<div style="padding: 30px; line-height: 22px; background-color: #393D49; color: #fff; font-size:16px; font-weight: 300;"><div style="font-size:18px;text-align:center;">提示！</div><br>'
                + '确定要拨打电话' + $("#inp-mobile").val() + '吗？</div>'
            ,success: function(layero){
                var btn = layero.find('.layui-layer-btn');
                btn.find('.layui-layer-btn0').attr({
                    href: 'tel:' + $("#inp-mobile").val()
                    ,target: '_blank'
                });
            }
        });
    }

    function showWx() {
        var save = function (e){
                e.clipboardData.setData('text/plain',$("#inp-wx").val());//下面会说到clipboardData对象
                e.preventDefault();//阻止默认行为
        }
        document.addEventListener('copy',save);
        document.execCommand("copy");//使文档处于可编辑状态，否则无效
        Toast('微信号复制成功',2000);
    }

    function showQrcode() {
        layer.open({
            type: 1
            ,title: false //不显示标题栏
            ,closeBtn: false
            ,area: '300px;'
            ,shade: 0.8
            ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
            ,btn: ['关闭']
            ,btnAlign: 'c'
            ,moveType: 1 //拖拽模式，0或者1
            ,content: '<div style="padding: 30px; line-height: 22px; background-color: #393D49; color: #fff; font-size:16px; font-weight: 300;"><img src="' + $("#inp-qrcode").val() + '"/></div>'
            ,success: function(layero){
            }
        });
    }

    function getAdvData(advType, divIndex){
        $.ajax({
            url: '${contextPath}/app/getHomePageData?advType=' + advType + '&copyNo=' + $("#inp-copyNo").val(),
            type: "get",
            dataType: "json",
            success: function(res) {
                var list = res.data;
                var str = "";
                $("#div" + divIndex).html("");
                if (advType == 1){
                    for (var i=0;i<list.length;i++){
                        str += '<div class="userProfile-product"><div class="product"><div class="leftImg"><img src="' + list[i].picUrl + '" /> </div>';
                        str += '<div class="top-name"><div class="title">' + list[i].productName + '</div>';
                        str += '<div class="price">' + list[i].price + '</div>';
                        str += '</div>';
                        if (list[i].type == 5){
                            str += '<div class="showBtn btn-onBottom" onclick="copyWatchWord(\'';
                            str += list[i].watchword + '\')">复制淘宝口令</div>';
                        }else{
                            str += '<div class="showBtn btn-onBottom" onclick="linkAdv(\'';
                            str += list[i].productUrl + '\')">去看看</div>';
                        }
                        str += '</div></div>';
                    }
                }else if (advType == 2){
                    for (var i=0;i<list.length;i++){
                        str += '<div class="userProfile-product"><div class="product"><div class="leftImg"><img src="' + list[i].picUrl + '" /> </div>';
                        str += '<div class="top-name"><div class="title">' + list[i].storeName + '</div>';
                        str += '<div class="desc">' + list[i].storeDesc + '</div>';
                        str += '</div>';
                        str += '<div class="showBtn btn-onTop" onclick="window.location.href=\'${contextPath}/app/store/myStore?id=' + list[i].id + '\'">进入店铺</div></div></div>';
                    }
                }else if (advType == 3){
                    for (var i=0;i<list.length;i++){
                        str += '<div class="userProfile-adv"><div class="adv"><div class="leftImg"><img src="' + list[i].picUrl + '" /> </div>';
                        str += '<div class="top-name"><div class="title">' + list[i].name + '</div>';
                        str += '<div class="desc">' + list[i].desc + '</div>';
                        str += '</div>';
                        if (list[i].type == 1){
                            str += '<div class="advType"><div class="typeImg"><img src="${contextPath}/statics/img/promotion/advPublic.png" /></div><div class="typeText">公众号</div></div>';
                            str += '<div class="advQrcode"><img src="' + list[i].qrcode + '"/></div>';
                        }else if (list[i].type == 2){
                            str += '<div class="advType"><div class="typeImg"><img src="${contextPath}/statics/img/promotion/advWx-applet.png" /></div><div class="typeText">小程序</div></div>';
                            str += '<div class="advQrcode"><img src="' + list[i].qrcode + '"/></div>';
                        }else if (list[i].type == 3){
                            str += '<div class="advType"><div class="typeImg"><img src="${contextPath}/statics/img/promotion/advWx.png" /></div><div class="typeText">微信</div></div>';
                            str += '<div class="advQrcode"><img src="' + list[i].qrcode + '"/></div>';
                        }else if (list[i].type == 4){
                            str += '<div class="advType"><div class="typeImg"><img src="${contextPath}/statics/img/promotion/advWx.png" /></div><div class="typeText">微信群</div></div>';
                            str += '<div class="advQrcode"><img src="' + list[i].qrcode + '"/></div>';
                        }else if (list[i].type == 5){
                            str += '<div class="showBtn btn-onTop" onclick="linkAdv(\'' + list[i].url + '\')">下载APP</div>';
                        }else if (list[i].type == 6){
                            str += '<div class="showBtn btn-onTop" onclick="linkAdv(\'' + list[i].url + '\')">进入官网</div>';
                        }else if (list[i].type == 7){
                            str += '<div class="showBtn btn-onTop" onclick="linkAdv(\'' + list[i].url + '\')">进去看看</div>';
                        }else if (list[i].type == 8){
                            str += '<div class="advType"><div class="typeImg"><img src="${contextPath}/statics/img/promotion/advOther-qrcode.png" /></div><div class="typeText">二维码</div></div>';
                            str += '<div class="advQrcode"><img src="' + list[i].qrcode + '"/></div>';
                        }
                        str += '</div></div>';
                    }
                }
                $("#div" + divIndex).append(str);
            }
        });
    }

    function linkAdv(str) {
        if (str.substr(0, 4) != 'http'){
            str = 'http://' + str;
        };
        window.location.href = str;
    }

    function copyWatchWord(str) {
        var save = function (e){
            e.clipboardData.setData('text/plain',str);//下面会说到clipboardData对象
            e.preventDefault();//阻止默认行为
        }
        document.addEventListener('copy',save);
        document.execCommand("copy");//使文档处于可编辑状态，否则无效
        Toast('淘宝口令复制成功!',2000);
    }
</script>
</body>
</html>