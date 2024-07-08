package com.cow.cow_instagram_practice.member.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cow.cow_instagram_practice.member.entity.Follow;
import com.cow.cow_instagram_practice.member.entity.Member;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FollowRepositoryTest {
	@Autowired
	FollowRepository followRepository;

	@BeforeEach
	void setUp() {
		Member follower = Member.builder().id("test123").build();
		Member following = Member.builder().id("0711kc").build();
		Follow createdFollow = Follow.of(follower, following);
		followRepository.save(createdFollow);
	}

	@Test
	@DisplayName("팔로우 등록하기")
	void saveFollow() {
		Member following = Member.builder().id("abcde").build();
		Member follower = Member.builder().id("qwert").build();
		Follow createdFollow = Follow.of(follower, following);
		Follow savedFollow = followRepository.save(createdFollow);

		Assertions.assertThat(savedFollow.getFollowing()).isEqualTo(following);
		Assertions.assertThat(savedFollow.getFollower()).isEqualTo(follower);
	}

	@Test
	@DisplayName("팔로워 조회하기")
	void getFollow() {
		List<Follow> followers = followRepository.findByFollowingId("0711kc");

		Assertions.assertThat(followers.size()).isEqualTo(1);
		Follow follow = followers.get(0);
		Assertions.assertThat(follow.getFollowing().getId()).isEqualTo("0711kc");
		Assertions.assertThat(follow.getFollower().getId()).isEqualTo("test123");
	}
}
