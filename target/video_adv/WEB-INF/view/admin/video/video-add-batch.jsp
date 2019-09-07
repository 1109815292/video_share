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
    <title>视频管理</title>
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
    <form class="layui-form">
        <div class="layui-form-item layui-form-text">
            <label for="L_sourceUrl" class="layui-form-label">视频源地址</label>
            <div class="layui-input-block">
                <textarea id="L_sourceUrl" name="sourceUrl" placeholder="视频源地址（请以回车换行作为记录分割）"
                          class="layui-textarea"></textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">
            </label>
            <div class="layui-input-inline">
                <button id="L_btn_analysis" type="button" class="layui-btn">解析视频源</button>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">微信用户</label>
            <div class="layui-input-inline">
                <select id="L_userId" name="userId" lay-verify="required" lay-search="">
                    <option value="">直接选择或搜索选择</option>
                    <c:forEach var="item" items="${adminUsers}" varStatus="status">
                        <option value="${item.id}" <c:if
                                test="${not empty video.userId and item.id eq video.userId}"> selected </c:if> >${item.userName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">
            </label>
            <div class="layui-input-block">
                <table class="layui-hide" id="table" lay-filter="table"></table>

                <script type="text/html" id="lineBar">
                    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i
                            class="layui-icon">&#xe640;</i>删除</a>
                </script>
            </div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label">
            </label>
            <button id="L_btn_save" type="button" class="layui-btn layui-btn-disabled" lay-filter="add" lay-submit="">
                保存
            </button>
        </div>
    </form>
</div>
<script>

    var storageData = {};
    storageData.videos = [];

    $(function () {
        $('#L_btn_analysis').on('click', analysis);
    });

    function analysis() {
        $.ajax({
            url: '${contextPath}/admin/video/analysisVideoSourceUrl',
            type: 'POST',
            data: {
                content: $('#L_sourceUrl').val()
            },
            success: function (res) {
                if (res.state === 1) {
                    storageData.videos.length = 0;
                    $.each(res.data, function (index, item) {
                        var video = {};
                        video.tempId = index + 1;
                        video.title = item.text;
                        video.coverImg = item.cover;
                        video.videoUrl = item.playAddr;
                        video.sourceUrl = item.sourceUrl;
                        video.sort = 0;
                        storageData.videos.push(video);
                    });
                    dataGrid.reload({data: storageData.videos});
                    lockData();
                }
            },
            error: function (res) {
                console.log(res);
            }
        });
    }

    function lockData() {
        $('#L_sourceUrl').attr("readonly", "readonly");
        $('#L_btn_save').removeClass('layui-btn-disabled');
        $('#L_btn_analysis').text('解除锁定');
        $('#L_btn_analysis').addClass('layui-btn-danger');
        $('#L_btn_analysis').off("click").on("click", unlockData);
    }

    function unlockData() {
        $('#L_sourceUrl').removeAttr("readonly");
        $('#L_btn_save').addClass('layui-btn-disabled');
        $('#L_btn_analysis').text('解析视频源');
        $('#L_btn_analysis').removeClass('layui-btn-danger');
        $('#L_btn_analysis').off("click").on("click", analysis);
    }

    var dataGrid;
    layui.use(['form', 'table'], function () {
        $ = layui.jquery;

        var form = layui.form
            , table = layui.table;

        dataGrid = table.render({
            elem: '#table'
            , cellMinWidth: 70
            , data: []
            , cols: [[
                {field: 'title', edit: 'text', title: '标题'}

                , {field: 'sourceUrl', title: '视频源地址'}

                , {field: 'tag', title: '标签', edit: 'text'}

                , {field: 'sharedWords', title: '分享描述', edit: 'text'}

                , {field: 'sort', title: '排序', edit: 'text'}

                , {fixed: 'right', title: '操作', toolbar: '#lineBar', width: 150}

            ]]
        });

        //监听提交
        form.on('submit(add)', function (data) {
            storageData.userId = $('#L_userId').val();
            //发异步，把数据提交给后台
            $.ajax({
                url: '${contextPath}/admin/video/saveVideoBatch',
                type: 'POST',
                contentType: 'application/json;charset=utf-8',
                data: JSON.stringify(storageData),
                success: function (res) {
                    if (res.state === 1) {
                        setTimeout(function () {
                            window.parent.location.reload();//修改成功后刷新父界面
                        }, 100);
                    }
                },
                error: function (res) {
                    console.log(res);
                }
            });
            return false;
        });

        //监听单元格编辑
        table.on('edit(table)', function (obj) {

        });

        //监听行工具事件
        table.on('tool(table)', function (obj) {
            if (obj.event === 'del') {
                $.each(storageData.videos, function (index, item) {
                    if (item.tempId === obj.data.tempId) {
                        storageData.videos.splice(index, 1);
                        return false;
                    }
                });
                obj.del();
                if (storageData.videos.length === 0) {
                    unlockData();
                }
            }
        });
    });

</script>
</body>

</html>