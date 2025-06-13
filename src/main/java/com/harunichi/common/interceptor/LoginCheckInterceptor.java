package com.harunichi.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/*
 로그인 상태를 확인하는 인터셉터 클래스.
 로그인하지 않은 사용자가 특정 URL에 접근할 경우, 로그인 페이지로 리다이렉트시킴.
*/
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

    /*
     컨트롤러 실행 전에 호출되는 메서드.
     세션에 로그인 정보(loginId)가 없으면 로그인 페이지로 이동시킴.
     @param request  클라이언트의 요청 객체
     @param response 서버의 응답 객체
     @param handler  컨트롤러 객체
     @return true: 컨트롤러 실행 계속 / false: 중단하고 리다이렉트
     @throws Exception 예외 발생 시 처리
    */
    @Override
    public boolean preHandle(HttpServletRequest request, 
                             HttpServletResponse response, 
                             Object handler) throws Exception {

        // 세션 객체 가져오기
        HttpSession session = request.getSession();

        // 세션에서 로그인 ID 확인
        String loginId = (String) session.getAttribute("loginId");

        // 로그인되어 있지 않으면 로그인 페이지로 이동
        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login");
            return false; // 요청 중단
        }

        // 로그인된 상태이면 요청 계속 진행
        return true;
    }
}
