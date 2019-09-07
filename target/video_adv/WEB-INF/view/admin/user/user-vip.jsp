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
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>用户管理</title>
    <meta name="renderer" content="webkit">
    <meta name="referer" content="never">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
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
        <input type="hidden" id="L_id" name="userId" value="${user.id}" >
        <div class="layui-form-item">
            <label class="layui-form-label">视推号</label>
            <div class="layui-input-block">
                <input type="text" disabled
                       autocomplete="off" class="layui-input" value="${user.copyNo}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">昵称</label>
            <div class="layui-input-block">
                <input type="text"  disabled
                       autocomplete="off" class="layui-input" value="${user.userName}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">是否会员</label>
            <div class="layui-input-block">
                <input type="text"  disabled
                       autocomplete="off" class="layui-input" value="${user.vipFlag}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">会员到期时间</label>
            <div class="layui-input-block">
                <input type="text"  disabled
                       autocomplete="off" class="layui-input" value="${user.vipDeadline}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">会员类型</label>
            <div class="layui-input-inline">
                <select name="vipTypeId" lay-verify="required">
                    <c:forEach var="item" items="${vipList}" varStatus="status">
                        <option value="${item.id}">${item.title}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">类型</label>
            <div class="layui-input-block">
                <%--<input type="radio" name="type" value="2" lay-filter="type" title="后台代充" checked="">--%>
                <input type="radio" name="type" value="3" lay-filter="type" title="后台赠送" checked>
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <textarea placeholder="请输入内容" id="L_remark" name="remark" class="layui-textarea"></textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">
            </label>
            <div class="layui-input-inline">
                <button type="button" id="L_btn_save" class="layui-btn" lay-filter="add" lay-submit=""> 保存</button>
            </div>
        </div>
    </form>

</div>
<script>
    $(function () {
        var title = $("input[name='type']:checked").attr("title");
        $('#L_remark').text(title);

    });

    layui.use(['form'], function () {
        var form = layui.form;

        form.on('radio(type)', function (obj) {
            console.log(obj);
            $('#L_remark').text($('input[name="type"]:checked').attr("title"));
        });

        //监听提交
        form.on('submit(add)', function(data){
            var submitBtn = $(data.elem);
            if (submitBtn.hasClass('layui-btn-disabled')) {
                return false;
            }

            //发异步，把数据提交给后台
            $.post("${contextPath}/admin/user/settingVip",data.field,function(data){
                if(data.state === 1){
                    layer.msg("保存成功！", {icon: 6});
                    setTimeout(function(){
                        window.parent.location.reload();//修改成功后刷新父界面
                    }, 100);

                }
            });
            return false;
        });
    });

</script>
</body>

</html>