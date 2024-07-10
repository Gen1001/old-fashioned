package com.example.oldfashioned.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oldfashioned.entity.Keep;

public interface KeepRepository extends JpaRepository<Keep, Integer>{
	public List<Keep> findByStoreIdAndUserId(Integer storeId, Integer userId); 
	public Page<Keep> findByUserId(Integer id, Pageable pageable);
	public void deleteByStoreIdAndUserId(Integer storeId, Integer userId);
}
