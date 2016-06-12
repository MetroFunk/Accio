package com.tec.accio.app;


import com.tec.accio.app.service.QueryService;
import com.tec.accio.app.service.impl.QueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.ModelAndView;



import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class BaseController {



    @RequestMapping("/")
    public String home(ModelAndView modelAndView) {
        // Spring uses InternalResourceViewResolver and return back index.jsp
        return "index";

    }

    @RequestMapping("/query")
    public ArrayList<String> query(String myQuery) throws IOException {
        QueryService a = new QueryServiceImpl();
        ArrayList<String> res = a.AccioIndexerService(myQuery);
        System.out.println(res);
        return res;
    }

}