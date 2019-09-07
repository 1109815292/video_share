<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
    <title>开通VIP服务</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="<%=path%>/statics/css/videostyle.css">
    <link rel="stylesheet" href="<%=path%>/statics/layui/css/layui.css">


</head>
<body style="background: #eee;">
<form class="layui-form" action="">
    <div class="cont">
        <table class="vipAuth" cellpadding="0" cellspacing="0">
            <thead>
            <th style="width:34%">功能</th>
            <th style="width:33%">免费用户</th>
            <th style="width:33%;color:#ff6b01;">付费用户</th>
            </thead>
            <tbody>
            <tr>
                <td>视频广告植入</td>
                <td>8条</td>
                <td style="color:#ff6b01;">不限</td>
            </tr>
            <tr>
                <td>视频主页广告</td>
                <td><img src="<%=path%>/statics/img/video/manage/bg_35.png"/></td>
                <td><img src="<%=path%>/statics/img/video/manage/bg_36.png"/></td>
            </tr>
            <tr>
                <td>谁看过我</td>
                <td><img src="<%=path%>/statics/img/video/manage/bg_35.png"/></td>
                <td><img src="<%=path%>/statics/img/video/manage/bg_36.png"/></td>
            </tr>
            <tr>
                <td>推广数据统计</td>
                <td><img src="<%=path%>/statics/img/video/manage/bg_35.png"/></td>
                <td><img src="<%=path%>/statics/img/video/manage/bg_36.png"/></td>
            </tr>
            <tr>
                <td>客户追踪雷达</td>
                <td><img src="<%=path%>/statics/img/video/manage/bg_35.png"/></td>
                <td><img src="<%=path%>/statics/img/video/manage/bg_36.png"/></td>
            </tr>
            <tr>
                <td>我的客户数据</td>
                <td><img src="<%=path%>/statics/img/video/manage/bg_35.png"/></td>
                <td><img src="<%=path%>/statics/img/video/manage/bg_36.png"/></td>
            </tr>
            </tbody>
        </table>
        <div class="vipTextBar">高级会员套餐</div>
        <div class="vipType" style="font-size:18px;" id="div-vipType">
            <ul id="menubar">
                <c:forEach var="vipTypeSetting" items="${vipTypeSettingList}">
                    <c:choose>
                        <c:when test="${vipTypeSetting.defaultType == 'Y'}">
                            <li class="color-red">
                                <input type="radio" checked name="vipType" value="${vipTypeSetting.id}"
                                       title="${vipTypeSetting.title}(原价${vipTypeSetting.originalPrice / 100}元) ">
                                <c:if test="${not empty vipTypeSetting.tips}">
                                    <span>${vipTypeSetting.tips}</span>
                                </c:if>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li>
                                <input type="radio" name="vipType" value="${vipTypeSetting.id}"
                                       title="${vipTypeSetting.title}(原价${vipTypeSetting.originalPrice / 100}元)">
                                <c:if test="${not empty vipTypeSetting.tips}">
                                    <span>${vipTypeSetting.tips}</span>
                                </c:if>
                            </li>
                        </c:otherwise>
                    </c:choose>

                </c:forEach>
            </ul>
        </div>

    </div>
    <div class="saveBtn">
        <button type="button" id="btn-save" onclick="wxPay()">微信支付</button>
    </div>
</form>

<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script src="<%=path%>/statics/layui/layui.all.js" charset="utf-8"></script>
<script>
    var appId;
    var timeStamp;
    var nonceStr;
    var packageValue;
    var signType;
    var paySign;

    $(function(){
        $("#menubar li").click(function() {
            $(this).siblings('li').removeClass('color-red');
            $(this).addClass('color-red');
        });
    });

    function wxPay() {
        var vipTypeId = $('#div-vipType input[name="vipType"]:checked ').val();
        $.ajax({
            url: "${contextPath}/app/advPay/wxPay",
            method: 'post',
            data: {'vipTypeId': vipTypeId},
            dataType: 'json',
            success: function (res) {
                appId = res.data.appId;
                timeStamp = res.data.timeStamp;
                nonceStr = res.data.nonceStr;
                packageValue = res.data.package;
                signType = res.data.signType;
                paySign = res.data.paySign;
                onBridgeReady(packageValue);
            }
        });
    }

    function onBridgeReady(packageValue) {
        var prepayId = packageValue.split("=")[1];
        WeixinJSBridge.invoke(
            'getBrandWCPayRequest', {
                "appId": appId,     //公众号名称，由商户传入
                "timeStamp": timeStamp,         //时间戳，自1970年以来的秒数
                "nonceStr": nonceStr, //随机串
                "package": packageValue,
                "signType": signType,         //微信签名方式：
                "paySign": paySign //微信签名
            },
            function (res) {
                if (res.err_msg === "get_brand_wcpay_request:ok") {
                    window.location.href = "success?prepayId=" + prepayId;
                }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                if (res.err_msg === "get_brand_wcpay_request:fail") {
                    window.location.href = "error";
                }
                if (res.err_msg === "get_brand_wcpay_request:cancel") {
                    window.location.href = "cancel?prepayId=" + prepayId;
                }
            }
        );
    }

</script>

</body>
</html>