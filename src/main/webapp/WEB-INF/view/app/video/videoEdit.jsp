<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/15 0015
  Time: 15:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>编辑视频</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="https://vjs.zencdn.net/7.5.5/video-js.css" rel="stylesheet">
    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link href="<%=path%>/statics/layui/css/layui.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=path%>/statics/css/xadmin.css">
    <link rel="stylesheet" href="<%=path%>/statics/layui/css/layui.css">

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->
    <script src="https://vjs.zencdn.net/ie8/1.1.2/videojs-ie8.min.js"></script>

</head>
<body class="bg-square">

<div class="topWarning">
    请勿带有色情、反动、敏感词、禁用词汇等违法信息。
</div>
<div class="cont" style="padding-bottom:100px;">
<form class="layui-form" lay-filter="videoEdit-form" action="<%=path%>/app/video/saveVideo" method="post" onsubmit="return check()">
    <div class="videoEdit-inp-group">
        <div class="label">标题 <span>(必填)</span></div>
        <div class="videoEdit-cont">
            <textarea rows="5" id="title" name="title">${video.text}</textarea>
        </div>
    </div>
    <div class="videoEdit-inp-group">
        <div class="label">插入广告</div>
        <div class="advBtn">
            <button type="button" class="layui-btn layui-btn-danger layui-btn-radius" onclick="selectAdv()">选择广告</button>
        </div>
        <input type="hidden" id="inp-advType" name="advType" value="${video.advType}"/>
        <input type="hidden" id="inp-advId" name="advIds" value="${video.advIds}"/>
        <div id="advList" class="videoEdit-adv-list"></div>
    </div>
    <div class="videoEdit-inp-group">
        <div class="label">分享描述</div>
        <div class="videoEdit-cont">
            <input type="text" id="inp-shareWords" name="sharedWords" placeholder="20个字内的分享描述" value="${video.sharedWords}"/>
        </div>
    </div>
    <div class="videoEdit-inp-group">
        <div class="label">是否公开</div>
        <div class="videoEdit-cont" style="text-align: left;">
            <div class="checkDiv" >
                <input type="checkbox" name="checkPublicFlag" checked="checked" lay-skin="switch" lay-filter="switchTest"  lay-text="ON|OFF">
            </div>
            <div class="desc">设为关闭，则本视频将不会对外显示</div>
        </div>
    </div>
    <div class="videoEdit-inp-group">
        <div class="label">按钮显示设置</div>
        <div class="videoEdit-cont" style="text-align: left;">
            <div class="checkDiv"  style="font-size:16px;">
                <input type="radio" name="showBtnType" value="1" title="换成我的广告" checked="" lay-filter="btnTypeRadio">
                <input type="radio" name="showBtnType" value="2" title="进入主页" lay-filter="btnTypeRadio">
            </div>
            <div class="desc" id="showBtnType-desc">视频中将会出现视推宝推广按钮</div>
        </div>
    </div>
    <div class="videoEdit-inp-group">
        <div class="label">排序</div>
        <div class="videoEdit-cont">
            <input type="" name="sort" value="${video.sort!=null?video.sort:0}"/>
        </div>
    </div>
    <input type="hidden" name="id" id="inp-videoId" value="${video.id}" />
    <input type="hidden" name="publicFlag" id="inp-publicFlag" value="${video.publicFlag}"/>
    <input type="hidden" name="showBtnType" id="inp-showBtnType" value="${video.showBtnType}"/>
    <input type="hidden" name="coverImg" value="${video.cover}"/>
    <input type="hidden" name="sourceUrl" value="${video.sourceUrl}" />
    <input type="hidden" name="videoUrl" value="${video.playAddr}" />
    <div class="saveBtn">
        <button type="submit" id="btn-save">保 存</button>
    </div>
</form>
</div>

