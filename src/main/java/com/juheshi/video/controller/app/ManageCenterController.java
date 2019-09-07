package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.LogViewVideo;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.DivideService;
import com.juheshi.video.service.LogViewVideoService;
import com.juheshi.video.util.Page;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app")
@SessionAttributes(names = {Constants.SESSION_APP_USER_ID, Constants.SESSION_APP_STATION_COPY_NO})
public class ManageCenterController {

    @Resource
    private AppUserService appUserService;
    @Resource
    private LogViewVideoService logViewVideoService;
    @Resource
    private DivideService divideService;

    @RequestMapping(value = "/manageCenter", produces = "text/html;charset=UTF-8")
    public ModelAndView manageCenter(@ModelAttribute(Constants.SESSION_APP_USER_ID) int userId,
                                     @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo) {
        AppUser appUser = appUserService.findUserById(userId);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        Double allIncome = divideService.countDivide(param);

        param.put("sdate", UtilDate.getDate());
        param.put("edate", UtilDate.getDate());
        Double dayIncome = divideService.countDivide(param);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userName", appUser.getUserName());
        modelAndView.addObject("headImg", appUser.getHeadImg());
        modelAndView.addObject("copyNo", appUser.getCopyNo());
        modelAndView.addObject("inviteCopyNo", appUser.getInviteCopyNo());
        modelAndView.addObject("vipFlag", appUser.getVipFlag());
        modelAndView.addObject("vipDeadline", appUser.getVipDeadline());
        modelAndView.addObject("dayIncome", dayIncome != null ? dayIncome : 0);
        modelAndView.addObject("allIncome", allIncome != null ? allIncome : 0);
        modelAndView.addObject("stationCopyNo", stationCopyNo);
        if (appUser.getStationFlag() != null && stationCopyNo.equals(appUser.getCopyNo())){
            modelAndView.addObject("stationFlag", appUser.getStationFlag());
        }
        modelAndView.setViewName("/app/manageCenter");
        return modelAndView;
    }

//    @RequestMapping(value = "/invitation", produces = "text/html;charset=UTF-8")
//    public ModelAndView invitation(HttpServletRequest request){
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("app/invitation/invitation");
//
//        return modelAndView;
//    }


    @RequestMapping(value = "/myFans", produces = "text/html;charset=UTF-8")
    public ModelAndView myFans(String search, @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId) {
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/app/myFans");
        int pageNo = 1;
        int pageSize = 99;
        Page<AppUser> page = appUserService.pageFindUserFans(userId, search, pageNo, pageSize);
        Page<AppUser> fansPage = appUserService.pageFindFansOfFans(userId, search, pageNo, pageSize);

        modelAndView.addObject("myFansList", page.getList());
        modelAndView.addObject("myFansCount", page.getTotal());
        modelAndView.addObject("fansOfFansList", fansPage.getList());
        modelAndView.addObject("fansOfFansCount", fansPage.getTotal());
        modelAndView.addObject("allCount", page.getTotal() + fansPage.getTotal());
        return modelAndView;
    }

}
