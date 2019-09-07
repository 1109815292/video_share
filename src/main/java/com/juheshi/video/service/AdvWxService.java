package com.juheshi.video.service;

import com.juheshi.video.dao.AdvWxDao;
import com.juheshi.video.entity.AdvWx;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdvWxService {

    @Resource
    private AdvWxDao advWxDao;

    public int createAdvWx(AdvWx wx) {
        wx.setViewCount(0);
        wx.setPeopleCount(0);
        return advWxDao.save(wx);
    }

    public AdvWx findById(Integer id) {
        return advWxDao.selectById(id);
    }

    public int modifyAdvWx(AdvWx wx) {
        return advWxDao.modifyWx(wx);
    }

    public List<AdvWx> findByUserId(Integer userId, Integer type) {
        return findByUserId(null, userId,type);
    }


    public List<AdvWx> findByUserId(String keywords, Integer userId, Integer type) {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("userId", userId);
        param.put("type", type);
        param.put("keywords", keywords);
        return advWxDao.selectListByParam(param);
    }

    public int deleteById(Integer id) {
        return advWxDao.deleteById(id);
    }

    public int modifyAdvWxViewCount(Integer id){
        return advWxDao.modifyWxViewCountById(id);
    }

    public int modifyAdvWxPeopleCount(Integer id){
        return advWxDao.modifyWxPeopleCountById(id);
    }
}
