package com.juheshi.video.dao;

import com.juheshi.video.entity.Industry;

import java.util.List;

public interface IndustryDao {

    Industry findIndustryById(Integer id);

    List<Industry> findAllIndustry();

    List<Industry> findChannelShow();

    List<Industry> findLabelShow();
}
