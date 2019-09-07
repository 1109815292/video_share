package com.juheshi.video.dao;

import com.juheshi.video.entity.AdvStoreImage;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AdvStoreImageDao {

    AdvStoreImage selectById(Integer id);

    List<AdvStoreImage> selectByAdvStoreId(@Param("advStoreId") Integer advStoreId);

    int save(AdvStoreImage advStoreImage);

    int saveBatch(List<AdvStoreImage> list);

    int updateForDelete(@Param("id") Integer id, @Param("deletedAt") Date deletedAt);

    int updateForDeleteBatch(@Param("ids") List<Integer> ids, @Param("deletedAt") Date deletedAt);

    int deleteById(Integer id);
}
