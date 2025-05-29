package com.harunichi.product.controller;


import java.io.File;
import java.util.UUID;

import com.harunichi.product.service.ProductService;
import com.harunichi.product.vo.ProductVo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("productController")
@RequestMapping("/product")
public class ProductControllerImpl implements ProductController {

    @Autowired
    private ProductService productService;

    // 상품 목록
    @Override
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("/product/list"); // 데이터 없이 JSP만 로드
    }
    
    // 상품 목록 페이징
    @RequestMapping(value = "/moreList.do", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> moreList(@RequestParam("page") int page) throws Exception {
        int pageSize = 8;
        int offset = (page - 1) * pageSize;
        List<ProductVo> products = productService.findPaged(offset, pageSize);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (ProductVo product : products) {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", product.getProductId());
            map.put("productTitle", product.getProductTitle());
            map.put("productPrice", product.getProductPrice());
            map.put("productImg", product.getProductImg());
            map.put("productCount", product.getProductCount());
            map.put("nick", product.getWriterNick()); 
            map.put("profileImg", product.getWriterProfileImg());

            result.add(map);
        }
        return result;
    }

    // 상품 상세
    @Override
    @RequestMapping(value = "/view.do", method = RequestMethod.GET)
    public ModelAndView view(int productId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        productService.incrementViewCount(productId);
        ProductVo product = productService.findById(productId);
        ModelAndView mav = new ModelAndView("/product/view");
        mav.addObject("product", product);
        return mav;
    }

    // 상품 등록 폼
    @Override
    @RequestMapping(value = "/write.do", method = RequestMethod.GET)
    public ModelAndView writeForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("/product/write");
    }

    // 상품 등록 처리
    @RequestMapping(value = "/write.do", method = RequestMethod.POST)
    @Override
    public ModelAndView write(ProductVo product,
                               @RequestParam("uploadFile") MultipartFile uploadFile,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

        String loginId = (String) request.getSession().getAttribute("loginId");
        product.setProductWriterId(loginId); // 작성자 저장

        // 이미지 업로드 처리
        if (!uploadFile.isEmpty()) {
        	String uploadDir = request.getSession().getServletContext().getRealPath("/resources/upload/product");
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID().toString() + "_" + uploadFile.getOriginalFilename();
            File dest = new File(uploadDir, fileName);
            uploadFile.transferTo(dest);

            product.setProductImg("/resources/upload/product/" + fileName);
        }

        productService.insert(product);
        return new ModelAndView("redirect:/product/list.do");
    }


    // 상품 삭제 처리
    @Override
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    public ModelAndView delete(@RequestParam("productId") int productId,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        
        String loginId = (String) request.getSession().getAttribute("loginId");

        ProductVo product = productService.findById(productId);

        // 삭제 권한: 작성자 본인 또는 관리자("admin" 로그인시)
        boolean isWriter = product != null && product.getProductWriterId().equals(loginId);
        boolean isAdmin = "admin".equalsIgnoreCase(loginId); // 관리자 ID

        if (isWriter || isAdmin) {
            productService.delete(productId);
        }

        return new ModelAndView("redirect:/product/list.do");
    }

    // 상품 수정 폼 요청
    @Override
    @RequestMapping(value = "/edit.do", method = RequestMethod.GET)
    public ModelAndView editForm(@RequestParam("productId") int productId,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProductVo product = productService.findById(productId);
        ModelAndView mav = new ModelAndView("/product/edit");
        mav.addObject("product", product);
        return mav;
    }

    // 상품 수정
    @Override
    @RequestMapping(value = "/edit.do", method = RequestMethod.POST)
    public ModelAndView edit(ProductVo product,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        String loginId = (String) request.getSession().getAttribute("loginId");

        ProductVo original = productService.findById(product.getProductId());

        boolean isWriter = original != null && original.getProductWriterId().equals(loginId);
        boolean isAdmin = "admin".equalsIgnoreCase(loginId);

        if (isWriter || isAdmin) {
            productService.update(product); // 수정 처리
        }

        return new ModelAndView("redirect:/product/view.do?productId=" + product.getProductId());
    }



}
