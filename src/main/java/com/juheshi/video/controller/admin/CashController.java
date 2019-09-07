package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.RechargeDivide;
import com.juheshi.video.entity.SystemUser;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.DivideService;
import com.juheshi.video.util.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/cash")
public class CashController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private DivideService divideService;

    @RequestMapping(value = "/dividePayFailure", produces = "text/html;charset=UTF-8")
    public ModelAndView dividePayFailure() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/cash/divide-pay-failure-list");
        return modelAndView;
    }

    @RequestMapping(value = "/recash", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object recash(Integer id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (userType.equals(SystemUser.TYPE_ADMIN)) {
            int num = 0;
            try {
                num = divideService.saveReCash(id);
                if (num == 0) {
                    resultMap.put("state", 0);
                    resultMap.put("msg", "支付失败");
                } else {
                    resultMap.put("state", 1);
                    resultMap.put("msg", "ok");
                }
            } catch (Exception e) {
                logger.error("重新提现支付失败", e.getMessage());
                resultMap.put("state", 0);
                resultMap.put("msg", "支付失败");
                resultMap.put("detail", e.getMessage());
            }

        } else {
            resultMap.put("state", 0);
            resultMap.put("msg", "error");
        }
        return resultMap;
    }

    @RequestMapping(value = "/getNoCashData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getNoCashData(@RequestParam(required = false, name = "fansId") Integer fansId,
                             @RequestParam(required = false, name = "payFlag") Integer payFlag,
                             @RequestParam(required = false, name = "dateFrom") String dateFromStr,
                             @RequestParam(required = false, name = "dateTo") String dateToStr,
                             @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                             @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                             HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (!userType.equals(SystemUser.TYPE_ADMIN)) {
            resultMap.put("code", 1);
            resultMap.put("msg", "您没有权限");
            resultMap.put("count", 0);
            resultMap.put("data", null);
            return resultMap;
        }

        Date dateFrom = null;
        Date dateTo = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (dateFromStr != null && !"".equals(dateFromStr)) {
            try {
                dateFrom = sdf.parse(dateFromStr + " 00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (dateToStr != null && !"".equals(dateToStr)) {
            try {
                dateTo = sdf.parse(dateToStr + " 23:59:59");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Page<RechargeDivide> p = divideService.pageFind(null, fansId, payFlag, 2, dateFrom, dateTo, page, limit);
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }
}
