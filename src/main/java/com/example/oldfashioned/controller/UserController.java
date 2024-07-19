package com.example.oldfashioned.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.form.UserEditForm;
import com.example.oldfashioned.repository.UserRepository;
import com.example.oldfashioned.security.UserDetailsImpl;
import com.example.oldfashioned.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	private final UserRepository userRepository;
	private final UserService userService;
	
	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}
	
	//自分の会員情報を表示する
	@GetMapping(" ")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		
		model.addAttribute("user", user);
		
		return "user/index";
	}
	
	//会員情報の編集ページに遷移する
	@GetMapping("/edit")
	public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		String userPhoto = user.getUserPhoto();
		UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getEmail(), user.getPostalCode(), user.getAddress(), user.getNickname(), user.getPhoneNumber(), user.getIntroduction(), null);
		
		model.addAttribute("profile", user.getUserPhoto());
		model.addAttribute("userPhoto", userPhoto);
		model.addAttribute("userEditForm", userEditForm);
		
		return "user/edit";
	}
	
	//入力された会員情報をDBに登録する
	@PostMapping("/update")
    public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		
        // メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (userService.isEmailChanged(userEditForm) && userService.isEmailRegisterd(userEditForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }
        
        // userEditFormで設定したエラーが発生していないか確認
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        
        //userEditFormに入力された会員情報の変更をDBに反映する
        userService.update(userEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
        
        return "redirect:/user";
    }   
}
