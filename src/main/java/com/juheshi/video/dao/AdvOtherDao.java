package com.juheshi.video.dao;

import com.juheshi.video.entity.AdvOther;

import java.util.List;
import java.util.Map;

public interface AdvOtherDao {
    AdvOther selectById(Integer id);

    List<AdvOther> selectListByParam(Map<String,Object> param);

    int save(AdvOther advOther);

    int modifyOther(AdvOther advOther);

    int modifyOtherViewCountById(Integer id);

    int modifyOtherPeopleCountById(Integer id);

    int deleteById(Integer id);
}
