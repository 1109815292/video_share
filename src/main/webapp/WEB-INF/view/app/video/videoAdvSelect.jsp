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
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>选择广告</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"
          name="viewport"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>

    <link href="https://vjs.zencdn.net/7.5.5/video-js.css" rel="stylesheet">
    <link href="<%=path%>/statics/css/videostyle.css" rel="stylesheet">
    <link href="<%=path%>/statics/layui/css/layui.css" rel="stylesheet">

    <!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->
    <script src="https://vjs.zencdn.net/ie8/1.1.2/videojs-ie8.min.js"></script>

</head>
<body class="bg-square">

<div class="videoAdvSel-top">
    <ul id="menubar">
        <li class="topActive">商品</li>
        <li>实体店</li>
        <li>广告</li>
    </ul>
    <div class="selBtn" onclick="selected()">
        完成
    </div>
</div>
<div class="cont">
    <div id="div0">
        <div class="product-search">
            <div class="searchDiv">
                <form action="<%=path%>/app/video/square">
                    <input type="text" placeholder="输入关键字搜索" name="keywords">
                    <div class="searchBtn"><img src="<%=path%>/statics/img/video/manage/search.png"/></div>
                </form>
            </div>
        </div>
        <div class="videoAdvSel-advType">
            <div id="searchProduct0" class="advType advTypeActive" onclick="searchProduct(0)">
                <div>全部商品</div>
            </div>
            <div id="searchProduct1" class="advType advTypeNotActive" onclick="searchProduct(1)">
                <div>通用商品</div>
            </div>
            <div id="searchProduct2" class="advType advTypeNotActive" onclick="searchProduct(2)">
                <div>京东</div>
            </div>
            <div id="searchProduct3" class="advType advTypeNotActive" onclick="searchProduct(3)">
                <div>微店</div>
            </div>
            <div id="searchProduct4" class="advType advTypeNotActive" onclick="searchProduct(4)">
                <div>有赞</div>
            </div>
            <div id="searchProduct5" class="advType advTypeNotActive" onclick="searchProduct(5)">
                <div>淘宝</div>
            </div>
        </div>
        <div id="product-list" class="product-list layui-form">
                <c:forEach var="product" items="${advProductList}">
                    <div class="videoAdvSel-product">
                        <div class="product">
                            <input type="checkbox" name="product" title="${product.productName}" value="${product.id}">
                        </div>
                        <div class="desc">
                            <c:if test="${product.type == 1}">通用商品</c:if>
                            <c:if test="${product.type == 2}">京东</c:if>
                            <c:if test="${product.type == 3}">微店</c:if>
                            <c:if test="${product.type == 4}">有赞</c:if>
                            <c:if test="${product.type == 5}">淘宝</c:if>
                        </div>
                    </div>
                </c:forEach>
        </div>

        <c:if test="${advProductList.size() == 0}">
            <div class="addAdv">
                <div class="addAdvDesc">您还没有添加商品</div>
                <div class="addAdvBtn" onclick="window.location.href='<%=path%>/app/promotionSet'">立即添加</div>
            </div>
        </c:if>
    </div>

    <div id="div1" style="display: none;">
        <div class="product-search">
            <div class="searchDiv">
                <form action="<%=path%>/app/video/square">
                    <input type="text" placeholder="输入关键字搜索" name="keywords">
                    <div class="searchBtn"><img src="<%=path%>/statics/img/video/manage/search.png"/></div>
                </form>
            </div>
        </div>
        <div class="product-list">
            <form class="layui-form">
                <c:forEach var="store" items="${advStoreList}">
                    <div class="videoAdvSel-product">
                        <div class="store">
                            <input type="radio" name="store" value="${store.id}" title="${store.storeName}">
                        </div>
                        <div class="desc">
                            实体店
                        </div>
                    </div>
                </c:forEach>
            </form>
        </div>
        <c:if test="${advStoreList.size() == 0}">
            <div class="addAdv">
                <div class="addAdvDesc">您还没有添加实体店</div>
                <div class="addAdvBtn" onclick="window.location.href='<%=path%>/app/promotionSet'">立即添加</div>
            </div>
        </c:if>
    </div>

    <div id="div2" style="display: none;">
        <div class="product-search">
            <div class="searchDiv">
                <form action="<%=path%>/app/video/square">
                    <input type="text" placeholder="输入关键字搜索" name="keywords">
                    <div class="searchBtn"><img src="<%=path%>/statics/img/video/manage/search.png"/></div>
                </form>
            </div>
        </div>
        <div class="videoAdvSel-advType">
            <div id="searchAdv0" class="advType advTypeActive" onclick="searchAdv(0)">
                <div>全部</div>
            </div>
            <div id="searchAdv1" class="advType advTypeNotActive" onclick="searchAdv(1)">
                <div>公众号</div>
            </div>
            <div id="searchAdv2" class="advType advTypeNotActive" onclick="searchAdv(2)">
                <div>小程序</div>
            </div>
            <div id="searchAdv3" class="advType advTypeNotActive" onclick="searchAdv(3)">
                <div>个人微信</div>
            </div>
            <div id="searchAdv4" class="advType advTypeNotActive" onclick="searchAdv(4)">
                <div>微信群</div>
            </div>
            <div id="searchAdv5" class="advType advTypeNotActive" onclick="searchAdv(5)">
                <div>推广APP</div>
            </div>
            <div id="searchAdv6" class="advType advTypeNotActive" onclick="searchAdv(6)">
                <div>推广官网</div>
            </div>
            <div id="searchAdv7" class="advType advTypeNotActive" onclick="searchAdv(7)">
                <div>推广链接</div>
            </div>
            <div id="searchAdv8" class="advType advTypeNotActive" onclick="searchAdv(8)">
                <div>二维码</div>
            </div>
        </div>
        <div id="advOther-list" class="product-list layui-form">
                <c:forEach var="advOther" items="${advOtherList}">
                    <div class="videoAdvSel-product">
                        <div class="store">
                            <input type="radio" name="other" value="${advOther.id}" title="${advOther.name}">
                        </div>
                        <div class="desc">
                            <c:choose>
                                <c:when test="${advOther.type == 1}">公众号</c:when>
                                <c:when test="${advOther.type == 2}">小程序</c:when>
                                <c:when test="${advOther.type == 3}">个人微信</c:when>
                                <c:when test="${advOther.type == 4}">微信群</c:when>
                                <c:when test="${advOther.type == 5}">APP</c:when>
                                <c:when test="${advOther.type == 6}">官网</c:when>
                                <c:when test="${advOther.type == 7}">链接</c:when>
                                <c:when test="${advOther.type == 8}">二维码</c:when>
                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
        </div>
        <c:if test="${advOtherList.size() == 0}">
            <div class="addAdv">
                <div class="addAdvDesc">您还没有添加广告</div>
                <div class="addAdvBtn" onclick="window.location.href='<%=path%>/app/promotionSet'">立即添加</div>
            </div>
        </c:if>
    </div>
