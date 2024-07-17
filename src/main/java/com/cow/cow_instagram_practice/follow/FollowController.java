package com.cow.cow_instagram_practice.follow;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cow.cow_instagram_practice.follow.service.FollowService;
import com.cow.cow_instagram_practice.member.controller.dto.response.FollowResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
	private final FollowService followService;

	@PostMapping("/{followingId}")
	public ResponseEntity<FollowResponse> follow(@PathVariable final String followingId, Principal principal) {
		return followService.create(principal.getName(), followingId);
	}

	@PostMapping("/unfollow/{followingId}")
	public ResponseEntity<Void> unfollow(@PathVariable final String followingId, Principal principal) {
		return followService.delete(principal.getName(), followingId);
	}
}
