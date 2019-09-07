package com.juheshi.video.dao;

import com.juheshi.video.entity.WithdrawOrder;
import com.juheshi.video.service.WithdrawService;
import org.apache.ibatis.annotations.Param;

public interface WithdrawOrderDao {

    int checkOrderNo(@Param("orderNo") String orderNo);

    int insertWithdrawOrder(WithdrawOrder withdrawOrder);

    void updateWithdrawOrder(WithdrawOrder withdrawOrder);

    WithdrawOrder findByRechargeDivideId(@Param("rechargeId") int rechargeId);

    double selectTotalAmount();
}
