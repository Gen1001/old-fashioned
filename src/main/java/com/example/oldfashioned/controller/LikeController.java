package com.example.oldfashioned.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.oldfashioned.entity.File;
import com.example.oldfashioned.entity.Like;
import com.example.oldfashioned.entity.Post;
import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.form.LikeForm;
import com.example.oldfashioned.repository.FileRepository;
import com.example.oldfashioned.repository.LikeRepository;
import com.example.oldfashioned.repository.PostRepository;
import com.example.oldfashioned.security.UserDetailsImpl;
import com.example.oldfashioned.service.LikeService;

@Transactional
@Controller
@RequestMapping("/likes")
public class LikeController {
	private final LikeRepository likeRepository;
	private final LikeService likeService;
	private final PostRepository postRepository;
	private final FileRepository fileRepository;
	
	public LikeController(LikeRepository likeRepository, LikeService likeService, PostRepository postRpeository, FileRepository fileRepository) {
		this.likeRepository = likeRepository;
		this.likeService = likeService;
		this.postRepository = postRpeository;
		this.fileRepository = fileRepository;
	}
	
	@GetMapping("")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		User user = userDetailsImpl.getUser();
		Page<Like> likePage = likeRepository.findByUserId(user.getId(), pageable);
		List<File> fileList = new ArrayList<>();
		for (Like like : likePage) {
			File file = fileRepository.findFirstByPostId(like.getPost().getId());
			fileList.add(file);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("fileList", fileList);
		model.addAttribute("likePage", likePage);
		
		return "likes/index";
	}
	
	@GetMapping("/create/{id}")
	public String register(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PathVariable("id") Integer id, LikeForm likeForm, RedirectAttributes redirectAttributes) {
		Post post = postRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
		
		likeForm.setPostId(post);
		likeForm.setUserId(user);
		likeService.create(likeForm);
		
		String redirectUrl = String.format("redirect:/posts/show/%d", post.getId());
		
		return redirectUrl;
	}
	
	@GetMapping("/delete/{id}") 
	public String delete(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PathVariable("id") Integer id, LikeForm likeForm, RedirectAttributes redirectAttributes) {
		Post post = postRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
		
		likeRepository.deleteByPostIdAndUserId(post.getId(), user.getId());
		
		String redirectUrl = String.format("redirect:/posts/show/%d", post.getId());
		
		return redirectUrl;
	}
}
