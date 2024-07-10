package com.example.oldfashioned.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oldfashioned.entity.VerificationToken;

public interface VerificationTokenRepository  extends JpaRepository<VerificationToken, Integer>{
	public VerificationToken findByToken(String token);
}
