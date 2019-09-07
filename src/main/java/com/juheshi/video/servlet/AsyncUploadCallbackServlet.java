package com.juheshi.video.servlet;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.Video;
import com.juheshi.video.service.VideoService;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class AsyncUploadCallbackServlet extends HttpServlet {

    @Autowired
    private VideoService videoService;

    public void init(ServletConfig config) throws ServletException {
        //在servlet中注入service
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream is = null;
        is = req.getInputStream();
        String body = IOUtils.toString(is, "utf-8");

        JSONObject jsonObject = new JSONObject(body);
        JSONArray jsonArray = jsonObject.getJSONArray("resources");

        JSONObject subObject;
        int callbackId;
        String originalUrl;
        String newUrl;
        Video v;
        for (int i = 0; i < jsonArray.length(); i++) {
            subObject = jsonArray.getJSONObject(i);
            callbackId = subObject.getInt("callback_id");
            originalUrl = subObject.getString("original_url");
            newUrl = subObject.getString("new_url");
            v = videoService.findById(callbackId);
            if (v != null && v.getCachedFlag().equals(Constants.NO)) {
                videoService.updateVideoUrlById(callbackId, newUrl);
            }
        }

    }
}
