package com.cow.cow_instagram_practice.follow.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class FollowId implements Serializable {
	private String follower;
	private String following;

	public static FollowId of(String followerId, String followingId) {
		return FollowId.builder()
			.follower(followerId)
			.following(followingId)
			.build();
	}
}
