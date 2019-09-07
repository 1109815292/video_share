package com.juheshi.video.util;

import com.juheshi.video.controller.WXPay.WXPay;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jdom.JDOMException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WxUtils {

    private static final Logger logger = LoggerFactory.getLogger(WxUtils.class);

    private static final int TIME_OUT = 3600;

    /**
     * 对外暴露的 JSAPI 接口
     * @param url
     * @return
     */
    public static Map<String,String> getWxSign(String accessToken, String url) throws IOException, JDOMException {
        return sign(getJsapiToken(accessToken),url);
    }

    /**
     * 获取AccessToken存入缓存，腾讯默认一个AccessToken2个小时内有效，过时之后再次获取
     */
    private static String getAccessToken() {
        try {
            String token = "";
            if (null == token || token.length() == 0) {
                String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                        + ConfigUtil.APPID + "&secret=" + ConfigUtil.SECRET;

                //这里大家可以自由选择一个 Http Get 的工具包
                String res = WXPay.httpsRequest(url, "GET", null, false);
                if (StringUtils.isNotEmpty(res)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> map = objectMapper.readValue(res, Map.class);
                    if (map != null) {
                        if (null != map.get("access_token")) {
                            return (String) map.get("access_token");
                        }
                    }
                }
            } else {
                return token;
            }
        } catch (Exception e) {
            logger.error("get access_token fail", e);
        }
        return "";
    }

    private static String getJsapiToken(String token) throws IOException, JDOMException {
        //String token = getAccessToken();
        if (StringUtils.isNotEmpty(token)) {
            String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" + token;
            String res = WXPay.httpsRequest(url, "GET", null, false);
            if (StringUtils.isNotEmpty(res)){
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = objectMapper.readValue(res, Map.class);
                if (map != null) {
                    return (String) map.get("ticket");
                }
            }
        }
        return "";
    }

    private static Map<String, String> sign(String jsapiTicket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapiTicket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;

        logger.warn(string1);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appid",ConfigUtil.APPID);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
