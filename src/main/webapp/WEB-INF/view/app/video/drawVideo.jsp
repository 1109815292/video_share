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
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>视频广场</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp" />

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />

    <link href="https://vjs.zencdn.net/7.5.5/video-js.css" rel="stylesheet">
    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="<%=path%>/statics/css/zdialog.css" />

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->
    <script src="https://vjs.zencdn.net/ie8/1.1.2/videojs-ie8.min.js"></script>

</head>
<body class="bg-square">

<c:choose>
    <c:when test="${not empty sessionScope.session_app_station_copy_no && sessionScope.session_app_station_copy_no != ''}">
        <div class="customer-btn" style="width:100%;height:60px;background: #ddd;margin:0;padding-top:15px;">
            <ul>
                <li class="active">吸粉视频</li>
                <li onclick="window.location.href='<%=path%>/app/stationmaster/storeVideoEdit'">商品视频</li>
            </ul>
        </div>
    </c:when>
    <c:otherwise>
        <div class="top-img">
            <img src="<%=path%>/statics/img/video/square/videoDraw01.jpg" />
        </div>
    </c:otherwise>
</c:choose>

    <form action="<%=path%>/app/video/videoEdit" method="post" onsubmit="return check()">
    <div class="video-main">
        <div class="video-draw-cont">
            <textarea rows="10" id="content" name="content" placeholder="请复制短视频链接或口令，粘贴到这里，无需删除文字。">${content}</textarea>
        </div>
        <input type="hidden" id="code" value="${data.code}" />
        <div class="video-draw-msg" id="msg">${data.msg}</div>
    </div>

    <div class="video-drawbtn">
        <button type="submit">生成短视频链接</button>
    </div>
    </form>

    <div class="video-draw-example">
        <div class="text">获取视频链接步骤</div>
        <div><img src="<%=path%>/statics/img/video/square/videoDraw02.jpg" /> </div>
    </div>

    <script src="<%=path%>/statics/js/jquery.min.js"></script>
    <script src="<%=path%>/statics/js/toast.js"></script>
    <script src='<%=path%>/statics/js/zdialog.js'></script>
    <script>
        $(function () {
            if ($("#code").val() == 300001){
                $("#msg").html("");
                openVip();
            }
        })
        function check() {
            if (isEmpty($("#content").val())){
                Toast('链接不能为空！',2000);
                return false;
            }
            return true;
        }

        function isEmpty(s1)
        {
            var sValue = s1 + "";
            var test = / /g;
            sValue = sValue.replace(test, "");
            return sValue==null || sValue.length<=0;
        }

        function openVip() {
            $.DialogByZ.Confirm({Title: "", Content: "非会员只能生成8条视频！</br>是否立即开通会员？",FunL:confirmOpen,FunR:cancel})
        };
        function confirmOpen(){
            window.location.href = '${contextPath}/app/advPay/openVip';
        }
        function cancel() {
            $.DialogByZ.Close();
        }
    </script>
</body>
</html>