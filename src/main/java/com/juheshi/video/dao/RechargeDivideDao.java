package com.juheshi.video.dao;

import com.juheshi.video.entity.DivideRate;
import com.juheshi.video.entity.RechargeDivide;

import java.util.List;
import java.util.Map;

public interface RechargeDivideDao {

    List<DivideRate> findAllDivideRate();

    List<Map<String, Object>> findDivideByDivideUser(int userId);

    Double countDivide(Map<String, Object> param);

    void insertRechargeDivide(RechargeDivide rechargeDivide);

    void updateRechargeDivide(RechargeDivide rechargeDivide);

    long countForPageSelectByParamWithCascade(Map<String,Object> param);

    List<RechargeDivide> pageSelectByParamWithCascade(Map<String,Object> param);

    RechargeDivide findById(Integer id);
}
