package com.juheshi.video.service;

import com.juheshi.video.dao.LogViewHomepageDao;
import com.juheshi.video.entity.AppUserPage;
import com.juheshi.video.entity.LogViewHomepage;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service
public class LogViewHomepageService {

    @Resource
    private LogViewHomepageDao logViewHomepageDao;

    @Resource
    private AppUserPageService appUserPageService;


    public int createLog(Integer userPageId, Integer userId) {
        int exists = logViewHomepageDao.checkUserView(userPageId, userId);
        if (exists == 0) {
            appUserPageService.modifyPeopleCount(userPageId);
        }
        appUserPageService.modifyViewCount(userPageId);
        return logViewHomepageDao.save(new LogViewHomepage(userPageId, userId, Timestamp.valueOf(UtilDate.getDateFormatter())));
    }



}
