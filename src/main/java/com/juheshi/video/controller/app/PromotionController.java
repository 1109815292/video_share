package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.*;
import com.juheshi.video.service.AdvOtherService;
import com.juheshi.video.service.AdvProductService;
import com.juheshi.video.service.AdvStoreService;
import com.juheshi.video.service.AdvWxService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/app")
@SessionAttributes(Constants.SESSION_APP_USER_ID)
public class PromotionController {

    @Resource
    private AdvProductService advProductService;

    @Resource
    private AdvStoreService advStoreService;

    @Resource
    private AdvWxService advWxService;

    @Resource
    private AdvOtherService advOtherService;

    //推广设置
    @RequestMapping(value = "/promotionSet", produces = "text/html;charset=UTF-8")
    public ModelAndView promotionSet() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("app/promotion/promotionSet");

        return modelAndView;
    }


    // ********************************** 商品推广 **********************************
    //添加或编辑商品推广
    @RequestMapping(value = "/advProductSet", produces = "text/html;charset=UTF-8")
    public ModelAndView advProductSet(@RequestParam(required = false, name = "id") Integer id,
                                      @RequestParam(required = false, name = "type") Integer type,
                                      @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        ModelAndView modelAndView = new ModelAndView();
        if (id == null && type == null) {
            modelAndView.setViewName("app/common/error/404_advProduct");
            return modelAndView;
        }
        if (id != null) {
            AdvProduct product = advProductService.findById(id);
            if (product != null && product.getUserId().equals(userId)) {
                modelAndView.addObject("product", product);
                type = product.getType();
            } else {
                modelAndView.setViewName("app/common/error/404_advProduct");
                return modelAndView;
            }
        }
        switch (type) {
            case AdvProduct.TYPE_NORMAL://普通商品
                modelAndView.setViewName("app/promotion/advProductSetForNormal");
                break;
            case AdvProduct.TYPE_JD://京东
                modelAndView.setViewName("app/promotion/advProductSetForJD");
                break;
            case AdvProduct.TYPE_WEI_DIAN://微店
                modelAndView.setViewName("app/promotion/advProductSetForWeiDian");
                break;
            case AdvProduct.TYPE_YOU_ZAN://有赞
                modelAndView.setViewName("app/promotion/advProductSetForYouZan");
                break;
            case AdvProduct.TYPE_TAO_BAO://淘宝
                modelAndView.setViewName("app/promotion/advProductSetForTaoBao");
                break;
            default:
                modelAndView.setViewName("app/common/error/404_advProduct");
        }
        return modelAndView;
    }

    //保存商品推广
    @RequestMapping(value = "/saveProduct", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object saveProduct(AdvProduct product, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (product.getProductName() == null || "".equals(product.getProductName())) {
            result.put("code", 0);
            result.put("msg", "商品名称不能为空");
            return result;
        }
        if (product.getId() == null) { //新增
            product.setUserId(userId);
            if (product.getSort() == null)
                product.setSort(0);
            product.setViewCount(0);
            advProductService.createAdvProduct(product);
        } else { //修改
            advProductService.modifyAdvProduct(product);
        }

        result.put("code", 200);
        result.put("msg", "");
        return result;
    }

    //管理商品推广
    @RequestMapping(value = "/advProductManage", produces = "text/html;charset=UTF-8")
    public ModelAndView advProductManage(@RequestParam(name = "type") Integer type, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        ModelAndView modelAndView = new ModelAndView();
        List<AdvProduct> list = advProductService.findByUserId(userId, type);
        modelAndView.addObject("products", list);
        modelAndView.setViewName("app/promotion/advProductManage");
        return modelAndView;
    }


    //删除商品推广
    @RequestMapping(value = "/advProductDelete", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object advProductDelete(@RequestParam(name = "id") Integer id, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        AdvProduct product = advProductService.findById(id);
        if (product != null) {
            if (product.getUserId().equals(userId)) {
                advProductService.deleteById(id);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("message", "success");
        return resultMap;
    }


    // ********************************** 实体店推广 **********************************
    //添加或编辑实体店推广
    @RequestMapping(value = "/advStoreSet", produces = "text/html;charset=UTF-8")
    public ModelAndView advStoreSet(@RequestParam(required = false, name = "id") Integer id,@ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        ModelAndView modelAndView = new ModelAndView();
        if (id != null) {
            AdvStore store = advStoreService.findById(id);
            if (store != null && store.getUserId().equals(userId)) {
                modelAndView.addObject("store", store);
            } else {
                modelAndView.setViewName("app/common/error/404_advStore");
                return modelAndView;
            }
        }
        modelAndView.setViewName("app/promotion/advStoreSet");
        return modelAndView;
    }

    //保存实体店推广
    @RequestMapping(value = "/saveStore", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object saveStore(AdvStore store, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (store.getStoreName() == null || "".equals(store.getStoreName())) {
            result.put("code", 0);
            result.put("msg", "实体店名称不能为空");
            return result;
        }
        if (store.getId() == null) { //新增
            store.setUserId(userId);
            store.setViewCount(0);
            advStoreService.createAdvStore(store);
        } else { //修改
            advStoreService.modifyAdvStore(store);
        }

        result.put("code", 200);
        result.put("msg", "");
        return result;
    }

    //管理实体店推广
    @RequestMapping(value = "/advStoreManage", produces = "text/html;charset=UTF-8")
    public ModelAndView advStoreManage(@ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        ModelAndView modelAndView = new ModelAndView();
        List<AdvStore> list = advStoreService.findByUserId(userId);
        modelAndView.addObject("stores", list);
        modelAndView.setViewName("app/promotion/advStoreManage");
        return modelAndView;
    }


    //删除实体店推广
    @RequestMapping(value = "/advStoreDelete", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object advStoreDelete(@RequestParam(name = "id") Integer id, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        AdvStore store = advStoreService.findById(id);
        if (store != null) {
            if (store.getUserId().equals(userId)) {
                advStoreService.deleteById(id);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("message", "success");
        return resultMap;
    }


   /* // ********************************** 微信推广 **********************************
    //添加或编辑微信推广
    @RequestMapping(value = "/advWxSet", produces = "text/html;charset=UTF-8")
    public ModelAndView advWxSet(@RequestParam(required = false, name = "id") Integer id,
                                 @RequestParam(required = false, name = "type") Integer type,
                                 @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        ModelAndView modelAndView = new ModelAndView();
        if (id == null && type == null) {
            modelAndView.setViewName("app/common/error/404_advWx");
            return modelAndView;
        }
        if (id != null) {
            AdvWx wx = advWxService.findById(id);
            if (wx != null && wx.getUserId().equals(userId)) {
                modelAndView.addObject("wx", wx);
                type = wx.getType();
            } else {
                modelAndView.setViewName("app/common/error/404_advWx");
                return modelAndView;
            }

        }
        switch (type) {
            case AdvWx.TYPE_PUBLIC://公众号
                modelAndView.setViewName("app/promotion/advWxSetForPublic");
                break;
            case AdvWx.TYPE_APPLET://小程序
                modelAndView.setViewName("app/promotion/advWxSetForApplet");
                break;
            case AdvWx.TYPE_PERSON://个人微信
                modelAndView.setViewName("app/promotion/advWxSetForPerson");
                break;
            case AdvWx.TYPE_GROUP://微信群
                modelAndView.setViewName("app/promotion/advWxSetForGroup");
                break;
            default:
                modelAndView.setViewName("app/common/error/404_advWx");

        }
        return modelAndView;
    }

    //保存微信推广
    @RequestMapping(value = "/saveWx", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object saveWx(AdvWx wx, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (wx.getAdvName() == null || "".equals(wx.getAdvName())) {
            result.put("code", 0);
            result.put("msg", "名称不能为空");
            return result;
        }
        if (wx.getId() == null) { //新增
            wx.setUserId(userId);
            wx.setViewCount(0);
            advWxService.createAdvWx(wx);
        } else { //修改
            advWxService.modifyAdvWx(wx);
        }

        result.put("code", 200);
        result.put("msg", "");
        return result;
    }

    //管理微信推广
    @RequestMapping(value = "/advWxManage", produces = "text/html;charset=UTF-8")
    public ModelAndView advWxManage(@RequestParam(name = "type") Integer type, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        ModelAndView modelAndView = new ModelAndView();
        List<AdvWx> list = advWxService.findByUserId(userId, type);
        modelAndView.addObject("wxs", list);
        modelAndView.setViewName("app/promotion/advWxManage");
        return modelAndView;
    }


    //删除微信推广
    @RequestMapping(value = "/advWxDelete", produces = "text/html;charset=UTF-8")
    public String advWxDelete(@RequestParam(name = "id") Integer id, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        AdvWx wx = advWxService.findById(id);
        if (wx != null) {
            if (wx.getUserId().equals(userId)) {
                advWxService.deleteById(id);
                return "redirect:/app/advWxManage?type=" + wx.getType();
            }
        }
        return "redirect:/app/promotionSet";
    }*/

    // ********************************** 其他推广 **********************************
    //添加或编辑其他推广
    @RequestMapping(value = "/advOtherSet", produces = "text/html;charset=UTF-8")
    public ModelAndView advOtherSet(@RequestParam(required = false, name = "id") Integer id,
                                    @RequestParam(required = false, name = "type") Integer type,
                                    @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        ModelAndView modelAndView = new ModelAndView();
        if (id == null && type == null) {
            modelAndView.setViewName("app/common/error/404_advOther");
            return modelAndView;
        }
        if (id != null) {
            AdvOther other = advOtherService.findById(id);
            if (other != null && other.getUserId().equals(userId)) {
                modelAndView.addObject("other", other);
                type = other.getType();
            } else {
                modelAndView.setViewName("app/common/error/404_advOther");
                return modelAndView;
            }
        }
        switch (type) {
            case AdvOther.TYPE_PUBLIC://公众号
                modelAndView.setViewName("app/promotion/advOtherSetForWxPublic");
                break;
            case AdvOther.TYPE_APPLET://小程序
                modelAndView.setViewName("app/promotion/advOtherSetForWxApplet");
                break;
            case AdvOther.TYPE_PERSON://个人微信
                modelAndView.setViewName("app/promotion/advOtherSetForWxPerson");
                break;
            case AdvOther.TYPE_GROUP://微信群
                modelAndView.setViewName("app/promotion/advOtherSetForWxGroup");
                break;

            case AdvOther.TYPE_APP://APP
                modelAndView.setViewName("app/promotion/advOtherSetForApp");
                break;
            case AdvOther.TYPE_WEBSITE://官网
                modelAndView.setViewName("app/promotion/advOtherSetForWebsite");
                break;
            case AdvOther.TYPE_LINK://链接
                modelAndView.setViewName("app/promotion/advOtherSetForLink");
                break;
            case AdvOther.TYPE_QR_CODE://二维码
                modelAndView.setViewName("app/promotion/advOtherSetForQRCode");
                break;
        }
        return modelAndView;
    }

    //保存其他推广
    @RequestMapping(value = "/saveOther", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object saveOther(AdvOther other, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (other.getName() == null || "".equals(other.getName())) {
            result.put("code", 0);
            result.put("msg", "名称不能为空");
            return result;
        }
        if (other.getId() == null) { //新增
            other.setUserId(userId);
            other.setViewCount(0);
            advOtherService.createAdvOther(other);
        } else { //修改
            advOtherService.modifyAdvOther(other);
        }

        result.put("code", 200);
        result.put("msg", "");
        return result;
    }

    //管理其他推广
    @RequestMapping(value = "/advOtherManage", produces = "text/html;charset=UTF-8")
    public ModelAndView advOtherManage(@RequestParam(name = "type") Integer type, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        ModelAndView modelAndView = new ModelAndView();
        List<AdvOther> list = advOtherService.findByUserId(userId, type);
        modelAndView.addObject("others", list);
        modelAndView.setViewName("app/promotion/advOtherManage");
        return modelAndView;
    }


    //删除其他推广
    @RequestMapping(value = "/advOtherDelete", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object advOtherDelete(@RequestParam(name = "id") Integer id, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId) {
        AdvOther other = advOtherService.findById(id);
        if (other != null) {
            if (other.getUserId().equals(userId)) {
                advOtherService.deleteById(id);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("message", "success");
        return resultMap;
    }
}
