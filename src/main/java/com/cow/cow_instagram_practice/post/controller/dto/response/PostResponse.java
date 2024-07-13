package com.cow.cow_instagram_practice.post.controller.dto.response;

import java.time.LocalDateTime;

import com.cow.cow_instagram_practice.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class PostResponse {
	private final Long id;
	private final String content;
	private final String memberName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private final LocalDateTime date;

	public static PostResponse from(final Post post) {
		return PostResponse.builder()
			.id(post.getId())
			.content(post.getContent())
			.memberName(post.getMember().getName())
			.date(post.getDate())
			.build();
	}
}
