package com.juheshi.video.dao;

import com.juheshi.video.entity.SysParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysParamDao {
    SysParam selectByParamKey(@Param("paramKey") String paramKey);

    long count(Map<String, Object> param);

    List<SysParam> pageSelectByParam(Map<String, Object> param);
}
