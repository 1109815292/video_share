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
    <form class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">所属区域</label>
            <div class="layui-input-inline">
                <select id="select1" lay-filter="select1">
                    <option value="">请选择</option>
                    <c:forEach items="${list}" var="district">
                        <option value="${district.id}">${district.name}${district.suffix}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="layui-input-inline" style="display: none;">
                <select id="select2" lay-filter="select2"></select>
            </div>
            <div class="layui-input-inline"  style="display: none;">
                <select id="select3" lay-filter="select3"></select>
            </div>
        </div>

    </form>
</div>
<script type="text/javascript">

    layui.use(['form', 'layer'], function () {
        var form = layui.form;

        form.on('select(select1)', function (data) {
            select1(data.value, function () {
                form.render();
            });
        });


        form.on('select(select2)', function (data) {
            select2(data.value, function () {
                form.render();
            });
        });
    });

    function select1(pid, callback) {
        if (pid !== "") {
            $('#select3').parent().hide();
            $('#select3').empty();
            $.ajax({
                url: '${contextPath}/admin/district/getByParentId',
                type: 'get',
                data: {"pid": pid},
                success: function (res) {
                    if (res.state === 1) {
                        if(res.data.length > 0) {
                            $('#select2').parent().show();
                            $.each(res.data, function (index, item) {
                                if(index === 0) {
                                    $('#select2').empty();
                                    $('#select2').append('<option value="">请选择'+item.suffix+'</option>');
                                }
                                $('#select2').append('<option value="' + item.id + '">' + item.name + item.suffix + '</option>');
                            });
                            if (callback !== null && callback !== undefined)
                                callback();
                        }

                    }
                }
            });
        } else {
            $('#select2').parent().hide();
            $('#select2').empty();
            $('#select3').parent().hide();
            $('#select3').empty();
        }
    }

    function select2(pid, callback) {
        if (pid !== "") {
            $.ajax({
                url: '${contextPath}/admin/district/getByParentId',
                type: 'get',
                data: {"pid": pid},
                success: function (res) {
                    if (res.state === 1) {
                        if(res.data.length > 0) {
                            $('#select3').parent().show();
                            $.each(res.data, function (index, item) {
                                if(index === 0) {
                                    $('#select3').empty();
                                    $('#select3').append('<option value="">请选择'+item.suffix+'</option>');
                                }
                                $('#select3').append('<option value="' + item.id + '">' + item.name + item.suffix + '</option>');
                            });
                        }
                        if (callback !== null && callback !== undefined)
                            callback();
                    }
                }
            });
        }else {
            $('#select3').parent().hide();
            $('#select3').empty();
        }
    }

</script>
</body>

</html>