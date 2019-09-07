package com.juheshi.video.service;

import com.juheshi.video.dao.AdvOtherDao;
import com.juheshi.video.entity.AdvOther;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdvOtherService {

    @Resource
    private AdvOtherDao advOtherDao;

    public int createAdvOther(AdvOther other) {
        other.setViewCount(0);
        other.setPeopleCount(0);
        if (other.getUrl() != null && !"".equals(other.getUrl())) {
            if (!other.getUrl().contains("http")) {
                other.setUrl("http://" + other.getUrl());
            }
        }
        return advOtherDao.save(other);
    }

    public AdvOther findById(Integer id) {
        return advOtherDao.selectById(id);
    }

    public int modifyAdvOther(AdvOther other) {
        if (other.getUrl() != null && !"".equals(other.getUrl())) {
            if (!other.getUrl().contains("http")) {
                other.setUrl("http://" + other.getUrl());
            }
        }
        return advOtherDao.modifyOther(other);
    }

    public List<AdvOther> findByUserId(Integer userId, Integer type) {
       return findByUserId(null, userId, type);
    }

    public List<AdvOther> findByUserId(String keywords, Integer userId, Integer type) {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("userId", userId);
        param.put("type", type);
        param.put("keywords", keywords);
        return advOtherDao.selectListByParam(param);
    }

    public int deleteById(Integer id) {
        return advOtherDao.deleteById(id);
    }

    public int modifyAdvOtherViewCount(Integer id){
        return advOtherDao.modifyOtherViewCountById(id);
    }

    public int modifyAdvOtherPeopleCount(Integer id){
        return advOtherDao.modifyOtherPeopleCountById(id);
    }
}
