package com.juheshi.video.dao;

import com.juheshi.video.entity.VipTypeSetting;

import java.util.List;
import java.util.Map;

public interface VipTypeSettingDao {

    long count(Map<String, Object> param);

    List<VipTypeSetting> pageSelectVip(Map<String, Object> param);

    VipTypeSetting selectById(Integer id);

    int save(VipTypeSetting vipTypeSetting);

    List<VipTypeSetting> selectListByParam(Map<String,Object> param);

}
