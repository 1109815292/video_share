<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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

<div class="customer-btn" style="width:100%;height:60px;background: #ddd;margin:0;padding-top:15px;">
    <ul>
        <li onclick="window.location.href='<%=path%>/app/video/drawVideo'">吸粉视频</li>
        <li class="active">商品视频</li>
    </ul>
</div>
<div class="cont" style="padding-bottom:100px;">
    <form id="storeVideoForm" class="layui-form" lay-filter="videoEdit-form" action="<%=path%>/app/store/saveVideoStore"
          method="post"
          onsubmit="return check()">
        <div class="videoEdit-inp-group">
            <input type="hidden" id="inp-videoType" value="${videoStore.videoType}"/>
            <div class="videoEdit-cont">
                <select id="sel-videoType" lay-filter="videoOrPic" name="videoType">
                    <option value="1">发视频</option>
                    <option value="2">发图片</option>
                </select>
            </div>
        </div>
        <div class="videoEdit-inp-group">
            <div class="label">标题 <span>(必填)</span></div>
            <div class="videoEdit-cont">
                <textarea rows="2" id="title" name="title">${videoStore.title}</textarea>
            </div>
        </div>
        <div class="videoEdit-inp-group">
            <div class="label">分享描述</div>
            <div class="videoEdit-cont">
                <textarea rows="2" name="sharedWords">${videoStore.sharedWords}</textarea>
            </div>
        </div>
<%--        <div class="videoEdit-inp-group">
            <div class="label">简介</div>
            <div class="videoEdit-cont">
                <textarea rows="4" id="brief" name="brief">${videoStore.brief}</textarea>
            </div>
        </div>--%>
        <input type="hidden" name="coverImgMediaId" id="coverImgMediaId" />
        <input type="hidden" name="storeImgMediaIds" id="storeImgMediaIds" />
        <input type="hidden" name="coverImg" id="inp-coverImg" value="${videoStore.coverImg}"/>
        <input type="hidden" name="videoUrl" id="inp-videoUrl" value="${videoStore.videoUrl}"/>
        <input type="hidden" name="id" value="${videoStore.id}"/>
        <input type="hidden" name="stationCopyNo" value="${videoStore.stationCopyNo}"/>
        <input type="hidden" name="storeId" value="${videoStore.storeId}"/>
    </form>
    <div class="videoEdit-inp-group">
        <form action="" id="uploadImg" method="post" enctype="multipart/form-data">
            <div class="label">上传封面</div>
            <div class="videoEdit-cont">
                <div class="advPic productWidth" style="padding-bottom:30px;">
                    <label>
                        <%--<input style="position:absolute;opacity:0;width:110px;" accept="image/*" mutiple="mutiple"--%>
                        <%--onchange="uploadImgFile(1)" type="file" id="file"--%>
                        <%--name="uploadFile">--%>
                        <c:choose>
                            <c:when test="${empty videoStore.coverImg}">
                                <img id="picUrl" onclick="uploadImg(1, coverImgDoneCallback)"
                                     src="<%=path%>/statics/img/video/manage/bg_41.png">
                            </c:when>
                            <c:otherwise>
                                <img id="picUrl" onclick="uploadImg(1, coverImgDoneCallback)"
                                     src="${videoStore.coverImg}">
                            </c:otherwise>
                        </c:choose>
                    </label>
                </div>
            </div>
        </form>
    </div>


    <div id="store-video">
        <div class="videoEdit-inp-group">
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
    </div>

    <div id="store-pic" style="display: none;">
        <div class="homePageSetBar">店铺图片
            <div class="hint" id="count-hint">
                <span class="currentCount"><c:choose><c:when test="${videoStoreImageList.size() == 0}">0</c:when><c:otherwise>${fn:length(videoStoreImageList)}</c:otherwise></c:choose></span>/9
            </div>
        </div>
        <div class="advStore-pic">
            <div id="pic-cont">
                <c:forEach var="image" items="${videoStoreImageList}">
                    <div class="div-pic">
                        <img class="layui-upload-img" src="${image.picUrl}">
                        <button type="button" onclick="pic_del_remote(this, ${image.id})"
                                class="layui-btn layui-btn-sm layui-btn-primary">删除
                        </button>
                        <div style="display:none;" class="store-image-info">
                            <input type="hidden" class="store-img-url" value="${image.picUrl}">
                            <input type="hidden" class="store-img-id" value="${image.id}">
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div id="div-update" class="layui-upload"  <c:if test="${videoStoreImageList.size() == 9}">style="display: none;"</c:if>>
                <div class="div-pic btn-update">
                    <form action="" id="uploadImg2" method="post" enctype="multipart/form-data">
                        <%--<input style="position:absolute;opacity:0;width:110px;height: 100px;" accept="image/*"--%>
                        <%--mutiple="mutiple" onchange="uploadImgFile(2)" type="file"--%>
                        <%--name="uploadFile">--%>
                            <img class="layui-upload-img"
                                 onclick="uploadImg(9-parseInt($('.currentCount').text()), storeImgDoneCallback)"
                                 src="<%=path%>/statics/img/video/manage/bg_41.png">
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="saveBtn">
        <button type="submit" id="btn-save" onclick="saveStoreVideo()">保 存</button>
    </div>

