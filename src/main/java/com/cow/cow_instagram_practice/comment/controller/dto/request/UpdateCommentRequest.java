package com.cow.cow_instagram_practice.comment.controller.dto.request;

import com.cow.cow_instagram_practice.comment.entity.Comment;

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
public class UpdateCommentRequest {
	@Size(max = 50)
	private String content;

	public static UpdateCommentRequest from(Comment comment) {
		return UpdateCommentRequest.builder()
			.content(comment.getContent())
			.build();
	}
}
