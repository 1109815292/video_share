package com.juheshi.video.service;

import com.juheshi.video.common.Constants;
import com.juheshi.video.controller.WXPay.WXPay;
import com.juheshi.video.controller.WXPay.WXPayment;
import com.juheshi.video.dao.AdvStoreDao;
import com.juheshi.video.dao.PayOrderDao;
import com.juheshi.video.dao.RechargeDivideDao;
import com.juheshi.video.dao.WithdrawOrderDao;
import com.juheshi.video.entity.*;
import com.juheshi.video.util.IDGenerator;
import com.juheshi.video.util.Page;
import com.juheshi.video.util.UtilDate;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayOrderService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private PayOrderDao payOrderDao;
    @Resource
    private AppUserService appUserService;
    @Resource
    private RechargeDivideDao rechargeDivideDao;
    @Resource
    private WithdrawService withdrawService;
    @Resource
    private WithdrawOrderDao withdrawOrderDao;
    @Resource
    private AdvStoreDao advStoreDao;

    @Resource
    private StatsDayService statsDayService;

    public Page<PayOrder> pageFindAllPayOrder(Integer state, String search, int pageNo, int pageSize) {
        return this.pageFindAllPayOrder(state, search, null, pageNo, pageSize);
    }

    public Page<PayOrder> pageFindAllPayOrder(Integer state, String search, Integer objectType, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("state", state);
        param.put("search", search);
        param.put("objectType", objectType);
        long count = payOrderDao.count(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<PayOrder> list = payOrderDao.pageSelectByParam(param);
        return new Page<PayOrder>(count, list);
    }

    public Page<PayOrder> pageFindAllPayOrder(Integer state, String search, Integer objectType, Integer userId, int pageNo, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("state", state);
        param.put("search", search);
        param.put("userId", userId);
        param.put("objectType", objectType);
        long count = payOrderDao.count(param);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        List<PayOrder> list = payOrderDao.pageSelectByParam(param);
        return new Page<PayOrder>(count, list);
    }

    public PayOrder findById(Integer id) {
        return payOrderDao.selectById(id);
    }

    public int createPayOrder(PayOrder payOrder) {
        int exists;
        do {
            payOrder.setOrderNo(UtilDate.getOrderNum());
            exists = payOrderDao.checkOrderNo(payOrder.getOrderNo());
        } while (exists == 1);
        payOrder.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
        return payOrderDao.save(payOrder);
    }

    public int modifyPrepayRawData(PayOrder payOrder) {
        return payOrderDao.modifyPayOrder(payOrder);
    }

    //支付成功回调
    public void savePaySuccess(String outTradeNo, String rawData) throws IOException, JDOMException {
        PayOrder payOrder = payOrderDao.selectByOrderNo(outTradeNo);
        if (payOrder.getState().equals(PayOrder.STATE_PREPAY) || payOrder.getState().equals(PayOrder.STATE_PAID)) {
            AppUser appUser = appUserService.findUserById(payOrder.getUserId());
            if (appUser == null) {
                payOrder.setCallbackRawData(rawData);
                payOrder.setState(PayOrder.STATE_ERROR);
                payOrder.setStateRemark(MessageFormat.format("用户[userId={0}]数据不存在.", payOrder.getUserId()));
                payOrderDao.modifyPayOrder(payOrder);
                return;
            }
            //购买vip
            if (payOrder.getObjectType().equals(PayOrder.OBJECT_TYPE_VIP)) {
                boolean isVip = appUser.getVipFlag().equals(Constants.YES);
                //用户成为会员
                appUserService.addUserVip(Constants.USER_ADD_VIP_TYPE_APP_PURCHASE, payOrder.getUserId(), payOrder.getObjectId(), "线上会员购买:支付回调成功", null);

                //如果邀请人不为空，且原来不是会员
                if (appUser.getInviteId() != null && !isVip) {
                    //邀请人粉丝vip数+1
                    appUserService.modifyUserFansVipCount(appUser.getInviteId(), 1);
                }

            } else if (payOrder.getObjectType().equals(PayOrder.OBJECT_TYPE_STORE_VIP)){

                AppUser storeUser = appUserService.findUserByCopyNo(payOrder.getStoreCopyNo());

                boolean isVip = storeUser.getVipFlag().equals(Constants.YES);
                //用户成为会员
                appUserService.addUserVip(Constants.USER_ADD_VIP_TYPE_APP_PURCHASE, storeUser.getId(), payOrder.getObjectId(), "线下会员购买:支付回调成功", null);

                //如果邀请人不为空，且原来不是会员
                if (storeUser.getInviteId() != null && !isVip) {
                    //邀请人粉丝vip数+1
                    appUserService.modifyUserFansVipCount(storeUser.getInviteId(), 1);
                }

                List<AdvStore> list = advStoreDao.selectByUserId(storeUser.getId());
                if (list != null && list.size() > 0) { //用户存在实体店记录
                    AdvStore advStore = list.get(0);//默认取第1个记录
                    if(advStore.getIndustryId() == null ){//如果实体店没有行业类型
                        //修改实体店所属行业
                        advStore.setIndustryId(payOrder.getIndustryId());
                        advStoreDao.modifyStore(advStore);
                    }

                } else {//给用户增加店铺
                    AdvStore advStore = new AdvStore();
                    advStore.setUserId(storeUser.getId());
                    advStore.setIndustryId(payOrder.getIndustryId());
                    advStore.setStationCopyNo(appUser.getCopyNo());
                    advStore.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
                    advStoreDao.save(advStore);
                }


            }

            payOrder.setCallbackRawData(rawData);
            payOrder.setState(PayOrder.STATE_FINISHED);
            payOrder.setPayTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
            payOrder.setStateRemark("微信支付回调");
            payOrderDao.modifyPayOrder(payOrder);

            double dailyFlow = payOrder.getAmount();
            if (dailyFlow > 0) {
                //日统计-日流水增加
                statsDayService.modifyNewsStats(new StatsDay(StatsDay.TYPE_DAILY_FLOW, dailyFlow));
            }
        } else {
            logger.info("订单号[{}]已处理或手动处理", outTradeNo);
        }
    }

    //退款回调
    public int saveRefundSuccess(String outTradeNo, String rawData) {
        PayOrder payOrder = payOrderDao.selectByOrderNo(outTradeNo);
        payOrder.setCallbackRawData(rawData);
        payOrder.setState(PayOrder.STATE_REFUND);
        return payOrderDao.modifyPayOrder(payOrder);
    }

    public PayOrder findByOrderNo(String orderNo) {
        return payOrderDao.selectByOrderNo(orderNo);
    }

    public PayOrder findByPrepayId(String prepayId) {
        return payOrderDao.selectByPrepayId(prepayId);
    }

    public int modifyPayOrderState(Integer id, Integer stateFrom, Integer stateTo, String stateRemark) {
        return payOrderDao.modifyPayOrderState(id, stateFrom, stateTo, stateRemark);
    }

    public Double findTotalFlow() {
        return payOrderDao.selectTotalFlow();
    }

    public List<PayOrder> findByStateAndDivideState(int state, int divideState) {
        Map<String, Object> param = new HashMap<>();
        param.put("state", state);
        param.put("divideState", divideState);
        param.put("objectType", PayOrder.OBJECT_TYPE_VIP);
        return payOrderDao.selectByParam(param);
    }

    public int modifyPayOrderDivideState(Integer id, Integer divideState) {
        return payOrderDao.modifyPayOrderDivideState(id, divideState);
    }

    public List<Map<String, Object>> findStoreVipPayOrder(Integer userId, int pageNo, int pageSize){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("limit", pageSize);
        param.put("offset", (pageNo - 1) * pageSize);
        return payOrderDao.findStoreVipPayOrder(param);
    }
}
