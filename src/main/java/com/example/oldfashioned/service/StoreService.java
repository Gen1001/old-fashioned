package com.example.oldfashioned.service;

import org.springframework.stereotype.Service;

import com.example.oldfashioned.entity.Store;
import com.example.oldfashioned.form.PostRegisterForm;
import com.example.oldfashioned.repository.StoreRepository;

import jakarta.transaction.Transactional;

@Service
public class StoreService {
	StoreRepository storeRepository;
	
	public StoreService(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}
	
	@Transactional
	public Store create(PostRegisterForm postRegisterForm) {
		Store store = new Store();
		
		store.setLatitude(postRegisterForm.getLatitude());
		store.setLongitude(postRegisterForm.getLongitude());
		store.setName(postRegisterForm.getStoreName());
		
		return storeRepository.save(store);
	}
}
