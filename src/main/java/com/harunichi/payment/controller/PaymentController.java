package com.harunichi.payment.controller;

import com.harunichi.common.util.LoginCheck;
import com.harunichi.payment.service.IamportService;
import com.harunichi.payment.service.OrderService;
import com.harunichi.payment.vo.OrderVo;
import com.harunichi.product.service.ProductService;
import com.harunichi.product.vo.ProductVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	private ProductService productService;

	@Autowired
	private IamportService iamportService;

	@Autowired
	private OrderService orderService;
	
    // 공통 로그인 확인 메서드
    private boolean isNotLoggedIn(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return !LoginCheck.loginCheck(request.getSession(), request, response);
    }

    // 결제 화면 진입 시 로그인 확인
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView paymentForm(@RequestParam("productId") int productId,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        if (isNotLoggedIn(request, response)) return null;

        ProductVo product = productService.findById(productId);
        ModelAndView mav = new ModelAndView("/payment/form");
        mav.addObject("product", product);
        return mav;
    }

    // 결제 완료 후 포트원 검증 및 주문 저장
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> verifyAndSave(@RequestBody Map<String, String> payload,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
        if (isNotLoggedIn(request, response)) return null;

        Map<String, Object> result = new HashMap<>();

        String impUid = payload.get("imp_uid");
        String merchantUid = payload.get("merchant_uid");
        String productName = payload.get("product_name");
        int amount = Integer.parseInt(payload.get("amount"));
        String buyerId = (String) request.getSession().getAttribute("loginId");
        String buyerName = (String) request.getSession().getAttribute("loginName");

        try {
            // 포트원 액세스 토큰 발급
            String accessToken = iamportService.getAccessToken();

            // 결제 상태 확인
            Map<String, Object> paymentData = iamportService.getPaymentData(impUid, accessToken);
            String status = (String) paymentData.get("status");

            if ("paid".equals(status)) {
                // 주문 정보 저장
                OrderVo order = new OrderVo();
                order.setImpUid(impUid);
                order.setMerchantUid(merchantUid);
                order.setProductName(productName);
                order.setAmount(amount);
                order.setBuyerId(buyerId);
                order.setBuyerName(buyerName);
                order.setStatus("결제완료");

                orderService.insertOrder(order);

                result.put("success", true);
                result.put("message", "주문이 성공적으로 저장되었습니다.");
            } else {
                result.put("success", false);
                result.put("message", "결제 상태가 유효하지 않습니다: " + status);
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "예외 발생: " + e.getMessage());
        }

        return result;
    }

	// 결제 성공 시 이동할 페이지
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public ModelAndView success() {
		return new ModelAndView("/payment/success");
	}

	// 결제 실패 시 이동할 페이지
	@RequestMapping(value = "/fail", method = RequestMethod.GET)
	public ModelAndView fail() {
		return new ModelAndView("/payment/fail");
	}

    // 주문 내역 확인 (로그인 필요)
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ModelAndView orderList(HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        if (isNotLoggedIn(request, response)) return null;

        String loginId = (String) request.getSession().getAttribute("loginId");
        ModelAndView mav = new ModelAndView("/payment/orderList");
        mav.addObject("orderList", orderService.findByBuyerId(loginId));
        return mav;
    }
}
