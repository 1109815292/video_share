package com.juheshi.video.servlet;

import com.juheshi.video.common.Constants;
import com.juheshi.video.common.wechat.MessageUtil;
import com.juheshi.video.common.wechat.MsgType;
import com.juheshi.video.common.wechat.WeChatAPI;
import com.juheshi.video.entity.*;
import com.juheshi.video.service.*;
import com.juheshi.video.util.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class WxReceiveServlet extends HttpServlet {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private VarParamService varParamService;

    @Autowired
    private SysParamService sysParamService;

    @Autowired
    private CfgWxMessageService cfgWxMessageService;

    @Autowired
    private VideoStoreService videoStoreService;


    public void init(ServletConfig config) throws ServletException {
        logger.info("WxReceiveServlet init ...");
        //在servlet中注入service
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(" in WxReceiveServlet method [doGet]...");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        try {
            if (!WeChatAPI.checkSignature(req, resp)) {
                logger.debug("================>> check sign failure <<================");
                return;
            }
            logger.debug("================>> check sign pass <<================");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug(" in WxReceiveServlet method [doPost]...");
        String result = "";
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            Map<String, String> requestMap = MessageUtil.parseXml(req);
            // 发送方帐号（open_id）
            String openId = requestMap.get("FromUserName");
            // 公众帐号
            String fromUser = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            String eventType = requestMap.get("Event");
            logger.debug("消息类型：" + msgType);

            AppUser appUser = appUserService.findUserByOpenId(openId);
            AppUser inviter = null;
            switch (msgType) {
                case MsgType.EVENT:
                    String eventKey = requestMap.get("EventKey");
                    switch (eventType) {
                        case MsgType.SUBSCRIBE://关注
                            if (appUser == null) { //新用户点击关注
                                String accessToken = getAccessToken(false);
                                if (accessToken == null) {
                                    return;
                                }
                                HashMap userInfoMap = getUserInfo(accessToken, openId);
                                if (userInfoMap == null) {
                                    return;
                                }
                                appUser = new AppUser();
                                appUser.setOpenId(openId);
                                appUser.setUserName(EncryptUtil.filterEmoji(userInfoMap.get("nickname").toString()));
                                appUser.setHeadImg(userInfoMap.get("headimgurl").toString());
                                appUser.setCity(userInfoMap.get("city").toString());
                                appUser.setAmount(0.0);
                                appUser.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
                                appUser.setSubscribeFlag(Constants.YES);
                                if (eventKey != null && !"".equals(eventKey)) {//点击关注，并带有EventKey
                                    //发送提示消息
                                    if (eventKey.startsWith(WeChatAPI.EVENT_KEY_PREFIX)) {
                                        String ek = eventKey.split(WeChatAPI.EVENT_KEY_PREFIX)[1];
                                        inviter = appUserService.findUserByCopyNo(ek);
                                        if (inviter != null) {
                                            appUser.setInviteId(inviter.getId());
                                            appUser.setInviteCopyNo(inviter.getCopyNo());
                                            appUser.setUserLevel(inviter.getUserLevel() == null ? 1 : inviter.getUserLevel() + 1);
                                        }
                                    }
                                } else { //直接点击关注的新用户（）
                                    SysParam param = sysParamService.findByKey(Constants.SYS_PARAM_KEY_DEFAULT_INVITER);
                                    if (param != null) { //如果设置了默认邀请人
                                        inviter = appUserService.findUserByCopyNo(param.getParamValue());
                                        if (inviter != null) {
                                            appUser.setInviteId(inviter.getId());
                                            appUser.setInviteCopyNo(inviter.getCopyNo());
                                            appUser.setUserLevel(inviter.getUserLevel() == null ? 1 : inviter.getUserLevel() + 1);
                                        } else { //如果设置的默认邀请人不存在
                                            appUser.setUserLevel(1);
                                        }
                                    } else { //如果没设置默认邀请人
                                        appUser.setUserLevel(1);
                                    }
                                }
                                appUserService.saveUser(appUser);

                                if (inviter != null) {
                                    //邀请人粉丝数+1
                                    appUserService.modifyUserFansCount(inviter.getId());
                                    //粉丝的粉丝数+1
                                    if (inviter.getInviteId() != null)
                                        appUserService.modifyUserFansOfFansCount(inviter.getInviteId());
                                }


                                sendTemplateMessage(appUser, inviter, CfgWxMessage.TRIGGER_POSITION_NEW_REGISTER);

                            } else { //老用户再次关注
                                appUserService.modifyUserSubscribeFlag(appUser.getId(), Constants.YES);
                            }

                            //发送客服消息 - 关注
                            sendCustomServiceMessage(appUser, CfgWxMessage.TRIGGER_POSITION_SUBSCRIBE);
                            break;
                        case MsgType.UB_SUBSCRIBE://取消关注
                            if (appUser != null) {
                                appUserService.modifyUserSubscribeFlag(appUser.getId(), Constants.NO);
                            }
                            break;
                        case MsgType.SCAN://已关注用户触发扫描二维码
                            logger.debug("scan");
                            logger.debug("=============>>> EventKey:" + eventKey);
                            //发送客服消息 - 扫码
                            sendCustomServiceMessage(appUser, CfgWxMessage.TRIGGER_POSITION_SCAN);
                            break;

                    }
                    break;
                case MsgType.TEXT:
                    logger.debug("文本消息");
                    String content = requestMap.get("Content").trim();
                    switch (content) {
                        case "激活"://激活逻辑
                            final String finalOpenId = openId;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    AppUser appUser = appUserService.findUserByOpenId(finalOpenId);
                                    if(appUser == null)
                                        return;
                                    if(appUser.getStationFlag() == null || !appUser.getStationFlag().equals(Constants.YES))
                                        return;

                                    List<CfgWxMessage> list = cfgWxMessageService.listFindByTypeAndPosition(CfgWxMessage.TYPE_CUSTOMER_SERVICE_MESSAGE, CfgWxMessage.TRIGGER_POSITION_ACTIVE);
                                    if(list == null)
                                        return;
                                    CfgWxMessage cwm = list.get(0);

                                    //查询该站长下视频发送审核信息发送失败的记录
                                    List<VideoStore> videoStores = videoStoreService.findVideoStoreByStationCopyNo(appUser.getCopyNo(), 0, 0);
                                    List<Integer> ids = new ArrayList<>();
                                    String content = cwm.getContent();
                                    if(videoStores != null && videoStores.size() > 0) {
                                        content += "未审核的视频有：\r\n";
                                        for (VideoStore v : videoStores) {
                                            content += "---  <a href=\"http://wx.stb.pkddz.cn/app/stationmaster/videoCheck/"+v.getId()+"\">" + v.getTitle() + "</a>  --- \r\n";
                                            content += "\r\n";
                                            ids.add(v.getId());
                                        }
                                    }
                                    String accessToken = getAccessToken(false);
                                    try {
                                        new WxMessageUtil(accessToken).sendCustomServiceTextMessage(finalOpenId, content);
                                        if(ids.size() > 0) {
                                            //发送成功后修改状态
                                            videoStoreService.updateVideoStoreNotifiedForBatch(1, ids);
                                        }
                                    } catch (Exception e) {
                                        logger.error("激活-发送客服文本消息异常", e);
                                    }
                                }
                            }).start();
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    //发送历史记录连接
                    //result = wxService.autoSendHistory(openId, fromUser, historyUrl);
                    break;
            }

        } catch (NoSuchAlgorithmException e) {
            logger.error("验证签名报错:\n", e);
        } catch (Exception e) {
            logger.error("微信回调异常:\n", e);
        } finally {
            out.print(result);
            out.flush();
            out.close();
        }

    }

    private void sendTemplateMessage(AppUser appUser, AppUser inviter, int triggerPosition) {

        final AppUser finalAppUser = appUser;
        final AppUser finalInviter = inviter;
        final int finalTriggerPosition = triggerPosition;


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                List<CfgWxMessage> list = cfgWxMessageService.listFindByTypeAndPosition(CfgWxMessage.TYPE_TEMPLATE_MESSAGE, finalTriggerPosition);
                if (list != null && list.size() > 0) {
                    logger.debug("********************** 总共查询到" + list.size() + "条模板消息待发送 **********************");
                    if (finalInviter != null) {
                        String accessToken = getAccessToken(false);
                        WxMessageUtil wmu = null;
                        try {
                            wmu = new WxMessageUtil(accessToken);
                            for (CfgWxMessage m : list) {
                                try {
                                    String templateData = m.getTemplateData();
                                    if (templateData.contains("{{userName}}")) {
                                        templateData = templateData.replace("{{userName}}", finalAppUser.getUserName());
                                    }
                                    if (templateData.contains("time")) {
                                        templateData = templateData.replace("{{time}}",
                                                new SimpleDateFormat("yyyy-MM-dd HH:mm").format(finalAppUser.getCreatedTime()));
                                    }
                                    wmu.sendTemplateMessage(finalInviter.getOpenId(), m.getTemplateId(), templateData, m.getTemplateUrl());
                                    //休眠1秒
                                    Thread.sleep(500);
                                } catch (Exception e) {
                                    logger.error("发送模板消息异常", e);
                                }
                            }
                        } finally {
                            if (wmu != null)
                                wmu.close();
                        }

                    }
                }

            }
        });
        t.start();
    }

    private void sendCustomServiceMessage(AppUser appUser, int triggerPosition) {
        final AppUser finalAppUser = appUser;
        final int finalTriggerPosition = triggerPosition;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                List<CfgWxMessage> list = cfgWxMessageService.listFindByTypeAndPosition(CfgWxMessage.TYPE_CUSTOMER_SERVICE_MESSAGE, finalTriggerPosition);
                if (list != null && list.size() > 0) {
                    logger.debug("********************** 总共查询到" + list.size() + "条客服消息待发送 **********************");
                    String accessToken = getAccessToken(false);
                    WxMessageUtil wmu = null;
                    try {
                        wmu = new WxMessageUtil(accessToken);
                        for (CfgWxMessage m : list) {
                            try {
                                switch (m.getMsgType()) {
                                    case CfgWxMessage.MSG_TYPE_TEXT:
                                        String content = m.getContent();
                                        if (content.contains("{{userName}}")) {
                                            content = content.replace("{{userName}}", finalAppUser.getUserName());
                                        }
                                        wmu.sendCustomServiceTextMessage(finalAppUser.getOpenId(), content);
                                        break;
                                    case CfgWxMessage.MSG_TYPE_IMAGE:
                                        wmu.sendCustomServiceImageMessage(finalAppUser.getOpenId(), m.getMediaId());
                                        break;
                                    case CfgWxMessage.MSG_TYPE_NEWS:
                                        wmu.sendCustomServiceNewsMessage(finalAppUser.getOpenId(), m.getTitle(), m.getNewsDesc(), m.getNewsUrl(), m.getNewsPicUrl());
                                        break;
                                }
                                //休眠1秒
                                Thread.sleep(500);
                            } catch (Exception e) {
                                logger.error("微信发送客服消息处理异常", e);
                            }
                        }
                    } finally {
                        if (wmu != null)
                            wmu.close();
                    }

                }
            }
        });
        t.start();

    }

    private HashMap getUserInfo(String accessToken, String openId) {
        try {
            Object err;
            HashMap result = null;
            int retryTime = 0;
            do {
                String param = "access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
                String resultStr = HttpRequest.sendGet(ConfigUtil.WX_PUBLIC_USER_INFO, param);
                logger.debug("getUserInfo ---> resultStr : \n" + resultStr);
                ObjectMapper mapper = new ObjectMapper();
                result = mapper.readValue(resultStr, HashMap.class);
                err = result.get("errcode");
                logger.debug("getUserInfo ---> err : \n" + err);
                if (err != null) {
                    Integer errcode = (Integer) err;
                    if (errcode.equals(42001)) {
                        retryTime += 1;
                        if (retryTime > 3) {
                            logger.error(resultStr);
                            break;
                        }
                        accessToken = getAccessToken(true);

                    } else {

                        logger.error(resultStr);
                        break;
                    }
                }
            } while (err != null);
            return result;
        } catch (IOException e) {
            logger.error("微信公众号获取用户信息异常:\n", e);
            return null;
        }
    }

    private String getAccessToken(boolean needRefresh) {
        try {
            VarParam varParam = varParamService.findByVarName(Constants.VAR_PARAM_WX_ACCESS_TOKEN);
            if (varParam == null || varParam.getVarValue() == null ||
                    "".equals(varParam.getVarValue()) || varParam.getUpdatedTime() == null || varParam.getVarExpiresIn() == null ||
                    (Calendar.getInstance().getTime().getTime() - varParam.getUpdatedTime().getTime()) / 1000 >= varParam.getVarExpiresIn() || needRefresh) {
                String param = "grant_type=client_credential&appid=" + ConfigUtil.APPID + "&secret=" + ConfigUtil.SECRET;
                String resultStr = HttpRequest.sendGet(ConfigUtil.WX_PUBLIC_ACCESS_TOKEN_URL, param);
                ObjectMapper mapper = new ObjectMapper();
                HashMap map = mapper.readValue(resultStr, HashMap.class);
                String accessToken = map.get("access_token").toString();
                Integer expiresIn = Integer.parseInt(map.get("expires_in").toString());
                if (varParam == null) {
                    varParam = new VarParam();
                    varParam.setVarName(Constants.VAR_PARAM_WX_ACCESS_TOKEN);
                    varParam.setVarValue(accessToken);
                    varParam.setVarExpiresIn(expiresIn);
                    varParam.setVarDesc("微信公众号accessToken");
                    varParamService.create(varParam);
                } else {
                    varParam.setVarValue(accessToken);
                    varParam.setVarExpiresIn(expiresIn);
                    varParam.setRemark("调用刷新");
                    varParamService.modify(varParam);
                }
                return varParam.getVarValue();
            } else {
                return varParam.getVarValue();
            }
        } catch (IOException e) {
            logger.error("微信公众号获取ACCESS_TOKEN异常:\n", e);
            return null;
        }

    }
}
