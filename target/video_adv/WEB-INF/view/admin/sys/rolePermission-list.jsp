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

    <xblock>
        <button class="layui-btn layui-btn-normal" onclick="x_admin_show('增加权限','<%=path%>/admin/sys/sysRolePermissionEdit')"><i class="layui-icon"></i>增加</button>
    </xblock>

    <div class="layui-row" style="margin-bottom:10px;">
        <div class="layui-input-inline">
            <select name="contrller" id="sel-role"  lay-search style="height: 38px;" onchange="datareload()">
                <option value="">选择角色</option>
                <c:forEach var="role" items="${roleList}">
                    <option value="${role.id}">${role.roleName}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <table class="layui-hide" id="tablelist" lay-data="{id: 'idTest'}"></table>

</div>
<script>
    var datagrid;
    layui.use(['util','table'], function(){
        var table = layui.table;
        var util = layui.util;

        datagrid = table.render({
            elem: '#tablelist'
            ,url: '${contextPath}/admin/sys/getSysRolePermission'
            ,cellMinWidth: 80
            , id: 'idTest'
            ,cols: [[
                {field:'menuDesc', title: '菜单'}
                ,{field:'isGrant', title: '使用权限'}
                ,{field:'id', align:'center', title: '删除', templet:'<div><a href=\'javascript:;\' onclick="permission_del(this,{{d.id}})"><i class=\'icon iconfont\'>&#xe69d;</i></a></div>'}
            ]]
        });
    });

    function datareload(){
        datagrid.reload({
            where:{roldId: $("#sel-role").val()}
            ,url: '${contextPath}/admin/sys/getSysRolePermission'
        });
    }



    /*订单-删除*/
    function permission_del(obj,id){
        layer.confirm('确认要删除该权限吗？',function(index){
            //发异步删除数据
            $.post("${contextPath}/admin/sys/sysRolePermissionDelete",{"id":id},function(data) {
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