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
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>角色管理</title>
    <meta name="renderer" content="webkit">
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

    <![endif]-->
</head>

<body>

<div class="x-body">

    <xblock>
        <button class="layui-btn layui-btn-normal" onclick="x_admin_show('增加角色','<%=path%>/admin/sys/sysRoleEdit')"><i class="layui-icon"></i>增加</button>
    </xblock>
    <table class="layui-table" id="table-order">
        <thead>
        <tr>
            <th style="width:12%;">角色名称</th>
            <th style="width:14%;">角色描述</th>
            <th style="width:7%;">状态</th>
            <th style="width:6%;text-align: center;">编辑</th>
            <th style="width:6%;text-align: center;">删除</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="role" items="${roleList}">
        <tr>
            <td>${role.roleName}</td>
            <td>${role.roleDesc}</td>
            <td>${role.stateStr}</td>
            <td style="text-align: center;">
                <a title="编辑" onclick="x_admin_show('编辑角色','<%=path%>/admin/sys/sysRoleEdit?id=${role.id}')" href="javascript:;">
                    <i class="icon iconfont">&#xe69e;</i>
                </a>
            </td>
            <td style="text-align: center;">
                <a title="删除" onclick="user_del(this,'${role.id}')" href="javascript:;">
                    <i class="layui-icon">&#xe640;</i>
                </a>
            </td>
        </tr>
        </c:forEach>
        </tbody>
    </table>

</div>
<script>




    /*订单-删除*/
    function user_del(obj,id){
        layer.confirm('确认要删除该角色吗？',function(index){
            //发异步删除数据
            $.post("${contextPath}/admin/sys/sysRoleDelete",{"id":id},function(data) {
                if (data == "success"){
                    $(obj).parents("tr").remove();
                    layer.msg('已删除!',{icon:1,time:1000});
                }
            });

        });
    }


</script>
</body>

</html>