package com.harunichi.product.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.harunichi.common.util.FileUploadUtil;
import com.harunichi.common.util.LoginCheck;
import com.harunichi.product.service.ProductService;
import com.harunichi.product.vo.ProductVo;

@Controller("productController")
@RequestMapping("/product")
public class ProductControllerImpl implements ProductController {

    @Autowired
    private ProductService productService;
    
    // 로그인 공통화
    private boolean isNotLoggedIn(HttpServletRequest request, HttpServletResponse response) throws Exception {
 //       return !LoginCheck.loginCheck(request.getSession(), request, response);
    	  return false;
    }
    
    // 상품 목록
    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("/product/list");
    }

    // 상품 목록 페이징
    @RequestMapping(value = "/moreList", method = RequestMethod.GET)
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
            map.put("productStatus", product.getProductStatus());
            map.put("productCategory", product.getProductCategory());
            map.put("productCount", product.getProductCount());
            map.put("writerNick", product.getWriterNick());
            map.put("writerProfileImg", product.getWriterProfileImg());
            result.add(map);
        }
        return result;
    }

    // 상품 상세
    @Override
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView view(@RequestParam("productId") int productId,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        productService.incrementViewCount(productId);

        ProductVo product = productService.findById(productId);
        if (product == null) {
            return new ModelAndView("redirect:/product/list");
        }

        ModelAndView mav = new ModelAndView("/product/view");
        mav.addObject("product", product);
        // 작성자의 다른 상품은 Ajax로 /product/other 에서 불러옴
        return mav;
    }


    // 상품 등록 폼
    @Override
    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public ModelAndView writeForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (isNotLoggedIn(request, response)) return null;
        return new ModelAndView("/product/write");
    }

    // 상품 등록 처리
    @Override
    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public ModelAndView write(ProductVo product,
                              @RequestParam("uploadFile") MultipartFile uploadFile,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        if (isNotLoggedIn(request, response)) return null;
        String loginId = (String) request.getSession().getAttribute("loginId");
        product.setProductWriterId(loginId);

        if (!uploadFile.isEmpty()) {
            String uploadDir = request.getSession().getServletContext().getRealPath("/resources/images/product");
            String savedFile = FileUploadUtil.uploadFile(uploadFile, uploadDir);
            product.setProductImg("/resources/images/product/" + savedFile);
        }

        productService.insert(product);
        return new ModelAndView("redirect:/product/list");
    }

    // 상품 삭제
    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView delete(@RequestParam("productId") int productId,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        if (isNotLoggedIn(request, response)) return null;
        String loginId = (String) request.getSession().getAttribute("loginId");
        ProductVo product = productService.findById(productId);

        boolean isWriter = product != null && product.getProductWriterId().equals(loginId);
        boolean isAdmin = "admin".equalsIgnoreCase(loginId);

        if ((isWriter || isAdmin) && product != null) {
            String productImgPath = product.getProductImg();
            if (productImgPath != null && !productImgPath.trim().isEmpty()) {
                String uploadDir = request.getSession().getServletContext().getRealPath("/resources/images/product");
                String fileName = new File(productImgPath).getName();
                FileUploadUtil.deleteFile(uploadDir, fileName);
            }
            productService.delete(productId);
        }
        return new ModelAndView("redirect:/product/list");
    }

    // 상품 수정 폼
    @Override
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView editForm(@RequestParam("productId") int productId,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        if (isNotLoggedIn(request, response)) return null;
        ProductVo product = productService.findById(productId);
        ModelAndView mav = new ModelAndView("/product/edit");
        mav.addObject("product", product);
        return mav;
    }

    // 상품 수정
    @Override
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit(ProductVo product,
                             @RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             RedirectAttributes rttr) throws Exception {
        if (isNotLoggedIn(request, response)) return null;
        String loginId = (String) request.getSession().getAttribute("loginId");

        ProductVo original = productService.findById(product.getProductId());

        if (original == null) {
            rttr.addFlashAttribute("msg", "존재하지 않는 상품입니다.");
            return new ModelAndView("redirect:/product/list");
        }

        boolean isWriter = original.getProductWriterId().equals(loginId);
        boolean isAdmin = "admin".equalsIgnoreCase(loginId);

        if (!(isWriter || isAdmin)) {
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다.");
            return new ModelAndView("redirect:/product/view?productId=" + product.getProductId());
        }

        String uploadDir = request.getSession().getServletContext().getRealPath("/resources/images/product");
        String deleteImg = request.getParameter("deleteImg");
        boolean isDeleteRequested = (deleteImg != null);

        if (isDeleteRequested && original.getProductImg() != null) {
            String fileName = new File(original.getProductImg()).getName();
            FileUploadUtil.deleteFile(uploadDir, fileName);
            product.setProductImg(null);
        } else {
            product.setProductImg(original.getProductImg());
        }

        if (uploadFile != null && !uploadFile.isEmpty()) {
            if (original.getProductImg() != null) {
                String fileName = new File(original.getProductImg()).getName();
                FileUploadUtil.deleteFile(uploadDir, fileName);
            }
            String savedFile = FileUploadUtil.uploadFile(uploadFile, uploadDir);
            product.setProductImg("/resources/images/product/" + savedFile);
        }

        product.setProductWriterId(original.getProductWriterId());
        productService.update(product);

        rttr.addFlashAttribute("msg", "상품이 성공적으로 수정되었습니다.");
        return new ModelAndView("redirect:/product/view?productId=" + product.getProductId());
    }

    // 검색 + 필터 + Ajax 기반 목록
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> searchList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "1") int page) throws Exception {

        int pageSize = 8;
        int offset = (page - 1) * pageSize;

        // 문자열 상태를 Integer로 변환
        Integer statusVal = null;
        if (status != null && !status.isEmpty()) {
            try {
                statusVal = Integer.valueOf(status); // "0" 또는 "1"
            } catch (NumberFormatException e) {
                statusVal = null;
            }
        }

        List<ProductVo> products = productService.searchFiltered(
                keyword, category, statusVal, offset, pageSize
        );

        List<Map<String, Object>> result = new ArrayList<>();
        for (ProductVo product : products) {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", product.getProductId());
            map.put("productTitle", product.getProductTitle());
            map.put("productPrice", product.getProductPrice());
            map.put("productImg", product.getProductImg());
            map.put("productStatus", product.getProductStatus());
            map.put("productCount", product.getProductCount());
            map.put("writerNick", product.getWriterNick());
            map.put("writerProfileImg", product.getWriterProfileImg());
            result.add(map);
        }
        return result;
    }

    // 작성자의 다른 상품 (현재 상품 제외)
    @RequestMapping("/other")
    @ResponseBody
    public List<Map<String, Object>> findOtherProducts( 
            @RequestParam String writerId,
            @RequestParam int productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size) throws Exception {

        int offset = (page - 1) * size;
        
        List<ProductVo> products = productService.findOtherProducts(writerId, productId, offset, size);

        // ProductVo 리스트를 Map<String, Object> 리스트로 변환하여 반환
        List<Map<String, Object>> result = new ArrayList<>();
        for (ProductVo product : products) {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", product.getProductId());
            map.put("productTitle", product.getProductTitle());
            map.put("productPrice", product.getProductPrice());
            map.put("productImg", product.getProductImg());
            map.put("writerNick", product.getWriterNick());
            map.put("writerProfileImg", product.getWriterProfileImg());

            result.add(map);
        }
        return result;
    }
}