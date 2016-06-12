package com.tec.accio.app;


import com.tec.accio.app.service.QueryService;
import com.tec.accio.app.service.impl.QueryServiceImpl;
import org.apache.lucene.queryParser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @RequestMapping(value = "/queryAccio", method = RequestMethod.GET)
    public @ResponseBody ArrayList<String> queryAccio(String myQuery) throws IOException {
        QueryService a = new QueryServiceImpl();
        ArrayList<String> res = a.AccioIndexerService(myQuery);
        System.out.println("Query Accio!");
        return res;
    }


    @RequestMapping(value = "/queryLucene", method = RequestMethod.GET)
    public @ResponseBody ArrayList<String> queryLucene(String myQuery) throws IOException, ParseException, ClassNotFoundException {
        QueryService a = new QueryServiceImpl();
        ArrayList<String> res = a.LuceneIndexerService(myQuery);
        System.out.println("Query Lucene!");
        return res;
    }

}