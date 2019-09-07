package com.juheshi.video.dao;

import com.juheshi.video.entity.SeckillGoods;

import java.util.List;

public interface SeckillGoodsDao {
    SeckillGoods findOneSeckillGoods(SeckillGoods seckillGoods);

    List<SeckillGoods> listSeckillGoods(SeckillGoods seckillGoods);

    int listSeckillGoodsNum(SeckillGoods seckillGoods);

    void insertSeckillGoods(SeckillGoods seckillGoods);

    void updateSeckillGoods(SeckillGoods seckillGoods);

    void deleteSeckillGoods(Integer id);

}
