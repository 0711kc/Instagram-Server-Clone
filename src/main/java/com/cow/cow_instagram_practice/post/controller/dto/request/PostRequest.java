package com.cow.cow_instagram_practice.post.controller.dto.request;

import java.time.LocalDateTime;

import com.cow.cow_instagram_practice.image.entity.PostImage;
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
public class PostRequest {
	@NotBlank(message = "게시글 내용을 입력해주세요.")
	@Size(max = 100, message = "내용은 100글자 이하로 입력해주세요.")
	private String content;

	public Post toEntity(LocalDateTime localDateTime, PostImage postImage, Member member) {
		return Post.of(content, localDateTime, postImage, member);
	}
}
