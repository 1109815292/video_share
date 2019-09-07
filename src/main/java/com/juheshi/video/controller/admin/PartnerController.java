package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.RechargeDivide;
import com.juheshi.video.entity.SysParam;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
import java.util.Map;

@Controller
@RequestMapping("/admin/partner")
public class PartnerController {
    @Resource
    private AppUserService appUserService;

    @Resource
    private SystemService systemService;

    @Resource
    private DivideService divideService;

    @Resource
    private SysParamService sysParamService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    //合伙人列表
    @RequestMapping(value = "/partnerList", produces = "text/html;charset=UTF-8")
    public ModelAndView partnerList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/partner/partner-list");
        return modelAndView;
    }


    //查询合伙人用户数据
    @RequestMapping(value = "/getPartnerData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getPartnerData(@RequestParam(required = false, name = "search") String search,
                              @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                              @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                              HttpSession session) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (!userType.equals(SystemUser.TYPE_ADMIN)) {
            resultMap.put("code", 0);
            resultMap.put("msg", "您没有权限");
            resultMap.put("count", 0);
            resultMap.put("data", null);
            return resultMap;
        }
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Page<SystemUser> p = systemService.pageFindSysUser(search, null, new Integer[]{SystemUser.TYPE_PARTNER}, page, limit);
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }


    //跳转合伙人编辑页面
    @RequestMapping(value = "/partnerUserEdit", produces = "text/html;charset=UTF-8")
    public ModelAndView partnerUserEdit(Integer id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        Integer userType = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (!userType.equals(SystemUser.TYPE_ADMIN)) {
            return null;
        }
        if (id != null) {
            SystemUser user = systemService.findUserById(id);
            modelAndView.addObject("user", user);
        }
        modelAndView.setViewName("admin/partner/partner-user-edit");
        return modelAndView;
    }

    //保存合伙人信息
    @RequestMapping(value = "/savePartnerUserEdit", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object savePartnerUserEdit(SystemUser partnerUser, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (!userType.equals(SystemUser.TYPE_ADMIN)) {
            resultMap.put("state", 0);
            resultMap.put("msg", "您没有权限");
            return resultMap;
        }
        if (partnerUser.getId() == null) { //新增 - 设置角色
            Integer userId = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_ID);
            partnerUser.setParentId(userId);
            try {
                partnerUser.setPassword(new EncryptUtil().md5Digest(partnerUser.getPassword()));//密码md5
            } catch (Exception e) {
                logger.error("新增渠道失败！", e);
                resultMap.put("state", 0);
                resultMap.put("msg", "操作失败");
                resultMap.put("detail", e.getMessage());
                return resultMap;
            }
            partnerUser.setState(1);
            partnerUser.setType(SystemUser.TYPE_PARTNER);//标识为合伙人用户
            partnerUser.setRoleId(2);//管理员默认设定新增用户角色为2=合伙人
        }
        systemService.saveUser(partnerUser);
        resultMap.put("state", 1);
        resultMap.put("msg", "ok");
        return resultMap;
    }


    //绑定App用户
    @RequestMapping(value = "/bindPartnerUser", produces = "text/html;charset=UTF-8")
    public ModelAndView bindUser(Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/partner/partner-bind-user");
        return modelAndView;
    }


    //查询appUser
    @RequestMapping(value = "/getPartnerAppUserData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getPartnerAppUserData(@RequestParam(required = false, name = "search") String search,
                                     @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                     @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                                     HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if(!userType.equals(SystemUser.TYPE_ADMIN)) {
            resultMap.put("code", 1);
            resultMap.put("msg", "您没有权限");
            resultMap.put("count", 0);
            resultMap.put("data", null);
            return resultMap;
        }
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Page<AppUser> p = appUserService.pageFindAllUser(search, null, page, limit);
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());

        return resultMap;
    }


    //绑定操作
    @RequestMapping(value = "/doPartnerBind", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object doPartnerBind(@RequestParam(name = "sysUserId") Integer sysUserId,
                         @RequestParam(name = "copyNo") String copyNo,
                         HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if(!userType.equals(SystemUser.TYPE_ADMIN)) {
            resultMap.put("state", 0);
            resultMap.put("msg", "您没有权限");
            return resultMap;
        }
        if (sysUserId == null || copyNo == null || "".equals(copyNo)) {
            resultMap.put("state", 0);
            resultMap.put("msg", "参数异常");
            return resultMap;
        }
        AppUser appUser = appUserService.findUserByCopyNo(copyNo);
        if (appUser == null) {
            resultMap.put("state", 0);
            resultMap.put("msg", "绑定用户不存在");
            return resultMap;
        }
        SysParam param = sysParamService.findByKey(Constants.SYS_PARAM_KEY_DEFAULT_INVITER);
        if (param != null && !"".equals(param.getParamValue())) {
            if (appUser.getInviteId() != null && appUser.getInviteCopyNo() != null
                    && !appUser.getInviteCopyNo().equals(param.getParamValue())) {
                resultMap.put("state", 0);
                resultMap.put("msg", "该用户不是默认用户下级，无法变更");
                return resultMap;
            }
        }
        systemService.modifySystemUserForBindCopyNo(sysUserId, copyNo, AppUser.USER_LEVEL_PARTNER);
        resultMap.put("state", 1);
        resultMap.put("msg", "ok");
        return resultMap;
    }

    //合伙人收益页面
    @RequestMapping(value = "/partnerDivide", produces = "text/html;charset=UTF-8")
    public ModelAndView partnerDivide(Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/partner/partner-divide-list");
        return modelAndView;
    }


    //合伙人收益记录
    @RequestMapping(value = "/getPartnerDivideData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getPartnerDivideData(@RequestParam(required = false, name = "fansId") Integer fansId,
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
