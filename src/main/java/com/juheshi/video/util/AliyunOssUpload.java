package com.juheshi.video.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

import java.io.File;

public class AliyunOssUpload {
	
    private static String endpoint = "";
    private static String endpointOut = "";
    private static String accessKeyId = "";
    private static String accessKeySecret = "";
    private static String bucketName = "";
    
    
    public static void uploadFileToAliyun(String myKey, File file){
    	// 创建OSSClient实例
    	OSSClient ossClient = new OSSClient(endpointOut, accessKeyId, accessKeySecret);
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        // 设置上传内容类型docx
        meta.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        // 设置上传文件长度
        meta.setContentLength(file.length());

        // 上传文件
        try {
      	
            PutObjectResult m_Result = ossClient.putObject(bucketName, myKey, file, meta);
            System.out.println(m_Result.getETag());
            
            ossClient.shutdown();// 关闭client
            
        } catch (OSSException oe) {
            oe.printStackTrace();
            throw new OSSException();
        } catch (ClientException ce) {
            ce.printStackTrace();
            throw new ClientException();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
        }
    }
    
    
    

}
