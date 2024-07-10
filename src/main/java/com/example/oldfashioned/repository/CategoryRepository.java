package com.example.oldfashioned.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oldfashioned.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
	
}
