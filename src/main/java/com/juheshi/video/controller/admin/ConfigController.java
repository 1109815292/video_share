package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.CfgWxMessage;
import com.juheshi.video.entity.VarParam;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.CfgWxMessageService;
import com.juheshi.video.service.VarParamService;
import com.juheshi.video.util.Page;
import com.juheshi.video.util.WxMessageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/admin/cfg")
@Controller
public class ConfigController {

    @Resource
    private CfgWxMessageService cfgWxMessageService;

    @Resource
    private VarParamService varParamService;

    @Resource
    private AppUserService appUserService;

    @RequestMapping(value = "/wxMessage", produces = "text/html;charset=UTF-8")
    public ModelAndView wxMessage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/config/config-wx-message");
        return modelAndView;
    }

    @RequestMapping(value = "/wxMessageData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object wxMessageData(@RequestParam(required = false, name = "type") Integer type,
                                @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                @RequestParam(required = false, name = "limit", defaultValue = "10") int limit) {
        Page<CfgWxMessage> p = cfgWxMessageService.pageFindAll(type, page, limit);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }

    @RequestMapping(value = "/wxMessageEdit", produces = "text/html;charset=UTF-8")
    public ModelAndView wxMessageEdit(Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        if (id != null) {
            CfgWxMessage cfgWxMessage = cfgWxMessageService.findById(id);
            modelAndView.addObject("config", cfgWxMessage);
        }
        modelAndView.setViewName("admin/config/config-wx-message-edit");
        return modelAndView;
    }

    @RequestMapping(value = "/wxMessageTest", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object wxMessageTest(Integer id, String openId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        CfgWxMessage cfgWxMessage = cfgWxMessageService.findById(id);
        if (cfgWxMessage == null) {
            resultMap.put("state", 0);
            resultMap.put("msg", "该消息ID数据不存在");
            return resultMap;
        }
        VarParam varParam = varParamService.findByVarName(Constants.VAR_PARAM_WX_ACCESS_TOKEN);
        if (varParam == null) {
            resultMap.put("state", 0);
            resultMap.put("msg", "未配置accessToken");
            return resultMap;
        }
        AppUser appUser = appUserService.findUserByOpenId(openId);
        if (appUser == null) {
            resultMap.put("state", 0);
            resultMap.put("msg", "openId用户不存在");
            return resultMap;
        }
        try {
            WxMessageUtil wmu = new WxMessageUtil(varParam.getVarValue());
            if (cfgWxMessage.getType().equals(CfgWxMessage.TYPE_CUSTOMER_SERVICE_MESSAGE)) {
                switch (cfgWxMessage.getMsgType()) {
                    case CfgWxMessage.MSG_TYPE_TEXT:
                        String content = cfgWxMessage.getContent();
                        if (content.contains("{{userName}}")) {
                            content = content.replace("{{userName}}", appUser.getUserName());
                        }
                        wmu.sendCustomServiceTextMessage(openId, content);
                        break;
                    case CfgWxMessage.MSG_TYPE_IMAGE:
                        wmu.sendCustomServiceImageMessage(openId, cfgWxMessage.getMediaId());
                        break;
                    case CfgWxMessage.MSG_TYPE_NEWS:
                        wmu.sendCustomServiceNewsMessage(openId,
                                cfgWxMessage.getTitle(), cfgWxMessage.getNewsDesc(),
                                cfgWxMessage.getNewsUrl(), cfgWxMessage.getNewsPicUrl());
                        break;
                }
            } else if (cfgWxMessage.getType().equals(CfgWxMessage.TYPE_TEMPLATE_MESSAGE)) {
                String templateData = cfgWxMessage.getTemplateData();
                if (templateData.contains("{{userName}}")) {
                    templateData = templateData.replace("{{userName}}", appUser.getUserName());
                }
                if (templateData.contains("time")) {
                    templateData = templateData.replace("{{time}}",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime()));
                }
                wmu.sendTemplateMessage(openId, cfgWxMessage.getTemplateId(), templateData, cfgWxMessage.getTemplateUrl());
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", 0);
            resultMap.put("msg", "发送消息异常");
            return resultMap;
        }

        resultMap.put("state", 1);
        resultMap.put("msg", "ok");
        return resultMap;
    }

}
