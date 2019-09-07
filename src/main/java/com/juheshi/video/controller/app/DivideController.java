package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.service.DivideService;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/app")
@SessionAttributes(Constants.SESSION_APP_USER_ID)
public class DivideController {

    @Resource
    private DivideService divideService;

    @RequestMapping(value = "/income", produces = "text/html;charset=UTF-8")
    public ModelAndView income( @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId){

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        Double allIncome = divideService.countDivide(param);

        param.put("sdate", UtilDate.getDate());
        param.put("edate", UtilDate.getDate());
        Double dayIncome = divideService.countDivide(param);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        param.put("sdate", UtilDate.getFirstDay(year, month));
        Double monthIncome = divideService.countDivide(param);

        List<Map<String, Object>> rechargeDivideList = divideService.findDivideByUserId(userId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("dayIncome", dayIncome!=null?dayIncome:0);
        modelAndView.addObject("monthIncome", monthIncome!=null?monthIncome:0);
        modelAndView.addObject("allIncome", allIncome!=null?allIncome:0);
        modelAndView.addObject("rechargeDivideList", rechargeDivideList);
        modelAndView.setViewName("app/divide/income");

        return modelAndView;
    }
}
