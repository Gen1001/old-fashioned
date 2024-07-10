package com.example.oldfashioned.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.oldfashioned.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    public Store findByName(String name);
    public Store findById(Integer id);
}
