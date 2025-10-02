package com.example.project_stock.event;

import org.springframework.context.ApplicationEvent;

import com.example.project_stock.model.User;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	private final User user;
	
	public OnRegistrationCompleteEvent(User user) {
		super(user);
		this.user = user;
	}

   public User getUser() {
        return user;
    }
}
