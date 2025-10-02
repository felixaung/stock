package com.example.project_stock.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_stock.dto.AuthLogInDTO;
import com.example.project_stock.dto.AuthRegisterDTO;
import com.example.project_stock.event.OnRegistrationCompleteEvent;
import com.example.project_stock.model.User;
import com.example.project_stock.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/")
public class AuthController {
	
	private final UserService userService;
	private ApplicationEventPublisher eventPublisher;
	
	public AuthController(UserService userService, ApplicationEventPublisher eventPublisher) {
		this.userService = userService;
		this.eventPublisher = eventPublisher;
	}

	@GetMapping("/log-in")
	public String getLogInPage(Model model) {
		model.addAttribute("authLogin", new AuthLogInDTO());
		return "auth/login";
	}
	
	@PostMapping("/log-in")
	public String logIn(
	        @Valid @ModelAttribute("authLogin") AuthLogInDTO authLogIn,
	        BindingResult result,
	        HttpSession session,
	        Model model) {
	    
		if(result.hasErrors()) {
			return "auth/login";
		}	

	    User user;
	    try {
	        user = userService.checkLogIn(authLogIn);
	    } catch (UsernameNotFoundException ex) {
	        model.addAttribute("loginError", "Invalid email or password.");
	        return "auth/login";
	    }

	    if (user == null || user.getRole() == null) {
            model.addAttribute("loginError", "Invalid email or password.");
            return "auth/login";
	    }

	    session.setAttribute("loggedUser", user);

	    String roleName = user.getRole().getName().toLowerCase();
	    return switch (roleName) {
	        case "admin" -> "/admin/index";
	        case "user" -> "/user/index";
	        default -> "redirect:/";
	    };
	}
	
	@GetMapping("/register")
	public String getRegisterPage(Model model) {
        model.addAttribute("authRegister", new AuthRegisterDTO());
        return "auth/registration";
	}
	
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("authRegister") AuthRegisterDTO registerDTO,
			Model model,
			BindingResult result) {
		
		if(result.hasErrors()) {
			return "auth/registration";
		}
	    if (userService.emailExists(registerDTO.getEmail())) {
	        result.rejectValue("email", "error.authRegister", "An account with this email already exists.");
	        return "auth/registration";
	    }
        try {
        	User registered = userService.registerNewUserAccount(registerDTO);
        	model.addAttribute("success", "Registration successful! Please verify your email.");
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered));
            return "redirect:/log-in";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/registration";
        }
	}
	
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        String result = userService.validateVerificationToken(token);
        if (result.equals("valid")) {
            model.addAttribute("message", "Your account has been verified successfully.");
            return "auth/verified";
        } else {
            model.addAttribute("message", "Invalid verification token.");
            return "auth/verify-email";
        }
    }
}
