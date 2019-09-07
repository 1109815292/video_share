package com.juheshi.video.dao;

import com.juheshi.video.entity.CfgWxMessage;

import java.util.List;
import java.util.Map;

public interface CfgWxMessageDao {
    CfgWxMessage selectById(Integer id);

    List<CfgWxMessage> selectByParam(Map<String, Object> param);

    long countForPageSelectByParam(Map<String, Object> param);

    List<CfgWxMessage>  pageSelectByParam(Map<String, Object> param);


}
