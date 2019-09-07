<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/15 0015
  Time: 15:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>管理中心</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="<%=path%>/statics/css/videostyle.css">
    <link rel="stylesheet" href="<%=path%>/statics/search/style.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/stationmaster.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">


</head>
<body>

<div class="manageCenter-top">
    <c:choose>
        <c:when test="${vipFlag == 'Y'}">
            <div class="manageCenter-vip">
                <div class="manageCenter-vip-headImg">
                    <img src="${headImg}"/>
                </div>
                <div class="vipInfo">
                    <div class="flag">VIP</div>
                    <div class="deadline">到期时间：<fmt:formatDate value="${vipDeadline}" pattern="yyyy-MM-dd" /> </div>
                </div>
                <div class="vipName">
                        ${userName}
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="manageCenter-headImg">
                <img src="${headImg}"/>
            </div>
            <div class="manageCenter-userName">
                    ${userName}
            </div>
        </c:otherwise>
    </c:choose>
    <div class="manageCenter-homePage" onclick="window.location.href='<%=path%>/app/userProfile?copyNo=${copyNo}'">
        我的主页
    </div>
    <div class="manageCenter-copyId">
        <div class="userId">视推号：<span id="copyNo">${copyNo}</span></div>
        <div class="copybtn" onclick="copyCopyNo()"><span class="btn" onclick="" data-clipboard-target="#copyNo"  data-clipboard-action="copy">复制</span></div>
    </div>
    <div class="manageCenter-topBox manageCenter-topBox-border">
        <div class="topBox-label">每单收益自动提现</div>
        <ul>
            <li><span class="manageCenter-amount">${dayIncome}</span><span>今日收益（元）</span></li>
            <li><span class="manageCenter-amount">${allIncome}</span><span>累计收益（元）</span></li>
        </ul>
    </div>
</div>

<div class="manageCenter-cont">
    <ul>
        <li onclick="window.location.href='<%=path%>/app/video/myVideoList'">
            <img src="<%=path%>/statics/img/video/manage/bg_07.png"/>
            <span>我的作品</span>
        </li>
        <li onclick="window.location.href='<%=path%>/app/homePageSet'">
            <img src="<%=path%>/statics/img/video/manage/bg_08.png"/>
            <span>设置主页</span>
        </li>
        <li onclick="window.location.href='<%=path%>/app/promotionSet'">
            <div><img src="<%=path%>/statics/img/video/manage/bg_09.png"/></div>
            <span>推广设置</span>
        </li>
        <li onclick="window.location.href='<%=path%>/app/invitation'">
            <div><img src="<%=path%>/statics/img/video/manage/bg_10.png"/></div>
            <span>邀请好友</span>
        </li>
        <li onclick="window.location.href='<%=path%>/app/lookMe'">
            <div><img src="<%=path%>/statics/img/video/manage/bg_11.png"/></div>
            <span>谁看了我</span>
        </li>
        <li onclick="window.location.href='<%=path%>/app/customer/myCustomer'">
            <div><img src="<%=path%>/statics/img/video/manage/bg_12.png"/></div>
            <span>我的客户</span>
        </li>
        <li onclick="window.location.href='<%=path%>/app/income'">
            <div><img src="<%=path%>/statics/img/video/manage/bg_13.png"/></div>
            <span>收入明细</span>
        </li>
        <li onclick="window.location.href='<%=path%>/app/myFans'">
            <div><img src="<%=path%>/statics/img/video/manage/bg_14.png"/></div>
            <span>我的粉丝</span>
        </li>
        <c:if test="${not empty stationFlag && stationFlag == 'Y'}">
        <li onclick="window.location.href='<%=path%>/app/stationmaster/storeVipList'">
            <div><img src="<%=path%>/statics/img/stationmaster/bg_28.png"/></div>
            <span>实体店开通</span>
        </li>
        <li onclick="window.location.href='<%=path%>/app/stationmaster/storeVipList'">
            <div><img src="<%=path%>/statics/img/stationmaster/bg_29.png"/></div>
            <span>已开通实体店</span>
        </li>
        <li onclick="window.location.href='<%=path%>/app/stationmaster/stationSet'">
            <div><img src="<%=path%>/statics/img/stationmaster/bg_30.png"/></div>
            <span>站点设置</span>
        </li>
        </c:if>
    </ul>

    <div class="recommendMan">推荐人视推号：${inviteCopyNo}</div>

    <c:if test="${vipFlag ne 'Y'}">
        <div class="openVipBtn">
            <div class="btnDiv" onclick="window.location.href='<%=path%>/app/advPay/openVip'">
                开通VIP
            </div>
        </div>
    </c:if>
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
<input type="hidden" id="inp-copyNo" value="${copyNo}">
<script src='<%=path%>/statics/js/jquery.min.js'></script>
<script src='<%=path%>/statics/js/toast.js'></script>
<script src="<%=path%>/statics/js/clipboard.min.js"></script>
<script src="<%=path%>/statics/js/toast.js"></script>
<script>
    $(function () {
        var clipboard = new ClipboardJS('.btn');
        clipboard.on('success', function(e) {
            console.info('Action:', e.action);
            console.info('Text:', e.text);
            console.info('Trigger:', e.trigger);
            Toast('视推号复制成功', 2000);
            e.clearSelection();
        });

        clipboard.on('error', function(e) {
            console.error('Action:', e.action);
            console.error('Trigger:', e.trigger);
        });
    })

</script>
</body>
</html>