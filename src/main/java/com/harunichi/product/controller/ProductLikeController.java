package com.harunichi.product.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harunichi.common.util.LoginCheck;
import com.harunichi.product.service.ProductLikeService;
import com.harunichi.product.vo.ProductLikeVo;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/like")
public class ProductLikeController {

    @Autowired
    private ProductLikeService productLikeService;

    // 로그인 체크
    private boolean isNotLoggedIn(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return !LoginCheck.loginCheck(request.getSession(), request, response);
    }

    // 좋아요 토글 (누르면 추가, 다시 누르면 취소)
    @RequestMapping(value = "/toggle", method = RequestMethod.POST)
    public Map<String, Object> toggleLike(@RequestBody ProductLikeVo likeVo,
                                          HttpSession session,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        if (isNotLoggedIn(request, response)) return null;

        String userId = (String) session.getAttribute("loginId");
        likeVo.setLikeUserId(userId);

        boolean liked = productLikeService.toggleLike(likeVo);
        int likeCount = productLikeService.getLikeCount(likeVo.getProductId());

        Map<String, Object> result = new HashMap<>();
        result.put("liked", liked);
        result.put("likeCount", likeCount);
        return result;
    }

    // 좋아요 개수 조회
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Map<String, Object> getLikeCount(@RequestParam int productId) throws Exception {
        int count = productLikeService.getLikeCount(productId);

        Map<String, Object> result = new HashMap<>();
        result.put("likeCount", count);
        return result;
    }

    // 현재 사용자가 좋아요 눌렀는지 확인
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public Map<String, Object> isLiked(@RequestParam int productId,
                                       HttpSession session,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        if (isNotLoggedIn(request, response)) return null;

        String userId = (String) session.getAttribute("loginId");

        Map<String, Object> result = new HashMap<>();
        boolean liked = productLikeService.isLiked(productId, userId);
        result.put("liked", liked);
        return result;
    }
}
