package com.cow.cow_instagram_practice.follow.service;

import org.springframework.http.ResponseEntity;

import com.cow.cow_instagram_practice.member.controller.dto.response.FollowResponse;

public interface FollowService {
	ResponseEntity<FollowResponse> create(String followerId, String followingId);

	ResponseEntity<Void> delete(String followerId, String followingId);
}
