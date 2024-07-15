package com.example.oldfashioned.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.oldfashioned.entity.File;
import com.example.oldfashioned.entity.Post;
import com.example.oldfashioned.repository.FileRepository;
import com.example.oldfashioned.repository.PostRepository;

@Service
public class FileService {
	private final FileRepository fileRepository;
	private final PostRepository postRepository;
	
	public FileService(FileRepository fileRepository, PostRepository postRepository) {
		this.fileRepository = fileRepository;
		this.postRepository = postRepository;
	}
	
    @Transactional
    public File create(Integer postId, String url) {
        File file = new File();
        Post post = postRepository.getReferenceById(postId);

        file.setPost(post);
        file.setFileUrl(url);
        
        File saveFile = fileRepository.save(file);
        System.out.println("File saved with ID: " + saveFile.getId());

        return saveFile;
    }

}
