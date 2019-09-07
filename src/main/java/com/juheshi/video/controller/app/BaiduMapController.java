package com.juheshi.video.controller.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/app/map")
public class BaiduMapController {

    @RequestMapping(value = "/showMap")
    public ModelAndView showMap(Double longitude, Double latitude){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("longitude", longitude);
        modelAndView.addObject("latitude", latitude);
        modelAndView.setViewName("/app/store/showMap");
        return modelAndView;
    }
}
