package com.juheshi.video.interceptor;

import com.juheshi.video.common.Constants;
import com.juheshi.video.service.SystemService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthorityInterceptor implements HandlerInterceptor {

    @Resource
    private SystemService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String requestURI = httpServletRequest.getRequestURI();
        if (requestURI.contains("login") || requestURI.contains("session") || requestURI.contains("/static") || requestURI.contains("/assets")
                || requestURI.contains("/error") || requestURI.contains("userLogin") || requestURI.contains("index")
                || requestURI.contains("/app/") ) {
            return true;
        }
        String[] urlStr = requestURI.split("/");
        String actionName = urlStr[urlStr.length - 1];

        HttpSession session = httpServletRequest.getSession();
        Integer userId = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_ID);

        if (userService.checkAuthority(userId, actionName)){
            return true;
        }

        // 无权限
        httpServletResponse.sendRedirect( httpServletRequest.getContextPath() + "/admin/authorityError");

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
