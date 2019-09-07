package com.juheshi.video.dao;

import com.juheshi.video.entity.Video;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface VideoDao {


    long count(Map<String, Object> param);

    long countForDeleted();

    List<Video> pageSelectVideo(Map<String, Object> param);

    List<Video> pageSelectVideoForDeleted(Map<String, Object> param);

    List<Video> findVideoByStore(@Param("storeId") String storeId);

    List<Video> findVideoByCategory(Integer categoryId);

    Video selectById(int id);

    int save(Video video);

    int saveBatch(List<Video> list);

    int modifyCoverImgById(@Param("id") int id, @Param("coverImg") String coverImg);

    int modifyVideoUrlById(@Param("id") int id, @Param("videoUrl") String coverImg);

    int modifyVideo(Video video);

    int updateForDeleteByIds(@Param("ids") int[] ids, @Param("deletedAt") Date deletedAt);

    int deleteById(int id);

    int modifyVideoCachedFlagBatch(@Param("ids") int[] ids, @Param("cachedFlag") String cachedFlag);

    int modifyVideoIsTop(@Param("id") int id, @Param("isTop") Integer isTop);

    List<Video> selectByIds(@Param("ids") int[] ids);

    List<Video> selectByUserIdWithIgnore(@Param("userId") Integer userId, @Param("publicFlag") String publicFlag, @Param("limit") Integer limit, @Param("ignoreIds") Integer[] ignoreIds);

    int modifyVideoViewCount(Integer id);

    int modifyVideoPeopleCount(Integer id);

    int modifyVideoForwardCount(Integer id);


    long countWithIgnoreIds(Map<String, Object> param);

    List<Video> pageSelectByParamWithIgnore(Map<String, Object> param);

    int modifyVideoForApp(Video video);

    int modifyVideoCheckStateById(@Param("id") Integer id, @Param("checkState") Integer checkState, @Param("checkStateRemark") String checkStateRemark);

    int modifyVideoCheckStateByIds(@Param("ids") List<Integer> ids, @Param("checkState") Integer checkState);


    long countForAdmin(Map<String, Object> param);

    List<Video> pageSelectVideoForAdmin(Map<String, Object> param);

    int modifyVideoCategoryId(@Param("id")Integer id, @Param("categoryId")Integer categoryId);
}
