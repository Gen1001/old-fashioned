package com.example.oldfashioned.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oldfashioned.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	public Page<User> findByNicknameLike(String keyword, Pageable pageable);
	public User findByEmail(String email);
}
