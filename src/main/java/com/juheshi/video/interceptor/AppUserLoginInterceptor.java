package com.juheshi.video.interceptor;

import com.juheshi.video.common.Constants;
import com.juheshi.video.service.AppUserService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

public class AppUserLoginInterceptor implements HandlerInterceptor {

    @Resource
    private AppUserService appUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 获取请求的URL
        //http://xxx.xx.com/xxx/list?userId=1
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();


        if (requestURI.contains("/app/") &&
                (!requestURI.contains("/app/wxLogin/")) &&
                (!requestURI.contains("/app/advPay/wxPaySuccess")) &&
                (!requestURI.contains("/app/advPay/refundSuccess"))) {

            // 获取Session
            HttpSession session = request.getSession();

            if (queryString != null){
                if (queryString.contains("stationCopyNo")){
                    String stationCopyNo = queryString.substring(queryString.indexOf("stationCopyNo") + 14, queryString.indexOf("stationCopyNo") + 22);
                    session.setAttribute(Constants.SESSION_APP_STATION_COPY_NO, stationCopyNo);
                }
            }
            if (session.getAttribute(Constants.SESSION_APP_STATION_COPY_NO) == null){
                session.setAttribute(Constants.SESSION_APP_STATION_COPY_NO, "");
            }

            String openId = (String) session.getAttribute(Constants.SESSION_APP_USER_OPEN_ID);

            //临时赋值，以跳过微信登录
//            openId = "o5YVjv_dtUA5VvEV3To2NdvwkM5A";
//            session.setAttribute(Constants.SESSION_APP_USER_ID, 40);
//            session.setAttribute(Constants.SESSION_APP_USER_OPEN_ID, "o5YVjv_Nb0be9HPLo0PHxhEngjMk");
//            session.setAttribute(Constants.SESSION_APP_USER_COPY_NO, "43106955");


            if (openId == null || "".equals(openId)) {
                String redirectUrl = URLEncoder.encode(requestURI + ((queryString == null || "".equals(queryString.trim())) ? "" : ("?" + queryString)), "UTF-8");
                response.sendRedirect("/app/wxLogin/oauth-base?url=" + redirectUrl);
                return false;
            }
            //更新最后访问时间
            Integer userId = (Integer) session.getAttribute(Constants.SESSION_APP_USER_ID);
            appUserService.modifyUserLastViewTime(userId);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
