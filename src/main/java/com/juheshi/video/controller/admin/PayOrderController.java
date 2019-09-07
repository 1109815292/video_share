package com.juheshi.video.controller.admin;

import com.juheshi.video.entity.PayOrder;
import com.juheshi.video.service.PayOrderService;
import com.juheshi.video.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/order")
public class PayOrderController {

    @Resource
    private PayOrderService payOrderService;


    //获取视频列表
    @RequestMapping(value = "/orderList", produces = "text/html;charset=UTF-8")
    public ModelAndView videoList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/order/order-list");
        return modelAndView;
    }


    @RequestMapping(value = "/getOrderData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getOrderData(@RequestParam(required = false, name = "state") Integer state,
                               @RequestParam(required = false, name = "search") String search,
                               @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                               @RequestParam(required = false, name = "limit", defaultValue = "10") int limit) {
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Page<PayOrder> p = payOrderService.pageFindAllPayOrder(state,search, page, limit);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }

    @RequestMapping(value = "/orderDetail", produces = "text/html;charset=UTF-8")
    public ModelAndView orderDetail(Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        PayOrder order = payOrderService.findById(id);
        modelAndView.addObject("order", order);
        modelAndView.setViewName("admin/order/order-detail");
        return modelAndView;
    }

}
