package com.juheshi.video.service;


import com.juheshi.video.common.Constants;
import com.juheshi.video.dao.SystemDao;
import com.juheshi.video.entity.*;
import com.juheshi.video.util.EncryptUtil;
import com.juheshi.video.util.Page;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("SystemService")
public class SystemService {

    @Resource
    private SystemDao systemDao;

    @Resource
    private AppUserService appUserService;

    @Resource
    private SysParamService sysParamService;

    @Resource
    private StationmasterService stationmasterService;

    public SystemUser login(String userName, String password) throws Exception {
        EncryptUtil encryptUtil = new EncryptUtil();
        return systemDao.findByUserNameAndPassword(userName, encryptUtil.md5Digest(password));
    }

    public List<SystemUser> findAllAdminUser() {
        return systemDao.findAllUser();
    }

    public Integer countAllAdminUser() {
        return systemDao.countAllAdminUser();
    }

    public List<SystemRole> findAllRole() {
        return systemDao.findAllRole();
    }

    public List<SystemMenu> findAllSystemMenu() {
        return systemDao.findAllSysMenu();
    }

    public List<Map<String, Object>> findRolePermissionByRoleId(int SystemRoleId) {
        return systemDao.findRolePermissionByRoleId(SystemRoleId);
    }

    public SystemUser findUserById(int id) {
        return systemDao.findUserById(id);
    }

    public SystemRole findRoleById(int id) {
        return systemDao.findRoleById(id);
    }


    public void saveUser(SystemUser systemUser) {
        if (systemUser.getId() != null && systemUser.getId() > 0) {
            systemDao.updateUser(systemUser);
        } else {
            systemUser.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
            systemDao.insertUser(systemUser);
        }
    }

    public void saveRole(SystemRole systemRole) {
        if (systemRole.getId() != null && systemRole.getId() > 0) {
            systemDao.updateRole(systemRole);
        } else {
            systemDao.insertRole(systemRole);
        }
    }

    public void deleteUser(int id) {
        systemDao.deleteUser(id);
    }

    public void saveRolePermission(SystemRolePermission SystemRolePermission) {
        if (SystemRolePermission.getId() > 0) {

        } else {
            systemDao.insertRolePermission(SystemRolePermission);
        }
    }

    public void deleteRolePermission(int id) {
        systemDao.deleteRolePermission(id);
    }

    public void deleteRole(int id) {
        systemDao.deleteRole(id);
    }

    public boolean checkAuthority(int userId, String actionName) {
        SystemUser systemUser = systemDao.findUserById(userId);
        List<SystemMenu> systemMenuList = systemDao.findSysMenuByActionName("%" + actionName + "%");
//        if (systemMenuList.size() > 0){
//            SystemMenu systemMenu = systemMenuList.get(0);
//            List<SystemRolePermission> SystemRolePermissionList = SystemUserDao.findPermissionByRoleIdAndMenuId(systemUser.getRoleId(), systemMenu.getId());
//            if (SystemRolePermissionList.size() == 0){
//                return false;
//            }
//            SystemRolePermission systemRolePermission = SystemRolePermissionList.get(0);
//            if ("Y".equals(systemRolePermission.getIsGrant())){
//                return true;
//            }
//        }
        boolean find = false;
        int menuId = -1;
        if (systemMenuList.size() > 0) {
            for (SystemMenu m : systemMenuList) {
                String[] actions = m.getActionName().split(",");
                for (String action : actions) {
                    if (action.equals(actionName)) {
                        find = true;
                        menuId = m.getId();
                        break;
                    }
                }
                if (find) {
                    break;
                }
            }

            if (find && menuId != -1) {
                List<SystemRolePermission> SystemRolePermissionList = systemDao.findPermissionByRoleIdAndMenuId(systemUser.getRoleId(), menuId);
                if (SystemRolePermissionList.size() == 0) {
                    return false;
                }
                SystemRolePermission systemRolePermission = SystemRolePermissionList.get(0);
                if ("Y".equals(systemRolePermission.getIsGrant())) {
                    return true;
                }
            }
        }
        return false;
    }


    public Page<SystemUser> pageFindSysUser(String search, int pageNo, int pageSize) {
        return this.pageFindSysUser(search, null, null, pageNo, pageSize);
    }

    public Page<SystemUser> pageFindSysUser(String search, Integer parentId, Integer[] types, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("search", search);
        param.put("parentId", parentId);
        param.put("types", types);
        long count = systemDao.countForPageFindSysUserByParam(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<SystemUser> list = systemDao.pageFindSysUserByParam(param);
        Page<SystemUser> page = new Page<>();
        page.setTotal(count);
        page.setList(list);
        return page;
    }

    public int modifySystemUserForBindCopyNo(Integer sysUserId, String copyNo) {
        return this.modifySystemUserForBindCopyNo(sysUserId, copyNo, null);
    }

    public int modifySystemUserForBindCopyNo(Integer sysUserId, String copyNo, Integer userLevel) {
        if (userLevel != null) {
            AppUser appUser = appUserService.findUserByCopyNo(copyNo);
            if (appUser != null) {
                appUserService.modifyUserUserLevel(appUser.getId(), userLevel);
                appUserService.modifyUserClearInviter(appUser.getId());//清除邀请人信息
            }
        }
        return systemDao.modifySystemUserForBindCopyNo(sysUserId, copyNo);
    }

    public List<SystemUser> findAllSysUser(String search, Integer parentId, Integer[] types) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("search", search);
        param.put("parentId", parentId);
        param.put("types", types);
        return systemDao.findAllSysUserByParam(param);
    }

    public int modifySystemUserForBindStation(Integer sysUserId, AppUser appUser, Integer districtId) {
        //逻辑
        //将待绑定用户的邀请人id和邀请人视推号更改为默认邀请人
        SysParam param = sysParamService.findByKey(Constants.SYS_PARAM_KEY_DEFAULT_STATION_INVITER);
        if (param != null && param.getParamValue() != null && !"".equals(param.getParamValue())) {
            AppUser inviter = appUserService.findUserByCopyNo(param.getParamValue());
            if (inviter != null) {
                //修改appUser的stationFlag字段
                appUserService.modifyUserStationFlag(appUser.getId(), Constants.YES);
                //将用户userLevel更改为渠道等级
                appUserService.modifyUserUserLevel(appUser.getId(), AppUser.USER_LEVEL_CHANNEL);
                //将用户邀请人改为默认邀请人
                appUserService.modifyUserInviter(appUser.getId(), inviter.getId(), inviter.getCopyNo());

                Stationmaster stationmaster = stationmasterService.findByUserId(appUser.getId(), false);
                if (stationmaster == null) {
                    stationmaster = new Stationmaster();
                    stationmaster.setUserId(appUser.getId());
                    stationmaster.setCopyNo(appUser.getCopyNo());
                    stationmaster.setStationDistrictId(districtId);
                    stationmaster.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
                    stationmaster.setState(Stationmaster.STATE_NORMAL);
                    stationmasterService.create(stationmaster);
                }
//                appUserService.modifyUserStationDistrictId(appUser.getId(), districtId);
                return systemDao.modifySystemUserForBindCopyNo(sysUserId, appUser.getCopyNo());
            }

        }
        return 0;
    }
}
