package com.example.oldfashioned.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.oldfashioned.entity.User;

@Component
public class ForgetEventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;
	
	public ForgetEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	public void publishForgetEvent(User user, String requestUrl) {
		applicationEventPublisher.publishEvent(new ForgetEvent(this, user, requestUrl));
	}
}
