package com.example.oldfashioned.controller;

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

import com.example.oldfashioned.entity.Follow;
import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.form.FollowForm;
import com.example.oldfashioned.repository.FollowRepository;
import com.example.oldfashioned.repository.UserRepository;
import com.example.oldfashioned.security.UserDetailsImpl;
import com.example.oldfashioned.service.FollowService;

@Transactional
@Controller
@RequestMapping("/follows")
public class FollowController {
	private FollowRepository followRepository;
	private FollowService followService;
	private UserRepository userRepository;
	
	public FollowController(FollowRepository followRepository, FollowService followService, UserRepository userRepository) {
		this.followRepository = followRepository;
		this.followService = followService;
		this.userRepository = userRepository;
	}
	
	@GetMapping("/follow")
	public String follow(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		User user = userDetailsImpl.getUser();
		Page<Follow> followPage = followRepository.findByUserId(user.getId(), pageable);
		
		model.addAttribute("user", user);
		model.addAttribute("followPage", followPage);
		
		return "follows/follow";
	}
	
	@GetMapping("/follower")
	public String follower(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		User user = userDetailsImpl.getUser();
		Page<Follow> followPage = followRepository.findByFollowId(user.getId(), pageable);
		
		model.addAttribute("user", user);
		model.addAttribute("followPage", followPage);
		
		return "follows/follower";
	}
	
	@GetMapping("/create/{id}")
	public String register(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PathVariable("id") Integer id, FollowForm followForm, RedirectAttributes redirectAttributes) {
		User user = userRepository.getReferenceById(id);
		User self = userDetailsImpl.getUser();
		
		followForm.setUserId(self);
		followForm.setFollowId(user);
		followService.create(followForm);
		
		String redirectUrl = String.format("redirect:/posts/otherPage/%d", user.getId());
		
		return redirectUrl;
	}
	
	@GetMapping("/delete/{id}") 
	public String delete(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PathVariable("id") Integer id, FollowForm followForm, RedirectAttributes redirectAttributes) {
		User user = userRepository.getReferenceById(id);
		User self = userDetailsImpl.getUser();
		
		followRepository.deleteByUserIdAndFollowId(self.getId(), user.getId());
		
		String redirectUrl = String.format("redirect:/posts/otherPage/%d", user.getId());
		
		return redirectUrl;
	}
}
