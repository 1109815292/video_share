package com.juheshi.video.dao;

import com.juheshi.video.entity.SeckillStatis;

public interface SeckillStatisDao {

    SeckillStatis findOneSeckillStatis(SeckillStatis seckillStatis);

    void insertSeckillStatis(SeckillStatis seckillStatis);

    void updateSeckillStatis(SeckillStatis seckillStatis);

    void deleteSeckillStatis(Integer id);

}
