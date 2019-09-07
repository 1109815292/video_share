package com.juheshi.video.dao;

import com.juheshi.video.entity.VarParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface VarParamDao {

    VarParam selectByVarName(@Param("varName") String varName);

    int save(VarParam varParam);

    int modify(VarParam varParam);

    long count(Map<String, Object> param);

    List<VarParam> pageSelectByParam(Map<String, Object> param);

    VarParam selectById(Integer id);
}
