package com.example.oldfashioned.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oldfashioned.entity.PasswordToken;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Integer> {
	public PasswordToken findByToken(String token);
}
