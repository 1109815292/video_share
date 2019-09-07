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
    <title>渠道管理-编辑</title>
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
    <form class="layui-form" action="" method="post">
        <input type="hidden" name="id" value="${user.id}">
        <div class="layui-form-item">
            <label class="layui-form-label">
                登 录 名
            </label>
            <div class="layui-input-inline">
                <input type="text" id="L_email" name="userName" <c:if test="${empty user or empty user.id}">required</c:if> <c:if test="${not empty user and not empty user.id}">disabled</c:if>
                       autocomplete="off" class="layui-input" value="${user.userName}">
            </div>
        </div>
        <c:if test="${empty user or empty user.id}">
            <div class="layui-form-item">
                <label class="layui-form-label">
                    密码
                </label>
                <div class="layui-input-inline">
                    <input type="text" id="L_password" name="password" required
                           autocomplete="off" class="layui-input">
                </div>
            </div>
        </c:if>
        <div class="layui-form-item">
            <label for="L_email" class="layui-form-label">
                用户姓名
            </label>
            <div class="layui-input-inline">
                <input type="text" id="L_fullName" name="fullName" required
                       autocomplete="off" class="layui-input" value="${user.fullName}">
            </div>
        </div>
        <div class="layui-form-item">
            <label for="L_email" class="layui-form-label">
                电话
            </label>
            <div class="layui-input-inline">
                <input type="text" id="L_mobile" name="mobile" required
                       autocomplete="off" class="layui-input" value="${user.mobile}">
            </div>
        </div>
        <c:if test="${sessionScope.session_admin_user_type eq 1}">
            <div class="layui-form-item">
                <label class="layui-form-label">所属合伙人</label>
                <div class="layui-input-inline">
                    <select name="parentId" lay-verify="required" <c:if test="${not empty user.id}">disabled</c:if>   lay-search="">
                        <option value="">直接选择或搜索选择</option>
                        <c:forEach var="item" items="${partners}" varStatus="status">
                            <option value="${item.id}" <c:if test="${not empty user.parentId and item.id eq user.parentId}"> selected </c:if> >${item.userName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </c:if>
        <div class="layui-form-item">
            <label class="layui-form-label">
            </label>
            <button lay-filter="add" lay-submit="" class="layui-btn" >
                保存
            </button>
        </div>

    </form>

</div>
<script>
    var layer;
    layui.use(['form','layer'], function(){
        $ = layui.jquery;
        var form = layui.form;
        layer = layui.layer;

        //监听提交
        form.on('submit(add)', function(data){
            var submitBtn = $(data.elem);
            if (submitBtn.hasClass('layui-btn-disabled')) {
                return false;
            }

            var pathName = window.document.location.pathname;
            var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
            //发异步，把数据提交给后台
            $.post("${contextPath}/admin/chan/saveChanUserEdit",data.field,function(data){
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