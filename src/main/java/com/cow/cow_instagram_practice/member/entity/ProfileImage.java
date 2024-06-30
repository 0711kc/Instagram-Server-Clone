package com.cow.cow_instagram_practice.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String imageLink;

	@Builder
	private ProfileImage(final Long id, final String imageLink) {
		this.id = id;
		this.imageLink = imageLink;
	}

	public static ProfileImage from(String imageLink) {
		return ProfileImage.builder()
			.imageLink(imageLink)
			.build();
	}
}
