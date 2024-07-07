package com.cow.cow_instagram_practice.member.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cow.cow_instagram_practice.image.repository.ProfileImageRepository;
import com.cow.cow_instagram_practice.member.entity.Follow;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.image.entity.ProfileImage;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FollowRepositoryTest {
	@Autowired
	FollowRepository followRepository;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	ProfileImageRepository profileImageRepository;

	@BeforeEach
	void setUp() {
		String id = "test123";
		String password = "1234qwe!";
		String name = "Test";
		String nickname = "test123";
		String phone = "010-1234-5678";
		String email = "test@gmail.com";
		String imageLink = "https://mycowpracticebucket.s3.ap-northeast-2.amazonaws.com/anonymous.png";
		ProfileImage profileImage = getTestProfileImage(imageLink);
		Member member = getTestMember(id, password, name, nickname, phone, email, profileImage);

		profileImageRepository.save(profileImage);
		memberRepository.save(member);

		id = "0711kc";
		password = "1234qwe!";
		name = "Kim Chan";
		nickname = "kc123";
		phone = "010-9876-5432";
		email = "123qwe@gmail.com";
		imageLink = "https://mycowpracticebucket.s3.ap-northeast-2.amazonaws.com/anonymous.png";
		profileImage = getTestProfileImage(imageLink);
		member = getTestMember(id, password, name, nickname, phone, email, profileImage);

		profileImageRepository.save(profileImage);
		memberRepository.save(member);
	}

	@Test
	@DisplayName("팔로우 등록하기")
	void saveFollow() {
		Member follower = memberRepository.findById("test123").orElseThrow();
		Member following = memberRepository.findById("0711kc").orElseThrow();
		Follow createdFollow = Follow.of(follower, following);
		Follow savedFollow = followRepository.save(createdFollow);

		Assertions.assertThat(savedFollow.getFollowing()).isEqualTo(following);
		Assertions.assertThat(savedFollow.getFollower()).isEqualTo(follower);
	}

	@Test
	@DisplayName("팔로워 조회하기")
	void getFollow() {
		Member follower = memberRepository.findById("test123").orElseThrow();
		Member following = memberRepository.findById("0711kc").orElseThrow();
		Follow createdFollow = Follow.of(follower, following);
		followRepository.save(createdFollow);

		List<Follow> followers = followRepository.findByFollowingId("0711kc");

		Assertions.assertThat(followers.size()).isEqualTo(1);
		Follow follow = followers.get(0);
		Assertions.assertThat(follow.getFollowing()).isEqualTo(following);
		Assertions.assertThat(follow.getFollower()).isEqualTo(follower);
	}

	private Member getTestMember(String id, String password, String name, String nickname, String phone, String email,
		ProfileImage profileImage) {
		return Member.builder()
			.id(id)
			.password(password)
			.name(name)
			.nickname(nickname)
			.phone(phone)
			.email(email)
			.profileImage(profileImage)
			.build();
	}

	private ProfileImage getTestProfileImage(String imageLink) {
		return ProfileImage.from(imageLink);
	}
}
