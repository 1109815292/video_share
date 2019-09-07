<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title><%=(String) session.getAttribute("authorName")%></title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
	<input type="hidden" id="appId" value="${obj.appId }">
	<input type="hidden" id="timeStamp" value="${obj.timeStamp }">
	<input type="hidden" id="nonceStr" value="${obj.nonceStr }">
	<input type="hidden" id="package" value="${obj.package }">
	<input type="hidden" id="paySign" value="${obj.paySign }">
	<input type="hidden" id="infoId" value="${infoId }">
	<input type="hidden" id="goodsType" value="${goodsType }">
	<input type="hidden" id="authorId" value="${authorId }">
</body>
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
  		window.onload=function(){
  			onBridgeReady();
  		}
  		
  		function onBridgeReady(){
  		var appId = $("#appId").val();
  		var timeStamp = $("#timeStamp").val();
  		var nonceStr = $("#nonceStr").val();
  		var packageValue = $("#package").val();
  		var paySign = $("#paySign").val();
  		var infoId = $("#infoId").val();
  		var goodsType = $("#goodsType").val();

    WeixinJSBridge.invoke(
        'getBrandWCPayRequest', {
            "appId" : appId,     //公众号名称，由商户传入     
            "timeStamp" : timeStamp,         //时间戳，自1970年以来的秒数     
            "nonceStr" : nonceStr, //随机串     
            "package" : packageValue,     
            "signType" : "MD5",         //微信签名方式：     
            "paySign" : paySign //微信签名 
        },
        function(res){   
            if(res.err_msg == "get_brand_wcpay_request:ok" ) {
            	if (goodsType == 1 || goodsType == 8){
            		window.location.href = "info-list.action";
            	}
            	if (goodsType == 3){
            		window.location.href = "user-profile.action";
            	}
            	
            }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
            if(res.err_msg == "get_brand_wcpay_request:fail"){
            	window.location.href = "pay-error.action";
            }
            if(res.err_msg == "get_brand_wcpay_request:cancel"){
            	window.location.href = "pay-cancel.action";
            }
        }
    ); 
 }
if (typeof WeixinJSBridge == "undefined"){
    if( document.addEventListener ){
        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
    }else if (document.attachEvent){
        document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
    }
 }else{
    onBridgeReady();
 } 
  </script>
</html>
