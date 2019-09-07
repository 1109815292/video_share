package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.*;
import com.juheshi.video.service.*;
import com.juheshi.video.util.*;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/app/wxLogin/")
public class WxOauthController {

    @Resource
    private AppUserService appUserService;

    @Resource
    private SysParamService sysParamService;

    @Resource
    private VarParamService varParamService;

    @Resource
    private CfgWxMessageService cfgWxMessageService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/oauth-base", produces = "text/html;charset=UTF-8")
    public String oauthBase(HttpServletRequest request) {
        String redirectUrl = "";
        try {
            String url = request.getParameter("url");
            System.out.println("oauthBase-url:" + url);
            redirectUrl = "http://" + request.getServerName() //服务器地址
                    + request.getContextPath()      //项目名称
                    + "/app/wxLogin/loginBase?url=" + URLEncoder.encode(url, "UTF-8");
            ;

            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + ConfigUtil.APPID + "&redirect_uri=" + redirectUrl + "&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect";

    }

    @RequestMapping(value = "/loginBase", produces = "text/html;charset=UTF-8")
    public String wxRedirectBase(HttpServletRequest request, HttpSession session) throws IOException {
        String code = request.getParameter("code");
        String url = request.getParameter("url");
        String param = "appid=" + ConfigUtil.APPID + "&secret=" + ConfigUtil.SECRET + "&code=" + code + "&grant_type=authorization_code ";

        String resultStr = HttpRequest.sendGet(ConfigUtil.WX_OAUTH_BASE_URL, param);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(resultStr, HashMap.class);
        String access_token = map.get("access_token").toString();
        String openId = map.get("openid").toString();
        AppUser appUser = appUserService.findUserByOpenId(openId);
        AppUser inviter = null;
        if (appUser == null) {
            String userParam = "access_token=" + access_token + "&openid=" + openId + "&lang=zh_CN ";

            String userInfoStr = HttpRequest.sendGet(ConfigUtil.WX_OAUTH_INFO_URL, userParam);

            Map<String, Object> infoMap = mapper.readValue(userInfoStr, HashMap.class);

            String nickName = infoMap.get("nickname").toString();
            String sex = infoMap.get("sex").toString();
            String province = infoMap.get("province").toString();
            String city = infoMap.get("city").toString();
            String country = infoMap.get("country").toString();
            String headimgurl = infoMap.get("headimgurl").toString();

            appUser = new AppUser();
            appUser.setOpenId(openId);
            appUser.setUserName(EncryptUtil.filterEmoji(nickName));
            appUser.setHeadImg(headimgurl);
            appUser.setCity(city);
            appUser.setAmount(0.0);
            appUser.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
            appUser.setSubscribeFlag(Constants.NO);
            if (url.contains("register")) { //有推荐人
                String tmp = url.substring(0, url.indexOf("/register"));
                String copyNo = tmp.substring(tmp.lastIndexOf("/") + 1);
                if (!"".equals(copyNo)) {
                    inviter = appUserService.findUserByCopyNo(copyNo);
                    if (inviter != null) {
                        appUser.setInviteCopyNo(inviter.getCopyNo());
                        appUser.setInviteId(inviter.getId());
                        Integer inviterUserLevel = inviter.getUserLevel();
                        if (inviterUserLevel != null) {
                            appUser.setUserLevel(inviterUserLevel + 1);
                        }
                    }
                }
            } else if (url.contains("copyNo")) { //带有copyNo参数
                String temp = url.substring(url.indexOf("?") + 1);
                if (!"".equals(temp)) {
                    String[] paramArr = temp.split("&");
                    for (String p : paramArr) {
                        String[] kv = p.split("=");
                        if (kv[0].equals("copyNo") && kv.length == 2 && !"".equals(kv[1])) {
                            inviter = appUserService.findUserByCopyNo(kv[1]);
                            if (inviter != null) {
                                appUser.setInviteCopyNo(inviter.getCopyNo());
                                appUser.setInviteId(inviter.getId());
                                Integer inviterUserLevel = inviter.getUserLevel();
                                if (inviterUserLevel != null) {
                                    appUser.setUserLevel(inviterUserLevel + 1);
                                }
                            }
                            break;
                        }
                    }
                }
            } else { //无推荐人进入
                SysParam sp = sysParamService.findByKey(Constants.SYS_PARAM_KEY_DEFAULT_INVITER);
                if (sp != null) {
                    inviter = appUserService.findUserByCopyNo(sp.getParamValue());
                    if (inviter != null) {
                        appUser.setInviteCopyNo(inviter.getCopyNo());
                        appUser.setInviteId(inviter.getId());
                        Integer inviterUserLevel = inviter.getUserLevel();
                        if (inviterUserLevel != null) {
                            appUser.setUserLevel(inviterUserLevel + 1);
                        }
                    } else {
                        appUser.setUserLevel(1);
                    }
                } else {
                    appUser.setUserLevel(1);
                }
            }
            appUserService.saveUser(appUser);

            if(inviter != null) {
                //邀请人粉丝数+1
                appUserService.modifyUserFansCount(inviter.getId());
                //粉丝的粉丝数+1
                if(inviter.getInviteId() != null)
                    appUserService.modifyUserFansOfFansCount(inviter.getInviteId());
            }


        }

        session.setAttribute(Constants.SESSION_APP_USER_ID, appUser.getId());
        session.setAttribute(Constants.SESSION_APP_USER_COPY_NO, appUser.getCopyNo());
        session.setAttribute(Constants.SESSION_APP_USER_NAME, appUser.getUserName());
        session.setAttribute(Constants.SESSION_APP_USER_OPEN_ID, openId);

        //发送模板消息
        List<CfgWxMessage> list = cfgWxMessageService.listFindByTypeAndPosition(CfgWxMessage.TYPE_TEMPLATE_MESSAGE, CfgWxMessage.TRIGGER_POSITION_NEW_REGISTER);
        if (list != null && list.size() > 0) {
            logger.debug("********************** 总共查询到"+list.size()+"条模板消息待发送 **********************");
            final List<CfgWxMessage> finalList = list;

            final AppUser inviterUser = inviter;
            final AppUser finalAppUser = appUser;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(inviterUser!=null) {
                        VarParam varParam = varParamService.findByVarName(Constants.VAR_PARAM_WX_ACCESS_TOKEN);
                        if(varParam != null){
                            if (varParam.getVarValue() == null ||
                                    "".equals(varParam.getVarValue()) || varParam.getUpdatedTime() == null || varParam.getVarExpiresIn() == null ||
                                    (Calendar.getInstance().getTime().getTime() - varParam.getUpdatedTime().getTime()) / 1000 >= varParam.getVarExpiresIn()) {
                                try {
                                    String param = "grant_type=client_credential&appid=" + ConfigUtil.APPID + "&secret=" + ConfigUtil.SECRET;
                                    String resultStr = HttpRequest.sendGet(ConfigUtil.WX_PUBLIC_ACCESS_TOKEN_URL, param);
                                    ObjectMapper mapper = new ObjectMapper();
                                    HashMap map = mapper.readValue(resultStr, HashMap.class);
                                    String accessToken = map.get("access_token").toString();
                                    Integer expiresIn = Integer.parseInt(map.get("expires_in").toString());
                                    varParam.setVarValue(accessToken);
                                    varParam.setVarExpiresIn(expiresIn);
                                    varParam.setRemark("调用刷新");
                                    varParamService.modify(varParam);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            WxMessageUtil wmu = null;
                            try {
                                wmu = new WxMessageUtil(varParam.getVarValue());
                                for(CfgWxMessage m : finalList) {
                                    try {
                                        String templateData = m.getTemplateData();
                                        if(templateData.contains("{{userName}}")) {
                                            templateData = templateData.replace("{{userName}}", finalAppUser.getUserName());
                                        }
                                        if (templateData.contains("time")) {
                                            templateData = templateData.replace("{{time}}",
                                                    new SimpleDateFormat("yyyy-MM-dd HH:mm").format(finalAppUser.getCreatedTime()));
                                        }
                                        wmu.sendTemplateMessage(inviterUser.getOpenId(), m.getTemplateId(), templateData, m.getTemplateUrl());
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


        return "redirect:" + url;
    }

}
