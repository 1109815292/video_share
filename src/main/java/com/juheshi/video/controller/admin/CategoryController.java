package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.Category;
import com.juheshi.video.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;


    @RequestMapping("/getCategoryData")
    @ResponseBody
    public Object getCategoryData(Integer group) {
        List<Category> categories = categoryService.findByGroup(group, Constants.YES);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("state", 1);
        resultMap.put("data", categories);
        return resultMap;
    }
}
