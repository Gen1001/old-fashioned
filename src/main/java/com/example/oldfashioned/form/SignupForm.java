package com.example.oldfashioned.form;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignupForm {
	@NotBlank(message = "氏名を入力してください。")
	private String name;
	
	@NotBlank(message = "メールアドレスを入力してください。")
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
	
	@NotBlank(message = "パスワードを入力してください。")
	@Length(min = 8, message = "パスワードは8文字以上で入力してください。")
	private String password;
	
	@NotBlank(message = "パスワード（確認用）を入力してください。")
	private String passwordConfirmation;
}
