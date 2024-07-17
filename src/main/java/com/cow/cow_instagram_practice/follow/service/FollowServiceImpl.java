package com.cow.cow_instagram_practice.follow.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cow.cow_instagram_practice.follow.entity.Follow;
import com.cow.cow_instagram_practice.follow.entity.FollowId;
import com.cow.cow_instagram_practice.follow.repository.FollowRepository;
import com.cow.cow_instagram_practice.member.controller.dto.response.FollowResponse;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.member.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowServiceImpl implements FollowService {
	private final FollowRepository followRepository;
	private final MemberRepository memberRepository;

	@Override
	public ResponseEntity<FollowResponse> create(String followerId, String followingId) {
		if (followerId.equals(followingId)) {
			throw new IllegalArgumentException("[Error] 자기 자신을 팔로우할 수 없습니다.");
		}
		boolean isExistFollow = followRepository.existsById(FollowId.of(followerId, followingId));
		if (isExistFollow) {
			throw new IllegalArgumentException("[Error] 이미 팔로우된 상태입니다.");
		}
		Member follower = memberRepository.findByIdJoinFetch(followerId)
			.orElseThrow(() -> new EntityNotFoundException("[Error] 사용자를 찾을 수 없습니다."));
		Member following = memberRepository.findByIdJoinFetch(followingId)
			.orElseThrow(() -> new EntityNotFoundException("[Error] 사용자를 찾을 수 없습니다."));
		Follow follow = Follow.of(follower, following);
		Follow savedFollow = followRepository.save(follow);
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(FollowResponse.from(savedFollow));
	}

	@Override
	public ResponseEntity<Void> delete(String followerId, String followingId) {
		boolean isExistFollow = followRepository.existsById(FollowId.of(followerId, followingId));
		if (!isExistFollow) {
			throw new IllegalArgumentException("[Error] 팔로우 상태가 아닙니다.");
		}
		followRepository.deleteById(FollowId.of(followerId, followingId));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
