<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>欢迎页面-L-admin1.0</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="<%=path%>/statics/css/font.css">
    <link rel="stylesheet" href="<%=path%>/statics/css/xadmin.css">
    <link rel="stylesheet" href="<%=path%>/statics/lib/layui/css/layui.css">
    <script src="<%=path%>/statics/js/jquery.min.js"></script>
</head>
<body>
<div class="x-body layui-anim layui-anim-up">
    <blockquote class="layui-elem-quote">欢迎管理员：
        <span class="">${sessionScope.userName}</span></blockquote>

    <c:if test="${sessionScope.session_admin_user_type eq 1}">
        <div style="padding: 20px; background-color: #F2F2F2;">
            <div class="layui-row layui-col-space15">
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header">总用户数</div>
                        <div id="totalUser" class="layui-card-body">
                        </div>
                    </div>
                </div>
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header">总流水</div>
                        <div id="totalFlow" class="layui-card-body">
                        </div>
                    </div>
                </div>
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header">总收益</div>
                        <div id="totalProfit" class="layui-card-body">
                        </div>
                    </div>
                </div>
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header">累计提现</div>
                        <div id="totalCash" class="layui-card-body">
                        </div>
                    </div>
                </div>
            </div>
            <div class="layui-row layui-col-space15">
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header">本月新用户数</div>
                        <div id="monthNewUser" class="layui-card-body">
                        </div>
                    </div>
                </div>
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header">本月流水</div>
                        <div id="monthFlow" class="layui-card-body">
                        </div>
                    </div>
                </div>

                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header">本月收益</div>
                        <div id="monthProfit" class="layui-card-body">
                        </div>
                    </div>
                </div>
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header">本月累计提现</div>
                        <div id="monthCash" class="layui-card-body">
                        </div>
                    </div>
                </div>
            </div>

            <div class="layui-row layui-col-space15">
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header">今日新增用户</div>
                        <div id="todayNewUser" class="layui-card-body">
                        </div>
                    </div>
                </div>
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header">今日流水</div>
                        <div id="todayFlow" class="layui-card-body">

                        </div>
                    </div>
                </div>
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header">今日收益</div>
                        <div id="todayProfit" class="layui-card-body">
                        </div>
                    </div>
                </div>
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header">今日提现</div>
                        <div id="todayCash" class="layui-card-body">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $(function () {
                $.ajax({
                    url: '${contextPath}/admin/stats/query',
                    type: 'get',
                    success: function (res) {
                        if (res.state === 1) {
                            $('#totalUser').html(res.data.totalUser);
                            $('#totalFlow').html(res.data.totalFlow);
                            $('#totalCash').html(res.data.totalCash);
                            $('#totalProfit').html(res.data.totalFlow - res.data.totalCash);

                            $('#monthNewUser').html(res.data.month.newUserCount);
                            $('#monthFlow').html(res.data.month.dailyFlow);
                            $('#monthCash').html(res.data.month.cashAmount);
                            $('#monthProfit').html(res.data.month.dailyFlow - res.data.month.cashAmount);


                            $('#todayNewUser').html(res.data.today.newUserCount);
                            $('#todayFlow').html(res.data.today.dailyFlow);
                            $('#todayCash').html(res.data.today.cashAmount);
                            $('#todayProfit').html(res.data.today.dailyFlow - res.data.today.cashAmount);
                        } else {
                            console.log(res.msg);
                        }
                    }
                });
            });
        </script>
    </c:if>


</div>

</body>
</html>
