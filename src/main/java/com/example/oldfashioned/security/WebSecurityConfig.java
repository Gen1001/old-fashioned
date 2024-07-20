package com.example.oldfashioned.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	
	// HttpSecurityオブジェクトを使用してHTTPリクエストに対するセキュリティ設定を行う
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		
			// 特定のURLパターンに対するアクセス許可を設定する
			.authorizeHttpRequests((requests) -> requests
					.requestMatchers("/css/**", "/js/**", "/storage/**", "/**", "/signup/**", "/auth/**", "/passwordReset/**", "/mailsend/**", "/login/**", "/search/**", "/store/**", "/user/**", "/posts/category/1").permitAll()
					.anyRequest().authenticated()
			)
			
			// フォームベースのログイン設定を行う
			.formLogin((form) -> form
					.loginPage("/login")
					.loginProcessingUrl("/login")
					.defaultSuccessUrl("/", true)
					.failureUrl("/login?error")
					.permitAll()
			)
			
			// ログアウト設定を行う
			.logout((logout) -> logout
					.logoutSuccessUrl("/login?loggedOut")
					.permitAll()
			);
		
		return http.build();
	}
	
	//パスワードをハッシュ化する
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
