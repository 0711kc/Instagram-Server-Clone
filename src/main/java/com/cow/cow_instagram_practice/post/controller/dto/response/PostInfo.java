package com.cow.cow_instagram_practice.post.controller.dto.response;

import com.cow.cow_instagram_practice.post.entity.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class PostInfo {
	private final Long id;
	private final String imageLink;

	public static PostInfo from(Post post) {
		return PostInfo.builder()
			.id(post.getId())
			.imageLink(post.getPostImage().getImageLink())
			.build();
	}
}
