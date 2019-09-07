package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.util.AliyunOssUpload;
import com.juheshi.video.util.OSSClientUtil;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/app")
@SessionAttributes(Constants.SESSION_APP_USER_ID)
public class FileUploadController {

    public final static String URL_PREFIX = "http://oss.mogupai.com/";

    @Resource
    private AppUserService appUserService;

    @RequestMapping(value = "/uploadFile", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadFile(HttpServletRequest request,
                             @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId,
                             @RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile) throws IOException {

        AppUser appUser = appUserService.findUserById(userId);
        String fullPath = "";
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        String newFileName = "";
        Random random = new Random();

        //生成文件名 日期+6位随机数   判断是否重复

        String randomStr = "";
        for (int i = 0; i < 6; i++) {
            randomStr += random.nextInt(10);
        }
        newFileName = df.format(date) + randomStr + ".jpg";

        try {
            CommonsMultipartFile cf = (CommonsMultipartFile) uploadFile;
            DiskFileItem fi = (DiskFileItem) cf.getFileItem();
            File file = fi.getStoreLocation();
            FileInputStream fileInputStream = new FileInputStream(file);
            fullPath = OSSClientUtil.putImageDatePathFileWithId(newFileName, "image/jpeg", appUser.getCopyNo(), fileInputStream);
            //AliyunOssUpload.uploadFileToAliyun(newFileName, file);
        } catch (Exception e) {
            System.out.println("uploadfile:" + e.getStackTrace());
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("picUrl", URL_PREFIX + fullPath);

        return resultMap;
    }


    @RequestMapping(value = "/uploadVideo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadVideo(HttpServletRequest request,
                             @ModelAttribute(Constants.SESSION_APP_USER_ID) int userId,
                             @RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile) throws IOException {

        AppUser appUser = appUserService.findUserById(userId);
        String fullPath = "";
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        String newFileName = "";
        Random random = new Random();

        //生成文件名 日期+6位随机数   判断是否重复

        String randomStr = "";
        for (int i = 0; i < 6; i++) {
            randomStr += random.nextInt(10);
        }
        newFileName = df.format(date) + randomStr;

        try {
            CommonsMultipartFile cf = (CommonsMultipartFile) uploadFile;
            DiskFileItem fi = (DiskFileItem) cf.getFileItem();
            File file = fi.getStoreLocation();
            FileInputStream fileInputStream = new FileInputStream(file);
            fullPath = OSSClientUtil.putDatePathFile(newFileName, fi.getName(),"video/mp4", appUser.getCopyNo(), fileInputStream);
            //AliyunOssUpload.uploadFileToAliyun(newFileName, file);
        } catch (Exception e) {
            System.out.println("uploadfile:" + e.getStackTrace());
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("videoUrl", URL_PREFIX + fullPath);

        return resultMap;
    }
}
