package com.juheshi.video.common.wechat;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Hex;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by xya on 17-2-13.
 */
public class WeChatAPI {

    //微信基础支持：获取access_token接口
    public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APP_ID&secret=APP_SECRET";

    //微信向用户发送消息：发送客服消息接口
    public static final String CUSTOM_SEND = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";


    /**
     * 微信获取 access_token 接口
     * @param appId
     * @param appSecret
     * @return
     */
    public static JSONObject getAccessToken(final String appId, final String appSecret) {
        String apiUrl = ACCESS_TOKEN;
        apiUrl = apiUrl.replace("APP_ID", appId).replace("APP_SECRET", appSecret);
        return HttpsRequestUtil.httpRequest(apiUrl, "GET", null);
    }

    /**
     * 微信发送客服消息接口
     * @param accessToken
     * @param body
     * @return
     */
    public static JSONObject sendCustomMessage(final String accessToken, final String body) {
        String apiUrl = CUSTOM_SEND;
        apiUrl = apiUrl.replace("ACCESS_TOKEN", accessToken);
        return HttpsRequestUtil.httpRequest(apiUrl, "POST", body);
    }

    public final static String TOKEN = "e5841df2166dd424a57127423d276bbe";
    public final static String EVENT_KEY_PREFIX = "qrscene_";


    public static boolean checkSignature(HttpServletRequest request,HttpServletResponse response) throws NoSuchAlgorithmException, IOException {
        // 开发者提交信息后，微信服务器将发送GET请求到填写的服务器地址URL上，GET请求携带参数
        String signature = request.getParameter("signature");// 微信加密签名（token、timestamp、nonce。）
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");// 随机数
        String echostr = request.getParameter("echostr");// 随机字符串
        // 将token、timestamp、nonce三个参数进行字典序排序
        String[] params = new String[] { TOKEN, timestamp, nonce };
        Arrays.sort(params);
        // 将三个参数字符串拼接成一个字符串进行sha1加密
        String clearText = params[0] + params[1] + params[2];
        String algorithm = "SHA-1";
        MessageDigest md = MessageDigest.getInstance(algorithm);
        String sign = byteToStr((md.digest(clearText.toString().getBytes())));
        if(sign.equals(signature.toUpperCase())){
            if(null != echostr && !"".equals(echostr))
                response.getWriter().print(echostr);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }


    public static XStream createXstream() {
        return new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    boolean cdata = false;
                    Class<?> targetClass = null;

                    @Override
                    public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
                        super.startNode(name, clazz);
                        // 业务处理，对于用XStreamCDATA标记的Field，需要加上CDATA标签
                        if (!name.equals("xml")) {
                            cdata = needCDATA(targetClass, name);
                        } else {
                            targetClass = clazz;
                        }
                    }

                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        if (cdata) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                        } else {
                            writer.write(text);
                        }
                    }
                };
            }
        });
    }

    private static boolean needCDATA(Class<?> targetClass, String fieldAlias) {
        boolean cdata = false;
        // first, scan self
        cdata = existsCDATA(targetClass, fieldAlias);
        if (cdata)
            return cdata;
        // if cdata is false, scan supperClass until java.lang.Object
        Class<?> superClass = targetClass.getSuperclass();
        while (!superClass.equals(Object.class)) {
            cdata = existsCDATA(superClass, fieldAlias);
            if (cdata)
                return cdata;
            superClass = superClass.getClass().getSuperclass();
        }
        return false;
    }

    private static boolean existsCDATA(Class<?> clazz, String fieldAlias) {
        if ("MediaId".equals(fieldAlias)) {
            return true; // 特例添加 morning99
        }
        // scan fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 1. exists XStreamCDATA
            if (field.getAnnotation(XStreamCDATA.class) != null) {
                XStreamAlias xStreamAlias = field.getAnnotation(XStreamAlias.class);
                // 2. exists XStreamAlias
                if (null != xStreamAlias) {
                    if (fieldAlias.equals(xStreamAlias.value()))// matched
                        return true;
                } else {// not exists XStreamAlias
                    if (fieldAlias.equals(field.getName()))
                        return true;
                }
            }
        }
        return false;
    }
}
