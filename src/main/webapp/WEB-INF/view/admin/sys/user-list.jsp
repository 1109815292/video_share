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
    <title>用户管理</title>
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
        <button class="layui-btn layui-btn-normal" onclick="x_admin_show('增加用户','<%=path%>/admin/sys/sysUserEdit')"><i class="layui-icon"></i>增加</button>
    </xblock>

    <table class="layui-hide" id="tablelist" lay-filter="tablelist"></table>
    <script type="text/html" id="stateStr">
        {{#  if(d.state == 1){ }}
        正常
        {{#  } else if(d.state == 9) { }}
        禁用
        {{#  } }}
    </script>
</div>
<script>

    var datagrid;
    layui.use(['util','table'], function(){
        var table = layui.table;
        var util = layui.util;

        datagrid = table.render({
            elem: '#tablelist'
            ,url:'${contextPath}/admin/sys/getSysUserData'
            ,cellMinWidth: 70
            , id: 'id'
            ,cols: [[
                {field:'userName', title: '登录名'}
                ,{field:'fullName', title: '用户姓名'}
                ,{field:'mobile', title: '电话'}
                ,{field:'roleName', title: '角色'}
                ,{title: '状态', templet: "#stateStr"}
                ,{field:'createdTime',title: '创建时间',minWidth:110, templet:function (d) {
                        return util.toDateString(d.createdTime, "yyyy-MM-dd");
                    }}
                ,{fixed: 'right', align:'center', width:60, title: '修改', templet:'<div><a title="编辑" onclick="x_admin_show(\'编辑用户\',\'<%=path%>/admin/sys/sysUserEdit?id={{d.id}}\')" href="javascript:;">\n' +
                    '                <i class="icon iconfont">&#xe69e;</i>\n' +
                    '                </a></div>'}
                ,{fixed: 'right', align:'center', width:60, title: '删除', templet:'<div><a title="删除" onclick="user_del(this,\'{{d.id}}\')" href="javascript:;">\n' +
                    '                    <i class="layui-icon">&#xe640;</i>\n' +
                    '                </a></div>'}
            ]]
            ,page: true
        });
    });

    /*用于查询刷新*/
    function datareload(){
        datagrid.reload({
            where:{keywords: $("#inp-keywords").val()}
            ,url: '${contextPath}/admin/sys/getSysUserData'
        });
    }


    /*删除*/
    function user_del(obj,id){
        layer.confirm('确认要删除该用户吗？',function(index){
            //发异步删除数据
            $.post("${contextPath}/admin/sys/sysUserDelete",{"id":id},function(data) {
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