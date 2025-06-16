package com.harunichi.payment.controller;

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

	// 결제 하기 화면
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView paymentForm(@RequestParam("productId") int productId) throws Exception {
		ProductVo product = productService.findById(productId);
		ModelAndView mav = new ModelAndView("/payment/form");
		mav.addObject("product", product);
		return mav;
	}

	// 결제 검증 및 주문 저장 (포트원 공통 처리: 이니시스, 엑심베이)
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> verifyAndSave(@RequestBody Map<String, String> payload, HttpServletRequest request) {

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

			// 결제정보 조회
			Map<String, Object> paymentData = iamportService.getPaymentData(impUid, accessToken);
			String status = (String) paymentData.get("status");

			if ("paid".equals(status)) {
				// 주문 저장
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

	// 주문 내역 페이지
	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public ModelAndView orderList(HttpServletRequest request) throws Exception {
		String loginId = (String) request.getSession().getAttribute("loginId");
		ModelAndView mav = new ModelAndView("/payment/orderList");
		mav.addObject("orderList", orderService.findByBuyerId(loginId));
		return mav;
	}
}
