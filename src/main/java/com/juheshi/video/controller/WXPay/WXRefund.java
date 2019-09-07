package com.juheshi.video.controller.WXPay;



import com.juheshi.video.util.ConfigUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.SortedMap;
import java.util.TreeMap;


public class WXRefund {

	public static String refund(HttpServletRequest request, String refundNo, String orderNo, Integer amount, String refundDesc) {

		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		if ("退款".equals(refundDesc)){
			parameters.put("appid", ConfigUtil.APPID);
		}else{
			parameters.put("appid", ConfigUtil.APPID);
		}
		
		parameters.put("mch_id", ConfigUtil.MCH_ID);
		parameters.put("nonce_str", WXPay.CreateNoncestr());
		parameters.put("sign_type", ConfigUtil.SIGN_TYPE);
		parameters.put("out_trade_no", orderNo);
		parameters.put("out_refund_no", refundNo);
		parameters.put("total_fee", String.valueOf(amount * 100));
		parameters.put("refund_fee", String.valueOf(amount * 100));
		parameters.put("refund_fee_type", "CNY");
		parameters.put("refund_desc", refundDesc);
		parameters.put("notify_url", ConfigUtil.REFUND_NOTIFY_URL);
		String sign = WXPay.createSign("UTF-8", parameters);
		parameters.put("sign", sign);
		String requestXML = WXPay.getRequestXml(parameters);
		System.out.println("---------------" + requestXML + "--------------------");
		
		String result = WXPay.httpsRequest(ConfigUtil.REFUND_URL, "POST", requestXML, true);

		System.out.println("---------------" + result + "--------------------");
		return "success";
	}
}
