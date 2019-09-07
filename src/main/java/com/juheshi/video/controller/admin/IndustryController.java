package com.juheshi.video.controller.admin;

import com.juheshi.video.entity.Industry;
import com.juheshi.video.service.IndustryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/industry")
public class IndustryController {

    @Resource
    private IndustryService industryService;


    @RequestMapping("/getIndustryData")
    @ResponseBody
    public Object getIndustryData() {
        List<Industry> industries = industryService.findAllIndustry();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("state", 1);
        resultMap.put("data", industries);
        return resultMap;
    }
}
