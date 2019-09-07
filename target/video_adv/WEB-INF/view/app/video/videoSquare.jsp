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
<div class="square-top">
    <ul>
        <li class="square-top-active" onclick="test()">吸粉视频</li>
        <c:choose>
            <c:when test="${not empty stationCopyNo && stationCopyNo != ''}">
                <li onclick="window.location.href='<%=path%>/app/stationmaster/index'">商品视频</li>
            </c:when>
            <c:otherwise>
                <li onclick="window.location.href='<%=path%>/app/video/myVideoList'">我的视频</li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>

<div class="square-search">
    <div class="search d7">
        <form action="<%=path%>/app/video/square">
            <input type="text" placeholder="输入关键字搜索" name="keywords">
            <button type="submit"></button>
        </form>
    </div>
</div>
<div class="video-study">
    <div class="video-img"><img src="<%=path%>/statics/img/test/001.jpg" /> </div>
    <div class="textblock">
        <div class="video-title" style="font-size:20px;">3分钟学会使用视推宝</div>
        <div class="tagblock">
            <span class="video-tag">视推宝</span>
            <span class="video-tag">使用教程</span>
        </div>
    </div>
    <div class="video-button" onclick="window.location.href='https://mp.weixin.qq.com/s/oxbd3X284jm_9UTXUHe-LQ'">
        进入学习
    </div>
</div>

<div class="categorylist">
    <c:forEach var="category" items="${categoryList}">
        <div class="category" onclick="window.location.href='<%=path%>/app/video/videoSquare-category?categoryId=${category.id}'">
            <div class="category-cont">
                <div class="category-img">
                    <img src="<%=path%>${category.img}" />
                </div>
                <div class="category-text">
                    <div class="category-name">${category.name}</div>
                    <div class="category-videoCount">${category.videoCount}个视频</div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>


<div class="container">


    <aside id="fall-box" class="fall-box grid">
        <c:forEach var="video" items="${videoList}" varStatus="status">
        <div class="grid-item item" onclick="window.location.href='<%=path%>/app/video/play?id=${video.id}&index=${status.index}'">
            <div class="card">
                <img class="card__picture" src="${video.coverImg}" alt="">
                <div class="card-infos">
                    <div class="title">${video.title}</div>
                    <div class="videotag"># ${video.tag}</div>
                    <div class="videoBtn">
                        <img src="<%=path%>/statics/img/video/manage/bg_28.png" />
                    </div>
                </div>
            </div>
        </div>
        </c:forEach>
    </aside>
    <a href="javascript:;" class="more-a">
        <img src="<%=path%>/statics/img/video/manage/ic_loading.gif" />
    </a>
</div>

<c:choose>
    <c:when test="${not empty stationCopyNo && stationCopyNo != ''}">
        <div class="stationBottom">
            <ul>
                <li onclick="window.location.href='<%=path%>/app/stationmaster/index'"><div class="picdiv"><img src="<%=path%>/statics/img/stationmaster/bg_23.png" /></div><div class="textdiv">首页</div></li>
                <li onclick="window.location.href='<%=path%>/app/video/square?stationCopyNo=${stationCopyNo}'"><div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_16.png" /></div><div class="textdiv">视频广场</div></li>
                <li onclick="showTypeSelect()"><div class="picdiv"><img src="<%=path%>/statics/img/stationmaster/bg_25.png" /></div><div class="textdiv">发视频</div></li>
                <li onclick="window.location.href='<%=path%>/app/manageCenter'"><div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_17.png" /></div><div class="textdiv">管理中心</div></li>
            </ul>
        </div>
    </c:when>
    <c:otherwise>
        <div class="bottom">
            <ul>
                <li onclick="window.location.href='<%=path%>/app/video/square'"><div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_16.png" /></div><div class="textdiv">视频广场</div></li>
                <li onclick="window.location.href='<%=path%>/app/video/drawVideo'">
                    <div class="publish"><img src="<%=path%>/statics/img/video/manage/bg_19.png" /></div><div class="textdiv" style="margin-top:22px;">发视频</div></li>
                <li onclick="window.location.href='<%=path%>/app/manageCenter'"><div class="picdiv"><img src="<%=path%>/statics/img/video/manage/bg_17.png" /></div><div class="textdiv">管理中心</div></li>
            </ul>
        </div>
    </c:otherwise>
</c:choose>

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

    $(function(){

        /*顶部nav*/
        var swiper = new Swiper('.nav-container', {
            slidesPerView: 'auto',
            paginationClickable: true
        });
        $(".nav-ul .swiper-slide").click(function(){
            $(this).addClass("active-li").siblings().removeClass("active-li");
        });


        /*瀑布流初始化设置*/
        var $grid = $('.grid').masonry({
            itemSelector : '.grid-item',
            gutter:10
        });
        // layout Masonry after each image loads
        $grid.imagesLoaded().done( function() {
            console.log('uuuu===');
            $grid.masonry('layout');
        });
        var pageIndex = 2 ; var dataFall = [];
        var totalItem = 10;
        $(window).scroll(function(){
            $grid.masonry('layout');
            var scrollTop = $(this).scrollTop();var scrollHeight = $(document).height();var windowHeight = $(this).height();
            if(scrollTop + windowHeight == scrollHeight){
                $.ajax({
                    dataType:"json",
                    type:'get',
                    data:{'page':pageIndex, 'limit':totalItem},
                    url:'${contextPath}/app/video/getSquareVideo',
                    success:function(result){
                        dataFall = result.data;
                        setTimeout(function(){
                            appendFall();
                        },500)
                    },
                    error:function(e){
                        console.log('请求失败')
                    }

                })

            }

        })


        function appendFall(){
            $.each(dataFall, function(index ,value) {
                var dataLength = dataFall.length;
                $grid.imagesLoaded().done( function() {
                    $grid.masonry('layout');
                });
                var detailUrl;
                var $griDiv = $('<div class="grid-item item" onclick="window.location.href=\'${contextPath}/app/video/play?id='
                    + value.id + '&index=' + (totalItem * (pageIndex -1) + index) + '\'">');
                var $cardDiv = $('<div class="card">');
                $cardDiv.appendTo($griDiv);
                var $img = $("<img class='card__picture'>");
                $img.attr('src',value.coverImg).appendTo($cardDiv);
                var $infoDiv = $('<div class="card-infos">');
                $infoDiv.appendTo($cardDiv);
                var $titleDiv = $('<div class="title">' + value.title + '</div>');
                $titleDiv.appendTo($infoDiv);
                var tagStr = "";
                if (value.tag != null)
                    tagStr = value.tag
                var $tagDiv = $('<div class="videotag"># ' + tagStr + '</div>');
                $tagDiv.appendTo($infoDiv);
                var $imgDiv = $('<div class="videoBtn"><img src="${contextPath}/statics/img/video/manage/bg_28.png" /></div>');
                $imgDiv.appendTo($infoDiv);

                var $items = $griDiv;
                $items.imagesLoaded().done(function(){
                    $grid.masonry('layout');
                    $grid.append( $items ).masonry('appended', $items);
                })
            });
            pageIndex++;
        }
    })


</script>

</body>
</html>