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
    <title>微信消息管理</title>
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
        <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="detail"><i class="layui-icon">&#xe63c;</i>详细</a>
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
            , url: '${contextPath}/admin/cfg/wxMessageData'
            , cellMinWidth: 70
            , id: 'id'
            , cols: [[
                {field: 'id', width:40, title: 'ID'}
                , {field: 'title', title: '标题'}
                , {field: 'type', title: '消息类型', templet:function(d){
                        if(d.type === 1) {
                            return '客服消息';
                        }else if(d.type === 2) {
                            return '模板消息';
                        } else {
                            return d.type;
                        }
                    }}
                , {field: 'triggerPosition', title: '触发位置', templet:function(d){
                        switch (d.triggerPosition) {
                            case 11:
                                return '';
                            case 12:
                                return '';
                            case 21:
                                return '';
                            default:
                                return d.triggerPosition;
                        }
                    }}
                , {field: 'msgType', title: '客服消息类型',templet:function(d){
                        if(d.msgType === null){
                            return '-';
                        }
                        switch (d.msgType) {
                            case 1:
                                return '文本消息';
                            case 2:
                                return '图片消息';
                            case 3:
                                return '图文消息';
                        }
                    }}
                , {field: 'sort', title: '排序'}
                , {field: 'createdTime', title: '创建时间', templet:function(d){
                        return util.toDateString(d.createdTime, "yyyy-MM-dd HH:mm");
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
            if (obj.event === 'detail') {
                x_admin_show('详细信息', '<%=path%>/admin/cfg/wxMessageEdit?id=' + data.id);
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
            }
        }, 'data');
    }

    function reFetch(id) {
        $.ajax({
            url:'${contextPath}/admin/base/reFetch',
            type:'get',
            data:{id:id},
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