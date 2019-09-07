package com.juheshi.video.dao;

import com.juheshi.video.entity.Stationmaster;
import org.apache.ibatis.annotations.Param;

public interface StationmasterDao {

    int save(Stationmaster stationmaster);

    Stationmaster selectById(Integer id);

    Stationmaster selectByUserId(@Param("userId") Integer userId);

    Stationmaster selectByCopyNo(@Param("copyNo") String copyNo);

    int update(Stationmaster stationmaster);

}
