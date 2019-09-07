package com.juheshi.video.dao;

import com.juheshi.video.entity.AdvProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AdvProductDao {

     AdvProduct selectById(Integer id);

     List<AdvProduct> selectListByParam(Map<String,Object> param);

     int save(AdvProduct advProduct);

     int modifyProduct(AdvProduct advProduct);

     int modifyProductViewCountById(Integer id);

     int modifyProductPeopleCountById(Integer id);


     int deleteById(Integer id);

     List<AdvProduct> selectByIds(@Param("ids") Integer[] ids);
}
