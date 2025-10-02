package com.example.project_stock.event;

import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.project_stock.model.User;
import com.example.project_stock.serviceImplementation.EmailService;
import com.example.project_stock.serviceImplementation.VerificationTokenService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
	
    private VerificationTokenService tokenService;

    private EmailService emailService;
    
    public RegistrationListener(VerificationTokenService tokenService, EmailService emailService) {
    	this.tokenService = tokenService;
    	this.emailService = emailService;
    }
    
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		 this.confirmRegistration(event);	
	}
	
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Email Verification";
        String confirmationUrl = "http://localhost:8080/verify-email?token=" + token;
        String message = "Click the link to verify your email: " + confirmationUrl;

        emailService.sendEmail(recipientAddress, subject, message);
    }
}