<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script src="<%=path%>/statics/layui/layui.all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=path%>/statics/js/xadmin.js"></script>
<script>
    var advType;
    var advId;
    $(function(){
        advType = $("#inp-advType").val();
        if (advType != '' &&  advId != ''){
            $.ajax({
                url: '${contextPath}/app/video/getVideoInfo?id=' + $("#inp-videoId").val(),
                type: 'get',
                dataType: 'json',
                success: function (res) {
                    if (res.data.advType != null){
                        advType = res.data.advType;
                        if (advType == 1) {
                           var list = res.data.advProductList;
                           for (var i=0;i<list.length;i++){
                               setAdv(advType, list[i].id, list[i].productName);
                           }
                        }else if(advType == 2){
                            setAdv(advType, res.data.advStore.id, res.data.advStore.storeName);
                        }else if(advType == 3){
                            setAdv(advType, res.data.advOther.id, res.data.advOther.name);
                        }
                    }
                }
            })
        }
    });
    layui.use(['form'], function(){
        var form = layui.form
            ,layer = layui.layer

        var publicFlag = "";
        if ($("#inp-publicFlag").val() == '' || $("#inp-publicFlag").val() == 'Y'){
            publicFlag = "checked";
            $("#inp-publicFlag").val('Y');
        }
        form.val("videoEdit-form", {
            "checkPublicFlag": publicFlag
            ,"showBtnType": $("#inp-showBtnType").val()
        })

        //监听指定开关
        form.on('switch(switchTest)', function(data){
            if (this.checked){
                $("#inp-publicFlag").val("Y");
            }else{
                $("#inp-publicFlag").val("N");
            }
        });

        form.on('radio(btnTypeRadio)', function(data){
            if (data.value == '1'){
                $("#showBtnType-desc").html('视频中将会出现视推宝推广按钮');
            }else if (data.value == '2'){
                $("#showBtnType-desc").html('视频中不会出现任何视推宝推广信息');
            }
        });


        //监听提交
        form.on('submit(demo1)', function(data){
            layer.alert(JSON.stringify(data.field), {
                title: '最终的提交信息'
            })
            return false;
        });

    });

    function selectAdv() {
        layer.open({
            type: 2,
            title: false,
            closeBtn: false,
            content: '${contextPath}/app/video/videoAdvSelect',
            area: ['100%', '100%']
        });
    }

    function clearAdv() {
        $("#inp-advType").val("");
        $("#inp-advId").val("");
        $("#advList").html("");
    }

    function setAdv(advType, advId, advName) {
        $("#inp-advType").val(advType);
        if (advType == 1){
            var idStr = $("#inp-advId").val();
            if (idStr.length > 0){
                idStr += ",";
            }
            $("#inp-advId").val(idStr + advId);
            var str = "<div class='videoEdit-adv'>" + advName + "<div class='close' onclick='deleteAdv(this)'><img src='${contextPath}/statics/img/video/manage/bg_40.png' /></div></div>";
        }else if (advType == 2){
            $("#advList").html("");
            $("#inp-advId").val(advId);
            var str = "<div class='videoEdit-adv'>" + advName + "<div class='close' onclick='deleteAdv(this)'><img src='${contextPath}/statics/img/video/manage/bg_40.png' /></div></div>";
        }else if (advType == 3){
            $("#advList").html("");
            $("#inp-advId").val(advId);
            var str = "<div class='videoEdit-adv'>" + advName + "<div class='close' onclick='deleteAdv(this)'><img src='${contextPath}/statics/img/video/manage/bg_40.png' /></div></div>";
        }
        $("#advList").html($("#advList").html() + str);
    }
    
    function deleteAdv(obj) {
        if (advType == 1){

        }else{
            $("#inp-advType").val("");
            $("#inp-advId").val("");
        }
        $(obj).parent().remove();
    }

    function check() {
        if (isEmpty($("#title").val())){
            Toast('视频标题不能为空！',2000);
            return false;
        }
        return true;
    }


</script>
</body>
</html>