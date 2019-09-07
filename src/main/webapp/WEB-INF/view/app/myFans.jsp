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
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>我的粉丝</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=no, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=path%>/statics/search/style.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <link href="<%=path%>/statics/layui/css/layui.css" rel="stylesheet">

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->


</head>
<body class="bg-square">

<div class="fansTop">
    <ul>
        <li><span class="fansTop-amount">${allCount}</span><span>粉丝总数</span></li>
        <li><span class="fansTop-amount">${myFansCount}</span><span>我的粉丝</span></li>
        <li><span class="fansTop-amount">${fansOfFansCount}</span><span>好友粉丝</span></li>
    </ul>
</div>


<div class="square-search fansSearch">
    <div class="search d7">
        <form action="<%=path%>/app/myFans" method="get">
            <input type="text" name="search" placeholder="输入关键字搜索">
            <button type="submit"></button>
        </form>
    </div>
</div>

<%--        <div class="search-check">
            <form class="layui-form" style="width:360px;">
            <input type="checkbox" name="like1[write]" lay-skin="primary" title="有二维码用户" checked="">
            <input type="checkbox" name="like1[read]" lay-skin="primary" title="VIP用户" checked="">
            <input type="checkbox" name="like1[game]" lay-skin="primary" title="已关注公众号" checked="">
            </form>
        </div>--%>


<div class="fansBar">
    <ul id="menubar">
        <li class="selected">全部</li>
        <li>我的粉丝</li>
        <li>好友粉丝</li>
    </ul>
</div>

