package com.cow.cow_instagram_practice.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
	@Id
	@Size(max = 12)
	private String id;

	@Size(max = 20)
	private String password;

	@Size(max = 8)
	private String name;

	@Size(max = 20)
	private String nickname;

	@Size(max = 14)
	private String phone;

	@Size(max = 50)
	private String email;

	@ManyToOne(fetch = FetchType.LAZY)
	private ProfileImage profileImage;

	private Short role;

	@Builder
	private Member(final String id, final String password, final String name, final String nickname,
		final String phone, final String email, final ProfileImage profileImage, final Short role) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.phone = phone;
		this.email = email;
		this.profileImage = profileImage;
		this.role = role;
	}

	public static Member of(final String id, final String password, final String name, final String nickname,
		final String phone, final String email, final ProfileImage profileImage, final Short role) {
		return Member.builder()
			.id(id)
			.password(password)
			.name(name)
			.nickname(nickname)
			.phone(phone)
			.email(email)
			.profileImage(profileImage)
			.role(role)
			.build();
	}
}
