<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/5/9 0009
  Time: 19:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>绑定用户</title>
    <meta name="renderer" content="webkit">
    <meta name="referer" content="never">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="<%=path%>/statics/css/font.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/xadmin.css">
    <link rel="stylesheet" href="<%=path%>/statics/lib/layui/css/layui.css">
    <link rel="stylesheet" href="<%=path%>/statics/lib/layui/css/modules/laydate/default/laydate.css">

    <script src="<%=path%>/statics/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=path%>/statics/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=path%>/statics/js/xadmin.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
<div class="x-body">
    <div class="tableSearch">
        <div class="layui-inline">
            <input class="layui-input" id="tableSearch" placeholder="微信昵称/视推号" autocomplete="off">
        </div>
        <button class="layui-btn" data-type="reload">搜索</button>
    </div>

    <table class="layui-hide" id="tableList" lay-filter="tableList"></table>

    <script type="text/html" id="lineBar">
        <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="bind"><i
                class="layui-icon">&#xe63c;</i>绑定</a>
    </script>
</div>
<script>
    $(function () {

    });

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
            , url: '${contextPath}/admin/station/getStationAppUserData'
            , cellMinWidth: 70
            , id: 'id'
            , cols: [[
                {field: 'copyNo', title: '视推号'}
                , {
                    field: 'headImg', title: '头像', width: 80, templet: function (d) {
                        return '<img src="' + d.headImg + '" style="width:60px;" >'
                    }
                }
                , {field: 'userName', title: '昵称'}
                , {fixed: 'right', title: '操作', toolbar: '#lineBar', width: 150}
            ]]
            , page: true
        });


        //监听行工具事件
        table.on('tool(tableList)', function (obj) {
            var data = obj.data;
            if (obj.event === 'bind') {
                doBind(data.copyNo);
            }
        });


        var $ = layui.$, active = {
            reload: function () {
                var tableSearch = $('#tableSearch');
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
        table.reload('id', {
            page: {
                curr: dataGrid.config.page.curr
            }
            , where: {
                search: tableSearch.val()
            }
        }, 'data');
    }

    function doBind(copyNo) {
        <%--x_admin_show("选择区域", "${contextPath}/admin/district/select", 800,400);--%>
        layer.open({
            type: 2 //此处以iframe举例
            , title: '选择所属区域'
            , area: ['800px', '400px']
            , shade: 0
            , maxmin: true
            , content: '${contextPath}/admin/district/select'
            , btn: ['确定', '取消'] //只是为了演示
            , yes: function (index, layero) {
                //$(that).click();
                var select1 = layero.find('iframe').contents().find("#select1").val();
                var select2 = layero.find('iframe').contents().find("#select2").val();
                var select3 = layero.find('iframe').contents().find("#select3").val();

                if (select1 === null || select1 === "") {
                    layer.msg("请选择所属区域", {icon: 6});
                    return;
                }

                var districtId = select1;

                if (select2 !== null && select2 !== "")
                    districtId = select2;
                if (select3 !== null && select3 !== "")
                    districtId = select3;
                binding(copyNo, districtId, function(){
                    layer.close(index);
                });

            }
            , btn2: function () {
                layer.closeAll();
            }

            , zIndex: layer.zIndex //重点1
            , success: function (layero) {
                //layer.setTop(layero); //重点2
            }
        });


    }

    function binding(copyNo, districtId, callback) {
        $.ajax({
            url: '${contextPath}/admin/station/doStationBind',
            data: {
                sysUserId: '${param.id}',
                copyNo: copyNo,
                districtId: districtId,
            },
            success: function (res) {
                if (res.state === 1) {
                    layer.msg("保存成功！", {icon: 6});
                    setTimeout(function () {
                        window.parent.location.reload();//修改成功后刷新父界面
                    }, 100);
                } else {
                    layer.msg(res.msg);
                }
                if (callback !== null && callback !== undefined)
                    callback();
            }
        });
    }
</script>
</body>

</html>