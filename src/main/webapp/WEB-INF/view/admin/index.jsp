<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/5/9 0009
  Time: 17:32
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
    <title>视频广告管理系统</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />

    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" href="<%=path%>/statics/css/font.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/xadmin.css">
    <link rel="stylesheet" href="<%=path%>/statics/layui/css/layui.css">

    <script src="<%=path%>/statics/js/jquery.min.js"></script>
    <script src="<%=path%>/statics/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=path%>/statics/js/xadmin.js"></script>

</head>
<body>
<!-- 顶部开始 -->
<div class="container">
    <div class="logo"><a href="index.html">视频广告系统</a></div>
    <div class="left_open">
        <i title="展开左侧栏" class="iconfont">&#xe699;</i>
    </div>
    <ul class="layui-nav left fast-add" lay-filter="">
        <li class="layui-nav-item">
            <a href="javascript:;">+快捷操作</a>
            <dl class="layui-nav-child"> <!-- 二级菜单 -->

            </dl>
        </li>
    </ul>
    <ul class="layui-nav right" lay-filter="">
        <li class="layui-nav-item">
            <a href="javascript:;">${sessionScope.userName}</a>
            <dl class="layui-nav-child"> <!-- 二级菜单 -->
                <dd><a onClick="x_admin_show('修改密码','<%=path%>/admin/sys/updatePassword',600, 400)">修改密码</a></dd>
                <dd><a href="<%=path%>/admin/userLogout">退出</a></dd>
            </dl>
        </li>
    </ul>

