package com.juheshi.video.service;

import com.juheshi.video.common.Constants;
import com.juheshi.video.dao.LogViewAdvDao;
import com.juheshi.video.entity.AppUserClient;
import com.juheshi.video.entity.LogViewAdv;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class LogViewAdvService {

    @Resource
    private LogViewAdvDao logViewAdvDao;

    @Resource
    private AdvProductService advProductService;

    @Resource
    private AdvStoreService advStoreService;

    @Resource
    private AdvWxService advWxService;

    @Resource
    private AppUserClientService appUserClientService;

    @Resource
    private AdvOtherService advOtherService;

    public int createLog(Integer advUserId, Integer advType, Integer advId, Integer userId, Integer viewType) {

        //更新我的客户信息
        if (!appUserClientService.checkIsMyClient(advUserId, userId, viewType)) {
            appUserClientService.createAppUserClient(new AppUserClient(advUserId, userId));
        } else {
            appUserClientService.modifyAppUserClient(advUserId, userId, viewType);
        }

        int exists = logViewAdvDao.checkUserView(advType, advId, userId);
        switch (advType) {
            case Constants.ADV_TYPE_PRODUCT:
                if (exists == 0) {
                    advProductService.modifyAdvProductPeopleCount(advId);
                }
                advProductService.modifyAdvProductViewCount(advId);
                break;
            case Constants.ADV_TYPE_STORE:
                if (exists == 0) {
                    advStoreService.modifyAdvStorePeopleCount(advId);
                }
                advStoreService.modifyAdvStoreViewCount(advId);
                break;
            case Constants.ADV_TYPE_OTHER:
                if (exists == 0) {
                    advOtherService.modifyAdvOtherPeopleCount(advId);
                }
                advOtherService.modifyAdvOtherViewCount(advId);
                break;
        }
        return logViewAdvDao.save(new LogViewAdv(advType, advId, userId, Timestamp.valueOf(UtilDate.getDateFormatter())));
    }

    public long queryVideoViewCount(Integer advType, Integer advId) {
        return logViewAdvDao.selectViewCount(advType, advId);
    }

    public long queryVideoViewPeopleCount(Integer advType, Integer advId) {
        return logViewAdvDao.selectViewPeopleCount(advType, advId);
    }

    public List<Map<String, Object>> findAdvViewLogByAdvId(Integer advType, Integer advId){
        return logViewAdvDao.findAdvViewLogByAdvId(advType, advId);
    }
}
