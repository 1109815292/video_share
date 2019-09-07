package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.dto.VideoBatchDTO;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.Category;
import com.juheshi.video.entity.Video;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.CategoryService;
import com.juheshi.video.service.VideoService;
import com.juheshi.video.util.HttpRequest;
import com.juheshi.video.util.Page;
import com.juheshi.video.util.URLUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping("/admin/video")
public class VideoController {

    @Resource
    private VideoService videoService;

    @Resource
    private AppUserService appUserService;

    @Resource
    private CategoryService categoryService;

    @Value("#{resourceProperties['video.analysis.server']}")
    private String analysisServer;

    //获取视频列表
    @RequestMapping(value = "/videoList", produces = "text/html;charset=UTF-8")
    public ModelAndView videoList() {
        ModelAndView modelAndView = new ModelAndView();
        List<Category> list = categoryService.findAllCategory();
        modelAndView.addObject("categories", list);
        modelAndView.setViewName("admin/video/video-list");
        return modelAndView;
    }

    @RequestMapping(value = "/getVideoData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getVideoData(@RequestParam(required = false, name = "search") String search,
                               @RequestParam(required = false, name = "type") Integer type,
                               @RequestParam(required = false, name = "categoryId") Integer categoryId,
                               @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                               @RequestParam(required = false, name = "limit", defaultValue = "10") int limit) {
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Page<Video> p = videoService.pageFindAllVideoForAdmin(search, type,categoryId, page, limit);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }

    @RequestMapping(value = "/videoEdit", produces = "text/html;charset=UTF-8")
    public ModelAndView videoEdit(HttpServletRequest request) {
        //TODO 查询该管理员账号绑定的微信账号
        String id = request.getParameter("id");
        ModelAndView modelAndView = new ModelAndView();
        Video video = null;
        if (id != null && (!"".equals(id))) {
            video = videoService.findById(Integer.parseInt(id));
        }
        List<AppUser> list = appUserService.findAllUser(null, Constants.YES);
        List<Category> categories = categoryService.findByGroup(Category.GROUP_FANS_VIDEO, Constants.YES);
        modelAndView.addObject("video", video);
        modelAndView.addObject("adminUsers", list);
        modelAndView.addObject("categories", categories);
        modelAndView.setViewName("admin/video/video-edit");
        return modelAndView;
    }

    @RequestMapping(value = "/videoAddBatch", produces = "text/html;charset=UTF-8")
    public ModelAndView videoAddBatch(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        List<AppUser> list = appUserService.findAllUser(null, Constants.YES);
        modelAndView.addObject("adminUsers", list);
        modelAndView.setViewName("admin/video/video-add-batch");
        return modelAndView;
    }


    /**
     * 解析源视频
     *
     * @param content
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/analysisVideoSourceUrl", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object analysisVideoSourceUrl(@RequestParam(name = "content") String content) {
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
                //url = decodeHttpUrl(s.trim());
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

    @RequestMapping(value = "/saveVideoEdit", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object saveVideoEdit(Video video) {
        Integer id = video.getId();
        if (id == null) { //新增
            video.setShowBtnType(1);
            video.setCheckState(Video.CHECK_STATE_PASS);
            videoService.create(video, Constants.VIDEO_TYPE_UPLOAD_FROM_ADMIN);
        } else { //编辑
            videoService.updateVideo(video);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("state", 1);
        resultMap.put("msg", "success");
        return resultMap;
    }

    @RequestMapping(value = "/saveVideoBatch", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object saveVideoBatch(@RequestBody VideoBatchDTO videoBatchDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        videoService.createBatch(videoBatchDTO.getUserId(), videoBatchDTO.getVideos());
        resultMap.put("state", 1);
        resultMap.put("msg", "success");
        return resultMap;
    }


    /**
     * 逻辑删除视频资源
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/videoDelete", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object videoDelete(@RequestParam(name = "ids[]") int[] ids) {
        videoService.updateForDelete(ids);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("state", 1);
        resultMap.put("msg", "success");
        return resultMap;
    }

    @RequestMapping(value = "/recache", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object recache(@RequestParam(name = "ids[]") int[] ids) {
        videoService.updateForRecacheVideo(ids);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("state", 1);
        resultMap.put("msg", "success");
        return resultMap;
    }


    //获取未审核视频列表
    @RequestMapping(value = "/uncheckVideoList", produces = "text/html;charset=UTF-8")
    public ModelAndView uncheckVideoList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/video/video-uncheck-list");
        return modelAndView;
    }

    @RequestMapping(value = "/getUncheckVideoData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getUncheckVideoData(@RequestParam(required = false, name = "search") String search,
                                      @RequestParam(required = false, name = "checkState", defaultValue = "0") Integer checkState,
                                      @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                      @RequestParam(required = false, name = "limit", defaultValue = "10") int limit) {
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Page<Video> p = videoService.pageFindAllVideoForAdmin(search, null, checkState,
                new Integer[]{Constants.VIDEO_TYPE_UPLOAD_FROM_APP, Constants.VIDEO_TYPE_USER_COPY}, page, limit);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }

    @RequestMapping(value = "/checkVideoPass", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object checkVideoPass(Integer id) {
        videoService.modifyVideoCheckState(id, Video.CHECK_STATE_PASS);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("state", 1);
        resultMap.put("msg", "success");
        return resultMap;
    }

    @RequestMapping(value = "/checkVideoReject", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object checkVideoReject(Integer id, String checkStateRemark) {
        videoService.modifyVideoCheckState(id, Video.CHECK_STATE_REJECT, checkStateRemark);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("state", 1);
        resultMap.put("msg", "success");
        return resultMap;
    }

    //批量审批通过
    @RequestMapping(value = "/batchCheckVideoPass", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object batchCheckVideoPass(@RequestParam(name = "ids[]") Integer[] ids) {
        videoService.modifyVideoCheckState(Arrays.asList(ids), Video.CHECK_STATE_PASS);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("state", 1);
        resultMap.put("msg", "success");
        return resultMap;
    }

    @RequestMapping(value = "/changeCategory", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object changeCategory(Integer id, Integer categoryId) {
        videoService.modifyVideoCategory(id, categoryId);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("state", 1);
        resultMap.put("msg", "success");
        return resultMap;
    }


}
