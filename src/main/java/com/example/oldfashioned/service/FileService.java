package com.example.oldfashioned.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.oldfashioned.entity.File;
import com.example.oldfashioned.entity.Post;
import com.example.oldfashioned.form.PostRegisterForm;
import com.example.oldfashioned.repository.FileRepository;
import com.example.oldfashioned.repository.PostRepository;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
public class FileService {
	private final FileRepository fileRepository;
	private final PostRepository postRepository;
	private final S3Client s3Client;
	private final String bucketName = "old-fahioned";
	
	public FileService(FileRepository fileRepository, PostRepository postRepository) {
		this.fileRepository = fileRepository;
		this.postRepository = postRepository;
		this.s3Client = S3Client.builder()
				.region(Region.AP_SOUTHEAST_2)
				.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
	}
	
    @Transactional
    public void create(PostRegisterForm postRegisterForm, Integer postId) {
    	MultipartFile[] imageFiles = postRegisterForm.getImageFiles();
    	Post post = postRepository.getReferenceById(postId);
    	
//		Integer postId = postRepository.getReferenceById(post);
		
		for (MultipartFile imageFile : imageFiles) {
			String hashedFileName = generateNewFileName(imageFile.getOriginalFilename());
			String keyName = "clothes/" + hashedFileName;
			String fileUrl = uploadFile(s3Client, bucketName, keyName, imageFile);
			try {
				File file = new File();
				
				file.setPost(post);
				file.setFileUrl(fileUrl);
				
				file = fileRepository.save(file);
			} catch(Exception e) {
				System.out.println("error" + e.getMessage());
				e.printStackTrace();
			}
		}
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
