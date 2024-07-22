package com.example.oldfashioned.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.oldfashioned.entity.Category;
import com.example.oldfashioned.entity.File;
import com.example.oldfashioned.entity.Follow;
import com.example.oldfashioned.entity.Keep;
import com.example.oldfashioned.entity.Like;
import com.example.oldfashioned.entity.Post;
import com.example.oldfashioned.entity.Store;
import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.form.FollowForm;
import com.example.oldfashioned.form.PostRegisterForm;
import com.example.oldfashioned.repository.CategoryRepository;
import com.example.oldfashioned.repository.FileRepository;
import com.example.oldfashioned.repository.FollowRepository;
import com.example.oldfashioned.repository.KeepRepository;
import com.example.oldfashioned.repository.LikeRepository;
import com.example.oldfashioned.repository.PostRepository;
import com.example.oldfashioned.repository.StoreRepository;
import com.example.oldfashioned.repository.UserRepository;
import com.example.oldfashioned.security.UserDetailsImpl;
import com.example.oldfashioned.service.FileService;
import com.example.oldfashioned.service.PostService;
import com.example.oldfashioned.service.StoreService;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Controller
@RequestMapping("/posts")
public class PostController {
	private final PostRepository postRepository;
	private final CategoryRepository categoryRepository;
	private final StoreRepository storeRepository;
	private final StoreService storeService;
	private final PostService postService;
	private final UserRepository userRepository;
	private final LikeRepository likeRepository;
	private final KeepRepository keepRepository;
	private final FollowRepository followRepository;
	private final FileRepository fileRepository;
	private final FileService fileService;
	
	//GoogleMapのApiキーとMapIdを取得する
	@Value("${google.maps.api.key}")
	private String apiKey;
	@Value("${google.maps.map.id}")
	private String mapId;
	
	//AWSのバケット名を取得する
	private final S3Client s3Client;
	private final String bucketName = "old-fahioned";
	
	public PostController(PostRepository postRepository, CategoryRepository categoryRepository, StoreRepository storeRepository, StoreService storeService, PostService postService, UserRepository userRepositpry, LikeRepository likeRepository, KeepRepository keepRepository, FollowRepository followRepository, FileRepository fileRepository, FileService fileService) {
		this.postRepository = postRepository;
		this.categoryRepository = categoryRepository;
		this.storeRepository = storeRepository;
		this.storeService = storeService;
		this.postService = postService;
		this.userRepository = userRepositpry;
		this.likeRepository = likeRepository;
		this.keepRepository = keepRepository;
		this.followRepository = followRepository;
		this.fileRepository = fileRepository;
		this.fileService = fileService;
		
		//AWSのS3クライアントを初期化する
		this.s3Client = S3Client.builder()
				.region(Region.AP_SOUTHEAST_2)
				.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
	}
	
	//投稿一覧ページに遷移
	@GetMapping(" ")
	public String index(Model model, @PageableDefault(page = 0, size = 12, sort = "id", direction = Direction.ASC) Pageable pageable) {
		List<Category> category = categoryRepository.findAll();
		
		Page<File> filePage = fileRepository.findAllDistinctPostIdByOrderByCreatedAtDesc(pageable);
		
		model.addAttribute("filePage", filePage);
		model.addAttribute("category", category);
		
		return "posts/index";
	}
	
	//各カテゴリごとの投稿一覧ページに遷移
	@GetMapping("/category/{id}")
	public String index(Model model, @PageableDefault(page = 0, size = 12, sort = "id", direction = Direction.ASC) Pageable pageable, @PathVariable(name = "id") Integer id) {
		Page<File> filePage = fileRepository.findFilesDistinctPostIdByCategoryId(id, pageable);
		List<Category> category = categoryRepository.findAll();
		Category one = categoryRepository.getReferenceById(id);
		
		model.addAttribute("filePage", filePage);
		model.addAttribute("category", category);
		model.addAttribute("one", one);
		
		return "posts/index";
	}
	
	//投稿ページに遷移
	@GetMapping("/register")
	public String register(Model model) {
		List<Category> category = categoryRepository.findAll();
		List<Store> store = storeRepository.findAll();
		
		model.addAttribute("postRegisterForm", new PostRegisterForm());
		model.addAttribute("category", category);
		model.addAttribute("store", store);
		
		return "posts/register";
	}
	
	//画像を投稿してpostRegisterFormに入力された情報をDBに保存する
	@PostMapping("/create")
	public String create(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @ModelAttribute @Validated PostRegisterForm postRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		User user = userDetailsImpl.getUser();
		String searchId = postRegisterForm.getStoreName();
		Store storeId = postRegisterForm.getStoreId();
		
		//店舗名が入力された場合、入力された店舗名をDBから検索する
		if (searchId != null) {
			Store storeName = storeRepository.findByName(searchId);
			
			//店舗が空白でなく、DBから店舗名が見つからなかった場合店舗情報をDBに新しく登録する
			if (!searchId.trim().isEmpty() && storeName == null) {
				storeService.create(postRegisterForm);
				Store storeSearch = storeRepository.findByName(searchId);
				postRegisterForm.setStoreId(storeSearch);
			} else {
				postRegisterForm.setStoreId(postRegisterForm.getStoreId());
				postRegisterForm.setLatitude(storeId.getLatitude());
				postRegisterForm.setLongitude(storeId.getLongitude());
			}
			
		}
		
		//postRegisterFormで設定したエラーが発生してないか確認
		if (bindingResult.hasErrors()) {
			return "posts/register";
		}
		
		//userをformに紐づけDBに投稿情報を登録する
		postRegisterForm.setUserId(user);
		Integer postId = postService.create(postRegisterForm);
		Post post = postRepository.getReferenceById(postId);
		
		//formに紐ずいたimageFilesを取得する
		MultipartFile[] imageFiles = postRegisterForm.getImageFiles();
		
		//画像を一つずつAWSにアップロードする
		for (MultipartFile imageFile : imageFiles) {
			String hashedFileName = generateNewFileName(imageFile.getOriginalFilename());
			String keyName = "clothes/" + hashedFileName;
			String fileUrl = uploadFile(s3Client, bucketName, keyName, imageFile);
			fileService.create(fileUrl, post);
			}
		
		redirectAttributes.addFlashAttribute("successMessage", "投稿しました。");
		
		return "redirect:/posts/myPage";
	}
	
