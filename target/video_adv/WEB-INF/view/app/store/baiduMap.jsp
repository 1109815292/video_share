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
	  <style>
		  .contact-map-wrapper{
			  position: relative;
			  width: 100%;
		  }
		  .contact-map{
			  height: 100%;
			  width: 100%;
		  }
		  .contact-map-wrapper #r-result {
			  position: absolute;
			  top: 0;
			  left: 0;
			  margin: 16px;
			  height: 30px;
			  width: 280px;
		  }
		  .contact-map-wrapper #r-result input{
			  height: 100%;
			  width: 100%;
			  line-height: 30px;
			  background-color: #fff;
			  padding: 0 8px;
			  font-size: 14px;
		  }
		  #r-result input:focus{
			  border: 1px solid #3385ff;
		  }
		  #r-result input::-webkit-input-placeholder{
			  color: #999999;
		  }
		  #r-result input::-moz-placeholder{
			  color: #999999;
		  }
		  #r-result input:-ms-input-placeholder{
			  color: #999999;
		  }
	  </style>
  </head>


  <body style="margin:0;">

  <div class="contact-map-wrapper">
	  <div id="allmap" class="contact-map"></div>
	  <div id="r-result"><input type="text" id="suggestId" size="20" value="百度" placeholder="搜地点、查公交、找路线" /></div>
	  <div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
  </div>


  <script src="<%=path%>/statics/js/jquery.min.js"></script>
  <script src="<%=path%>/statics/layui/layui.all.js"></script>
  <script src="<%=path%>/statics/js/xadmin.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=cvBBimq5SKq85gBAmKzTCTkn9EbvzB93"></script>
	<script>
        var lat = 0;
        var lon = 0;
		$(function () {
            var h = parseInt(window.innerHeight + 0)
            if ($(".contact-map-wrapper").height() < h){
                $(".contact-map-wrapper").css("min-height", h + "px");
                $(".contact-map").css("min-height", h + "px");
            }
        })


        //获取地址信息，设置地址label
        function getAddress(){
            map.centerAndZoom(point,16);
            map.enableScrollWheelZoom(true);
            map.addControl(new BMap.NavigationControl());
            map.addControl(new BMap.GeolocationControl());
            var geoc = new BMap.Geocoder();
            map.addEventListener("click", function(e){
                var pt = e.point;
                geoc.getLocation(pt, function(res){
                    window.parent.setAddress(res.point.lng,res.point.lat,res.address);
                    x_admin_close();
                });
            });

            // 保存 touch 对象信息
            var obj = {};

            map.addEventListener('touchstart', function (e) {
                obj.e = e.changedTouches ? e.changedTouches[0] : e;
                obj.target = e.target;
                obj.time = Date.now();
                obj.X = obj.e.pageX;
                obj.Y = obj.e.pageY;
            });

            map.addEventListener('touchend', function (e) {
                obj.e = e.changedTouches ? e.changedTouches[0] : e;
                if (
                    obj.target === e.target &&
                    // 大于 750 可看成长按了
                    ((Date.now() - obj.time) < 750) &&
                    // 应用勾股定理判断，如果 touchstart 的点到 touchend 的点小于 15，就可当成地图被点击了
                    (Math.sqrt(Math.pow(obj.X - obj.e.pageX, 2) + Math.pow(obj.Y - obj.e.pageY, 2)) < 15)
                ) {
                    // 地图被点击了，执行一些操作
                    var pt = e.point;
                    geoc.getLocation(pt, function(res){
                        window.parent.setAddress(res.point.lng,res.point.lat,res.address);
                        x_admin_close();
                    });
                }
            });
        }
        function getCurrentLocation() {
            var geolocation = new BMap.Geolocation();
            geolocation.enableSDKLocation();
            geolocation.getCurrentPosition(function(r){
                if(this.getStatus() == BMAP_STATUS_SUCCESS){
                    var mk = new BMap.Marker(r.point);
                    map.addOverlay(mk);
                    map.panTo(r.point);
                    lat = r.point.lat;
                    lon = r.point.lng;
                }
            });
        }

        getCurrentLocation();
        var map = new BMap.Map("allmap");
        var point = new BMap.Point(lon,lat);
        getAddress();

        function G(id) { return document.getElementById(id); }
        var ac = new BMap.Autocomplete( //建立一个自动完成的对象
            { "input": "suggestId", "location": map }); ac.addEventListener("onhighlight", function (e) { //鼠标放在下拉列表上的事件
            var str = ""; var _value = e.fromitem.value; var value = "";
            if (e.fromitem.index > -1) { value = _value.province + _value.city + _value.district + _value.street + _value.business; } str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value; value = ""; if (e.toitem.index > -1) { _value = e.toitem.value; value = _value.province + _value.city + _value.district + _value.street + _value.business; } str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value; G("searchResultPanel").innerHTML = str;
        }); var myValue; ac.addEventListener("onconfirm", function (e) { //鼠标点击下拉列表后的事件
            var _value = e.item.value; myValue = _value.province + _value.city + _value.district + _value.street + _value.business; G("searchResultPanel").innerHTML = "onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue; setPlace();
        });
        function setPlace() {
            map.clearOverlays(); //清除地图上所有覆盖物
            function myFun() {
                var pp = local.getResults().getPoi(0).point; //获取第一个智能搜索的结果
                map.centerAndZoom(pp, 18); map.addOverlay(new BMap.Marker(pp)); //添加标注
            } var local = new BMap.LocalSearch(map, { //智能搜索
                onSearchComplete: myFun
            }); local.search(myValue);
        }




	</script>

  </body>
  

</html>
