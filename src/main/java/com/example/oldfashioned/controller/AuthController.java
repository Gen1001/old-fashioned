package com.example.oldfashioned.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.oldfashioned.entity.PasswordToken;
import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.entity.VerificationToken;
import com.example.oldfashioned.event.ForgetEventPublisher;
import com.example.oldfashioned.event.SignupEventPublisher;
import com.example.oldfashioned.form.PasswordResetForm;
import com.example.oldfashioned.form.SignupForm;
import com.example.oldfashioned.repository.UserRepository;
import com.example.oldfashioned.service.PasswordTokenService;
import com.example.oldfashioned.service.UserService;
import com.example.oldfashioned.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
	private final UserService userService;
	private final SignupEventPublisher signupEventPublisher;
	private final VerificationTokenService verificationTokenService;
	private final UserRepository userRepository;
	private final ForgetEventPublisher forgetEventPublisher;
	private final PasswordTokenService passwordTokenService;
	
	public AuthController(UserService userService, SignupEventPublisher signupEventPublisher, VerificationTokenService verificationTokenService, UserRepository userRepository, ForgetEventPublisher forgetEventPublisher, PasswordTokenService passwordTokenService) {
		this.userService = userService;
		this.signupEventPublisher = signupEventPublisher;
		this.verificationTokenService = verificationTokenService;
		this.userRepository = userRepository;
		this.forgetEventPublisher = forgetEventPublisher;
		this.passwordTokenService = passwordTokenService;
		
	}
	
	//ログイン画面に遷移
	@GetMapping("/login")
	public String login() {
		return "auth/login";
	}
	
	//登録画面に遷移
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("signupForm", new SignupForm());
		return "auth/signup";
	}
	
	//SignupFormに入力された内容をDBに登録
	@PostMapping("/signup")
	public String register(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest, MultipartFile imageFile) {
		
		//入力されたメールアドレスが既に登録済みか確認
		if (userService.isEmailRegisterd(signupForm.getEmail())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
			bindingResult.addError(fieldError);
		}
		
		//入力された二つのパスワードが一致するか確認
		if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
			bindingResult.addError(fieldError);
		}
		
		//SignupFormで設定したエラーを発生していないか確認
		if (bindingResult.hasErrors()) {
			return "auth/signup";
		}
		
		//会員情報をDBに登録する
		User createdUser = userService.create(signupForm);
		String requestUrl = new String(httpServletRequest.getRequestURL());
		
		//認証メールを送信
		signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
		redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに送信した認証メールに記載されているリンクをクリックし、会員登録を完了してください。");
		
		return "redirect:/login";
	}
	
	//送信したメールからログインページに遷移
	@GetMapping("/signup/verify")
	public String verify(@RequestParam(name = "token") String token, Model model, RedirectAttributes redirectAttributes) {
		VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
		
		//トークンが無効でないか確認
		if (verificationToken != null) {
			User user = verificationToken.getUser();
			userService.enableUser(user);
			String successMessage = "会員登録が完了しました。ご入力いただいた情報をもとにログインしてください。";
			
			redirectAttributes.addFlashAttribute("successMessage", successMessage);
		} else {
			String errorMessage = "トークンが無効です。";
			
			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
		}
		
		return "redirect:/login";
	}
	
	//パスワード再設定フォームを送信するメールアドレスを入力するページに遷移
	@GetMapping("/forget")
	public String forget() {
		return "auth/mailsend";
	}
	
	//入力されたメールアドレスにパスワード再設定メールを送信
	@PostMapping("/reset")
	public String reset(Model model, HttpServletRequest httpServletRequest, @RequestParam("email") String email, RedirectAttributes redirectAttributes) {
		User user = userRepository.findByEmail(email);
		
		//メールアドレスがDBに登録済みかどうか確認
		if (user == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "ご入力いただいたメールアドレスが存在しません");
			return "redirect:/forget";
		}
		
		String requestUrl = new String(httpServletRequest.getRequestURL());
		
		//パスワード再設定メールを送信
		forgetEventPublisher.publishForgetEvent(user, requestUrl);
		
		redirectAttributes.addFlashAttribute("successMessage", "ご登録済みのメールアドレスにパスワード再設定メールを送信しました。メールに記載されているリンクをクリックし、再設定を完了してください。");
		
		return "redirect:/login";
	}
	
	//パスワード再設定フォームに遷移
	@GetMapping("/reset/verify")
	public String verify(Model model, @RequestParam(name = "token") String token, RedirectAttributes redirectAttributes) {
		PasswordToken passwordToken = passwordTokenService.getPasswordToken(token);
		
		//トークンが無効でないか確認
		if (passwordToken != null) {
			User user = passwordToken.getUser();
			PasswordResetForm passwordResetForm = new PasswordResetForm(user.getId(), user.getPassword(), user.getPassword());
			
			model.addAttribute("passwordResetForm", passwordResetForm);
			
			return "auth/password";
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "トークンが無効です。");
			
			return "redirect:/auth/mailsend";
		}
	}
	
	//入力されたパスワードを再設定する
	@PostMapping("/passwordReset")
	public String change(RedirectAttributes redirectAttributes, @ModelAttribute @Validated PasswordResetForm passwordResetForm) {
		
		//入力された二つのパスワードが一致するか確認
		if (!userService.isSamePassword(passwordResetForm.getPassword(), passwordResetForm.getPasswordConfirmation())) {
			redirectAttributes.addFlashAttribute("errorMessage", "パスワードが一致しません。");
			
			return "redirect:/auth/password";
		}
		
		//DBに登録された会員情報のパスワードを変更する
		userService.changePassword(passwordResetForm);
		
		redirectAttributes.addFlashAttribute("successMessage", "パスワードを再設定しました。");
		
		return "redirect:/login";
	}
}
