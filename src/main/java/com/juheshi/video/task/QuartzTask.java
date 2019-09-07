package com.juheshi.video.task;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.SeckillOrder;
import com.juheshi.video.entity.StatsDay;
import com.juheshi.video.entity.VarParam;
import com.juheshi.video.service.*;
import com.juheshi.video.util.ConfigUtil;
import com.juheshi.video.util.HttpRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Component
public class QuartzTask {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private StatsDayService statsDayService;

    @Resource
    private AppUserService appUserService;

    @Resource
    private VarParamService varParamService;

    @Resource
    private VideoStoreService videoStoreService;

    @Resource
    private SeckillOrderService seckillOrderService;


    /**
     * CRON表达式                含义
     "0 0 12 * * ?"            每天中午十二点触发
     "0 15 10 ? * *"            每天早上10：15触发
     "0 15 10 * * ?"            每天早上10：15触发
     "0 15 10 * * ? *"        每天早上10：15触发
     "0 15 10 * * ? 2005"    2005年的每天早上10：15触发
     "0 * 14 * * ?"            每天从下午2点开始到2点59分每分钟一次触发
     "0 0/5 14 * * ?"        每天从下午2点开始到2：55分结束每5分钟一次触发
     "0 0/5 14,18 * * ?"        每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发
     "0 0-5 14 * * ?"        每天14:00至14:05每分钟一次触发
     "0 10,44 14 ? 3 WED"    三月的每周三的14：10和14：44触发
     "0 15 10 ? * MON-FRI"   每个周一、周二、周三、周四、周五的10：15触发
     */

    /**
     * 每天0点触发（创建每日统计）
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void statsDayCreateTask() {
        Calendar cal = Calendar.getInstance();
        int exists = statsDayService.checkExists(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        if (exists == 0) {
            statsDayService.createNewStats(new StatsDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)));
        } else {
            logger.debug("日期为【" + new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + "】的日统计记录已存在");
        }

    }

    /**
     * 每天0点触发（修改48小时内视频标识）
     */
//    @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    public void modifyVideoStoreWithin48hoursTask() {
        int num = videoStoreService.updateVideoStoreWithin48hoursForTask();
        logger.debug("每天0点触发（修改48小时内视频标识）--> 成功修改【" + num + "】条记录");
    }

    /**
     * 每天1点触发（修改会员标识）
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void modifyVipFlagTask() {
//        Calendar cal = Calendar.getInstance();
//        int num = appUserService.modifyVipFlagForTask(Constants.YES, Constants.NO, cal.getTime());
        int num = appUserService.modifyVipFlagForTask2();
        logger.debug("每天1点触发（修改会员标识）--> 成功修改【" + num + "】条记录");
    }

    /**
     * 刷新微信access_token定时任务
     * 每天整点触发
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void refreshWeChatAccessToken() {
        logger.debug("***************** 刷新微信access_token定时任务开始执行 *****************" + Calendar.getInstance().getTime());
        long start = System.currentTimeMillis();
        VarParam varParam = varParamService.findByVarName(Constants.VAR_PARAM_WX_ACCESS_TOKEN);
        if (varParam == null)
            return;
        try {
            String param = "grant_type=client_credential&appid=" + ConfigUtil.APPID + "&secret=" + ConfigUtil.SECRET;
            String resultStr = HttpRequest.sendGet(ConfigUtil.WX_PUBLIC_ACCESS_TOKEN_URL, param);
            ObjectMapper mapper = new ObjectMapper();
            HashMap map = mapper.readValue(resultStr, HashMap.class);
            String accessToken = map.get("access_token").toString();
            Integer expiresIn = Integer.parseInt(map.get("expires_in").toString());
            varParam.setVarValue(accessToken);
            varParam.setVarExpiresIn(expiresIn);
            varParam.setRemark("定时任务刷新");
            varParamService.modify(varParam);
        } catch (IOException e) {
            logger.error("微信定时刷新access_token任务异常", e);
        }
        long done = System.currentTimeMillis();
        logger.debug("***************** 刷新微信access_token定时任务执行完成【耗时:{}毫秒】*****************", (done - start));
    }

    @Scheduled(cron = "0 0/5 * * * ?" )
    public void quitOrderByScheduled() {
        logger.debug("订单刷新功能：主要将5分钟未付款的订单设置成取消状态！");
        SeckillOrder seckillOrder = new SeckillOrder ();
        seckillOrder.setStatus ( "0" );
        List<SeckillOrder> seckillOrders = seckillOrderService.listSeckillOrder ( seckillOrder );
        Timestamp timestamp = new Timestamp ( System.currentTimeMillis () - 300000 );
        for (SeckillOrder order : seckillOrders) {
            if(order.getCreateTime ().after ( timestamp )){
                order.setStatus ( "3" );
                seckillOrderService.quitSeckillOrder ( order );
            }
        }
    }
}
