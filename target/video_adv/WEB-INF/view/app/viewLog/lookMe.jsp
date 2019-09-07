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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>谁看了我</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp" />

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" href="<%=path%>/statics/css/videostyle.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/stationmaster.css">
    <link rel="stylesheet" href="<%=path%>/statics/search/style.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">


</head>
<body class="bg-square">

    <div class="lookMe-top">
        <div class="manageCenter-headImg">
            <img src="${headImg}" />
        </div>
        <div class="manageCenter-userName">
            ${userName}的主页
        </div>
        <div class="manageCenter-topBox lookMe-border">
            <ul>
                <li><span class="lookMe-count-font">${viewCount}</span><span>访客（人）</span></li>
                <li style="border-left:1px solid #fff;"><span class="lookMe-count-font">${peopleCount}</span><span>访问（次）</span></li>
            </ul>
        </div>
    </div>
    <div class="lookMe-bar">
        <ul id="menubar">
            <li class="selected">视频</li>
            <li>商品</li>
            <li>广告</li>
            <li>实体店</li>
        </ul>
    </div>

    <div class="cont" style="margin-top:0;">
        <div id="div0" class="myVideo-cont"></div>
        <div id="div1" class="" style="display: none;"></div>
        <div id="div2" class="" style="display: none;"></div>
        <div id="div3" class="" style="display: none;"></div>
    </div>

    <c:choose>
        <c:when test="${not empty stationCopyNo && stationCopyNo != ''}">
            <div class="stationBottom">
                <ul>
                    <li onclick="window.location.href='<%=path%>/app/stationmaster/index'"><div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_15.png" /></div><div class="textdiv">首页</div></li>
                    <li onclick="window.location.href='<%=path%>/app/video/square'"><div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_15.png" /></div><div class="textdiv">视频广场</div></li>
                    <li onclick="showTypeSelect()"><div class="picdiv"><img src="<%=path%>/statics/img/stationmaster/bg_25.png" /></div><div class="textdiv">发视频</div></li>
                    <li onclick="window.location.href='<%=path%>/app/manageCenter'"><div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_18.png" /></div><div class="textdiv">管理中心</div></li>
                </ul>
            </div>
        </c:when>
        <c:otherwise>
            <div class="bottom">
                <ul>
                    <li onclick="window.location.href='<%=path%>/app/video/square'">
                        <div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_15.png"/></div>
                        <div class="textdiv">视频广场</div>
                    </li>
                    <li onclick="window.location.href='<%=path%>/app/video/drawVideo'">
                        <div class="publish"><img src="<%=path%>/statics/img/video/manage/bg_19.png"/></div>
                        <div class="textdiv" style="margin-top:22px;">发视频</div>
                    </li>
                    <li onclick="window.location.href='<%=path%>/app/manageCenter'">
                        <div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_18.png"/></div>
                        <div class="textdiv">管理中心</div>
                    </li>
                </ul>
            </div>
        </c:otherwise>
    </c:choose>

    <div id="video-type-select" class="video-type-select">
        <div class="video-type-btn" onclick="window.location.href='<%=path%>/app/video/drawVideo'">吸粉视频</div>
        <div class="video-type-btn" onclick="window.location.href='<%=path%>/app/stationmaster/storeVideoEdit'">商品视频</div>
    </div>

    <div class="noVipCover" id="noVipCover">
        <input type="hidden" id="vipFlag" value="${vipFlag}" />
        <div class="coverImg"><img src="<%=path%>/statics/img/video/manage/information.png" /></div>
        <div class="hintText">
            <div class="">您还未开通VIP会员</div>
            <div class="">开通VIP后查看详细数据</div>
        </div>
        <div class="openVip" onclick="window.location.href='<%=path%>/app/advPay/openVip'">立即开通</div>
    </div>

    <script src='<%=path%>/statics/js/jquery.min.js'></script>
    <script src='<%=path%>/statics/js/toast.js'></script>
