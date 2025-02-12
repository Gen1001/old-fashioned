package com.example.oldfashioned.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.oldfashioned.entity.User;

@Component
public class SignupEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    
    public SignupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;                
    }
    
	// userとrequestUrlを引数にSignupEventクラスのコンストラクタを呼び出す
    public void publishSignupEvent(User user, String requestUrl) {
        applicationEventPublisher.publishEvent(new SignupEvent(this, user, requestUrl));
    }
}
