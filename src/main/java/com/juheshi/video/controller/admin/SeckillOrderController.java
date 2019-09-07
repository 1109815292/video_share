package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.AppUserPage;
import com.juheshi.video.entity.SeckillOrder;
import com.juheshi.video.entity.SystemUser;
import com.juheshi.video.service.AppUserPageService;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.SeckillOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单模块
 */
@Controller
@RequestMapping("/admin/seckillOrder")
public class SeckillOrderController {

    @Resource
    private AppUserService appUserService;

    @Resource
    private SeckillOrderService seckillOrderService;

    @Resource
    private AppUserPageService appUserPageService;

    //订单展示页面
    @RequestMapping(value = "/getOrder", produces = "text/html;charset=UTF-8")
    public ModelAndView getOrder() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/seckill/seckill-order-list");
        return modelAndView;
    }

    @RequestMapping(value = "/getOrderDate", produces = "text/html;charset=UTF-8")
    public Object getOrderDate(@RequestParam(required = false, name = "id") Integer id,
                               @RequestParam(required = false, name = "seckillId") Integer seckillId,
                               @RequestParam(required = false, name = "status") String status,
                               @RequestParam(required = false, name = "createTimeBegin") Timestamp createTimeBegin,
                               @RequestParam(required = false, name = "createTimeEnd") Timestamp createTimeEnd,
                               @RequestParam(required = false, name = "examineTimeBegin") Timestamp examineTimeBegin,
                               @RequestParam(required = false, name = "examineTimeEnd") Timestamp examineTimeEnd,
                               @RequestParam(required = false, name = "consumeTimeBegin") Timestamp consumeTimeBegin,
                               @RequestParam(required = false, name = "consumeTimeEnd") Timestamp consumeTimeEnd,
                               @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                               @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                               HttpServletRequest request){
        HttpSession session = request.getSession();
        Map<String, Object> resultMap = new HashMap<String, Object> ();
        Integer userType = (Integer) session.getAttribute( Constants.SESSION_ADMIN_USER_TYPE);
        String bindCopyNo = (String) session.getAttribute(Constants.SESSION_ADMIN_USER_APP_USER_COPY_NO);
        Integer userId;
        if (userType.equals( SystemUser.TYPE_ADMIN)) {
            userId = null;
        } else {
            AppUser appUser = appUserService.findUserByCopyNo(bindCopyNo);
            userId = appUser.getId();
        }
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setId ( id );
        seckillOrder.setSeckillId ( seckillId );
        if(status.equals ( "0" ) || status.equals ( "1" )){
            seckillOrder.setExamine ( status );
        }else {
            seckillOrder.setStatus ( status );
        }
        seckillOrder.setCreateTimeBegin ( createTimeBegin );
        seckillOrder.setCreateTimeBegin ( createTimeEnd );
        seckillOrder.setExamineTimeBegin ( examineTimeBegin );
        seckillOrder.setExamineTimeEnd ( examineTimeEnd );
        seckillOrder.setConsumeTimeBegin ( consumeTimeBegin );
        seckillOrder.setConsumeTimeEnd ( consumeTimeEnd );
        List<SeckillOrder> seckillOrders = seckillOrderService.listSeckillOrder ( seckillOrder );
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", seckillOrderService.listSeckillOrderNum(seckillOrder));
        resultMap.put("data", seckillOrders);
        return resultMap;
    }

    //订单展示页面
    @RequestMapping(value = "/getUser", produces = "text/html;charset=UTF-8")
    public ModelAndView getUser() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/seckill/seckill-user-list");
        return modelAndView;
    }

    @RequestMapping(value = "/getUserDate", produces = "text/html;charset=UTF-8")
    public Object getUserDate(@RequestParam(required = false, name = "userId") Integer userId,
                               @RequestParam(required = false, name = "phone") String phone,
                               @RequestParam(required = false, name = "times") Integer times,
                               @RequestParam(required = false, name = "consumeTimeBegin") Timestamp consumeTimeBegin,
                               @RequestParam(required = false, name = "consumeTimeEnd") Timestamp consumeTimeEnd,
                               @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                               @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                               HttpServletRequest request){
        HttpSession session = request.getSession();
        Map<String, Object> resultMap = new HashMap<String, Object> ();
        Integer userType = (Integer) session.getAttribute( Constants.SESSION_ADMIN_USER_TYPE);
        String bindCopyNo = (String) session.getAttribute(Constants.SESSION_ADMIN_USER_APP_USER_COPY_NO);
        if (userType.equals( SystemUser.TYPE_ADMIN)) {
            userId = null;
        } else {
            AppUser appUser = appUserService.findUserByCopyNo(bindCopyNo);
            userId = appUser.getId();
        }
        Map map = new HashMap<> ();
        map.put ( "userId" , userId );
        map.put ( "phone" , phone );
        map.put ( "times" , times );
        map.put ( "consumeTimeBegin" , consumeTimeBegin );
        map.put ( "consumeTimeEnd" , consumeTimeEnd );
        List<AppUserPage> userPage = appUserPageService.getUserPage ( map );
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", appUserPageService.getUserPage ( map ));
        resultMap.put("data", userPage);
        return resultMap;
    }
}
