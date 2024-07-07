package com.cow.cow_instagram_practice.member.controller.dto.request;

import com.cow.cow_instagram_practice.member.entity.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateMemberRequest {
	@Size(min = 6, max = 12, message = "아이디는 6글자 이상 12글자 이하로 입력해주세요.")
	private String id;

	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$",
		message = "비밀번호는 8글자 이상 16글자 이하로 입력해주세요. "
			+ "영문자, 숫자, 특수문자가 최소 하나씩 들어가야 됩니다.")
	private String password;

	@Size(min = 2, max = 8, message = "사용자 이름은 2글자 이상 8글자 이하로 입력해주세요.")
	private String name;

	@Size(min = 2, max = 20, message = "사용자 이름은 2글자 이상 20글자 이하로 입력해주세요.")
	private String nickname;

	@Pattern(regexp = "^010[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
	private String phone;

	@Email
	private String email;

	private MemberRole role;

	public static UpdateMemberRequest from(Member member) {
		return UpdateMemberRequest.builder()
			.id(member.getId())
			.password(member.getPassword())
			.name(member.getName())
			.nickname(member.getNickname())
			.phone(member.getPhone())
			.email(member.getEmail())
			.role(MemberRole.findByPermission(member.getRole()))
			.build();
	}
}
