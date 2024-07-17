package com.cow.cow_instagram_practice.config;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.cow.cow_instagram_practice.jwt.CustomUserDetails;
import com.cow.cow_instagram_practice.member.entity.Member;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {

	@Override
	public SecurityContext createSecurityContext(WithAuthUser annotation) {
		String id = annotation.id();
		String password = "password";
		String role = annotation.role();

		Member member = Member.builder()
			.id(id)
			.password(password)
			.role(role)
			.build();
		CustomUserDetails authUser = CustomUserDetails.from(member);
		UsernamePasswordAuthenticationToken token =
			new UsernamePasswordAuthenticationToken(authUser, password, List.of(new SimpleGrantedAuthority(role)));
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(token);
		return context;
	}
}
