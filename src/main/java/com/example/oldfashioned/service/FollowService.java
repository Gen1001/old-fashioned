package com.example.oldfashioned.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.oldfashioned.entity.Follow;
import com.example.oldfashioned.form.FollowForm;
import com.example.oldfashioned.repository.FollowRepository;

@Service
public class FollowService {
	private final FollowRepository followRepository;
	
	public FollowService(FollowRepository followRepository) {
		this.followRepository = followRepository;
	}
	
	
	@Transactional
	public void create(FollowForm followForm) {
		Follow follow = new Follow();
		
		follow.setUser(followForm.getUserId());
		follow.setFollow(followForm.getFollowId());
		
		followRepository.save(follow);
	}
}
