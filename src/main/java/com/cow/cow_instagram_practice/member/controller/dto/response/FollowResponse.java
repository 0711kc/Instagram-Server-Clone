package com.cow.cow_instagram_practice.member.controller.dto.response;

import com.cow.cow_instagram_practice.follow.entity.Follow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "Member Response : 회원 결과 DTO")
public class FollowResponse {
	private final String followerId;
	private final String followingId;

	public static FollowResponse from(Follow follow) {
		return FollowResponse.builder()
			.followerId(follow.getFollower().getId())
			.followingId(follow.getFollowing().getId())
			.build();
	}
}
