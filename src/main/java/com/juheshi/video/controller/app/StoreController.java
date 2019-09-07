package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.*;
import com.juheshi.video.service.*;
import com.juheshi.video.util.Page;
import com.juheshi.video.util.UtilDate;
import com.juheshi.video.util.WxMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/app/store")
@SessionAttributes(names = {Constants.SESSION_APP_STATION_COPY_NO, Constants.SESSION_APP_USER_ID})
public class StoreController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AdvStoreService advStoreService;
    @Resource
    private VideoService videoService;
    @Resource
    private VideoStoreService videoStoreService;

    @Resource
    private VideoStoreImageService videoStoreImageService;

    @Resource
    private AppUserService appUserService;
    @Resource
    private CfgWxMessageService cfgWxMessageService;
    @Resource
    private StationmasterService stationmasterService;

    @Resource
    private VarParamService varParamService;
    @Resource
    private LogViewVideoService logViewVideoService;

    @RequestMapping(value = "/myStore", produces = "text/html;charset=UTF-8")
    public ModelAndView myStore(@RequestParam(required = false, name = "id") Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        if (id != null) {
            AdvStore store = advStoreService.findById(id);
            List<Video> videoList = videoService.findVideoByStore(id);
            modelAndView.addObject("store", store);
            modelAndView.addObject("videoList", videoList);
        }
        modelAndView.setViewName("app/store/myStore");
        return modelAndView;
    }

    @RequestMapping(value = "/getStoreList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getStoreList(@RequestParam(required = false, name = "industryId") Integer industryId,
                               @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo) {

        List<AdvStore> advStoreList = advStoreService.findByType(stationCopyNo, industryId, null);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("data", advStoreList);
        return resultMap;
    }

    @RequestMapping(value = "/getStoreVideo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getStoreVideo(@RequestParam(required = false, name = "search") String search,
                                @RequestParam(required = false, name = "industryId") Integer industryId,
                                @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                                @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo) {
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        List<Map<String, Object>> storeVideoList = videoStoreService.findVideoStore(industryId, stationCopyNo, page, limit);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("msg", "success");
        resultMap.put("data", storeVideoList);
        return resultMap;

    }

    @RequestMapping(value = "/getStoreImage", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getStoreImage(@RequestParam(required = false, name = "videoId") Integer videoId) {

        List<VideoStoreImage> videoStoreImageList = videoStoreService.findImageByVideoId(videoId);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("data", videoStoreImageList);

        return resultMap;
    }


    @RequestMapping(value = "/removeVideoStoreImg", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object removeVideoStoreImg(Integer id, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        VideoStoreImage vsi = videoStoreImageService.findById(id);
        VideoStore vs = videoStoreService.findVideoStoreById(vsi.getVideoId());
        if (!userId.equals(vs.getUserId())) {
            resultMap.put("code", 0);
            resultMap.put("msg", "无法删除他人图片");
            return resultMap;
        }
        videoStoreImageService.deleteById(id);
        resultMap.put("code", 200);
        resultMap.put("msg", "success");
        return resultMap;
    }


    @RequestMapping(value = "/saveVideoStore", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object saveVideoStore(VideoStore videoStore, @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo,
                                 @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        AppUser appUser = appUserService.findUserById(userId);
        if (appUser != null) {
            //判断是否为线下会员
            if (appUser.getVipFlag().equals(Constants.YES) && appUser.getVipType().equals(AppUser.VIP_TYPE_OFFLINE)) {

                if (videoStore.getId() != null) { //编辑操作
                    //商品视频保存后审核状态改为待审核
                    videoStore.setCheckState(0);
                    //将发送通知标识改为0
                    videoStore.setNotified(0);
                    videoStoreService.updateVideoStore(videoStore);
                } else {//新增操作
                    videoStore.setCopyNo(appUser.getCopyNo());
                    videoStore.setUserId(userId);
                    videoStore.setStationCopyNo(stationCopyNo);
                    videoStore.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
                    videoStore.setCheckState(0);
                    videoStore.setWithin48hours(1);

                    List<AdvStore> advStoreList = advStoreService.findByUserId(userId);
                    if (advStoreList.size() > 0) {
                        videoStore.setStoreId(advStoreList.get(0).getId());
                        videoStore.setIndustryId(advStoreList.get(0).getIndustryId());
                    }
                    videoStoreService.insertVideoStore(videoStore);
                }
                //给站长发送审核推送
                if (videoStore.getStationCopyNo() != null) {
//                    final String finalStationCopyNo = videoStore.getStationCopyNo();
                    final VideoStore finalVideoStore = videoStore;
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AppUser stationAppUser = appUserService.findUserByCopyNo(finalVideoStore.getStationCopyNo());
                            if (stationAppUser != null) {
                                //给站长发送审核推送
                                List<CfgWxMessage> list = cfgWxMessageService.listFindByTypeAndPosition(CfgWxMessage.TYPE_CUSTOMER_SERVICE_MESSAGE,
                                        CfgWxMessage.TRIGGER_POSITION_SAVE_VIDEO_STORE);
                                if (list != null && list.size() > 0) {
                                    logger.debug("********************** 总共查询到" + list.size() + "条客服消息待发送 **********************");
                                    VarParam varParam = varParamService.findByVarName(Constants.VAR_PARAM_WX_ACCESS_TOKEN);
                                    if (varParam != null) {
                                        WxMessageUtil wmu = new WxMessageUtil(varParam.getVarValue());
                                        for (CfgWxMessage m : list) {
                                            try {
                                                switch (m.getMsgType()) {
                                                    case CfgWxMessage.MSG_TYPE_TEXT:
                                                        String content = m.getContent();
                                                        if (content.contains("{{title}}")) {
                                                            content = content.replace("{{title}}", finalVideoStore.getTitle());
                                                        }
                                                        if (content.contains("{{video_store_id}}")) {
                                                            content = content.replace("{{video_store_id}}", finalVideoStore.getId().toString());
                                                        }
                                                        if (content.contains("{{store_id}}")) {
                                                            content = content.replace("{{store_id}}", finalVideoStore.getStoreId().toString());
                                                        }
                                                        wmu.sendCustomServiceTextMessage(stationAppUser.getOpenId(), content);
                                                        //发送成功后修改通知状态
                                                        videoStoreService.updateVideoStoreNotified(finalVideoStore.getId(), 1);
                                                        break;
                                                    case CfgWxMessage.MSG_TYPE_IMAGE:
                                                        break;
                                                    case CfgWxMessage.MSG_TYPE_NEWS:
                                                        break;
                                                }
                                            } catch (Exception e) {
                                                if(e.getMessage() != null && e.getMessage().contains("45015")) {
                                                    //发送模板消息
                                                    List<CfgWxMessage> list2 = cfgWxMessageService.listFindByTypeAndPosition(CfgWxMessage.TYPE_TEMPLATE_MESSAGE,
                                                            CfgWxMessage.TRIGGER_POSITION_VIDEO_STORE_NOTIFY_ERROR);
                                                    if(list2!=null && list2.size()>0) {
                                                        CfgWxMessage cwm = list2.get(0);
                                                        try {
                                                            wmu.sendTemplateMessage(stationAppUser.getOpenId(), cwm.getTemplateId(),
                                                                    cwm.getTemplateData(), cwm.getTemplateUrl()==null?"":cwm.getTemplateUrl());
                                                        } catch (Exception e1) {
                                                            logger.error("发送模板消息异常", e1);
                                                        }
                                                    }
                                                }
                                                logger.error("发送审核推送异常", e);
                                            }

                                        }
                                    }

                                }
                            }
                        }
                    });
                    t.start();
                }
                resultMap.put("code", 200);
                resultMap.put("message", "success");
                return resultMap;
            } else {
                resultMap.put("code", 201);
                resultMap.put("message", "您还不是会员或会员已到期，无法操作");
                return resultMap;
            }
        } else {
            resultMap.put("code", 101);
            resultMap.put("message", "无此用户");
            return resultMap;
        }
    }

    @RequestMapping(value = "/myStoreVideoList", produces = "text/html;charset=UTF-8")
    public ModelAndView myStoreVideoList(@ModelAttribute(Constants.SESSION_APP_USER_ID) int userId,
                                         @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo) {

        List<VideoStore> videoStoreList = videoStoreService.findVideoStoreByUserId(userId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("videoStoreList", videoStoreList);
        modelAndView.addObject("stationCopyNo", stationCopyNo);
        modelAndView.setViewName("app/stationmaster/myStoreVideoList");

        return modelAndView;
    }

    @RequestMapping(value = "/deleteStoreVideo")
    @ResponseBody
    public Object deleteStoreVideo(@ModelAttribute(Constants.SESSION_APP_USER_ID) int userId, Integer id) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        VideoStore v = videoStoreService.findVideoStoreById(id);
        if (!v.getUserId().equals(userId)) {
            resultMap.put("code", 0);
            resultMap.put("message", "不能删除他人视频");
            return resultMap;
        }
        videoStoreService.deleteById(id);
        resultMap.put("code", 200);
        resultMap.put("message", "success");
        return resultMap;
    }

    @RequestMapping(value = "/storeVideoPlay", produces = "text/html;charset=UTF-8")
    public ModelAndView storeVideoPlay(HttpServletRequest request, @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId,
                                       @ModelAttribute(Constants.SESSION_APP_USER_COPY_NO) String copyNo,
                                       @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        String type = request.getParameter("type");
        String indexStr = request.getParameter("index");
        Integer index = 0;
        if (indexStr != null && (!"".equals(indexStr))) {
            index = Integer.valueOf(indexStr);
        }

        VideoStore videoStore = videoStoreService.findVideoStoreById(id);
        AppUser lookUser = appUserService.findUserById(userId);
        AppUser appUser = appUserService.findUserById(videoStore.getUserId());
        AppUserPage appUserPage = appUserService.findUserPageByUserId(videoStore.getUserId());
        String pageName = appUserPage.getPageName();
        if (pageName == null || "".equals(pageName)) {
            pageName = appUser.getUserName();
        }
        logViewVideoService.createLog(videoStore.getId(), userId);

        ModelAndView modelAndView = new ModelAndView();
        if (stationCopyNo != null && (!"".equals(stationCopyNo))) {
            Stationmaster stationmaster = stationmasterService.findByCopyNo(stationCopyNo, false);
            Map<String, Object> stationUserMap = new HashMap<String, Object>();
            stationUserMap.put("headImg", stationmaster.getPublicImg());
            stationUserMap.put("userName", stationmaster.getStationName());
            stationUserMap.put("wx", stationmaster.getPublicWx());
            stationUserMap.put("WxQRCode", stationmaster.getPublicQRCode());
            modelAndView.addObject("stationCopyNo", stationCopyNo);
            modelAndView.addObject("stationUserMap", stationUserMap);
        }

        modelAndView.addObject("video", videoStore);

        modelAndView.addObject("headImg", appUser.getHeadImg());
        modelAndView.addObject("userCopyNo", copyNo);
        modelAndView.addObject("pageName", pageName);
        modelAndView.addObject("index", index);
        modelAndView.addObject("type", type);
        modelAndView.addObject("subscribeFlag", lookUser.getSubscribeFlag());
        modelAndView.setViewName("app/stationmaster/storeVideoPlay");

        return modelAndView;
    }

    @RequestMapping(value = "getStoreVideoList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getStoreVideoList(HttpServletRequest request,
                                    @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                    @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                                    @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopeNo) {
        Integer id = Integer.valueOf(request.getParameter("playVideoId"));
        Integer index = Integer.valueOf(request.getParameter("playVideoIndex"));
        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<Map<String, Object>> videoStoreList = videoStoreService.findVideoStore(null, stationCopeNo, page, limit, new Integer[]{id});

        resultMap.put("data", videoStoreList);
        return resultMap;
    }

    @RequestMapping(value = "/setStoreVideoTop", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object setStoreVideoTop(Integer id) {
        VideoStore videoStore = videoStoreService.findVideoStoreById(id);
        if (videoStore != null) {
            videoStore.setIsTop(1);
            videoStoreService.updateVideoStore(videoStore);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("message", "success");
        return resultMap;
    }

    @RequestMapping(value = "/storeVideoCheck", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object storeVideoCheck(Integer id, Integer state) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        VideoStore videoStore = videoStoreService.findVideoStoreById(id);
        if (videoStore != null) {
            videoStore.setCheckState(state);
            videoStoreService.updateVideoStore(videoStore);
            resultMap.put("code", 200);
            resultMap.put("message", "success");
        } else {
            resultMap.put("code", 300011);
            resultMap.put("message", "无此视频！");
        }
        return resultMap;
    }

    @RequestMapping(value = "/modifyWxQRCode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object modifyWxQRCode(String wxQRCode, Integer storeId) {
        AdvStore advStore = advStoreService.findById(storeId);
        advStore.setWxQRCode(wxQRCode);
        advStoreService.modifyAdvStore(advStore);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("message", "success");
        return resultMap;
    }

    @RequestMapping(value = "/checkCopyNo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object checkCopyNo(String copyNo) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        AppUser appUser = appUserService.findUserByCopyNo(copyNo);
        if (appUser == null) {
            resultMap.put("code", 101);
            resultMap.put("message", "无此用户，请检查视推号是否正确！");
            return resultMap;
        }
        if (appUser.getVipFlag().equals("Y")) {
            resultMap.put("code", 201);
            resultMap.put("message", "该用户已是会员，是否继续充值？");
        } else {
            resultMap.put("code", 200);
            resultMap.put("message", "success");
        }
        return resultMap;
    }

    @RequestMapping(value = "/getBaiduMap", produces = "text/html;charset=UTF-8")
    public ModelAndView getBaiduMap() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("app/store/baiduMap");
        return modelAndView;
    }
}
