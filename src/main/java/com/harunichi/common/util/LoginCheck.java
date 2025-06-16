package com.harunichi.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginCheck {

    public static boolean loginCheck(HttpSession session, HttpServletRequest request, 
    							  HttpServletResponse response) throws IOException {
        System.out.println("loginCheck------------------------------------");
       
        try {
    		request.setCharacterEncoding("UTF-8");
    		response.setContentType("text/html;charset=utf-8");
    		String contextPath = request.getContextPath();
    		PrintWriter out = response.getWriter();
    		
        	String userId = (String)session.getAttribute("id");		
        	System.out.println("userId : " + userId);
        	
        	if (userId == null || userId.equals("")) {
        	    out.println("<script>");
        	    out.println("alert('로그인 후 이용 가능합니다.');");
        	    out.println("location.href='" + contextPath + "/member/loginpage.do';");
        	    out.println("</script>");
        	    out.flush();
        	    out.close();
        	    return false;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

        return true;
    }	
	
}
