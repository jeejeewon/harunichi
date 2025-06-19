package com.harunichi.admin.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.harunichi.main.controller.MainController;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping({"", "/"})
    public String showAdminMain(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "/admin/main";  // tiles_admin.xml에 적어둔 name값 적기!
    }
    
    @RequestMapping({"/member"})
    public String showAdminMember(HttpServletRequest request, Model model) {
    	model.addAttribute("currentUri", request.getRequestURI());
    	return "/admin/member";  // tiles_admin.xml에 적어둔 name값 적기!
    }
}
