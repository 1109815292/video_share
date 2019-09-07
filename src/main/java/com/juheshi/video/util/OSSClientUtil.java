package com.juheshi.video.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by Auser on 2017/11/7.
 */
public class OSSClientUtil {

    //添加/结尾
    public final static String URL_PREFIX = "http://oss.mogupai.com/";

    // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
    // 如果您还没有创建Bucket，endpoint选择请参看文档中心的“开发人员指南 > 基本概念 > 访问域名”，
    // 链接地址是：https://help.aliyun.com/document_detail/oss/user_guide/oss_concept/endpoint.html?spm=5176.docoss/user_guide/endpoint_region
    // endpoint的格式形如“http://oss-cn-hangzhou.aliyuncs.com/”，注意http://后不带bucket名称，
    // 比如“http://bucket-name.oss-cn-hangzhou.aliyuncs.com”，是错误的endpoint，请去掉其中的“bucket-name”。
//    private final static String endpoint = "oss-cn-zhangjiakou.aliyuncs.com";
    private final static String endpoint = "oss-cn-hangzhou.aliyuncs.com";

    // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
    // 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
    // 注意：accessKeyId和accessKeySecret前后都没有空格，从控制台复制时请检查并去除多余的空格。
    private final static String accessKeyId = "LTAINw3ASv6QAa9B";
    private final static String accessKeySecret = "fbr42rdfwCj8Xb8T7EJyaVdUl1V4dT";


    // Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
    private final static String bucketName = "kuandaibang";

    //OSSClient对象
    public static OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

    //上传目录
//    public final static String UPLOAD = "upload";

    //图片目录
    public final static String IMAGES = "image";

    //视频目录
    public final static String VIDEOS = "video";

    //文件目录
    public final static String FILES = "files";

    //缩略图目录
    public final static String THUMBNAILS = "thumbnails";


    /**
     * 创建目录
     *
     * @param folderName
     * @throws Exception
     */
    public static void createFolder(String folderName) throws Exception {
        if (!folderName.endsWith("/")) folderName = folderName + "/";
        if (!ossClient.doesObjectExist(bucketName, folderName)) {
            ObjectMetadata objectMeta = new ObjectMetadata();
            byte[] buffer = new byte[0];
            ByteArrayInputStream in = new ByteArrayInputStream(buffer);
            objectMeta.setContentLength(0);
            ossClient.putObject(bucketName, folderName, in, objectMeta);
        }
    }

    /**
     * 删除目录
     *
     * @param folderName
     */
    public static void deleteFolder(String folderName) {
        if (!folderName.endsWith("/")) folderName = folderName + "/";
        if (!ossClient.doesObjectExist(bucketName, folderName)) {
            ossClient.deleteObject(bucketName, folderName);
        }
    }

    /**
     * 上传文件
     *
     * @param folderPath
     * @param fileName
     * @param in
     * @throws Exception 同名文件被替换
     */
    public static void putFile(String folderPath, String fileName, InputStream in) throws Exception {
        //没有目录则创建目录
        createFolder(folderPath);
        if (!folderPath.endsWith("/")) folderPath = folderPath + "/";
        String fullPath = folderPath + fileName;
        ossClient.putObject(bucketName, fullPath, in);
    }

    public static void putFile(String folderPath, String fileName, String contentType, InputStream in) throws Exception {
        //没有目录则创建目录
        createFolder(folderPath);
        if (!folderPath.endsWith("/")) folderPath = folderPath + "/";
        String fullPath = folderPath + fileName;
        ObjectMetadata om = new ObjectMetadata();
        om.setContentType(contentType);
        ossClient.putObject(bucketName, fullPath, in, om);
    }


    /**
     * 上传文件
     *
     * @param folderPath
     * @param fileName
     * @param in
     * @param contentType
     * @param contentLength
     * @throws Exception 同名文件被替换
     */
    public static void putFile(String folderPath, String fileName, InputStream in, String contentType, long contentLength) throws Exception {
        //没有目录则创建目录
        createFolder(folderPath);
        if (!folderPath.endsWith("/")) folderPath = folderPath + "/";
        String fullPath = folderPath + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(contentLength);
        metadata.setContentEncoding("utf-8");
        ossClient.putObject(bucketName, fullPath, in, metadata);
    }

    /**
     * 上传日期目录图片
     */
    public static String putImageDatePathFile(String fileName, InputStream in) throws Exception {
        String dateStr = DateUtil.convertDate2String("yyyyMMdd", new Date());
//        String fullPath = UPLOAD+"/"+IMAGES+"/"+dateStr;
        String fullPath = IMAGES + "/" + dateStr;
        putFile(fullPath, fileName, in);
        return fullPath + "/" + fileName;
    }

