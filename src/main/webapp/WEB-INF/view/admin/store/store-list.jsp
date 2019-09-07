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
    <title>店铺管理</title>
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
                <option value="">所属行业:&nbsp;ALL</option>
                <c:forEach items="${industries}" var="industry">
                    <option value="${industry.id}">所属行业:&nbsp;${industry.industry}</option>
                </c:forEach>
            </select>
        </div>
        <div class="layui-inline">
            <input class="layui-input" id="tableSearch" placeholder="店铺名称" autocomplete="off">
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
    <script type="text/html" id="selectTpl">
        <div class="layui-input-inline">
            <select style="height:20px;" name="industry" lay-filter="industry" data-id="{{d.id}}" data-value="{{d.industryId}}">

            </select>
        </div>
    </script>
</div>
<script>

    var industryOptions = "<option value=''>未选择</option>\n";
    $.ajax({
        async: false,
        url: '${contextPath}/admin/industry/getIndustryData',
        type: 'get',
        success: function (res) {
            var data = res.data;
            for (var x in data) {
                industryOptions += '<option value = "' + data[x].id + '">' + data[x].industry + '</option>\n';
            }
        }
    });

    var datagrid;
    layui.use(['util', 'table', 'form'], function () {
        var table = layui.table;
        var util = layui.util;
        var form = layui.form;

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
                        industryId: tableSelect.val(),
                        search: tableSearch.val()
                    }
                }, 'data');
            }
        };

        datagrid = table.render({
            elem: '#tableList'
            , toolbar: '#toolbar'
            , url: '${contextPath}/admin/store/getStoreData'
            , cellMinWidth: 70
            , id: 'id'
            , done: function (res, curr, count) {
                // 渲染dictName列
                // 渲染之前组装select的option选项值
                $("select[name='industry']").html(industryOptions);
                layui.each($("select[name='industry']"), function (index, item) {
                    var elem = $(item);
                    elem.val(elem.data('value'));
                });
                form.render('select');
                $(".layui-table-box, .layui-table-col-special .layui-table-cell").css('overflow', 'visible');
                // $(".layui-table-body, .layui-table-box, .layui-table-col-special .layui-table-cell").css('overflow-x', 'scroll');
            }
            , cols: [[
                {field: 'id', title: 'ID'}
                , {field: 'storeName', title: '店铺名称', width:400}
                , {title: '分类', templet: '#selectTpl', width:160}
                , {field: 'viewCount', title: '观看次数'}
                , {field: 'peopleCount', title: '观看人数'}
                , {field: 'stationCopyNo', title: '所属站长视推号'}
                , {
                    field: 'createdTime', title: '创建时间', minWidth: 110, templet: function (d) {
                        return util.toDateString(d.createdTime, "yyyy-MM-dd");
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
            }
        });

        //监听行工具事件
        table.on('tool(tableList)', function (obj) {
            var data = obj.data;
        });


        form.on('select(industry)', function(data){
            // console.log(data.elem); //得到select原始DOM对象
            // console.log(data.value); //得到被选中的值
            var id = $(data.elem).attr("data-id");
            var industryId = data.value;
            changeIndustry(id, industryId);
            // layer.tips(this.value + ' ' + this.name + '：'+ obj.elem.checked, obj.othis);
        });

        $('.tableSearch .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
    });

    function changeIndustry(id, industryId) {
        $.post("${contextPath}/admin/store/changeIndustry", {"id": id, "industryId":industryId}, function (res) {
            if (res.state !== 1) {
                layer.msg('操作失败', {icon: 1, time: 1000});
            }
        });
    }


</script>
</body>

</html>