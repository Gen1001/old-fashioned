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

import com.example.oldfashioned.entity.Keep;
import com.example.oldfashioned.entity.Store;
import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.form.KeepForm;
import com.example.oldfashioned.repository.KeepRepository;
import com.example.oldfashioned.repository.StoreRepository;
import com.example.oldfashioned.security.UserDetailsImpl;
import com.example.oldfashioned.service.KeepService;

@Transactional
@Controller
@RequestMapping("/keeps")
public class KeepController {
	private KeepRepository keepRepository;
	private KeepService keepService;
	private StoreRepository storeRepository;
	
	public KeepController(KeepRepository keepRepository, KeepService keepService, StoreRepository storeRepository) {
		this.keepRepository = keepRepository;
		this.keepService = keepService;
		this.storeRepository = storeRepository;
	}
	
	//店舗保存ページに遷移
	@GetMapping("")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		User user = userDetailsImpl.getUser();
		Page<Keep> keepPage = keepRepository.findByUserId(user.getId(), pageable);
		
		model.addAttribute("user", user);
		model.addAttribute("keepPage", keepPage);
		
		return "keeps/index";
	}
	
	//店舗を保存する
	@GetMapping("/create/{id}")
	public String register(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PathVariable("id") Long id, KeepForm keepForm, RedirectAttributes redirectAttributes) {
		Store store = storeRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
		
		keepForm.setStoreId(store);
		keepForm.setUserId(user);
		
		//DBに店舗情報を登録する
		keepService.create(keepForm);
		
		String redirectUrl = String.format("redirect:/posts/storePage/%d", store.getId());
		
		return redirectUrl;
	}
	
	//店舗の保存ページから削除する
	@GetMapping("/delete/{id}") 
	public String delete(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PathVariable("id") Long id, KeepForm keepForm, RedirectAttributes redirectAttributes) {
		Store store = storeRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
		
		//DBから店舗情報を削除する
		keepRepository.deleteByStoreIdAndUserId(store.getId(), user.getId());
		
		String redirectUrl = String.format("redirect:/posts/storePage/%d", store.getId());
		
		return redirectUrl;
	}
}
