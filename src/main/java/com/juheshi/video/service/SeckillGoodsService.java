package com.juheshi.video.service;

import com.juheshi.video.dao.SeckillGoodsDao;
import com.juheshi.video.entity.SeckillGoods;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SeckillGoodsService {

    @Resource
    private SeckillGoodsDao seckillGoodsDao;

    @Resource
    private RedisTemplate redisTemplate;

    public SeckillGoods findOneSeckillGoods(SeckillGoods seckillGoods){
        SeckillGoods Goods = (SeckillGoods)redisTemplate.opsForHash().get ( "seckillGoods" , seckillGoods.getId () + "" );
        if(Goods == null){
            Goods = seckillGoodsDao.findOneSeckillGoods ( seckillGoods );
            redisTemplate.opsForHash().put ( "seckillGoods" ,seckillGoods.getId ()+"", Goods );
        }
        return Goods;
    }

    public List<SeckillGoods> listSeckillGoods(SeckillGoods seckillGoods){
        List<SeckillGoods> goods = seckillGoodsDao.listSeckillGoods ( seckillGoods );
        return goods;
    }

    public int listSeckillGoodsNum(SeckillGoods seckillGoods){
        int num = seckillGoodsDao.listSeckillGoodsNum ( seckillGoods );
        return num;
    }

    public void insertSeckillGoods(SeckillGoods seckillGoods){
        seckillGoodsDao.insertSeckillGoods ( seckillGoods );
        redisTemplate.opsForHash().put ( "seckillGoods" ,seckillGoods.getId ()+"", seckillGoods );
    }

    public void updateSeckillGoods(SeckillGoods seckillGoods){
        seckillGoodsDao.updateSeckillGoods ( seckillGoods );
        redisTemplate.opsForHash().put ( "seckillGoods" ,seckillGoods.getId ()+"", seckillGoods );
    }

    public void deleteSeckillGoods(Integer id){
        seckillGoodsDao.deleteSeckillGoods ( id );
        redisTemplate.opsForHash().delete ( "seckillGoods" ,id +  "");
    }

}
