package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.AppUserClient;
import com.juheshi.video.entity.AppUserPage;
import com.juheshi.video.service.AppUserClientService;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.LogViewVideoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/app/customer")
@SessionAttributes(names = {Constants.SESSION_APP_USER_ID, Constants.SESSION_APP_STATION_COPY_NO})
public class MyCustomerController {

    @Resource
    private AppUserClientService appUserClientService;
    @Resource
    private AppUserService appUserService;
    @Resource
    private LogViewVideoService logViewVideoService;


    @RequestMapping(value = "/myCustomer", produces = "text/html;charset=UTF-8")
    public ModelAndView myCustomer( @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId,
                                    @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo) {
        ModelAndView modelAndView = new ModelAndView();
        AppUser appUser = appUserService.findUserById(userId);

        List<AppUserClient> activeClients = appUserClientService.findActiveClient(userId, 30);

        List<AppUserClient> allClients = appUserClientService.findAllClient(userId);
        modelAndView.addObject("activeClients", activeClients);
        modelAndView.addObject("allClients", allClients);
        modelAndView.addObject("vipFlag", appUser.getVipFlag());
        modelAndView.addObject("stationCopyNo", stationCopyNo);
        modelAndView.setViewName("app/customer/myCustomer");
        return modelAndView;
    }


    @RequestMapping(value = "/viewAdv", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object viewAdv(@RequestParam(name = "advType") Integer advType,
                          @RequestParam(name = "advId") Integer advId,
                          @RequestParam(name = "viewType") Integer viewType,
                          @RequestParam(name = "copyNo") String copyNo,
                          @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId){

        AppUser appUser = appUserService.findUserByCopyNo(copyNo);

        if (!appUser.getId().equals(userId)){
            appUserClientService.addAdvViewCount(advType, advId, appUser.getId(), userId, viewType);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("msg", "success");
        return resultMap;
    }

    @RequestMapping(value = "/customerTrack", produces = "text/html;charset=UTF-8")
    public ModelAndView customerTrack(@RequestParam(name = "copyNo") String copyNo, @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId){
        ModelAndView modelAndView = new ModelAndView();

        AppUser appUser = appUserService.findUserByCopyNo(copyNo);
        AppUserPage appUserPage = appUserService.findUserPageByUserId(appUser.getId());

        Calendar c = Calendar.getInstance();
        c.setTime(appUser.getLastViewTime());
        Calendar sysdate = Calendar.getInstance();
        sysdate.setTime(new Date());
        long times = c.getTimeInMillis() - sysdate.getTimeInMillis();
        long activeDays = times / (1000 * 3600 * 24) + 1;

        AppUserClient appUserClient = appUserClientService.selectByUserIdAndclientUserId(userId, appUser.getId());

        List<Map<String, Object>> videoViewLogList = logViewVideoService.findVideoViewLogByUserId(appUser.getId(), userId);

        Integer videoId = 0;
        Integer id;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = null;
        int viewCount = 0;
        for (Map<String, Object> map:videoViewLogList){
            id = (Integer) map.get("id");
            if (videoId.equals(id)){
                viewCount++;
                    List<Map<String, Object>> viewList = (List<Map<String, Object>>) resultMap.get("viewList");
                    Map<String, Object> viewMap = new HashMap<String, Object>();
                    viewMap.put("viewTime", map.get("createdTime"));
                    viewList.add(viewMap);
                    resultMap.put("viewList", viewList);
                resultMap.put("viewCount", viewCount);
            }else{
                if (videoId > 0){
                    resultList.add(resultMap);
                }
                resultMap = new HashMap<String, Object>();
                viewCount = 1;
                resultMap.put("id", id);
                resultMap.put("coverImg", map.get("coverImg"));
                resultMap.put("title", map.get("title"));
                resultMap.put("viewCount", viewCount);
                List<Map<String, Object>> viewList = new ArrayList<Map<String, Object>>();
                Map<String, Object> viewMap = new HashMap<String, Object>();
                viewMap.put("viewTime", map.get("createdTime"));
                viewList.add(viewMap);
                resultMap.put("viewList", viewList);
                videoId = id;
            }
        }
        if (videoViewLogList.size() > 0){
            resultList.add(resultMap);
        }

        Map<String, Object> returnDataMap = new HashMap<String, Object>();
        returnDataMap.put("headImg", appUser.getHeadImg());
        returnDataMap.put("userName", appUser.getUserName());
        returnDataMap.put("lastViewTime", appUser.getLastViewTime());
        returnDataMap.put("city", appUser.getCity());
        returnDataMap.put("wxQRCode", appUserPage.getWxQRCode());
        returnDataMap.put("resultList", resultList);
        returnDataMap.put("videoViewCount", appUserClient.getVideoViewCount()!=null?appUserClient.getVideoViewCount():0);
        returnDataMap.put("productViewCount", appUserClient.getProductViewCount()!=null?appUserClient.getProductViewCount():0);
        returnDataMap.put("advViewCount", appUserClient.getAdvViewCount()!=null?appUserClient.getAdvViewCount():0);
        returnDataMap.put("homePageViewCount", appUserClient.getHomePageViewCount()!=null?appUserClient.getHomePageViewCount():0);
        returnDataMap.put("activeDays", activeDays);
        returnDataMap.put("wx", appUserPage.getWx());

        modelAndView.addObject("data", returnDataMap);
        modelAndView.setViewName("app/customer/customerTrack");
        return modelAndView;

    }


}
