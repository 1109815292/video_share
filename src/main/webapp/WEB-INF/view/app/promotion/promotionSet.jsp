
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
    <title>推广设置</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" href="<%=path%>/statics/css/videostyle.css">
    <link rel="stylesheet" href="<%=path%>/statics/search/style.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">


</head>
<body class="bg-square;">

<div style="background:#eee;padding:9px 0 100px 0;">
    <div class="promotionSet">
        <ul>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advProduct.png" /></div>
                <div class="setLabel">推广商品</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advProductManage?type=1'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advProductSet?type=1'">添加</div>
            </li>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advProduct-jd.png" /></div>
                <div class="setLabel">京东商品</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advProductManage?type=2'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advProductSet?type=2'">添加</div>
            </li>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advProduct-wd.png" /></div>
                <div class="setLabel">微店商品</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advProductManage?type=3'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advProductSet?type=3'">添加</div>
            </li>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advProduct-yz.png" /></div>
                <div class="setLabel">有赞商品</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advProductManage?type=4'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advProductSet?type=4'">添加</div>
            </li>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advProduct-tb.png" /></div>
                <div class="setLabel">淘宝商品</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advProductManage?type=5'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advProductSet?type=5'">添加</div>
            </li>
        </ul>
    </div>
    <div class="promotionSet-bar">实体店推广</div>
    <div class="promotionSet">
        <ul>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advStore.png" /></div>
                <div class="setLabel">实体店资料</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advStoreManage'">管理</div>
            </li>
        </ul>
    </div>
    <div class="promotionSet-bar">推广微信</div>
    <div class="promotionSet">
        <ul>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advPublic.png" /></div>
                <div class="setLabel">推广公众号</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherManage?type=1'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherSet?type=1'">添加</div>
            </li>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advWx-applet.png" /></div>
                <div class="setLabel">推广小程序</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherManage?type=2'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherSet?type=2'">添加</div>
            </li>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advWx.png" /></div>
                <div class="setLabel">推广个人微信</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherManage?type=3'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherSet?type=3'">添加</div>
            </li>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advWx.png" /></div>
                <div class="setLabel">推广微信群</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherManage?type=4'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherSet?type=4'">添加</div>
            </li>
        </ul>
    </div>
    <div class="promotionSet-bar">其他推广</div>
    <div class="promotionSet">
        <ul>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advOther-app.png" /></div>
                <div class="setLabel">推广APP</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherManage?type=5'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherSet?type=5'">添加</div>
            </li>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advWeb.png" /></div>
                <div class="setLabel">推广官网</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherManage?type=6'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherSet?type=6'">添加</div>
            </li>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advLink.png" /></div>
                <div class="setLabel">推广链接</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherManage?type=7'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherSet?type=7'">添加</div>
            </li>
            <li>
                <div class="promotionIcon"><img src="<%=path%>/statics/img/promotion/advOther-qrcode.png" /></div>
                <div class="setLabel">推广二维码</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherManage?type=8'">管理</div>
                <div class="promotionBtn" onclick="window.location.href='<%=path%>/app/advOtherSet?type=8'">添加</div>
            </li>
        </ul>
    </div>

    <div class="back" onclick="window.location.href='<%=path%>/app/manageCenter'">
       返回
    </div>
</div>
</body>
</html>