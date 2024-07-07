package com.cow.cow_instagram_practice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cow.cow_instagram_practice.jwt.JWTUtil;
import com.cow.cow_instagram_practice.jwt.LoginFilter;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
public class SecurityConfig implements WebMvcConfigurer {
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JWTUtil jwtUtil;

	private final String[] WHITE_LIST_POST = {
		"/member/new",
		"/login"
	};


	private final String[] WHITE_LIST_GET = {
		"/member/{memberId}"
	};

	private final String[] WHITE_LIST_SWAGGER = {
		"/swagger-ui/**",
		"/v3/api-docs/**"
	};

	// TODO DELETE와 GET은 나중에 지워야됨
	private final String[] WHITE_LIST_DELETE = {
		"/member/{memberId}"
	};

	private final String[] WHITE_LIST_PATCH = {
		"/member/{memberId}"
	};


	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable);

		http
			.formLogin(AbstractHttpConfigurer::disable);

		http
			.httpBasic(AbstractHttpConfigurer::disable);

		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers(HttpMethod.POST, WHITE_LIST_POST).permitAll()
				.requestMatchers(HttpMethod.GET, WHITE_LIST_GET).permitAll()
				.requestMatchers(HttpMethod.DELETE, WHITE_LIST_DELETE).permitAll()
				.requestMatchers(HttpMethod.PATCH, WHITE_LIST_PATCH).permitAll()
				.requestMatchers(WHITE_LIST_SWAGGER).permitAll()
				.anyRequest().authenticated());

		http
			.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
				UsernamePasswordAuthenticationFilter.class);

		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
