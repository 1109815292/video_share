package com.juheshi.video.service;

import com.juheshi.video.dao.CategoryDao;
import com.juheshi.video.entity.Category;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    @Resource
    private CategoryDao categoryDao;

    public List<Category> findAllCategory(){
        return categoryDao.findAllCategory();
    }

    public List<Category> findByGroup(Integer group, String enabledFlag) {
        Map<String, Object> param = new HashMap<>();
        param.put("group", group);
        param.put("enabledFlag", enabledFlag);
        return categoryDao.selectByParam(param);
    }

    public int create(Category category) {
        return categoryDao.save(category);
    }

    public int modifyCategoryVideoCount(Integer id, Integer num){
        return categoryDao.updateCategoryVideoCount(id, num);
    }
}
