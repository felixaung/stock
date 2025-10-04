package com.example.project_stock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.project_stock.serviceImplementation.CustomUserService;
import com.example.project_stock.serviceImplementation.UserServiceImplementation;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final UserServiceImplementation userService;

    public SecurityConfig(UserServiceImplementation userService) {
        this.userService = userService;
    }
	     
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/log-in", "/register", "/verify-email", "/bootstrap/**", "/images/**","/","/home").permitAll()
            .requestMatchers("/admin/**").hasRole("admin")
            .requestMatchers("/user/**").hasRole("user")
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/log-in")
            .loginProcessingUrl("/log-in") 
            .usernameParameter("email")    
            .passwordParameter("password")
            .defaultSuccessUrl("/home", true)
            .failureUrl("/log-in?error=true")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/log-in?logout=true")
            .permitAll()
        );

    return http.build();
	}
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new  BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder encoder)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
        		.userDetailsService(userService)
                .passwordEncoder(encoder)
                .and()
                .build();
    }
}
