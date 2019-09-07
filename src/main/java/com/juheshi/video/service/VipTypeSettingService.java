package com.juheshi.video.service;

import com.juheshi.video.dao.VipTypeSettingDao;
import com.juheshi.video.entity.Video;
import com.juheshi.video.entity.VipTypeSetting;
import com.juheshi.video.util.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VipTypeSettingService {

    @Resource
    private VipTypeSettingDao vipTypeSettingDao;


    public Page<VipTypeSetting> pageFindAllVip(int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        long count = vipTypeSettingDao.count(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<VipTypeSetting> list = vipTypeSettingDao.pageSelectVip(param);

        Page<VipTypeSetting> page = new Page<>();
        page.setTotal(count);
        page.setList(list);
        return page;
    }

    public List<VipTypeSetting> findAllVip() {
        return findAllVip(null, null);
    }

    public List<VipTypeSetting> findAllVip(Integer type) {
        return findAllVip(null, type);
    }

    public List<VipTypeSetting> findAllVip(String enabledFlag) {
        return findAllVip(enabledFlag, null);
    }

    public List<VipTypeSetting> findAllVip(String enabledFlag, Integer type) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("enabledFlag", enabledFlag);
        param.put("type", type);
        return vipTypeSettingDao.selectListByParam(param);
    }

    public VipTypeSetting findVipById(Integer id) {
        return vipTypeSettingDao.selectById(id);
    }
}
