package com.juheshi.video.controller.WXPay;

import com.juheshi.video.util.ConfigUtil;
import org.jdom.JDOMException;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;


public class WXPayment {

	public static String payment(String paymentNo, String openId, Integer amount) throws IOException, JDOMException {

		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("mch_appid", ConfigUtil.APPID);
		parameters.put("mchid", ConfigUtil.MCH_ID);
		parameters.put("nonce_str", WXPay.CreateNoncestr());
		parameters.put("partner_trade_no", paymentNo);
		parameters.put("openid", openId);
		parameters.put("check_name", "NO_CHECK");
		parameters.put("amount", String.valueOf(amount));
		parameters.put("desc", ConfigUtil.WITHDRAW_DESC);
		parameters.put("spbill_create_ip", "118.31.41.0");
		String sign = WXPay.createSign("UTF-8", parameters);
		parameters.put("sign", sign);
		String requestXML = WXPay.getRequestXml(parameters);
		System.out.println("---------------" + requestXML + "--------------------");
		
		String result = WXPay.httpsRequest(ConfigUtil.PAYMENT_URL, "POST", requestXML, true);

		System.out.println("---------------" + result + "--------------------");
		return result;
	}
}
