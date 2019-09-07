package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.controller.WXPay.MD5Util;
import com.juheshi.video.controller.WXPay.WXPay;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.PayOrder;
import com.juheshi.video.entity.VipTypeSetting;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.PayOrderService;
import com.juheshi.video.service.VipTypeSettingService;
import com.juheshi.video.util.ConfigUtil;
import com.juheshi.video.util.IPUtils;
import com.juheshi.video.util.UtilDate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.jdom.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping(value = "/app/advPay")
@SessionAttributes(Constants.SESSION_APP_USER_ID)
public class WxPayController {

    @Resource
    private AppUserService appUserService;
    @Resource
    private PayOrderService payOrderService;
    @Resource
    private VipTypeSettingService vipTypeSettingService;

    /*会员线上充值*/
    @RequestMapping(value = "/wxPay", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object wxPay(HttpServletRequest request, @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId) throws IOException, JDOMException {

        Integer vipTypeId = Integer.valueOf(request.getParameter("vipTypeId"));
        VipTypeSetting vipTypeSetting = vipTypeSettingService.findVipById(vipTypeId);
        AppUser appUser = appUserService.findUserById(userId);

        PayOrder payOrder = new PayOrder();
        payOrder.setOpenId(appUser.getOpenId());
        payOrder.setUserId(userId);
        payOrder.setPayType(1);
        payOrder.setObjectType(PayOrder.OBJECT_TYPE_VIP);
        payOrder.setObjectId(vipTypeId);
        payOrder.setAmount(Double.valueOf(vipTypeSetting.getPresentPrice()) / 100);
        payOrder.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
        payOrder.setState(0);
        payOrder.setIPAddress(IPUtils.getIpAddr(request));

        payOrderService.createPayOrder(payOrder);

        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", ConfigUtil.APPID);
        parameters.put("mch_id", ConfigUtil.MCH_ID);
        parameters.put("nonce_str", WXPay.CreateNoncestr());
        parameters.put("body", ConfigUtil.BODY);
        parameters.put("out_trade_no", payOrder.getOrderNo());
        parameters.put("total_fee", String.valueOf(vipTypeSetting.getPresentPrice()));
        parameters.put("spbill_create_ip", IPUtils.getIpAddr(request));
        parameters.put("notify_url", ConfigUtil.NOTIFY_URL);
        parameters.put("trade_type", "JSAPI");
        parameters.put("openid", appUser.getOpenId());
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
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (prepay_id != null) {
            SortedMap<Object, Object> obj = new TreeMap<Object, Object>();
            obj.put("appId", ConfigUtil.APPID);
            String timestamp = String.valueOf(new Date().getTime());
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

        return resultMap;
    }

    /*实体店开通*/
    @RequestMapping(value = "/storeVipPay", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object storeVipPay(HttpServletRequest request, @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId) throws IOException, JDOMException {

        Integer industryId = Integer.valueOf(request.getParameter("industryId"));
        String storeCopyNo = request.getParameter("storeCopyNo");
        VipTypeSetting vipTypeSetting = null;
        List<VipTypeSetting> vipTypeSettingList = vipTypeSettingService.findAllVip(2);
        if (vipTypeSettingList.size() > 0){
            vipTypeSetting = vipTypeSettingList.get(0);
        }

        AppUser appUser = appUserService.findUserById(userId);

        PayOrder payOrder = new PayOrder();
        payOrder.setOpenId(appUser.getOpenId());
        payOrder.setUserId(userId);
        payOrder.setPayType(1);
        payOrder.setObjectType(PayOrder.OBJECT_TYPE_STORE_VIP);
        payOrder.setObjectId(vipTypeSetting.getId());
        payOrder.setAmount(Double.valueOf(vipTypeSetting.getPresentPrice()) / 100);
        payOrder.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
        payOrder.setState(0);
        payOrder.setIPAddress(IPUtils.getIpAddr(request));
        payOrder.setStoreCopyNo(storeCopyNo);
        payOrder.setIndustryId(industryId);

        payOrderService.createPayOrder(payOrder);

        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", ConfigUtil.APPID);
        parameters.put("mch_id", ConfigUtil.MCH_ID);
        parameters.put("nonce_str", WXPay.CreateNoncestr());
        parameters.put("body", ConfigUtil.BODY);
        parameters.put("out_trade_no", payOrder.getOrderNo());
        parameters.put("total_fee", String.valueOf(vipTypeSetting.getPresentPrice()));
        parameters.put("spbill_create_ip", IPUtils.getIpAddr(request));
        parameters.put("notify_url", ConfigUtil.NOTIFY_URL);
        parameters.put("trade_type", "JSAPI");
        parameters.put("openid", appUser.getOpenId());
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
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (prepay_id != null) {
            SortedMap<Object, Object> obj = new TreeMap<Object, Object>();
            obj.put("appId", ConfigUtil.APPID);
            String timestamp = String.valueOf(new Date().getTime());
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

        return resultMap;
    }

    @RequestMapping(value = "wxPaySuccess")
    public void paySuccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        System.out.println("~~~~~~~~~~~~~~~~付款成功~~~~~~~~~");
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
        Map<Object, Object> map = WXPay.doXMLParse(result);
        for (Object keyValue : map.keySet()) {
            System.out.println(keyValue + "=" + map.get(keyValue));
        }
        if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
            payOrderService.savePaySuccess(map.get("out_trade_no").toString(), result);
            response.getWriter().write(WXPay.setXML("SUCCESS", "")); // 告诉微信服务器，我收到信息了，不要在调用回调action了
            System.out.println("-------------" + WXPay.setXML("SUCCESS", ""));
        }
    }

    @RequestMapping(value = "refundSuccess")
    public void refundSuccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        System.out.println("~~~~~~~~~~~~~~~~退款成功~~~~~~~~~");
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
        Map<Object, Object> encryptMap = WXPay.doXMLParse(result);


        String req_info = encryptMap.get("req_info").toString();
        //通知信息为加密字符串，对通知信息进行解密
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] req_info_b = Base64.decode(req_info.getBytes("UTF-8"));
        SecretKeySpec key = new SecretKeySpec(MD5Util.MD5Encode(ConfigUtil.API_KEY, null).toLowerCase().getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        String refundStr = new String(cipher.doFinal(req_info_b));
        Map<Object, Object> map = WXPay.doXMLParse(refundStr);
        for (Object keyValue : map.keySet()) {
            System.out.println(keyValue + "=" + map.get(keyValue));
        }
        if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
            // 对数据库的操作
            payOrderService.saveRefundSuccess(map.get("out_refund_no").toString(), refundStr);
            response.getWriter().write(WXPay.setXML("SUCCESS", "")); // 告诉微信服务器，我收到信息了，不要在调用回调action了
            System.out.println("-------------" + WXPay.setXML("SUCCESS", ""));
        }
    }


