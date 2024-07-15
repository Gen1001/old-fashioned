package com.example.oldfashioned.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.oldfashioned.entity.File;
import com.example.oldfashioned.entity.Post;
import com.example.oldfashioned.repository.FileRepository;

@Service
public class FileService {
	private final FileRepository fileRepository;
	
	public FileService(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}
	
    @Transactional
    public File create(Post post, String url) {
        File file = new File();

        file.setPost(post);
        file.setFileUrl(url);
        
        File saveFile = fileRepository.save(file);
        System.out.println("File saved with ID: " + saveFile.getId());

        return saveFile;
    }

}
