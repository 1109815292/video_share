package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.SeckillGoods;
import com.juheshi.video.entity.SeckillStatis;
import com.juheshi.video.entity.SystemUser;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.service.SeckillGoodsService;
import com.juheshi.video.service.SeckillGroupService;
import com.juheshi.video.service.SeckillStatisService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 秒杀模块
 */
@Controller
@RequestMapping("/admin/seckillGoods")
public class SeckillGoodsController {

    @Resource
    private SeckillGoodsService seckillGoodsService;

    @Resource
    private AppUserService appUserService;

    @Resource
    private SeckillStatisService seckillStatisService;

    @Resource
    private SeckillGroupService seckillGroupService;

    //秒杀列表页面
    @RequestMapping(value = "/getSeckill", produces = "text/html;charset=UTF-8")
    public ModelAndView addSeckill(Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/seckill/seckill-goods-list");
        return modelAndView;
    }

    @RequestMapping(value = "/getSeckillDate", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public Object getSeckillDate(@RequestParam(required = false, name = "name") String name,
                                         @RequestParam(required = false, name = "storeId") Integer storeId,
                                         @RequestParam(required = false, name = "status") String status,
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
        SeckillGoods seckillGoods = new SeckillGoods ();
        seckillGoods.setName ( name );
        seckillGoods.setStoreId ( 0 );
        seckillGoods.setStatus ( status );
        seckillGoods.setLimit ( limit );
        seckillGoods.setOffset ( (page - 1) * limit );
        List<SeckillGoods> seckillGoods1 = seckillGoodsService.listSeckillGoods ( seckillGoods );
        for (SeckillGoods goods : seckillGoods1) {
            SeckillStatis statis = new SeckillStatis ();
            statis.setId ( goods.getStoreId () );
            goods.setSeckillStatis ( seckillStatisService.findOneSeckillStatis ( statis ) );
        }
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", seckillGoodsService.listSeckillGoodsNum(seckillGoods));
        resultMap.put("data", seckillGoods1);
        return resultMap;
    }

    @RequestMapping(value = "/addSeckill", produces = "text/html;charset=UTF-8")
    public ModelAndView addSeckill( SeckillGoods seckillGoods ) {
        ModelAndView modelAndView = new ModelAndView();
        SeckillGoods oneSeckillGoods = seckillGoodsService.findOneSeckillGoods ( seckillGoods );
        modelAndView.addObject("oneSeckillGoods", oneSeckillGoods);
        modelAndView.setViewName("admin/seckill/seckill-goods-add");
        return modelAndView;
    }

    @RequestMapping(value = "/addSeckillDate", produces = "text/html;charset=UTF-8")
    public ModelAndView addSeckillDate( SeckillGoods seckillGoods ) {
        ModelAndView modelAndView = new ModelAndView();
        if(seckillGoods.getId () != 0){
            seckillGoodsService.updateSeckillGoods ( seckillGoods );
            if(seckillGoods.getGroups () == 1){
                seckillGroupService.updateSeckillGroup ( seckillGoods.getSeckillGroup () );
            }
        }else {
            seckillGoodsService.insertSeckillGoods ( seckillGoods );
            if(seckillGoods.getGroups () == 1){
                seckillGroupService.insertSeckillGroup ( seckillGoods.getSeckillGroup () );
            }
        }
        modelAndView.setViewName("admin/seckill/seckill-goods-list");
        return modelAndView;
    }
}
