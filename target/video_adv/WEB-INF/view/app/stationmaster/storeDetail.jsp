<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

    <link href="<%=path%>/statics/css/swiper.min.css" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/statics/css/reset.css" type="text/css" rel="stylesheet"/>
    <link href="<%=path%>/statics/css/find.css" type="text/css" rel="stylesheet"/>

    <style>
        #wraper {
            float: left;
            width: 100%;
        }
    </style>
</head>
<body style="background: #161922;color:#fff;">
<div class="storeDetail">
    <div class="storeTitle">${advStore.storeName}</div>
    <div class="storeTagRow">
        <div class="count">${advStore.viewCount}人来过</div>
        <div class="tag">${storeUserMap.industry}</div>
    </div>
    <div class="storeAddress" onclick="openLocation()">
        <div class="addressImg"><img src="<%=path%>/statics/img/video/manage/map.png"/></div>
        <div class="">${advStore.address}</div>
        <div class="distance">距离您当前的位置<span id="span-distince">0</span>KM</div>
    </div>
    <input type="hidden" id="inp-longitude" value="${advStore.longitude}" />
    <input type="hidden" id="inp-latitude" value="${advStore.latitude}" />
    <input type="hidden" id="inp-address" value="${advStore.address}" />
</div>
<c:forEach var="video" items="${videoStoreList}">
    <div class="video-wrapper">
        <div class="video-top">
            <div class="userHeadImg"><img src="${storeUserMap.headImg}"/></div>
            <div class="userName">${storeUserMap.userName}</div>
            <div class="createdTime"><fmt:formatDate value="${video.createdTime}" pattern="MM-dd"/></div>
            <div class="clear"></div>
        </div>
        <div class="videoTitle">${video.title}</div>
        <div class="video-coverImg" style="position: relative;">
            <img src="${video.coverImg}"/>
            <div class="storeDetailPlay" onclick="window.location.href='<%=path%>/app/store/storeVideoPlay?id=${video.id}'">
                <img src="<%=path%>/statics/img/video/manage/bg_37.png"/>
            </div>
        </div>
        <div class="video-bottom">
            <ul>
                <li>
                    <div class="imgDiv"><img src="<%=path%>/statics/img/video/manage/mark_fill.png"/></div>
                    <div class="textDiv">${video.commentCount}</div>
                </li>
                <li>
                    <div class="imgDiv"><img src="<%=path%>/statics/img/video/manage/forward_fill.png"/></div>
                    <div class="textDiv">${video.forwardCount}</div>
                </li>
            </ul>
        </div>
        <div class="addComment">
            <div class="commentHeadImg"><img src="${userMap.headImg}"/></div>
            <div class="commentHint" onclick="openComment(${video.id})">添加评论...</div>
        </div>
    </div>
    <div class="comment-wrapper">
        <div class="videoIdPosition" style="display: none;">${video.id}</div>
        <div class="commentCount"><span>${video.commentCount}</span>条评论</div>
        <div class="div-comment">
        <c:if test="${video.videoCommentList.size() > 0}">
            <c:forEach var="comment" items="${video.videoCommentList}">
                <div class="comment">
                    <div class="comment-headImg"><img src="${comment.appUser.headImg}"/></div>
                    <div class="comment-middle">
                        <div class="comment-username">${comment.appUser.userName}</div>
                        <div class="comment-createdTime"><fmt:formatDate value="${comment.createdTime}" pattern="MM-dd"/></div>
                        <div class="comment-cont">${comment.commentCont}</div>
                    </div>
                    <div class="comment-right">
                        <div class="like" onclick="commentLike(${comment.id}, this)">
                            <input type="hidden" value="${comment.likeFlag}"/>
                            <c:choose>
                                <c:when test="${comment.likeFlag == 'Y'}">
                                    <img src="<%=path%>/statics/img/stationmaster/like_yes.png"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="<%=path%>/statics/img/stationmaster/like_no.png"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="likeCount">${comment.likeCount}</div>
                    </div>
                </div>
            </c:forEach>
        </c:if>
        </div>
    </div>


</c:forEach>

