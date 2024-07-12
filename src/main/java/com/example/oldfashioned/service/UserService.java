package com.example.oldfashioned.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.oldfashioned.entity.Role;
import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.form.PasswordResetForm;
import com.example.oldfashioned.form.SignupForm;
import com.example.oldfashioned.form.UserEditForm;
import com.example.oldfashioned.repository.RoleRepository;
import com.example.oldfashioned.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Transactional
	public User create(SignupForm signupForm) {
		User user = new User();
		Role role = roleRepository.findByName("ROLE_GENERAL");
		MultipartFile imageFile = signupForm.getImageFile();
		
		if (!imageFile.isEmpty()) {
			user.setUserPhoto(saveCroppedImage(imageFile));

		}
		
		user.setName(signupForm.getName());
		user.setRole(role);
		user.setEmail(signupForm.getEmail());
		user.setPostalCode(signupForm.getPostalCode());
		user.setAddress(signupForm.getAddress());
		user.setNickname(signupForm.getNickname());
		user.setPhoneNumber(signupForm.getPhoneNumber());
		user.setIntroduction(signupForm.getIntroduction());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		user.setEnabled(false);
		
		return userRepository.save(user);
	}
	
	@Transactional
	public User update(UserEditForm userEditForm) {
		User user = userRepository.getReferenceById(userEditForm.getId());
		
		MultipartFile imageFile = userEditForm.getImageFile();
		
		if (!imageFile.isEmpty()) {
			user.setUserPhoto(saveCroppedImage(imageFile));
		}
		
		user.setName(userEditForm.getName());
		user.setEmail(userEditForm.getEmail());
		user.setPostalCode(userEditForm.getPostalCode());
		user.setAddress(userEditForm.getAddress());
		user.setNickname(userEditForm.getNickname());
		user.setPhoneNumber(userEditForm.getPhoneNumber());
		user.setIntroduction(userEditForm.getIntroduction());
		
		return userRepository.save(user);
	}
	
	//メールアドレスが登録済みかチェック
	public boolean isEmailRegisterd(String email) {
		User user = userRepository.findByEmail(email);
		return user != null;
	}
	
	//パスワードの一致チェック
	public boolean isSamePassword(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}
	
	//メールアドレスが変更されたかチェック
	public boolean isEmailChanged(UserEditForm userEditForm) {
		User currentUser = userRepository.getReferenceById(userEditForm.getId());
		
		return !userEditForm.getEmail().equals(currentUser.getEmail());
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
    
    //ユーザーを有効にする
    @Transactional
    public void enableUser(User user) {
    	user.setEnabled(true);
    	userRepository.save(user);
    }
    
    //パスワードを再設定する
    @Transactional
    public void changePassword(PasswordResetForm passwordResetForm) {
    	User user = userRepository.getReferenceById(passwordResetForm.getId());
    	
    	user.setPassword(passwordEncoder.encode(passwordResetForm.getPassword()));
    	userRepository.save(user);
    }
    
    //クリップした画像を保存する
    @Transactional
    public String saveCroppedImage(MultipartFile imageFile) {
    	
        String hashedFileName = generateNewFileName(imageFile.getOriginalFilename());
        Path filePath = Paths.get("src/main/resources/static/profile/" + hashedFileName);

        try {
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("画像の保存中にエラーが発生しました。", e);
        }

        return hashedFileName;
    }

}
