package com.example.oldfashioned.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEditForm {
	@NotNull
	private Integer id;
	
	@NotBlank(message = "氏名を入力してください。")
	private String name;
	
	@NotBlank(message = "メールアドレスを入力してください。")
	@Email(message = "メールアドレスは正しい形式で入力してください。")
	private String email;
	
	@NotNull(message = "郵便番号を入力してください。")
	private Integer postalCode;
	
	@NotBlank(message = "住所を入力してください。")
	private String address;
	
	@NotBlank(message = "表示されるニックネームを入力してください。")
	private String nickname;
	
	@NotNull(message = "電話番号を入力してください。")
	private Integer phoneNumber;
	
	private String introduction;
	
	private MultipartFile imageFile;
	
}
