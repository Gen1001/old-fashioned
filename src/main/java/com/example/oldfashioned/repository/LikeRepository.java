package com.example.oldfashioned.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oldfashioned.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Integer>{
	public List<Like> findByPostIdAndUserId(Integer postId, Integer userId); 
	public Page<Like> findByUserId(Integer id, Pageable pageable);
	public void deleteByPostIdAndUserId(Integer postId, Integer userId);
}
