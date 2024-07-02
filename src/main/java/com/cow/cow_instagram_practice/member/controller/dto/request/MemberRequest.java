package com.cow.cow_instagram_practice.member.controller.dto.request;

import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.member.entity.ProfileImage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberRequest {
	@NotBlank(message = "아이디를 입력해주세요.")
	@Size(min = 6, max = 12, message = "아이디는 6글자 이상 12글자 이하로 입력해주세요.")
	private String id;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$",
		message = "비밀번호는 8글자 이상 16글자 이하로 입력해주세요. "
			+ "영문자, 숫자, 특수문자가 최소 하나씩 들어가야 됩니다.")
	private String password;

	@NotBlank(message = "이름을 입력해주세요.")
	@Size(min = 2, max = 8, message = "사용자 이름은 2글자 이상 8글자 이하로 입력해주세요.")
	private String name;

	@NotBlank(message = "별명을 입력해주세요.")
	@Size(min = 2, max = 20, message = "사용자 이름은 2글자 이상 20글자 이하로 입력해주세요.")
	private String nickname;

	@NotBlank(message = "핸드폰 번호를 입력해주세요.")
	@Pattern(regexp = "^010[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
	private String phone;

	@NotNull(message = "이메일을 입력해주세요.")
	@Email
	private String email;

	@NotNull(message = "역할을 입력해주세요.")
	private MemberRole role;

	public Member toEntity(final ProfileImage profileImage) {
		return Member.of(id, password, name, nickname, phone, email, profileImage, role.getIndex());
	}
}
