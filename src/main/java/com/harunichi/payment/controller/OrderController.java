package com.harunichi.payment.controller;

import com.harunichi.payment.service.OrderService;
import com.harunichi.payment.vo.OrderVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/list")
    public ModelAndView orderList(HttpServletRequest request) throws Exception {
        String loginId = (String) request.getSession().getAttribute("loginId");

        ModelAndView mav = new ModelAndView("/order/orderList");

        if (loginId != null && !loginId.isEmpty()) {
            List<OrderVo> orderList = orderService.findByBuyerId(loginId);
            mav.addObject("orderList", orderList);
        } else {
            mav.setViewName("redirect:/member/login");
        }

        return mav;
    }
}
