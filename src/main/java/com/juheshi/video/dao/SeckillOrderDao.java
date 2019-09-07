package com.juheshi.video.dao;

import com.juheshi.video.entity.SeckillOrder;

import java.util.List;

public interface SeckillOrderDao {

    SeckillOrder findOneSeckillOrder(SeckillOrder seckillOrder);

    List<SeckillOrder> listSeckillOrder (SeckillOrder seckillOrder);

    void insertSeckillOrder(SeckillOrder seckillOrder);

    void updateSeckillOrder(SeckillOrder seckillOrder);

    void deleteSeckillOrder(Integer id);

    int listSeckillOrderNum(SeckillOrder seckillOrder);


}
