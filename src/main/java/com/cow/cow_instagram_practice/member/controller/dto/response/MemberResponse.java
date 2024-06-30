package com.cow.cow_instagram_practice.member.controller.dto.response;

import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRole;
import com.cow.cow_instagram_practice.member.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {
	private final String id;

	private final String name;

	private final String nickname;

	private final String phone;

	private final String email;

	private final String image;

	private final String role;

	@Builder
	private MemberResponse(final String id, final String name, final String nickname,
		final String phone, final String email, final String image, final String role) {
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.phone = phone;
		this.email = email;
		this.image = image;
		this.role = role;
	}

	public static MemberResponse from(Member member) {
		return MemberResponse.builder()
			.id(member.getId())
			.name(member.getName())
			.nickname(member.getNickname())
			.phone(member.getPhone())
			.email(member.getEmail())
			.image(member.getProfileImage().getImageLink())
			.role(MemberRole.getDescription(member.getRole()))
			.build();
	}
}
