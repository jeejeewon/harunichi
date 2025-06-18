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
    public String adminMain(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "/admin/main";  // Tiles에 정의된 어드민 메인 body
    }
}
