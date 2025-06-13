package com.harunichi.product.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harunichi.product.service.ProductLikeService;
import com.harunichi.product.vo.ProductLikeVo;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/like")
public class ProductLikeController {

    @Autowired
    private ProductLikeService productLikeService;

    // 좋아요 토글 (누르면 추가, 다시 누르면 취소)
    @RequestMapping(value = "/toggle", method = RequestMethod.POST)
    public Map<String, Object> toggleLike(@RequestBody ProductLikeVo likeVo, HttpSession session) throws Exception {
        String userId = (String) session.getAttribute("loginId");

        // 안전 체크
        if (userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // 세션에서 로그인 ID 주입
        likeVo.setLikeUserId(userId);

        // VO 기반 서비스 호출
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
    public Map<String, Object> isLiked(@RequestParam int productId, HttpSession session) throws Exception {
        String userId = (String) session.getAttribute("loginId");

        Map<String, Object> result = new HashMap<>();
        if (userId == null) {
            result.put("liked", false);
            return result;
        }

        boolean liked = productLikeService.isLiked(productId, userId);
        result.put("liked", liked);
        return result;
    }
}
