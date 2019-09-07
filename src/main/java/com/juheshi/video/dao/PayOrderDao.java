package com.juheshi.video.dao;

import com.juheshi.video.entity.PayOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PayOrderDao {


    PayOrder selectById(Integer id);

    long count(Map<String, Object> param);

    List<PayOrder> pageSelectByParam(Map<String, Object> param);

    List<Map<String, Object>> findStoreVipPayOrder(Map<String, Object> param);

    int save(PayOrder payOrder);

    int modifyPayOrder(PayOrder payOrder);

    int checkOrderNo(@Param("orderNo") String orderNo);

    PayOrder selectByOrderNo(@Param("orderNo") String orderNo);

    PayOrder selectByPrepayId(@Param("prepayId")String prepayId);

    int modifyPayOrderState(@Param("id") Integer id, @Param("stateFrom") Integer stateFrom, @Param("stateTo") Integer stateTo, @Param("stateRemark") String stateRemark);

    double selectTotalFlow();

    List<PayOrder> selectByParam(Map<String, Object> param);

    int modifyPayOrderDivideState(@Param("id")Integer id, @Param("divideState")Integer divideState);
}
