package com.example.oldfashioned.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.service.VerificationTokenService;

@Component
public class SignupEventListener {
    private final VerificationTokenService verificationTokenService;    
    private final JavaMailSender javaMailSender;
    
    public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
        this.verificationTokenService = verificationTokenService;        
        this.javaMailSender = mailSender;
    }

	//SignupEventが発生された時にトークンを作成してメールを送信する
    @EventListener
    private void onSignupEvent(SignupEvent signupEvent) {
    	
		//UUIDを使って生成したトークンをDBに登録する
        User user = signupEvent.getUser();
        String token = UUID.randomUUID().toString();
        verificationTokenService.create(user, token);
        
		//送信するメールの内容を設定する
        String recipientAddress = user.getEmail();
        String subject = "メール認証";
        String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
        String message = "以下のリンクをクリックして会員登録を完了してください。";
        
		//メールを送信する
        SimpleMailMessage mailMessage = new SimpleMailMessage(); 
        mailMessage.setTo(recipientAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(message + "\n" + confirmationUrl);
        javaMailSender.send(mailMessage);
    }
}
