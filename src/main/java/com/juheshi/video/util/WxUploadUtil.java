package com.juheshi.video.util;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.VarParam;
import com.juheshi.video.service.VarParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;
import java.util.UUID;

@Component
public class WxUploadUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String WX_FILE_API_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token={{ACCESS_TOKEN}}&media_id={{MEDIA_ID}}";


    @Resource
    private VarParamService varParamService;

    public String uploadImageToOSS(String mediaId, String ossPath) {
        final VarParam v = varParamService.findByVarName(Constants.VAR_PARAM_WX_ACCESS_TOKEN);
        if (v == null || v.getVarValue() == null || "".equals(v.getVarValue()))
            return null;

        String fileUrl = WX_FILE_API_URL.replace("{{ACCESS_TOKEN}}", v.getVarValue()).replace("{{MEDIA_ID}}", mediaId);
        try {
            URL url = new URL(fileUrl);
            String contentType = URLUtil.getContentType(url);
            if (contentType == null || "".equals(contentType) || contentType.equals("application/octet-stream")) {
                contentType = "image/jpeg";
            }
            String[] tmp = contentType.split("/");
            String suffix = tmp[tmp.length - 1];
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
            String path = OSSClientUtil.putImageDatePathFileWithId(fileName, contentType, ossPath, url.openStream());
            if (!"".equals(path))
                return OSSClientUtil.URL_PREFIX + path;
            else
                return mediaId;
        } catch (Exception e) {
            logger.error("异步下载微信图片资源并上传至OSS异常, media_id=[{}]", mediaId, e);
            return mediaId;
        }
    }
}
