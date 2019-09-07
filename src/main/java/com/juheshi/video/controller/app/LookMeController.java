package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.*;
import com.juheshi.video.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/app")
@SessionAttributes(names = {Constants.SESSION_APP_USER_ID, Constants.SESSION_APP_STATION_COPY_NO})
public class LookMeController {

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
    private LogViewVideoService logViewVideoService;
    @Resource
    private LogViewAdvService logViewAdvService;

    @RequestMapping(value = "/lookMe", produces = "text/html;charset=UTF-8")
    public ModelAndView lookMe(@ModelAttribute(Constants.SESSION_APP_USER_ID) int userId,
                               @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo) {
        ModelAndView modelAndView = new ModelAndView();
        AppUser appUser = appUserService.findUserById(userId);
        AppUserPage userPage = appUserService.findUserPageByUserId(userId);

        modelAndView.addObject("headImg", appUser.getHeadImg());
        modelAndView.addObject("userName", appUser.getUserName());
        modelAndView.addObject("viewCount", userPage.getViewCount());
        modelAndView.addObject("peopleCount", userPage.getPeopleCount());
        modelAndView.addObject("vipFlag", appUser.getVipFlag());
        modelAndView.addObject("stationCopyNo", stationCopyNo);
        modelAndView.setViewName("app/viewLog/lookMe");
        return modelAndView;
    }

    @RequestMapping(value = "/lookMe/videoData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object queryVideoData(@ModelAttribute(Constants.SESSION_APP_USER_ID) int userId) {
        List<Video> list = videoService.findVideoByUserIdWithIgnoreIds(userId, null, 999, null);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("state", 1);
        result.put("data", list);
        return result;
    }

    @RequestMapping(value = "/lookMe/advData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object queryAdvData(Integer advType, @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        switch (advType) {
            case Constants.ADV_TYPE_PRODUCT:
                List<AdvProduct> products = advProductService.findByUserId(userId, null);
                result.put("data", products);
                break;
            case Constants.ADV_TYPE_STORE:
                List<AdvStore> stores = advStoreService.findByUserId(userId);
                result.put("data", stores);
                break;
            case Constants.ADV_TYPE_OTHER:
                List<AdvOther> others = advOtherService.findByUserId(userId, null);
                result.put("data", others);
                break;
        }
        result.put("state", 1);
        return result;
    }

    //视频查看详情
    @RequestMapping(value = "/lookMe/videoViewLog", produces = "text/html;charset=UTF-8")
    public ModelAndView videoViewLog(@RequestParam(name = "id") Integer id, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId){
        ModelAndView modelAndView = new ModelAndView();

        Video video = videoService.findById(id);
        List<Map<String, Object>> videoViewLogList = logViewVideoService.findVideoViewLogByVideoId(id, userId);

        List<Map<String, Object>> resultList = sortData(videoViewLogList);
        modelAndView.addObject("video", video);
        modelAndView.addObject("resultList", resultList);
        modelAndView.setViewName("app/viewLog/videoViewLog");
        return modelAndView;
    }

    //产品查看详情
    @RequestMapping(value = "/lookMe/productViewLog", produces = "text/html;charset=UTF-8")
    public ModelAndView productViewLog(@RequestParam(name = "id") Integer id){
        ModelAndView modelAndView = new ModelAndView();

        AdvProduct advProduct = advProductService.findById(id);
        List<Map<String, Object>> videoViewLogList = logViewAdvService.findAdvViewLogByAdvId(1, id);

        List<Map<String, Object>> resultList = sortData(videoViewLogList);
        modelAndView.addObject("advProduct", advProduct);
        modelAndView.addObject("resultList", resultList);
        modelAndView.setViewName("app/viewLog/productViewLog");
        return modelAndView;
    }

    //广告查看详情
    @RequestMapping(value = "/lookMe/advViewLog", produces = "text/html;charset=UTF-8")
    public ModelAndView advViewLog(@RequestParam(name = "id") Integer id){
        ModelAndView modelAndView = new ModelAndView();

        AdvOther advOther = advOtherService.findById(id);
        List<Map<String, Object>> videoViewLogList = logViewAdvService.findAdvViewLogByAdvId(3, id);

        List<Map<String, Object>> resultList = sortData(videoViewLogList);
        modelAndView.addObject("advOther", advOther);
        modelAndView.addObject("resultList", resultList);
        modelAndView.setViewName("app/viewLog/advViewLog");
        return modelAndView;
    }

    //店铺查看详情
    @RequestMapping(value = "/lookMe/storeViewLog", produces = "text/html;charset=UTF-8")
    public ModelAndView storeViewLog(@RequestParam(name = "id") Integer id){
        ModelAndView modelAndView = new ModelAndView();

        AdvStore advStore = advStoreService.findById(id);
        List<Map<String, Object>> videoViewLogList = logViewAdvService.findAdvViewLogByAdvId(2, id);

        List<Map<String, Object>> resultList = sortData(videoViewLogList);
        modelAndView.addObject("advStore", advStore);
        modelAndView.addObject("resultList", resultList);
        modelAndView.setViewName("app/viewLog/storeViewLog");
        return modelAndView;
    }




    private List<Map<String, Object>> sortData(List<Map<String, Object>> videoViewLogList){
        Integer preUserId = 0;
        Integer userId;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = null;
        int viewCount = 0;
        for (Map<String, Object> map:videoViewLogList){
            userId = (Integer) map.get("id");
            if (preUserId.equals(userId)){
                viewCount++;
                List<Map<String, Object>> viewList = (List<Map<String, Object>>) resultMap.get("viewList");
                Map<String, Object> viewMap = new HashMap<String, Object>();
                viewMap.put("viewTime", map.get("createdTime"));
                viewList.add(viewMap);
                resultMap.put("viewList", viewList);
                resultMap.put("viewCount", viewCount);
            }else{
                if (preUserId > 0){
                    resultList.add(resultMap);
                }
                resultMap = new HashMap<String, Object>();
                viewCount = 1;
                resultMap.put("id", userId);
                resultMap.put("headImg", map.get("headImg"));
                resultMap.put("userName", map.get("userName"));
                resultMap.put("copyNo", map.get("copyNo"));
                resultMap.put("viewCount", viewCount);
                List<Map<String, Object>> viewList = new ArrayList<Map<String, Object>>();
                Map<String, Object> viewMap = new HashMap<String, Object>();
                viewMap.put("viewTime", map.get("createdTime"));
                viewList.add(viewMap);
                resultMap.put("viewList", viewList);
                preUserId = userId;
            }
        }
        if (videoViewLogList.size() > 0){
            resultList.add(resultMap);
        }

        return resultList;
    }


}
