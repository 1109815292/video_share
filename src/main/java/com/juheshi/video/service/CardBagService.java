package com.juheshi.video.service;

import com.juheshi.video.dao.CardBagDao;
import com.juheshi.video.entity.CardBag;
import com.juheshi.video.entity.SeckillOrder;
import com.juheshi.video.util.ExamCodeUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class CardBagService {

    @Resource
    private CardBagDao cardBagDao;

    @Resource
    private SeckillOrderService seckillOrderService;

    @Resource
    private RedisTemplate redisTemplate;

    public CardBag findOneCardBag(CardBag cardBag){
        CardBag bag = (CardBag)redisTemplate.opsForHash().get ( "CardBag" , cardBag.getId () + "" );
        if (bag == null){
            bag = cardBagDao.findOneCardBag ( cardBag );
            redisTemplate.opsForHash().put ( "CardBag" , cardBag.getId () + "" ,bag );
        }
        return bag;
    }

    public List<CardBag> listCardBag(CardBag cardBag){
        List<CardBag> cardBags = cardBagDao.listCardBag ( cardBag );
        return cardBags;
    }

    public void insertCardBag(CardBag cardBag){
        String examCode = ExamCodeUtil.getExamCode ( cardBag.getSeckillGoods ().getStoreId () , cardBag.getUserId () , cardBag.getOrderId () , cardBag.getSeckillId () );
        cardBag.setExamCode ( examCode );
        cardBagDao.insertCardBag ( cardBag );
        redisTemplate.opsForHash().put ( "CardBag" , cardBag.getId () + "" ,cardBag );
    }

    public void updateCardBag(CardBag cardBag){
        SeckillOrder seckillOrder = new SeckillOrder ();
        seckillOrder.setId ( cardBag.getOrderId () );
        SeckillOrder oneSeckillOrder = seckillOrderService.findOneSeckillOrder ( seckillOrder );
        oneSeckillOrder.setExamine ( "1" );
        oneSeckillOrder.setExamineTime ( new Timestamp ( System.currentTimeMillis () ) );
        seckillOrderService.updateSeckillOrder ( oneSeckillOrder );
    }

    public void deleteCardBag(Integer id){
        cardBagDao.deleteCardBag ( id );
        redisTemplate.opsForHash().delete ( "CardBag" , id + "" );
    }
}
