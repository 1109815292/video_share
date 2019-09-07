package com.juheshi.video.dao;

import com.juheshi.video.entity.LogViewVideo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LogViewVideoDao {

    int save(LogViewVideo logViewVideo);

    long selectViewCount(Integer videoId);

    long selectViewPeopleCount(Integer videoId);

    int checkUserView(@Param("videoId") Integer videoId, @Param("userId") Integer userId);

    List<Map<String, Object>> findVideoViewLogByUserId(@Param("userId") Integer userId, @Param("videoUserId") Integer videoUserId);

    List<Map<String, Object>> findVideoViewLogByVideoId(@Param("videoId") Integer videoId, @Param("userId") Integer userId);
}
