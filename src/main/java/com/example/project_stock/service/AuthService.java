package com.example.project_stock.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.project_stock.dto.AuthLogInDTO;
import com.example.project_stock.dto.AuthRegisterDTO;
import com.example.project_stock.model.User;

public interface AuthService extends UserDetailsService,AuthenticationSuccessHandler{

	User registerNewUserAccount(AuthRegisterDTO authRegister);
	
	String validateVerificationToken(String token);

	User checkLogIn(AuthLogInDTO authLogIn);
	
	boolean emailExists(String email);
	

}
