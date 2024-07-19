package com.example.oldfashioned.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.oldfashioned.entity.PasswordToken;
import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.repository.PasswordTokenRepository;

@Service
public class PasswordTokenService {
	private final PasswordTokenRepository passwordTokenRepository;
    
    public PasswordTokenService(PasswordTokenRepository passwordTokenRepository) {        
        this.passwordTokenRepository = passwordTokenRepository;
    } 
    
    //passwordTokenテーブルにトークン情報を登録する
    @Transactional
    public void create(User user, String token) {
        PasswordToken passwordToken = new PasswordToken();
        
        passwordToken.setUser(user);
        passwordToken.setToken(token);        
        
        passwordTokenRepository.save(passwordToken);
    }    
    
    // トークンの文字列で検索した結果を返す
    public PasswordToken getPasswordToken(String token) {
        return passwordTokenRepository.findByToken(token);
    }    
}