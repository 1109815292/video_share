package com.juheshi.video.service;

import com.juheshi.video.dao.VideoStoreImageDao;
import com.juheshi.video.entity.VideoStoreImage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VideoStoreImageService {

    @Resource
    private VideoStoreImageDao videoStoreImageDao;

    public VideoStoreImage findById(Integer id) {
        return videoStoreImageDao.selectById(id);
    }

    public int deleteById(Integer id) {
        return videoStoreImageDao.deleteById(id);
    }
}
