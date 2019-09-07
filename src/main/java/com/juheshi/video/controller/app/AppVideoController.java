package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.*;
import com.juheshi.video.service.*;
import com.juheshi.video.util.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.jdom.JDOMException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app/video")
@SessionAttributes(names = {Constants.SESSION_APP_USER_ID, Constants.SESSION_APP_USER_COPY_NO, Constants.SESSION_APP_STATION_COPY_NO})
public class AppVideoController {

    @Resource
    private VideoService videoService;
    @Resource
    private AdvProductService advProductService;
    @Resource
    private AdvStoreService advStoreService;
    @Resource
    private AdvOtherService advOtherService;
    @Resource
    private AdvWxService advWxService;
    @Resource
    private AppUserService appUserService;
    @Resource
    private LogViewVideoService logViewVideoService;
    @Resource
    private SysParamService sysParamService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private StationmasterService stationmasterService;

    @Resource
    private VarParamService varParamService;

    @Value("#{resourceProperties['video.analysis.server']}")
    private String analysisServer;


    @RequestMapping(value = "/square", produces = "text/html;charset=UTF-8")
    public ModelAndView square(String keywords, @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo) {
        if (keywords != null && !"".equals(keywords.trim())) {
            try {
                keywords = new String(keywords.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        int page = 1;
        int limit = 10;
        Page<Video> videoPage = videoService.pageFindAllVideo(keywords, null, Constants.YES, page, limit);
        List<Category> categoryList = categoryService.findAllCategory();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("videoList", videoPage.getList());
        modelAndView.addObject("categoryList", categoryList);
        modelAndView.addObject("stationCopyNo", stationCopyNo);
        modelAndView.setViewName("app/video/videoSquare");
        return modelAndView;
    }

    @RequestMapping(value = "/getSquareVideo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getSquareVideo(@RequestParam(required = false, name = "search") String search,
                                 @RequestParam(required = false, name = "type") Integer type,
                                 @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                 @RequestParam(required = false, name = "limit", defaultValue = "10") int limit){
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Page<Video> p = videoService.pageFindAllVideo(search, type, page, limit);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;

    }

    @RequestMapping(value = "/videoSquare-category", produces = "text/html;charset=UTF-8")
    public ModelAndView videoSquareCategory(Integer categoryId){
        List<Video> videoList = videoService.findVideoByCategory(categoryId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("videoList", videoList);
        modelAndView.setViewName("app/video/videoSquare-category");
        return modelAndView;
    }

    @RequestMapping(value = "/myVideoList", produces = "text/html;charset=UTF-8")
    public ModelAndView myVideoList(String keywords, @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId,
                                                    @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo) {
        if (keywords != null && !"".equals(keywords.trim())) {
            try {
                keywords = new String(keywords.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        int page = 1;
        int limit = 99;

        Map<String, Object> param = new HashMap<String, Object>();
        if (keywords != null && (!"".equals(keywords))) {
            param.put("search", keywords);
        }
        param.put("userId", userId);
        param.put("limit", limit);
        param.put("offset", (page - 1) * limit);
        Page<Video> videoPage = videoService.findAllVideo(param);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("videoList", videoPage.getList());
        modelAndView.addObject("stationCopyNo", stationCopyNo);
        modelAndView.setViewName("app/video/myVideoList");

        return modelAndView;
    }

    @RequestMapping(value = "/play", produces = "text/html;charset=UTF-8")
    public ModelAndView play(HttpServletRequest request, @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId,
                                                         @ModelAttribute(Constants.SESSION_APP_USER_COPY_NO) String copyNo,
                                                        @ModelAttribute(Constants.SESSION_APP_STATION_COPY_NO) String stationCopyNo) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        String type = request.getParameter("type");
        String indexStr = request.getParameter("index");
        Integer index = 0;
        if (indexStr != null && (!"".equals(indexStr))){
            index = Integer.valueOf(indexStr);
        }

        Video video = videoService.findById(id);
        AppUser lookUser = appUserService.findUserById(userId);
        AppUser appUser = appUserService.findUserById(video.getUserId());
        AppUserPage appUserPage = appUserService.findUserPageByUserId(video.getUserId());
        String pageName = appUserPage.getPageName();
        if (pageName == null || "".equals(pageName)){
            pageName = appUser.getUserName();
        }
        logViewVideoService.createLog(video.getId(), userId);

        ModelAndView modelAndView = new ModelAndView();
        if (stationCopyNo != null && (!"".equals(stationCopyNo))){
            Stationmaster stationmaster = stationmasterService.findByCopyNo(stationCopyNo, false);
            Map<String, Object> stationUserMap = new HashMap<String, Object>();
            stationUserMap.put("headImg", stationmaster.getPublicImg());
            stationUserMap.put("userName", stationmaster.getStationName());
            stationUserMap.put("publicWx", stationmaster.getPublicWx());
            stationUserMap.put("publicQRCode", stationmaster.getPublicQRCode());
            modelAndView.addObject("stationCopyNo", stationCopyNo);
            modelAndView.addObject("stationUserMap", stationUserMap);
        }
//        int page = 1;
//        int limit = 10;
//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("userId", userId);
//        param.put("limit", limit);
//        param.put("offset", (page - 1) * limit);
//        Page<Video> videoPage = videoService.findAllVideo(param);

/*        Page<Video> p;
        if ("myVideo".equals(type)){
            p = videoService.pageFindVideoWithIgnoreIds(video.getUserId(), Constants.YES, new Integer[]{id}, 1, 5);
        }else{
            p = videoService.pageFindVideoWithIgnoreIds(null, Constants.YES, new Integer[]{id}, 1, 5);
        }*/

        modelAndView.addObject("video", video);
        //modelAndView.addObject("videoList", p.getList());

        modelAndView.addObject("headImg", appUser.getHeadImg());
        modelAndView.addObject("userCopyNo", copyNo);
        modelAndView.addObject("pageName", pageName);
        modelAndView.addObject("index", index);
        modelAndView.addObject("type", type);
        modelAndView.addObject("subscribeFlag", lookUser.getSubscribeFlag());
        modelAndView.setViewName("app/video/videoPlay");

        return modelAndView;
    }

    @RequestMapping(value = "getPlayVideoList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getPlayVideoList(HttpServletRequest request,
                                   @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                   @RequestParam(required = false, name = "limit", defaultValue = "10") int limit) {
        Integer id = Integer.valueOf(request.getParameter("playVideoId"));
        Integer index = Integer.valueOf(request.getParameter("playVideoIndex"));
        String type = request.getParameter("type");
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Page<Video> p;
        if ("myVideo".equals(type)) {
            Video video = videoService.findById(id);
            p = videoService.pageFindVideoWithIgnoreIds(video.getUserId(), Constants.YES, new Integer[]{id}, page, limit, index);
        } else {
            p = videoService.pageFindVideoWithIgnoreIds(null, Constants.YES, new Integer[]{id}, page, limit, index);
        }

        resultMap.put("data", p.getList());
        return resultMap;
    }

    @RequestMapping(value = "/getVideoInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getVideoInfo(@RequestParam(name = "id") int id){

        Video video = videoService.findById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        Integer advType = video.getAdvType();
        map.put("advType", advType);
        if (advType != null && advType > 0){
            if (advType == Constants.ADV_TYPE_PRODUCT){
                List<AdvProduct> advProductList = advProductService.findProductByIds(video.getAdvIds());
                map.put("advProductList", advProductList);
            }else if (advType == Constants.ADV_TYPE_STORE){
                AdvStore advStore = advStoreService.findById(Integer.valueOf(video.getAdvIds()));
                map.put("advStore", advStore);
            }else if (advType == Constants.ADV_TYPE_OTHER){
                AdvOther advOther = advOtherService.findById(Integer.valueOf(video.getAdvIds()));
                map.put("advOther", advOther);
            }
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("data", map);

        return resultMap;
    }

    @RequestMapping(value = "/drawVideo", produces = "text/html;charset=UTF-8")
    public ModelAndView drawVideo(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("app/video/drawVideo");

        return modelAndView;
    }

    @RequestMapping(value = "/videoEdit", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
    public ModelAndView videoEdit(@RequestParam(name = "content") String content, @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        ModelAndView modelAndView = new ModelAndView();
        SysParam sysParam = sysParamService.findByKey(Constants.NOT_VIP_MAX_VIDEO_COUNT);
        Integer maxCount = Integer.valueOf(sysParam.getParamValue());
        AppUser appUser = appUserService.findUserById(userId);
        if (!"Y".equals(appUser.getVipFlag())){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", userId);
            long count = videoService.count(param);
            if (count >= maxCount){
                resultMap.put("code", 300001);
                resultMap.put("msg", "非会员最多只能上传8个视频");
                modelAndView.addObject("data", resultMap);
                modelAndView.addObject("content", content);
                modelAndView.setViewName("app/video/drawVideo");
                return modelAndView;
            }
        }

        Map<String, Object> analysisMap = this.analysisVideoSourceUrl(content);
        if (analysisMap.get("state").equals(1)) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) analysisMap.get("data");
            Map<String, Object> dataMap = list.get(0);
            modelAndView.addObject("video", dataMap);
            modelAndView.setViewName("app/video/videoEdit");
        }else if (analysisMap.get("state").equals(0)){
            resultMap.put("code", 300002);
            resultMap.put("msg", analysisMap.get("msg"));
            modelAndView.addObject("data", resultMap);
            modelAndView.addObject("content", content);
            modelAndView.setViewName("app/video/drawVideo");
        }
        return modelAndView;
    }


    @RequestMapping(value = "/checkVideoCount",  produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object checkVideoCount(@ModelAttribute(Constants.SESSION_APP_USER_ID) int userId){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        SysParam sysParam = sysParamService.findByKey(Constants.NOT_VIP_MAX_VIDEO_COUNT);
        Integer maxCount = Integer.valueOf(sysParam.getParamValue());
        AppUser appUser = appUserService.findUserById(userId);
        if (!"Y".equals(appUser.getVipFlag())){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", userId);
            long count = videoService.count(param);
            if (count < maxCount){
                resultMap.put("code", 200);
                resultMap.put("data", true);
                return resultMap;
            }else{
                resultMap.put("code", 300001);
                resultMap.put("msg", "非会员最多只能上传8个视频");
                resultMap.put("data", false);
                return resultMap;
            }
        }
        resultMap.put("code", 200);
        resultMap.put("data", true);
        return resultMap;
    }


    @RequestMapping(value = "/originalVideoEdit", produces = "text/html;charset=UTF-8")
    public ModelAndView videoEdit(@RequestParam(name = "id") int id, @RequestParam(name = "type") String type) {
        ModelAndView modelAndView = new ModelAndView();

        Video video = videoService.findById(id);
        if (video != null) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("cover", video.getCoverImg());
            dataMap.put("playAddr", video.getVideoUrl());
            dataMap.put("text", video.getTitle());
            dataMap.put("sourceUrl", video.getSourceUrl());
            dataMap.put("sharedWords", video.getSharedWords());
            dataMap.put("publicFlag", video.getPublicFlag());
            dataMap.put("showBtnType", video.getShowBtnType());
            dataMap.put("sort", video.getSort());
            if (type != null && "myVideo".equals(type)){
                dataMap.put("id", video.getId());
                dataMap.put("advType", video.getAdvType());
                dataMap.put("advIds", video.getAdvIds());
            }
            modelAndView.addObject("video", dataMap);
        }

        modelAndView.setViewName("app/video/videoEdit");
        return modelAndView;
    }


    @RequestMapping(value = "/videoAdvSelect", produces = "text/html;charset=UTF-8")
    public ModelAndView videoAdvSelect(@ModelAttribute(Constants.SESSION_APP_USER_ID) int userId) {
        ModelAndView modelAndView = new ModelAndView();
        List<AdvProduct> advProductList = advProductService.findByUserId(userId, null);
        List<AdvStore> advStoreList = advStoreService.findByUserId(userId);
        List<AdvWx> advWxList = advWxService.findByUserId(userId, null);
        List<AdvOther> advOtherList = advOtherService.findByUserId(userId, null);

        modelAndView.addObject("advProductList", advProductList);
        modelAndView.addObject("advStoreList", advStoreList);
        modelAndView.addObject("advWxList", advWxList);
        modelAndView.addObject("advOtherList", advOtherList);
        modelAndView.setViewName("app/video/videoAdvSelect");
        return modelAndView;
    }

    @RequestMapping(value = "/getAdvList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getAdvList(@RequestParam(required = false, name = "keywords") String keywords,
                             @RequestParam(required = false, name = "advType") Integer advType,
                             @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId){
        if (advType != null && advType == 0){
            advType = null;
        }
        List<AdvProduct> advProductList = advProductService.findByUserId(keywords, userId, advType);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("data", advProductList);
        return resultMap;
    }

    @RequestMapping(value = "/getAdvOtherList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getAdvOtherList(@RequestParam(required = false, name = "keywords") String keywords,
                             @RequestParam(required = false, name = "advType") Integer advType,
                             @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId){
        if (advType != null && advType == 0){
            advType = null;
        }
        List<AdvOther> advOtherList = advOtherService.findByUserId(keywords, userId, advType);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("data", advOtherList);
        return resultMap;
    }

    @RequestMapping(value = "saveVideo", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
    public String saveVideo(Video video,
                            @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId,
                            @ModelAttribute(Constants.SESSION_APP_USER_COPY_NO) String copyNo) {
        if(video.getId() !=null ) {
            videoService.updateVideoForApp(video);
        } else {
            video.setUserId(userId);
            video.setCopyNo(copyNo);
            video.setCheckState(Video.CHECK_STATE_DRAFT);
            videoService.create(video, Constants.VIDEO_TYPE_UPLOAD_FROM_APP);
        }

        return "redirect:/app/video/myVideoList";
    }

    @RequestMapping(value = "/videoDelete", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object videoDelete(@RequestParam(name = "id") int id){
        int[] ids = new int[1];
        ids[0] = id;
        videoService.updateForDelete(ids);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("message", "success");
        return resultMap;
    }


    @RequestMapping(value = "/getWxConfig", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getWxConfig(String url) throws IOException, JDOMException {
        VarParam varParam = varParamService.findByVarName(Constants.VAR_PARAM_WX_ACCESS_TOKEN);
        if(varParam == null) {
            String param = "grant_type=client_credential&appid=" + ConfigUtil.APPID + "&secret=" + ConfigUtil.SECRET;
            String resultStr = HttpRequest.sendGet(ConfigUtil.WX_PUBLIC_ACCESS_TOKEN_URL, param);
            ObjectMapper mapper = new ObjectMapper();
            HashMap map = mapper.readValue(resultStr, HashMap.class);
            String accessToken = map.get("access_token").toString();
            Integer expiresIn = Integer.parseInt(map.get("expires_in").toString());
            varParam = new VarParam();
            varParam.setVarName(Constants.VAR_PARAM_WX_ACCESS_TOKEN);
            varParam.setVarValue(accessToken);
            varParam.setVarExpiresIn(expiresIn);
            varParam.setVarDesc("微信公众号accessToken");
            varParam.setRemark("创建");
            varParamService.create(varParam);
        }
        return WxUtils.getWxSign(varParam.getVarValue(), url);
    }

    /**
     * 解析源视频
     *
     * @param content
     * @return
     */

    private Map<String, Object> analysisVideoSourceUrl(String content) {
        Map<String, Object> result = new HashMap<String, Object>();
        if ("".equals(content.trim())) {
            result.put("state", 0);
            result.put("msg", "参数异常");
            return result;
        }


        String[] c = content.split("\n");
        boolean hasUrl = false;
        if (c.length > 0) {
            String url;
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (String s : c) {
                url = URLUtil.getUrlFromContent(s.trim());
                if ("".equals(url.trim())) {
                    continue;
                }
                String resp = HttpRequest.sendGet(analysisServer, "url=" + url);
                JSONObject jsonObject = new JSONObject(resp);
                int code = jsonObject.getJSONObject("json").getInt("code");
                if (code == 200) {
                    Map<String, Object> dataMap = new HashMap<String, Object>();
                    dataMap.put("cover", jsonObject.getJSONObject("json").getJSONObject("data").getString("cover"));
                    dataMap.put("playAddr", jsonObject.getJSONObject("json").getJSONObject("data").getString("playAddr"));
                    dataMap.put("text", jsonObject.getJSONObject("json").getJSONObject("data").getString("text"));
                    dataMap.put("sourceUrl", url);
                    list.add(dataMap);
                    hasUrl = true;
                }
            }

            if (hasUrl) {
                result.put("state", 1);
                result.put("data", list);
                result.put("msg", "");
            } else {
                result.put("state", 0);
                result.put("msg", "视频源地址异常");
            }

        } else {
            result.put("state", 0);
            result.put("msg", "未发现url");
        }

        return result;
    }

}
