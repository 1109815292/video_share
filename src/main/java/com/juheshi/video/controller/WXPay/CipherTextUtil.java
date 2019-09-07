package com.juheshi.video.controller.WXPay;

import com.juheshi.video.util.ConfigUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;


public class CipherTextUtil  {

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS7Padding";
    /**
     * 生成key
     */
    private static SecretKeySpec key = new SecretKeySpec(MD5Util.MD5Encode(ConfigUtil.API_KEY, null).toLowerCase().getBytes(), ALGORITHM);

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * AES解密
     */
    public static String decryptData(byte[] b) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(b));
    }

    
    public static String decryptRefundNotify(String req_info) throws Exception{
    	byte[] b = Base64.decode(req_info.getBytes("UTF-8"));
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(b)); 
    }
  
    public static void main(String[] args) throws Exception{
    	String req_info = "/ca3BR6xyWdADYvX6VoEmCP4V/e/c/dKWG1F00JjwBItZ5JeWQPziu1Uz8wgMfJYBp5i+rGeT6W8lAE8S99vGkhnhJ+nZmzQ11fnlUzIl3aoT+xL6WlcgcB9+AZPbovK7wi+980/es1D8nCL2J+C0RCytueiN7g/rBw/VdY4X5BUadlQKufnDL112nlTZ16zh8rGfmgvvP2XBAS/IYPx1x+ZHDnNNrZSUwlyEOi3vJRmONQDkvbrWHvEQXyaTghx+YsHpD6mjq5ln5zKQaOrgw6tBsT77lAgPTkEHDfMRuBqDmh9VCa66Bz079pkod0CIwNeT2RvEtNPEyEbXo51XZTGnl3w5aTxaIsWJt6RQheq952wBq3nM5U8AYqiO4DM1iO0iwFiRIDeri+buKFKSUP11eP2wtEcyviMBY5ta96I3nYA/CsXxeBnYFwWuin/+pbeToDYT2uAouwFKb988YbQ+KVOYmd6pdo+1b5w2vdgtGdcF8izgx0lcoC1e6kwx9Jqzl8YXPoPIHQoJxqptMDrRSPVNzfsoPJC832IrVnIFkEu/3U9ADU7l/A/n6TLN8jSqUO2XkjUr6inUMU5R8oZDBjzVitBTMNCmqqmaWmRX2FW3rDSyVTZbpzUajf1swgnr+NDEK3yBAJe+iRq0got/QAOKpe0fe6RkYhtWG72uRbFCIuYEeF44+1FAb227NXffWKhyLDAIPNIDlUcjZ7HkVpxbPGsixGvbM+45XBfoGSzw/impnuYBozPlnXxbefHKktCGeplQYsV2GjHpFvzh+17bFfGzgxRQjcLTNw8QgrP67r6StUbLeRj7w6wt5bgFqm6NuVG8SCBydhTkHEacppkYbBE0dF0idcrJcWHg8kMqEx3F5LQQ7h1go98VTnptymt/0s9Ti5yUQiTc51jmQVxzGHYKVC4JkXJAg8L2IAICp7lSd03Vlx59+Ta+QxXf3QcNjxjJm+m4ExFxB+B8LSyMZvYmhr0JAMs2VYrSP+A5u/xLxup4qQKVKzYdKbonk9kWVh1nuSCCQ1Bi4GEaF0VB/641eEMWfXlyqg=";
    	byte[] b = Base64.decode(req_info.getBytes("UTF-8"));
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, key);
        String result = new String(cipher.doFinal(b));
        System.out.println("~~~~~~~~~result: " + result + "~~~~~~~~~~~~~");
    }

}
