package com.example.oldfashioned.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.entity.VerificationToken;
import com.example.oldfashioned.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {
	private final VerificationTokenRepository verificationTokenRepository;
	
	public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
		this.verificationTokenRepository = verificationTokenRepository;
	}
	
	// verificationTokenテーブルにトークン情報を登録する
	@Transactional
	public void create(User user, String token) {
		VerificationToken verificationToken = new VerificationToken();
		
		verificationToken.setUser(user);
		verificationToken.setToken(token);
		
		verificationTokenRepository.save(verificationToken);
	}
	
	// tokenを引数にDBからtokenを検索した結果を返す
	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}
}
