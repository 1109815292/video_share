package com.juheshi.video.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.CardBag;
import com.juheshi.video.service.CardBagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * APP卡包管理
 */
@Controller
@RequestMapping("/app/cardbag")
@SessionAttributes(Constants.SESSION_APP_USER_ID)
public class AppCardBagController {

    @Resource
    private CardBagService cardBagService;

    @RequestMapping(value = "/getCardBag", produces = "text/html;charset=UTF-8")
    public JSONObject getCardBag(@RequestParam(required = false, name = "userID") Integer userID){
        JSONObject json = new JSONObject (  );
        CardBag cardBag = new CardBag ();
        cardBag.setUserId ( userID );
        try {
            List<CardBag> cardBags = cardBagService.listCardBag ( cardBag );
            json.put ( "status" , 1 );
            json.put ( "msg" , "查询完成！" );
            json.put ( "data" , cardBags );
        }catch (Exception e){
            json.put ( "status" , 0 );
            json.put ( "msg" , "查询异常！" );
        }
        return json;
    }

    @RequestMapping(value = "/checkCard", produces = "text/html;charset=UTF-8")
    public JSONObject checkCard(@RequestParam(required = false, name = "examCode") String examCode,
                                @RequestParam(required = false, name = "storeId") String storeId){
        JSONObject json = new JSONObject (  );
        CardBag cardBag = new CardBag ();
        cardBag.setExamCode ( examCode );
        cardBag.setStoreId ( storeId );
        List<CardBag> cardBags = cardBagService.listCardBag ( cardBag );
        if (cardBags == null){
            json.put ( "status" , 0 );
            json.put ( "msg" , "店铺没有此秒杀劵，请核对后使用！" );
        }
        CardBag card = cardBags.get ( 0 );
        if (card.getSeckillOrder ().getExamine ().equals ( "1" )){
            json.put ( "status" , 0 );
            json.put ( "msg" , "秒杀劵已核销！" );
        }
        Timestamp timestamp = new Timestamp ( System.currentTimeMillis () );
        if (card.getSeckillGoods ().getConsumeTime ().after ( timestamp )){
            json.put ( "status" , 0 );
            json.put ( "msg" , "秒杀券已过期！" );
        }
        try {
            cardBagService.updateCardBag(card);
            json.put ( "status" , 1 );
            json.put ( "msg" , "核销完成！" );
            json.put ( "data" , cardBags );
        }catch (Exception e){
            json.put ( "status" , 0 );
            json.put ( "msg" , "核销失败，请联系管理员！" );
        }
        return json;
    }
}
