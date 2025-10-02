package com.example.project_stock.serviceImplementation;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project_stock.dto.AuthLogInDTO;
import com.example.project_stock.dto.AuthRegisterDTO;
import com.example.project_stock.model.Role;
import com.example.project_stock.model.User;
import com.example.project_stock.repository.RoleRepository;
import com.example.project_stock.repository.UserRepository;
import com.example.project_stock.service.UserService;


@Service
public class UserServiceImplementation implements UserDetailsService, UserService{
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepositroy;
	private final EmailService emailService;
	
	public UserServiceImplementation(UserRepository userRepository, EmailService emailService,
			RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.roleRepositroy = roleRepository;
	}
	
	@Override
	public User checkLogIn(AuthLogInDTO authLogIn) {
	    return userRepository.findByEmail(authLogIn.getEmail());
	    
	}
	

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + email);
        }
        return user; 
    }

	@Override
	public User registerNewUserAccount(AuthRegisterDTO authRegister) {
		
	        User user = new User();
	        user.setEmail(authRegister.getEmail());
	        user.setPassword(new BCryptPasswordEncoder().encode(authRegister.getPassword()));
	        user.setEnabled(false);

	        Role defaultRole = roleRepositroy.findByName("user")
	                .orElseThrow(() -> new RuntimeException("Default role not found"));
	        user.setRole(defaultRole);

	        String token = UUID.randomUUID().toString();
	        user.setVerificationToken(token);
	        userRepository.save(user);

	        String confirmationUrl = "http://localhost:8080/verify-email?token=" + token;
	        emailService.sendEmail(
	                user.getEmail(),
	                "Email Verification",
	                "Click the link to verify your email: " + confirmationUrl
	        );

	        return user;

	 

	}
	
    public String validateVerificationToken(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);
        if (user == null) {
            return "invalid";
        }

        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

	@Override
	public boolean emailExists(String email) {
		
		return userRepository.findByEmail(email) != null;
	}



}
