package com.juheshi.video.service;

import com.juheshi.video.dao.AdvStoreDao;
import com.juheshi.video.dao.AdvStoreImageDao;
import com.juheshi.video.dao.VideoStoreDao;
import com.juheshi.video.dao.VideoStoreImageDao;
import com.juheshi.video.entity.AdvStoreImage;
import com.juheshi.video.entity.VideoStore;
import com.juheshi.video.entity.VideoStoreImage;
import com.juheshi.video.util.OSSClientUtil;
import com.juheshi.video.util.UtilDate;
import com.juheshi.video.util.WxUploadUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoStoreService {

    @Resource
    private VideoStoreDao videoStoreDao;
    @Resource
    private VideoStoreImageDao videoStoreImageDao;

    @Resource
    private WxUploadUtil wxUploadUtil;

    public VideoStore findVideoStoreById(Integer id) {
        return videoStoreDao.findVideoStoreById(id);
    }

    public List<VideoStore> findVideoStoreByUserId(Integer userId) {
        return videoStoreDao.findVideoStoreByUserId(userId);
    }

    public List<VideoStore> findVideoStoreByStoreId(Integer storeId) {
        return videoStoreDao.findVideoStoreByStoreId(storeId);
    }

    public List<Map<String, Object>> findVideoStore(Integer industryId, String stationCopyNo, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (industryId != null && industryId > 0) {
            param.put("industryId", industryId);
        }
        param.put("stationCopyNo", stationCopyNo);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        return videoStoreDao.findVideoStore(param);
    }

    public List<Map<String, Object>> findVideoStore(Integer industryId, String stationCopyNo, int pageNo, int pageSize, Integer[] ignoreIds) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (industryId != null && industryId > 0) {
            param.put("industryId", industryId);
        }
        if (ignoreIds != null) {
            param.put("ignoreIds", ignoreIds);
        }
        param.put("stationCopyNo", stationCopyNo);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        return videoStoreDao.findVideoStore(param);
    }


    public List<Map<String, Object>> findRecommendVideoStore(String stationCopyNo, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("stationCopyNo", stationCopyNo);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        return videoStoreDao.findRecommendVideoStore(param);
    }

    public List<VideoStoreImage> findImageByVideoId(Integer videoId) {
        return videoStoreImageDao.selectByVideoId(videoId);
    }

    public void insertVideoStore(VideoStore videoStore) {
        videoStoreDao.insertVideoStore(videoStore);
        if (videoStore.getCoverImgMediaId() != null && !"".equals(videoStore.getCoverImgMediaId())) {
            uploadCoverImg(videoStore);
        }
        if (videoStore.getStoreImgMediaIds() != null && !"".equals(videoStore.getStoreImgMediaIds())) {
            uploadStoreImg(videoStore);
        }
    }

    private void uploadStoreImg(VideoStore videoStore) {
        final Map<Integer, String> map = new HashMap<>();
        final String finalCopyNo = videoStore.getCopyNo();
        String[] mediaIdArr = videoStore.getStoreImgMediaIds().split(",");
        for (String mediaId : mediaIdArr) {
            VideoStoreImage vsi = new VideoStoreImage(videoStore.getId(), mediaId, 0, Timestamp.valueOf(UtilDate.getDateFormatter()));
            videoStoreImageDao.save(vsi);
            map.put(vsi.getId(), mediaId);
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for(Integer id : map.keySet()) {
                    String mediaId = map.get(id);
                    String ossUrl = wxUploadUtil.uploadImageToOSS(mediaId, finalCopyNo);
                    videoStoreImageDao.updateVideoStoreImage(id, ossUrl);
                }
            }
        });
        t.start();
    }

    private void uploadCoverImg(VideoStore videoStore) {
        final String finalMediaId = videoStore.getCoverImgMediaId();
        final String finalCopyNo = videoStore.getCopyNo();
        final Integer finalVideoStoreId = videoStore.getId();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String ossUrl = wxUploadUtil.uploadImageToOSS(finalMediaId, finalCopyNo);
                //更新对应记录
                updateVideoStoreCoverImg(finalVideoStoreId, ossUrl);
            }
        });
        t.start();
    }

    public void updateVideoStore(VideoStore videoStore) {
        videoStoreDao.updateVideoStore(videoStore);
        //异步下载封面图并上传到oss
        if (videoStore.getCoverImgMediaId() != null && !"".equals(videoStore.getCoverImgMediaId())) {
            uploadCoverImg(videoStore);
        }
        if (videoStore.getStoreImgMediaIds() != null && !"".equals(videoStore.getStoreImgMediaIds())) {
            uploadStoreImg(videoStore);
        }
    }

    public int updateVideoStoreWithin48hoursForTask() {
        return videoStoreDao.updateVideoStoreWithin48hoursForTask();
    }

    public int deleteById(Integer id) {
        return videoStoreDao.deleteById(id);
    }

    public int updateVideoStoreCoverImg(Integer id, String coverImg) {
        return videoStoreDao.updateVideoStoreCoverImg(id, coverImg);
    }

    public int updateVideoStoreNotified(Integer id, Integer notified) {
        return videoStoreDao.updateVideoNotified(id, notified);
    }

    public int updateVideoStoreNotifiedForBatch(Integer notified, List<Integer> ids) {
        return videoStoreDao.batchUpdateVideoNotified(notified, ids);
    }

    public List<VideoStore> findVideoStoreByStationCopyNo(String stationCopyNo, Integer checkState, int notified) {
        Map<String,Object> param = new HashMap<>();
        param.put("stationCopyNo", stationCopyNo);
        param.put("checkState", checkState);
        param.put("notified", notified);
        return videoStoreDao.simpleSelectByParam(param);
    }
}
