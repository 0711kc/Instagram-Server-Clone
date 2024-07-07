package com.cow.cow_instagram_practice.member.controller.dto.response;

import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRole;
import com.cow.cow_instagram_practice.member.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "Member Response : 회원 결과 DTO")
public class MemberResponse {
	@Schema(description = "아이디")
	private final String id;

	@Schema(description = "이름")
	private final String name;

	@Schema(description = "별명")
	private final String nickname;

	@Schema(description = "전화번호")
	private final String phone;

	@Schema(description = "이메일")
	private final String email;

	@Schema(description = "프로필 사진 링크")
	private final String image;

	@Schema(description = "권한")
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
