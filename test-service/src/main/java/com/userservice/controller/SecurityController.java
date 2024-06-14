package com.userservice.controller;

import com.userservice.model.user.AuthenticationRequest;
import com.userservice.model.user.Role;
import com.userservice.model.user.User;
import com.userservice.service.UserService;
import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/test/security")
public class SecurityController {

    private final UserService userService;

    @GetMapping("/register")
    @Timed("registerUser")
    public String registerHowStudent(Model model) {
        model.addAttribute("user", new User());
        return "registerHowStudent";
    }

    @PostMapping("/register")
    public String sendUserInfo(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "redirect:/v1/test/security/registerHowStudent";
        }
        if(user.getIsTeacher()){
            user.setRole(Role.TEACHER);
        }else {
            user.setRole(Role.STUDENT);
        }
        userService.registration(user,session);
        return "redirect:/v1/test/security/login";
    }

    @GetMapping("/login")
    @Timed("userLogin")
    public String login(Model model) {
        model.addAttribute("authentication", new AuthenticationRequest());
        return "login";
    }

    @GetMapping("/verification")
    @Timed("verificationUser")
    public String verification(@Valid @ModelAttribute("authentication") AuthenticationRequest authenticationRequest, BindingResult result
            , HttpServletResponse response,HttpSession session) {
        String token =(String) session.getAttribute("token");
        if (result.hasErrors()) {
            return "redirect:/v1/test/security/login";
        }
        String email = userService.verify(authenticationRequest,session);
        if (email != null) {
            session.setAttribute("email",email);
            return "redirect:/v1/test/home";
        }
        return "redirect:/v1/test/security/login";
    }



}
