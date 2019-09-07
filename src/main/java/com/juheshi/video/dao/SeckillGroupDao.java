package com.juheshi.video.dao;

import com.juheshi.video.entity.SeckillGroup;

public interface SeckillGroupDao {

    SeckillGroup findOneSeckillGroup(SeckillGroup seckillGroup);

    void insertSeckillGroup(SeckillGroup seckillGroup);

    void updateSeckillGroup(SeckillGroup seckillGroup);

    void deleteSeckillGroup(Integer id);

}
