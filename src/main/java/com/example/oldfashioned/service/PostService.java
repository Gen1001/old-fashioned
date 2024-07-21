package com.example.oldfashioned.service;

import org.springframework.stereotype.Service;

import com.example.oldfashioned.entity.Post;
import com.example.oldfashioned.form.PostRegisterForm;
import com.example.oldfashioned.repository.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService {
	private final PostRepository postRepository;
	
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
    }

	// postテーブルに投稿情報を登録する
	@Transactional
	public Integer create(PostRegisterForm postRegisterForm) {
		Post post = new Post();
		
		post.setStore(postRegisterForm.getStoreId());
		post.setCategory(postRegisterForm.getCategoryId());
		post.setUser(postRegisterForm.getUserId());
		post.setName(postRegisterForm.getName());
		post.setContent(postRegisterForm.getContent());
		post.setLatitude(postRegisterForm.getLatitude());
		post.setLongitude(postRegisterForm.getLongitude());
		post.setStoreName(postRegisterForm.getStoreName());
		
		postRepository.save(post);
		Integer postId = post.getId();
		return postId;
	}
}
