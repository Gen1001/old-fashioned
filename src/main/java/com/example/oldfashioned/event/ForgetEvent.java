package com.example.oldfashioned.event;

import org.springframework.context.ApplicationEvent;

import com.example.oldfashioned.entity.User;

import lombok.Getter;

@Getter
public class ForgetEvent extends ApplicationEvent {
	private User user;
	private String requestUrl;
	
	public ForgetEvent(Object source, User user, String requestUrl) {
		super(source);
		this.user = user;
		this.requestUrl = requestUrl;
	}
}
