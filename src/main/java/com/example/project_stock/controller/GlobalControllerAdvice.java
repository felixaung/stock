package com.example.project_stock.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.project_stock.model.User;
import com.example.project_stock.repository.UserRepository;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserRepository userRepository;

    public GlobalControllerAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("currentUser")
    public User addUserDetailsToModel(Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        if (principal != null) {
            String email = principal.getUsername();
            return userRepository.findByEmail(email);
        }
        return null;
    }
    
}
