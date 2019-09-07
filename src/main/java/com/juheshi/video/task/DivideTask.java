package com.juheshi.video.task;

import com.juheshi.video.entity.PayOrder;
import com.juheshi.video.service.DivideService;
import com.juheshi.video.service.PayOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;

@Controller
public class DivideTask {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private boolean running = true;

    private static final long SLEEP_TIME = 60 * 1000;


    @Resource
    private PayOrderService payOrderService;


    @Resource
    private DivideService divideService;


    @PostConstruct
    public void init() {
        logger.info("分成任务开启");
        Thread divideThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        List<PayOrder> list = payOrderService.findByStateAndDivideState(PayOrder.STATE_FINISHED, PayOrder.DIVIDE_STATE_NO);
                        if (list != null) {
                            logger.debug("共{}条记录待分成处理", list.size());
                            for (PayOrder order : list) {
                                handleDivide(order);
                            }
                        }
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        logger.error("分成任务线程中断", e);
                    }
                }
            }
        });
        divideThread.start();
    }

    private void handleDivide(PayOrder order) {
        //处理分成
        try {
            divideService.handleDivide(order);
        } catch (Exception e) {
            logger.error("处理分成异常", e);
        }
    }

    @PreDestroy
    public void destroy() {
        logger.info("分成任务销毁");
        this.running = false;
    }
}
