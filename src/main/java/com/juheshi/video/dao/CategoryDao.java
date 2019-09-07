package com.juheshi.video.dao;

import com.juheshi.video.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryDao {

    int save(Category category);

    Category selectById(Integer id);

    List<Category> findAllCategory();

    List<Category> selectByParam(Map<String, Object> param);

    int updateCategoryVideoCount(@Param("id")Integer id, @Param("num") Integer num);
}
