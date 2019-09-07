package com.juheshi.video.dao;

import com.juheshi.video.entity.AdvWx;

import java.util.List;
import java.util.Map;

public interface AdvWxDao {
    int save(AdvWx advWx);

    AdvWx selectById(Integer id);

    int modifyWx(AdvWx wx);

    List<AdvWx> selectListByParam(Map<String, Object> param);

    int deleteById(Integer id);

    int modifyWxViewCountById(Integer id);

    int modifyWxPeopleCountById(Integer id);
}
