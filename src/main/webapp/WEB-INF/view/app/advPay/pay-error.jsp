<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title><%=(String) session.getAttribute("authorName")%></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" type="text/css" href="css/info-style.css">
	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">

  </head>
  
  <body style="background:#e0e0e0;">

	<div class="notice-nonews">
  		<p>支付失败！</p>
  	</div>
    <div class="notice-btn">
    	<button class="btn btn-default btn-sm" onclick="window.location.href='info-list.action'">返 回</button>
    </div>    
	

  </body>
  

</html>
