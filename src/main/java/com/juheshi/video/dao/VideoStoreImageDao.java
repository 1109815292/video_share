package com.juheshi.video.dao;

import com.juheshi.video.entity.AdvStoreImage;
import com.juheshi.video.entity.VideoStoreImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoStoreImageDao {

    VideoStoreImage selectById(@Param("id") Integer id);

    List<VideoStoreImage> selectByVideoId(@Param("videoId") Integer videoId);

    int save(VideoStoreImage videoStoreImage);

    int saveBatch(List<VideoStoreImage> list);

    int updateVideoStoreImage(@Param("id") Integer id,@Param("picUrl") String picUrl);

    int deleteById(Integer id);
}
