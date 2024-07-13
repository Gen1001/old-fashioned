package com.example.oldfashioned.form;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignupForm {
	@NotBlank(message = "氏名を入力してください。")
	private String name;
	
	@NotBlank(message = "メールアドレスを入力してください。")
	private String email;
	
	@NotBlank(message = "郵便番号を入力してください。")
    @Pattern(regexp = "\\d{7}", message = "数字7桁を入力してください。")
	private String postalCode;
	
	@NotBlank(message = "住所を入力してください。")
	private String address;
	
	@NotBlank(message = "表示されるニックネームを入力してください。")
	private String nickname;
	
	@NotBlank(message = "電話番号を入力してください。")
    @Pattern(regexp = "\\d{10,11}", message = "数字10桁または11桁を入力してください。")
	private String phoneNumber;
	
	private String introduction;
	
	private MultipartFile imageFile;
	
	@NotBlank(message = "パスワードを入力してください。")
	@Length(min = 8, message = "パスワードは8文字以上で入力してください。")
	private String password;
	
	@NotBlank(message = "パスワード（確認用）を入力してください。")
	private String passwordConfirmation;
}
