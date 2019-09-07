package com.juheshi.video.service;

import com.juheshi.video.dao.VarParamDao;
import com.juheshi.video.entity.VarParam;
import com.juheshi.video.util.Page;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Service
public class VarParamService {

    @Resource
    private VarParamDao varParamDao;

    public VarParam findByVarName(String varName) {
        return varParamDao.selectByVarName(varName);
    }

    public int create(VarParam varParam) {
        Date now = Calendar.getInstance().getTime();
        varParam.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter(now)));
        varParam.setUpdatedTime(Timestamp.valueOf(UtilDate.getDateFormatter(now)));
        return varParamDao.save(varParam);
    }

    public int modify(VarParam varParam) {
        varParam.setUpdatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
        return varParamDao.modify(varParam);
    }

    public Page<VarParam> pageFindAll(int pageNo, int pageSize) {

        Map<String, Object> param = new HashMap<String, Object>();
        long count = varParamDao.count(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<VarParam> list = varParamDao.pageSelectByParam(param);
        Page<VarParam> page = new Page<VarParam>();
        page.setList(list);
        page.setTotal(count);
        return page;
    }

    public VarParam findById(Integer id) {
        return varParamDao.selectById(id);
    }
}