<script>

    $(function(){
        if ($("#vipFlag").val() != 'Y'){
            $("#noVipCover").attr("height", window.innerHeight);
            $("#noVipCover").show();
            return;
        }

        $("#menubar li").click(function() {
            $(this).siblings('li').removeClass('selected');
            $(this).addClass('selected');
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

        $.ajax({
            url: '${contextPath}/app/lookMe/videoData',
            type: "get",
            dataType: "json",
            success: function(res) {
                var list = res.data;
                var str = "";
                $("#div0").html("");
                for (var i=0;i<list.length;i++){
                    str += '<div class="myVideo" onclick="window.location.href=\'${contextPath}/app/lookMe/videoViewLog?id=' + list[i].id + '\'"><div class="video-img"><img src="' + list[i].coverImg + '" /> </div>';
                    str += '<div class="textblock"><div class="video-title">' + list[i].title + '</div>';
                    da = new Date(list[i].createdTime);
                    var month = da.getMonth()+1+'月';
                    var date = da.getDate()+'日';
                    str += '<div class="tagblock"><span class="myVideo-date">' + [month,date].join('-') + '</span>';
                    if (list[i].tag !== null && list[i].tag !== '' && list[i].tag !== undefined){
                        str += '<span class="video-tag" style="float:right;margin-right:30px;">' + list[i].tag + '</span>';
                    }
                    str += '</div></div><div class="myVideo-bottom">';
                    str += '<ul><li><img src="${contextPath}/statics/img/video/manage/bg_33.png" /><span>' + list[i].viewCount + '</span></li>';
                    str += '<li><img src="${contextPath}/statics/img/video/lookMe/bg_01.png" /><span>' + list[i].forwardCount + '</span></li>';
                    str += '<li><img src="${contextPath}/statics/img/video/manage/bg_34.png" /><span>' + list[i].peopleCount + '</span></li></ul>';
                    str += '</div></div>';
                }
                $("#div0").append(str);
            }
        });
    });



    function getAdvData(advType, divIndex){
        $.ajax({
            url: '${contextPath}/app/lookMe/advData?advType=' + advType,
            type: "get",
            dataType: "json",
            success: function(res) {
                var list = res.data;
                var str = "";
                $("#div" + divIndex).html("");
                if (advType == 1){
                for (var i=0;i<list.length;i++){
                    str += '<div class="cont-block four-radius" onclick="window.location.href=\'${contextPath}/app/lookMe/productViewLog?id=' + list[i].id + '\'"><div class="block-top margin-top-20"><div class="top-coverImg img-square"><img src="' + list[i].picUrl + '" /> </div>';
                    str += '<div class="top-name"><div class="title">' + list[i].productName + '</div>';
                    str += '<div class="price">' + list[i].price + '</div>';
                    str += '</div></div><div class="block-bottom">';
                    str += '<ul><li style="width:50%;margin-top:5px;"><img src="${contextPath}/statics/img/video/manage/bg_33.png" /><span>' + list[i].viewCount + '</span></li>';
                    str += '<li style="width:50%;margin-top:5px;"><img src="${contextPath}/statics/img/video/manage/bg_34.png" /><span>' + list[i].peopleCount + '</span></li></ul>';
                    str += '</div></div>';
                }
                }else if (advType == 2){
                    for (var i=0;i<list.length;i++){
                        str += '<div class="cont-block four-radius" onclick="window.location.href=\'${contextPath}/app/lookMe/storeViewLog?id=' + list[i].id + '\'"><div class="block-top margin-top-20"><div class="top-coverImg img-square"><img src="' + list[i].picUrl + '" /> </div>';
                        str += '<div class="top-name"><div class="title">' + list[i].storeName + '</div>';
                        str += '<div class="desc">' + list[i].storeDesc + '</div>';
                        str += '</div></div><div class="block-bottom">';
                        str += '<ul><li style="width:50%;margin-top:5px;"><img src="${contextPath}/statics/img/video/manage/bg_33.png" /><span>' + list[i].viewCount + '</span></li>';
                        str += '<li style="width:50%;margin-top:5px;"><img src="${contextPath}/statics/img/video/manage/bg_34.png" /><span>' + list[i].peopleCount + '</span></li></ul>';
                        str += '</div></div>';
                    }
                }else if (advType == 3){
                    for (var i=0;i<list.length;i++){
                        str += '<div class="cont-block four-radius" onclick="window.location.href=\'${contextPath}/app/lookMe/advViewLog?id=' + list[i].id + '\'"><div class="block-top margin-top-20"><div class="top-coverImg img-square"><img src="' + list[i].picUrl + '" /> </div>';
                        str += '<div class="top-name"><div class="title">' + list[i].name + '</div>';
                        str += '<div class="desc">' + list[i].desc + '</div>';
                        str += '</div></div><div class="block-bottom">';
                        str += '<ul><li style="width:50%;margin-top:5px;"><img src="${contextPath}/statics/img/video/manage/bg_33.png" /><span>' + list[i].viewCount + '</span></li>';
                        str += '<li style="width:50%;margin-top:5px;"><img src="${contextPath}/statics/img/video/manage/bg_34.png" /><span>' + list[i].peopleCount + '</span></li></ul>';
                        str += '</div></div>';
                    }
                }
                $("#div" + divIndex).append(str);
            }
        });
    }
</script>
</body>
</html>