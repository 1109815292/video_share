package com.juheshi.video.dao;

import com.juheshi.video.entity.AppUserPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppUserPageDao {

    int save(AppUserPage appUserPage);

    AppUserPage selectByUserId(@Param("userId") Integer userId);

    int modifyUserPage(AppUserPage appUserPage);

    int modifyPeopleCount(@Param("id") Integer id);

    int modifyViewCount(@Param("id") Integer id);

    List<AppUserPage> getUserPage(Map map);

    int numUserPage(Map map);
}
