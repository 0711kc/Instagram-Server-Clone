package com.cow.cow_instagram_practice.follow.service;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cow.cow_instagram_practice.follow.entity.Follow;
import com.cow.cow_instagram_practice.follow.entity.FollowId;
import com.cow.cow_instagram_practice.follow.repository.FollowRepository;
import com.cow.cow_instagram_practice.member.controller.dto.response.FollowResponse;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {
	@InjectMocks
	FollowServiceImpl followService;

	@Mock
	FollowRepository followRepository;

	@Mock
	MemberRepository memberRepository;

	@Test
	@DisplayName("팔로우 등록하기")
	public void create() {
		String followerId = "0711lc";
		String followingId = "test1234";
		Member follower = Member.builder().id(followerId).build();
		Member following = Member.builder().id(followingId).build();
		Follow follow = Follow.builder().follower(follower).following(following).build();
		given(followRepository.existsById(any(FollowId.class))).willReturn(false);
		given(followRepository.save(any(Follow.class))).willReturn(follow);
		given(memberRepository.findByIdJoinFetch(followerId)).willReturn(Optional.ofNullable(follower));
		given(memberRepository.findByIdJoinFetch(followingId)).willReturn(Optional.ofNullable(following));

		ResponseEntity<FollowResponse> responseEntity = followService.create(followerId, followingId);
		FollowResponse followResponse = responseEntity.getBody();

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(followResponse).isNotNull();
		verify(followRepository).existsById(any(FollowId.class));
		verify(followRepository).save(any(Follow.class));
		verify(memberRepository).findByIdJoinFetch(followerId);
		verify(memberRepository).findByIdJoinFetch(followingId);
	}

	@Test
	@DisplayName("팔로우 삭제하기")
	public void delete() {
		String followerId = "0711lc";
		String followingId = "test1234";
		given(followRepository.existsById(any(FollowId.class))).willReturn(true);

		ResponseEntity<Void> responseEntity = followService.delete(followerId, followingId);

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(followRepository).existsById(any(FollowId.class));
	}
}
