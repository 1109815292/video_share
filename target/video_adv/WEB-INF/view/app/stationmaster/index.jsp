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
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <title>视频广场</title>

    <link rel="stylesheet" href="<%=path%>/statics/waterfall2/css/main.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/videostyle.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/stationmaster.css">
    <link rel="stylesheet" href="<%=path%>/statics/search/style.css">
    <link rel="stylesheet" href="<%=path%>/statics/waterfall2/dist/sortable.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

    <link href="<%=path%>/statics/css/reset.css" type="text/css" rel="stylesheet"/>
    <link href="<%=path%>/statics/css/find.css" type="text/css" rel="stylesheet"/>

    <style>
        #wraper {
            float: left;
            width: 100%;
        }
    </style>
</head>
<body style="background: #eee;">

<div class="stationTop">
    <div class="stationDistrict">${districtStr}</div>
    <div class="stationSearch">
        <div class="search d7">
            <form action="<%=path%>/app/video/square">
                <input type="text" placeholder="输入关键字搜索" name="keywords" style="width:90%;">
                <button type="submit" style="float:left;margin:-3px 0 0 -36px;"></button>
            </form>
        </div>
    </div>
</div>
<div class="channel">
    <ul>
        <c:forEach var="channel" items="${channelIndustryList}">
            <li onclick="window.location.href='<%=path%>/app/stationmaster/storeList?industryId=${channel.id}'">
                <div class="channel-icon"><img src="<%=path%>${channel.imgUrl}"/></div>
                <div class="channel-text">${channel.industry}</div>
            </li>
        </c:forEach>
    </ul>
</div>

<div class="videoType">
    <ul id="menubar">
        <li class="typeSelected"><input type="hidden" value="0"><span>推荐</span></li>
        <c:forEach var="label" items="${labelIndustryList}">
            <li><input type="hidden" value="${label.id}"><span>${label.industry}</span></li>
        </c:forEach>
    </ul>
</div>

<div class="container">
    <aside id="fall-box" class="fall-box grid" style="margin-top:8px;">
        <c:forEach var="video" items="${videoList}">
            <div class="grid-item item"
                 onclick="window.location.href='<%=path%>/app/store/storeVideoPlay?id=${video.id}'">
                <div class="card">
                    <div class="card__picture">
                        <img class="" src="${video.coverImg}" alt="">
                        <div class="video-industry">${video.industry.industry}</div>
                    </div>
                    <div class="card-infos">
                        <div class="title">${video.title}</div>
                        <div class="video-user">
                            <div class="video-user-headImg"><img src="${video.appUser.headImg}"/></div>
                            <div class="video-user-userName">${video.appUser.userName}</div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </aside>
    <a href="javascript:;" class="more-a">
        <img src="<%=path%>/statics/img/video/manage/ic_loading.gif"/>
    </a>
</div>

<div class="stationBottom">
    <ul>
        <li>
            <div class="picdiv"><img src="<%=path%>/statics/img/stationmaster/bg_24.png"/></div>
            <div class="textdiv">首页</div>
        </li>
        <li onclick="window.location.href='<%=path%>/app/video/square'">
            <div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_15.png"/></div>
            <div class="textdiv">视频广场</div>
        </li>
        <li onclick="showTypeSelect()">
            <div class="picdiv"><img src="<%=path%>/statics/img/stationmaster/bg_25.png"/></div>
            <div class="textdiv">发视频</div>
        </li>
        <li onclick="window.location.href='<%=path%>/app/manageCenter'">
            <div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_17.png"/></div>
            <div class="textdiv">管理中心</div>
        </li>
    </ul>
</div>
<div id="video-type-select" class="video-type-select">
    <div class="video-type-btn" onclick="window.location.href='<%=path%>/app/video/drawVideo'">吸粉视频</div>
    <div class="video-type-btn" onclick="window.location.href='<%=path%>/app/stationmaster/storeVideoEdit'">商品视频</div>
