package com.example.oldfashioned.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.oldfashioned.entity.Post;
import com.example.oldfashioned.form.PostRegisterForm;
import com.example.oldfashioned.repository.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService {
	private PostRepository postRepository;
	
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}
	
	@Transactional
	public Post create(PostRegisterForm postRegisterForm) {
		Post post = new Post();
		MultipartFile imageFile = postRegisterForm.getImageFile();
		
		if (!imageFile.isEmpty()) {
			String postPhoto = imageFile.getOriginalFilename();
			String hashedUserPhoto = generateNewFileName(postPhoto);
			Path filePath = Paths.get("src/main/resources/static/clothes/" + hashedUserPhoto);
			copyImageFile(imageFile, filePath);
			post.setPostPhoto(hashedUserPhoto);

		}
		
		post.setStore(postRegisterForm.getStoreId());
		post.setCategory(postRegisterForm.getCategoryId());
		post.setUser(postRegisterForm.getUserId());
		post.setName(postRegisterForm.getName());
		post.setContent(postRegisterForm.getContent());
		post.setLatitude(postRegisterForm.getLatitude());
		post.setLongitude(postRegisterForm.getLongitude());
		post.setStoreName(postRegisterForm.getStoreName());
		
		return postRepository.save(post);
	}
	
	// UUIDを使って生成したファイル名を返す
    public String generateNewFileName(String fileName) {
        String[] fileNames = fileName.split("\\.");                
        for (int i = 0; i < fileNames.length - 1; i++) {
            fileNames[i] = UUID.randomUUID().toString();            
        }
        String hashedFileName = String.join(".", fileNames);
        return hashedFileName;
    }     
    
    // 画像ファイルを指定したファイルにコピーする
    public void copyImageFile(MultipartFile imageFile, Path filePath) {           
        try {
            Files.copy(imageFile.getInputStream(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }          
    } 
    
}
