package com.cow.cow_instagram_practice.member.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cow.cow_instagram_practice.image.repository.ProfileImageRepository;
import com.cow.cow_instagram_practice.image.entity.ProfileImage;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProfileImageRepositoryTest {
	@Autowired
	ProfileImageRepository profileImageRepository;

	@Test
	@DisplayName("이미지 저장하기")
	void saveProfileImage() {
		String imageLink = "https://mycowpracticebucket.s3.ap-northeast-2.amazonaws.com/profile.png";
		ProfileImage profileImage = ProfileImage.from(imageLink);

		ProfileImage savedProfileImage = profileImageRepository.save(profileImage);

		Assertions.assertThat(savedProfileImage.getId()).isEqualTo(2L);
		Assertions.assertThat(savedProfileImage.getImageLink()).isEqualTo(imageLink);
	}
	@Test
	@DisplayName("이미지 불러오기")
	void getProfileImage() {
		String imageLink = "https://mycowpracticebucket.s3.ap-northeast-2.amazonaws.com/anonymous.png";

		Optional<ProfileImage> profileImage = profileImageRepository.findById(1L);

		Assertions.assertThat(profileImage).isPresent();
		Assertions.assertThat(profileImage.get().getImageLink()).isEqualTo(imageLink);
	}
}
