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

</div>


<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script type="text/javascript" src="<%=path%>/statics/waterfall2/dist/sortable.js"></script>

<script src="<%=path%>/statics/js/imagesloaded.pkgd.min.js"></script>
<script src="<%=path%>/statics/js/masonry.pkgd.min.js"></script>
<script src="<%=path%>/statics/js/swiper.min.js"></script>

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

        if ($(".container").height() < window.innerHeight){
            $(".container").height(window.innerHeight + "px");
        }


    })
</script>

</body>
</html>