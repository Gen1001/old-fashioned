package com.example.oldfashioned.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.oldfashioned.entity.Post;
import com.example.oldfashioned.form.PostRegisterForm;
import com.example.oldfashioned.repository.PostRepository;

import jakarta.transaction.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
public class PostService {
	private final PostRepository postRepository;
	
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
    }

	@Transactional
	public Integer create(PostRegisterForm postRegisterForm) {
		Post post = new Post();
		
		post.setStore(postRegisterForm.getStoreId());
		post.setCategory(postRegisterForm.getCategoryId());
		post.setUser(postRegisterForm.getUserId());
		post.setName(postRegisterForm.getName());
		post.setContent(postRegisterForm.getContent());
		post.setLatitude(postRegisterForm.getLatitude());
		post.setLongitude(postRegisterForm.getLongitude());
		post.setStoreName(postRegisterForm.getStoreName());
		
		postRepository.save(post);
		Integer postId = post.getId();
		return postId;
	}
	
	public String generateNewFileName(String fileName) {
        String[] fileNames = fileName.split("\\.");
        for (int i = 0; i < fileNames.length - 1; i++) {
            fileNames[i] = UUID.randomUUID().toString();
        }
        return String.join(".", fileNames);
    }

    public String uploadFile(S3Client s3, String bucketName, String keyName, MultipartFile imageFile) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            PutObjectResponse response = s3.putObject(putObjectRequest,
                    RequestBody.fromBytes(imageFile.getBytes()));

            String fileUrl = s3.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build()).toString();

            System.out.println("File uploaded successfully. ETag: " + response.eTag());
            return fileUrl;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file to S3.", e);
        }
    }
    
}
