<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>实体店开通</title>

    <link rel="stylesheet" href="<%=path%>/statics/css/stationmaster.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/videostyle.css">
    <link rel="stylesheet" href="<%=path%>/statics/layui/css/layui.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/zdialog.css">
</head>
<body style="background: #eee;padding:0;margin:0;">

<div class="storeList-cont">
    <c:forEach var="payOrder" items="${payOrderList}">
    <div class="pay-wrapper">
        <div class="pay-top">
            <div class="store-headImg"><img src="${payOrder.storeUser.headImg}" /></div>
            <div class="store-userName">${payOrder.storeUser.userName}</div>
        </div>
        <div class="pay-amount"><span>￥</span>${payOrder.amount}</div>
        <div class="pay-bottom"><div class="pay-time"><fmt:formatDate value="${payOrder.payTime}" pattern="yyyy-MM-dd HH:mm:ss" /></div></div>
    </div>
    </c:forEach>

</div>

<div class="saveBtn">
    <button type="button" id="btn-save" onclick="storeVipPay()">开通实体店</button>
</div>


<div id="payCover" class="payCover">

</div>
<div id="storeVipPay" class="storeVipPay">
    <form class="layui-form">
    <div class="storePay-top">用户激活</div>
    <div class="videoEdit-inp-group" style="margin-top:10px;">
        <div class="label">行业标签</div>
        <div class="videoEdit-cont" style="margin-top:6px;">
            <select name="industryId" id="select-industryId">
                <c:forEach var="industry" items="${industryList}">
                    <option value="${industry.id}">${industry.industry}</option>
                </c:forEach>
            </select>
        </div>
    </div>
        <div class="videoEdit-inp-group" style="margin-top:10px;">
            <div class="label">视推号</div>
            <div class="videoEdit-cont" style="margin-top:6px;">
                    <input id="storeCopyNo" name="storeCopyNo">
            </div>
        </div>
        <div class="storePay-btn" onclick="wxPay()">帮他开通VIP</div>
    </form>
</div>

<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script src='<%=path%>/statics/layui/layui.all.js'></script>
<script src='<%=path%>/statics/js/zdialog.js'></script>
<script type="text/javascript">

    function storeVipPay() {
        window.scrollTo(0,0);
        $("#payCover").height($(document).height() + "px");
        $("#payCover").show();
        $("#storeVipPay").show();
    }

    var appId;
    var timeStamp;
    var nonceStr;
    var packageValue;
    var signType;
    var paySign;

    function wxPay() {
        var storeCopyNo = $("#storeCopyNo").val();
        if (storeCopyNo == ''){
            $.DialogByZ.Alert({Title: "提示", Content: "请输入视推号",BtnL:"确定",FunL:alerts});
            return;
        }

        $.ajax({
            url: "${contextPath}/app/advPay/storeVipPay",
            method: 'post',
            data: {'storeCopyNo': storeCopyNo, 'industryId': $("#select-industryId").val()},
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
                    window.location.href = "${contextPath}/app/stationmaster/storeVipList?prepayId=" + prepayId;
                }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                if (res.err_msg === "get_brand_wcpay_request:fail") {
                    window.location.href = "${contextPath}/app/stationmaster/storeVipList";
                }
                if (res.err_msg === "get_brand_wcpay_request:cancel") {
                    window.location.href = "${contextPath}/app/stationmaster/storeVipList?prepayId=" + prepayId;
                }
            }
        );
    }

    function alerts(){
        $.DialogByZ.Close();
    }
</script>

</body>
</html>