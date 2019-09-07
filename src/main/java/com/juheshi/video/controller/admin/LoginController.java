package com.juheshi.video.controller.admin;


import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.SystemUser;
import com.juheshi.video.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Resource
    private SystemService systemService;


    @RequestMapping(value = "/admin/login")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/login");
        return modelAndView;
    }

    @RequestMapping(value = "/session")
    public ModelAndView session(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/session");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/welcome")
    public ModelAndView welcome(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin/welcome");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/authorityError")
    public ModelAndView authorityError(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin/authorityError");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/userLogin")
    public String userLogin(HttpSession session, String userName, String password) throws Exception {
        SystemUser user = systemService.login(userName, password);
        if (user != null){
            session.setAttribute(Constants.SESSION_ADMIN_USER_NAME, user.getUserName());
            session.setAttribute(Constants.SESSION_ADMIN_USER_ID, user.getId());
            session.setAttribute(Constants.SESSION_ADMIN_USER_TYPE, user.getType());
            session.setAttribute(Constants.SESSION_ADMIN_USER_APP_USER_COPY_NO, user.getCopyNo());
            return "redirect:/admin/index";
        }else{
            return "redirect:/admin/login";
        }
    }

    @RequestMapping(value="/admin/userLogout")
    public String userLogout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:/admin/login";
    }

    @RequestMapping(value = {"/admin/index", "/admin"})
    public ModelAndView index(){


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin/index");
        return modelAndView;
    }





}
