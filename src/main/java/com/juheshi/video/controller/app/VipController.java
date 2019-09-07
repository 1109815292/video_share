package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.VipTypeSetting;
import com.juheshi.video.service.VipTypeSettingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/app/advPay")
public class VipController {

    @Resource
    private VipTypeSettingService vipTypeSettingService;

    @RequestMapping(value = "/openVip", produces = "text/html;charset=UTF-8")
    public ModelAndView openVip(){
        List<VipTypeSetting> vipTypeSettingList = vipTypeSettingService.findAllVip(Constants.YES, VipTypeSetting.VIP_TYPE_ONLINE);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("vipTypeSettingList", vipTypeSettingList);
        modelAndView.setViewName("app/advPay/vipPay");
        return modelAndView;
    }


}
