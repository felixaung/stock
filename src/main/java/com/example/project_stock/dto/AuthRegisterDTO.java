package com.example.project_stock.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterDTO {
	
	@NotEmpty(message = "Name can be empty")
	String name;
	
	@Email(message = "Invalid email address. Please enter a proper email")
	@NotEmpty(message = "Email can't be empty")
	String email;

	@NotEmpty(message = "Password can't be empty")
	@Size(max = 20, min = 8, message="Password should be greater than 8 and less than 20")
	String password;
}
