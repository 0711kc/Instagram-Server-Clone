package com.cow.cow_instagram_practice.image.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.net.URL;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.cow.cow_instagram_practice.image.entity.ProfileImage;
import com.cow.cow_instagram_practice.image.repository.ProfileImageRepository;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
	@InjectMocks
	ImageServiceImpl imageService;

	@Mock
	AmazonS3Client s3Client;

	@Mock
	ProfileImageRepository profileImageRepository;

	@Test
	@DisplayName("이미지 업로드")
	public void uploadImage() throws IOException {
		MockMultipartFile imageFile = new MockMultipartFile(
			"image",
			"profile.png",
			MediaType.IMAGE_PNG_VALUE,
			"profile".getBytes()
		);
		String imageLink = "https://mycowpracticebucket.s3.ap-northeast-2.amazonaws.com/profile.png";
		ProfileImage profileImage = ProfileImage.builder().id(2L).imageLink(imageLink).build();
		given(s3Client.getUrl(any(), any())).willReturn(new URL(imageLink));
		given(profileImageRepository.save(any()))
			.willReturn(profileImage);

		ProfileImage savedProfileImage = imageService.uploadProfileImage(imageFile);

		Assertions.assertThat(savedProfileImage.getImageLink()).isEqualTo(imageLink);
	}
}
