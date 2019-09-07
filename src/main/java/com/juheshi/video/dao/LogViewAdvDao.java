package com.juheshi.video.dao;

import com.juheshi.video.entity.LogViewAdv;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LogViewAdvDao {

    int save(LogViewAdv logViewAdv);

    long selectViewCount(@Param("advType") Integer advType, @Param("advId") Integer advId);

    long selectViewPeopleCount(@Param("advType") Integer advType, @Param("advId") Integer advId);

    int checkUserView(@Param("advType") Integer advType, @Param("advId") Integer advId, @Param("userId") Integer userId);

    List<Map<String, Object>> findAdvViewLogByAdvId(@Param("advType") Integer advType, @Param("advId") Integer advId);
}
