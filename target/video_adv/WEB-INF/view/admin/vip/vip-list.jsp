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
    <title>VIP设置</title>
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
        <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i class="layui-icon">&#xe63c;</i>编辑</a>
        {{# if (d.enabledFlag=== 'Y') { }}
        <a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="disabled"><i
                class="icon iconfont">&#xe69e;</i>禁用</a>
        {{# }else{ }}
        <a class="layui-btn layui-btn-xs" lay-event="enabled"><i class="icon iconfont">&#xe69e;</i>启用</a>
        {{# } }}
        <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i class="layui-icon">&#xe63c;</i>删除</a>

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
            , url: '${contextPath}/admin/vip/getVipData'
            , cellMinWidth: 70
            , id: 'id'
            , cols: [[
                {field: 'id', title: 'ID', hide: 'true'}
                , {field: 'title', title: '标题'}
                , {field: 'tag', title: '标签'}
                , {field: 'description', title: '描述', hide: 'true'}
                , {
                    field: 'originalPrice', title: '原价', hide: 'true', templet: function (d) {
                        return d.originalPrice / 100;
                    }
                }
                , {
                    field: 'presentPrice', title: '现价', templet: function (d) {
                        return d.presentPrice / 100;
                    }
                }
                , {field: 'days', title: '天数'}
                , {field: 'giftDays', title: '赠送天数'}
                , {field: 'tips', title: '提示'}
                , {
                    field: 'type', title: 'vip类型', templet: function (d) {
                        if (d.type === 1) {
                            return '线上vip';
                        } else if (d.type === 2) {
                            return '线下vip';
                        }
                    }
                }
                , {field: 'visibleType', title: '可见类别', hide: true}
                , {field: 'sort', title: '排序', hide: 'true'}
                , {field: 'enabledFlag', title: '启用状态'}
                , {
                    field: 'createdTime', title: '创建时间', minWidth: 110, templet: function (d) {
                        return util.toDateString(d.createdTime, "yyyy-MM-dd");
                    }
                }
                , {fixed: 'right', title: '操作', toolbar: '#lineBar', width: 200}
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
            if (obj.event === 'edit') {
                x_admin_show('详情', '<%=path%>/admin/vip/vipEdit?id=' + data.id);
            } else if (obj.event === 'disabled') { //禁用
                changeEnabled(data.id, 0);
            } else if (obj.event === 'enabled') {//启用
                changeEnabled(data.id, 1);
            } else if (obj.event === 'del') {

            }
        });


        var $ = layui.$, active = {
            reload: function () {
                //执行重载
                table.reload('id', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    , where: {}
                }, 'data');
            }
        };

    });

    function changeEnabled(id, enabled) {
        $.ajax({
            url: '${contextPath}/admin/vip/changeEnabled',
            type: 'POST',
            data: {id: id, enabled: enabled},
            success: function (res) {
                if (res.state === 1) {
                    layer.msg("操作成功");
                } else {
                    layer.alert("操作失败:" + res.msg);
                }
            }
        });
    }


    function refresh() {
        table.reload('id', {
            page: {
                curr: dataGrid.config.page.curr
            }
            , where: {}
        }, 'data');
    }

</script>
</body>

</html>