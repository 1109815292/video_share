package com.juheshi.video.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.juheshi.video.common.Constants;
import com.juheshi.video.controller.WXPay.WXPay;
import com.juheshi.video.entity.PayOrder;
import com.juheshi.video.entity.SeckillGoods;
import com.juheshi.video.entity.SeckillOrder;
import com.juheshi.video.service.PayOrderService;
import com.juheshi.video.service.SeckillGoodsService;
import com.juheshi.video.service.SeckillOrderService;
import com.juheshi.video.util.ConfigUtil;
import com.juheshi.video.util.IPUtils;
import com.juheshi.video.util.UtilDate;
import org.jdom.JDOMException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * 订单模块
 */
@Controller
@RequestMapping("/app/seckill")
@SessionAttributes(Constants.SESSION_APP_USER_ID)
public class AppSeckillOrderController {

    @Resource
    private SeckillGoodsService seckillGoodsService;

    @Resource
    private SeckillOrderService seckillOrderService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private PayOrderService payOrderService;

    //订单展示页面
    @RequestMapping(value = "/order", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public JSONObject getOrder(@RequestParam(required = false, name = "userID") String userID,
                               @RequestParam(required = false, name = "seckillID") Integer seckillID,
                               @RequestParam(required = false, name = "price") Double price,
                               @RequestParam(required = false, name = "isCost") Integer isCost,
                               @RequestParam(required = false, name = "receiverAddress") String receiverAddress,
                               @RequestParam(required = false, name = "receiverMobile") String receiverMobile,
                               @RequestParam(required = false, name = "receiver") String receiver,
                               @RequestParam(required = false, name = "mainOrder") Integer mainOrder) {
        JSONObject json = new JSONObject (  );
        SeckillGoods seckillGoods = new SeckillGoods ();
        seckillGoods.setId ( seckillID );
        seckillGoods = seckillGoodsService.findOneSeckillGoods ( seckillGoods );
        if(seckillGoods.getNum () < 1){
            json.put ( "status" , 0 );
            json.put ( "msg" , "秒杀商品不足，请关注公众号不定期秒杀活动！" );
            return json;
        }
        Timestamp timestamp = new Timestamp ( System.currentTimeMillis () );
        if(seckillGoods.getStartTime ().after ( timestamp ) ){
            json.put ( "status" , 0 );
            json.put ( "msg" , "秒杀活动暂未开始，请在活动开始后参与！" );
            return json;
        }
        if(seckillGoods.getEndTime ().before ( timestamp )){
            json.put ( "status" , 0 );
            json.put ( "msg" , "秒杀活动已经结束，请关注公众号不定期秒杀活动！" );
            return json;
        }
        SeckillOrder seckillOrder = new SeckillOrder ();
        seckillOrder.setUserId ( userID );
        seckillOrder.setSeckillId ( seckillID );
        List<SeckillOrder> seckillOrders = seckillOrderService.listSeckillOrder ( seckillOrder );
        int i = 0;
        for (SeckillOrder order : seckillOrders) {
            if(order.getStatus ().equals ( "0" )){
                json.put ( "status" , 0 );
                json.put ( "msg" , "你有未支付订单，请先支付未支付订单！" );
                return json;
            }
            if(order.getStatus ().equals ( "1" )){
                i++;
            }
        }
        if (i >= seckillGoods.getQuota ()){
            json.put ( "status" , 0 );
            json.put ( "msg" , "秒杀商品最多购买" + seckillGoods.getQuota () + "个！" );
            return json;
        }
        seckillOrder.setReceiverAddress ( receiverAddress );
        seckillOrder.setReceiverMobile ( receiverMobile );
        seckillOrder.setReceiver ( receiver );
        seckillOrder.setIsCost ( isCost );
        seckillOrder.setMainOrder ( mainOrder );
        seckillOrder.setMoney ( price );
        seckillOrder.setShopMoney ( price * seckillGoods.getSeparate () / 100 );
        seckillOrder.setStoreId ( seckillGoods.getStoreId () );
        seckillOrder.setCreateTime ( timestamp );
        seckillOrder.setStatus ( "0" );
        seckillOrderService.insertSeckillOrder ( seckillOrder , seckillGoods);
        json.put ( "status" , 1 );
        json.put ( "msg" , "订单成功！" );
        JSONObject back = new JSONObject (  );
        back.put ( "orderId" , seckillOrder.getId () );
        back.put ( "payTime" , redisTemplate.opsForValue ().get ( "SECKILL_TIMEOUT" ) );
        json.put ( "data" , back );
        return json;
    }

    @RequestMapping(value = "/quitorder", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject quitOrder(@RequestParam(required = false, name = "userID") String userID,
                               @RequestParam(required = false, name = "orderId") Integer orderId){
        JSONObject json = new JSONObject (  );
        SeckillOrder seckillOrder = new SeckillOrder ();
        seckillOrder.setUserId ( userID );
        seckillOrder.setId ( orderId );
        seckillOrder.setStatus ( "2" );
        try {
            seckillOrderService.quitSeckillOrder ( seckillOrder );
        }catch (Exception e){
            json.put ( "status" , 0 );
            json.put ( "msg" , "取消订单失败！" );
            return json;
        }
        json.put ( "status" , 1 );
        json.put ( "msg" , "取消订单成功！" );
        return json;
    }

    @RequestMapping(value = "/reback", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject rebackOrder(@RequestParam(required = false, name = "userID") String userID,
                                @RequestParam(required = false, name = "orderId") Integer orderId){
        JSONObject json = new JSONObject (  );
        SeckillOrder seckillOrder = new SeckillOrder ();
        seckillOrder.setStatus ( "5" );
        seckillOrderService.updateSeckillOrder ( seckillOrder );
        //TODO
        //此处做退款惭怍
        json.put ( "status" ,1 );
        json.put ( "msg" , "订单退款成功！" );
        return json;
    }


    @RequestMapping(value = "/paySeckillGoods", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object storeVipPay(HttpServletRequest request, @RequestParam(required = false, name = "userID") Integer userID,
                              @RequestParam(required = false, name = "orderId") Integer orderId) throws IOException, JDOMException {
        JSONObject json = new JSONObject (  );
        SeckillOrder seckillOrder = new SeckillOrder ();
        seckillOrder.setId ( orderId );
        SeckillOrder oneSeckillOrder = seckillOrderService.findOrderByRedis ( seckillOrder );
        if (oneSeckillOrder == null){
            json.put ( "status" , 0 );
            json.put ( "msg" , "订单失效，已过付款期限！请重新下单！" );
            return json;
        }
        PayOrder payOrder = new PayOrder();
        //appUser.getOpenId()此处不知道写什么
        payOrder.setOpenId("");
        payOrder.setUserId(userID);
        payOrder.setPayType(1);
        payOrder.setObjectType(PayOrder.OBJECT_TYPE_VIP);
        payOrder.setObjectId(orderId);
        payOrder.setAmount(Double.valueOf(oneSeckillOrder.getMoney ()) / 100);
        payOrder.setCreatedTime(Timestamp.valueOf( UtilDate.getDateFormatter()));
        payOrder.setState(0);
        payOrder.setIPAddress(IPUtils.getIpAddr(request));
        payOrderService.createPayOrder(payOrder);
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", ConfigUtil.APPID);
        parameters.put("mch_id", ConfigUtil.MCH_ID);
        parameters.put("nonce_str", WXPay.CreateNoncestr());
        parameters.put("body", ConfigUtil.BODY);
        parameters.put("out_trade_no", payOrder.getOrderNo());
        parameters.put("total_fee", String.valueOf(oneSeckillOrder.getMoney ()));
        parameters.put("spbill_create_ip", IPUtils.getIpAddr(request));
        parameters.put("notify_url", ConfigUtil.NOTIFY_URL);
        parameters.put("trade_type", "JSAPI");
        //appUser.getOpenId()此处同上不知道写什么
        parameters.put("openid", "");
        String sign = WXPay.createSign("UTF-8", parameters);
        parameters.put("sign", sign);
        String requestXML = WXPay.getRequestXml(parameters);
        System.out.println("requestXML:" + requestXML);
        String result = WXPay.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL,
                "POST", requestXML, false);// 调用微信统一接口获取prepayId
        payOrder.setPrepayRawData(result);
        Map<String, String> map = WXPay.doXMLParse(result);// 解析微信返回的信息（仍然返回xml格式），以Map形式存储便于取值
        String prepay_id = map.get("prepay_id");
        payOrder.setPrepayId(prepay_id);
        //保存prepay原始数据
        payOrderService.modifyPrepayRawData(payOrder);
        Timestamp time = new Timestamp ( System.currentTimeMillis () );
        seckillOrder.setPayTime ( time );
        seckillOrder.setStatus ( "1" );
        seckillOrder.setTransactionId ( payOrder.getId ().toString () );
        seckillOrderService.updateSeckillOrder ( seckillOrder );
        Map<String, Object> resultMap = new HashMap<String, Object> ();
        if (prepay_id != null) {
            SortedMap<Object, Object> obj = new TreeMap<Object, Object>();
            obj.put("appId", ConfigUtil.APPID);
            String timestamp = String.valueOf(new Date ().getTime());
            int length = timestamp.length();
            obj.put("timeStamp", Long.toString(Integer.valueOf(timestamp.substring(0, length - 3))));
            obj.put("nonceStr", WXPay.CreateNoncestr());
            obj.put("package", "prepay_id=" + prepay_id);
            obj.put("signType", "MD5");
            String paySign = WXPay.createSign("UTF-8", obj);
            obj.put("paySign", paySign); // paySign的生成规则和Sign的生成规则一致
            obj.put("sendUrl", ConfigUtil.SUCCESS_URL); // 付款成功后跳转的页面
            String userAgent = request.getHeader("user-agent");
            //char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger") + 15);
            //obj.put("agent", new String(new char[] { agent }));// 微信版本号，用于前面提到的判断用户手机微信的版本是否是5.0以上版本。

            resultMap.put("code", 200);
            resultMap.put("message", "success");
            resultMap.put("data", obj);
        } else {
            resultMap.put("code", 600001);
            resultMap.put("message", "error");
        }
        return json;
    }
}
