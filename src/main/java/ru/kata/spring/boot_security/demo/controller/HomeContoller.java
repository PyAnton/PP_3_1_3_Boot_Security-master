package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeContoller {
    @GetMapping("/")
    public String getHome() {
        return "redirect:/user";
    }
    @GetMapping("/user")
    public String getUser() {
        return "user";
    }
    @GetMapping("/admin")
    public String getAdmin() {
        return "admin";
    }
    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/logout") // POST-запрос для фактического выхода
    public String customLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout"; // После выхода перенаправляет на страницу логина с сообщением

    }
}
