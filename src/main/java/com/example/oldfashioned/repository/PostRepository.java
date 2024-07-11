package com.example.oldfashioned.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oldfashioned.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	public List<Post> findTop12ByOrderByCreatedAtDesc();
	public Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
	public Page<Post> findByCategoryId(Integer id, Pageable pageable);
	public Page<Post> findByUserId(Integer id, Pageable pageable);
	public Page<Post> findByStoreId(Integer id, Pageable pageable);
	public Post findByCategoryId(Integer id);
	public Post findById(Long id);
}
