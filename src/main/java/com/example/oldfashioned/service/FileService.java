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
	
	//fileテーブルに画像の情報を登録する
    @Transactional
    public File create(String fileUrl, Post post) {
    	File file = new File();
    	
    	file.setPost(post);
    	file.setFileUrl(fileUrl);
    	
    	return fileRepository.save(file);
    }
}
