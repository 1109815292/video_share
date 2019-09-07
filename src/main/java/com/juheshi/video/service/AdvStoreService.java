package com.juheshi.video.service;

import com.juheshi.video.dao.AdvStoreDao;
import com.juheshi.video.dao.AdvStoreImageDao;
import com.juheshi.video.entity.AdvStore;
import com.juheshi.video.entity.AdvStoreImage;
import com.juheshi.video.util.Page;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdvStoreService {

    @Resource
    private AdvStoreDao advStoreDao;

    @Resource
    private AdvStoreImageDao advStoreImageDao;

    public int createAdvStore(AdvStore store) {
        store.setViewCount(0);
        store.setPeopleCount(0);
        if (store.getStoreUrl() != null && !"".equals(store.getStoreUrl())) {
            if (!store.getStoreUrl().contains("http")) {
                store.setStoreUrl("http://" + store.getStoreUrl());
            }
        }
        int num = advStoreDao.save(store);
        if (store.getImages() != null && store.getImages().size() > 0) {
            for (AdvStoreImage asi : store.getImages()) {
                asi.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
                asi.setSort(0);
                asi.setAdvStoreId(store.getId());
            }
            advStoreImageDao.saveBatch(store.getImages());
        }
        return num;
    }

    public AdvStore findById(Integer id) {
        AdvStore store = advStoreDao.selectById(id);
        if (store != null) {
            List<AdvStoreImage> list = advStoreImageDao.selectByAdvStoreId(store.getId());
            if (list != null && list.size() > 0) {
                store.setImages(list);
            }
        }
        return store;
    }

    public int modifyAdvStore(AdvStore store) {
        if (store.getStoreUrl() != null && !"".equals(store.getStoreUrl())) {
            if (!store.getStoreUrl().contains("http")) {
                store.setStoreUrl("http://" + store.getStoreUrl());
            }
        }
        int num = advStoreDao.modifyStore(store);
        if (store.getDeletedStoreImageIds() != null && !"".equals(store.getDeletedStoreImageIds())) {
            String[] tmpArr = store.getDeletedStoreImageIds().split(",");
            List<Integer> ids = new ArrayList<>();
            for (String s : tmpArr) {
                ids.add(Integer.parseInt(s));
            }
            //删除图片
            advStoreImageDao.updateForDeleteBatch(ids, Timestamp.valueOf(UtilDate.getDateFormatter()));
        }

        if (store.getImages() != null && store.getImages().size() > 0) {
            List<AdvStoreImage> newData = new ArrayList<>();
            for (AdvStoreImage asi : store.getImages()) {
                if (asi.getId() == null) {
                    asi.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
                    asi.setSort(0);
                    asi.setAdvStoreId(store.getId());
                    newData.add(asi);
                }
            }
            if (newData.size() > 0) {
                advStoreImageDao.saveBatch(newData);
            }
        }
        return num;
    }

    public List<AdvStore> findByUserId(Integer userId) {
        return findByUserId(null, userId);
    }

    public List<AdvStore> findByUserId(String keywords, Integer userId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("keywords", keywords);
        return advStoreDao.selectListByParam(param);
    }

    public List<AdvStore> findByType(String stationCopyNo, Integer industryId, Integer sort) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("stationCopyNo", stationCopyNo);
        param.put("industryId", industryId);
        param.put("sort", sort);
        return advStoreDao.selectListByParam(param);
    }

    public int deleteById(Integer id) {
        return advStoreDao.deleteById(id);
    }

    public int modifyAdvStoreViewCount(Integer id) {
        return advStoreDao.modifyStoreViewCountById(id);
    }

    public int modifyAdvStorePeopleCount(Integer id) {
        return advStoreDao.modifyStorePeopleCountById(id);
    }

    public Page<AdvStore> pageFindAdvStore(String stationCopyNo, String search, Integer industryId, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("stationCopyNo", stationCopyNo);
        param.put("search", search);
        param.put("industryId", industryId);
        long count = advStoreDao.count(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<AdvStore> list = advStoreDao.pageSelectByParam(param);
        Page<AdvStore> page = new Page<>();
        page.setTotal(count);
        page.setList(list);
        return page;
    }

    public int modifyAdvStoreIndustry(Integer id, Integer industryId) {
        return advStoreDao.modifyStoreIndustry(id, industryId);
    }
}
