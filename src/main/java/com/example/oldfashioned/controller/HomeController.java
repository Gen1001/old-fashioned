package com.example.oldfashioned.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.oldfashioned.entity.Category;
import com.example.oldfashioned.entity.Post;
import com.example.oldfashioned.entity.Store;
import com.example.oldfashioned.repository.CategoryRepository;
import com.example.oldfashioned.repository.PostRepository;
import com.example.oldfashioned.repository.StoreRepository;

@Controller
public class HomeController {
	private final PostRepository postRepository;
	private final CategoryRepository categoryRepository;
	private final StoreRepository storeRepository;
	@Value("${google.maps.api.key}")
	private String apiKey;
	@Value("${google.maps.map.id}")
	private String mapId;
	
	public HomeController(PostRepository postRepository, CategoryRepository categoryRepository, StoreRepository storeRepository) {
		this.postRepository = postRepository;
		this.categoryRepository = categoryRepository;
		this.storeRepository = storeRepository;
	}
	@GetMapping("/")
	public String index(Model model) {
		List<Post> postPage;
		List<Category> category = categoryRepository.findAll();
		List<Store> store = storeRepository.findAll();
		
		postPage = postRepository.findTop12ByOrderByCreatedAtDesc();
		
		model.addAttribute("postPage", postPage);
		model.addAttribute("category", category);
		model.addAttribute("store", store);
		model.addAttribute("apiKey", apiKey);
		model.addAttribute("mapId", mapId);
		
		
		return "index";
	}
	
	
}
