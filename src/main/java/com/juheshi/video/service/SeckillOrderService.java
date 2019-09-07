package com.juheshi.video.service;

import com.juheshi.video.dao.SeckillOrderDao;
import com.juheshi.video.entity.SeckillGoods;
import com.juheshi.video.entity.SeckillOrder;
import com.juheshi.video.entity.SeckillStatis;
import com.juheshi.video.entity.SysParam;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillOrderService {

    @Resource
    private SeckillOrderDao seckillOrderDao;

    @Resource
    private SeckillGoodsService seckillGoodsService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SeckillStatisService seckillStatisService;

    @Resource
    private SysParamService sysParamService;

    public SeckillOrder findOneSeckillOrder(SeckillOrder seckillOrder){
        SeckillOrder Order = (SeckillOrder)redisTemplate.opsForHash ().get ( "SeckillOrder" , seckillOrder.getId () + "" );
        if (Order == null){
            Order = seckillOrderDao.findOneSeckillOrder ( seckillOrder );
            redisTemplate.opsForHash ().put ( "SeckillOrder" , seckillOrder.getId () + "" ,Order );
        }
        return Order;
    }

    public SeckillOrder findOrderByRedis(SeckillOrder seckillOrder){
        SeckillOrder Order = (SeckillOrder)redisTemplate.opsForHash ().get ( "SeckillOrder" , seckillOrder.getId () + "" );
        return Order;
    }

    public List<SeckillOrder> listSeckillOrder (SeckillOrder seckillOrder){
        List<SeckillOrder> seckillOrders = seckillOrderDao.listSeckillOrder ( seckillOrder );
        return seckillOrders;
    }

    public int listSeckillOrderNum (SeckillOrder seckillOrder){
        int num = seckillOrderDao.listSeckillOrderNum ( seckillOrder );
        return num;
    }

    public void insertSeckillOrder(SeckillOrder seckillOrder , SeckillGoods seckillGoods){
        synchronized (seckillGoods){
            seckillGoods.setNum ( seckillGoods.getNum () - 1 );
        }
        seckillGoodsService.updateSeckillGoods ( seckillGoods );
        seckillOrderDao.insertSeckillOrder ( seckillOrder );
        SeckillStatis seckillStatis = new SeckillStatis ( seckillOrder.getSeckillId () );
        seckillStatisService.insertSeckillStatis ( seckillStatis );
        redisTemplate.opsForHash ().put ( "SeckillOrder" , seckillOrder.getId () + "" ,seckillOrder );
        SysParam seckill_timeout = (SysParam)redisTemplate.opsForValue ().get ( "SECKILL_TIMEOUT" );
        redisTemplate.expire ( seckillOrder.getId () + "" ,Long.valueOf (seckill_timeout.getParamValue ()), TimeUnit.SECONDS );
    }

    public void updateSeckillOrder(SeckillOrder seckillOrder){
        SeckillStatis seckillStatis = new SeckillStatis ();
        seckillStatis.setSeckillId ( seckillOrder.getSeckillId () );
        seckillStatis = seckillStatisService.findOneSeckillStatis ( seckillStatis );
        seckillStatis.setSalaNum ( seckillStatis.getSalaNum () + 1 );
        seckillStatis.setTotelPrice ( seckillStatis.getTotelPrice () + seckillOrder.getMoney () );
        seckillStatisService.updateSeckillStatis ( seckillStatis );
        seckillOrderDao.updateSeckillOrder ( seckillOrder );
        redisTemplate.opsForHash ().put ( "SeckillOrder" , seckillOrder.getId () + "" ,seckillOrder );
    }

    public void deleteSeckillOrder(Integer id){
        seckillOrderDao.deleteSeckillOrder ( id );
        redisTemplate.opsForHash ().delete ( "SeckillOrder" , id + "" );
    }

    public void quitSeckillOrder(SeckillOrder seckillOrder){
        SeckillGoods seckillGoods = new SeckillGoods ();
        synchronized (seckillGoods){
            seckillGoods.setId ( seckillOrder.getSeckillId () );
            seckillGoods = seckillGoodsService.findOneSeckillGoods ( seckillGoods );
            seckillGoods.setNum ( seckillGoods.getNum () + 1 );
        }
        seckillGoodsService.updateSeckillGoods ( seckillGoods );
        seckillOrderDao.updateSeckillOrder ( seckillOrder );
        redisTemplate.opsForHash ().delete ( "SeckillOrder" , seckillOrder.getId () + "" );
    }
}
