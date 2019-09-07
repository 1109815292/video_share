package com.juheshi.video.dao;

import com.juheshi.video.entity.SystemRole;
import com.juheshi.video.entity.SystemRolePermission;
import com.juheshi.video.entity.SystemMenu;
import com.juheshi.video.entity.SystemUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SystemDao {

    SystemUser findByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

    List<SystemUser> findAllUser();
    Integer countAllAdminUser();

    List<SystemRole> findAllRole();

    List<SystemMenu> findAllSysMenu();
    List<SystemMenu> findSysMenuByActionName(@Param("actionName") String actionName);

    List<Map<String, Object>> findRolePermissionByRoleId(@Param("roleId") int SystemRoleId);

    List<SystemRolePermission> findPermissionByRoleIdAndMenuId(@Param("roleId") int SystemRoleId, @Param("menuId") int menuId);

    SystemUser findUserById(@Param("id") int id);
    SystemRole findRoleById(@Param("id") int id);

    void insertUser(SystemUser user);
    void updateUser(SystemUser user);
    void deleteUser(@Param("id") int id);

    void insertRole(SystemRole SystemRole);

    void updateRole(SystemRole SystemRole);

    void insertRolePermission(SystemRolePermission SystemRolePermission);

    void deleteRolePermission(int id);

    void deleteRole(@Param("id") int id);


    List<SystemUser> pageFindSysUserByParam(Map<String,Object> param);

    long countForPageFindSysUserByParam(Map<String,Object> param);

    int modifySystemUserForBindCopyNo(@Param("id") Integer id, @Param("copyNo") String copyNo);

    List<SystemUser> findAllSysUserByParam(Map<String, Object> param);
}
