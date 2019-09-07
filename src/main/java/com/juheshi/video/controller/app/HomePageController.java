package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.*;
import com.juheshi.video.service.*;
import com.juheshi.video.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app")
@SessionAttributes(Constants.SESSION_APP_USER_ID)
public class HomePageController {

    @Resource
    private AppUserService appUserService;
    @Resource
    private VideoService videoService;

    @Resource
    private AdvProductService advProductService;
    @Resource
    private AdvStoreService advStoreService;
    @Resource
    private AdvWxService advWxService;
    @Resource
    private AdvOtherService advOtherService;
    @Resource
    private LogViewHomepageService logViewHomepageService;

    @RequestMapping(value = "/homePageSet", produces = "text/html;charset=UTF-8")
    public ModelAndView homePageSet(@ModelAttribute(Constants.SESSION_APP_USER_ID) int userId) {
        ModelAndView modelAndView = new ModelAndView();
        AppUser appUser = appUserService.findUserById(userId);
        AppUserPage aup = appUserService.findUserPageByUserId(userId);
        modelAndView.addObject("appUser", appUser);
        modelAndView.addObject("homePage", aup);
        modelAndView.setViewName("/app/homePageSet");
        return modelAndView;
    }

    @RequestMapping(value = "/saveHomePage", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object saveHomePage(AppUserPage appUserPage, @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        appUserPage.setUserId(userId);
        appUserService.modifyUserPage(appUserPage);
        resultMap.put("code", 200);
        resultMap.put("msg", "success");
        return resultMap;
    }

    @RequestMapping(value = "/userProfile", produces = "text/html;charset=UTF-8")
    public ModelAndView userProfile(String copyNo, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {

        AppUser user = appUserService.findUserByCopyNo(copyNo);
        //查询主页数据
        AppUserPage userPage = appUserService.findUserPageByUserId(user.getId());
        if (!user.getId().equals(userId)) { //他人访问主页
            logViewHomepageService.createLog(userPage.getId(), userId);
        }
        userPage.setHeadImg(user.getHeadImg());
        userPage.setUserName(user.getUserName());
        userPage.setCopyNo(user.getCopyNo());
        userPage.setVipFlag(user.getVipFlag());

        List<Video> videoList = videoService.findVideoByUserIdWithIgnoreIds(user.getId(), Constants.YES, 99, null);

        List<AdvProduct> advProductList = advProductService.findByUserId(user.getId(), null);
        List<AdvStore> advStoreList = advStoreService.findByUserId(user.getId());
        List<AdvWx> advWxList = advWxService.findByUserId(user.getId(), null);
        List<AdvOther> advOtherList = advOtherService.findByUserId(user.getId(), null);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userPage", userPage);

        modelAndView.addObject("videoCount", videoList.size());
        modelAndView.addObject("videoList", videoList);

        modelAndView.addObject("advProductCount", advProductList.size());
        modelAndView.addObject("advStoreCount", advStoreList.size());
        modelAndView.addObject("advWxCount", advWxList.size());
        modelAndView.addObject("advOtherCount", advOtherList.size());

        modelAndView.addObject("advProductList", advProductList);
        modelAndView.addObject("advStoreList", advStoreList);
        modelAndView.addObject("advWxList", advWxList);
        modelAndView.addObject("advOtherList", advOtherList);
        modelAndView.setViewName("/app/userProfile");

        return modelAndView;
    }

    @RequestMapping(value = "/getHomePageData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getHomePageData(Integer advType, String copyNo) {
        AppUser appUser = appUserService.findUserByCopyNo(copyNo);
        Map<String, Object> result = new HashMap<String, Object>();
        switch (advType) {
            case Constants.ADV_TYPE_PRODUCT:
                List<AdvProduct> products = advProductService.findByUserId(appUser.getId(), null);
                result.put("data", products);
                break;
            case Constants.ADV_TYPE_STORE:
                List<AdvStore> stores = advStoreService.findByUserId(appUser.getId());
                result.put("data", stores);
                break;
            case Constants.ADV_TYPE_OTHER:
                List<AdvOther> others = advOtherService.findByUserId(appUser.getId(), null);
                result.put("data", others);
                break;
        }
        result.put("state", 1);
        return result;
    }
}
