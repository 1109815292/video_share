package com.juheshi.video.interceptor;

import com.juheshi.video.common.Constants;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 获取请求的URL
        String requestURI = request.getRequestURI();
        // URL:login.jsp是公开的;这个demo是除了login.jsp是可以公开访问的，其它的URL都进行拦截控制
        // 注意：一些静态文件不能拦截，否则会死循环，知道内存耗尽
        if (requestURI.indexOf("login") >= 0 || requestURI.contains("register") || requestURI.contains("/static") || requestURI.contains("/assets")
                || requestURI.contains("/error") || requestURI.contains("userLogin") || requestURI.contains("session")
                || requestURI.contains("/app/") || requestURI.contains("/updatePassword") || requestURI.contains("getSystemVer")) {
            return true;
        }
        // 获取Session
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute(Constants.SESSION_ADMIN_USER_NAME);

        if (userName != null) {
            return true;
        }
        // 不符合条件的，跳转到登录界面
        response.sendRedirect( request.getContextPath() + "/session");

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
