package com.juheshi.video.service;

import com.juheshi.video.dao.WithdrawOrderDao;
import com.juheshi.video.entity.PayOrder;
import com.juheshi.video.entity.WithdrawOrder;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service("WithdrawService")
public class WithdrawService {

    @Resource
    WithdrawOrderDao withdrawOrderDao;

    public int createWithdrawOrder(WithdrawOrder withdrawOrder) {
        int exists;
        do {
            withdrawOrder.setOrderNo(UtilDate.getOrderNum());
            exists = withdrawOrderDao.checkOrderNo(withdrawOrder.getOrderNo());
        } while (exists == 1);
        withdrawOrder.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
        return withdrawOrderDao.insertWithdrawOrder(withdrawOrder);
    }

    public Double findTotalAmount(){
        return withdrawOrderDao.selectTotalAmount();
    }

}
