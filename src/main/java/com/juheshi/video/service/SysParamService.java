package com.juheshi.video.service;

import com.juheshi.video.dao.SysParamDao;
import com.juheshi.video.entity.SysParam;
import com.juheshi.video.util.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysParamService {

    @Resource
    private SysParamDao sysParamDao;

    @Resource
    private RedisTemplate redisTemplate;

    public SysParam findByKey(String key) {
        SysParam param = (SysParam)redisTemplate.opsForValue ().get ( key );
        if( param == null ){
            param = sysParamDao.selectByParamKey ( key );
            redisTemplate.opsForValue ().set ( key ,param );
        }
        return param;
    }

    public Page<SysParam> pageFindAll(int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        long count = sysParamDao.count(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<SysParam> list = sysParamDao.pageSelectByParam(param);
        Page<SysParam> page = new Page<SysParam>();
        page.setList(list);
        page.setTotal(count);
        return page;
    }
}
