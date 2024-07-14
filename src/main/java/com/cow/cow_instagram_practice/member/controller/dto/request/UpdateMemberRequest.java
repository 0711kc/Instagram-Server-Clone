package com.cow.cow_instagram_practice.member.controller.dto.request;

import com.cow.cow_instagram_practice.member.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Schema(title = "Update Member Request : 회원 정보 수정 요청 DTO")
public class UpdateMemberRequest {
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$",
		message = "비밀번호는 8글자 이상 16글자 이하로 입력해주세요. "
			+ "영문자, 숫자, 특수문자가 최소 하나씩 들어가야 됩니다.")
	@Schema(description = "비밀번호")
	private String password;

	@Size(min = 2, max = 8, message = "사용자 이름은 2글자 이상 8글자 이하로 입력해주세요.")
	@Schema(description = "이름")
	private String name;

	@Size(min = 2, max = 20, message = "사용자 이름은 2글자 이상 20글자 이하로 입력해주세요.")
	@Schema(description = "별명")
	private String nickname;

	@Pattern(regexp = "^010[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
	@Schema(description = "전화번호")
	private String phone;

	@Email
	@Schema(description = "이메일")
	private String email;

	public static UpdateMemberRequest from(Member member) {
		return UpdateMemberRequest.builder()
			.password(member.getPassword())
			.name(member.getName())
			.nickname(member.getNickname())
			.phone(member.getPhone())
			.email(member.getEmail())
			.build();
	}
}
