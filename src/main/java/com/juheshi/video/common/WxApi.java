package com.juheshi.video.common;

import com.juheshi.video.util.ConfigUtil;
import com.juheshi.video.util.http.HttpUtil;
import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 微信统一接口
 * Created by on 2017/2/13.
 */
public class WxApi {

    /**
     * 获取ACCESS_TOKEN
     */
    private static String getAccessToken() {
        String get_access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + ConfigUtil.APPID + "&secret=" + ConfigUtil.SECRET;
        String json = HttpUtil.getUrl(get_access_token_url);

        JSONObject jsonObject = JSONObject.fromObject(json);
        String access_token = jsonObject.getString("access_token");
        return access_token;
    }

    /**
     * 生成临时二维码并返回ticket
     */
    private static String createQrcode(String copyNo,String accessToken, long expiresIn) {
//        String accessToken = getAccessToken();

        String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;

        JSONObject data = new JSONObject();
        data.put("action_name", "QR_STR_SCENE");
        data.put("expire_seconds", expiresIn);//20天过期

        JSONObject action_info = new JSONObject();

        JSONObject scene = new JSONObject();
        //action_info.put("scene_id", 30382);
        scene.put("scene_str", copyNo);

        action_info.put("scene", scene);

        data.put("action_info", action_info);


        String responseBody = HttpUtil.sendJson(requestUrl, data);
        if (responseBody == null) {
            return null;
        }
        JSONObject responseJson = JSONObject.fromObject(responseBody);
        if (responseJson == null) {
            return null;
        }
        String ticket = responseJson.getString("ticket");
        if (ticket == null || "".equals(ticket)) {
            return null;
        }

        return ticket;
    }

    /**
     * 根据barAccessKey与ticket获取二进制二维码数据流
     */
    public static ByteArrayOutputStream getQrcodeByteStream(String copyNo,String accessToken, long expiresIn) {
        if (copyNo == null) {
            return null;
        }

        // 创建永久二维码
        String ticket = createQrcode(copyNo,accessToken, expiresIn);
        if (ticket == null) {
            return null;
        }

        // 下载二维码，用于存储到数据库
        String requestUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(60000);
            InputStream inStream = conn.getInputStream();

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            return outStream;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
