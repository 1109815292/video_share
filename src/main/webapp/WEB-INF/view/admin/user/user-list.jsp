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
    <title>用户管理</title>
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

</head>

<body>

<div class="x-body">
    <div class="tableSearch">
        <div class="layui-inline">
            <select id="tableSelect" class="layui-select">
                <option value="">会员状态:&nbsp;ALL</option>
                <option value="Y">会员状态:&nbsp;会员</option>
                <option value="N">会员状态:&nbsp;非会员</option>
            </select>
        </div>
        <div class="layui-inline">
            <input class="layui-input" id="tableSearch" placeholder="用户uid / 昵称" autocomplete="off">
        </div>
        <button class="layui-btn" data-type="reload">搜索</button>
    </div>

    <table class="layui-hide" id="tableList" lay-filter="tableList"></table>

    <script type="text/html" id="toolbar">
        <div class="layui-btn-container">
            <button class="layui-btn layui-btn-sm" lay-event="settingVip"><i
                    class="layui-icon">&#xe735;</i>设置会员
            </button>
            <button class="layui-btn layui-btn-primary layui-btn-sm" lay-event="refresh"><i
                    class="layui-icon">&#xe669;</i>刷新
            </button>
        </div>
    </script>

    <script type="text/html" id="lineBar">
        <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="detail"><i class="layui-icon">&#xe63c;</i>详情</a>
        {{# if (d.state=== 1) { }}
        <a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="block"><i class="icon iconfont">&#xe69e;</i>封禁</a>
        {{# }else{ }}
        <a class="layui-btn layui-btn-xs" lay-event="unblock"><i class="icon iconfont">&#xe69e;</i>解封</a>
        {{# } }}

    </script>

    <script type="text/html" id="switchPublishFlagTpl">
        <input type="checkbox" name="publishFlag" value={{d.id}} lay-skin="switch" lay-text="YES|NO"
               lay-filter="ifPublishFlag" {{ d.publishFlag== 'Y'?'checked':''}} >
    </script>

    <script type="text/html" id="switchAdminFlagTpl">
        <input type="checkbox" name="adminFlag" value={{d.id}} lay-skin="switch" lay-text="YES|NO"
               lay-filter="ifAdminFlag" {{ d.adminFlag== 'Y'?'checked':''}} >
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

        dataGrid = table.render({
            elem: '#tableList'
            , toolbar: '#toolbar'
            , url: '${contextPath}/admin/user/getUserData'
            , cellMinWidth: 70
            , id: 'id'
            , cols: [[
                {type: 'checkbox', fixed: 'left'}
                , {field: 'copyNo', title: '视推号'}
                , {field: 'userName', title: '昵称'}
                , {
                    field: 'inviter', title: '邀请人', width:200, templet: function (d) {
                        if (d.inviter !== null) {
                            return d.inviter.userName + "("+d.inviter.copyNo+")";
                        } else {
                            return '无';
                        }
                    }
                }
                , {field: 'openId', title: 'openID', hide: 'true'}
                , {field: 'gender', title: '性别', hide: 'true'}
                , {field: 'mobile', title: '手机号', hide: 'true'}
                , {field: 'vipFlag', align: 'center', title: '是否会员'}
                , {field: 'vipFlag', align: 'center', title: '会员类型', templet:function(d){
                        switch (d.vipType) {
                            case 0:
                                return "";
                            case 1:
                                return "线上vip";
                            case 2:
                                return "线下vip";
                            default:
                                return d.vipType;

                        }
                    }}
                , {
                    field: 'vipDeadline', title: '会员到期时间', templet: function (d) {
                        if (d.vipFlag === 'Y' && d.vipDeadline === null) {
                            return "永久会员";
                        } else {
                            if (d.vipDeadline === null)
                                return "";
                            return util.toDateString(d.vipDeadline, "yyyy-MM-dd HH:mm");
                        }
                    }
                }
                , {
                    field: 'publishFlag',
                    title: '是否可发布内容',
                    align: 'center',
                    templet: '#switchPublishFlagTpl',
                    unresize: true
                }
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
                , {field: 'amount', title: '余额'}
                , {
                     title: '用户类型', align: 'center', templet: function (b) {
                        switch (b.userLevel) {
                            case 1:
                                return '<span class="layui-badge layui-bg-red">合伙人</span>';
                            case 2:
                                if(b.stationFlag === 'Y')
                                    return '<span class="layui-badge layui-bg-orange">站长</span>';
                                else
                                    return '<span class="layui-badge layui-bg-green">渠道</span>';
                            default:
                                return '<span class="layui-badge layui-bg-blue">一般用户</span>';
                        }
                    }
                }
                , {
                    field: 'adminFlag', title: '是否管理员', align: 'center', templet: '#switchAdminFlagTpl', unresize: true
                }
                , {fixed: 'right', title: '操作', toolbar: '#lineBar', width: 150}
            ]]
            , page: true
        });

        //头工具栏事件
        table.on('toolbar(tableList)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            switch (obj.event) {
                case 'settingVip':
                    var data = checkStatus.data;
                    if (data.length === 0) {
                        layer.alert('未勾选操作记录');
                        return;
                    }
                    if (data.length > 1) {
                        layer.alert('只能操作一条记录');
                        return;
                    }
                    x_admin_show('设置会员', '<%=path%>/admin/user/userVip?id=' + data[0].id);
                    break;
                case 'refresh':
                    refresh();

                    break;
            }
        });

        //监听行工具事件
        table.on('tool(tableList)', function (obj) {
            var data = obj.data;
            if (obj.event === 'detail') {
                x_admin_show('详情', '<%=path%>/admin/user/userDetail?id=' + data.id);
            } else if (obj.event === 'block') {
                changeState(data.id, 0);
            } else if (obj.event === 'unblock') {
                changeState(data.id, 1);
            }
        });


        form.on('switch(ifPublishFlag)', function (obj) {
            changePublishFlag(this.value, obj.elem.checked);
        });

        form.on('switch(ifAdminFlag)', function (obj) {
            changeAdminFlag(this.value, obj.elem.checked);
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
                        search: tableSearch.val(),
                        vipFlag: tableSelect.val()
                    }
                }, 'data');
            }
        };

        $('.tableSearch .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
    });

    function changeAdminFlag(id, adminFlag) {
        $.ajax({
            url: '${contextPath}/admin/user/changeAdminFlag',
            type: 'POST',
            data: {id: id, adminFlag: adminFlag ? 'Y' : 'N'},
            success: function (res) {
                if (res.state === 1) {
                    layer.msg("操作成功");
                } else {
                    layer.alert("操作失败:" + res.msg);
                }
            }
        });
    }

    function changePublishFlag(id, publishFlag) {
        $.ajax({
            url: '${contextPath}/admin/user/changePublishFlag',
            type: 'POST',
            data: {id: id, publishFlag: publishFlag ? 'Y' : 'N'},
            success: function (res) {
                if (res.state === 1) {
                    layer.msg("操作成功");
                } else {
                    layer.alert("操作失败:" + res.msg);
                }
            }
        });
    }

    function changeState(id, state) {
        $.ajax({
            url: '${contextPath}/admin/user/changeState',
            type: 'POST',
            data: {id: id, state: state},
            success: function (res) {
                if (res.state === 1) {
                    layer.msg("操作成功");
                    refresh();
                } else {
                    layer.alert("操作失败:" + res.msg);
                }
            }
        });
    }

    function refresh() {
        var tableSearch = $('#tableSearch');
        var tableSelect = $('#tableSelect');
        table.reload('id', {
            page: {
                curr: dataGrid.config.page.curr
            }
            , where: {
                memberFlag: tableSelect.val(),
                search: tableSearch.val()
            }
        }, 'data');
    }

</script>
</body>

</html>