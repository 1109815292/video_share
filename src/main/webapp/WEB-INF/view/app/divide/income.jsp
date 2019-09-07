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
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>收益明细</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />

    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->


</head>
<body class="bg-square">

    <div class="incomeTop">
        <ul>
            <li><span class="incomeTop-amount">${dayIncome}</span><span>今日收益（元）</span></li>
            <li><span class="incomeTop-amount">${monthIncome}</span><span>本月收益（元）</span></li>
            <li><span class="incomeTop-amount">${allIncome}</span><span>累计收益（元）</span></li>
        </ul>
        <div class="incomeTop-label">每笔订单系统自动打入您的微信钱包</div>
    </div>

    <c:choose>
        <c:when test="${empty rechargeDivideList}">
            <div class="incomeTop-noData">
                <img src="<%=path%>/statics/img/video/manage/bg_24.png" />
            </div>
        </c:when>
        <c:otherwise>
            <div class="cont">
                <table class="income-table">
                    <thead>
                        <th style="width:30%;">日期</th>
                        <th style="width:50%;">粉丝</th>
                        <th style="width:20%;">收益</th>
                    </thead>
                    <tbody>
                    <c:forEach var="rechargeDivide" items="${rechargeDivideList}">
                        <tr>
                            <td style="text-align: center;"><fmt:formatDate value="${rechargeDivide.createdTime}" pattern="yyyy-MM-dd" /> </td>
                            <td style="text-align: center">
                                <div class="headImg">
                                    <img src="${rechargeDivide.fansUser.headImg}">
                                </div>
                                <div class="userName">${rechargeDivide.fansUser.userName}</div>
                            </td>
                            <td style="text-align: center;">${rechargeDivide.divideAmount}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>



    <div class="back" onclick="javascript:history.back(-1);">
        返回
    </div>
</body>
</html>