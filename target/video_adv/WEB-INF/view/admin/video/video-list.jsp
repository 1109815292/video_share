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
    <title>视频管理</title>
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
    <style type="text/css">
        .layui-table-cell {
            min-height: 38px;
            line-height: 38px;
        }
    </style>
</head>

<body>

<div class="x-body">

    <div class="tableSearch">
        <div class="layui-inline">
            <select id="tableSelect" class="layui-select">
                <option value="">视频来源:&nbsp;ALL</option>
                <option value="1">视频来源:&nbsp;APP上传</option>
                <option value="2">视频来源:&nbsp;APP替换</option>
                <option value="3">视频来源:&nbsp;后台上传</option>
            </select>
        </div>
        <div class="layui-inline">
            <select id="tableSelect2" class="layui-select">
                <option value="">视频分类:&nbsp;ALL</option>
                <c:forEach items="${categories}" var="category">
                    <option value="${category.id}">视频分类:&nbsp;${category.name}</option>
                </c:forEach>
                <option value="-1">视频分类:&nbsp;未分类</option>
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
            <button class="layui-btn layui-btn-sm" onclick="x_admin_show('增加视频','<%=path%>/admin/video/videoEdit')"><i
                    class="layui-icon"></i>增加
            </button>

            <button class="layui-btn layui-btn-sm"
                    onclick="x_admin_show('批量增加视频','<%=path%>/admin/video/videoAddBatch')"><i
                    class="layui-icon"></i>批量增加
            </button>

            <button class="layui-btn layui-btn-danger layui-btn-sm" lay-event="delVideo"><i
                    class="layui-icon">&#xe640;</i>批量删除
            </button>

            <button class="layui-btn layui-btn-warm layui-btn-sm" lay-event="recacheVideo"><i
                    class="layui-icon">&#xe666;</i>重新缓存
            </button>

            <button class="layui-btn layui-btn-primary layui-btn-sm" lay-event="refresh"><i
                    class="layui-icon">&#xe669;</i>刷新
            </button>
        </div>
    </script>

    <script type="text/html" id="lineBar">
        {{#  if(d.type === 3){ }}
        <a class="layui-btn layui-btn-xs" lay-event="edit"><i class="icon iconfont">&#xe69e;</i>编辑</a>
        <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i
                class="layui-icon">&#xe640;</i>删除</a>
        {{#  } }}

    </script>

    <script type="text/html" id="selectTpl">
        <div class="layui-input-inline">
            <select style="height:20px;" name="category" lay-filter="category" data-id="{{d.id}}" data-value="{{d.categoryId}}">

            </select>
        </div>
    </script>
</div>

<script>
    var categoryOptions = "<option value=''>未选择</option>\n";
    $.ajax({
        async: false,
        url: '${contextPath}/admin/category/getCategoryData',
        type: 'get',
        success: function (res) {
            var data = res.data;
            for (var x in data) {
                categoryOptions += '<option value = "' + data[x].id + '">' + data[x].name + '</option>\n';
            }
        }
    });

    var datagrid;
    layui.use(['util', 'table', 'form'], function () {
        var table = layui.table;
        var util = layui.util;
        var form = layui.form;

        datagrid = table.render({
            elem: '#tableList'
            , toolbar: '#toolbar'
            , url: '${contextPath}/admin/video/getVideoData'
            , cellMinWidth: 70
            , id: 'id'
            , done: function (res, curr, count) {
                // 渲染dictName列
                // 渲染之前组装select的option选项值
                $("select[name='category']").html(categoryOptions);
                layui.each($("select[name='category']"), function (index, item) {
                    var elem = $(item);
                    elem.val(elem.data('value'));
                });
                form.render('select');
                $(".layui-table-box, .layui-table-col-special .layui-table-cell").css('overflow', 'visible');
                // $(".layui-table-body, .layui-table-box, .layui-table-col-special .layui-table-cell").css('overflow-x', 'scroll');
            }
            , cols: [[
                {type: 'checkbox', fixed: 'left'},
                {field: 'id', title: 'ID'}
                , {
                    field: 'title', title: '标题', width: 400, templet: function (d) {
                        return '<a target="_blank" href="' + d.videoUrl + '">' + d.title + '</a>';
                    }
                }
                , {field: 'sourceUrl', title: '视频地址', hide: true}
                , {
                    field: 'coverImg', title: '封面图地址', hide: true,
                    templet: '<div><a href="{{d.coverImg}}" target="_blank">{{d.coverImg}}</a></div>'
                }
                , {
                    field: 'videoUrl', title: '视频地址', hide: true,
                    templet: '<div><a href="{{d.videoUrl}}" target="_blank">{{d.videoUrl}}</a></div>'
                }
                , {title: '分类', templet: '#selectTpl', width:160}
                , {
                    field: 'type', align: 'center', title: '视频来源',width:100, templet: function (d) {
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
                , {
                    field: 'checkState', title: '审核状态',width:100, templet: function (d) {
                        switch (d.checkState) {
                            case -1:
                                return '<span class="layui-badge layui-bg-red">驳回</span>';
                            case 0:
                                return '<span class="layui-badge layui-bg-orange">待审核</span>';
                            case 1:
                                return '<span class="layui-badge layui-bg-green">已通过</span>';
                            default:
                                return d.checkState;
                        }
                    }
                }
                , {field: 'viewCount', title: '观看次数'}
                , {field: 'forwardCount', title: '转发人数'}
                , {field: 'peopleCount', title: '观看人数'}
                , {field: 'cachedFlag', title: '缓存状态', hide: true}
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
                case 'getCheckData':
                    var data = checkStatus.data;
                    layer.alert(JSON.stringify(data));
                    break;
                case 'delVideo':
                    var data = checkStatus.data;
                    if (data.length === 0) {
                        layer.alert('未勾选操作记录');
                        return;
                    }
                    layer.confirm('确认删除' + data.length + '条记录?', function (index) {
                        var ids = [];
                        $.each(data, function (index, item) {
                            ids.push(item.id)
                        });
                        layer.close(index);
                        video_del(ids, function () {
                            datagrid.reload('tableList');
                        });
                    });
                    break;
                case 'recacheVideo':
                    var data = checkStatus.data;
                    if (data.length === 0) {
                        layer.alert('未勾选操作记录');
                        return;
                    }
                    layer.confirm('确认重新缓存' + data.length + '条记录?', function (index) {
                        var ids = [];
                        $.each(data, function (index, item) {
                            ids.push(item.id)
                        });
                        layer.close(index);
                        video_recache(ids, function () {
                            layer.alert('已开始重新缓存,请稍后刷新');
                        });
                    });
                    break;
                case 'isAll':
                    layer.msg(checkStatus.isAll ? '全选' : '未全选');
                    break;
                case 'refresh':
                    var tableSearch = $('#tableSearch');
                    var tableSelect = $('#tableSelect');
                    var tableSelect2 = $('#tableSelect2');
                    table.reload('id', {
                        page: {
                            curr: datagrid.config.page.curr
                        }
                        , where: {
                            type: tableSelect.val(),
                            search: tableSearch.val(),
                            categoryId:tableSelect2.val()
                        }
                    }, 'data');
                    break;
            }
        });

        //监听行工具事件
        table.on('tool(tableList)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                layer.confirm('确认删除操作吗？', function (index) {
                    var ids = [];
                    ids.push(data.id);
                    obj.del();
                    layer.close(index);
                    video_del(ids);

                });
            } else if (obj.event === 'edit') {
                x_admin_show('编辑', '<%=path%>/admin/video/videoEdit?id=' + data.id);
            }
        });

        form.on('select(category)', function(data){
            // console.log(data.elem); //得到select原始DOM对象
            // console.log(data.value); //得到被选中的值
            var id = $(data.elem).attr("data-id");
            var categoryId = data.value;
            changeCategory(id, categoryId);
            // layer.tips(this.value + ' ' + this.name + '：'+ obj.elem.checked, obj.othis);
        });

        var $ = layui.$, active = {
            reload: function () {
                var tableSearch = $('#tableSearch');
                var tableSelect = $('#tableSelect');
                var tableSelect2 = $('#tableSelect2');

                //执行重载
                table.reload('id', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    , where: {
                        type: tableSelect.val(),
                        search: tableSearch.val(),
                        categoryId:tableSelect2.val()
                    }
                }, 'data');
            }
        };

        $('.tableSearch .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
    });


    function video_del(ids, callback) {
        //发异步删除数据
        $.post("${contextPath}/admin/video/videoDelete", {"ids": ids}, function (res) {
            if (res.state === 1) {
                layer.msg('已删除!', {icon: 1, time: 1000});
                if (callback !== null && callback !== undefined) {
                    callback()
                }
            }
        });

    }

    function video_recache(ids, callback) {
        $.post("${contextPath}/admin/video/recache", {"ids": ids}, function (res) {
            if (res.state === 1) {
                if (callback !== null && callback !== undefined) {
                    callback()
                }
            }
        });
    }

    function changeCategory(id, categoryId) {
        $.post("${contextPath}/admin/video/changeCategory", {"id": id, "categoryId":categoryId}, function (res) {
            if (res.state !== 1) {
                layer.msg('操作失败', {icon: 1, time: 1000});
            }
        });
    }

</script>
</body>

</html>