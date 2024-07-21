package com.example.oldfashioned.service;

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

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
	//AWSのバケット名を取得する
    private final S3Client s3Client;
    private final String bucketName = "old-fahioned";

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        
		//AWSのS3クライアントを初期化する
        this.s3Client = S3Client.builder()
                .region(Region.AP_SOUTHEAST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    // userテーブルに会員情報を登録
    @Transactional
    public User create(SignupForm signupForm) {
        User user = new User();
        Role role = roleRepository.findByName("ROLE_GENERAL");
        
        // signupFormに紐づいたimageFileが空でなければAWSに画像をアップロードする
        MultipartFile imageFile = signupForm.getImageFile();
        if (!imageFile.isEmpty()) {
        	
        	// imageFileをもとにUUIDを用いてファイル名を生成する
            String hashedFileName = generateNewFileName(imageFile.getOriginalFilename()); 
            
            // 画像をAWSにアップロードする
            String keyName = "profile/" + hashedFileName;
            String fileUrl = uploadFile(s3Client, bucketName, keyName, imageFile);
            user.setUserPhoto(fileUrl); // URLを設定
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

    // userテーブルに会員情報の変更を反映する
    @Transactional
    public User update(UserEditForm userEditForm) {
        User user = userRepository.getReferenceById(userEditForm.getId());

        // signupFormに紐づいたimageFileが空でなければAWSに画像をアップロードする
        MultipartFile imageFile = userEditForm.getImageFile();
        if (!imageFile.isEmpty()) {
        	
        	// imageFileをもとにUUIDを用いてファイル名を生成する
            String hashedFileName = generateNewFileName(imageFile.getOriginalFilename());
            
            // 画像をAWSにアップロードする
            String keyName = "profile/" + hashedFileName;
            String fileUrl = uploadFile(s3Client, bucketName, keyName, imageFile);
            user.setUserPhoto(fileUrl); // URLを設定
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

    // メールアドレスが登録済みかチェック
    public boolean isEmailRegisterd(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

    // パスワードの一致チェック
    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

    // メールアドレスが変更されたかチェック
    public boolean isEmailChanged(UserEditForm userEditForm) {
        User currentUser = userRepository.getReferenceById(userEditForm.getId());
        return !userEditForm.getEmail().equals(currentUser.getEmail());
    }

    // UUIDを使って生成したファイル名を返す
    public String generateNewFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String hashedFileName = UUID.randomUUID().toString() + extension;
        return hashedFileName;
    }

    // 画像をawsに登録し、URLを返す
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

    // ユーザーを有効にする
    @Transactional
    public void enableUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    // パスワードを再設定する
    @Transactional
    public void changePassword(PasswordResetForm passwordResetForm) {
        User user = userRepository.getReferenceById(passwordResetForm.getId());
        user.setPassword(passwordEncoder.encode(passwordResetForm.getPassword()));
        userRepository.save(user);
    }
}
