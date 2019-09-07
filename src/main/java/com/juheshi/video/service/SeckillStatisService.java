package com.juheshi.video.service;

import com.juheshi.video.dao.SeckillStatisDao;
import com.juheshi.video.entity.SeckillStatis;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SeckillStatisService {

    @Resource
    SeckillStatisDao seckillStatisDao;

    @Resource
    private RedisTemplate redisTemplate;

    public SeckillStatis findOneSeckillStatis(SeckillStatis seckillStatis){
        SeckillStatis Statis = (SeckillStatis)redisTemplate.opsForHash().get ( "SeckillStatis" , seckillStatis.getId () + "" );
        if (Statis == null){
            Statis = seckillStatisDao.findOneSeckillStatis ( seckillStatis );
            redisTemplate.opsForHash().put ( "SeckillStatis" , seckillStatis.getId () + "" ,Statis );
        }
        return Statis;
    }

    public void insertSeckillStatis(SeckillStatis seckillStatis){
        seckillStatisDao.insertSeckillStatis ( seckillStatis );
        redisTemplate.opsForHash().put ( "SeckillStatis" , seckillStatis.getId () + "" ,seckillStatis );
    }

    public void updateSeckillStatis(SeckillStatis seckillStatis){
        seckillStatisDao.updateSeckillStatis ( seckillStatis );
        redisTemplate.opsForHash().put ( "SeckillStatis" , seckillStatis.getId () + "" ,seckillStatis );
    }

    public void deleteSeckillStatis(Integer id){
        seckillStatisDao.deleteSeckillStatis ( id );
        redisTemplate.opsForHash().delete ( "SeckillStatis" , id + "" );
    }
}