    public static String putImageDatePathFile(String fileName, String contentType, InputStream in) throws Exception {
        String dateStr = DateUtil.convertDate2String("yyyyMMdd", new Date());
//        String fullPath = UPLOAD+"/"+IMAGES+"/"+dateStr;
        String fullPath = IMAGES + "/" + dateStr;
        putFile(fullPath, fileName, contentType, in);
        return fullPath + "/" + fileName;
    }

    public static String putImageDatePathFileWithId(String fileName, String contentType, String id, InputStream in) throws Exception {
        String dateStr = DateUtil.convertDate2String("yyyyMMdd", new Date());
//        String fullPath = UPLOAD+"/"+IMAGES+"/"+dateStr;
        String fullPath;
        if (id != null && !"".equals(id)) {
            fullPath = IMAGES + "/" + id + "/" + dateStr;
        } else {
            fullPath = IMAGES + "/" + dateStr;
        }
        putFile(fullPath, fileName, contentType, in);
        return fullPath + "/" + fileName;
    }

    /**
     * 上传日期目录文件
     */
    public static String putDatePathFile(String fileName, InputStream in) throws Exception {
        String dateStr = DateUtil.convertDate2String("yyyyMMdd", new Date());
//        String fullPath = UPLOAD+"/";
        String fullPath = "";
        if (fileName.endsWith(".jpg")
                || fileName.endsWith(".png")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".gif")
                || fileName.endsWith(".bmp")
        ) {
            fullPath += IMAGES + "/" + dateStr;
        } else if (fileName.endsWith(".mp4")
                || fileName.endsWith(".webm")
                || fileName.endsWith(".mpeg")
                || fileName.endsWith(".mov")
                || fileName.endsWith(".avi")
                || fileName.endsWith(".mkv")
                || fileName.endsWith(".flv")
                || fileName.endsWith(".f4v")
                || fileName.endsWith(".m4v")
                || fileName.endsWith(".rm")
                || fileName.endsWith(".rmvb")
                || fileName.endsWith(".3gp")
        ) {
            fullPath += VIDEOS + "/" + dateStr;
        } else {
            fullPath += FILES + "/" + dateStr;
        }
        putFile(fullPath, fileName, in);
        return fullPath + "/" + fileName;
    }

    public static String putDatePathFile(String fileName, String originalFileName, String contentType, String id, InputStream in) throws Exception {
        String dateStr = DateUtil.convertDate2String("yyyyMMdd", new Date());
//        String fullPath = UPLOAD+"/";
        String fullPath = "";
        if (originalFileName.endsWith(".jpg")
                || originalFileName.endsWith(".png")
                || originalFileName.endsWith(".jpeg")
                || originalFileName.endsWith(".gif")
                || originalFileName.endsWith(".bmp")
                ) {
            fullPath += IMAGES + "/" + dateStr;
        } else if (originalFileName.endsWith(".mp4")
                || originalFileName.endsWith(".webm")
                || originalFileName.endsWith(".mpeg")
                || originalFileName.endsWith(".mov")
                || originalFileName.endsWith(".avi")
                || originalFileName.endsWith(".mkv")
                || originalFileName.endsWith(".flv")
                || originalFileName.endsWith(".f4v")
                || originalFileName.endsWith(".m4v")
                || originalFileName.endsWith(".rm")
                || originalFileName.endsWith(".rmvb")
                || originalFileName.endsWith(".3gp")
                ) {
            if (id != null && !"".equals(id)) {
                fullPath += VIDEOS + "/" + id + "/" + dateStr;
            }else{
                fullPath += VIDEOS + "/" + dateStr;
            }
        } else {
            fullPath += FILES + "/" + dateStr;
        }
        putFile(fullPath, fileName, contentType, in);
        return fullPath + "/" + fileName;
    }

    /**
     * 上传日期目录图片
     */
    public static String putImageDatePathThmbnailFile(String fileName, InputStream in) throws Exception {
        String dateStr = DateUtil.convertDate2String("yyyyMMdd", new Date());
//        String fullPath = UPLOAD+"/"+IMAGES+"/"+dateStr+"/"+THUMBNAILS;
        String fullPath = IMAGES + "/" + dateStr + "/" + THUMBNAILS;
        putFile(fullPath, fileName, in);
        return fullPath + "/" + fileName;
    }

    public static OSSObject getOSSObject(String url) {
        return ossClient.getObject(bucketName, url);
    }

    /**
     * 删除文件
     *
     * @param
     * @throws Exception
     */

    public static void deleteFile(String folderPath, String fileName) throws Exception {
        if (!folderPath.endsWith("/")) folderPath = folderPath + "/";
        String filePath = folderPath + fileName;
        ossClient.deleteObject(bucketName, filePath);
    }

//    public static void main(String[] args) throws Exception {
//        createFolder("test/ddd");
//    }


    public static OSSObject getFile(String key) {

        return ossClient.getObject(bucketName, key);
    }

    public static boolean doesFileExist(String key) {
        return ossClient.doesObjectExist(bucketName, key);
    }

}
