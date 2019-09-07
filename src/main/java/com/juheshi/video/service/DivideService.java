package com.juheshi.video.service;

import com.juheshi.video.controller.WXPay.WXPay;
import com.juheshi.video.controller.WXPay.WXPayment;
import com.juheshi.video.dao.RechargeDivideDao;
import com.juheshi.video.dao.WithdrawOrderDao;
import com.juheshi.video.entity.*;
import com.juheshi.video.util.Page;
import com.juheshi.video.util.UtilDate;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("DivideService")
public class DivideService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RechargeDivideDao rechargeDivideDao;

    @Resource
    private WithdrawOrderDao withdrawOrderDao;

    @Resource
    private AppUserService appUserService;

    @Resource
    private WithdrawService withdrawService;

    @Resource
    private StatsDayService statsDayService;

    @Resource
    private PayOrderService payOrderService;


    public Double countDivide(Map<String, Object> param) {
        return rechargeDivideDao.countDivide(param);
    }

    public List<Map<String, Object>> findDivideByUserId(int userId) {
        return rechargeDivideDao.findDivideByDivideUser(userId);
    }


    public Page<RechargeDivide> pageFind(Integer divideUserId, Integer fansId, Integer payFlag, Date dateFrom, Date dateTo, int pageNo, int pageSize) {
        return this.pageFind(divideUserId, fansId, payFlag, null, dateFrom, dateTo, pageNo, pageSize);
    }

    public Page<RechargeDivide> pageFind(Integer divideUserId, Integer fansId, Integer payFlag, Integer minUserLevel, Date dateFrom, Date dateTo, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("divideUserId", divideUserId);
        param.put("fansId", fansId);
        param.put("payFlag", payFlag);
        param.put("dateFrom", dateFrom);
        param.put("dateTo", dateTo);
        param.put("minUserLevel", minUserLevel);
        long count = rechargeDivideDao.countForPageSelectByParamWithCascade(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<RechargeDivide> list = rechargeDivideDao.pageSelectByParamWithCascade(param);
        Page<RechargeDivide> page = new Page<RechargeDivide>();
        page.setList(list);
        page.setTotal(count);
        return page;
    }

    //重新提现
    public int saveReCash(Integer id) throws IOException, JDOMException {
        RechargeDivide rechargeDivide = rechargeDivideDao.findById(id);
        if (rechargeDivide != null) {
            WithdrawOrder wo = withdrawOrderDao.findByRechargeDivideId(rechargeDivide.getId());
            if (wo == null) {
                AppUser divideUser = appUserService.findUserById(rechargeDivide.getDivideUserId());
                wo = new WithdrawOrder();
                wo.setAmount(rechargeDivide.getDivideAmount());
                wo.setRechargeId(rechargeDivide.getId());
                wo.setOpenId(divideUser.getOpenId());
                wo.setUserId(divideUser.getId());
                wo.setState(0);
                wo.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
                withdrawService.createWithdrawOrder(wo);
            } else {
                if (wo.getState() != 0) {
                    return 0;
                }
            }

            String resultStr = WXPayment.payment(wo.getOrderNo(), wo.getOpenId(), (int) (wo.getAmount() * 100));
            //修改WithdrawOrder状态和RechargeDivide状态
            System.out.println("~~~~~~~~~~~~~~~重新提现支付回调~~~~~~~~~~~~~~~");
            System.out.println(resultStr);
            wo.setStateRemark(resultStr);
            Map<String, String> map = WXPay.doXMLParse(resultStr);// 解析微信返回的信息（仍然返回xml格式），以Map形式存储便于取值
            if ("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))) {
                wo.setState(1);
                rechargeDivide.setPayFlag(1);
                rechargeDivideDao.updateRechargeDivide(rechargeDivide);
                withdrawOrderDao.updateWithdrawOrder(wo);
                //日统计-提现
                statsDayService.modifyNewsStats(new StatsDay(StatsDay.TYPE_CASH_AMOUNT, wo.getAmount()));
                return 1;
            }
            withdrawOrderDao.updateWithdrawOrder(wo);
        }
        return 0;
    }


    public void handleDivide(PayOrder order) throws Exception {
        AppUser appUser = appUserService.findUserById(order.getUserId());
        double totalCash = 0.0;
        //收益分成
        Double amount = order.getAmount();
        List<DivideRate> divideRateList = rechargeDivideDao.findAllDivideRate();

        Integer fansId = appUser.getId();
        int userLevel = appUser.getUserLevel();
        String inviteNo = appUser.getInviteCopyNo();
        Double countRate = 0.0;

        for (int i = 0; i < (userLevel - 1); i++) {
            AppUser inviteUser = appUserService.findUserByCopyNo(inviteNo);
            Double divideRate;
            if (inviteUser.getUserLevel() == 1) {
                divideRate = 1 - countRate;
            } else {
                divideRate = divideRateList.get(i).getDivideRate();
                countRate += divideRate;
            }
            Double divideAmount = amount * divideRate;

            //增加分成记录
            RechargeDivide rechargeDivide = new RechargeDivide();
            rechargeDivide.setUserId(appUser.getId());
            rechargeDivide.setRechargeAmount(amount);
            rechargeDivide.setDivideUserId(inviteUser.getId());
            rechargeDivide.setDivideAmount(divideAmount);
            rechargeDivide.setFansId(fansId);
            rechargeDivide.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
            rechargeDivide.setPayFlag(0);
            rechargeDivideDao.insertRechargeDivide(rechargeDivide);
            fansId = inviteUser.getId();
            inviteNo = inviteUser.getInviteCopyNo();

            //增加用户表income收益数据
            appUserService.modifyUserIncome(divideAmount, inviteUser.getId());

            //非合伙人立即提现
            if (inviteUser.getUserLevel() > 1) {
                WithdrawOrder withdrawOrder = new WithdrawOrder();
                withdrawOrder.setOpenId(inviteUser.getOpenId());
                withdrawOrder.setUserId(inviteUser.getId());
                withdrawOrder.setAmount(divideAmount);
                withdrawOrder.setRechargeId(rechargeDivide.getId());
                withdrawOrder.setState(0);
                withdrawService.createWithdrawOrder(withdrawOrder);
                String resultStr = WXPayment.payment(withdrawOrder.getOrderNo(), inviteUser.getOpenId(), (int) (divideAmount * 100));
                withdrawOrder.setStateRemark(resultStr);
                Map<String, String> map = WXPay.doXMLParse(resultStr);// 解析微信返回的信息（仍然返回xml格式），以Map形式存储便于取值
                if ("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))) {
                    withdrawOrder.setState(1);
                    rechargeDivide.setPayFlag(1);
                    rechargeDivideDao.updateRechargeDivide(rechargeDivide);
                    totalCash += divideAmount;
                }
                withdrawOrderDao.updateWithdrawOrder(withdrawOrder);
            }
        }

        if(totalCash > 0) {
            //日统计-提现金额
            statsDayService.modifyNewsStats(new StatsDay(StatsDay.TYPE_CASH_AMOUNT, totalCash));
        }

        //修改订单分成状态
        payOrderService.modifyPayOrderDivideState(order.getId(), PayOrder.DIVIDE_STATE_YES);

    }
}
