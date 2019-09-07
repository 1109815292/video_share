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
    <title>合伙人收益</title>
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
            <label class="layui-form-label">开始日期</label>
            <div class="layui-input-block">
                <input type="text" name="from" id="date_from" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">结束日期</label>
            <div class="layui-input-block">
                <input type="text" name="to" id="date_to" autocomplete="off" class="layui-input">
            </div>
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
        //日期
        laydate.render({
            elem: '#date_from'
        });
        laydate.render({
            elem: '#date_to'
        });

        dataGrid = table.render({
            elem: '#tableList'
            , toolbar: '#toolbar'
            , url: '${contextPath}/admin/partner/getPartnerDivideData'
            , where: {}
            , cellMinWidth: 70
            , id: 'id'
            , cols: [[
                {
                    field: 'appUser', title: '支付用户', templet: function (d) {
                        return d.appUser.userName;
                    }
                }
                , {field: 'rechargeAmount', title: '支付金额'}
                , {
                    field: 'fansUser', title: '收益来源用户', templet: function (d) {
                        return d.fansUser.userName;
                    }
                }
                , {field: 'divideUser', title: '收益人', templet:function(d){
                        return d.divideUser.userName;
                    }}
                , {field: 'divideAmount', title: '收益金额'}
                , {
                    field: 'payFlag', title: '提现状态', templet: function (d) {
                        if (d.payFlag === 0) {
                            return '<span class="layui-badge layui-bg-red">未提现</span>';
                        } else if (d.payFlag === 1) {
                            return '<span class="layui-badge layui-bg-green">已提现</span>';
                        }
                    }
                }
                , {
                    field: 'createdTime', title: '时间', templet: function (d) {
                        return util.toDateString(d.createdTime, "yyyy-MM-dd HH:mm:ss");
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
                var dateFrom = $('#date_from');
                var dateTo = $('#date_to');
                //执行重载
                table.reload('id', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    , where: {
                        dateFrom: dateFrom.val(),
                        dateTo: dateTo.val()
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
        var dateFrom = $('#date_from');
        var dateTo = $('#date_to');
        table.reload('id', {
            page: {
                curr: dataGrid.config.page.curr
            }
            , where: {
                dateFrom: dateFrom.val(),
                dateTo: dateTo.val()
            }
        }, 'data');
    }

</script>
</body>

</html>