</div>
<!-- 顶部结束 -->
<!-- 中部开始 -->
<!-- 左侧菜单开始 -->
<div class="left-nav">
    <div id="side-nav">
        <c:choose>
            <c:when test="${sessionScope.session_admin_user_type eq 1}">
                <ul id="nav">
                    <li >
                        <a href="javascript:;">
                            <i class="iconfont">&#xe6c0;</i>
                            <cite>用户统计</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">

                        </ul>
                    </li>
                    <li >
                        <a href="javascript:;">
                            <i class="iconfont">&#xe70c;</i>
                            <cite>运营管理</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">
                            <li>
                                <a _href="<%=path%>/admin/video/videoList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>视频管理</cite>

                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/video/uncheckVideoList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>视频管理审核</cite>

                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/user/userList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>用户列表</cite>

                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/vip/vipList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>VIP设置</cite>

                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/cash/dividePayFailure">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>未提现成功记录</cite>

                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/cfg/wxMessage">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>微信消息配置</cite>

                                </a>
                            </li>
                        </ul>
                    </li>
                    <li >
                        <a href="javascript:;">
                            <i class="iconfont">&#xe6e4;</i>
                            <cite>财务管理</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">
                            <li>
                                <a _href="<%=path%>/admin/order/orderList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>订单明细</cite>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li >
                        <a href="javascript:;">
                            <i class="iconfont">&#xe6e4;</i>
                            <cite>合伙人管理</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">
                            <li>
                                <a _href="<%=path%>/admin/partner/partnerList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>合伙人列表</cite>

                                </a>
                            </li>
                        </ul>
                        <ul class="sub-menu">
                            <li>
                                <a _href="<%=path%>/admin/partner/partnerDivide">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>合伙人收益</cite>

                                </a>
                            </li>
                        </ul>
                    </li>
                    <li >
                        <a href="javascript:;">
                            <i class="iconfont">&#xe6e4;</i>
                            <cite>渠道管理</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">
                            <li>
                                <a _href="<%=path%>/admin/chan/chanList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>渠道列表</cite>

                                </a>
                            </li>
                        </ul>
                    </li>
                    <li >
                        <a href="javascript:;">
                            <i class="iconfont">&#xe6e4;</i>
                            <cite>站长管理</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">
                            <li>
                                <a _href="<%=path%>/admin/station/stationList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>站长列表</cite>
                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/seckillGoods/getSeckill">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>秒杀管理</cite>
                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/seckillGoods/addSeckill">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>添加秒杀</cite>
                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/seckillOrder/getOrder">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>订单管理</cite>
                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/seckillGoods/addSeckill">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>秒杀会员</cite>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li >
                        <a href="javascript:;">
                            <i class="iconfont">&#xe6e4;</i>
                            <cite>商家管理</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">
                            <li>
                                <a _href="<%=path%>/admin/merchant/merchantList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>商家列表</cite>

                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/store/storeList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>店铺列表</cite>

                                </a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="javascript:;">
                            <i class="iconfont">&#xe6cb;</i>
                            <cite>基础数据</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">
                            <li>
                                <a _href="<%=path%>/admin/base/sysParamList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>系统参数</cite>

                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/base/varParamList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>系统变量</cite>

                                </a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="javascript:;">
                            <i class="iconfont">&#xe6ae;</i>
                            <cite>系统管理</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">
                            <li>
                                <a _href="<%=path%>/admin/sys/sysUserList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>系统用户管理</cite>

                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/sys/sysRoleList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>角色管理</cite>

                                </a>
                            </li >
                            <li>
                                <a _href="<%=path%>/admin/sys/sysRolePermissionList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>角色权限</cite>

                                </a>
                            </li>


                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:when test="${sessionScope.session_admin_user_type eq 2}">
                <ul id="nav">
                    <li >
                        <a href="javascript:;">
                            <i class="iconfont">&#xe6c0;</i>
                            <cite>用户统计</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">

                        </ul>
                    </li>
                    <li >
                        <a href="javascript:;">
                            <i class="iconfont">&#xe6e4;</i>
                            <cite>渠道管理</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">
                            <li>
                                <a _href="<%=path%>/admin/chan/chanList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>渠道列表</cite>

                                </a>
                            </li>
                            <li>
                                <a _href="<%=path%>/admin/chan/chanDivide">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>渠道收益</cite>

                                </a>
                            </li>
                        </ul>
                    </li>
                    <li >
                        <a href="javascript:;">
                            <i class="iconfont">&#xe6e4;</i>
                            <cite>站长管理</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">
                            <li>
                                <a _href="<%=path%>/admin/station/stationList">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>站长列表</cite>

                                </a>
                            </li>
                        </ul>
                    </li>

                </ul>
            </c:when>
            <c:when test="${sessionScope.session_admin_user_type eq 3 or sessionScope.session_admin_user_type eq 4}">
                <ul id="nav">
                    <li >
                        <a href="javascript:;">
                            <i class="iconfont">&#xe6e4;</i>
                            <cite>渠道管理</cite>
                            <i class="iconfont nav_right">&#xe6a7;</i>
                        </a>
                        <ul class="sub-menu">
                            <li>
                                <a _href="<%=path%>/admin/chan/chanDivide">
                                    <i class="iconfont">&#xe6a7;</i>
                                    <cite>渠道收益</cite>

                                </a>
                            </li>
                        </ul>
                    </li>
                    <c:if test="${sessionScope.session_admin_user_type eq 4}">
                        <li >
                            <a href="javascript:;">
                                <i class="iconfont">&#xe6e4;</i>
                                <cite>站长管理</cite>
                                <i class="iconfont nav_right">&#xe6a7;</i>
                            </a>
                            <ul class="sub-menu">
                                <li>
                                    <a _href="<%=path%>/admin/station/stationOrderList">
                                        <i class="iconfont">&#xe6a7;</i>
                                        <cite>实体店充值记录</cite>

                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li >
                            <a href="javascript:;">
                                <i class="iconfont">&#xe6e4;</i>
                                <cite>商家管理</cite>
                                <i class="iconfont nav_right">&#xe6a7;</i>
                            </a>
                            <ul class="sub-menu">
                                <li>
                                    <a _href="<%=path%>/admin/merchant/merchantList">
                                        <i class="iconfont">&#xe6a7;</i>
                                        <cite>商家列表</cite>

                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:if>
                </ul>
            </c:when>
        </c:choose>

    </div>
</div>
<!-- <div class="x-slide_left"></div> -->
<!-- 左侧菜单结束 -->
<!-- 右侧主体开始 -->
<div class="page-content">
    <div class="layui-tab tab" lay-filter="xbs_tab" lay-allowclose="false">
        <ul class="layui-tab-title">
            <li class="home"><i class="layui-icon">&#xe68e;</i>我的桌面</li>
        </ul>
        <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">
                <iframe src='<%=path%>/admin/welcome' frameborder="0" scrolling="yes" class="x-iframe"></iframe>
            </div>
        </div>
    </div>
</div>
<div class="page-content-bg"></div>
<!-- 右侧主体结束 -->
<!-- 中部结束 -->
<!-- 底部开始 -->
<!--<div class="footer">
    <div class="copyright">Copyright ©2019 L-admin v2.3 All Rights Reserved</div>
</div>-->
<!-- 底部结束 -->
</body>
</html>
