/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.juheshi.video.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncryptUtil {  

    private static final String UTF8 = "utf-8";  
  
    /** 
     * MD5数字签名 
     * @param src 
     * @return 
     * @throws Exception 
     */  
    public String md5Digest(String src) throws Exception {
       // 定义数字签名方法, 可用：MD5, SHA-1  
       MessageDigest md = MessageDigest.getInstance("MD5");  
       byte[] b = md.digest(src.getBytes(UTF8));  
       return this.byte2HexStr(b);  
    }  
      
    /** 
     * BASE64编码
     * @param src 
     * @return 
     * @throws Exception 
     */  
    public String base64Encoder(String src) throws Exception {  
        BASE64Encoder encoder = new BASE64Encoder();  
        return encoder.encode(src.getBytes(UTF8));  
    }  
      
    /** 
     * BASE64解码
     * @param dest 
     * @return 
     * @throws Exception 
     */  
    public String base64Decoder(String dest) throws Exception {  
        BASE64Decoder decoder = new BASE64Decoder();  
        return new String(decoder.decodeBuffer(dest), UTF8);  
    }  
      
    /** 
     * 字节数组转化为大�?6进制字符�?
     * @param b 
     * @return 
     */  
    private String byte2HexStr(byte[] b) {  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < b.length; i++) {  
            String s = Integer.toHexString(b[i] & 0xFF);  
            if (s.length() == 1) {  
                sb.append("0");  
            }  
            sb.append(s.toUpperCase());  
        }  
        return sb.toString();  
    }  
 
    /** 
     * 截取汉字
     * @param
     * @return 
     */     
	public String getSubString(String str, int pstart, int pend) {
		String resu = "";
		int beg = 0;
		int end = 0;
		int count1 = 0;
		char[] temp = new char[str.length()];
		str.getChars(0, str.length(), temp, 0);
		boolean[] bol = new boolean[str.length()];
		for (int i = 0; i < temp.length; i++) {
			bol[i] = false;
			if ((int) temp[i] > 255) {
				count1++;
				bol[i] = true;
			}
		}
		if (pstart > str.length() + count1) {
			resu = null;
		}
		if (pstart > pend) {
			resu = null;
		}
		if (pstart < 1) {
			beg = 0;
		} else {
			beg = pstart - 1;
		}
		if (pend > str.length() + count1) {
			end = str.length() + count1;
		} else {
			end = pend;//
		}
		//
		if (resu != null) {
			if (beg == end) {
				int count = 0;
				if (beg == 0) {
					if (bol[0] == true)
						resu = null;
					else
						resu = new String(temp, 0, 1);
				} else {
					int len = beg;//
					for (int y = 0; y < len; y++) {//
						if (bol[y] == true)
							count++;
						len--;//
					}

					if (count == 0) {// ˵��ǰ��û������
						if ((int) temp[beg] > 255)// ˵���Լ�������
							resu = null;// ���ؿ�
						else
							resu = new String(temp, beg, 1);
					} else {// ǰ�������ģ���ôһ������Ӧ��2���ַ����?
						if ((int) temp[len + 1] > 255)// ˵���Լ�������
							resu = null;// ���ؿ�
						else
							resu = new String(temp, len + 1, 1);
					}
				}
			} else {// ������������µıȽ�?
				int temSt = beg;
				int temEd = end - 1;// ��������?
				for (int i = 0; i < temSt; i++) {
					if (bol[i] == true)
						temSt--;
				}// ѭ����Ϻ�temSt��ʾǰ�ַ��������?
				for (int j = 0; j < temEd; j++) {
					if (bol[j] == true)
						temEd--;
				}// ѭ����Ϻ�temEd-1��ʾ����ַ��������
				if (bol[temSt] == true)// ˵�����ַ�˵���������Ǻ��ֵĺ�벿�֣���ôӦ���ǲ���ȡ��?
				{
					int cont = 0;
					for (int i = 0; i <= temSt; i++) {
						cont++;
						if (bol[i] == true)
							cont++;
					}
					if (pstart == cont)// ��ż��Ӧ��,���pstart<cont��Ҫ��
						temSt++;// ����һλ��ʼ
				}
				if (bol[temEd] == true) {// ��ΪtemEd��ʾsubstring
											// ���������˴���һ�����֣�����Ҫȷ���Ƿ�Ӧ�ú��������?
					int cont = 0;
					for (int i = 0; i <= temEd; i++) {
						cont++;
						if (bol[i] == true)
							cont++;
					}
					if (pend < cont)// �Ǻ��ֵ�ǰ�벿�ֲ�Ӧ��
						temEd--;// ����ֻȡ��ǰһ��
				}
				if (temSt == temEd) {
					resu = new String(temp, temSt, 1);
				} else if (temSt > temEd) {
					resu = null;
				} else {
					resu = str.substring(temSt, temEd + 1);
				}
			}
		}
		return resu;// ���ؽ��?
	}  

	 // 下面的代码将字符串以正确方式显示（包括回车，换行，空格）,把空格转换成&nbsp
	public String strConvert (String str) {
		 if (str != null && !"".equals(str)) {
		 while (str.indexOf("\n") != -1) {
		 str = str.substring(0, str.indexOf("\n")) + "<br>"
		 + str.substring(str.indexOf("\n") + 1);
		 }
		 while (str.indexOf(" ") != -1) {
		 str = str.substring(0, str.indexOf(" ")) + "&nbsp;"
		 + str.substring(str.indexOf(" ") + 1);
		 }
		 }
		 return str;
	}
	
	
	//取上�?��核算周期
	public String preCyc(String cyc){
		Integer cycYear = Integer.parseInt(cyc.substring(0,4));
		Integer cycMonth = Integer.parseInt(cyc.substring(4));
		
		if (cycMonth == 1){
			cycYear = cycYear-1;
			cycMonth = 12;
		}else{
			cycMonth = cycMonth-1;
		}
		
		String preCyc = "";
		if (cycMonth>9){
			preCyc = String.valueOf(cycYear) + String.valueOf(cycMonth);
		}else{
			preCyc = String.valueOf(cycYear) + "0" + String.valueOf(cycMonth);
		}
		return preCyc;
	}
	
	//取下�?��核算周期
	public String nextCyc(String cyc){
		Integer cycYear = Integer.parseInt(cyc.substring(0,4));
		Integer cycMonth = Integer.parseInt(cyc.substring(4));
		
		if (cycMonth == 12){
			cycYear = cycYear+1;
			cycMonth = 1;
		}else{
			cycMonth = cycMonth+1;
		}
		
		String nextCyc = "";
		if (cycMonth>9){
			nextCyc = String.valueOf(cycYear) + String.valueOf(cycMonth);
		}else{
			nextCyc = String.valueOf(cycYear) + "0" + String.valueOf(cycMonth);
		}
		return nextCyc;
	}
	
	
	public String formatDouble(double s) {
		DecimalFormat fmt = new DecimalFormat("##,###,##0.00");
		return fmt.format(s);
	}
	
	public String formatMoney(double s) {
		DecimalFormat fmt = new DecimalFormat("\u00A4##,###,##0.00");
		return fmt.format(s);
	}

	public static String filterEmoji(String source) {
		if (source == null) {
			return source;
		}
		Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
				Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
		Matcher emojiMatcher = emoji.matcher(source);
		if (emojiMatcher.find()) {
			source = emojiMatcher.replaceAll("*");
			return source;
		}
		return source;
	}
}  
