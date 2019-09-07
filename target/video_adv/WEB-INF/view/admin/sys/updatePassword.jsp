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
    <title>欢迎页面-L-admin1.0</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" href="<%=path%>/statics/css/font.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/xadmin.css">
    <link rel="stylesheet" href="<%=path%>/statics/layui/css/layui.css">
    <link rel="stylesheet" href="<%=path%>/statics/lib/layui/css/modules/laydate/default/laydate.css">

    <script src="<%=path%>/statics/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=path%>/statics/layui/layui.all.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=path%>/statics/js/xadmin.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
<div class="x-body">
    <form action="<%=path%>/admin/sys/saveNewPassword" method="post" onsubmit="return check()">
        <div class="layui-form-item" style="margin-top:20px;">
            <label for="L_email" class="layui-form-label">
                原密码
            </label>
            <div class="layui-input-inline">
                <input type="password" id="L_email" name="password" required
                       autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label for="L_email" class="layui-form-label">
                新密码
            </label>
            <div class="layui-input-inline">
                <input type="password" id="L_pass" name="newPassword" required
                       autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label for="L_email" class="layui-form-label">
                确认密码
            </label>
            <div class="layui-input-inline">
                <input type="password" id="L_repass" name="repNewPassword" required
                       autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label for="" class="layui-form-label">
            </label>
            <button  class="layui-btn" id="btn-save">
                保存
            </button>
            <button type="button"  class="layui-btn layui-btn-primary" onclick="x_admin_close()">
                取消
            </button>
        </div>
    </form>
</div>
<script>


    layui.use(['form','layer'], function(){
        var form = layui.form
            ,layer = layui.layer;

        //自定义验证规则
        form.verify({
            password: function(value){
                if(value.length < 5){
                    return '昵称至少得5个字符啊';
                }
            }
            ,newPassword: [/(.+){6,12}$/, '密码必须6到12位']
            ,repNewPassword: function(value){
                if($('#L_pass').val()!=$('#L_repass').val()){
                    return '两次密码不一致';
                }
            }
        });

        //监听提交
        form.on('submit(add)', function(data){
            var newPass = $("#L_pass").val();
            var rePass = $("#L_repass").val();
            if (newPass != rePass){
                layer.msg("两次输入的密码不一致！", {icon: 6});
                return false;
            }

            //发异步，把数据提交给后台
            $.post("${contextPath}/admin/sys/saveNewPassword",data.field,function(data){
                if(data == "success"){
                    layer.msg("保存成功！", {icon: 6});
                    setTimeout(function(){
                        window.parent.location.reload();//修改成功后刷新父界面
                    }, 1000);

                }
            });

            return false;
        });

        $("#sel-classType").val($("#inp-classType").val());
        form.render();
    });


    function check() {
        var newPass = $("#L_pass").val();
        var rePass = $("#L_repass").val();
        if (newPass != rePass){
            layer.msg("两次输入的密码不一致！", {icon: 6});
            return false;
        }
    }

</script>
</body>

</html>