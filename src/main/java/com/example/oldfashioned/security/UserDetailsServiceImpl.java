package com.example.oldfashioned.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.oldfashioned.entity.User;
import com.example.oldfashioned.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	// メールアドレスを引数として受け取り対応するユーザー情報と権限情報を返す
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			User user = userRepository.findByEmail(email);
			String userRoleName = user.getRole().getName();
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(userRoleName));
			return new UserDetailsImpl(user, authorities);
		} catch (Exception e) {
			throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
		}
	}
}
