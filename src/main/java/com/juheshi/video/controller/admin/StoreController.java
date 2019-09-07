package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.AdvStore;
import com.juheshi.video.entity.AppUser;
import com.juheshi.video.entity.Industry;
import com.juheshi.video.entity.SystemUser;
import com.juheshi.video.service.AdvStoreService;
import com.juheshi.video.service.IndustryService;
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
import java.util.List;
import java.util.Map;

@Controller("adminStoreController")
@RequestMapping("/admin/store")
public class StoreController {

    @Resource
    private AdvStoreService advStoreService;

    @Resource
    private IndustryService industryService;

    //店铺列表页面
    @RequestMapping(value = "/storeList", produces = "text/html;charset=UTF-8")
    public ModelAndView merchantList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/store/store-list");
        List<Industry> list = industryService.findAllIndustry();
        modelAndView.addObject("industries", list);
        return modelAndView;
    }



    //查询店铺列表数据接口
    @RequestMapping(value = "/getStoreData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getMerchantData(@RequestParam(required = false, name = "industryId") Integer industryId,
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
        Page<AdvStore> p = advStoreService.pageFindAdvStore(bindCopyNo, search, industryId, page, limit);

        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("count", p.getTotal());
        resultMap.put("data", p.getList());
        return resultMap;
    }

    @RequestMapping(value = "/changeIndustry", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object changeIndustry(Integer id, Integer industryId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if(industryId == null){
            resultMap.put("state", 0);
            resultMap.put("msg", "行业id不能为空");
            return resultMap;
        }
        advStoreService.modifyAdvStoreIndustry(id, industryId);

        resultMap.put("state", 1);
        resultMap.put("msg", "success");
        return resultMap;
    }
}
