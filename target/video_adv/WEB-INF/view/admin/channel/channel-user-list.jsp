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
    <title>渠道管理</title>
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
            <input class="layui-input" id="tableSearch" placeholder="用户uid / 昵称" autocomplete="off">
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

</div>
<script>

    var dataGrid;
    var table, layer;
    layui.use(['form', 'layer','util', 'table'], function () {
        table = layui.table;
        layer = layui.layer;
        var form = layui.form;
        var util = layui.util;

        dataGrid = table.render({
            elem: '#tableList'
            , toolbar: '#toolbar'
            , url: '${contextPath}/admin/chan/getChanUserData'
            , where: {
                copyNo:'${param.copyNo}'
            }
            , cellMinWidth: 70
            , id: 'id'
            , cols: [[
                {type: 'checkbox', fixed: 'left'}
                , {field: 'copyNo', title: '视推号'}
                , {field: 'userName', title: '昵称'}
                , {field: 'openId', title: 'openID', hide: 'true'}
                , {field: 'gender', title: '性别', hide: 'true'}
                , {field: 'mobile', title: '手机号', hide: 'true'}
                , {field: 'vipFlag', align: 'center', title: '是否会员'}

                , {
                    field: 'state', title: '用户状态', align: 'center', templet: function (b) {
                        switch (b.state) {
                            case 0:
                                return '<span class="layui-badge layui-bg-red">封禁</span>';
                            case 1:
                                return '<span class="layui-badge layui-bg-green">正常</span>';
                        }
                    }
                }
            ]]
            , page: true
        });

        //头工具栏事件
        table.on('toolbar(tableList)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            switch (obj.event) {
                case 'refresh':
                    refresh();
                    break;
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
                search: tableSearch.val()
            }
        }, 'data');
    }

</script>
</body>

</html>