package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.StatsDay;
import com.juheshi.video.entity.SystemUser;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.PayOrderService;
import com.juheshi.video.service.StatsDayService;
import com.juheshi.video.service.WithdrawService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/stats")
public class StatsController {

    @Resource
    private StatsDayService statsDayService;

    @Resource
    private AppUserService appUserService;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private WithdrawService withdrawService;

    @RequestMapping(value = "/query", produces = "application/json;charset=UTF-8" )
    @ResponseBody
    public Object query(HttpSession session){
        Map<String,Object> result = new HashMap<>();
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (!userType.equals(SystemUser.TYPE_ADMIN)) {
            result.put("state", 0);
            result.put("msg", "您没有权限");
            return result;
        }
        //查询总用户数
        long totalUser = appUserService.findAllUserCount();
        //查询总流水
        double totalFlow = payOrderService.findTotalFlow();
        //查询总提现
        double totalCash = withdrawService.findTotalAmount();

        StatsDay month = statsDayService.findMonth();

        StatsDay today = statsDayService.findToday();


        Map<String,Object> data = new HashMap<>();
        data.put("totalUser", totalUser);
        data.put("totalFlow", totalFlow);
        data.put("totalCash", totalCash);
        data.put("today", today);
        data.put("month", month);
        result.put("state", 1);
        result.put("msg", "ok");
        result.put("data", data);

        return result;
    }
}
