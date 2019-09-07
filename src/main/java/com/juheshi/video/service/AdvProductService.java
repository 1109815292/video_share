package com.juheshi.video.service;

import com.juheshi.video.dao.AdvProductDao;
import com.juheshi.video.entity.AdvProduct;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdvProductService {

    @Resource
    private AdvProductDao advProductDao;


    public int createAdvProduct(AdvProduct product) {
        product.setViewCount(0);
        product.setPeopleCount(0);
        if (product.getProductUrl() != null && !"".equals(product.getProductUrl())) {
            if (!product.getProductUrl().contains("http")) {
                product.setProductUrl("http://" + product.getProductUrl());
            }
        }
        return advProductDao.save(product);
    }

    public AdvProduct findById(Integer id) {
        return advProductDao.selectById(id);
    }

    public int modifyAdvProduct(AdvProduct product) {
        if (product.getProductUrl() != null && !"".equals(product.getProductUrl())) {
            if (!product.getProductUrl().contains("http")) {
                product.setProductUrl("http://" + product.getProductUrl());
            }
        }
        return advProductDao.modifyProduct(product);
    }

    public List<AdvProduct> findByUserId(Integer userId, Integer type) {
        return findByUserId(null, userId, type);
    }

    public List<AdvProduct> findByUserId(String keywords, Integer userId, Integer type) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("keywords", keywords);
        param.put("type", type);
        return advProductDao.selectListByParam(param);
    }

    public int deleteById(Integer id) {
        return advProductDao.deleteById(id);
    }


    public int modifyAdvProductViewCount(Integer id) {
        return advProductDao.modifyProductViewCountById(id);
    }

    public int modifyAdvProductPeopleCount(Integer id) {
        return advProductDao.modifyProductPeopleCountById(id);
    }

    public List<AdvProduct> findProductByIds(String ids) {
        if (!"".equals(ids)) {
            String[] strIdArr = ids.split(",");
            Integer[] idArr = new Integer[strIdArr.length];
            for (int i = 0; i < strIdArr.length; i++) {
                idArr[i] = Integer.valueOf(strIdArr[i]);
            }
            return advProductDao.selectByIds(idArr);
        }
        return null;
    }
}
