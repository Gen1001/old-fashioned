package com.example.oldfashioned.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.oldfashioned.entity.Category;
import com.example.oldfashioned.entity.Store;
import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.repository.CategoryRepository;
import com.example.oldfashioned.repository.StoreRepository;
import com.example.oldfashioned.repository.UserRepository;

@Controller
@RequestMapping("/search")
public class SearchController {
	private final CategoryRepository categoryRepository;
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	
	public SearchController(CategoryRepository categoryRepository, StoreRepository storeRepository, UserRepository userRepository){
		this.categoryRepository = categoryRepository;
		this.storeRepository = storeRepository;
		this.userRepository = userRepository;
	}
	
	@GetMapping("/user")
	public String user(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
		List<User> user = userRepository.findAll();
		Page<User> userPage;
		
		if (keyword != null && !keyword.isEmpty()) {
			String likePattern = "%" + keyword + "%";
			userPage = userRepository.findByNicknameLike(likePattern, pageable);
		} else {
			userPage = userRepository.findAll(pageable);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("userPage", userPage);
		
		return "/search/user";
	}
	
	@GetMapping("/store")
	public String index(Model model) {
		List<Category> category = categoryRepository.findAll();
		List<Store> store = storeRepository.findAll();
		
		model.addAttribute("category", category);
		model.addAttribute("store", store);
		
		return "/index";
	}
}