package com.tec.accio.app;


import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.ModelAndView;



import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {

    @RequestMapping("/")
    public String home(ModelAndView modelAndView) {
        System.out.println("ok");
        // Spring uses InternalResourceViewResolver and return back index.jsp
        return "index";

    }




}