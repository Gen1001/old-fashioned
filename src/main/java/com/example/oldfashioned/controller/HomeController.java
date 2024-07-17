package com.example.oldfashioned.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.oldfashioned.entity.Category;
import com.example.oldfashioned.entity.File;
import com.example.oldfashioned.entity.Store;
import com.example.oldfashioned.repository.CategoryRepository;
import com.example.oldfashioned.repository.FileRepository;
import com.example.oldfashioned.repository.StoreRepository;

@Controller
public class HomeController {
	private final CategoryRepository categoryRepository;
	private final StoreRepository storeRepository;
	private final FileRepository fileRepository;
	@Value("${google.maps.api.key}")
	private String apiKey;
	@Value("${google.maps.map.id}")
	private String mapId;
	
	public HomeController(CategoryRepository categoryRepository, StoreRepository storeRepository, FileRepository fileRepository) {
		this.categoryRepository = categoryRepository;
		this.storeRepository = storeRepository;
		this.fileRepository = fileRepository;
	}
	@GetMapping("/")
	public String index(Model model) {
		List<Category> category = categoryRepository.findAll();
		List<Store> store = storeRepository.findAll();
		Pageable pageable = PageRequest.of(0, 12);
		Page<File> file = fileRepository.findTop12DistinctPostIdByOrderByCreatedAtDesc(pageable);
		
		model.addAttribute("category", category);
		model.addAttribute("store", store);
		model.addAttribute("apiKey", apiKey);
		model.addAttribute("mapId", mapId);
		model.addAttribute("file", file);
		
		return "index";
	}
	
	
}
