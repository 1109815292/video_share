package com.juheshi.video.service;

import com.juheshi.video.dao.LogViewVideoDao;
import com.juheshi.video.entity.LogViewVideo;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class LogViewVideoService {

    @Resource
    private LogViewVideoDao logViewVideoDao;

    @Resource
    private VideoService videoService;

    public int createLog(Integer videoId, Integer userId) {
        int exists = logViewVideoDao.checkUserView(videoId, userId);
        if (exists == 0) {
            videoService.modifyVideoPeopleCount(videoId);
        }
        videoService.modifyVideoViewCount(videoId);
        return logViewVideoDao.save(new LogViewVideo(videoId, userId, Timestamp.valueOf(UtilDate.getDateFormatter())));
    }

    public List<Map<String, Object>> findVideoViewLogByUserId(Integer userId, Integer videoUserId){
        return logViewVideoDao.findVideoViewLogByUserId(userId, videoUserId);
    }

    public List<Map<String, Object>> findVideoViewLogByVideoId(Integer videoId, Integer userId){
        return logViewVideoDao.findVideoViewLogByVideoId(videoId, userId);
    }

    public long queryVideoViewCount(Integer videoId) {
        return logViewVideoDao.selectViewCount(videoId);
    }

    public long queryVideoViewPeopleCount(Integer videoId) {
        return logViewVideoDao.selectViewPeopleCount(videoId);
    }

}
