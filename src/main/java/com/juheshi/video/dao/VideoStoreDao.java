package com.juheshi.video.dao;

import com.juheshi.video.entity.Video;
import com.juheshi.video.entity.VideoStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface VideoStoreDao {

    List<VideoStore> findVideoStoreByStoreId(Integer storeId);

    List<VideoStore> findVideoStoreByUserId(Integer userId);

    List<VideoStore> findVideoForPlay(Map<String, Object> param);

    List<Map<String, Object>> findVideoStore(Map<String, Object> param);

    List<Map<String, Object>> findRecommendVideoStore(Map<String, Object> param);

    VideoStore findVideoStoreById(Integer id);

    void insertVideoStore(VideoStore videoStore);

    void updateVideoStore(VideoStore videoStore);

    int updateVideoStoreWithin48hoursForTask();

    int deleteById(Integer id);

    int updateVideoStoreCoverImg(@Param("id") Integer id,@Param("coverImg") String coverImg);

    int updateVideoNotified(@Param("id")Integer id, @Param("notified") Integer notified);

    int batchUpdateVideoNotified(@Param("notified") Integer notified, @Param("ids") List<Integer> ids);

    List<VideoStore> simpleSelectByParam(Map<String, Object> param);
}
