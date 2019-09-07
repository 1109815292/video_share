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
    <title>秒杀添加</title>
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
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
<div class="x-body">
    <form class="layui-form" action="" method="post">

        <div class="layui-form-item">
            <button class="layui-btn layui-btn-sm" onclick="x_admin_show('关联店铺','<%=path%>/admin/video/videoEdit')"><i
                    class="layui-icon"></i>关联店铺
            </button>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">
                秒杀商品名
            </label>
            <div class="layui-input-inline">
                <input type="text" id="name" name="name" required
                       autocomplete="off" class="layui-input" value="${oneSeckillGoods.name}">
            </div>
        </div>
        <div class="layui-form-item">
            <label  class="layui-form-label">
                商品简介
            </label>
            <div class="layui-input-inline">
                <input type="text" id="introduction" name="introduction" required
                       autocomplete="off" class="layui-input" value="${oneSeckillGoods.introduction}">
            </div>
        </div>
        <div class="layui-form-item">
            <label  class="layui-form-label">
                商品数量
            </label>
            <div class="layui-input-inline">
                <input type="number" id="num" name="num" required
                       autocomplete="off" class="layui-input" value="${oneSeckillGoods.num}">
            </div>
        </div>
        <div class="layui-form-item">
            <label  class="layui-form-label">
                单人限购
            </label>
            <div class="layui-input-inline">
                <input type="number" id="quota" name="quota" required
                       autocomplete="off" class="layui-input" value="${oneSeckillGoods.quota}">
            </div>
        </div>
        <div class="layui-form-item">
            <label  class="layui-form-label">
                原价
            </label>
            <div class="layui-input-inline">
                <input type="text" id="price" name="price" required
                       autocomplete="off" class="layui-input" value="${oneSeckillGoods.price}">
            </div>
        </div>
        <div class="layui-form-item">
            <label  class="layui-form-label">
                秒杀价
            </label>
            <div class="layui-input-inline">
                <input type="text" id="costPrice" name="costPrice" required
                       autocomplete="off" class="layui-input" value="${oneSeckillGoods.costPrice}">
            </div>
        </div>
        <div class="layui-form-item">
            <label  class="layui-form-label">
                运营商分成
            </label>
            <div class="layui-input-inline">
                <input type="text" id="separate" name="separate" required
                       autocomplete="off" class="layui-input" value="${oneSeckillGoods.separate}">
            </div>
        </div>
        <div id="layui-form-item">
            <form action="" id="uploadVideo" method="post" enctype="multipart/form-data">
                <div class="label">上传视频</div>
                <div class="videoEdit-cont">
                    <div class="advPic productWidth" style="padding-bottom:30px;">
                        <label>
                            <input style="position:absolute;opacity:0;width:110px;" accept="video/*" mutiple="mutiple"
                                   onchange="uploadVideo(this)" type="file" id="file-video"
                                   name="uploadFile">
                            <div class="uploadVideoHint"></div>
                            <c:choose>
                                <c:when test="${not empty videoStore.videoUrl && videoStore.videoUrl != ''}">
                                    <img id="img-camera" src="<%=path%>/statics/img/stationmaster/camera_fill.png">
                                </c:when>
                                <c:otherwise>
                                    <img id="img-camera" src="<%=path%>/statics/img/stationmaster/camera.png">
                                </c:otherwise>
                            </c:choose>
                        </label>
                    </div>
                </div>
            </form>
        </div>

        <div class="layui-form-item">
            <label  class="layui-form-label">
                电话
            </label>
            <div class="layui-input-inline">
                <input type="text" id="phone" name="phone" required
                       autocomplete="off" class="layui-input" value="${oneSeckillGoods.phone}">
            </div>
        </div>
        <div class="layui-form-item">
            <label  class="layui-form-label">
                开始时间
            </label>
            <div class="layui-input-inline">
                <input type="datetime-local" id="startTime" name="startTime" required
                       autocomplete="off" class="layui-input" value="${oneSeckillGoods.startTime}">
            </div>
        </div>
        <div class="layui-form-item">
            <label  class="layui-form-label">
                结束时间
            </label>
            <div class="layui-input-inline">
                <input type="datetime-local" id="endTime" name="endTime" required
                       autocomplete="off" class="layui-input" value="${oneSeckillGoods.endTime}">
            </div>
        </div>
        <div class="layui-form-item">
            <label  class="layui-form-label">
                消费截止时间
            </label>
            <div class="layui-input-inline">
                <input type="datetime-local" id="consumeTime" name="consumeTime" required
                       autocomplete="off" class="layui-input" value="${oneSeckillGoods.consumeTime}">
            </div>
        </div>
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
    $(function () {
        $("#sel-role").val($("#inp-role").val());
        $("#sel-state").val($("#inp-state").val());
        renderForm();
    })
    function renderForm() {
        layui.use('form', function() {
            var form = layui.form; //高版本建议把括号去掉，有的低版本，需要加()
            form.render();
        });
    }

    layui.use(['form','layer'], function(){
        $ = layui.jquery;
        var form = layui.form
            ,layer = layui.layer;

        //自定义验证规则
        form.verify({
            nikename: function(value){
                if(value.length < 5){
                    return '昵称至少得5个字符啊';
                }
            }
            ,pass: [/(.+){6,12}$/, '密码必须6到12位']
            ,repass: function(value){
                if($('#L_pass').val()!=$('#L_repass').val()){
                    return '两次密码不一致';
                }
            }
        });

        //监听提交
        form.on('submit(add)', function(data){
            //发异步，把数据提交给后台
            $.post("${contextPath}/admin/seckillGoods/addSeckillDate",data.field,function(data){
                if(data == "success"){
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