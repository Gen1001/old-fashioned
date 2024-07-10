package com.example.oldfashioned.form;

import org.springframework.web.multipart.MultipartFile;

import com.example.oldfashioned.entity.Category;
import com.example.oldfashioned.entity.Store;
import com.example.oldfashioned.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostRegisterForm {
	private Category categoryId;
	
	private Store storeId;
	
	private User userId;
	
	private String storeName;
	
	@NotNull(message = "投稿画像を選択してください。")
	private MultipartFile imageFile;
	
	@NotBlank(message = "商品名を入力してください。")
	private String name;
	
	@NotBlank(message = "投稿内容を入力してください。")
	private String content;
	
	@NotNull
	private Double latitude;
	
	@NotNull 
	private Double longitude;
}
