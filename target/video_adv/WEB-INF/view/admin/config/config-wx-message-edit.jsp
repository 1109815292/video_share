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
        <div class="layui-form-item">
            <label class="layui-form-label">标题</label>
            <div class="layui-input-block">
                <input type="text"  name="title"
                       autocomplete="off" class="layui-input" value="${config.title}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">消息类型</label>
            <div class="layui-input-block">
                <select name="type" <c:if test="${not empty config.id}">disabled</c:if>   lay-search="">
                    <option value="">直接选择或搜索选择</option>
                    <option value="1" <c:if test="${config.type eq 1}"> selected </c:if> >客服消息</option>
                    <option value="2" <c:if test="${config.type eq 2}"> selected </c:if> >模板消息</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">触发位置</label>
            <div class="layui-input-block">
                <select name="triggerPosition" lay-verify="required" <c:if test="${not empty config.id}">disabled</c:if>   lay-search="">
                    <option value="">直接选择或搜索选择</option>
                    <optgroup label="客服消息">
                        <option value="11" <c:if test="${config.triggerPosition eq 11}"> selected </c:if> >关注公众号</option>
                        <option value="12" <c:if test="${config.triggerPosition eq 12}"> selected </c:if> >扫二维码</option>
                    </optgroup>
                    <optgroup label="模板消息">
                        <option value="21" <c:if test="${config.triggerPosition eq 21}"> selected </c:if> >新用户注册</option>
                    </optgroup>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">内容</label>
            <div class="layui-input-block">
                <textarea name="content" class="layui-textarea">${config.content}</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">模板ID</label>
            <div class="layui-input-block">
                <input type="text"  name="templateId" <c:if test="${not empty config.id}">disabled</c:if>
                       autocomplete="off" class="layui-input" value="${config.templateId}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">模板数据</label>
            <div class="layui-input-block">
                <textarea class="layui-textarea" name="templateData">${config.templateData}</textarea>
            </div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label">模板外链</label>
            <div class="layui-input-block">
                <input type="text"  name="templateUrl"
                       autocomplete="off" class="layui-input" value="${config.templateUrl}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">客服消息类型</label>
            <div class="layui-input-block">
                <select name="msgType" <c:if test="${not empty config.id}">disabled</c:if> >
                    <option value="">直接选择或搜索选择</option>
                    <option value="1" <c:if test="${config.msgType eq 1}"> selected </c:if> >文本消息</option>
                    <option value="2" <c:if test="${config.msgType eq 2}"> selected </c:if> >图片消息</option>
                    <option value="3" <c:if test="${config.msgType eq 3}"> selected </c:if> >图文消息</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">媒体数据ID</label>
            <div class="layui-input-block">
                <input type="text"  name="mediaId"
                       autocomplete="off" class="layui-input" value="${config.mediaId}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">图文消息描述</label>
            <div class="layui-input-block">
                <textarea class="layui-textarea" name="newsDesc">${config.newsDesc}</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">图文消息外链</label>
            <div class="layui-input-block">
                <input type="text"  name="newsUrl"
                       autocomplete="off" class="layui-input" value="${config.newsUrl}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">图文消息图片</label>
            <div class="layui-input-block">
                <input type="text"  name="newsPicUrl"
                       autocomplete="off" class="layui-input" value="${config.newsPicUrl}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">排序号</label>
            <div class="layui-input-block">
                <input type="text"  name="sort"
                       autocomplete="off" class="layui-input" value="${config.sort}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">
            </label>
            <button lay-filter="add" lay-submit="" class="layui-btn" >
                保存
            </button>
            <button lay-filter="test" lay-submit="" class="layui-btn" >
                测试
            </button>
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