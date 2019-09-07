<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  AppUser: Administrator
  Date: 2019/5/9 0009
  Time: 19:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>订单管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="<%=path%>/statics/css/font.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/xadmin.css">
    <link rel="stylesheet" href="<%=path%>/statics/layui/css/layui.css">
    <link rel="stylesheet" href="<%=path%>/statics/lib/layui/css/modules/laydate/default/laydate.css">

    <script src="<%=path%>/statics/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=path%>/statics/layui/layui.all.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=path%>/statics/js/xadmin.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>

    <![endif]-->
</head>

<body>

<div class="x-body">

    <div class="tableSearch">
        <div class="layui-inline">
            <select id="tableSelect" class="layui-select">
                <option value="">订单状态:&nbsp;ALL</option>
                <option value="0">订单状态:&nbsp;预支付</option>
                <option value="1">订单状态:&nbsp;已支付</option>
                <option value="2">订单状态:&nbsp;已完成</option>
                <option value="3">订单状态:&nbsp;支付回调处理异常</option>
                <option value="-1">订单状态:&nbsp;已取消</option>
            </select>
        </div>
        <div class="layui-inline">
            <input class="layui-input" id="tableSearch" placeholder="昵称 / 视推号" autocomplete="off">
        </div>
        <button class="layui-btn" data-type="reload">搜索</button>
    </div>


    <table class="layui-hide" id="tableList" lay-filter="tableList"></table>

    <script type="text/html" id="toolbar">
        <div class="layui-btn-container">
            <button class="layui-btn layui-btn-primary layui-btn-sm" lay-event="refresh"><i
                    class="layui-icon">&#xe669;</i>刷新
            </button>
        </div>
    </script>

    <script type="text/html" id="lineBar">
        <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="detail"><i class="layui-icon">&#xe63c;</i>明细</a>
    </script>

</div>
<script>

    var dataGrid;
    var table, layer;
    layui.use(['form', 'util', 'layer', 'table'], function () {
        table = layui.table;
        layer = layui.layer;
        var util = layui.util;
        var form = layui.form;

        dataGrid = table.render({
            elem: '#tableList'
            , toolbar: '#toolbar'
            , url: '${contextPath}/admin/order/getOrderData'
            , cellMinWidth: 70
            , id: 'id'
            , cols: [[
                {field: 'id', title: 'ID', hide: 'true'}
                , {field: 'orderNo', title: '订单编号'}
                , {
                    field: 'userName', title: '昵称', templet: function (d) {
                        return d.appUser.userName;
                    }
                }
                , {
                    field: 'copyNo', title: '视推号', templet: function (d) {
                        return d.appUser.copyNo;
                    }
                }
                , {field: 'openId', title: '用户openId', hide: 'true'}

                , {field: 'amount', title: '订单金额'}
                , {
                    field: 'payType', title: '支付方式', align: 'center', templet: function (d) {
                        switch (d.payType) {
                            case 1:
                                return '<span class="layui-badge layui-bg-green">微信支付</span>';
                            default:
                                return d.payType;
                        }
                    }
                }
                , {
                    field: 'payTime', title: '支付时间', templet: function (d) {
                        if (d.payTime === null|| d.payTime === undefined)
                            return "-";
                        return util.toDateString(d.payTime, "yyyy-MM-dd HH:mm");
                    }
                }
                , {
                    field: 'objectType', title: '订单对象类别', templet: function (d) {
                        switch (d.objectType) {
                            case 1:
                                return '<span class="layui-badge layui-bg-orange">线上会员</span>';
                            case 2:
                                return '<span class="layui-badge layui-bg-blue">实体店会员</span>';
                            default:
                                return d.objectType;
                        }
                    }
                }
                , {field: 'objectId', title: '订单对象ID'}
                , {field: 'state', title: '订单状态', minWidth: 140,templet: function (d) {
                        switch (d.state) {
                            case -1:
                                return '<span class="layui-badge layui-bg-gray">已取消</span>';
                            case 0:
                                return '<span class="layui-badge layui-bg-blue">预生成</span>';
                            case 1:
                                return '<span class="layui-badge layui-bg-orange">已支付</span>';
                            case 2:
                                return '<span class="layui-badge layui-bg-green">已完成</span>';
                            case 3:
                                return '<span class="layui-badge layui-bg-red">支付回调处理异常</span>';
                            default:
                                return d.state;
                        }
                    }}
                , {
                    field: 'createdTime', title: '创建时间', minWidth: 110, templet: function (d) {
                        return util.toDateString(d.createdTime, "yyyy-MM-dd");
                    }
                }
                , {fixed: 'right', title: '操作', toolbar: '#lineBar'}
            ]]
            , page: true
        });

        //头工具栏事件
        table.on('toolbar(tableList)', function (obj) {
            switch (obj.event) {
                case 'refresh':
                    refresh();
                    break;
            }
        });

        //监听行工具事件
        table.on('tool(tableList)', function (obj) {
            var data = obj.data;
            if (obj.event === 'detail') {
                x_admin_show('详情', '<%=path%>/admin/order/orderDetail?id=' + data.id);
            }
        });


        var $ = layui.$, active = {
            reload: function () {
                var tableSearch = $('#tableSearch');
                var tableSelect = $('#tableSelect');
                //执行重载
                table.reload('id', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    , where: {
                        state: tableSelect.val(),
                        search: tableSearch.val()
                    }
                }, 'data');
            }
        };

        $('.tableSearch .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });


    });


    function refresh() {
        var tableSearch = $('#tableSearch');
        var tableSelect = $('#tableSelect');
        table.reload('id', {
            page: {
                curr: dataGrid.config.page.curr
            }
            , where: {
                state: tableSelect.val(),
                search: tableSearch.val()
            }
        }, 'data');
    }

</script>
</body>

</html>