package com.juheshi.video.dao;

import com.juheshi.video.entity.AppUserVip;

import java.util.List;

public interface AppUserVipDao {

    int save(AppUserVip appUserVip);

    List<AppUserVip> selectListByUserId(Integer userId);
}
