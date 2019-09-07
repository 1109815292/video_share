package com.juheshi.video.dao;

import com.juheshi.video.entity.AppUser;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AppUserDao {

    int checkCopyNo(@Param("copyNo") String copyNo);

    int modifyUserHeadImgById(@Param("id") Integer id, @Param("headImg") String headImg);

    int save(AppUser user);

    AppUser selectOneByParam(Map<String, Object> param);

    long count(Map<String, Object> param);

    List<AppUser> pageSelectByParam(Map<String, Object> param);

    List<AppUser> pageSelectByParamWithInviter(Map<String,Object> param);

    List<AppUser> selectByParam(Map<String, Object> param);

    List<AppUser> findFansOfFans(Map<String, Object> param);

    long countFansOfFans(Map<String, Object> param);

    AppUser selectById(int id);

    int modifyUserPublishFlag(@Param("id") Integer id, @Param("publishFlag") String publishFlag);

    int modifyUserAdminFlag(@Param("id") Integer id, @Param("adminFlag") String adminFlag);

    int modifyUserState(@Param("id") Integer id, @Param("state") Integer state);

    int modifyUserVip(AppUser appUser);

    int modifyUserInviteQRCode(@Param("id") Integer id, @Param("inviteQRCode") String inviteQRCode, @Param("inviteQRCodeExpiresIn") Timestamp inviteQRCodeExpiresIn);

    int modifyUserLastViewTime(@Param("id") Integer id, @Param("lastViewTime") Timestamp lastViewTime);

    int modifyUserSubscribeFlag(@Param("id") Integer id, @Param("subscribeFlag") String subscribeFlag);

    int modifyUserLevel(@Param("id") Integer id, @Param("userLevel") Integer userLevel);

    int modifyVipFlagForTask(@Param("vipFlagFrom") String vipFlagFrom, @Param("vipFlagTo") String vipFlagTo, @Param("today") Date today);

    long selectAllUserCount();

    int modifyUserFansCount(Integer id);

    int modifyUserFansOfFansCount(Integer id);

    int modifyUserFansVipCount(@Param("id") Integer id, @Param("num") Integer num);

    int modifyUserIncome(@Param("income") Double income, @Param("id") Integer id);


    List<AppUser> selectForTask(@Param("vipFlag") String vipFlagFrom, @Param("today") Date today);

    int modifyVipFlagForTask2(@Param("vipFlag")String vipFlag, @Param("ids") List<Integer> ids);

    int modifyFansVipCountForTask(@Param("ids")List<Integer> ids);

    int modifyUserClearInviter(Integer id);

    int modifyUserStationFlag(@Param("id") Integer id, @Param("stationFlag") String stationFlag);

    int modifyUserInviter(@Param("id") Integer id, @Param("inviteId") Integer inviteId, @Param("inviteCopyNo") String inviteCopyNo);
}
