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
    <title>秒杀管理</title>
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
            <label class="layui-form-label">搜索关键字</label>
            <div class="layui-input-block">
                <input type="text" name="name" id="name" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">店铺ID</label>
            <div class="layui-input-block">
                <input type="text" name="storeId" id="storeId" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-inline">
            <select id="tableSelect" class="layui-select">
                <option value="">全部</option>
                <option value="0">抢购中</option>
                <option value="1">已下架</option>
            </select>
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
    layui.use(['form', 'layer', 'util', 'table'], function () {
        table = layui.table;
        layer = layui.layer;
        var form = layui.form;
        var util = layui.util;
        var laydate = layui.laydate;

        dataGrid = table.render({
            elem: '#tableList'
            , toolbar: '#toolbar'
            , url: '${contextPath}/admin/seckillGoods/getSeckillDate'
            , where: {}
            , cellMinWidth: 70
            , id: 'id'
            , cols: [[
                {
                    field: 'smallPic', title: '商品图片'
                }
                , {field: 'name', title: '商品标题'}
                , {field: 'storeName', title: '店铺名称'}
                , {field: 'num', title: '库存情况'}
                , {field: 'startTime', title: '开始时间', templet: function (d) {
                        return util.toDateString(d.startTime, "yyyy-MM-dd HH:mm:ss");
                    }
                }
                , {field: 'seckillStatis.salaNum', title: '销量'}
                , {field: 'seckillStatis.examNum', title: '已核销的券'}
                , {field: 'separate', title: '运营商分成'}
                , {field: 'seckillStatis.totelPrice', title: '成交额'}
                , {field: 'status', title: '审核状态', templet: function (d) {
                        if (d.status === 0) {
                            return '<span class="layui-badge layui-bg-red">未提现</span>';
                        } else if (d.status === 1) {
                            return '<span class="layui-badge layui-bg-green">已提现</span>';
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
                var name = $('#name');
                var storeId = $('#storeId');
                var tableSelect = $('#tableSelect');
                //执行重载
                table.reload('id', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    , where: {
                        name: name.val(),
                        storeId: storeId.val(),
                        tableSelect: tableSelect.val()
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
        var name = $('#name');
        var storeId = $('#storeId');
        var tableSelect = $('#tableSelect');
        table.reload('id', {
            page: {
                curr: dataGrid.config.page.curr
            }
            , where: {
                name: name.val(),
                storeId: storeId.val(),
                tableSelect: tableSelect.val()
            }
        }, 'data');
    }

</script>
</body>

</html>