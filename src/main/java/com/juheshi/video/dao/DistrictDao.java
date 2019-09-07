package com.juheshi.video.dao;

import com.juheshi.video.entity.District;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DistrictDao {

    District selectById(Integer id);

    List<District> selectByParentId(@Param("parentId") Integer parentId);

    District selectByIdWithCascade(Integer id);


}
