package com.example.oldfashioned.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oldfashioned.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Integer>{
	public List<Follow> findByUserIdAndFollowId(Integer userId, Integer followId); 
	public Page<Follow> findByUserId(Integer id, Pageable pageable);
	public Page<Follow> findByFollowId(Integer id, Pageable pageable);
	public void deleteByUserIdAndFollowId(Integer userId, Integer followId);
}
