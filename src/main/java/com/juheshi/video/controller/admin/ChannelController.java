package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.RechargeDivide;
import com.juheshi.video.entity.SystemUser;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.DivideService;
import com.juheshi.video.service.SysParamService;
import com.juheshi.video.service.SystemService;
import com.juheshi.video.util.EncryptUtil;
import com.juheshi.video.util.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 渠道管理
 */
@Controller
@RequestMapping("/admin/chan")
public class ChannelController {


    @Resource
    private AppUserService appUserService;

    @Resource
    private SystemService systemService;

    @Resource
    private DivideService divideService;

    @Resource
    private SysParamService sysParamService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    //获取视频列表
    @RequestMapping(value = "/chanList", produces = "text/html;charset=UTF-8")
    public ModelAndView chanList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/channel/channel-list");
        return modelAndView;
    }

    //查询渠道用户
    @RequestMapping(value = "/getChanData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getChanData(@RequestParam(required = false, name = "search") String search,
                              @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                              @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                              HttpSession session) {
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        Integer userId = null;
        switch (userType) {
            case SystemUser.TYPE_ADMIN:
                break;
            case SystemUser.TYPE_PARTNER:
                userId = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_ID);
                break;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Page<SystemUser> p = systemService.pageFindSysUser(search, userId, new Integer[]{SystemUser.TYPE_CHANNEL}, page, limit);
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }


    @RequestMapping(value = "/chanUserList", produces = "text/html;charset=UTF-8")
    public ModelAndView chanUserList(String copyNo) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/channel/channel-user-list");
        return modelAndView;
    }

    @RequestMapping(value = "/chanUserEdit", produces = "text/html;charset=UTF-8")
    public ModelAndView chanUserEdit(Integer id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        Integer userType = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (id != null) {
            Integer userId = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_ID);

            SystemUser user = systemService.findUserById(id);
            switch (userType) {
                case SystemUser.TYPE_ADMIN:
                    modelAndView.addObject("user", user);
                    break;
                case SystemUser.TYPE_PARTNER:
                    if (user.getParentId().equals(userId)) {
                        modelAndView.addObject("user", user);
                    }
                    break;
            }

        }
        if (userType.equals(SystemUser.TYPE_ADMIN)) {
            //查询所有合伙人
            List<SystemUser> list = systemService.findAllSysUser(null, null, new Integer[]{SystemUser.TYPE_PARTNER});
            modelAndView.addObject("partners", list);
        }
        modelAndView.setViewName("admin/channel/channel-user-edit");
        return modelAndView;
    }

    //保存渠道信息
    @RequestMapping(value = "/saveChanUserEdit", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object saveChanUserEdit(SystemUser chanUser, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (chanUser.getId() == null) { //新增 - 设置角色
            if(userType.equals(SystemUser.TYPE_ADMIN)) {
                if (chanUser.getParentId() == null) {
                    Integer userId = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_ID);
                    chanUser.setParentId(userId);
                }
            } else { //非管理员用户
                Integer userId = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_ID);
                chanUser.setParentId(userId);
            }
            try {
                chanUser.setPassword(new EncryptUtil().md5Digest(chanUser.getPassword()));//密码md5
            } catch (Exception e) {
                logger.error("新增渠道失败！", e);
                resultMap.put("state", 0);
                resultMap.put("msg", "操作失败");
                resultMap.put("detail", e.getMessage());
                return resultMap;
            }
            chanUser.setState(1);
            chanUser.setType(SystemUser.TYPE_CHANNEL);//标识为渠道用户
            chanUser.setRoleId(3);//合伙人默认设定新增用户角色为3=渠道

        }
        systemService.saveUser(chanUser);
        resultMap.put("state", 1);
        resultMap.put("msg", "ok");
        return resultMap;
    }

    //合伙人查询渠道下用户
    @RequestMapping(value = "/getChanUserData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getChanUserData(@RequestParam(required = false, name = "search") String search,
                                  @RequestParam(required = false, name = "copyNo") String copyNo,
                                  @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                  @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                                  HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        String appUserCopyNo = null;
        switch (userType) {
            case SystemUser.TYPE_ADMIN:
                appUserCopyNo = copyNo;
                break;
            case SystemUser.TYPE_PARTNER:
                appUserCopyNo = (String) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_APP_USER_COPY_NO);
                AppUser chanAppUser = appUserService.findUserByCopyNo(copyNo);
                if (chanAppUser != null && chanAppUser.getInviteCopyNo().equals(appUserCopyNo)) {
                    appUserCopyNo = chanAppUser.getCopyNo();
                } else {
                    resultMap.put("code", 1);
                    resultMap.put("msg", "没有权限");
                    resultMap.put("count", 0);
                    resultMap.put("data", null);
                    return resultMap;
                }
                break;
        }

        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Page<AppUser> p = appUserService.pageFindAllUserWithCopyNo(search, appUserCopyNo, page, limit);
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }


    //绑定用户
    @RequestMapping(value = "/bindUser", produces = "text/html;charset=UTF-8")
    public ModelAndView bindUser(Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/channel/channel-bind-user");
        return modelAndView;
    }

    //查询appUser
    @RequestMapping(value = "/getChanAppUserData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getChanAppUserData(@RequestParam(required = false, name = "search") String search,
                                     @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                     @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                                     HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        Page<AppUser> p = null;
        if (userType.equals(SystemUser.TYPE_ADMIN)) { //如果是管理员
            p = appUserService.pageFindAllUser(search, null, page, limit);
        } else if (userType.equals(SystemUser.TYPE_PARTNER)) {//如果是合伙人或站长
            String sysUserCopyNo = (String) session.getAttribute(Constants.SESSION_ADMIN_USER_APP_USER_COPY_NO);
            p = appUserService.pageFindAllUser(search, sysUserCopyNo, null, page, limit);
        }

        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());

        return resultMap;
    }

    //绑定操作
    @RequestMapping(value = "/doBind", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object doBind(@RequestParam(name = "sysUserId") Integer sysUserId,
                         @RequestParam(name = "copyNo") String copyNo,
                         HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (sysUserId == null || copyNo == null || "".equals(copyNo)) {
            resultMap.put("state", 0);
            resultMap.put("msg", "参数异常");
            return resultMap;
        }
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        AppUser appUser = appUserService.findUserByCopyNo(copyNo);
        if (appUser == null) {
            resultMap.put("state", 0);
            resultMap.put("msg", "绑定用户不存在");
            return resultMap;
        }
        if (userType.equals(SystemUser.TYPE_ADMIN)) { //如果是管理员
//            SysParam param = sysParamService.findByKey(Constants.SYS_PARAM_KEY_DEFAULT_INVITER);
//            if (param != null && !"".equals(param.getParamValue())) {
//                if (!appUser.getInviteCopyNo().equals(param.getParamValue())) {
//                    resultMap.put("state", 0);
//                    resultMap.put("msg", "该用户不是默认用户下级，无法变更");
//                    return resultMap;
//                }
//            }
            //管理员忽略直属下级判断，直接绑定
            systemService.modifySystemUserForBindCopyNo(sysUserId, copyNo);
            resultMap.put("state", 1);
            resultMap.put("msg", "ok");
            return resultMap;
        } else if (userType.equals(SystemUser.TYPE_PARTNER)) {//如果是合伙人
            String sysUserCopyNo = (String) session.getAttribute(Constants.SESSION_ADMIN_USER_APP_USER_COPY_NO);
            if (appUser.getInviteCopyNo().equals(sysUserCopyNo)) {//判断是否为直属下级
                systemService.modifySystemUserForBindCopyNo(sysUserId, copyNo);
                resultMap.put("state", 1);
                resultMap.put("msg", "ok");
                return resultMap;
            } else {
                resultMap.put("state", 0);
                resultMap.put("msg", "该用户不是您的直属下级，无法绑定");
                return resultMap;
            }
        }
        return resultMap;
    }

    @RequestMapping(value = "/chanDivide", produces = "text/html;charset=UTF-8")
    public ModelAndView chanDivide(Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/channel/channel-divide-list");
        return modelAndView;
    }


    @RequestMapping(value = "/getChanDivideData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getChanDivideData(@RequestParam(required = false, name = "fansId") Integer fansId,
                                    @RequestParam(required = false, name = "payFlag") Integer payFlag,
                                    @RequestParam(required = false, name = "dateFrom") String dateFromStr,
                                    @RequestParam(required = false, name = "dateTo") String dateToStr,
                                    @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                    @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                                    HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        String bindCopyNo = (String) session.getAttribute(Constants.SESSION_ADMIN_USER_APP_USER_COPY_NO);
        if(bindCopyNo == null || "".equals(bindCopyNo)){
            resultMap.put("code", 1);
            resultMap.put("msg", "未绑定视推号");
            resultMap.put("count", 0);
            resultMap.put("data", null);
            return resultMap;
        }
        Integer userId;
        if (userType.equals(SystemUser.TYPE_ADMIN)) {
            userId = null;
        } else {
            AppUser appUser = appUserService.findUserByCopyNo(bindCopyNo);
            userId = appUser.getId();
        }
        Date dateFrom = null;
        Date dateTo = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (dateFromStr != null && !"".equals(dateFromStr)) {
            try {
                dateFrom = sdf.parse(dateFromStr + " 00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (dateToStr != null && !"".equals(dateToStr)) {
            try {
                dateTo = sdf.parse(dateToStr + " 23:59:59");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Page<RechargeDivide> p = divideService.pageFind(userId, fansId, payFlag, dateFrom, dateTo, page, limit);
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }


}
