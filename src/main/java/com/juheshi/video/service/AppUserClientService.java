package com.juheshi.video.service;

import com.juheshi.video.dao.*;
import com.juheshi.video.entity.AppUserClient;
import com.juheshi.video.entity.LogViewAdv;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppUserClientService {

    @Resource
    private AppUserClientDao appUserClientDao;
    @Resource
    private LogViewAdvDao logViewAdvDao;
    @Resource
    private AdvProductDao advProductDao;
    @Resource
    private AdvStoreDao advStoreDao;
    @Resource
    private AdvOtherDao advOtherDao;

    public AppUserClient findById(Integer id){
        return appUserClientDao.selectById(id);
    }

    public AppUserClient selectByUserIdAndclientUserId(Integer userId, Integer clientUserId){
        List<AppUserClient> appUserClientList = appUserClientDao.selectByUserIdAndclientUserId(userId, clientUserId);
        if (appUserClientList.size() > 0){
            return appUserClientList.get(0);
        }else{
            return null;
        }
    }

    public List<AppUserClient> findActiveClient(Integer userId, Integer days) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("days", days);
        return appUserClientDao.selectByParam(param);
    }

    public List<AppUserClient> findAllClient(Integer userId) {

        return findActiveClient(userId, null);
    }

    public long countViewCountByViewType(Integer userId, Integer clientUserId){
        return appUserClientDao.countViewCountByViewType(userId, clientUserId);
    }

    public int createAppUserClient(AppUserClient appUserClient) {
        appUserClient.setViewCount(1);
        appUserClient.setLastViewTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
        return appUserClientDao.save(appUserClient);
    }

    public void addAdvViewCount(Integer advType, Integer advId, Integer userId, Integer clientUserId, Integer viewType){

        int checkView = logViewAdvDao.checkUserView(advType, advId, clientUserId);

        //修改广告查看数和查看人
        if (advType == 1){
            advProductDao.modifyProductViewCountById(advId);
            if (checkView == 0){
                advProductDao.modifyProductPeopleCountById(advId);
            }
        }else if (advType == 2){
            advStoreDao.modifyStoreViewCountById(advId);
            if (checkView == 0){
                advStoreDao.modifyStorePeopleCountById(advId);
            }
        }else if (advType == 3){
            advOtherDao.modifyOtherViewCountById(advId);
            if (checkView == 0){
                advOtherDao.modifyOtherPeopleCountById(advId);
            }
        }

        if (advType > 0){
            //增加广告查看日志
            LogViewAdv logViewAdv = new LogViewAdv();
            logViewAdv.setAdvType(advType);
            logViewAdv.setAdvId(advId);
            logViewAdv.setUserId(clientUserId);
            logViewAdv.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
            logViewAdvDao.save(logViewAdv);
        }

        //增加或修改客户信息
        List<AppUserClient> appUserClientList = appUserClientDao.selectByUserIdAndclientUserId(userId, clientUserId);
        if (appUserClientList.size() > 0){
            AppUserClient appUserClient = appUserClientList.get(0);
            appUserClient.setViewCount(appUserClient.getViewCount() + 1);
            switch (viewType){
                case 1:
                    Integer videoViewCount = appUserClient.getVideoViewCount()!=null?appUserClient.getVideoViewCount():0;
                    appUserClient.setVideoViewCount(videoViewCount + 1);break;
                case 2:
                    Integer productViewCount = appUserClient.getProductViewCount()!=null?appUserClient.getProductViewCount():0;
                    appUserClient.setProductViewCount(productViewCount + 1);break;
                case 3:
                    Integer advViewCount = appUserClient.getAdvViewCount()!=null?appUserClient.getAdvViewCount():0;
                    appUserClient.setAdvViewCount(advViewCount+ 1);break;
                case 4:
                    Integer homePageViewCount = appUserClient.getHomePageViewCount()!=null?appUserClient.getHomePageViewCount():0;
                    appUserClient.setHomePageViewCount(homePageViewCount + 1);break;
            }
            appUserClientDao.updateAppUserClient(appUserClient);
        }else{
            AppUserClient appUserClient = new AppUserClient();
            appUserClient.setUserId(userId);
            appUserClient.setClientUserId(clientUserId);
            appUserClient.setViewCount(1);
            switch (viewType){
                case 1: appUserClient.setVideoViewCount(1);break;
                case 2: appUserClient.setProductViewCount(1);break;
                case 3: appUserClient.setAdvViewCount(1);break;
                case 4: appUserClient.setHomePageViewCount(1);break;
            }
            appUserClient.setLastViewTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
            appUserClientDao.save(appUserClient);
        }

    }

    public int modifyAppUserClient(Integer userId, Integer clientUserId, Integer viewType) {
        return appUserClientDao.modifyViewCount(userId, clientUserId, Timestamp.valueOf(UtilDate.getDateFormatter()));
    }

    public boolean checkIsMyClient(Integer userId, Integer clientUserId, Integer viewType) {
        return appUserClientDao.checkIsMyClient(userId, clientUserId) > 0;
    }
}
