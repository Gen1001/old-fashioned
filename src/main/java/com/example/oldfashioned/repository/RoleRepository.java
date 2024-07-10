package com.example.oldfashioned.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oldfashioned.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	public Role findByName(String name);
}