</div>

<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script src="<%=path%>/statics/layui/layui.all.js" charset="utf-8"></script>
<script src="<%=path%>/statics/js/toast.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=path%>/statics/js/xadmin.js"></script>
<script src='https://res.wx.qq.com/open/js/jweixin-1.4.0.js' type="text/javascript"></script>
<script>
    Array.prototype.indexOf = function(val) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] === val) return i;
        }
        return -1;
    };

    Array.prototype.remove = function(val) {
        var index = this.indexOf(val);
        if (index > -1) {
            this.splice(index, 1);
        }
    };


    $(function () {
        $.ajax({
            async: false,
            url: '${contextPath}/app/video/getWxConfig',//后台请求接口，找后台要
            data: {url: window.location.href},
            type: "get",
            dataType: "json",
            success: function (data) {
                wx.config({
                    debug: false,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。true为调用开启
                    appId: data.appid,//appid// 必填，公众号的唯一标识
                    timestamp: data.timestamp, // 必填，生成签名的时间戳
                    nonceStr: data.nonceStr,// 必填，生成签名的随机串
                    signature: data.signature,// 必填，签名，
                    jsApiList: ['chooseImage', 'uploadImage', 'chooseVideo', "previewImage", "downloadImage"]
                });
                wx.ready(function () {

                });
            },
            error: function (xhr) {
                console.log("error");
            }
        });


        var videoType = $("#inp-videoType").val();
        if (videoType == 1) {
            $("#store-video").show();
            $("#store-pic").hide();
        } else if (videoType == 2) {
            $("#store-video").hide();
            $("#store-pic").show();
        }
    });
    var pictureCount = 0;
    layui.use(['form'], function () {
        var form = layui.form
            , layer = layui.layer;

        form.val("videoEdit-form", {
            "videoType": $("#inp-videoType").val()
        })

        form.on('select(industry)', function (data) {
            if (data.value == 6) {
                $("#div-otherIndustry").show();
            } else {
                $("#div-otherIndustry").hide();
            }
        })

        form.on('select(videoOrPic)', function (data) {
            if (data.value == '1') {//传视频
                $("#store-video").show();
                $("#store-pic").hide();
                $('#storeImgMediaIds').val("");
            } else {//传图片
                $("#store-video").hide();
                $("#store-pic").show();
            }
        });

        //监听提交
        form.on('submit(demo1)', function (data) {
            layer.alert(JSON.stringify(data.field), {
                title: '最终的提交信息'
            })
            return false;
        });

    });

    function check() {
        if (isEmpty($("#title").val())) {
            Toast('视频标题不能为空！', 2000);
            return false;
        }
        return true;
    }

    function saveStoreVideo() {
        $("#btn-save").attr("disabled", true);
        if ($("#sel-videoType").val() == 2) {
            var storeImageInfo = $('.store-image-info');
            $.each(storeImageInfo, function (index, item) {
                var url = $(item).children('input[class="store-img-url"]').val();
                var id = $(item).children('input[class="store-img-id"]').val();
                $('#storeVideoForm').append('<input type="hidden" name="images[' + index + '].picUrl" value="' + url + '">');
                if (id !== null && id !== undefined) {
                    $('#storeVideoForm').append('<input type="hidden" name="images[' + index + '].id" value="' + id + '">');
                }
            });
        }
        $.ajax({
            url: '${contextPath}/app/store/saveVideoStore',
            method: 'post',
            data: $("#storeVideoForm").serialize(),
            dataTyps: 'json',
            success: function (res) {
                if (res.code == 200) {
                    Toast('保存成功！', 3000);
                    window.location.href = '${contextPath}/app/store/myStoreVideoList';
                }
            }
        })
    }

    function uploadImage() {
        var that = this;
        $.ajax({
            async: false,
            url: '${contextPath}/app/video/getWxConfig',//后台请求接口，找后台要
            data: {url: window.location.href},
            type: "get",
            dataType: "json",
            success: function (data) {
                wx.config({
                    debug: false,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。true为调用开启
                    appId: data.appid,//appid// 必填，公众号的唯一标识
                    timestamp: data.timestamp, // 必填，生成签名的时间戳
                    nonceStr: data.nonceStr,// 必填，生成签名的随机串
                    signature: data.signature,// 必填，签名，
                    jsApiList: ['chooseImage', 'uploadImage', 'chooseVideo', "previewImage", "downloadImage"]
                });

                wx.ready(function () {
                    wx.chooseImage({
                        count: 1, // 默认9
                        sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
                        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                        success: function (res) {
                            var localIds = res.localIds;
                            that.wxUploadImage(localIds, function (picUrl) {
                                that.dataInfo[name] = picUrl;
                            });
                            $("#picUrl").attr("src", localIds);
                            $("#inp-coverImg").val(localIds);
                        }
                    });
                });
            },
            error: function (xhr) {
                console.log("error");
            }
        });
    }

    function coverImgDoneCallback(localId, serverId) {
        $('#picUrl').attr("src", localId);
        $('#coverImgMediaId').val(serverId);
    }


    function storeImgDoneCallback(localId, serverId) {
        serverIds.push(serverId);
        var str = '';
        str += '<div class="div-pic" id='+serverId+'>';
        str += '<img class="layui-upload-img" src="'+localId+'">';
        str += '<button type="button" onclick="pic_del_local(this)" class="layui-btn layui-btn-sm layui-btn-primary">删除</button>';
        str += '</div>';
        $("#pic-cont").append(str);

        var ids = "";
        $.each(serverIds, function(index, item){
            ids += item + ",";
        });
        ids = ids.substring(0, ids.length - 1);
        $('#storeImgMediaIds').val(ids);


        var currentCount = parseInt($('.currentCount').text());
        $('.currentCount').text(++currentCount);

        if(currentCount >= 9) {
            $("#div-update").hide();
        } else {
            $("#div-update").show();
        }
    }

    function pic_del_remote(obj, id){
        $.ajax({
            url:'${contextPath}/app/store/removeVideoStoreImg',
            data:{id:id},
            success:function(res){
                if(res.code === 200){
                    var currentCount = parseInt($('.currentCount').text());
                    $('.currentCount').text(currentCount - 1);

                    $(obj).parent().remove();

                    $("#div-update").show();
                } else {
                    console.log(res.msg);
                }
            }
        });
    }

    function pic_del_local(obj){
        var id = $(obj).parent().attr('id');
        $(obj).parent().remove();
        serverIds.remove(id);
        var ids = "";
        $.each(serverIds, function(index, item){
            ids += item + ",";
        });
        ids = ids.substring(0, ids.length - 1);
        $('#storeImgMediaIds').val(ids);

        var currentCount = parseInt($('.currentCount').text());
        $('.currentCount').text(currentCount - 1);

        $("#div-update").show();
    }
    // var serverIds = "";

    var serverIds = [];
    function uploadImg(count, doneCallback) {
        wx.chooseImage({
            count: count,
            sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album', 'camera'],      // 可以指定来源是相册还是相机，默认二者都有
            success: function (res) {
                var localIds = res.localIds;      // 返回选定照片的本地ID列表
                syncUpload(localIds, doneCallback);
            }
        });
    }

    function syncUpload(localIds, doneCallback) {
        var localId = localIds.pop();
        wx.uploadImage({
            localId: localId,                // 需要上传的图片的本地ID，由chooseImage接口获得
            isShowProgressTips: 1,           // 默认为1，显示进度提示
            success: function (res) {
                var serverId = res.serverId; // 返回图片的服务器端serverId
                if (doneCallback != null)
                    doneCallback(localId, serverId);
                if (localIds.length > 0) {
                    syncUpload(localIds, doneCallback);
                }
            }
        });
    }

    function uploadImgFile(index) {
        var formData;
        if (index === 1) {
            formData = new FormData($("#uploadImg")[0]);
        } else if (index === 2) {
            formData = new FormData($("#uploadImg2")[0]);
        }
        $.ajax({
            url: '${contextPath}/app/uploadFile',
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            dataType: 'json',
            success: function (res) {
                if (index === 1) {
                    $("#picUrl").attr("src", res.picUrl);
                    $("#inp-coverImg").val(res.picUrl);
                } else if (index === 2) {
                    var str = "<div class='div-pic'><img class='layui-upload-img' src='" + res.picUrl + "'>";
                    str += "<button type='button' onclick='pic_del(this, " + res.id + ")' class='layui-btn layui-btn-sm layui-btn-primary' >删除</button>";
                    str += "<div style='display:none;' class='store-image-info'><input type='hidden' class='store-img-url' value='" + res.picUrl + "' ></div>";
                    str += "</div>";
                    $("#pic-cont").append(str);
                    pictureCount++;
                    $("#count-hint").html(pictureCount + "/9");
                    if (pictureCount > 8) {
                        $("#div-update").hide();
                    } else {
                        $("#div-update").show();
                    }
                }
            },
        });
        $('#file').val('');
    }

    function uploadVideo(file) {
        var fileSize = 0;
        var fileMaxSize = 1024 * 200;//1M
        var filePath = file.value;
        if(filePath){
            fileSize =file.files[0].size;
            var size = fileSize / 1024;
            if (size > fileMaxSize) {
                alert("文件大小不能大于200M！");
                file.value = "";
                return false;
            }else if (size <= 0) {
                alert("文件大小不能为0M！");
                file.value = "";
                return false;
            }
        }else{
            alert("error");
            return false;
        }
        $(".uploadVideoHint").html("正在上传......");

        var formData;
        formData = new FormData($("#uploadVideo")[0]);
        $.ajax({
            url: '${contextPath}/app/uploadVideo',
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            dataType: 'json',
            success: function (res) {
                $(".uploadVideoHint").html("");
                $("#img-camera").attr('src', '${contextPath}/statics/img/stationmaster/camera_fill.png');
                $("#inp-videoUrl").val(res.videoUrl);
                Toast("视频上传完成！", 2000);
            },
        });
        $('#file').val('');
    }


    function pic_del(obj, id) {
        var ids = $('#deletedStoreImageIds').val();
        if (id !== null && id !== undefined && id !== "") {
            if (ids !== "") {
                $('#deletedStoreImageIds').val(ids + "," + id);
            } else {
                $('#deletedStoreImageIds').val(id);
            }
        }
        $(obj).parent().remove();
        pictureCount--;
        $("#count-hint").html(pictureCount + "/9");
        if (pictureCount > 8) {
            $("#div-update").hide();
        } else {
            $("#div-update").show();
        }
    }


</script>
</body>
</html>