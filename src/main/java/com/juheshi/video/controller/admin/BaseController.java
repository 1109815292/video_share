package com.juheshi.video.controller.admin;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.SysParam;
import com.juheshi.video.entity.SystemUser;
import com.juheshi.video.entity.VarParam;
import com.juheshi.video.service.SysParamService;
import com.juheshi.video.service.VarParamService;
import com.juheshi.video.util.ConfigUtil;
import com.juheshi.video.util.HttpRequest;
import com.juheshi.video.util.Page;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础数据
 */
@Controller
@RequestMapping("/admin/base")
public class BaseController {

    @Resource
    private SysParamService sysParamService;

    @Resource
    private VarParamService varParamService;

    @RequestMapping(value = "/sysParamList", produces = "text/html;charset=UTF-8")
    public ModelAndView sysParamList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/base/base-sys-param-list");
        return modelAndView;
    }

    @RequestMapping(value = "/getSysParamData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getSysParamData(@RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                    @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                                    HttpSession session) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (userType.equals(SystemUser.TYPE_ADMIN)) {
            Page<SysParam> p = sysParamService.pageFindAll(page, limit);
            resultMap.put("code", 0);
            resultMap.put("msg", "success");
            resultMap.put("count", p.getTotal());
            resultMap.put("data", p.getList());
        } else {
            resultMap.put("code", 1);
            resultMap.put("msg", "没有权限");
            resultMap.put("count", 0);
            resultMap.put("data",null);
        }
        return resultMap;
    }

    @RequestMapping(value = "/varParamList", produces = "text/html;charset=UTF-8")
    public ModelAndView varParamList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/base/base-var-param-list");
        return modelAndView;
    }

    @RequestMapping(value = "/getVarParamData", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getVarParamData(@RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                  @RequestParam(required = false, name = "limit", defaultValue = "10") int limit,
                                  HttpSession session) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer userType = (Integer) session.getAttribute(Constants.SESSION_ADMIN_USER_TYPE);
        if (userType.equals(SystemUser.TYPE_ADMIN)) {
            Page<VarParam> p = varParamService.pageFindAll(page, limit);
            resultMap.put("code", 0);
            resultMap.put("msg", "success");
            resultMap.put("count", p.getTotal());
            resultMap.put("data", p.getList());
        } else {
            resultMap.put("code", 1);
            resultMap.put("msg", "没有权限");
            resultMap.put("count", 0);
            resultMap.put("data",null);
        }
        return resultMap;
    }

    @RequestMapping(value = "/reFetch", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object reFetch(@RequestParam("id")Integer id){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        VarParam varParam = varParamService.findById(id);
        if(varParam.getVarName().equals(Constants.VAR_PARAM_WX_ACCESS_TOKEN)) {
            try {
                String param = "grant_type=client_credential&appid=" + ConfigUtil.APPID + "&secret=" + ConfigUtil.SECRET;
                String resultStr = HttpRequest.sendGet(ConfigUtil.WX_PUBLIC_ACCESS_TOKEN_URL, param);
                ObjectMapper mapper = new ObjectMapper();
                HashMap map = mapper.readValue(resultStr, HashMap.class);
                String accessToken = map.get("access_token").toString();
                Integer expiresIn = Integer.parseInt(map.get("expires_in").toString());
                varParam.setVarValue(accessToken);
                varParam.setVarExpiresIn(expiresIn);
                varParam.setRemark("手动刷新");
                varParamService.modify(varParam);

                resultMap.put("state", 1);
                resultMap.put("msg", "ok");
                return resultMap;
            } catch (IOException e) {
                e.printStackTrace();
                resultMap.put("state", 0);
                resultMap.put("msg", "获取失败");
                resultMap.put("detail", e.getMessage());
                return resultMap;
            }
        } else {
            resultMap.put("state", 0);
            resultMap.put("msg", "该变量无法重新获取");
            return resultMap;
        }

    }
}
