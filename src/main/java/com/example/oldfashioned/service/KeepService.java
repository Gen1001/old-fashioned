package com.example.oldfashioned.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.oldfashioned.entity.Keep;
import com.example.oldfashioned.form.KeepForm;
import com.example.oldfashioned.repository.KeepRepository;

@Service
public class KeepService {
	private final KeepRepository keepRepository;
	
	public KeepService(KeepRepository keepRepository) {
		this.keepRepository = keepRepository;
	}
	
	//keepテーブルに店舗の保存情報を登録する
	@Transactional
	public void create(KeepForm keepForm) {
		Keep keep = new Keep();
		
		keep.setStore(keepForm.getStoreId());
		keep.setUser(keepForm.getUserId());
		
		keepRepository.save(keep);
	}
}
