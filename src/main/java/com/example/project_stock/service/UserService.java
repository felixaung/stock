package com.example.project_stock.service;

import com.example.project_stock.dto.AuthLogInDTO;
import com.example.project_stock.dto.AuthRegisterDTO;
import com.example.project_stock.model.User;

public interface UserService{
	
	User registerNewUserAccount(AuthRegisterDTO authRegister);
	
	String validateVerificationToken(String token);

	User checkLogIn(AuthLogInDTO authLogIn);
	
	boolean emailExists(String email);
}
