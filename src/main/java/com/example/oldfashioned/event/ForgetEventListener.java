package com.example.oldfashioned.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.service.PasswordTokenService;

@Component
public class ForgetEventListener {
	private final JavaMailSender javaMailSender;
	private final PasswordTokenService passwordTokenService;
	
	public ForgetEventListener(JavaMailSender mailSender, PasswordTokenService passwordTokenService) {
		this.javaMailSender = mailSender;
		this.passwordTokenService = passwordTokenService;
	}
	
	//forgetEventが発生された時にトークンを作成してメールを送信する
	@EventListener
	private void onForgetEvent(ForgetEvent forgetEvent) {
		
		//UUIDを使って生成したトークンをDBに登録する
		User user = forgetEvent.getUser();
		String token = UUID.randomUUID().toString();
		passwordTokenService.create(user, token);
		
		//送信するメールの内容を設定する
		String recipientAddress = user.getEmail();
		String subject = "パスワード再設定";
		String confirmationUrl = forgetEvent.getRequestUrl() + "/verify?token=" + token;
		String message = "以下のリンクをクリックしてパスワードを再設定してください。";
		
		//メールを送信する
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + confirmationUrl);
		javaMailSender.send(mailMessage);
	}
}