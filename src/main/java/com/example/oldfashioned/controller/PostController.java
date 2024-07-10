package com.example.oldfashioned.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.oldfashioned.entity.Category;
import com.example.oldfashioned.entity.Follow;
import com.example.oldfashioned.entity.Keep;
import com.example.oldfashioned.entity.Like;
import com.example.oldfashioned.entity.Post;
import com.example.oldfashioned.entity.Store;
import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.form.FollowForm;
import com.example.oldfashioned.form.PostRegisterForm;
import com.example.oldfashioned.repository.CategoryRepository;
import com.example.oldfashioned.repository.FollowRepository;
import com.example.oldfashioned.repository.KeepRepository;
import com.example.oldfashioned.repository.LikeRepository;
import com.example.oldfashioned.repository.PostRepository;
import com.example.oldfashioned.repository.StoreRepository;
import com.example.oldfashioned.repository.UserRepository;
import com.example.oldfashioned.security.UserDetailsImpl;
import com.example.oldfashioned.service.PostService;
import com.example.oldfashioned.service.StoreService;

@Controller
@RequestMapping("/posts")
public class PostController {
	private final PostRepository postRepository;
	private final CategoryRepository categoryRepository;
	private final StoreRepository storeRepository;
	private final StoreService storeService;
	private final PostService postService;
	private final UserRepository userRepository;
	private final LikeRepository likeRepository;
	private final KeepRepository keepRepository;
	private final FollowRepository followRepository;
	
	public PostController(PostRepository postRepository, CategoryRepository categoryRepository, StoreRepository storeRepository, StoreService storeService, PostService postService, UserRepository userRepositpry, LikeRepository likeRepository, KeepRepository keepRepository, FollowRepository followRepository) {
		this.postRepository = postRepository;
		this.categoryRepository = categoryRepository;
		this.storeRepository = storeRepository;
		this.storeService = storeService;
		this.postService = postService;
		this.userRepository = userRepositpry;
		this.likeRepository = likeRepository;
		this.keepRepository = keepRepository;
		this.followRepository = followRepository;
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		List<Category> category = categoryRepository.findAll();
		List<Store> store = storeRepository.findAll();
		
		model.addAttribute("postRegisterForm", new PostRegisterForm());
		model.addAttribute("category", category);
		model.addAttribute("store", store);
		
		return "posts/register";
	}
	
	@PostMapping("/create")
	public String create(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @ModelAttribute @Validated PostRegisterForm postRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		User user = userDetailsImpl.getUser();
		String searchId = postRegisterForm.getStoreName();
		
		if (bindingResult.hasErrors()) {
			return "posts/register";
		}
		
		Store store = storeRepository.findByName(searchId);
		if (store == null) {
			storeService.create(postRegisterForm);
			Store storeSearch = storeRepository.findByName(searchId);
			postRegisterForm.setStoreId(storeSearch);
		} else {
			postRegisterForm.setStoreId(postRegisterForm.getStoreId());
		}
		
		
		postRegisterForm.setUserId(user);
		
		postService.create(postRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "投稿しました。");
		
		return "redirect:/posts/myPage";
	}
	
	@GetMapping("/myPage")
	public String myPage(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		User user = userDetailsImpl.getUser();
		Page<Post> postPage = postRepository.findByUserId(user.getId(), pageable);
		
		model.addAttribute("postPage", postPage);
		model.addAttribute("user", user);
		
		return "posts/myPage";
	}
	
	@GetMapping("/{id}")
	public String show(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, @PathVariable("id") Long id) {
		Post post = postRepository.findById(id);
		User user = post.getUser();
		
		if (userDetailsImpl != null) {
			User self = userDetailsImpl.getUser();
			List<Like> like = likeRepository.findByPostIdAndUserId(post.getId(), self.getId());
			
			if (like.isEmpty()) {
				Boolean likeRegister = true;
				model.addAttribute("likeRegister", likeRegister);
			} else {
				Boolean likeDelete = true;
				model.addAttribute("likeDelete", likeDelete);
			}
		}
		
		model.addAttribute("post", post);
		model.addAttribute("user", user);
		
		return "posts/show";
	}
	
	@GetMapping("/storePage/{id}")
	public String storePage(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, @PathVariable("id") Integer id, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		Page<Post> postPage = postRepository.findByStoreId(id, pageable);
		Store store = storeRepository.findById(id);
		
		if (userDetailsImpl != null) {
			User self = userDetailsImpl.getUser();
			List<Keep> keep = keepRepository.findByStoreIdAndUserId(store.getId(), self.getId());
			
			if (keep.isEmpty()) {
				Boolean keepRegister = true;
				model.addAttribute("keepRegister", keepRegister);
			} else {
				Boolean keepDelete = true;
				model.addAttribute("keepDelete", keepDelete);
			}
			
		}
		
		model.addAttribute("postPage", postPage);
		model.addAttribute("store", store);
		
		return "posts/storePage";
	}
	
	@GetMapping("/otherPage/{id}")
	public String otherPage(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, @PathVariable("id") Integer id, FollowForm followForm, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		Page<Post> postPage = postRepository.findByUserId(id, pageable);
		User user = userRepository.findById(id).orElse(null);	
		
		if (userDetailsImpl != null) {
			User self = userDetailsImpl.getUser();
			List<Follow> follow = followRepository.findByUserIdAndFollowId(user.getId(), self.getId());
			
			if (follow.isEmpty()) {
				Boolean followRegister = true;
				model.addAttribute("followRegister", followRegister);
			} else {
				Boolean followDelete = true;
				model.addAttribute("followDelete", followDelete);
			}
		}
		
		model.addAttribute("postPage", postPage);
		model.addAttribute("user", user);
		
		return "posts/otherPage";
	}
}
