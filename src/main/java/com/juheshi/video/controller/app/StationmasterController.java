package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.*;
import com.juheshi.video.service.*;
import com.juheshi.video.util.Page;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/app/stationmaster")
@SessionAttributes(names = {Constants.SESSION_APP_STATION_COPY_NO, Constants.SESSION_APP_USER_ID})
public class StationmasterController {

    @Resource
    private VideoService videoService;
    @Resource
    private AdvStoreService advStoreService;
    @Resource
    private IndustryService industryService;
    @Resource
    private AppUserService appUserService;
    @Resource
    private VideoCommentService videoCommentService;
    @Resource
    private VideoStoreService videoStoreService;
    @Resource
    private PayOrderService payOrderService;
    @Resource
    private StationmasterService stationmasterService;


    @RequestMapping(value = "/index", produces = "text/html;charset=UTF-8")
    public ModelAndView index(String keywords, @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo){
        Stationmaster stationmaster = stationmasterService.findByCopyNo(stationCopyNo, true);
        if (keywords != null && !"".equals(keywords.trim())) {
            try {
                keywords = new String(keywords.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        int page = 1;
        int limit = 10;
        List<Map<String, Object>> videoStoreList = videoStoreService.findVideoStore(null, stationCopyNo, page, limit);
        List<Industry> channelIndustryList = industryService.findChannelShow();
        List<Industry> labelIndustryList = industryService.findLabelShow();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("channelIndustryList", channelIndustryList);
        modelAndView.addObject("labelIndustryList", labelIndustryList);
        modelAndView.addObject("videoList", videoStoreList);
        modelAndView.addObject("stationCopyNo", stationCopyNo);
        modelAndView.addObject("districtStr", stationmaster.getDistrict().getName());
        modelAndView.setViewName("/app/stationmaster/index");
        return modelAndView;
    }

    @RequestMapping(value = "/storeList", produces = "text/html;charset=UTF-8")
    public ModelAndView storeList(String keywords, Integer industryId, @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo){
        if (keywords != null && !"".equals(keywords.trim())) {
            try {
                keywords = new String(keywords.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        int page = 1;
        int limit = 10;

        List<Industry> industryList = industryService.findAllIndustry();
        List<AdvStore> advStoreList = advStoreService.findByType(stationCopyNo, industryId, null);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("advStoreList", advStoreList);
        modelAndView.addObject("industryList", industryList);
        modelAndView.setViewName("/app/stationmaster/storeList");
        return modelAndView;
    }

    @RequestMapping(value = "/storeDetail/{id}", produces = "text/html;charset=UTF-8")
    public ModelAndView storeDetail(@PathVariable("id") Integer id, @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo,
                                    @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId){
        AppUser appUser = appUserService.findUserById(userId);
        AdvStore advStore = advStoreService.findById(id);
        AppUser storeUser = appUserService.findUserById(advStore.getUserId());
        Industry industry = industryService.findIndustryById(advStore.getIndustryId());
        List<VideoStore> videoStoreList = videoStoreService.findVideoStoreByStoreId(id);

        for (VideoStore p:videoStoreList){
            for (VideoComment comment: p.getVideoCommentList()){
                comment.setLikeFlag(videoCommentService.checkCommentLike(comment.getId(), userId));
            }
        }

        Map<String, Object> storeUserMap = new HashMap<String, Object>();
        storeUserMap.put("headImg", storeUser.getHeadImg());
        storeUserMap.put("userName", storeUser.getUserName());
        storeUserMap.put("industry", industry.getIndustry());
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("headImg", appUser.getHeadImg());
        userMap.put("userName", appUser.getUserName());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("advStore", advStore);
        modelAndView.addObject("videoStoreList", videoStoreList);
        modelAndView.addObject("storeUserMap", storeUserMap);
        modelAndView.addObject("userMap", userMap);
        modelAndView.setViewName("/app/stationmaster/storeDetail");
        return modelAndView;
    }

    @RequestMapping(value = "/videoCheck/{id}", produces = "text/html;charset=UTF-8")
    public ModelAndView videoCheck(@PathVariable("id") Integer id, @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo,
                                    @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId){
        VideoStore videoStore = videoStoreService.findVideoStoreById(id);
        AdvStore advStore = advStoreService.findById(videoStore.getStoreId());

        Industry industry = industryService.findIndustryById(advStore.getIndustryId());
        AppUser storeUser = appUserService.findUserById(videoStore.getUserId());
        Map<String, Object> storeUserMap = new HashMap<String, Object>();
        storeUserMap.put("headImg", storeUser.getHeadImg());
        storeUserMap.put("userName", storeUser.getUserName());
        Map<String, Object> stationMap = new HashMap<String, Object>();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("storeUserMap", storeUserMap);
        modelAndView.addObject("advStore", advStore);
        modelAndView.addObject("videoStore", videoStore);
        modelAndView.addObject("industry", industry);
        modelAndView.setViewName("/app/stationmaster/storeVideoCheck");
        return modelAndView;
    }


    @RequestMapping(value = "/storeVideoEdit", produces = "text/html;charset=UTF-8")
    public ModelAndView storeVideoEdit(Integer videoId, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId){
        ModelAndView modelAndView = new ModelAndView();
        if (videoId != null && videoId > 0){
            VideoStore videoStore = videoStoreService.findVideoStoreById(videoId);
            List<VideoStoreImage> videoStoreImageList = videoStoreService.findImageByVideoId(videoId);
            modelAndView.addObject("videoStore", videoStore);
            modelAndView.addObject("videoStoreImageList", videoStoreImageList);
        }
        AppUser appUser = appUserService.findUserById(userId);
        if (appUser.getVipFlag().equals(Constants.YES) && appUser.getVipType().equals(AppUser.VIP_TYPE_OFFLINE)) {
            List<Industry> industryList = industryService.findAllIndustry();
            modelAndView.addObject("industryList", industryList);

            modelAndView.setViewName("/app/stationmaster/storeVideoEdit");
        }else{
            modelAndView.setViewName("/app/stationmaster/vip-error");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/storeVipList", produces = "text/html;charset=UTF-8")
    public ModelAndView storeVipList(@ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId,
                                     @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                     @RequestParam(required = false, name = "limit", defaultValue = "100") int limit){

        List<Industry> industryList = industryService.findAllIndustry();
        List<Map<String, Object>> payOrderList = payOrderService.findStoreVipPayOrder(userId, page, limit);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("industryList", industryList);
        modelAndView.addObject("payOrderList", payOrderList);
        modelAndView.setViewName("/app/stationmaster/storeVipList");
        return modelAndView;

    }

    @RequestMapping(value = "/stationSet", produces = "text/html;charset=UTF-8")
    public ModelAndView stationSet(@ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId){
        Stationmaster stationmaster = stationmasterService.findByUserId(userId, true);
        District district = stationmaster.getDistrict();
        String districtStr = district.getName();
        boolean hasParent = true;
        while (hasParent){
            if (district.getParent() != null){
                district = district.getParent();
                districtStr = district.getName() + "·"+ districtStr;
            }else{
                hasParent = false;
            }
        }
        stationmaster.setDistrictStr(districtStr);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("stationmaster", stationmaster);
        modelAndView.setViewName("/app/stationmaster/stationSet");
        return modelAndView;
    }

    @RequestMapping(value = "/updateStationInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object updateStationInfo(HttpServletRequest request, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId){
        Stationmaster stationmaster = stationmasterService.findByUserId(userId, false);
        String publicImg = request.getParameter("publicImg");
        if (publicImg != null && (!"".equals(publicImg))){
            stationmaster.setPublicImg(publicImg);
        }
        String stationName = request.getParameter("stationName");
        if (stationName != null && (!"".equals(stationName))){
            stationmaster.setStationName(stationName);
        }
        String publicWx = request.getParameter("publicWx");
        if (publicWx != null && (!"".equals(publicWx))){
            stationmaster.setPublicWx(publicWx);
        }
        String publicQRCode = request.getParameter("publicQRCode");
        if (publicQRCode != null && (!"".equals(publicQRCode))){
            stationmaster.setPublicQRCode(publicQRCode);
        }
        stationmasterService.modify(stationmaster);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("message", "success");
        return resultMap;
    }





}