    //用户取消支付
    @RequestMapping(value = "/cancel", produces = "application/json;charset=UTF-8")
    public String cancel(String prepayId, @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("app/advPay/pay-cancel");
        PayOrder order = payOrderService.findByPrepayId(prepayId);
        if (order != null && order.getUserId().equals(userId) && order.getState().equals(PayOrder.STATE_PREPAY)) {
            payOrderService.modifyPayOrderState(order.getId(), PayOrder.STATE_PREPAY, PayOrder.STATE_CANCEL, "前端支付取消回调");
        }
//        return modelAndView;
        return "redirect:/app/advPay/openVip";
    }

    //用户支付成功
    @RequestMapping(value = "/success", produces = "application/json;charset=UTF-8")
    public String success(String prepayId, @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("app/advPay/pay-success");
        PayOrder order = payOrderService.findByPrepayId(prepayId);
        if (order != null && order.getUserId().equals(userId) && order.getState().equals(PayOrder.STATE_PREPAY)) {
            payOrderService.modifyPayOrderState(order.getId(), PayOrder.STATE_PREPAY, PayOrder.STATE_PAID, "前端支付成功回调");
        }
//        return modelAndView;
        return "redirect:/app/manageCenter";
    }

    //支付错误
    public String error() {
        return "error";
    }
}