<input type="hidden" id="inp-appUser-headImg" value="${userMap.headImg}"/>
<input type="hidden" id="inp-appUser-userName" value="${userMap.userName}"/>
<div id="comment-dialog" class="comment-dialog">
    <input type="hidden" name="videoId" id="inp-videoId">
    <div class="comment-btn">
        <div class="left-cancel" onclick="closeComment()">取消</div>
        <div class="right-submit" onclick="saveComment()">发送</div>
    </div>
    <div>
        <textarea id="commentCont" name="commentCont" cols="" rows="5"></textarea>
    </div>
</div>

</body>

<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script src='https://res.wx.qq.com/open/js/jweixin-1.4.0.js' type="text/javascript"></script>
<script src='<%=path%>/statics/js/getDistance.js'></script>
<script type="text/javascript">

    function getUserLocation() {
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
                            $("#span-distince").html(GetDistance(res.latitude, res.longitude, $("#inp-latitude").val(), $("#inp-longitude").val()));
                        }
                    });
                });
            },
            error: function (xhr) {
                console.log("error");
            }
        });
    }

    $(function () {
        getUserLocation();
    })

    function openComment(videoId) {
        $("#inp-videoId").val(videoId);
        $("#comment-dialog").show();
    }

    function closeComment() {
        $("#comment-dialog").hide();
        $("#inp-videoId").val("");
        $("#commentCont").val("");
    }

    function saveComment() {
        var videoId = $("#inp-videoId").val();
        var commentCont = $("#commentCont").val();
        $.ajax({
            url: "${contextPath}/app/comment/saveComment",
            method: "POST",
            data: {"videoId": videoId, "commentCont": commentCont},
            dataType: "json",
            success: function (res) {
                if (res.code == 200) {
                    var str = '<div class="comment"><div class="comment-headImg"><img src="' + $("#inp-appUser-headImg").val() + '" /> </div>';
                    str += '<div class="comment-middle"><div class="comment-username">' + $("#inp-appUser-userName").val() + '</div>';
                    str += '<div class="comment-createdTime">08-02</div><div class="comment-cont">' + commentCont + '</div></div>';
                    str += '<div class="comment-right"><div class="like" onclick="commentLike(' + res.data.id + ', this)">';
                    str += '<input type="hidden" value="N"/><img src="<%=path%>/statics/img/stationmaster/like_no.png"/></div>';
                    str += '<div class="likeCount">0</div></div></div>';
                    $(".videoIdPosition").each(function () {
                        if ($(this).html() == videoId) {
                            $(this).parent().find(".div-comment").eq(0).before(str);
                            var countSpan = $(this).parent().find(".commentCount > span").eq(0);
                            var count = parseInt($(countSpan).html());
                            $(countSpan).html(count + 1);
                        }
                    });
                }

            }
        })
        closeComment();
    }

    function commentLike(commentId, obj) {
        var likeInput = $(obj).find("input")[0];
        var likeFlag = $(likeInput).val();
        if (likeFlag == 'Y') {
            $.ajax({
                url: '${contextPath}/app/comment/cancelLike?commentId=' + commentId,
                dataType: 'json',
                success: function (res) {
                    if (res.code == 200) {
                        var img = $(obj).find("img")[0];
                        $(img).attr("src", '${contextPath}/statics/img/stationmaster/like_no.png');
                        var countDiv = $(obj).parent().find(".likeCount");
                        var likeCount = parseInt($(countDiv).html()) - 1;
                        $(countDiv).html(likeCount);
                        $(likeInput).val("N");
                    }
                }
            })
        } else {
            $.ajax({
                url: '${contextPath}/app/comment/commentLike?commentId=' + commentId,
                dataType: 'json',
                success: function (res) {
                    if (res.code == 200) {
                        var img = $(obj).find("img")[0];
                        $(img).attr("src", '${contextPath}/statics/img/stationmaster/like_yes.png');
                        var countDiv = $(obj).parent().find(".likeCount");
                        var likeCount = parseInt($(countDiv).html()) + 1;
                        $(countDiv).html(likeCount);
                        $(likeInput).val("Y");
                    }
                }
            })
        }
    }

    //打开地图
    function openLocation() {
        var latitude = $("#inp-latitude").val();
        var longitude = $("#inp-longitude").val();
        window.location.href='${contextPath}/app/map/showMap?longitude=' + longitude + '&latitude=' + latitude;
    };

</script>

</body>
</html>