package com.juheshi.video.util;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLUtil {
    public static String getContentType(URL url) {
        //利用nio提供的类判断文件ContentType
        String content_type = null;
        try {
            Path path = Paths.get(url.toURI().toString());
            content_type = Files.probeContentType(path);

            //若失败则调用另一个方法进行判断
            if (content_type == null) {
                content_type = new MimetypesFileTypeMap().getContentType(new File(url.toURI().toString()));
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return content_type;
    }

    public static String getUrlFromContent(String content) {
        Matcher matcher = Patterns.WEB_URL.matcher(content);
        String result = "";
        if (matcher.find()) {
            result = matcher.group();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getUrlFromContent("如果你每天想我一次”\n" +
                "https://www.iesdouyin.com/share/video/6702031358950149379/?region=CN&mid=6694101526874458887&u_code=46j2205l3891&titleType=title&utm_source=copy_link&utm_campaign=client_share&utm_medium=android&app=aweme&iid=79902461098&timestamp=1563954098"));
    }

}