	//自分の投稿一覧ページに遷移
	@GetMapping("/myPage")
	public String myPage(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, @PageableDefault(page = 0, size = 12, sort = "id", direction = Direction.ASC) Pageable pageable) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		Page<File> filePage = fileRepository.findFilesDistinctPostIdByUserId(user.getId(), pageable);
		
		model.addAttribute("filePage", filePage);
		model.addAttribute("user", user);
		
		return "posts/myPage";
	}
	
	//投稿詳細ページに遷移
	@GetMapping("/show/{id}")
	public String show(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, @PathVariable("id") Long id) {
		Post post = postRepository.findById(id);
		List<File> file = fileRepository.findByPostId(id);
		List<Like> likeSum = likeRepository.findByPostId(id);
		User user = post.getUser();
		
		//会員登録済みかどうかを確認
		if (userDetailsImpl != null) {
			
			//自分の投稿の場合いいねができないようにするため属性を付与する
			User self = userDetailsImpl.getUser();
			if (post.getUser().getId() == self.getId()) {
				model.addAttribute("self", self);
			}
			
			//いいね情報を取得して、いいねされていなければボタンを表示し、されていれば非表示にするため属性を付与する
			List<Like> like = likeRepository.findByPostIdAndUserId(post.getId(), self.getId());
			if (like.isEmpty()) {
				Boolean likeRegister = true;
				model.addAttribute("likeRegister", likeRegister);
			} else {
				Boolean likeDelete = true;
				model.addAttribute("likeDelete", likeDelete);
			}
		}
		
//		いいね数を取得する
		Integer sum = likeSum.size();
		
		model.addAttribute("file", file);
		model.addAttribute("post", post);
		model.addAttribute("user", user);
		model.addAttribute("sum", sum);
		model.addAttribute("apiKey", apiKey);
		model.addAttribute("mapId", mapId);
		
		return "posts/show";
	}
	
	//店舗の投稿一覧ページに遷移
	@GetMapping("/storePage/{id}")
	public String storePage(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, @PathVariable("id") Integer id, @PageableDefault(page = 0, size = 12, sort = "id", direction = Direction.ASC) Pageable pageable) {
		Page<File> filePage = fileRepository.findFilesDistinctPostIdByStoreId(id, pageable);
		Store store = storeRepository.findById(id);
		
		//会員登録済みかどうか確認
		if (userDetailsImpl != null) {
			
			//保存情報を取得し、店舗を保存していなかったらボタンを表示し、保存済みの場合非表示にするための属性を付与する
			User self = userDetailsImpl.getUser();
			List<Keep> keep = keepRepository.findByStoreIdAndUserId(store.getId(), self.getId());
			if (keep.isEmpty()) {
				Boolean keepRegister = true;
				model.addAttribute("keepRegister", keepRegister);
			} else {
				Boolean keepDelete = true;
				model.addAttribute("keepDelete", keepDelete);
			}
			
		}
		
		model.addAttribute("filePage", filePage);
		model.addAttribute("store", store);
		model.addAttribute("apiKey", apiKey);
		model.addAttribute("mapId", mapId);
		
		return "posts/storePage";
	}
	
	//他のユーザーの投稿一覧ページに遷移
	@GetMapping("/otherPage/{id}")
	public String otherPage(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model, @PathVariable("id") Integer id, FollowForm followForm, @PageableDefault(page = 0, size = 12, sort = "id", direction = Direction.ASC) Pageable pageable) {
		Page<File> filePage = fileRepository.findFilesDistinctPostIdByUserId(id, pageable);
		User user = userRepository.findById(id).orElse(null);	
		
		//会員登録済みかどうか確認
		if (userDetailsImpl != null) {
			
			//自分のページに遷移していたらフォローできなくする
			User self = userDetailsImpl.getUser();
			if (user.getId() == self.getId()) {
			model.addAttribute("self", self);
			}
			List<Follow> follow = followRepository.findByUserIdAndFollowId(user.getId(), self.getId());
			
			//DBからフォロー情報を取得し、フォローしていなければボタンを表示し、フォロー済みであれば非表示にする属性を付与する
			if (follow.isEmpty()) {
				Boolean followRegister = true;
				model.addAttribute("followRegister", followRegister);
			} else {
				Boolean followDelete = true;
				model.addAttribute("followDelete", followDelete);
			}
		}
		
		model.addAttribute("filePage", filePage);
		model.addAttribute("user", user);
		
		
		return "posts/otherPage";
	}
	
	//UUIDを使って生成したファイル名を返す
	public String generateNewFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String hashedFileName = UUID.randomUUID().toString() + extension;
        return hashedFileName;
    }

	//画像をAWSに保存し、urlを返す
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
