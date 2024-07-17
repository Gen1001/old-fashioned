package com.example.oldfashioned.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.oldfashioned.entity.Like;
import com.example.oldfashioned.form.LikeForm;
import com.example.oldfashioned.repository.LikeRepository;

@Service
public class LikeService {
	private final LikeRepository likeRepository;
	
	public LikeService(LikeRepository likeRepository) {
		this.likeRepository = likeRepository;
	}
	
	@Transactional
	public void create(LikeForm likeForm) {
		Like like = new Like();
		
		like.setPost(likeForm.getPostId());
		like.setUser(likeForm.getUserId());
		
		likeRepository.save(like);
	}
}
