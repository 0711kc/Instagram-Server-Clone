package com.cow.cow_instagram_practice.comment.controller.dto.response;

import java.time.LocalDateTime;

import com.cow.cow_instagram_practice.comment.entity.Comment;
import com.cow.cow_instagram_practice.post.controller.dto.response.PostInfo;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CommentResponse {
	private final Long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private final LocalDateTime date;
	private final PostInfo postInfo;
	private final String memberId;
	private final String memberName;
	private final Long parentId;
	private final String content;

	public static CommentResponse from(Comment comment) {
		Long parentId = null;
		if (comment.isReply()) {
			parentId = comment.getParent().getId();
		}
		return CommentResponse.builder()
			.id(comment.getId())
			.date(comment.getDate())
			.content(comment.getContent())
			.postInfo(PostInfo.from(comment.getPost()))
			.memberId(comment.getMember().getId())
			.memberName(comment.getMember().getName())
			.parentId(parentId)
			.build();
	}
}