</div>

<script src="<%=path%>/statics/js/jquery.min.js"></script>
<script src="<%=path%>/statics/layui/layui.all.js" charset="utf-8"></script>
<script>
    var advType = 1;
    var form;
    layui.use(['form'], function() {
       form = layui.form;
    });
    $(function () {
        $("#menubar li").click(function () {
            $(this).siblings('li').removeClass('topActive');
            $(this).addClass('topActive');
            for (var i = 0; i < 4; i++) {
                if ($(this).index() == i) {
                    $("#div" + i).show();
                } else {
                    $("#div" + i).hide();
                }
            }
            advType = $(this).index() + 1;
        });
    });

    function searchProduct(advType){
        $.ajax({
            url: '${contextPath}/app/video/getAdvList?advType=' + advType,
            type: "get",
            dataType: "json",
            success: function(res) {
                if (res.code == 200){
                    $("#product-list").html("");
                    var str = "";
                    for (var i=0;i<res.data.length;i++){
                        str += '<div class="videoAdvSel-product"><div class="product"><input type="checkbox" name="product" title="';
                        str += res.data[i].productName + '" value="' + res.data[i].id + '"></div><div class="desc">';
                        if (res.data[i].type == 1){
                            str += '通用商品';
                        }else if (res.data[i].type == 2){
                            str += '京东';
                        }else if (res.data[i].type == 3){
                            str += '微店';
                        }else if (res.data[i].type == 4){
                            str += '有赞';
                        }else if (res.data[i].type == 5){
                            str += '淘宝';
                        }
                        str += '</div></div>';
                    }
                    $("#product-list").html(str);
                    form.render();
                }
            }
        });
        for (var i=0;i<6;i++){
            if (advType == i){
                $("#searchProduct" + i).removeClass("advTypeNotActive");
                $("#searchProduct" + i).addClass("advTypeActive");
            }else{
                $("#searchProduct" + i).removeClass("advTypeActive");
                $("#searchProduct" + i).addClass("advTypeNotActive");
            }
        }
    }

    function searchAdv(advType) {
        $.ajax({
            url: '${contextPath}/app/video/getAdvOtherList?advType=' + advType,
            type: "get",
            dataType: "json",
            success: function(res) {
                if (res.code == 200){
                    $("#advOther-list").html("");
                    var str = "";

                    for (var i=0;i<res.data.length;i++){
                        str += '<div class="videoAdvSel-product"><div class="store"><input type="radio" name="other" title="';
                        str += res.data[i].name + '" value="' + res.data[i].id + '"></div><div class="desc">';
                        if (res.data[i].type == 1){
                            str += '公众号';
                        }else if (res.data[i].type == 2){
                            str += '小程序';
                        }else if (res.data[i].type == 3){
                            str += '个人微信';
                        }else if (res.data[i].type == 4){
                            str += '微信群';
                        }else if (res.data[i].type == 5){
                            str += 'APP';
                        }else if (res.data[i].type == 6){
                            str += '官网';
                        }else if (res.data[i].type == 7){
                            str += '链接';
                        }else if (res.data[i].type == 8){
                            str += '二维码';
                        }
                        str += '</div></div>';
                    }
                    $("#advOther-list").html(str);
                    form.render();
                }
            }
        })
        for (var i=0;i<8;i++){
            if (advType == i){
                $("#searchAdv" + i).removeClass("advTypeNotActive");
                $("#searchAdv" + i).addClass("advTypeActive");
            }else{
                $("#searchAdv" + i).removeClass("advTypeActive");
                $("#searchAdv" + i).addClass("advTypeNotActive");
            }
        }
    }

    function selected() {
        window.parent.clearAdv();
        if (advType == 1) {
            var productSel = document.getElementsByName("product");
            for (var i = 0; i < productSel.length; i++) {
                if (productSel[i].checked) {
                    window.parent.setAdv(advType, productSel[i].value, productSel[i].title);
                }
            }
        } else if (advType == 2) {
            var storeId = $("input[name='store']:checked").val();
            var sotreName = $("input[name='store']:checked").attr("title");
            window.parent.setAdv(advType, storeId, sotreName);
        } else if (advType == 3){
            var storeId = $("input[name='other']:checked").val();
            var sotreName = $("input[name='other']:checked").attr("title");
            window.parent.setAdv(advType, storeId, sotreName);
        }
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    }
</script>
</body>
</html>