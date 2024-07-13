package com.cow.cow_instagram_practice.image.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cow.cow_instagram_practice.image.entity.PostImage;
import com.cow.cow_instagram_practice.image.entity.ProfileImage;
import com.cow.cow_instagram_practice.image.repository.PostImageRepository;
import com.cow.cow_instagram_practice.image.repository.ProfileImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
	private final AmazonS3Client s3Client;
	private final ProfileImageRepository profileImageRepository;
	private final PostImageRepository postImageRepository;

	@Value("${s3.bucket}")
	private String bucket;

	@Override
	public ProfileImage uploadProfileImage(MultipartFile image) throws IOException {
		String imageLink = upload(image);
		ProfileImage profileImage = ProfileImage.from(imageLink);
		return profileImageRepository.save(profileImage);
	}

	@Override
	public PostImage uploadPostImage(MultipartFile image) throws IOException {
		String imageLink = upload(image);
		PostImage postImage = PostImage.from(imageLink);
		return postImageRepository.save(postImage);
	}

	private String upload(MultipartFile image) throws IOException {
		String originalFileName = image.getOriginalFilename();
		String fileName = changeFileName(originalFileName);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(image.getContentType());
		metadata.setContentLength(image.getSize());

		s3Client.putObject(bucket, fileName, image.getInputStream(), metadata);
		return s3Client.getUrl(bucket, fileName).toString();
	}

	private String changeFileName(String originalFileName) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return originalFileName + "_" + LocalDateTime.now().format(formatter);
	}
}
