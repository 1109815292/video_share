<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>非店铺会员</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" type="text/css" href="css/info-style.css">
	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
	  <style>
		  .notice-nonews{width:100%;line-height:40px;text-align: center;margin-top:160px;font-size:18px;}
		  .notice-btn{width:100%;text-align: center;margin-top:100px;}
		  .notice-btn button{width:100px;}
	  </style>
  </head>
  
  <body style="background:#e0e0e0;">

	<div class="notice-nonews">
  		<p>非实体店会员不能发店铺视频！</p>
		<p>要开通请联系站长！</p>
  	</div>
    <div class="notice-btn">
    	<button class="btn btn-default btn-sm" onclick="window.location.href='${contextPath}/app/stationmaster/index'">返 回</button>
    </div>    
	

  </body>
  

</html>
