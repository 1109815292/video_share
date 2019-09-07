package com.juheshi.video.service;

import com.juheshi.video.dao.AppUserPageDao;
import com.juheshi.video.entity.AppUserPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AppUserPageService {

    @Resource
    private AppUserPageDao appUserPageDao;

    public int modifyPeopleCount(Integer userPageId) {
       return appUserPageDao.modifyPeopleCount(userPageId);
    }

    public int modifyViewCount(Integer userPageId) {
        return appUserPageDao.modifyViewCount(userPageId);
    }

    public List<AppUserPage> getUserPage(Map map){
        return appUserPageDao.getUserPage ( map );
    }

    public int numUserPage(Map map){
        return appUserPageDao.numUserPage ( map );
    }
}
