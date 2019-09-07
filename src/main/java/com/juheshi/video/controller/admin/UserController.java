package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.AppUserPage;
import com.juheshi.video.entity.VipTypeSetting;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.VipTypeSettingService;
import com.juheshi.video.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    private AppUserService appUserService;

    @Resource
    private VipTypeSettingService vipTypeSettingService;

    //获取用户列表
    @RequestMapping(value = "/userList", produces = "text/html;charset=UTF-8")
    public ModelAndView userList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/user/user-list");
        return modelAndView;
    }

    //查询用户列表接口
    @RequestMapping(value = "/getUserData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getUserData(@RequestParam(required = false, name = "vipFlag") String vipFlag,
                              @RequestParam(required = false, name = "search") String search,
                              @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                              @RequestParam(required = false, name = "limit", defaultValue = "10") int limit) {
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Page<AppUser> p = appUserService.pageFindAllUser(search, vipFlag, page, limit);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }


    //查询绑定为管理员的前台用户接口
    @RequestMapping(value = "/getAdminUserData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getAdminUserData(@RequestParam(required = false, name = "search") String search) {
        List<AppUser> list = appUserService.findAllUser(search, Constants.YES);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("state", 1);
        result.put("data", list);
        result.put("msg", "");
        return result;
    }

    //查询用户主页接口
    @RequestMapping(value = "/userDetail", produces = "text/html;charset=UTF-8")
    public ModelAndView userDetail(Integer id) {
        AppUserPage userPage = appUserService.findUserPageByUserId(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userPage", userPage);
        modelAndView.setViewName("admin/user/user-detail");
        return modelAndView;
    }

    //更改用户可发布内容标志接口
    @RequestMapping(value = "/changePublishFlag", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object changePublishFlag(Integer id, String publishFlag) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (publishFlag == null || "".equals(publishFlag) || "N".equals(publishFlag.toUpperCase())) {
            publishFlag = Constants.NO;
        } else if ("Y".equals(publishFlag.toUpperCase())) {
            publishFlag = Constants.YES;
        } else {
            result.put("state", 0);
            result.put("msg", "参数错误");
            return result;
        }
        appUserService.modifyUserPublishFlag(id, publishFlag);
        result.put("state", 1);
        result.put("msg", "");
        return result;
    }

    //更改用户管理员标志接口
    @RequestMapping(value = "/changeAdminFlag", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object changeAdminFlag(Integer id, String adminFlag) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (adminFlag == null || "".equals(adminFlag) || "N".equals(adminFlag.toUpperCase())) {
            adminFlag = Constants.NO;
        } else if ("Y".equals(adminFlag.toUpperCase())) {
            adminFlag = Constants.YES;
        } else {
            result.put("state", 0);
            result.put("msg", "参数错误");
            return result;
        }
        appUserService.modifyUserAdminFlag(id, adminFlag);
        result.put("state", 1);
        result.put("msg", "");
        return result;
    }

    @RequestMapping(value = "/changeState", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object changeState(Integer id, Integer state) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (state != AppUser.STATE_NORMAL && state != AppUser.STATE_BLOCK) {
            result.put("state", 0);
            result.put("msg", "参数错误");
            return result;
        }
        appUserService.modifyUserState(id, state);
        result.put("state", 1);
        result.put("msg", "");
        return result;
    }


    //查询用户vip信息
    @RequestMapping(value = "/userVip", produces = "text/html;charset=UTF-8")
    public ModelAndView userVip(Integer id) {
        AppUser user = appUserService.findUserById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        List<VipTypeSetting> vipList = vipTypeSettingService.findAllVip(Constants.YES);
        modelAndView.addObject("vipList", vipList);
        modelAndView.setViewName("admin/user/user-vip");
        return modelAndView;
    }

    //设置vip
    @RequestMapping(value = "/settingVip", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object settingVip(Integer userId, Integer vipTypeId, Integer type ,String remark, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (type !=Constants.USER_ADD_VIP_TYPE_ADMIN_GIVE &&
                type !=Constants.USER_ADD_VIP_TYPE_ADMIN_PURCHASE) {
            result.put("state", 0);
            result.put("msg", "参数错误");
            return result;
        }

        Object obj = session.getAttribute(Constants.SESSION_ADMIN_USER_ID);
        if (obj == null) {
            result.put("state", 0);
            result.put("msg", "您未登陆，请登陆");
            return result;
        }
        appUserService.addUserVip(type, userId, vipTypeId, remark, (Integer)obj);
        result.put("state", 1);
        result.put("msg", "");
        return result;
    }



}
