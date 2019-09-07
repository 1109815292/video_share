package com.juheshi.video.service;

import com.juheshi.video.dao.DistrictDao;
import com.juheshi.video.entity.District;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DistrictService {

    @Resource
    private DistrictDao districtDao;

    public List<District> findByParentId(Integer parentId) {
        return districtDao.selectByParentId(parentId);
    }


    public District findByIdWithCascade(Integer districtId) {
        return districtDao.selectByIdWithCascade(districtId);
    }
}
