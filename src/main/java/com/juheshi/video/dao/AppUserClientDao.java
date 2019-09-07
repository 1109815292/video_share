package com.juheshi.video.dao;

import com.juheshi.video.entity.AppUserClient;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface AppUserClientDao {

    int save(AppUserClient appUserClient);

    void updateAppUserClient(AppUserClient appUserClient);

    AppUserClient selectById(Integer id);

    List<AppUserClient> selectByUserIdAndclientUserId(@Param("userId") Integer userId, @Param("clientUserId") Integer clientUserId);

    List<AppUserClient> selectByUserId(@Param("userId") Integer userId);

    List<AppUserClient> selectByParam(Map<String, Object> param);

    long countViewCountByViewType(@Param("userId") Integer userId, @Param("clientUserId") Integer clientUserId);

    int modifyViewCount(@Param("userId") Integer userId, @Param("clientUserId") Integer clientUserId,
                        @Param("lastViewTime") Timestamp lastViewTime);

    int checkIsMyClient(@Param("userId") Integer userId, @Param("clientUserId") Integer clientUserId);
}
