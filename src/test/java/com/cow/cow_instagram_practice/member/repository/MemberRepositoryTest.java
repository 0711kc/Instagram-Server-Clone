package com.cow.cow_instagram_practice.member.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.member.entity.ProfileImage;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	ProfileImageRepository profileImageRepository;

	@Autowired
	FollowRepository followRepository;

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
	}

	@Test
	@DisplayName("회원 저장하기")
	void saveMember() {
		String id = "0711kc";
		String password = "1234qwe!";
		String name = "Kim Chan";
		String nickname = "kc123";
		String phone = "010-9876-5432";
		String email = "123qwe@gmail.com";
		String imageLink = "https://mycowpracticebucket.s3.ap-northeast-2.amazonaws.com/anonymous.png";
		ProfileImage profileImage = getTestProfileImage(imageLink);
		Member member = getTestMember(id, password, name, nickname, phone, email, profileImage);

		profileImageRepository.save(profileImage);
		Member savedMember = memberRepository.save(member);

		Assertions.assertThat(savedMember.getId()).isEqualTo(id);
		Assertions.assertThat(savedMember.getPassword()).isEqualTo(password);
		Assertions.assertThat(savedMember.getName()).isEqualTo(name);
		Assertions.assertThat(savedMember.getNickname()).isEqualTo(nickname);
		Assertions.assertThat(savedMember.getId()).isEqualTo(id);
		Assertions.assertThat(savedMember.getPhone()).isEqualTo(phone);
		Assertions.assertThat(savedMember.getEmail()).isEqualTo(email);
		Assertions.assertThat(savedMember.getProfileImage().getImageLink()).isEqualTo(imageLink);
		Assertions.assertThat(savedMember.getPosts().size()).isEqualTo(0);
		Assertions.assertThat(savedMember.getComments().size()).isEqualTo(0);
		Assertions.assertThat(savedMember.getFollowers().size()).isEqualTo(0);
		Assertions.assertThat(savedMember.getFollowings().size()).isEqualTo(0);
	}

	@Test
	@DisplayName("회원 불러오기")
	void getMember() {
		Optional<Member> findMember = memberRepository.findById("test123");

		Assertions.assertThat(findMember).isPresent();
		Member member = findMember.get();
		Assertions.assertThat(member.getId()).isNotNull();
		Assertions.assertThat(member.getPassword()).isNotNull();
		Assertions.assertThat(member.getName()).isNotNull();
		Assertions.assertThat(member.getNickname()).isNotNull();
		Assertions.assertThat(member.getPhone()).isNotNull();
		Assertions.assertThat(member.getEmail()).isNotNull();
		Assertions.assertThat(member.getProfileImage()).isNotNull();
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
