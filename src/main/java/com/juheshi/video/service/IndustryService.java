package com.juheshi.video.service;

import com.juheshi.video.dao.IndustryDao;
import com.juheshi.video.entity.Industry;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndustryService {

    @Resource
    private IndustryDao industryDao;

    public Industry findIndustryById(Integer id){
        return industryDao.findIndustryById(id);
    }

    public List<Industry> findAllIndustry(){
        return industryDao.findAllIndustry();
    }

    public List<Industry> findChannelShow(){
        return industryDao.findChannelShow();
    }

    public List<Industry> findLabelShow(){
        return industryDao.findLabelShow();
    }
}
