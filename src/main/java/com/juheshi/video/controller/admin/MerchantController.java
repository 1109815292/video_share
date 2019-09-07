package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.SystemUser;
import com.juheshi.video.service.AppUserService;
import com.juheshi.video.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/admin/merchant")
public class MerchantController {

    @Resource
    private AppUserService appUserService;

    //获取用户列表
    @RequestMapping(value = "/merchantList", produces = "text/html;charset=UTF-8")
    public ModelAndView merchantList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/merchant/merchant-list");
        return modelAndView;
    }


    //查询商家用户列表接口
    @RequestMapping(value = "/getMerchantData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getMerchantData(@RequestParam(required = false, name = "vipFlag") String vipFlag,
                                  @RequestParam(required = false, name = "search") String search,
                                  @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                  @RequestParam(required = false, name = "limit", defaultValue = "10") int limit, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (search != null && !"".equals(search.trim())) {
            try {
                search = new String(search.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Integer userType = (Integer) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        String bindCopyNo = null;
        switch (userType) {
            case SystemUser.TYPE_ADMIN:
                break;
            case SystemUser.TYPE_STATION:
                bindCopyNo = (String) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER_APP_USER_COPY_NO);
                break;
            default:
                resultMap.put("code", 1);
                resultMap.put("msg", "没有权限");
                return resultMap;
        }
        Page<AppUser> p = appUserService.pageFindAllUserForStation(search, bindCopyNo, Constants.YES, AppUser.VIP_TYPE_OFFLINE, page, limit);

        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }

}
