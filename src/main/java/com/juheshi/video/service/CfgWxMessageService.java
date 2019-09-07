package com.juheshi.video.service;

import com.juheshi.video.dao.CfgWxMessageDao;
import com.juheshi.video.entity.CfgWxMessage;
import com.juheshi.video.util.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CfgWxMessageService {

    @Resource
    private CfgWxMessageDao cfgWxMessageDao;

    public List<CfgWxMessage> listFindByTypeAndPosition(Integer type, Integer triggerPosition) {
        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
        param.put("triggerPosition", triggerPosition);
        return cfgWxMessageDao.selectByParam(param);
    }

    public Page<CfgWxMessage> pageFindAll(Integer type, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("type", type);
        long count = cfgWxMessageDao.countForPageSelectByParam(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<CfgWxMessage> list = cfgWxMessageDao.pageSelectByParam(param);
        Page<CfgWxMessage> page = new Page<>();
        page.setTotal(count);
        page.setList(list);
        return page;
    }

    public CfgWxMessage findById(Integer id) {
        return cfgWxMessageDao.selectById(id);
    }
}
