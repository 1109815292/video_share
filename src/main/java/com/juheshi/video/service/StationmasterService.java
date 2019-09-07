package com.juheshi.video.service;

import com.juheshi.video.dao.StationmasterDao;
import com.juheshi.video.entity.District;
import com.juheshi.video.entity.Stationmaster;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StationmasterService {

    @Resource
    private StationmasterDao stationmasterDao;

    @Resource
    private DistrictService districtService;

    public Stationmaster findById(Integer id) {
        return stationmasterDao.selectById(id);
    }

    public Stationmaster findByUserId(Integer userId, boolean withDistrictCascade) {
        Stationmaster result = stationmasterDao.selectByUserId(userId);
        if (result != null && result.getStationDistrictId() != null && withDistrictCascade) {
            result.setDistrict(queryDistrict(result.getStationDistrictId()));
        }
        return result;
    }

    public Stationmaster findByCopyNo(String copyNo, boolean withDistrictCascade) {
        Stationmaster result = stationmasterDao.selectByCopyNo(copyNo);
        if (result != null && result.getStationDistrictId() != null && withDistrictCascade) {
            result.setDistrict(queryDistrict(result.getStationDistrictId()));
        }
        return result;
    }

    public int create(Stationmaster stationmaster) {
        return stationmasterDao.save(stationmaster);
    }

    public int modify(Stationmaster stationmaster) {
        return stationmasterDao.update(stationmaster);
    }

    private District queryDistrict(Integer districtId) {
        return districtService.findByIdWithCascade(districtId);
    }


}
