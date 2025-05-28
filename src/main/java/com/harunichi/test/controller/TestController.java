package com.harunichi.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String showTestPage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("TestController - showTestPage() 메소드 시작! Request for /test");

        logger.info("TestController - showTestPage() 메소드 종료. Returning view name: /test");

        return "/test";
    }
}
