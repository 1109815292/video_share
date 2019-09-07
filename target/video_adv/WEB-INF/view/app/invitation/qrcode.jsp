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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>邀请好友</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link href="<%=path%>/statics/css/swiper.min.css" rel="stylesheet">
    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->


</head>
<body class="bg-invitation">
<c:if test="${needShowReset == 'Y'}">
<div class="invitation-qrcode-deadline">
    <div class="text"> 二维码到期时间：<span id="deadline"><fmt:formatDate value="${inviteQRCodeExpiresIn}" pattern="yyyy-MM-dd" /></span></div>
    <div class="generate" onclick="resetQRCode()">重新生成</div>
</div>
</c:if>

<div id="invitationCont" class="invitation-cont" style="margin-top:70px;">
    <div class="swiper-container">
        <div class="swiper-wrapper">
            <div id="invitation-img0" class="swiper-slide">
                    <img  src="<%=path%>/statics/img/invite/invite01.jpg"/>
                    <div id="qrcode-img0" class="qrcode-img">
                        <img src="data:image/png;base64,${inviteQRCode}">
                    </div>
            </div>
            <div class="swiper-slide">
                <div id="invitation-img1">
                    <img src="<%=path%>/statics/img/invite/invite02.jpg"/>
                    <div id="qrcode-img1" class="qrcode-img">
                        <img src="data:image/png;base64,${inviteQRCode}">
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div id="invitation-img2">
                    <img src="<%=path%>/statics/img/invite/invite03.jpg"/>
                    <div id="qrcode-img2" class="qrcode-img">
                        <img src="data:image/png;base64,${inviteQRCode}">
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div id="invitation-img3">
                    <img src="<%=path%>/statics/img/invite/invite04.jpg"/>
                    <div id="qrcode-img3" class="qrcode-img">
                        <img src="data:image/png;base64,${inviteQRCode}">
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<div class="invitation-btn">
    <ul>
        <li onclick="window.location.href='${contextPath}/app/invitation?tabIndex=0'" id="li-link">专属推广链接</li>
        <li id="li-poster" class="active">专属推广海报</li>
    </ul>
</div>

<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/statics/js/html2canvas.min.js"></script>
<script src='<%=path%>/statics/js/swiper.min.js'></script>
<script>
    var activeIndex = 0;
    $(function () {
        var contWidth = $("#invitationCont").width();
        var contHeight = parseInt((contWidth / 1280) * 2016);
        $("#invitationCont").height(contHeight);
        $(".swiper-container").height(contHeight);

        var mySwiper = new Swiper('.swiper-container', {
            autoplay: false,//可选选项，自动滑动

            on: {
                slideChangeTransitionEnd: function(){
/*                    if (this.activeIndex == 3){
                        alert($("#qrcode-img3").attr("bottom"));
                        $("#qrcode-img3").attr("bottom", "50px");
                        $("#qrcode-img3").attr("right", "50px");
                    }*/
                    activeIndex = this.activeIndex;
                    takeScreenshot();
                },
            },
        })

        takeScreenshot();
    });


    function takeScreenshot() {
        var shareContent = document.getElementById('invitationCont');//需要截图的包裹的（原生的）DOM 对象
        var width = shareContent.offsetWidth; //获取dom 宽度
        var height = shareContent.offsetHeight; //获取dom 高度
        var canvas = document.createElement("canvas"); //创建一个canvas节点
        var scale = 2; //定义任意放大倍数 支持小数
        canvas.width = width * scale; //定义canvas 宽度 * 缩放
        canvas.height = height * scale; //定义canvas高度 *缩放
        canvas.getContext("2d").scale(scale, scale); //获取context,设置scale

        var opts = {
            scale: scale, // 添加的scale 参数
            canvas: canvas, //自定义 canvas
            logging: true, //日志开关
            width: width, //dom 原始宽度
            height: height, //dom 原始高度
            backgroundColor: 'transparent',
        };
        html2canvas(shareContent, opts).then(function (canvas) {
            IMAGE_URL = canvas.toDataURL("image/png");
            $("#invitation-img" + activeIndex).html("<img src='" + IMAGE_URL + "' style='display:block; width:100%;' />");
            $("#qrcode-img" + activeIndex).hide();
        })
    }

    function resetQRCode() {
        $.ajax({
            url: '${contextPath}/app/inv/resetQRCode',
            method: 'get',
            dataType: 'json',
            success:function (res) {
                if (res.state == 1){
                    window.location.reload();
                }
            }
        })
    }


</script>
</body>
</html>