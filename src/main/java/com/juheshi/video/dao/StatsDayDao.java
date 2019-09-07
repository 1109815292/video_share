package com.juheshi.video.dao;

import com.juheshi.video.entity.StatsDay;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface StatsDayDao {

    int save(StatsDay statsDay);

    int update(StatsDay statsDay);

    StatsDay selectByYearMonthDay(@Param("statsYear") int statsYear, @Param("statsMonth")int statsMonth, @Param("statsDay")int statsDay);

    int checkExists(@Param("statsYear") int statsYear, @Param("statsMonth")int statsMonth, @Param("statsDay")int statsDay);

    StatsDay selectSumByParam(Map<String,Object> param);
}
