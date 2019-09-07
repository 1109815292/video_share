package com.juheshi.video.controller.admin;

import com.juheshi.video.entity.District;
import com.juheshi.video.service.DistrictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/district")
public class DistrictController {

    @Resource
    private DistrictService districtService;

    @RequestMapping("/select")
    public ModelAndView select() {
        List<District> list = districtService.findByParentId(0);
        return new ModelAndView("/admin/district/select", "list", list);
    }

    @RequestMapping("/getByParentId")
    @ResponseBody
    public Object getByParentId(@RequestParam(required = false, defaultValue = "0") Integer pid) {
        Map<String, Object> resultMap = new HashMap<>();
        List<District> list = districtService.findByParentId(pid);
        resultMap.put("state", 1);
        resultMap.put("data", list);
        return resultMap;
    }
}
