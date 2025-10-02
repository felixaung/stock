package com.example.project_stock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
//    @GetMapping("/")
//    public String index(){
//        return "redirect:/home";
//    } 
//	 used in WebConfig.java instead here
	
    @GetMapping("/home")
    public String home(){
        return "home";
    }
    
    @GetMapping("/about")
    public String about() { return "about"; }

    @GetMapping("/contact")
    public String contact() { return "contact"; }

    @GetMapping("/help")
    public String help() { return "help"; }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/signup")
    public String signup() { return "signup"; }
}

