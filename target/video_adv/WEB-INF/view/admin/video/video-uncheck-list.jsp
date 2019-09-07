<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
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
    <title>视频管理-视频审核</title>
    <meta name="referer" content="never">
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
                <option value="0">审核状态:&nbsp;未审核</option>
                <option value="1">审核状态:&nbsp;已审核</option>
                <option value="-1">审核状态:&nbsp;已驳回</option>
            </select>
        </div>
        <div class="layui-inline">
            <input class="layui-input" id="tableSearch" placeholder="标题" autocomplete="off">
        </div>
        <button class="layui-btn" data-type="reload">搜索</button>
    </div>

    <table class="layui-hide" id="tableList" lay-filter="tableList"></table>
    <script type="text/html" id="toolbar">
        <div class="layui-btn-container">
            <button class="layui-btn layui-btn-primary layui-btn-sm" lay-event="refresh"><i
                    class="layui-icon">&#xe669;</i>刷新
            </button>

            <button class="layui-btn layui-btn-sm" lay-event="batchCheckStatePass"><i
                    class="layui-icon">&#xe605;</i>批量审核通过
            </button>
        </div>
    </script>

    <script type="text/html" id="lineBar">
        <a class="layui-btn layui-btn-xs" lay-event="checkPass"><i class="layui-icon">&#xe605;</i>通过</a>

        <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="checkReject"><i class="layui-icon">&#x1006;</i>  驳回</a>
    </script>
</div>
<script>
    /*用于查询刷新*/
    function datareload() {
        datagrid.reload({
            where: {search: $("#inp-keywords").val()}
            , url: '${contextPath}/admin/video/getUncheckVideoData'
        });
    }

    var datagrid;
    layui.use(['util', 'table'], function () {
        var table = layui.table;
        var util = layui.util;

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
                        checkState: tableSelect.val(),
                        search: tableSearch.val()
                    }
                }, 'data');
            }
        };

        datagrid = table.render({
            elem: '#tableList'
            , toolbar: '#toolbar'
            , url: '${contextPath}/admin/video/getUncheckVideoData'
            , where:{
                checkState:$('#tableSelect').val()
            }
            , cellMinWidth: 70
            , id: 'id'
            , cols: [[
                {type: 'checkbox', fixed: 'left'},
                {field: 'id', title: 'ID'}
                , {field: 'title', title: '标题', width:400, templet:function(d){
                        return '<a target="_blank" href="'+d.videoUrl+'">'+d.title+'</a>';
                    }}
                , {field: 'sourceUrl', title: '视频地址', hide:true}
                , {
                    field: 'coverImg', title: '封面图地址',
                    templet: '<div><a href="{{d.coverImg}}" target="_blank">{{d.coverImg}}</a></div>'
                }
                , {
                    field: 'videoUrl', title: '视频地址', hide:true,
                    templet: '<div><a href="{{d.videoUrl}}" target="_blank">{{d.videoUrl}}</a></div>'
                }
                , {field: 'checkState', title: '审核状态', templet:function(d){
                    switch (d.checkState) {
                        case 0:
                            return '<span class="layui-badge layui-bg-orange">待审核</span>';
                        case 1:
                            return '<span class="layui-badge layui-bg-green">已审核</span>';
                        case -1:
                            return '<span class="layui-badge layui-bg-red">已驳回</span>';
                    }
                    }}
                , {
                    field: 'type', align: 'center', title: '视频来源', hide:true, templet: function (d) {
                        switch (d.type) {
                            case 1:
                                return '<span class="layui-badge layui-bg-green">APP上传</span>';
                            case 2:
                                return '<span class="layui-badge layui-bg-orange">APP替换</span>';
                            case 3:
                                return '<span class="layui-badge layui-bg-blue">后台上传</span>';
                            default:
                                return d.type;

                        }
                    }
                }
                , {field: 'viewCount', title: '观看次数'}
                , {field: 'forwardCount', title: '转发人数'}
                , {field: 'peopleCount', title: '观看人数'}
                , {field: 'cachedFlag', title: '缓存状态', hide:true}
                , {
                    field: 'createdTime', title: '创建时间', minWidth: 110, templet: function (d) {
                        return util.toDateString(d.createdTime, "yyyy-MM-dd");
                    }
                }
                , {fixed: 'right', title: '操作', toolbar: '#lineBar', width: 150}
            ]]
            , page: true
        });

        //头工具栏事件
        table.on('toolbar(tableList)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            switch (obj.event) {
                case 'isAll':
                    layer.msg(checkStatus.isAll ? '全选' : '未全选');
                    break;
                case 'refresh':
                    var tableSearch = $('#tableSearch');
                    var tableSelect = $('#tableSelect');
                    table.reload('id', {
                        page: {
                            curr: datagrid.config.page.curr
                        }
                        , where: {
                            type: tableSelect.val(),
                            search: tableSearch.val()
                        }
                    }, 'data');
                    break;
                case 'batchCheckStatePass':
                    var data = checkStatus.data;
                    if (data.length === 0) {
                        layer.alert('未勾选操作记录');
                        return;
                    }
                    layer.confirm('确认批量审核通过' + data.length + '条记录?', function (index) {
                        var ids = [];
                        $.each(data, function (index, item) {
                            ids.push(item.id)
                        });
                        layer.close(index);
                        video_batch_check_pass(ids, function () {
                            datagrid.reload('tableList');
                        });
                    });
                    break;
            }
        });

        //监听行工具事件
        table.on('tool(tableList)', function (obj) {
            var data = obj.data;
            if (obj.event === 'checkPass') {
                video_check_pass(data.id,function () {
                    datagrid.reload('tableList');
                });
            } else if(obj.event === 'checkReject') {
                video_check_reject(data.id,function () {
                    datagrid.reload('tableList');
                });
            }
        });




        $('.tableSearch .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
    });


    function video_batch_check_pass(ids, callback) {
        //发异步批量审核通过
        $.post("${contextPath}/admin/video/batchCheckVideoPass", {"ids": ids}, function (res) {
            if (res.state === 1) {
                layer.msg('操作成功!', {icon: 1, time: 1000});
                if (callback !== null && callback !== undefined) {
                    callback()
                }
            }
        });
    }

    function video_check_pass(id, callback) {
        $.post("${contextPath}/admin/video/checkVideoPass", {"id": id}, function (res) {
            layer.msg('操作成功!', {icon: 1, time: 1000});
            if (callback !== null && callback !== undefined) {
                callback()
            }
        });
    }

    function video_check_reject(id,callback) {
        layer.prompt({
            formType: 2,
            title: '驳回原因',
            area: ['400px', '150px'] //自定义文本域宽高
        }, function(value, index, elem){
            $.post("${contextPath}/admin/video/checkVideoReject", {"id": id, "checkStateRemark":value}, function (res) {
                layer.msg('操作成功!', {icon: 1, time: 1000});
                if (callback !== null && callback !== undefined) {
                    callback()
                }
            });
            layer.close(index);
        });
    }

</script>
</body>

</html>