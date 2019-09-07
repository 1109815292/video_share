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
    
    <title>百度地图</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />

  </head>


  <body style="margin:0;">

  <div class="contact-map-wrapper">
	  <div id="allmap" class="contact-map"></div>
  </div>

  <input type="hidden" id="inp-longitude" value="${longitude}" />
  <input type="hidden" id="inp-latitude" value="${latitude}" />

  <script src="<%=path%>/statics/js/jquery.min.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=cvBBimq5SKq85gBAmKzTCTkn9EbvzB93"></script>
	<script>
		$(function () {
            var h = parseInt(window.innerHeight + 0)
            if ($(".contact-map-wrapper").height() < h){
                $(".contact-map-wrapper").css("min-height", h + "px");
                $(".contact-map").css("min-height", h + "px");
            }
			var lat = $("#inp-latitude").val();
            var lon = $("#inp-longitude").val();

            var map = new BMap.Map("allmap");
            var point = new BMap.Point(lon,lat);
            map.centerAndZoom(point, 15);
            var marker = new BMap.Marker(point);  // 创建标注
            map.addOverlay(marker);               // 将标注添加到地图中
            marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画


        })
	</script>

  </body>
  

</html>
