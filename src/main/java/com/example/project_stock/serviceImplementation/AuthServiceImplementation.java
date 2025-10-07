package com.example.project_stock.serviceImplementation;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project_stock.dto.AuthLogInDTO;
import com.example.project_stock.dto.AuthRegisterDTO;
import com.example.project_stock.model.Role;
import com.example.project_stock.model.User;
import com.example.project_stock.repository.RoleRepository;
import com.example.project_stock.repository.UserRepository;
import com.example.project_stock.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AuthServiceImplementation implements AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepositroy;
	private final EmailService emailService;
	
	public AuthServiceImplementation(UserRepository userRepository, EmailService emailService,
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
	public User registerNewUserAccount(AuthRegisterDTO authRegister) {
	        User user = new User();
	        user.setName(authRegister.getName());
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
	
//  @Override
//  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//      User user = userRepository.findByEmail(email);
//      if (user == null || !user.isEnabled()) {
//          throw new UsernameNotFoundException("User not found or not verified");
//      }
//
//      // Role from DB is lowercase (e.g., "admin"), but must be "ROLE_admin"
//      String role = "ROLE_" + user.getRole().getName().toLowerCase();
//
//      return new org.springframework.security.core.userdetails.User(
//              user.getEmail(),
//              user.getPassword(),
//              List.of(new SimpleGrantedAuthority(role))
//      );
//  }s

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    User user = userRepository.findByEmail(email);
	    if (user == null) {
	        throw new UsernameNotFoundException("User not found with email: " + email);
	    }

	    return org.springframework.security.core.userdetails.User.builder()
	            .username(user.getEmail())
	            .password(user.getPassword())
	            .roles(user.getRole().getName().toUpperCase()) 
	            .disabled(!user.isEnabled())     
	            .build();
	}


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
//        	log.trace(authority.getAuthority());
        	String role = authority.getAuthority();
            System.out.println("Authority: " + role); // Logging the role
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/index");
                return;
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                response.sendRedirect("/user/index");
                return;
            }
        }

        
//        response.sendRedirect("/home");
		
	}

}
