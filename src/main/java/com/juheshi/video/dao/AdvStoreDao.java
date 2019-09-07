package com.juheshi.video.dao;

import com.juheshi.video.entity.AdvStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AdvStoreDao {

    int save(AdvStore advStore);

    AdvStore selectById(Integer id);

    List<AdvStore> selectByUserId(Integer userId);

    int modifyStore(AdvStore advStore);

    List<AdvStore> selectListByParam(Map<String, Object> param);

    int deleteById(Integer id);

    int modifyStoreViewCountById(Integer id);

    int modifyStorePeopleCountById(Integer id);

    long count(Map<String, Object> param);

    List<AdvStore> pageSelectByParam(Map<String, Object> param);

    int modifyStoreIndustry(@Param("id") Integer id, @Param("industryId") Integer industryId);
}
