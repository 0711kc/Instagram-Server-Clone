package com.cow.cow_instagram_practice.member.controller.dto.response;

import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRole;
import com.cow.cow_instagram_practice.member.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
	private final String id;
	private final String name;
	private final String nickname;
	private final String phone;
	private final String email;
	private final String image;
	private final String role;

	public static MemberResponse from(Member member) {
		return MemberResponse.builder()
			.id(member.getId())
			.name(member.getName())
			.nickname(member.getNickname())
			.phone(member.getPhone())
			.email(member.getEmail())
			.image(member.getProfileImage().getImageLink())
			.role(MemberRole.findByPermission(member.getRole()).getPermission())
			.build();
	}
}
