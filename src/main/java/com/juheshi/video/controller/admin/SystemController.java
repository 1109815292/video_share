package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.SystemRole;
import com.juheshi.video.entity.SystemRolePermission;
import com.juheshi.video.entity.SystemMenu;
import com.juheshi.video.entity.SystemUser;
import com.juheshi.video.service.SystemService;
import com.juheshi.video.util.EncryptUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/sys")
public class SystemController {

    @Resource
    private SystemService systemService;


    @RequestMapping(value = "/sysUserList", produces = "text/html;charset=UTF-8")
    public ModelAndView userList(){

        List<SystemUser> userList = systemService.findAllAdminUser();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userList", userList);
        modelAndView.setViewName("admin/sys/user-list");

        return modelAndView;
    }

    @RequestMapping(value = "/getSysUserData", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getSysUserData(HttpServletRequest request) throws IOException {

        List<SystemUser> userList = systemService.findAllAdminUser();
        Integer count = systemService.countAllAdminUser();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", count);
        resultMap.put("data", userList);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultMap);
        return json;
    }


    @RequestMapping(value = "/sysUserEdit", produces = "text/html;charset=UTF-8")
    public ModelAndView userEdit(HttpServletRequest request){

        String id = request.getParameter("id");
        ModelAndView modelAndView = new ModelAndView();
        SystemUser SystemUser;
        if (id != null && (!"".equals(id))){
            SystemUser = systemService.findUserById(Integer.valueOf(id));
        }else{
            SystemUser = new SystemUser();
        }
        List<SystemRole> roleList = systemService.findAllRole();
        modelAndView.addObject("user", SystemUser);
        modelAndView.addObject("roleList", roleList);
        modelAndView.setViewName("admin/sys/user-edit");
        return modelAndView;
    }

    @RequestMapping(value = "/sysUserSave", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String userSave(SystemUser SystemUser){
        systemService.saveUser(SystemUser);
        return "success";
    }

    @RequestMapping(value = "/sysUserDelete", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String userDelete(HttpServletRequest request){
        String id = request.getParameter("id");
        systemService.deleteUser(Integer.valueOf(id));
        return "success";
    }


    @RequestMapping(value = "/sysRoleList", produces = "text/html;charset=UTF-8")
    public ModelAndView roleList(){

        List<SystemRole> roleList = systemService.findAllRole();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roleList", roleList);
        modelAndView.setViewName("admin/sys/role-list");

        return modelAndView;
    }

    @RequestMapping(value = "/sysRoleEdit", produces = "text/html;charset=UTF-8")
    public ModelAndView roleEdit(HttpServletRequest request){

        String id = request.getParameter("id");
        ModelAndView modelAndView = new ModelAndView();

        if (id != null && (!"".equals(id))){
            SystemRole SystemRole = systemService.findRoleById(Integer.valueOf(id));
            modelAndView.addObject("role", SystemRole);
        }
        modelAndView.setViewName("admin/sys/role-edit");
        return modelAndView;
    }

    @RequestMapping(value = "/sysRoleSave", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String roleSave(HttpServletRequest request){

        String id = request.getParameter("id");
        String roleName = request.getParameter("roleName");
        String roleDesc = request.getParameter("roleDesc");

        SystemRole SystemRole = new SystemRole();
        if (id != null && (!"".equals(id))){
            SystemRole.setId(Integer.valueOf(id));
        }
        SystemRole.setRoleName(roleName);
        SystemRole.setRoleDesc(roleDesc);
        SystemRole.setState(1);
        systemService.saveRole(SystemRole);

        return "success";
    }

    @RequestMapping(value = "/sysRoleDelete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String roleDelete(HttpServletRequest request){
        String id = request.getParameter("id");
        systemService.deleteRole(Integer.valueOf(id));
        return "success";
    }



    @RequestMapping(value = "/sysRolePermissionList", produces = "text/html;charset=UTF-8")
    public ModelAndView rolePermissionList(){

        List<SystemRole> roleList = systemService.findAllRole();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roleList", roleList);
        modelAndView.setViewName("admin/sys/rolePermission-list");
        return modelAndView;
    }

    @RequestMapping(value = "/getSysRolePermission", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getRolePermission(HttpServletRequest request) throws IOException {
        String roleIdStr = request.getParameter("roldId");
        Integer roleId = 0;
        if (roleIdStr != null && (!"".equals(roleIdStr))){
            roleId = Integer.valueOf(roleIdStr);
        }

        List<Map<String, Object>> rolePermissionList = systemService.findRolePermissionByRoleId(roleId);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", rolePermissionList.size());
        resultMap.put("data", rolePermissionList);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultMap);
        return json;
    }

    @RequestMapping(value = "/sysRolePermissionEdit", produces = "text/html;charset=UTF-8")
    public ModelAndView rolePermissionEdit(){

        List<SystemMenu> systemMenuListList = systemService.findAllSystemMenu();
        List<SystemRole> roleList = systemService.findAllRole();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("systemMenuListList", systemMenuListList);
        modelAndView.addObject("roleList", roleList);
        modelAndView.setViewName("admin/sys/rolePermission-edit");
        return modelAndView;
    }

    @RequestMapping(value = "/saveSysRolePermission", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveRolePermission(HttpServletRequest request){
        String roleId = request.getParameter("roleId");
        String menuId = request.getParameter("menuId");
        String isGrant = request.getParameter("isGrant");

        SystemRolePermission SystemRolePermission = new SystemRolePermission();
        SystemRolePermission.setRoleId(Integer.valueOf(roleId));
        SystemRolePermission.setMenuId(Integer.valueOf(menuId));
        SystemRolePermission.setIsGrant(isGrant);

        systemService.saveRolePermission(SystemRolePermission);

        return "success";
    }

    @RequestMapping(value = "/sysRolePermissionDelete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String rolePermissionDelete(HttpServletRequest request){
        String id = request.getParameter("id");

        systemService.deleteRolePermission(Integer.valueOf(id));

        return "success";
    }

    @RequestMapping(value = "/updatePassword", produces = "text/html;charset=UTF-8")
    public ModelAndView updatePassword(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/sys/updatePassword");

        return modelAndView;
    }

    @RequestMapping(value = "/saveNewPassword", produces = "text/html;charset=UTF-8")
    public String saveNewPassword(HttpServletRequest request, HttpSession session) throws Exception {

        String newPassword = request.getParameter("newPassword");
        Integer userId = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_ID);
        SystemUser user = systemService.findUserById(userId);
        if (user != null){
            EncryptUtil encryptUtil = new EncryptUtil();
            user.setPassword(encryptUtil.md5Digest(newPassword));
            systemService.saveUser(user);
        }

        return "admin/sys/updatePasswordComplete";
    }

    @RequestMapping(value = "/updatePasswordComplete", produces = "text/html;charset=UTF-8")
    public ModelAndView updatePasswordComplete(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/sys/updatePasswordComplete");

        return modelAndView;
    }

}
