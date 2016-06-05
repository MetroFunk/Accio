package com.tec.accio.app;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
public class Application  {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);

    }
}