<div class="cont">
    <div id="div0">
        <c:forEach var="fans" items="${myFansList}">
            <div class="fans-div">
                <div class="customerImg">
                    <img src="${fans.headImg}"/>
                    <c:if test="${fans.vipFlag == 'Y'}">
                        <div class="vipFlag">VIP</div>
                    </c:if>
                </div>
                <div class="customer-user">
                    <div class="userName">${fans.userName}</div>
                    <div class="lastlogin">最近活跃：${fans.lastViewTime}</div>
                </div>
                <div class="qrcode">
                    <c:if test="${not empty fans.userPage.wxQRCode}">
                        <img src="<%=path%>/statics/img/video/manage/bg_27.png" onclick="showQrcode('${fans.userPage.wxQRCode}')"/>
                    </c:if>
                </div>
                <div class="fans-no">
                    <ul>
                        <li>视推号：${fans.copyNo}</li>
                        <li>顾问号：${fans.inviteCopyNo}</li>
                    </ul>
                </div>
                <c:if test="${fans.vipFlag == 'Y'}">
                    <div class="fans-vip">
                        <ul>
                            <li>
                                <div>${fans.fansCount}</div>
                                <div>TA的粉丝</div>
                            </li>
                            <li>
                                <div>${fans.fansOfFansCount}</div>
                                <div>TA好友的粉丝</div>
                            </li>
                            <li>
                                <div>${fans.fansVipCount}</div>
                                <div>VIP数</div>
                            </li>
                            <li>
                                <div>${fans.income}</div>
                                <div>收入总额</div>
                            </li>
                        </ul>
                    </div>
                </c:if>
            </div>
        </c:forEach>
        <c:forEach var="fans" items="${fansOfFansList}">
            <div class="fans-div">
                <div class="customerImg">
                    <img src="${fans.headImg}"/>
                    <c:if test="${fans.vipFlag == 'Y'}">
                        <div class="vipFlag">VIP</div>
                    </c:if>
                </div>
                <div class="customer-user">
                    <div class="userName">${fans.userName}</div>
                    <div class="lastlogin">最近活跃：${fans.lastViewTime}</div>
                </div>
                <div class="qrcode">
                    <c:if test="${not empty fans.userPage.wxQRCode}">
                        <img src="<%=path%>/statics/img/video/manage/bg_27.png" onclick="showQrcode('${fans.userPage.wxQRCode}')"/>
                    </c:if>
                </div>
                <div class="fans-no">
                    <ul>
                        <li>视推号：${fans.copyNo}</li>
                        <li>顾问号：${fans.inviteCopyNo}</li>
                    </ul>
                </div>
                <c:if test="${fans.vipFlag == 'Y'}">
                    <div class="fans-vip">
                        <ul>
                            <li>
                                <div>${fans.fansCount}</div>
                                <div>TA的粉丝</div>
                            </li>
                            <li>
                                <div>${fans.fansOfFansCount}</div>
                                <div>TA好友的粉丝</div>
                            </li>
                            <li>
                                <div>${fans.fansVipCount}</div>
                                <div>VIP数</div>
                            </li>
                            <li>
                                <div>${fans.income}</div>
                                <div>收入总额</div>
                            </li>
                        </ul>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </div>
    <div id="div1" style="display: none;">
        <c:forEach var="fans" items="${myFansList}">
            <div class="fans-div">
                <div class="customerImg">
                    <img src="${fans.headImg}"/>
                    <c:if test="${fans.vipFlag == 'Y'}">
                        <div class="vipFlag">VIP</div>
                    </c:if>
                </div>
                <div class="customer-user">
                    <div class="userName">${fans.userName}</div>
                    <div class="lastlogin">最近活跃：${fans.lastViewTime}</div>
                </div>
                <div class="qrcode">
                    <c:if test="${not empty fans.userPage.wxQRCode}">
                        <img src="<%=path%>/statics/img/video/manage/bg_27.png" onclick="showQrcode('${fans.userPage.wxQRCode}')"/>
                    </c:if>
                </div>
                <div class="fans-no">
                    <ul>
                        <li>视推号：${fans.copyNo}</li>
                        <li>顾问号：${fans.inviteCopyNo}</li>
                    </ul>
                </div>
                <c:if test="${fans.vipFlag == 'Y'}">
                    <div class="fans-vip">
                        <ul>
                            <li>
                                <div>${fans.fansCount}</div>
                                <div>TA的粉丝</div>
                            </li>
                            <li>
                                <div>${fans.fansOfFansCount}</div>
                                <div>TA好友的粉丝</div>
                            </li>
                            <li>
                                <div>${fans.fansVipCount}</div>
                                <div>VIP数</div>
                            </li>
                            <li>
                                <div>${fans.income}</div>
                                <div>收入总额</div>
                            </li>
                        </ul>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </div>
    <div id="div2" style="display: none;">
        <c:forEach var="fans" items="${fansOfFansList}">
            <div class="fans-div">
                <div class="customerImg">
                    <img src="${fans.headImg}"/>
                    <c:if test="${fans.vipFlag == 'Y'}">
                        <div class="vipFlag">VIP</div>
                    </c:if>
                </div>
                <div class="customer-user">
                    <div class="userName">${fans.userName}</div>
                    <div class="lastlogin">最近活跃：${fans.lastViewTime}</div>
                </div>
                <div class="qrcode">
                    <c:if test="${not empty fans.userPage.wxQRCode}">
                        <img src="<%=path%>/statics/img/video/manage/bg_27.png" onclick="showQrcode('${fans.userPage.wxQRCode}')"/>
                    </c:if>
                </div>
                <div class="fans-no">
                    <ul>
                        <li>视推号：${fans.copyNo}</li>
                        <li>顾问号：${fans.inviteCopyNo}</li>
                    </ul>
                </div>
                <c:if test="${fans.vipFlag == 'Y'}">
                    <div class="fans-vip">
                        <ul>
                            <li>
                                <div>${fans.fansCount}</div>
                                <div>TA的粉丝</div>
                            </li>
                            <li>
                                <div>${fans.fansOfFansCount}</div>
                                <div>TA好友的粉丝</div>
                            </li>
                            <li>
                                <div>${fans.fansVipCount}</div>
                                <div>VIP数</div>
                            </li>
                            <li>
                                <div>${fans.income}</div>
                                <div>收入总额</div>
                            </li>
                        </ul>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </div>
</div>

<div class="back" onclick="javascript:history.back(-1);">
    返回
</div>

<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script src='<%=path%>/statics/layui/layui.all.js'></script>
<script>
    $(function () {
        $("#menubar li").click(function () {
            $(this).siblings('li').removeClass('selected');
            $(this).addClass('selected');
            for (var i = 0; i < 4; i++) {
                if ($(this).index() == i) {
                    $("#div" + i).show();
                } else {
                    $("#div" + i).hide();
                }
            }
        });
    });

    function showQrcode(qrcode) {
        layer.open({
            type: 1
            ,title: false //不显示标题栏
            ,closeBtn: false
            ,area: '300px;'
            ,shade: 0.8
            ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
            ,btn: ['关闭']
            ,btnAlign: 'c'
            ,moveType: 1 //拖拽模式，0或者1
            ,content: '<div style="padding: 30px; line-height: 22px; background-color: #393D49; color: #fff; font-size:16px; font-weight: 300;"><img src="' + qrcode + '"/></div>'
            ,success: function(layero){
            }
        });
    }
</script>
</body>
</html>