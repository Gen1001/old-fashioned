package com.example.oldfashioned.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.oldfashioned.entity.File;

public interface FileRepository extends JpaRepository<File, Integer> {
	File findFirstByPostId(Integer id);
	List<File> findByPostId(Long id);
	@Query("SELECT f FROM File f WHERE f.id IN (SELECT MAX(f2.id) FROM File f2 GROUP BY f2.post.id) ORDER BY f.createdAt DESC")
	Page<File> findTop12DistinctPostIdByOrderByCreatedAtDesc(Pageable pageable);
	@Query("SELECT f FROM File f WHERE f.id IN (SELECT MAX(f2.id) FROM File f2 GROUP BY f2.post.id) ORDER BY f.createdAt DESC")
	Page<File> findAllDistinctPostIdByOrderByCreatedAtDesc(Pageable pageable);
	@Query("SELECT f FROM File f WHERE f.id IN (SELECT MAX(f2.id) FROM File f2 WHERE f2.post.category.id = :categoryId GROUP BY f2.post.id) ORDER BY f.createdAt DESC")
	Page<File> findFilesDistinctPostIdByCategoryId(Integer categoryId, Pageable pageable);
	@Query("SELECT f FROM File f WHERE f.id IN (SELECT MAX(f2.id) FROM File f2 WHERE f2.post.user.id = :userId GROUP BY f2.post.id) ORDER BY f.createdAt DESC")
	Page<File> findFilesDistinctPostIdByUserId(Integer userId, Pageable pageable);
	@Query("SELECT f FROM File f WHERE f.id IN (SELECT MAX(f2.id) FROM File f2 WHERE f2.post.store.id = :storeId GROUP BY f2.post.id) ORDER BY f.createdAt DESC")
	Page<File> findFilesDistinctPostIdByStoreId(Integer storeId, Pageable pageable);
}

