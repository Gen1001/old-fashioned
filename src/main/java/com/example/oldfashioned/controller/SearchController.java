package com.example.oldfashioned.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.oldfashioned.entity.Store;
import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.repository.StoreRepository;
import com.example.oldfashioned.repository.UserRepository;

@Controller
@RequestMapping("/search")
public class SearchController {
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	
	//GoogleMapのApiキーとMapIdを取得する
	@Value("${google.maps.api.key}")
	private String apiKey;
	@Value("${google.maps.map.id}")
	private String mapId;
	
	public SearchController(StoreRepository storeRepository, UserRepository userRepository){
		this.storeRepository = storeRepository;
		this.userRepository = userRepository;
	}
	
	//ユーザー一覧ページに遷移
	@GetMapping("/user")
	public String user(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
		List<User> user = userRepository.findAll();
		Page<User> userPage;
		
		//入力されたユーザー名がnllと空でなけば検索したユーザー名に近い一覧を表示する
		if (keyword != null && !keyword.isEmpty()) {
			String likePattern = "%" + keyword + "%";
			userPage = userRepository.findByNicknameLike(likePattern, pageable);
		} else {
			userPage = userRepository.findAll(pageable);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("userPage", userPage);
		
		return "search/user";
	}
	
	//店舗一覧ページに遷移
	@GetMapping("/store")
	public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
		List<Store> store = storeRepository.findAll();
		Page<Store> storePage;
		
		//入力された店舗名がnullと空でなければ検索した店舗名に近い一覧を表示する
		if (keyword != null && !keyword.isEmpty()) {
			String likePattern = "%" + keyword + "%";
			storePage = storeRepository.findByNameLike(likePattern, pageable);
		} else {
			storePage = storeRepository.findAll(pageable);
		}
		
		model.addAttribute("store", store);
		model.addAttribute("storePage", storePage);
		model.addAttribute("apiKey", apiKey);
		model.addAttribute("mapId", mapId);		
		
		return "search/store";
	}
	
}