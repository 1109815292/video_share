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

    <table class="layui-hide" id="tableList" lay-filter="tableList"></table>

    <script type="text/html" id="toolbar">
        <div class="layui-btn-container">
            <button class="layui-btn layui-btn-primary layui-btn-sm" lay-event="refresh"><i
                    class="layui-icon">&#xe669;</i>刷新
            </button>
        </div>
    </script>
    <script type="text/html" id="lineBar">
    <a id="btn_cash_{{d.id}}" class="layui-btn layui-btn-xs" lay-event="cash"><i class="icon iconfont">&#xe69e;</i>重新提现</a>
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
            , url: '${contextPath}/admin/cash/getNoCashData'
            , where: {
                payFlag:0
            }
            , cellMinWidth: 70
            , id: 'id'
            , cols: [[
                {field: 'id', title: 'ID', width:40}
                ,{field: 'appUser', title: '支付用户', templet:function(d){
                        return d.appUser.userName;
                    }}
                , {field: 'rechargeAmount', title: '支付金额'}
                , {field: 'fansUser', title: '收益来源用户', templet:function(d){
                        return d.fansUser.userName;
                    }}

                , {field: 'divideAmount', title: '收益金额'}
                , {field: 'divideUser', title: '收益人', templet:function(d){
                        return d.divideUser.userName;
                    }}

                , {field: 'payFlag', title: '提现状态', templet:function(d){
                        if(d.payFlag === 0) {
                            return '<span class="layui-badge layui-bg-red">未提现</span>';
                        } else if(d.payFlag === 1) {
                            return '<span class="layui-badge layui-bg-green">已提现</span>';
                        }
                    }}
                , {fixed: 'right', title: '操作', toolbar: '#lineBar', width: 150}
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

        //监听行工具事件
        table.on('tool(tableList)', function (obj) {
            var data = obj.data;
            if (obj.event === 'cash') {
                layer.confirm('确认提现操作？', function (index) {
                    layer.close(index);
                    reRash(data.id);
                });
            }
        });

        var $ = layui.$, active = {
            reload: function () {
                //执行重载
                table.reload('id', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    , where: {
                        payFlag:0
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
        table.reload('id', {
            page: {
                curr: dataGrid.config.page.curr
            }
            , where: {
                payFlag:0
            }
        }, 'data');
    }

    function reRash(id) {
        $.ajax({
            url :'${contextPath}/admin/cash/recash',
            data:{
              id:  id
            },
            beforeSend:function(){
                $('btn_cash_'+id).addClass("ayui-btn-disabled");
            },
            complete:function(){
                $('btn_cash_'+id).removeClass("ayui-btn-disabled");
            },
            success:function(res){
                if (res.state === 1) {
                    layer.msg("操作成功");
                    refresh();
                } else {
                    layer.msg(res.msg);
                }
            }
        });
    }

</script>
</body>

</html>