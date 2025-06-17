package com.harunichi.payment.controller;

import com.harunichi.common.util.LoginCheck;
import com.harunichi.payment.service.OrderService;
import com.harunichi.payment.vo.OrderVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/list")
    public ModelAndView orderList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 체크
        if (!LoginCheck.loginCheck(request.getSession(), request, response)) {
            return null; // 리턴 null 시, LoginCheck 내부에서 리다이렉트 스크립트 처리됨
        }

        String loginId = (String) request.getSession().getAttribute("loginId");
        List<OrderVo> orderList = orderService.findByBuyerId(loginId);

        ModelAndView mav = new ModelAndView("/order/orderList");
        mav.addObject("orderList", orderList);
        return mav;
    }
}
