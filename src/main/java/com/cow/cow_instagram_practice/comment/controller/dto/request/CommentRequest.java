package com.cow.cow_instagram_practice.comment.controller.dto.request;

import java.time.LocalDateTime;

import com.cow.cow_instagram_practice.comment.entity.Comment;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.post.entity.Post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentRequest {
	@NotBlank(message = "댓글 내용을 입력해주세요.")
	@Size(max = 50, message = "내용은 50글자 이하로 입력해주세요.")
	private String content;

	public Comment toEntity(LocalDateTime date, Member member, Post post, Comment parent) {
		return Comment.of(content, date, member, post, parent);
	}
}
