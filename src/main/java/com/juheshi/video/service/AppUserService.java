package com.juheshi.video.service;

import com.juheshi.video.common.Constants;
import com.juheshi.video.common.WxApi;
import com.juheshi.video.dao.AppUserDao;
import com.juheshi.video.dao.AppUserPageDao;
import com.juheshi.video.dao.AppUserVipDao;
import com.juheshi.video.entity.*;
import com.juheshi.video.util.*;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.codec.binary.Base64;

@Service("UserService")
public class AppUserService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AppUserDao appUserDao;

    @Resource
    private AppUserPageDao appUserPageDao;

    @Resource
    private AppUserVipDao appUserVipDao;

    @Resource
    private LogVipRecordService logVipRecordService;

    @Resource
    private VipTypeSettingService vipTypeSettingService;

    @Resource
    private SysParamService sysParamService;

    @Resource
    private StatsDayService statsDayService;

    @Resource
    private VarParamService varParamService;


    @Value("#{resourceProperties['temp.download.dir']}")
    private String tempDownloadDir;

    public AppUser findUserById(int id) {
        return appUserDao.selectById(id);
    }

    public AppUser findUserByCopyNo(String copyNo) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("copyNo", copyNo);
        return appUserDao.selectOneByParam(param);
    }

    public AppUser findUserByOpenId(String openId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("openId", openId);
        return appUserDao.selectOneByParam(param);
    }


    public int saveUser(AppUser user) {
        user.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
        user.setAdminFlag(Constants.NO); //管理员标记
        user.setVipFlag(Constants.NO); //会员标记
        user.setPublishFlag(Constants.YES); //允许发布内容标记
        user.setAmount((double) 0);
        user.setState(AppUser.STATE_NORMAL);
        int exists;
        do {
            user.setCopyNo(IDGenerator.getRandom(Constants.APP_USER_ID_LENGTH));
            exists = appUserDao.checkCopyNo(user.getCopyNo());
        } while (exists == 1);

        int num = appUserDao.save(user);
        appUserPageDao.save(new AppUserPage(user.getId()));

        final AppUser finalUser = user;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                uploadUserHeader(finalUser);
            }
        });
        t.start();

        //异步生成邀请码
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                generateInviteQRCode(finalUser);
            }
        });
        t2.start();

        //日统计-新用户+1
        statsDayService.modifyNewsStats(new StatsDay(StatsDay.TYPE_NEW_USER, null));
        return num;
    }

    public Page<AppUser> pageFindAllUser(String search, String vipFlag, int pageNo, int pageSize) {
        return this.pageFindAllUser(search, null, vipFlag, pageNo, pageSize);
    }

    public Page<AppUser> pageFindAllUser(String search, String inviteCopyNo, String vipFlag, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("search", search);
        param.put("vipFlag", vipFlag);
        param.put("inviteCopyNo", inviteCopyNo);
        long count = appUserDao.count(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<AppUser> list = appUserDao.pageSelectByParamWithInviter(param);
       //List<AppUser> list = appUserDao.pageSelectByParam(param);
        Page<AppUser> page = new Page<>();
        page.setTotal(count);
        page.setList(list);
        return page;
    }

    public Page<AppUser> pageFindAllUserForStation(String search, String stationCopyNo, String vipFlag, Integer vipType, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("search", search);
        param.put("vipFlag", vipFlag);
        param.put("vipType", vipType);
        param.put("stationCopyNo", stationCopyNo);
        long count = appUserDao.count(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
//        List<AppUser> list = appUserDao.pageSelectByParamWithInviter(param);
        List<AppUser> list = appUserDao.pageSelectByParam(param);
        Page<AppUser> page = new Page<>();
        page.setTotal(count);
        page.setList(list);
        return page;
    }


    public List<AppUser> findAllUser(String search, String adminFlag) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("search", search);
        param.put("adminFlag", adminFlag);
        return appUserDao.selectByParam(param);
    }

    private void uploadUserHeader(AppUser user) {
        try {
            URL url = new URL(user.getHeadImg());
            String fileName = UUID.randomUUID().toString().replaceAll("-", "");
            String contentType = URLUtil.getContentType(url);
            if (contentType == null || "".equals(contentType) || contentType.equals("application/octet-stream")) {
                contentType = "image/jpeg";
            }
            String path = OSSClientUtil.putImageDatePathFileWithId(fileName, contentType, user.getCopyNo(), url.openStream());
            if (!"".equals(path)) {
                appUserDao.modifyUserHeadImgById(user.getId(), OSSClientUtil.URL_PREFIX + path);
            }
        } catch (Exception e) {
            logger.error("上传头像异常\n 封面图地址: " + user.getHeadImg() + "\n", e);
        }
    }

    public AppUserPage findUserPageByUserId(Integer userId) {

        return appUserPageDao.selectByUserId(userId);
    }


    public int modifyUserPublishFlag(Integer id, String publishFlag) {
        return appUserDao.modifyUserPublishFlag(id, publishFlag);
    }

    public int modifyUserAdminFlag(Integer id, String adminFlag) {
        return appUserDao.modifyUserAdminFlag(id, adminFlag);
    }

    public int modifyUserState(Integer id, Integer state) {
        return appUserDao.modifyUserState(id, state);
    }

    public int modifyUserLastViewTime(Integer id) {
        AppUser appUser = appUserDao.selectById(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (appUser.getLastViewTime() == null || !sdf.format(appUser.getLastViewTime()).equals(sdf.format(Calendar.getInstance().getTime()))) {
            //日统计-活跃用户+1
            statsDayService.modifyNewsStats(new StatsDay(StatsDay.TYPE_ACTIVE_USER, null));
        }
        return appUserDao.modifyUserLastViewTime(id, Timestamp.valueOf(UtilDate.getDateFormatter(Calendar.getInstance().getTime())));
    }

    public int addUserVip(Integer type, Integer userId, Integer vipTypeId, String remark, Integer sysUserId) {

        AppUser user = appUserDao.selectById(userId);

        VipTypeSetting vip = vipTypeSettingService.findVipById(vipTypeId);

        boolean isPermanentVip = vip.getDays() == -1;

        user.setVipFlag(Constants.YES);

        if(vip.getType().equals(VipTypeSetting.VIP_TYPE_ONLINE)) { //线上会员
            user.setVipType(AppUser.VIP_TYPE_ONLINE);
        } else if (vip.getType().equals(VipTypeSetting.VIP_TYPE_OFFLINE)) { //线下会员
            user.setVipType(AppUser.VIP_TYPE_OFFLINE);
        }

        Date now = Calendar.getInstance().getTime();

        Timestamp effectiveTime;
        Timestamp deadline;

        if (user.getVipDeadline() != null && now.getTime() - user.getVipDeadline().getTime() < 0) { //曾经买过vip且vip未过期
            effectiveTime = user.getVipDeadline(); //本次购买vip的生效日期为上个vip的结束日期
            if (isPermanentVip) {
                deadline = null;
            } else { //计算会员结束日期
                Calendar calDeadline = Calendar.getInstance();
                calDeadline.setTime(user.getVipDeadline());
                calDeadline.add(Calendar.DAY_OF_YEAR, vip.getDays() + vip.getGiftDays());
                deadline = Timestamp.valueOf(UtilDate.getDateFormatter(calDeadline.getTime()));
            }
        } else { //首次购买vip或vip已过期
            effectiveTime = Timestamp.valueOf(UtilDate.getDateFormatter());//从当前时间开始生效
            if (isPermanentVip) {
                deadline = null;
            } else { //计算会员结束日期
                Calendar calDeadline = Calendar.getInstance();
                calDeadline.setTime(now);
                calDeadline.add(Calendar.DAY_OF_YEAR, vip.getDays() + vip.getGiftDays());
                deadline = Timestamp.valueOf(UtilDate.getDateFormatter(calDeadline.getTime()));
            }
        }

        //更新用户vip结束时间
        user.setVipDeadline(deadline);

        //添加用户vip记录
        appUserVipDao.save(new AppUserVip(userId, vipTypeId, effectiveTime, deadline, Timestamp.valueOf(UtilDate.getDateFormatter())));

        //更新数据库
        int num = appUserDao.modifyUserVip(user);
        //记录日志
        logVipRecordService.createLog(new LogVipRecord(userId, vipTypeId, type, vip.getPresentPrice(), remark, sysUserId));
        return num;
    }


    public int modifyUserPage(AppUserPage appUserPage) {
        //修改头像
        if (appUserPage.getHeadImg() != null && (!"".equals(appUserPage.getHeadImg()))) {
            AppUser appUser = appUserDao.selectById(appUserPage.getUserId());
            if (!appUser.getHeadImg().equals(appUserPage.getHeadImg())) {
                appUserDao.modifyUserHeadImgById(appUser.getId(), appUserPage.getHeadImg());
            }
        }

        //
        return appUserPageDao.modifyUserPage(appUserPage);
    }

    //生成邀请二维码
    public String generateInviteQRCode(AppUser appUser) {
        InputStream is = null;
        int expireDay;
        try {
            //查询配置过期时间
            SysParam paramExpiresIn = sysParamService.findByKey(Constants.SYS_PARAM_KEY_WX_QR_CODE_EXPIRES_IN);
            if (paramExpiresIn == null || paramExpiresIn.getParamValue().equals("")) {
                expireDay = 20;
            } else {
                try {
                    expireDay = Integer.parseInt(paramExpiresIn.getParamValue());
                } catch (Exception e) {
                    expireDay = 20;
                }
            }

            VarParam varParam = varParamService.findByVarName(Constants.VAR_PARAM_WX_ACCESS_TOKEN);
            if (varParam == null) {
                String param = "grant_type=client_credential&appid=" + ConfigUtil.APPID + "&secret=" + ConfigUtil.SECRET;
                String resultStr = HttpRequest.sendGet(ConfigUtil.WX_PUBLIC_ACCESS_TOKEN_URL, param);
                ObjectMapper mapper = new ObjectMapper();
                HashMap map = mapper.readValue(resultStr, HashMap.class);
                String accessToken = map.get("access_token").toString();
                Integer expiresIn = Integer.parseInt(map.get("expires_in").toString());
                varParam = new VarParam();
                varParam.setVarName(Constants.VAR_PARAM_WX_ACCESS_TOKEN);
                varParam.setVarValue(accessToken);
                varParam.setVarExpiresIn(expiresIn);
                varParam.setVarDesc("微信公众号accessToken");
                varParam.setRemark("创建");
                varParamService.create(varParam);
            }

            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".png";
            File logoFile = FileDown.downloadFile(appUser.getHeadImg(), tempDownloadDir);
            ByteArrayOutputStream baos = WxApi.getQrcodeByteStream(appUser.getCopyNo(), varParam.getVarValue(), expireDay * 24 * 60 * 60);

            byte[] dataBytes = baos.toByteArray();

            is = new ByteArrayInputStream(dataBytes);//转换后的输入流
            BufferedImage srcImage = ImageIO.read(is);
            //BufferedImage srcImage = ImageIO.write( new File(tempDownloadDir + "/" + fileName));
            ImageIO.write(srcImage, "png", baos);

            //生成邀请图片(P.S 因为用的微信生成的二维码,此处width和height没效果)
            String base64String = ZXingCodeUtils.createQRCode(logoFile, srcImage, "", 200, 200, "");
            byte[] bytes = Base64.decodeBase64(base64String);

            OSSClientUtil.putFile(OSSClientUtil.IMAGES + "/" + appUser.getCopyNo() + "/invite", fileName, new ByteInputStream(bytes, bytes.length));
            String inviteQRCode = OSSClientUtil.URL_PREFIX + OSSClientUtil.IMAGES + "/" + appUser.getCopyNo() + "/invite/" + fileName;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, expireDay);
            appUserDao.modifyUserInviteQRCode(appUser.getId(), inviteQRCode, Timestamp.valueOf(UtilDate.getDateFormatter(cal.getTime())));
            return inviteQRCode;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public Page<AppUser> pageFindUserFans(Integer userId, String search, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("inviteId", userId);
        param.put("search", search);
        long count = appUserDao.count(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<AppUser> list = appUserDao.pageSelectByParam(param);
        Page<AppUser> p = new Page<>();
        p.setTotal(count);
        p.setList(list);
        return p;
    }

    public Page<AppUser> pageFindFansOfFans(Integer userId, String search, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("inviteId", userId);
        param.put("search", search);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<AppUser> list = appUserDao.findFansOfFans(param);
        long count = appUserDao.countFansOfFans(param);
        Page<AppUser> p = new Page<>();
        p.setTotal(count);
        p.setList(list);
        return p;
    }

    public int modifyUserSubscribeFlag(Integer userId, String subscribeFlag) {
        return appUserDao.modifyUserSubscribeFlag(userId, subscribeFlag);
    }

    public Page<AppUser> pageFindAllUserWithCopyNo(String search, String inviteCopyNo, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("inviteCopyNo", inviteCopyNo);
        param.put("search", search);
        long count = appUserDao.count(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<AppUser> list = appUserDao.pageSelectByParam(param);
        Page<AppUser> p = new Page<>();
        p.setTotal(count);
        p.setList(list);
        return p;
    }

    public int modifyUserUserLevel(Integer id, Integer userLevel) {
        return appUserDao.modifyUserLevel(id, userLevel);
    }

    public int modifyVipFlagForTask(String vipFlagFrom, String vipFlagTo, Date today) {
        return appUserDao.modifyVipFlagForTask(vipFlagFrom, vipFlagTo, today);
    }

    public int modifyVipFlagForTask2() {
        Date today = Calendar.getInstance().getTime();
        List<AppUser> list = appUserDao.selectForTask(Constants.YES, today);
        if (list != null && list.size() > 0) {
            List<Integer> ids = new ArrayList<>();
            List<Integer> inviterIds = new ArrayList<>();

            for (AppUser au : list) {
                ids.add(au.getId());
                if (au.getInviteId() != null) {
                    inviterIds.add(au.getInviteId());
                }
            }
            int num = appUserDao.modifyVipFlagForTask2(Constants.NO, ids);

            if (inviterIds.size() > 0) {
                appUserDao.modifyFansVipCountForTask(inviterIds);
            }


            return num;
        } else {
            return 0;
        }

    }

    public long findAllUserCount() {
        return appUserDao.selectAllUserCount();
    }


    public int modifyUserFansCount(Integer id) {
        return appUserDao.modifyUserFansCount(id);
    }

    public int modifyUserFansOfFansCount(Integer id) {
        return appUserDao.modifyUserFansOfFansCount(id);
    }

    public int modifyUserFansVipCount(Integer id, Integer num) {
        return appUserDao.modifyUserFansVipCount(id, num);
    }

    public int modifyUserIncome(Double income, Integer id) {
        return appUserDao.modifyUserIncome(income, id);
    }

    public int modifyUserClearInviter(Integer id) {
        return appUserDao.modifyUserClearInviter(id);
    }

    public int modifyUserInviter(Integer id, Integer inviterId, String inviterCopyNo) {
        return appUserDao.modifyUserInviter(id, inviterId, inviterCopyNo);
    }

    public int modifyUserStationFlag(Integer id, String stationFlag) {
        return appUserDao.modifyUserStationFlag(id, stationFlag);
    }

}