</div>

<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script type="text/javascript" src="<%=path%>/statics/waterfall2/dist/sortable.js"></script>

<script src="<%=path%>/statics/js/imagesloaded.pkgd.min.js"></script>
<script src="<%=path%>/statics/js/masonry.pkgd.min.js"></script>
<script src="<%=path%>/statics/js/swiper.min.js"></script>
<script src="<%=path%>/statics/js/toast.js"></script>
<script type="text/javascript">
    var pageIndex = 2;
    var dataFall = [];
    var totalItem = 10;
    var industryId = 0;
    var $grid;
    $(function () {
        $("#menubar li").click(function () {
            $(this).siblings('li').removeClass('typeSelected');
            $(this).addClass('typeSelected');
            for (var i = 0; i < 4; i++) {
                if ($(this).index() == i) {
                    $("#div" + i).show();
                } else {
                    $("#div" + i).hide();
                }
            }
            var oInput = $(this).find("input")[0];
            industryId = $(oInput).val();
            pageIndex = 1;
            $grid.masonry('destroy').empty();
            resize();
            getMore();
            initMasonry();
        });
        initMasonry();
        resize();
    });

    function resize() {
        var h = parseInt(window.innerHeight + 200)
        if ($(".container").height() < h){
            $(".container").css("min-height", h + "px");
        }
    }

    function initMasonry() {
        /*瀑布流初始化设置*/
        $grid = $('.grid').masonry({
            itemSelector: '.grid-item',
            gutter: 10
        });
        // layout Masonry after each image loads
        $grid.imagesLoaded().done(function () {
            $grid.masonry('layout');
        });
    }


    $(window).scroll(function () {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight == scrollHeight) {
            getMore();
        }
    });


    function getMore() {
        $.ajax({
            dataType: "json",
            type: 'get',
            data: {'page': pageIndex, 'limit': totalItem, 'industryId': industryId},
            url: '${contextPath}/app/store/getStoreVideo',
            success: function (result) {
                dataFall = result.data;
                setTimeout(function () {
                    appendFall();
                }, 500)
            },
            error: function (e) {
                console.log('请求失败')
            }
        });
    }

    function appendFall() {
        $.each(dataFall, function (index, value) {
            var dataLength = dataFall.length;
            $grid.imagesLoaded().done(function () {
                $grid.masonry('layout');
            });
            var detailUrl;
            var $griDiv = $('<div class="grid-item item" onclick="window.location.href=\'${contextPath}/app/store/storeVideoPlay?id=' + value.id + '\'">');
            var $cardDiv = $('<div class="card">');
            $cardDiv.appendTo($griDiv);
            var $pictureDiv = $('<div class="card__picture">');
            var $img = $("<img class='card__picture'>");
            $img.attr('src', value.coverImg).appendTo($pictureDiv);
            var $industryDiv = $('<div class="video-industry">' + value.industry.industry + '</div>');
            $industryDiv.appendTo($pictureDiv);
            $pictureDiv.appendTo($cardDiv);
            var $infoDiv = $('<div class="card-infos">');
            $infoDiv.appendTo($cardDiv);
            var $titleDiv = $('<div class="title">' + value.title + '</div>');
            $titleDiv.appendTo($infoDiv);
            var $userDiv = $('<div class="video-user">');
            var $headImgDiv = $('<div class="video-user-headImg"><img src="' + value.appUser.headImg + '" /></div>');
            $headImgDiv.appendTo($userDiv);
            var $userNameDiv = $('<div class="video-user-userName">' + value.appUser.userName + '</div>');
            $userNameDiv.appendTo($userDiv);
            $userDiv.appendTo($infoDiv);

            var $items = $griDiv;
            $items.imagesLoaded().done(function () {
                $grid.masonry('layout');
                $grid.append($items).masonry('appended', $items);
            })
        });
        pageIndex++;
    };


</script>

</body>
</html>