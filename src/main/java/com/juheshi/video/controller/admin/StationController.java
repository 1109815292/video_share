package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.PayOrder;
import com.juheshi.video.entity.SystemUser;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.PayOrderService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/station")
public class StationController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SystemService systemService;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private AppUserService appUserService;

    //站长列表
    @RequestMapping(value = "/stationList", produces = "text/html;charset=UTF-8")
    public ModelAndView stationList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/station/station-list");
        return modelAndView;
    }

    //查询站长用户数据
    @RequestMapping(value = "/getStationData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getStationData(@RequestParam(required = false, name = "search") String search,
                                 @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                 @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                                 HttpSession session) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Integer userId = null;
        switch (userType) {
            case SystemUser.TYPE_ADMIN:
                break;
            case SystemUser.TYPE_PARTNER:
                userId = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_ID);
                break;
        }
        Page<SystemUser> p = systemService.pageFindSysUser(search, userId, new Integer[]{SystemUser.TYPE_STATION}, page, limit);
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }


    //添加&编辑站长信息
    @RequestMapping(value = "/stationUserEdit", produces = "text/html;charset=UTF-8")
    public ModelAndView stationUserEdit(Integer id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        Integer userType = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (id != null) {
            SystemUser user = systemService.findUserById(id);
            modelAndView.addObject("user", user);
        }
        modelAndView.setViewName("admin/station/station-user-edit");
        return modelAndView;
    }

    //保存站长信息
    @RequestMapping(value = "/saveStationUserEdit", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object saveStationUserEdit(SystemUser stationUser, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (stationUser.getId() == null) { //新增 - 设置角色
            if(userType.equals(SystemUser.TYPE_ADMIN)) {
                Integer userId = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_ID);
                stationUser.setParentId(userId);
            } else { //非管理员用户
                resultMap.put("state", 0);
                resultMap.put("msg", "没有权限");
                return resultMap;
            }
            try {
                stationUser.setPassword(new EncryptUtil().md5Digest(stationUser.getPassword()));//密码md5
            } catch (Exception e) {
                logger.error("新增站长失败！", e);
                resultMap.put("state", 0);
                resultMap.put("msg", "操作失败");
                resultMap.put("detail", e.getMessage());
                return resultMap;
            }
            stationUser.setState(1);
            stationUser.setType(SystemUser.TYPE_STATION);//标识为渠道用户
            stationUser.setRoleId(4);//合伙人默认设定新增用户角色为4=合伙人

        }
        systemService.saveUser(stationUser);
        resultMap.put("state", 1);
        resultMap.put("msg", "ok");
        return resultMap;
    }

    //绑定App用户
    @RequestMapping(value = "/bindStationUser", produces = "text/html;charset=UTF-8")
    public ModelAndView bindStationUser(Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/station/station-bind-user");
        return modelAndView;
    }

    //绑定操作
    @RequestMapping(value = "/doStationBind", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object doStationBind(@RequestParam(name = "sysUserId") Integer sysUserId,
                         @RequestParam(name = "copyNo") String copyNo,
                         @RequestParam(name = "districtId") Integer districtId,
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
            //管理员绑定站长app视推号
            systemService.modifySystemUserForBindStation(sysUserId, appUser, districtId);
            resultMap.put("state", 1);
            resultMap.put("msg", "ok");
            return resultMap;
        } else {//如果是合伙人
            resultMap.put("state", 0);
            resultMap.put("msg", "没有权限");
            return resultMap;

        }
    }

    //查询appUser
    @RequestMapping(value = "/getStationAppUserData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getStationAppUserData(@RequestParam(required = false, name = "search") String search,
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
        } else  {//如果是合伙人或站长
            resultMap.put("code", 1);
            resultMap.put("msg", "没有权限");
            return resultMap;
        }
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());

        return resultMap;
    }

    //站长充值记录
    @RequestMapping(value = "/stationOrderList", produces = "text/html;charset=UTF-8")
    public ModelAndView stationOrderList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/station/station-order-list");
        return modelAndView;
    }

    @RequestMapping(value = "/getStationOrderData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getStationOrderData(@RequestParam(required = false, name = "state") Integer state,
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
        Page<PayOrder> p = payOrderService.pageFindAllPayOrder(state, search, PayOrder.OBJECT_TYPE_STORE_VIP, page, limit);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }

}
