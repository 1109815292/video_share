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
    <title>视频广场</title>

    <link rel="stylesheet" href="<%=path%>/statics/waterfall2/css/main.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/videostyle.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/stationmaster.css">
    <link rel="stylesheet" href="<%=path%>/statics/search/style.css">
    <link rel="stylesheet" href="<%=path%>/statics/layui/css/layui.css">
    <link rel="stylesheet" href="<%=path%>/statics/waterfall2/dist/sortable.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

    <link href="<%=path%>/statics/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=path%>/statics/css/reset.css" type="text/css" rel="stylesheet" />
    <link href="<%=path%>/statics/css/find.css" type="text/css" rel="stylesheet" />

    <style>
        #wraper{float:left;width: 100%;}
    </style>
</head>
<body style="background: #eee;">

<div class="storeSearch">
    <div class="square-search" style="margin-top:0;">
        <div class="search d7">
            <form action="<%=path%>/app/video/square">
                <input type="text" placeholder="输入关键字搜索" name="keywords">
                <button type="submit"></button>
            </form>
        </div>
    </div>
</div>
<form class="layui-form" style="width: 100%;">
<div class="layui-row" style="width: 100%;">

    <div class="layui-col-xs4 layui-col-sm12 layui-col-md4">
        <div class="grid-demo">
            <div class="layui-input-inline">
                <select name="modules" lay-verify="required" lay-search="">
                    <option value="">地址筛选</option>
                </select>
            </div>
        </div>
    </div>
    <div class="layui-col-xs4 layui-col-sm7 layui-col-md8">
        <div class="grid-demo">
            <div class="layui-input-inline">
                <select name="industry" lay-verify="required" lay-search="" lay-filter="industrySelect">
                    <c:forEach var="industry" items="${industryList}">
                        <option value="${industry.id}">${industry.industry}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </div>
    <div class="layui-col-xs4 layui-col-sm5 layui-col-md4">
        <div class="grid-demo">
            <div class="layui-input-inline">
                <select name="modules" lay-verify="required" lay-search="">
                    <option value="">排序筛选</option>
                </select>
            </div>
        </div>
    </div>

</div>
</form>

<div id="storeList-cont" class="storeList-cont">
    <c:forEach var="store" items="${advStoreList}">
    <div class="storeInfo" onclick="window.location.href='<%=path%>/app/stationmaster/storeDetail/${store.id}'">
        <div class="storeInfoImg">
            <img src="${store.picUrl}">
        </div>
        <div class="storeInfoName">
            <div class="storeListName"><img src="<%=path%>/statics/img/promotion/advStore.png" />${store.storeName}</div>
            <div class="storeListDesc">${store.storeDesc}</div>
            <div class="distance">
                <input type="hidden" value="${store.longitude}" />
                <input type="hidden" value="${store.latitude}" />
                <span>0KM</span>
            </div>
        </div>
    </div>
    </c:forEach>
</div>



<div class="stationBottom">
    <ul>
        <li onclick="window.location.href='<%=path%>/app/stationmaster/index'"><div class="picdiv"><img src="<%=path%>/statics/img/stationmaster/bg_24.png" /></div><div class="textdiv">首页</div></li>
        <li onclick="window.location.href='<%=path%>/app/video/square'"><div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_15.png" /></div><div class="textdiv">视频广场</div></li>
        <li onclick="showTypeSelect()"><div class="picdiv"><img src="<%=path%>/statics/img/stationmaster/bg_25.png" /></div><div class="textdiv">发视频</div></li>
        <li onclick="window.location.href='<%=path%>/app/manageCenter'"><div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_17.png" /></div><div class="textdiv">管理中心</div></li>
    </ul>
</div>
<div id="video-type-select" class="video-type-select">
    <div class="video-type-btn" onclick="window.location.href='<%=path%>/app/video/drawVideo'">吸粉视频</div>
    <div class="video-type-btn" onclick="window.location.href='<%=path%>/app/stationmaster/storeVideoEdit'">商品视频</div>
</div>

<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script src="<%=path%>/statics/js/toast.js"></script>
<script src="<%=path%>/statics/layui/layui.all.js"></script>
<script src='https://res.wx.qq.com/open/js/jweixin-1.4.0.js' type="text/javascript"></script>
<script src='<%=path%>/statics/js/getDistance.js'></script>
<script type="text/javascript">

    var userLongitude = 0;
    var userLatitude = 0;
    $(function(){
        if ($("#storeList-cont").height() < window.innerHeight){
            $("#storeList-cont").height(window.innerHeight + "px");
        }

        calcDistance();
    });

    layui.use('form', function(){
        var form = layui.form;

        form.on('select(industrySelect)', function(data){
            $.ajax({
                url: '${contextPath}/app/store/getStoreList',
                data: {'industryId': data.value},
                method: 'get',
                dataType: 'json',
                success: function(res){
                    $("#storeList-cont").empty();
                    var storeList = res.data;
                    var str = '';
                    for (var i=0;i<storeList.length;i++){
                        str += '<div class="storeInfo" onclick="window.location.href=\'${contextPath}/app/stationmaster/storeDetail/' + storeList[i].id + '\'">';
                        str += '<div class="storeInfoImg"><img src="' + storeList[i].picUrl  + '"></div>';
                        str += '<div class="storeInfoName">'
                        str += '<div class="storeListName"><img src="${contextPath}/statics/img/promotion/advStore.png" />' + storeList[i].storeName + '</div>';
                        str += '<div class="storeListDesc">' + storeList[i].storeDesc + '</div>';
                        str += '<div class="distance">0KM</div></div></div>';
                    }
                    $("#storeList-cont").append(str);
                }

            })
        });
    });

    function calcDistance() {
        $.ajax({
            async: false,
            url: '${contextPath}/app/video/getWxConfig',//后台请求接口，找后台要
            data: {url: window.location.href},
            type: "get",
            dataType: "json",
            success: function (data) {
                wx.config({
                    debug: false,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。true为调用开启
                    appId: data.appid,//appid// 必填，公众号的唯一标识
                    timestamp: data.timestamp, // 必填，生成签名的时间戳
                    nonceStr: data.nonceStr,// 必填，生成签名的随机串
                    signature: data.signature,// 必填，签名，
                    jsApiList: ['openLocation', 'getLocation']
                });
                wx.ready(function () {
                    wx.getLocation({
                        type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                        success: function (res) {
                            userLongitude = res.longitude;
                            userLatitude = res.latitude;

                            $(".distance").each(function () {
                                var inputs = $(this).find("input");
                                var storeLongitude = $(inputs.eq(0)).val();
                                var storeLatitude = $(inputs.eq(1)).val();
                                var storeDistance = GetDistance(userLongitude, userLatitude, storeLongitude, storeLatitude);
                                var distanceStr;
                                if (storeDistance < 1){
                                    distanceStr = parseInt(storeDistance * 1000) + "M";
                                }else{
                                    distanceStr = storeDistance + "KM";
                                }
                                var distanceSpan = $(this).find("span").eq(0);
                                $(distanceSpan).html(distanceStr);
                            })
                        }
                    });
                });
            },
            error: function (xhr) {
                console.log("error");
            }
        });
    }


</script>

</body>
</html>