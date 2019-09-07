package com.juheshi.video.controller.admin;


import com.juheshi.video.entity.Video;
import com.juheshi.video.entity.VipTypeSetting;
import com.juheshi.video.service.VipTypeSettingService;
import com.juheshi.video.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/vip")
public class VipTypeSettingController {

    @Resource
    private VipTypeSettingService vipTypeSettingService;

    @RequestMapping(value = "/vipList", produces = "text/html;charset=UTF-8")
    public ModelAndView videoList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/vip/vip-list");
        return modelAndView;
    }

    @RequestMapping(value = "/getVipData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getVipData(@RequestParam(required = false, name = "page", defaultValue = "1") int page,
                               @RequestParam(required = false, name = "limit", defaultValue = "10") int limit) {
        Page<VipTypeSetting> p = vipTypeSettingService.pageFindAllVip( page, limit);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }
}
