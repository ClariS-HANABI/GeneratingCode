package com.claris.generatingcode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 页面控制器
 */
@Controller
@RequestMapping
public class ViewController {

    /**
     * 去代码生成器页面
     */
    @RequestMapping("/view")
    public ModelAndView goProductCode() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productCode");
        return mv;
    }

}
