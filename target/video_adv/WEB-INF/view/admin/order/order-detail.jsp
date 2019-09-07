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
    <title>订单管理</title>
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
        <div class="layui-form-item">
            <label class="layui-form-label">订单编号</label>
            <div class="layui-input-block">
                <input type="text"  name="pageName" disabled
                       autocomplete="off" class="layui-input" value="${order.orderNo}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">订单金额</label>
            <div class="layui-input-block">
                <input type="text"  name="companyName" disabled
                       autocomplete="off" class="layui-input" value="${order.amount}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">支付类型</label>
            <div class="layui-input-block">
                <input type="text"  name="name" disabled
                       autocomplete="off" class="layui-input" value="${order.payType}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">支付时间</label>
            <div class="layui-input-block">
                <input type="text"  name="mobile" disabled
                       autocomplete="off" class="layui-input" value="${order.payTime}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">订单对象类型</label>
            <div class="layui-input-block">
                <input type="text"  name="wx" disabled
                       autocomplete="off" class="layui-input" value="${order.objectType}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">订单对象ID</label>
            <div class="layui-input-block">
                <input type="text"  name="sign" disabled
                       autocomplete="off" class="layui-input" value="${order.objectId}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">支付状态</label>
            <div class="layui-input-block">
                <input type="text"  name="sign" disabled
                       autocomplete="off" class="layui-input" value="${order.state}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">预支付原始数据</label>
            <div class="layui-input-block">
                 <textarea class="layui-textarea">${order.prepayRawData}</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">支付回调原始数据</label>
            <div class="layui-input-block">
                <textarea class="layui-textarea">${order.callbackRawData}</textarea>
            </div>
        </div>
    </form>

</div>
<script>
    $(function () {
        $("#sel-role").val($("#inp-role").val());
        $("#sel-state").val($("#inp-state").val());
        renderForm();

        $('#L_btn_analysis').on("click", analysis);
    });

    function analysis() {
        $.ajax({
            url: '${contextPath}/admin/video/analysisVideoSourceUrl',
            type:'POST',
            dataType:'json',
            data:{
                content:$('#L_sourceUrl').val()
            },
            success:function(res){
                if (res.state === 1) {
                    bindData(res.data[0]);
                    lockData();
                } else {
                    layer.alert("解析异常,请确认视频源地址");
                }
            }
        });
    }

    function lockData() {
        $('#L_sourceUrl').attr("readonly","readonly");
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

    function bindData(data) {
        $('#L_title').val(data.text);
        $('#L_coverImg').val(data.cover);
        $('#L_videoUrl').val(data.playAddr);
        $('#L_sourceUrl').val(data.sourceUrl);
    }



    function renderForm() {
        layui.use('form', function() {
            var form = layui.form; //高版本建议把括号去掉，有的低版本，需要加()
            form.render();
        });
    }

    var layer;
    layui.use(['form','layer'], function(){
        $ = layui.jquery;
        var form = layui.form;
        layer = layui.layer;

        //自定义验证规则
        form.verify({


        });



        //监听提交
        form.on('submit(add)', function(data){
            var submitBtn = $(data.elem);
            if (submitBtn.hasClass('layui-btn-disabled')) {
                return false;
            }

            //发异步，把数据提交给后台
            $.post("${contextPath}/admin/video/saveVideoEdit",data.field,function(data){
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