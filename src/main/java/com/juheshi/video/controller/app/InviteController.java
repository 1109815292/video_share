package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.SysParam;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.SysParamService;
import com.juheshi.video.util.ImgUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/app")
@SessionAttributes(Constants.SESSION_APP_USER_ID)
public class InviteController {

    @Resource
    private AppUserService appUserService;

    @Resource
    private SysParamService sysParamService;

    @RequestMapping(value = "/inv/{id}/register")
    public String invite(@PathVariable("id") Integer id) {
        return "redirect:/app/video/square";
    }

    @RequestMapping(value = "/invitation")
    public ModelAndView invitation(
            @RequestParam(required = false, name = "tabIndex", defaultValue = "1") Integer tabIndex,
            @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        AppUser appUser = appUserService.findUserById(userId);
        if (tabIndex.equals(0)) { //链接展示页
            SysParam param1 = sysParamService.findByKey(Constants.SYS_PARAM_KEY_INV_TEXT_1);
            SysParam param2 = sysParamService.findByKey(Constants.SYS_PARAM_KEY_INV_TEXT_2);
            modelAndView.addObject("inv_text_1", param1.getParamValue());
            modelAndView.addObject("inv_text_2", param2.getParamValue());
            StringBuilder sb = new StringBuilder();
            sb.append(request.getScheme()).append(":").append("//").append(request.getServerName());
            if (request.getServerPort() != 80) {
                sb.append(":").append(request.getServerPort());
            }
            if (!request.getContextPath().equals("")) {
                sb.append(request.getContextPath());
            }
            sb.append("/app/inv/").append(appUser.getCopyNo()).append("/register");
            modelAndView.addObject("inv_url", sb.toString());
            modelAndView.setViewName("app/invitation/link");
        } else if (tabIndex.equals(1)) { //二维码展示页
            if (appUser.getInviteQRCode() == null || appUser.getInviteQRCode().equals("") || appUser.getInviteQRCodeExpiresIn() == null) {
                modelAndView.addObject("needShowReset", "Y");
            } else {
                Date now = Calendar.getInstance().getTime();
//                if (now.getTime() - appUser.getInviteQRCodeExpiresIn().getTime() >= -10*24*60*60*1000) { //离过期时间还有10天
//                    modelAndView.addObject("inviteQRCodeExpiresIn", appUser.getInviteQRCodeExpiresIn());
//                    modelAndView.addObject("needShowReset", "Y");
//                } else {
//                    modelAndView.addObject("needShowReset", "N");
//                }
                modelAndView.addObject("inviteQRCodeExpiresIn", appUser.getInviteQRCodeExpiresIn());
                modelAndView.addObject("needShowReset", "Y");
            }

            modelAndView.addObject("inviteQRCode", ImgUtil.NetImageToBase64(appUser.getInviteQRCode()));
            modelAndView.setViewName("app/invitation/qrcode");
        } else {
            modelAndView.setViewName("app/invitation/invitation");
        }
        return modelAndView;
    }

    @RequestMapping("/inv/resetQRCode")
    @ResponseBody
    public Object resetQRCode(@ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        AppUser appUser = appUserService.findUserById(userId);
        String inviteQRCode = appUserService.generateInviteQRCode(appUser);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("state", 1);
        result.put("data", inviteQRCode);
        return result;
    }
}
