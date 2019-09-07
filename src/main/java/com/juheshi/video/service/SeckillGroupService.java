package com.juheshi.video.service;

import com.juheshi.video.dao.SeckillGroupDao;
import com.juheshi.video.entity.SeckillGroup;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SeckillGroupService {

    @Resource
    SeckillGroupDao seckillGroupDao;

    @Resource
    private RedisTemplate redisTemplate;

    public SeckillGroup findOneSeckillGroup(SeckillGroup seckillGroup){
        SeckillGroup Group = (SeckillGroup)redisTemplate.opsForHash().get ( "SeckillGroup" , seckillGroup.getId () + "" );
        if (Group == null){
            Group = seckillGroupDao.findOneSeckillGroup ( seckillGroup );
            redisTemplate.opsForHash().put ( "SeckillGroup" , seckillGroup.getId () + "" ,Group );
        }
        return Group;
    }

    public void insertSeckillGroup(SeckillGroup seckillGroup){
        seckillGroupDao.insertSeckillGroup ( seckillGroup );
        redisTemplate.opsForHash().put ( "SeckillGroup" , seckillGroup.getId () + "" ,seckillGroup );
    }

    public void updateSeckillGroup(SeckillGroup seckillGroup){
        seckillGroupDao.updateSeckillGroup ( seckillGroup );
        redisTemplate.opsForHash().put ( "SeckillGroup" , seckillGroup.getId () + "" ,seckillGroup );
    }

    public void deleteSeckillGroup(Integer id){
        seckillGroupDao.deleteSeckillGroup ( id );
        redisTemplate.opsForHash().delete ( "SeckillGroup" , id + "" );
    }
}
