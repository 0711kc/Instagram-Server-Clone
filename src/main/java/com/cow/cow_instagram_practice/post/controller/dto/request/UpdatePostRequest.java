package com.cow.cow_instagram_practice.post.controller.dto.request;

import com.cow.cow_instagram_practice.post.entity.Post;

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
public class UpdatePostRequest {
	@Size(max = 100)
	private String content;

	public static UpdatePostRequest from(Post post) {
		return UpdatePostRequest.builder()
			.content(post.getContent())
			.build();
	}
}
