package com.example.project_stock.serviceImplementation;

import org.springframework.stereotype.Service;

import com.example.project_stock.model.User;
import com.example.project_stock.repository.UserRepository;

@Service
public class VerificationTokenService {

	
    private UserRepository userRepository;
    
    public VerificationTokenService(UserRepository userRepository) {
    	this.userRepository = userRepository;
    }

    public void createVerificationToken(User user, String token) {
        user.setVerificationToken(token);
        userRepository.save(user);
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
}
