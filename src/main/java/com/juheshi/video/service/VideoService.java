package com.juheshi.video.service;

import com.juheshi.video.common.Constants;
import com.juheshi.video.dao.VideoDao;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.Video;
import com.juheshi.video.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

@Service("VideoService")
public class VideoService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private VideoDao videoDao;

    @Resource
    private AppUserService appUserService;

    @Value("#{resourceProperties['async-upload.url']}")
    private String asyncUploadUrl;
    @Value("#{resourceProperties['async-upload.callback.url']}")
    private String asyncUploadCallbackUrl;

    @Value("#{resourceProperties['video.analysis.server']}")
    private String analysisServer;

    @Resource
    private CategoryService categoryService;

    public Page<Video> pageFindAllVideo(String search, Integer type, int pageNo, int pageSize) {
        return pageFindAllVideo(search, type, null, pageNo, pageSize);
    }

    public Page<Video> pageFindAllVideo(String search, Integer type, String publicFlag, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("search", search);
        param.put("type", type);
        param.put("publicFlag", publicFlag);
        long count = videoDao.count(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<Video> list = videoDao.pageSelectVideo(param);
        Page<Video> page = new Page<>();
        page.setTotal(count);
        page.setList(list);
        return page;
    }


    public Page<Video> pageFindAllVideoForAdmin(String search, Integer type, int pageNo, int pageSize) {
        return pageFindAllVideoForAdmin(search, type, null, pageNo, pageSize);
    }

    public Page<Video> pageFindAllVideoForAdmin(String search, Integer type,Integer categoryId, int pageNo, int pageSize) {
        return pageFindAllVideoForAdmin(search, type, categoryId, null, pageNo, pageSize);
    }

    public Page<Video> pageFindAllVideoForAdmin(String search, Integer type, Integer categoryId, String publicFlag, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("search", search);
        param.put("type", type);
        param.put("publicFlag", publicFlag);
        param.put("categoryId", categoryId);
        long count = videoDao.countForAdmin(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<Video> list = videoDao.pageSelectVideoForAdmin(param);
        Page<Video> page = new Page<>();
        page.setTotal(count);
        page.setList(list);
        return page;
    }

    public Page<Video> pageFindAllVideoForAdmin(String search, String publicFlag, Integer checkState, Integer[] types, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("search", search);
        param.put("publicFlag", publicFlag);
        param.put("checkState", checkState);
        param.put("types", types);
        long count = videoDao.countForAdmin(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<Video> list = videoDao.pageSelectVideoForAdmin(param);
        Page<Video> page = new Page<>();
        page.setTotal(count);
        page.setList(list);
        return page;
    }

    public Page<Video> findAllVideo(Map<String, Object> param) {
        long count = videoDao.count(param);
        List<Video> list = videoDao.pageSelectVideo(param);
        Page<Video> page = new Page<>();
        page.setTotal(count);
        page.setList(list);
        return page;
    }

    public List<Video> findVideoByCategory(Integer categoryId) {
        return videoDao.findVideoByCategory(categoryId);
    }

    public List<Video> findVideoByStore(int storeId) {
        return videoDao.findVideoByStore(String.valueOf(storeId));
    }


    //创建video
    public int create(Video video, Integer type) {
        video.setViewCount(0);
        video.setForwardCount(0);
        video.setPeopleCount(0);
        video.setCachedFlag(Constants.NO);
        video.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
        video.setType(type);
        if (video.getPublicFlag() == null ||
                "".equals(video.getPublicFlag()) ||
                "off".equals(video.getPublicFlag().toLowerCase()))
            video.setPublicFlag(Constants.NO);
        else
            video.setPublicFlag(Constants.YES);

        if (video.getUserId() != null) {
            AppUser appUser = appUserService.findUserById(video.getUserId());
            if (appUser != null) {
                video.setAppUser(appUser);
                video.setCopyNo(appUser.getCopyNo());
            }
        }
        int num = videoDao.save(video);

        final Video v = video;
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //上传封面图
                uploadCoverImage(v);
            }
        });
        t1.start();

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //发起Http请求，异步保存视频资源
                sendAsyncUploadRequest(v);
            }
        });
        t2.start();
        return num;
    }

    public Video findById(int id) {
        return videoDao.selectById(id);
    }

    //修改视频地址
    public int updateVideoUrlById(int id, String videoUrl) {
        return videoDao.modifyVideoUrlById(id, videoUrl);
    }

    //修改视频信息
    public int updateVideo(Video video) {
        if (video.getPublicFlag() == null ||
                "".equals(video.getPublicFlag()) ||
                "off".equals(video.getPublicFlag().toLowerCase()))
            video.setPublicFlag(Constants.NO);
        else
            video.setPublicFlag(Constants.YES);
        return videoDao.modifyVideo(video);
    }

    //逻辑删除
    public int updateForDelete(int[] ids) {
        return videoDao.updateForDeleteByIds(ids, Calendar.getInstance().getTime());
    }

    //物理删除
    public int deleteById(int id) {
        //TODO 删除oss资源？
        return videoDao.deleteById(id);
    }

    //分页查询逻辑删除的数据
    public Page<Video> pageFindAllVideoForDeleted(String search, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        long count = videoDao.countForDeleted();
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<Video> list = videoDao.pageSelectVideoForDeleted(param);
        Page<Video> page = new Page<>();
        page.setTotal(count);
        page.setList(list);
        return page;
    }


    public int createBatch(Integer userId, List<Video> videos) {
        AppUser appUser = appUserService.findUserById(userId);
        ;
        Timestamp now = Timestamp.valueOf(UtilDate.getDateFormatter());
        for (Video v : videos) {
            v.setCreatedTime(now);
            v.setPublicFlag(Constants.YES);
            v.setCachedFlag(Constants.NO);
            v.setPeopleCount(0);
            v.setViewCount(0);
            v.setForwardCount(0);
            v.setUserId(userId);
            v.setCopyNo(appUser.getCopyNo());
            v.setAppUser(appUser);
            v.setShowBtnType(1);
            v.setCheckState(Video.CHECK_STATE_PASS);
            v.setType(Constants.VIDEO_TYPE_UPLOAD_FROM_ADMIN);
        }
        int num = videoDao.saveBatch(videos);
        final List<Video> finalVideos = videos;
        //异步保存封面图
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //上传封面图
                uploadCoverImage(finalVideos.toArray(new Video[0]));
            }
        });
        t1.start();

        //异步发起缓存视频请求
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //发起Http请求，异步保存视频资源
                sendAsyncUploadRequest(finalVideos.toArray(new Video[0]));
            }
        });
        t2.start();
        return num;
    }

    private void uploadCoverImage(Video... videos) {
        for (Video v : videos) {
            try {
                URL url = new URL(v.getCoverImg());
                String fileName = UUID.randomUUID().toString().replaceAll("-", "");
                String contentType = URLUtil.getContentType(url);
                if (contentType.equals("application/octet-stream")) {
                    contentType = "image/jpeg";
                }
                String path = OSSClientUtil.putImageDatePathFileWithId(fileName, contentType, v.getAppUser().getCopyNo(), url.openStream());
                if (!"".equals(path)) {
                    videoDao.modifyCoverImgById(v.getId(), OSSClientUtil.URL_PREFIX + path);
                }
            } catch (Exception e) {
                logger.error("上传封面图异常\n 封面图地址: " + v.getCoverImg() + "\n", e);
            }
        }
    }

    private void sendAsyncUploadRequest(Video... videos) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("callback_url", asyncUploadCallbackUrl);
        JSONArray jsonArray = new JSONArray();
        for (Video v : videos) {
            JSONObject subObj = new JSONObject();
            subObj.put("id", v.getAppUser().getCopyNo());
            subObj.put("callback_id", v.getId());
            subObj.put("original_url", v.getVideoUrl());
            jsonArray.put(subObj);
        }
        jsonObject.put("resources", jsonArray);
        HttpRequest.sendPost(asyncUploadUrl, jsonObject.toString());
    }

    public int updateForRecacheVideo(int[] ids) {
        List<Video> list = videoDao.selectByIds(ids);
        AppUser user = null;
        for (Video v : list) {
            if (v.getUserId() != null) {
                user = appUserService.findUserById(v.getUserId());
                if (user != null) {
                    v.setAppUser(user);
                }
            }
            String resp = HttpRequest.sendGet(analysisServer, "url=" + v.getSourceUrl());
            JSONObject jsonObject = new JSONObject(resp);
            int code = jsonObject.getJSONObject("json").getInt("code");
            if (code == 200) {
                v.setVideoUrl(jsonObject.getJSONObject("json").getJSONObject("data").getString("playAddr"));
                v.setCoverImg(jsonObject.getJSONObject("json").getJSONObject("data").getString("cover"));
            }
        }
        final List<Video> finalVideos = list;
        //异步保存封面图
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //上传封面图
                uploadCoverImage(finalVideos.toArray(new Video[0]));
            }
        });
        t1.start();

        //异步发起缓存视频请求
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //发起Http请求，异步保存视频资源
                sendAsyncUploadRequest(finalVideos.toArray(new Video[0]));
            }
        });
        t2.start();
        return videoDao.modifyVideoCachedFlagBatch(ids, Constants.NO);
    }

    public List<Video> findVideoByUserIdWithIgnoreIds(Integer userId, String publicFlag, Integer limit, Integer[] ignoreIds) {
        return videoDao.selectByUserIdWithIgnore(userId, publicFlag, limit, ignoreIds);
    }

    public Page<Video> pageFindVideoWithIgnoreIds(Integer userId, String publicFlag, Integer[] ignoreIds, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("publicFlag", publicFlag);
        param.put("ignoreIds", ignoreIds);
        long count = videoDao.countWithIgnoreIds(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<Video> list = videoDao.pageSelectByParamWithIgnore(param);
        Page<Video> page = new Page<Video>();
        page.setList(list);
        page.setTotal(count);
        return page;
    }

    public Page<Video> pageFindVideoWithIgnoreIds(Integer userId, String publicFlag, Integer[] ignoreIds, int pageNo, int pageSize, Integer index) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("publicFlag", publicFlag);
        param.put("ignoreIds", ignoreIds);
        long count = videoDao.countWithIgnoreIds(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize + index);
        List<Video> list = videoDao.pageSelectByParamWithIgnore(param);
        Page<Video> page = new Page<Video>();
        page.setList(list);
        page.setTotal(count);
        return page;
    }

    //修改置顶状态
    public int updateVideoIsTop(int id, Integer isTop) {
        return videoDao.modifyVideoIsTop(id, isTop);
    }

    public int modifyVideoPeopleCount(Integer videoId) {
        return videoDao.modifyVideoPeopleCount(videoId);
    }

    public int modifyVideoViewCount(Integer videoId) {
        return videoDao.modifyVideoViewCount(videoId);
    }

    public int modifyVideoForwardCount(Integer videoId) {
        return videoDao.modifyVideoForwardCount(videoId);
    }

    public int updateVideoForApp(Video video) {
        if (video.getPublicFlag() == null ||
                "".equals(video.getPublicFlag()))
            video.setPublicFlag(Constants.YES);
        return videoDao.modifyVideoForApp(video);
    }

    public long count(Map<String, Object> param) {
        return videoDao.count(param);
    }

    public int modifyVideoCheckState(Integer id, Integer checkState, String checkStateRemark) {
        return videoDao.modifyVideoCheckStateById(id, checkState, checkStateRemark);
    }

    public int modifyVideoCheckState(Integer id, Integer checkState) {
        return videoDao.modifyVideoCheckStateById(id, checkState, null);
    }

    public int modifyVideoCheckState(List<Integer> ids, Integer checkState) {
        return videoDao.modifyVideoCheckStateByIds(ids, checkState);
    }

    public int modifyVideoCategory(Integer id, Integer categoryId) {
        Video v = videoDao.selectById(id);
        if (v.getCategoryId() != null) {
            //视频分类视频数-1
            categoryService.modifyCategoryVideoCount(v.getCategoryId(), -1);
        }
        //视频分类视频数+1
        categoryService.modifyCategoryVideoCount(categoryId, +1);
        return videoDao.modifyVideoCategoryId(id, categoryId);
    }
}
