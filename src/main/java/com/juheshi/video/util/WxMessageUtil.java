package com.juheshi.video.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WxMessageUtil {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static final String URL_SEND_CUSTOM_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send";

    public static final String URL_SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send";

    private String accessToken;

    private CloseableHttpClient httpClient;

    public WxMessageUtil(String accessToken) {
        this.accessToken = accessToken;
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(300);
        connManager.setDefaultMaxPerRoute(300);
        httpClient = HttpClients.custom().setConnectionManager(connManager).build();
    }

    public void sendCustomServiceTextMessage(String toUser, String content) throws Exception {
        String postUrl = URL_SEND_CUSTOM_MESSAGE + "?access_token=" + accessToken;

        JSONObject textObj = new JSONObject();
        textObj.put("content", content);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("touser", toUser);
        jsonObj.put("msgtype", "text");
        jsonObj.put("text", textObj);
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            int maxTryTime = 3;
            int count = 0;
            do {
                HttpEntity entity = new StringEntity(jsonObj.toString(), ContentType.APPLICATION_JSON);
                httpPost = new HttpPost(postUrl);
                httpPost.setEntity(entity);
                httpPost.setConfig(RequestConfig.custom()
                        .setSocketTimeout(5000)
                        .setConnectTimeout(5000)
                        .setConnectionRequestTimeout(5000)
                        .build());
                response = httpClient.execute(httpPost);
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    String res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    logger.error("发送客服文本消息失败. \nresponse={}", res);
                    return;
                } else {
                    String res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    JSONObject resObj = JSONObject.fromObject(res);
                    if (null != resObj.getString("errcode") && !"".equals(resObj.getString("errcode")) && resObj.getInt("errcode") != 0) {
                        if (resObj.getInt("errcode") == 45015) { //48小时内和公众号无交互的错误
                            throw new Exception("用户48小时内和公众号无交互【errorcode="+45015+"】");
                        }
                        if (resObj.getInt("errcode") == 43004) { //用户未关注公众号错误
                            throw new Exception("用户未关注公众号【errorcode="+43004+"】");
                        }
                        if (count > 0) {
                            logger.warn("第 {} 次重试发送客服文本消息失败. \nresponse={}", count, res);
                        }
                        if (count == 3) {
                            logger.error("3次重试发送客服文本消息失败. \nresponse={}", res);
                        }
                        count++;
                    } else {
                        break;
                    }
                }

            } while (count <= maxTryTime);

        } finally {
            if (response != null)
                response.close();
            if (httpPost != null)
                httpPost.releaseConnection();
        }

    }

    public void sendCustomServiceImageMessage(String toUser, String mediaId) throws Exception {
        String postUrl = URL_SEND_CUSTOM_MESSAGE + "?access_token=" + accessToken;
        JSONObject imageObj = new JSONObject();
        imageObj.put("media_id", mediaId);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("touser", toUser);
        jsonObj.put("msgtype", "image");
        jsonObj.put("image", imageObj);
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            int maxTryTime = 3;
            int count = 0;
            do {
                HttpEntity entity = new StringEntity(jsonObj.toString(), ContentType.APPLICATION_JSON);
                httpPost = new HttpPost(postUrl);
                httpPost.setEntity(entity);
                httpPost.setConfig(RequestConfig.custom()
                        .setSocketTimeout(5000)
                        .setConnectTimeout(5000)
                        .setConnectionRequestTimeout(5000)
                        .build());
                response = httpClient.execute(httpPost);
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    String res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    logger.error("发送客服图片消息失败. \nresponse={}", res);
                } else {
                    String res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    JSONObject resObj = JSONObject.fromObject(res);
                    if (null != resObj.getString("errcode") && !"".equals(resObj.getString("errcode")) && resObj.getInt("errcode") != 0) {
                        if (count > 0) {
                            logger.warn("第 {} 次重试发送客服图片消息失败. \nresponse={}", count, res);
                        }
                        if (count == 3) {
                            logger.error("3次重试发送客服图片消息失败. \nresponse={}", res);
                        }
                        count++;
                    } else {
                        break;
                    }
                }
            } while (count <= maxTryTime);

        } finally {
            if (response != null)
                response.close();
            if (httpPost != null)
                httpPost.releaseConnection();
        }

    }

    public void sendCustomServiceNewsMessage(String toUser, String title, String description, String url, String picUrl) throws Exception {
        String postUrl = URL_SEND_CUSTOM_MESSAGE + "?access_token=" + accessToken;


        JSONObject articleObj = new JSONObject();
        articleObj.put("title", title);
        articleObj.put("description", description);
        articleObj.put("url", url);
        articleObj.put("picurl", picUrl);

        JSONArray articleArr = new JSONArray();
        articleArr.add(articleObj);

        JSONObject articles = new JSONObject();
        articles.put("articles", articleArr);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("touser", toUser);
        jsonObj.put("msgtype", "news");
        jsonObj.put("news", articles);
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            int maxTryTime = 3;
            int count = 0;
            do {
                HttpEntity entity = new StringEntity(jsonObj.toString(), ContentType.APPLICATION_JSON);
                httpPost = new HttpPost(postUrl);
                httpPost.setEntity(entity);
                httpPost.setConfig(RequestConfig.custom()
                        .setSocketTimeout(5000)
                        .setConnectTimeout(5000)
                        .setConnectionRequestTimeout(5000)
                        .build());
                response = httpClient.execute(httpPost);
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    String res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    logger.error("发送客服图文消息失败. \nresponse={}", res);
                } else {
                    String res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    JSONObject resObj = JSONObject.fromObject(res);
                    if (null != resObj.getString("errcode") && !"".equals(resObj.getString("errcode")) && resObj.getInt("errcode") != 0) {
                        if (count > 0) {
                            logger.warn("第 {} 次重试发送客服图文消息失败. \nresponse={}", count, res);
                        }
                        if (count == 3) {
                            logger.error("3次重试发送客服图文消息失败. \nresponse={}", res);
                        }
                        count++;
                    } else {
                        break;
                    }
                }
            } while (count <= maxTryTime);


        } finally {
            if (response != null)
                response.close();
            if (httpPost != null)
                httpPost.releaseConnection();
        }

    }

    public void sendTemplateMessage(String toUser, String templateId, String templateData, String templateUrl) throws Exception {
        String postUrl = URL_SEND_TEMPLATE_MESSAGE + "?access_token=" + accessToken;

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("touser", toUser);
        jsonObj.put("template_id", templateId);
        jsonObj.put("url", templateUrl);
        jsonObj.put("data", JSONObject.fromObject(templateData));
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            int maxTryTime = 3;
            int count = 0;
            do {
                HttpEntity entity = new StringEntity(jsonObj.toString(), ContentType.APPLICATION_JSON);
                httpPost = new HttpPost(postUrl);
                httpPost.setEntity(entity);
                httpPost.setConfig(RequestConfig.custom()
                        .setSocketTimeout(5000)
                        .setConnectTimeout(5000)
                        .setConnectionRequestTimeout(5000)
                        .build());
                response = httpClient.execute(httpPost);
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    String res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    logger.error("发送模板消息失败. \nresponse={}", res);
                } else {
                    String res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    JSONObject resObj = JSONObject.fromObject(res);
                    if (null != resObj.getString("errcode") && !"".equals(resObj.getString("errcode")) && resObj.getInt("errcode") != 0) {
                        if (count > 0) {
                            logger.warn("第 {} 次重试发送模板消息失败. \nresponse={}", count, res);
                        }
                        if (count == 3) {
                            logger.error("3次重试发送模板消息失败. \nresponse={}", res);
                        }
                        count++;
                    } else {
                        break;
                    }
                }
            } while (count <= maxTryTime);

        } finally {
            if (response != null)
                response.close();
            if (httpPost != null)
                httpPost.releaseConnection();
        }
    }

    public void close() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
