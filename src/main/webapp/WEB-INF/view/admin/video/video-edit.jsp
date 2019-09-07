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
    <title>视频管理</title>
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
        <input type="hidden" id="L_id" name="id" value="${video.id}">
        <div class="layui-form-item layui-form-text">
            <label for="L_sourceUrl"  class="layui-form-label">视频源地址</label>
            <div class="layui-input-block">
                <textarea id="L_sourceUrl" name="sourceUrl" placeholder="视频源地址" class="layui-textarea" <c:if test="${not empty video.id}">readonly</c:if> >${video.sourceUrl}</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">
            </label>
            <c:choose>
                <c:when test="${not empty video.id}">
                    <div class="layui-input-inline">
                        <button type="button" class="layui-btn layui-btn-disabled" >解析视频源</button>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="layui-input-inline">
                        <button id="L_btn_analysis" type="button" class="layui-btn" >解析视频源</button>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">
                标题
            </label>
            <div class="layui-input-block">
                <input type="text" id="L_title" name="title" required
                       autocomplete="off" class="layui-input" value="${video.title}">
            </div>
        </div>

        <div class="layui-form-item">
            <label for="L_tag" class="layui-form-label">
                标签
            </label>
            <div class="layui-input-block">
                <input type="text" id="L_tag" name="tag"
                       autocomplete="off" class="layui-input" value="${video.tag}">
            </div>
        </div>
        <div class="layui-form-item">
            <label for="L_tag" class="layui-form-label">
                分享描述
            </label>
            <div class="layui-input-block">
                <input type="text" id="L_sharedWords" name="sharedWords"
                       autocomplete="off" class="layui-input" value="${video.sharedWords}">
            </div>
        </div>
        <div class="layui-form-item">
            <label for="L_tag" class="layui-form-label">
                视频分类
            </label>
            <div class="layui-input-inline">
                <select name="categoryId" lay-verify="required" lay-search="">
                    <option value="">直接选择或搜索选择</option>
                    <c:forEach var="item" items="${categories}" varStatus="status">
                        <option value="${item.id}" <c:if test="${not empty video.categoryId and item.id eq video.categoryId}"> selected </c:if> >${item.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">是否公开</label>
            <div class="layui-input-block">
                <c:choose>
                    <c:when test="${not empty video.id}">
                        <input type="checkbox"  <c:if test="${video.publicFlag eq 'Y'}">value="${video.publicFlag}" checked</c:if> name="publicFlag" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">
                    </c:when>
                    <c:otherwise>
                        <input type="checkbox" checked name="publicFlag" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
        <div class="layui-form-item">
            <label for="L_sort" class="layui-form-label">
                排序
            </label>
            <div class="layui-input-inline">
                <input type="text" id="L_sort" name="sort" required
                       autocomplete="off" class="layui-input" <c:choose><c:when test="${not empty video.sort }">value="${video.sort}"</c:when><c:otherwise>value="0"</c:otherwise></c:choose>>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">微信用户</label>
            <div class="layui-input-inline">
                <select name="userId" lay-verify="required" <c:if test="${not empty video.id}">disabled</c:if>   lay-search="">
                    <option value="">直接选择或搜索选择</option>
                    <c:forEach var="item" items="${adminUsers}" varStatus="status">
                        <option value="${item.id}" <c:if test="${not empty video.userId and item.id eq video.userId}"> selected </c:if> >${item.userName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label">
            </label>
            <div class="layui-input-inline">
                <button type="button" id="L_btn_save" class="layui-btn" lay-filter="add" lay-submit=""> 保存</button>
            </div>
        </div>
        <input type="hidden" id="L_coverImg" name="coverImg" value="${video.coverImg}">
        <input type="hidden" id="L_videoUrl" name="videoUrl" value="${video.videoUrl}">
        <input type="hidden" id="L_advType" name="advType" value="${video.advType}">
        <input type="hidden" id="L_advIds" name="advIds" value="${video.advIds}">
    </form>

</div>
<script>
    $(function () {
        $("#sel-role").val($("#inp-role").val());
        $("#sel-state").val($("#inp-state").val());
        renderForm();

        //如果id不存在，默认保存禁用
        var id = $('#L_id').val();
        if(id === null || id === undefined || id === "") {
            $('#L_btn_save').addClass('layui-btn-disabled');
        }

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

            var pathName = window.document.location.pathname;
            var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
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